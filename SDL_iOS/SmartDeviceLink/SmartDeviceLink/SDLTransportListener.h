//  SDLTransportListener.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@protocol SDLTransportListener

- (void) onTransportConnected;
- (void) onTransportDisconnected;
- (void) onBytesReceived:(Byte*)bytes length:(long) length;

@end
