//
//  NetworkInterface.h
//  SmartDeviceLinkProxy
//
//  Created by Ivilink Tsc on 10/01/14.
//
//

#import <Foundation/Foundation.h>

@interface SDLTcpNetworkInterface : NSObject
@property(copy,nonatomic) NSString * broadcastAddress;
@property(nonatomic) int32_t broadcastAddressRaw;
@property(copy,nonatomic) NSString * localAddress;
@property(copy,nonatomic) NSString * interfaceName;
- (NSString*) toString;
@end
