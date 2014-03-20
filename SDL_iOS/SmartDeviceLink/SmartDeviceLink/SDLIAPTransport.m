//  SDLIAPTransport.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SDLIAPTransport.h>
#import <SmartDeviceLink/SDLDebugTool.h>
#import <SmartDeviceLink/SDLSiphonServer.h>

#define SYNC_PROTOCOL_STRING @"com.ford.sync.prot0"

@interface SDLIAPTransport ()

-(void) accessoryConnected:(NSNotification*) connectNotification;
-(void) accessoryDisconnected:(NSNotification*) connectNotification;
-(NSString*) stringForEventCode:(NSStreamEvent) eventCode;

@end

@implementation SDLIAPTransport
@synthesize session;
@synthesize inStream;
@synthesize outStream;

-(id) init {
    
    [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"consoleLog" object:@"iAP Init"]];
    [SDLDebugTool logInfo:@"iAP Init"];
    
    if (self = [super initWithEndpoint:nil endpointParam:nil]) {
		transportLock = [[NSObject alloc] init];
		writeQueue = [[NSMutableArray alloc] initWithCapacity:10];
		spaceAvailable = NO;
        
        if (!registeredForNotifications) {
            registeredForNotifications = YES;
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(accessoryConnected:) name:EAAccessoryDidConnectNotification object:nil];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(accessoryDisconnected:) name:EAAccessoryDidDisconnectNotification object:nil];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationWillEnterForeground:) name:UIApplicationWillEnterForegroundNotification object:nil];
            [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(applicationDidEnterBackground:) name:UIApplicationDidEnterBackgroundNotification object:nil];
        }
        
        if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateBackground) {
            appInBackground = YES;
        } else {
            appInBackground = NO;
        }
        
        [self checkConnectedHeadUnitAccessory];
        
        transportUsable = YES;
        [SDLSiphonServer init];
	}
	return self;
}

- (void)stream:(NSStream *)aStream handleEvent:(NSStreamEvent)eventCode {
	if (aStream == inStream) {
		if (eventCode == NSStreamEventHasBytesAvailable || eventCode == NSStreamEventOpenCompleted) {
			UInt8 buf[1024];
			while (YES) {
				int bytesRead = [inStream read:buf maxLength:1024];
                
                [SDLSiphonServer _siphonRawTransportDataFromSync:buf msgBytesLength:bytesRead];
                
				if (bytesRead > 0) {
					[self handleBytesReceivedFromTransport:buf	length:bytesRead];
				} else {
					break;
				}
                
			}
		} else if (eventCode == NSStreamEventEndEncountered) {
			[self disconnect];
		}
	} else if (aStream == outStream) {
		if (eventCode == NSStreamEventHasSpaceAvailable) {
			if (writeQueue.count > 0) {
				@synchronized(transportLock) {
					NSData* msgBytes = [writeQueue objectAtIndex:0];
                    
					spaceAvailable = NO;
                    
					int bytesWritten = [outStream write:msgBytes.bytes maxLength:msgBytes.length];
                    
                    [SDLSiphonServer _siphonRawTransportDataFromApp:msgBytes.bytes msgBytesLength:bytesWritten]; 
                    
					if (bytesWritten < msgBytes.length) {
						NSData* leftover = [NSData dataWithBytes:msgBytes.bytes + bytesWritten length:msgBytes.length - bytesWritten];
						//Insert the leftover bytes at the front of the queue
						[writeQueue insertObject:leftover atIndex:0];
					}
					[writeQueue removeObjectAtIndex:0];
				}
			} else {
				spaceAvailable = YES;
			}
		} else if (eventCode == NSStreamEventOpenCompleted) {
            [self notifyTransportConnected];
		} else if (eventCode == NSStreamEventEndEncountered) {
			//[self handleTransportDisconnected];
		}
	}
}

