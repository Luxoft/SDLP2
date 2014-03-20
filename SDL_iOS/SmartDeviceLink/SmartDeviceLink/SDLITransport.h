//  SDLITransport.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLTransportListener.h>

@protocol SDLITransport

@property(assign) int mtuSize;

- (bool) connect;
- (void) disconnect;
- (bool) sendBytes:(NSData*) msg;
- (void) addTransportListener:(NSObject<SDLTransportListener>*) transListener;
- (void) removeTransportListener:(NSObject<SDLTransportListener>*) transListener;

@end
