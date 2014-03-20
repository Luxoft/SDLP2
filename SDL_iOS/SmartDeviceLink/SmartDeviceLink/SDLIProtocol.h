//  SDLProtocol.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLITransport.h>

#import <SmartDeviceLink/SDLProtocolListener.h>
#import <SmartDeviceLink/SDLProtocolMessage.h>

@protocol SDLIProtocol<SDLTransportListener>

-(void) handleBytesFromTransport:(Byte*) receivedBytes length:(long) receivedBytesLength;

-(void) sendStartSessionWithType:(SDLSessionType) sessionType;
-(void) sendEndSessionWithType:(SDLSessionType)sessionType sessionID:(Byte)sessionID;
-(void) sendData:(SDLProtocolMessage*) protocolMsg;

@property(assign) NSObject<SDLITransport>* transport;

-(void) addProtocolListener:(NSObject<SDLProtocolListener>*) listener;
-(void) removeProtocolListener:(NSObject<SDLProtocolListener>*) listener;

@end