//  SDLProxy.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company
//  Version: SDL-2.0.0

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLExternalLibrary.h>
#import <SmartDeviceLink/SDLIProxy.h>
#import <SmartDeviceLink/SDLIProtocol.h>
#import <SmartDeviceLink/SDLProxyListener.h>
#import <SmartDeviceLink/SDLRPCRequestFactory.h>
#import <SmartDeviceLink/SDLITransport.h>

@interface SDLProxy : NSObject<SDLProtocolListener, NSURLConnectionDelegate> {
    Byte _version;
    
	NSObject<SDLITransport>* transport;
	NSObject<SDLIProtocol>* protocol;
	NSMutableArray* proxyListeners;
    NSMutableArray* externalLibraries;
	Byte rpcSessionID;
	Byte bulkSessionID;
	
    NSTimer* handshakeTimer;
    
	BOOL isConnected;
    BOOL alreadyDestructed;
    
    NSMutableData* httpResponseData;
}

-(id)  initWithTransport:(NSObject<SDLITransport>*) transport protocol:(NSObject<SDLIProtocol>*) protocol delegate:(NSObject<SDLProxyListener>*) delegate;

-(void) dispose;
-(void) addDelegate:(NSObject<SDLProxyListener>*) delegate;

-(void) registerLibrary:(id<SDLExternalLibrary>) externalLibrary;

-(void) sendRPCRequest:(SDLRPCMessage*) msg;
-(void) handleRpcMessage:(NSDictionary*) msg;

-(NSString*) getProxyVersion;

-(void) destroyHandshakeTimer;
-(void) handleProtocolMessage:(SDLProtocolMessage*) msgData;

+(void)enableSiphonDebug;
+(void)disableSiphonDebug;

-(NSObject<SDLITransport>*)getTransport;
-(NSObject<SDLIProtocol>*)getProtocol;

@end
