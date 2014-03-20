//  SDLProtocolFrameHeader.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLProtocolMessage.h>

@implementation SDLProtocolFrameHeader

@synthesize _version;
@synthesize _compressed;
@synthesize _frameType;
@synthesize _sessionType;
@synthesize _frameData;
@synthesize _sessionID;
@synthesize _dataSize;
@synthesize _messageID;


-(id) init {
	if (self = [super init]) {
		_version = 1;
		_compressed = false;
		_frameType = SDLFrameType_Control;
		_sessionType = SDLSessionType_RPC;
		_frameData = 0;
		_sessionID = 0;
		_dataSize = 0;
        _messageID = 0;
	}
	return self;
}
				
@end
