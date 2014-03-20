//  SDLProtocolFrameHeader.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

typedef enum SDLFrameType {
	SDLFrameType_Control = 0x00,
	SDLFrameType_Single = 0x01,
	SDLFrameType_First = 0x02,
	SDLFrameType_Consecutive = 0x03,
	
} SDLFrameType;

typedef enum SDLSessionType {
	SDLSessionType_RPC = 0x7,
	SDLSessionType_BulkData = 0xF,
} SDLSessionType;

typedef enum SDLFrameData {
	SDLFrameData_Heartbeat = 0x00,
	SDLFrameData_StartSession = 0x01,
	SDLFrameData_StartSessionACK = 0x02,
	SDLFrameData_StartSessionNACK = 0x03,
	SDLFrameData_EndSession = 0x04,
	
	SDLFrameData_SingleFrame = 0x00,
	SDLFrameData_FirstFrame = 0x00,
} SDLFrameData;

@interface SDLProtocolFrameHeader : NSObject {
	Byte _version;
	BOOL _compressed;
	SDLFrameType _frameType;
	SDLSessionType _sessionType;
	Byte _frameData;
	Byte _sessionID;
	UInt32 _dataSize;
    UInt32 _messageID;
}

@property(assign) Byte _version;
@property(assign) BOOL _compressed;
@property(assign) SDLFrameType _frameType;
@property(assign) SDLSessionType _sessionType;
@property(assign) Byte _frameData;
@property(assign) Byte _sessionID;
@property(assign) UInt32 _dataSize;
@property(assign) UInt32 _messageID;

@end