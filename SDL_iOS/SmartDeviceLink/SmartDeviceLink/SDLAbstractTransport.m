//  SDLAbstractTransport.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAbstractTransport.h>

#import <SmartDeviceLink/SDLTransportListener.h>

@interface SDLAbstractTransport ()

+ (id) createMutableArrayUsingWeakReferences;

@end

@implementation SDLAbstractTransport

@synthesize endpointName;
@synthesize endpointParam;
@synthesize guid;
@synthesize isSecure;
@synthesize isConnected;

@synthesize mtuSize;

- (id) initWithEndpoint:(NSString*) endpoint endpointParam:(NSString*) param {
    if (self = [super init]) {
        endpointName = [endpoint retain];
        endpointParam = [param retain];
        
        NSMutableString* tempGuid = [[NSMutableString alloc] initWithFormat: @"%x", arc4random()];
        for (int i=0; i < 3; i++) {
            [tempGuid appendFormat:@"%x", arc4random()];
        }
        guid = tempGuid;
        
        transportListeners = [SDLAbstractTransport createMutableArrayUsingWeakReferences];
        
        accumBufLength = 4096;
        accumBuf = (UInt8*)malloc(accumBufLength * sizeof(UInt8));
        accumBufWritePos = 0;
        accumBufLock = [[NSLock alloc] init];
        
        return self;
    }
    return self;
}

- (void) dealloc {
    [endpointName release];
    [endpointParam release];
	[transportListeners release]; 
	[guid release];
	[accumBufLock release];
	free(accumBuf);
	
	[super dealloc];
}

// This follows the create rule, meaning that the caller of this method owns the returned instance
+ (id) createMutableArrayUsingWeakReferences {
	CFArrayCallBacks callbacks = {0, NULL, NULL, CFCopyDescription, CFEqual};
	return (id)(CFArrayCreateMutable(0, 0, &callbacks));
}

- (void) addTransportListener:(NSObject<SDLTransportListener>*)listener {

	@synchronized (transportListeners) {
		[transportListeners addObject:listener]; // If this array isn't released, it's objects will not be released either
	}
}

- (void) removeTransportListener:(NSObject<SDLTransportListener>*)listener {
	@synchronized (transportListeners) {
		[transportListeners removeObject:listener];
	}
}

- (void) notifyTransportConnected {
	self.isConnected = YES;
	
	NSArray *localListeners = nil;
	@synchronized (transportListeners) {
		localListeners = [transportListeners copy];
	}
	
	for (NSObject<SDLTransportListener>* listener in localListeners) {
		[listener onTransportConnected];
	}
	[localListeners release];
}

- (void) notifyTransportDisconnected {
    NSLog(@"%@", NSStringFromSelector(_cmd));
	if (self.isConnected) {
		self.isConnected = NO;
		
		NSArray *localListeners = nil;
		@synchronized (transportListeners) {
			localListeners = [transportListeners copy];
		}
		
		for (NSObject<SDLTransportListener>* listener in localListeners) {
			[listener onTransportDisconnected];
		}
		[localListeners release];
	}
}

- (void) handleBytesReceivedFromTransport:(Byte*) receivedBytes length:(int) receivedBytesLength {
	
	NSArray *localListeners = nil;
	@synchronized (transportListeners) {
		localListeners = [transportListeners copy];
	}
	
	for (NSObject<SDLTransportListener>* listener in localListeners) {
		[listener onBytesReceived:receivedBytes length:receivedBytesLength];
	}
	[localListeners release];
	
} // end-message

- (bool) sendBytes:(NSData*) msgBytes {
	[self doesNotRecognizeSelector:_cmd];
	return NO;
}

- (void) disconnect {
	[self doesNotRecognizeSelector:_cmd];
}

- (bool) connect {
	[self doesNotRecognizeSelector:_cmd];
	return NO;
}


@end
