//  SDLProtocolListener.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLProtocolMessage.h>

@protocol SDLProtocolListener

-(void) handleProtocolSessionStarted:(SDLSessionType) sessionType sessionID:(Byte) sessionID version:(Byte) version;
-(void) onProtocolMessageReceived:(SDLProtocolMessage*) msg;

-(void) onProtocolOpened;
-(void) onProtocolClosed;
-(void) onError:(NSString*) info exception:(NSException*) e;

@end

