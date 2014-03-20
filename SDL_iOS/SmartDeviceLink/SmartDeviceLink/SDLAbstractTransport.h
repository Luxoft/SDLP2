//  SDLAbstractTransport.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLITransport.h>

@interface SDLAbstractTransport : NSObject <SDLITransport> {
	NSString* guid;
	NSString* endpointName;
	NSString* endpointParam;
	bool isSecure;
	bool isConnected;
	NSMutableArray* transportListeners;
	
	UInt8* accumBuf;
	long accumBufWritePos;
	long accumBufLength;
	NSLock* accumBufLock;
	
	int mtuSize;
}

@property (readonly) NSString* guid;
@property (readonly) NSString* endpointName;
@property (readonly) NSString* endpointParam;
@property (assign) bool isSecure;
@property (assign) bool isConnected;

@property(assign) int mtuSize;

- (id) initWithEndpoint:(NSString*) endpoint endpointParam:(NSString*) endointParam;
- (void) notifyTransportConnected;
- (void) notifyTransportDisconnected;
- (void) handleBytesReceivedFromTransport:(Byte*) receivedBytes length:(int) receivedBytesLength;

@end
