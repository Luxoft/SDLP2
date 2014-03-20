//
//  NetworkInterface.m
//  SmartDeviceLinkProxy
//
//  Created by Ivilink Tsc on 10/01/14.
//
//

#import <SmartDeviceLink/SDLTCPNetworkInterface.h>

@implementation SDLTcpNetworkInterface

- (NSString*)toString
{
    return [NSString stringWithFormat: @"iface: %@, local: %@, brd: %@", [self interfaceName], [self localAddress], [self broadcastAddress]];
}

- (void) dealloc
{
    [_broadcastAddress release];
    [_localAddress release];
    [_interfaceName release];
    [super dealloc];
}
@end
