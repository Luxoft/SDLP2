//  SDLProxyFactory.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLProxyFactory.h>

#import <SmartDeviceLink/SDLDebugTool.h>
#import <SmartDeviceLink/SDLIAPTransport.h>
#import <SmartDeviceLink/SDLTCPTransport.h>
#import <SmartDeviceLink/SDLProtocol.h>

@implementation SDLProxyFactory

+(SDLProxy*) buildProxyWithListener:(NSObject<SDLProxyListener>*) delegate {
    SDLIAPTransport* transport = [[SDLIAPTransport alloc] init];
    SDLProtocol* protocol = [[SDLProtocol alloc] init];
    
    SDLProxy *ret = [[SDLProxy alloc] initWithTransport:transport protocol:protocol delegate:delegate];
    
    [transport release];
    [protocol release];
    
	return [ret autorelease];
}

+(SDLProxy*) buildProxyWithListener:(NSObject<SDLProxyListener>*) delegate
                              tcpIPAddress: (NSString*) ipaddress
                                   tcpPort: (NSString*) port {
    
    SDLTCPTransport* transport = [[SDLTCPTransport alloc] initWithEndpoint:ipaddress endpointParam:port];
    SDLProtocol* protocol = [[SDLProtocol alloc] init];
    
    SDLProxy *ret = [[SDLProxy alloc] initWithTransport:transport protocol:protocol delegate:delegate];
    
    [transport release];
    [protocol release];
    
	return [ret autorelease];
}

@end