-(NSString*) stringForEventCode:(NSStreamEvent) eventCode {
	switch (eventCode) {
		case NSStreamEventOpenCompleted:
			return @"OpenCompleted";
			break;
		case NSStreamEventHasSpaceAvailable:
			return @"HasSpaceAvailable";
			break;
		case NSStreamEventEndEncountered:
			return @"EndEncountered";
			break;
		case NSStreamEventErrorOccurred:
			return @"ErrorOccurred";
			break;
		case NSStreamEventHasBytesAvailable:
			return @"HasBytesAvailable";
			break;
		default:
			break;
	}
	return nil;
}

- (BOOL)connect:(EAAccessory*)accessoryItem usedProtocol:(NSString*)protocolName
{
    @synchronized (transportLock) {
		// Select the first accessory:
        self.session = [[[EASession alloc] initWithAccessory:accessoryItem forProtocol:protocolName] autorelease];        
        
		if(accessoryItem != nil && self.session != nil) {
			self.inStream = self.session.inputStream;
			self.outStream = self.session.outputStream;
			
			@try {		
				// Initialize and schedule the input stream:
				if(self.inStream != nil) {
                    [SDLDebugTool logInfo:@"IAPTransport: connect:usedProtocol: Initializing input steam"];
					self.inStream.delegate = self;
					[self.inStream scheduleInRunLoop:[NSRunLoop mainRunLoop] forMode:NSDefaultRunLoopMode];
					[self.inStream open];
				} else {	
					return NO;
				}
				
				// Initialize and schedule the output stream:
				if(self.outStream != nil) {
                    [SDLDebugTool logInfo:@"IAPTransport: connect:usedProtocol: Initializing output steam"];
					self.outStream.delegate = self;
					[self.outStream scheduleInRunLoop:[NSRunLoop mainRunLoop] forMode:NSDefaultRunLoopMode];
					[self.outStream open];
				} else {	
					return NO;
				}
			}
			@catch (id streamEx) {
				return NO;
			}//end catch
		} else {
            [SDLDebugTool logInfo:@"IAPTransport: connect:usedProtocol: Session and Accessory not set"];
			return NO;
		}
	}
	return YES;
}//end connect

- (bool) connect {
    if (connectedHeadUnitAccessory != nil) {
        [self connect:connectedHeadUnitAccessory usedProtocol:SYNC_PROTOCOL_STRING];
    } else {
        [self checkConnectedHeadUnitAccessory];
        if (connectedHeadUnitAccessory != nil) {
            [self connect:connectedHeadUnitAccessory usedProtocol:SYNC_PROTOCOL_STRING];
        } else {
            return NO;
        }
    }
	return YES;
}

-(void) checkConnectedHeadUnitAccessory {
    for (EAAccessory* anAccessory in [[EAAccessoryManager sharedAccessoryManager] connectedAccessories]) {
        [SDLDebugTool logInfo:@"accessory name: %@", anAccessory.name];
		for (NSString *aProtocolString in [anAccessory protocolStrings]) {
			[SDLDebugTool logInfo:@"IAPTransport: Found protocol string: %@", aProtocolString];
            if ([aProtocolString isEqualToString:SYNC_PROTOCOL_STRING]) {
                [SDLDebugTool logInfo:@"IAPTransport: Found accessory: %@", anAccessory.name];
                
                if (connectedHeadUnitAccessory != nil) {
                    [connectedHeadUnitAccessory release];
                    connectedHeadUnitAccessory = nil;
                }
                
                connectedHeadUnitAccessory = [anAccessory retain];
                return;
                break;
			}
		}
	}
}

-(void)applicationDidBecomeActive:(NSNotification *) notification {
    [self connect];
}

-(void)applicationWillEnterForeground:(NSNotification *)notification {
    appInBackground = NO;
    [self connect];
}

-(void)applicationDidEnterBackground:(NSNotification *)notification {
    appInBackground = YES;
}

