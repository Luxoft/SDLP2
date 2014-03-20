//  SDLProtocolFrameHeaderFactory.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

#import <SmartDeviceLink/SDLProtocolFrameHeader.h>

@interface SDLProtocolFrameHeaderFactory : NSObject {}

+(SDLProtocolFrameHeader*) parseHeader:(NSData*) header;
+(SDLProtocolFrameHeader*) startSessionWithSessionType:(SDLSessionType)sessionType messageID:(UInt32)messageID version:(Byte)version;
+(SDLProtocolFrameHeader*) endSessionWithSessionType:(SDLSessionType)sessionType sessionID:(Byte)sessionID messageID:(UInt32)messageID version:(Byte)version;
+(SDLProtocolFrameHeader*) singleFrameWithSessionType:(SDLSessionType)sessionType sessionID:(Byte)sessionID dataSize:(NSInteger)dataSize messageID:(UInt32)messageID version:(Byte)version;
+(SDLProtocolFrameHeader*) firstFrameWithSessionType:(SDLSessionType)sessionType sessionID:(Byte)sessionID messageID:(UInt32)messageID version:(Byte)version;
+(SDLProtocolFrameHeader*) consecutiveFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte)sessionID dataSize:(NSInteger)dataSize messageID:(UInt32)messageID version:(Byte)version;
+(SDLProtocolFrameHeader*) lastFrameWithSessionType:(SDLSessionType) sessionType sessionID:(Byte)sessionID dataSize:(NSInteger)dataSize messageID:(UInt32)messageID version:(Byte)version;

@end
