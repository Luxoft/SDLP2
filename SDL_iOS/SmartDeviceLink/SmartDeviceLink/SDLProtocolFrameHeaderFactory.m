//  SDLProtocolFrameHeaderFactory.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLProtocolFrameHeaderFactory.h>

#import <SmartDeviceLink/SDLBitConverter.h>

@implementation SDLProtocolFrameHeaderFactory

+(SDLProtocolFrameHeader*) parseHeader:(NSData*) header {
	SDLProtocolFrameHeader* msg = [[[SDLProtocolFrameHeader alloc] init] autorelease];
	
	Byte version = (Byte)(((Byte*)header.bytes)[0] >> 4);
	BOOL compressed = (1 == ((((Byte*)header.bytes)[0] & 0x08) >> 3));
	Byte frameType = (Byte)(((Byte*)header.bytes)[0] & 0x07);
	Byte sessionType = ((Byte*)header.bytes)[1];
	Byte frameData = ((Byte*)header.bytes)[2];
	Byte sessionID = ((Byte*)header.bytes)[3];
	UInt32 dataSize = [SDLBitConverter intFromByteArray:((Byte*)header.bytes) offset:4];
    
    if (version == 2) {
        UInt32 messageID = [SDLBitConverter intFromByteArray:((Byte*)header.bytes) offset:8];
        msg._messageID = messageID;
    } else {
        msg._messageID = 0;
    }
	
	msg._version = version;
	msg._compressed = compressed;
	msg._frameType = frameType;
	msg._sessionType = sessionType;
	msg._frameData = frameData;
	msg._sessionID = sessionID;
	msg._dataSize = dataSize;
	
	return msg;
}

+(SDLProtocolFrameHeader*) startSessionWithSessionType:(SDLSessionType) sessionType messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._frameType = SDLFrameType_Control;
	header._sessionType = sessionType;
	header._frameData = SDLFrameData_StartSession;
    header._messageID = messageID;
	
	return header;
}


+(SDLProtocolFrameHeader*) endSessionWithSessionType:(SDLSessionType)sessionType sessionID:(Byte)sessionID messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._frameType = SDLFrameType_Control;
	header._sessionType = sessionType;
	header._sessionID = sessionID;
	header._frameData = SDLFrameData_EndSession;
    header._messageID = messageID;

	return header;
}
+(SDLProtocolFrameHeader*) singleFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte) sessionID dataSize:(NSInteger) dataSize messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._sessionType = sessionType;
	header._sessionID = sessionID;
	header._frameType = SDLFrameType_Single;
	header._dataSize = dataSize;
    header._messageID = messageID;
	
	return header;
}

+(SDLProtocolFrameHeader*) firstFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte) sessionID messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._sessionType = sessionType;
	header._sessionID = sessionID;
	header._frameType = SDLFrameType_First;
	header._dataSize = 8;
    header._frameData = 1;
    header._messageID = messageID;
	
	return header;
}

+(SDLProtocolFrameHeader*) consecutiveFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte) sessionID dataSize:(NSInteger) dataSize messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._sessionType = sessionType;
	header._sessionID = sessionID;
	header._frameType = SDLFrameType_Consecutive;
	header._dataSize = dataSize;
    header._frameData = 1;
    header._messageID = messageID;
	
	return header;
}

+(SDLProtocolFrameHeader*) lastFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte) sessionID dataSize:(NSInteger) dataSize messageID:(UInt32)messageID version:(Byte)version {
	SDLProtocolFrameHeader* header = [[[SDLProtocolFrameHeader alloc] init] autorelease];
    header._version = version;
	header._sessionType = sessionType;
	header._sessionID = sessionID;
	header._frameType = SDLFrameType_Consecutive;
	header._dataSize = dataSize;
    header._frameData = 0;
    header._messageID = messageID;
	
	return header;
}
@end
