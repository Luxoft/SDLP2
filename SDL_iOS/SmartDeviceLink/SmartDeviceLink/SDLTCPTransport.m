//  SDLTCPTransport.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLTCPTransport.h>
#import <SmartDeviceLink/SDLDebugTool.h>
#import <errno.h>
#import <signal.h>
#import <stdio.h>
#import <unistd.h>
#import <sys/types.h>
#import <sys/socket.h>
#import <sys/wait.h>
#import <netinet/in.h>
#import <netdb.h>

int call_socket(const char* hostname, const char* port) { 
    
    int status, sock;
    struct addrinfo hints;
    struct addrinfo* servinfo;
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    
    //no host name?, no problem, get local host
    if (hostname == nil){
        char localhost[128];
        gethostname(localhost, sizeof localhost);
        hostname = (const char*) &localhost;
    }
    
    //getaddrinfo setup
    if ((status = getaddrinfo(hostname, port, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo error: %s\n", gai_strerror(status));
        return(-1);
    }
    
    //get socket
    if ((sock = socket(servinfo->ai_family, servinfo->ai_socktype, servinfo->ai_protocol)) < 0)
		return(-1);
    
    //connect
    if (connect(sock, servinfo->ai_addr, servinfo->ai_addrlen) < 0) {
		close(sock);
		return(-1);
	}
    
    freeaddrinfo(servinfo); // free the linked-list
    return(sock);
} 

@implementation SDLTCPTransport

static void TCPCallback(CFSocketRef socket, CFSocketCallBackType type, CFDataRef address, const void *data, void *info) {
	if (kCFSocketConnectCallBack == type) {
		SDLTCPTransport *transport = (SDLTCPTransport *)info;
		SInt32 errorNumber = 0;
		if (data) {
			SInt32 *errorNumberPtr = (SInt32 *)data;
			errorNumber = *errorNumberPtr;
		}
		[transport notifyTransportConnected];
	} else if (kCFSocketDataCallBack == type) {
		SDLTCPTransport *transport = (SDLTCPTransport *)info;
        if ([(NSData*)data length] == 0)
        {
            [transport notifyTransportDisconnected];
        }
        else
        {
            [transport handleBytesReceivedFromTransport:(UInt8 *)CFDataGetBytePtr((CFDataRef)data) length:CFDataGetLength((CFDataRef)data)];
        }
    } else {
		[SDLDebugTool logInfo:@"unhandled TCPCallback: %d", type];
	}
}

- (bool) connect {
    
    [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"consoleLog" object:@"TCP Init"]];
    [SDLDebugTool logInfo:@"TCP Init"];
    
    int sock_fd = call_socket([endpointName UTF8String], [endpointParam UTF8String]);
	if (sock_fd < 0) {
        
        [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"consoleLog" object:@"Server Not Ready, Connection Failed"]];
		[SDLDebugTool logInfo:@"Server Not Ready, Connection Failed"];
        
		return NO;
	}
	
	CFSocketContext socketCtxt = {0, self, NULL, NULL, NULL};
	socket = CFSocketCreateWithNative(kCFAllocatorDefault, sock_fd, kCFSocketDataCallBack|kCFSocketConnectCallBack , (CFSocketCallBack) &TCPCallback, &socketCtxt);
	CFRunLoopSourceRef source = CFSocketCreateRunLoopSource(kCFAllocatorDefault, socket, 0);
	CFRunLoopRef loop = CFRunLoopGetCurrent();
	CFRunLoopAddSource(loop, source, kCFRunLoopDefaultMode);
	CFRelease(source);
	
	return sock_fd >= 0;
}

-(NSString*) getHexString:(NSData*) data {
	NSMutableString* ret = [NSMutableString stringWithCapacity:(data.length * 2)];
	for (int i = 0; i < data.length; i++) {
		[ret appendFormat:@"%02X", ((Byte*)data.bytes)[i]];
	}
	return ret;
}

- (bool) sendBytes:(NSData*) msgBytes {
//	NSString* byteStr = [self getHexString:msgBytes];
//	[SDLDebugTool logInfo:@"Sending %i bytes: %@", msgBytes.length, byteStr];
	
	CFSocketError e = CFSocketSendData(socket, NULL, (CFDataRef)msgBytes, 10000);
	return e==0;
}

- (void) dealloc {
	if (socket != nil) {
		CFSocketInvalidate(socket);
		CFRelease(socket);
	}
	
	[super dealloc];
}

@end
