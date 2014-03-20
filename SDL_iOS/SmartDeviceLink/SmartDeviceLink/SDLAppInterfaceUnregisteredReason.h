//  SDLAppInterfaceUnregisteredReason.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLAppInterfaceUnregisteredReason : SDLEnum {}

+(SDLAppInterfaceUnregisteredReason*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLAppInterfaceUnregisteredReason*) USER_EXIT;
+(SDLAppInterfaceUnregisteredReason*) IGNITION_OFF;
+(SDLAppInterfaceUnregisteredReason*) BLUETOOTH_OFF;
+(SDLAppInterfaceUnregisteredReason*) USB_DISCONNECTED;
+(SDLAppInterfaceUnregisteredReason*) REQUEST_WHILE_IN_NONE_HMI_LEVEL;
+(SDLAppInterfaceUnregisteredReason*) TOO_MANY_REQUESTS;
+(SDLAppInterfaceUnregisteredReason*) DRIVER_DISTRACTION_VIOLATION;
+(SDLAppInterfaceUnregisteredReason*) MASTER_RESET;
+(SDLAppInterfaceUnregisteredReason*) FACTORY_DEFAULTS;
+(SDLAppInterfaceUnregisteredReason*) APP_UNAUTHORIZED;

@end
