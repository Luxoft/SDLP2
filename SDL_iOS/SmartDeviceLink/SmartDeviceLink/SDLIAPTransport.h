//  SDLIAPTransport.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLAbstractTransport.h>
#import <ExternalAccessory/ExternalAccessory.h>

@interface SDLIAPTransport : SDLAbstractTransport<NSStreamDelegate> {
	EASession* session;
	NSInputStream* inStream;
	NSOutputStream* outStream;
	NSObject* transportLock;
	
	NSMutableArray* writeQueue;
	
	BOOL spaceAvailable;
	
    BOOL registeredForNotifications;
    BOOL appInBackground;
    BOOL transportUsable;
    
    EAAccessory *connectedHeadUnitAccessory;
}

@property(nonatomic, retain) EASession* session;
@property(nonatomic, retain) NSStream* inStream;
@property(nonatomic, retain) NSStream* outStream;

-(void) checkConnectedHeadUnitAccessory;

@end