-(EAAccessory*) getSyncAccessoryFromNotification:(NSNotification*) notification {
    EAAccessory *accessory = [[notification userInfo] objectForKey:EAAccessoryKey];
    
    for (NSString *protocolString in [accessory protocolStrings]) {
        if ([protocolString isEqualToString:SYNC_PROTOCOL_STRING]) {
            return accessory;
        }
    }
    
    return nil;
}

-(void) accessoryConnected:(NSNotification*) connectNotification {
    EAAccessory *connectedAccessory = [self getSyncAccessoryFromNotification:connectNotification];
    
    if(connectedAccessory == nil) {
    	// connectedAccessory is not a SYNC accessory
        return;
    }
    
    // We're assuming connectedSyncAccessory will be nil
	if (connectedHeadUnitAccessory != nil) {
        [connectedHeadUnitAccessory release];
        connectedHeadUnitAccessory = nil;
	}
    
	connectedHeadUnitAccessory = [connectedAccessory retain];
	[self connect];
    
}

-(void) accessoryDisconnected:(NSNotification*) connectNotification {
    EAAccessory *disconnectedAccessory = [self getSyncAccessoryFromNotification:connectNotification];
    
    if(disconnectedAccessory == nil) {
    	// disconnectedAccessory is not a SYNC accessory
        return;
    }
    
    if ([disconnectedAccessory connectionID] == [connectedHeadUnitAccessory connectionID]) {
        if (session != nil) {
            [self disconnect];
        }
        
        [connectedHeadUnitAccessory release];
        connectedHeadUnitAccessory = nil;
    }
}

-(NSString*) getHexString:(UInt8*)bytes length:(int) length {	
	NSMutableString* ret = [NSMutableString stringWithCapacity:(length * 2)];
	for (int i = 0; i < length; i++) {
		[ret appendFormat:@"%02X", ((Byte*)bytes)[i]];
	}
	return ret;
}

-(NSString*) getHexString:(NSData*) data {
	return [self getHexString:(Byte*)data.bytes length:data.length];
}


-(void) queueData:(NSData*) msgBytes {
	@synchronized (transportLock) {
		if (spaceAvailable) {
			spaceAvailable = NO;
			
			int bytesWritten = [outStream write:msgBytes.bytes maxLength:msgBytes.length];
            
            [SDLSiphonServer _siphonRawTransportDataFromApp:msgBytes.bytes msgBytesLength:bytesWritten];
            
			if (bytesWritten < msgBytes.length) {
				[writeQueue insertObject:[NSData dataWithBytes:msgBytes.bytes + bytesWritten length:msgBytes.length - bytesWritten] atIndex:0];
			}
			
		} else {
			[writeQueue addObject:msgBytes];
		}
	}
}

- (bool) sendBytes:(NSData*) msgBytes {
	[self queueData:msgBytes];
	return YES;
}

- (void) disconnect {
    @synchronized (transportLock) {
		
        transportUsable = NO;
        
		if (session != nil) {
			[self notifyTransportDisconnected];
			
            [outStream close];
			[outStream removeFromRunLoop:[NSRunLoop mainRunLoop] forMode:NSDefaultRunLoopMode];
			[outStream setDelegate:nil];
            [outStream release];
            outStream = nil;
            
            [inStream close];
            [inStream removeFromRunLoop:[NSRunLoop mainRunLoop] forMode:NSDefaultRunLoopMode];
			[inStream setDelegate:nil];
			[inStream release];
			inStream = nil;
			
			[session release];
			session = nil;
			
			[writeQueue release];
			writeQueue = nil;
		}
	}
}

- (void) dealloc {
    
    [self disconnect];
    
    if (registeredForNotifications) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:EAAccessoryDidConnectNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:EAAccessoryDidDisconnectNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidEnterBackgroundNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationWillEnterForegroundNotification object:nil];
        registeredForNotifications = NO;
    }
    
    [connectedHeadUnitAccessory release];
    connectedHeadUnitAccessory = nil;
    
	
	[transportLock release];
    
	[super dealloc];
}

@end
