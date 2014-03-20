//  SDLAppInterfaceUnregisteredReason.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLAppInterfaceUnregisteredReason.h>

SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_USER_EXIT = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_IGNITION_OFF = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_BLUETOOTH_OFF = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_USB_DISCONNECTED = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_REQUEST_WHILE_IN_NONE_HMI_LEVEL = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_TOO_MANY_REQUESTS = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_DRIVER_DISTRACTION_VIOLATION = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_MASTER_RESET = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_FACTORY_DEFAULTS = nil;
SDLAppInterfaceUnregisteredReason* SDLAppInterfaceUnregisteredReason_APP_UNAUTHORIZED = nil;

NSMutableArray* SDLAppInterfaceUnregisteredReason_values = nil;
@implementation SDLAppInterfaceUnregisteredReason

+(SDLAppInterfaceUnregisteredReason*) valueOf:(NSString*) value {
    for (SDLAppInterfaceUnregisteredReason* item in SDLAppInterfaceUnregisteredReason.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    if (SDLAppInterfaceUnregisteredReason_values == nil) {
        SDLAppInterfaceUnregisteredReason_values = [[NSMutableArray alloc] initWithObjects:
                SDLAppInterfaceUnregisteredReason.USER_EXIT,
                SDLAppInterfaceUnregisteredReason.IGNITION_OFF,
                SDLAppInterfaceUnregisteredReason.BLUETOOTH_OFF,
                SDLAppInterfaceUnregisteredReason.USB_DISCONNECTED,
                SDLAppInterfaceUnregisteredReason.REQUEST_WHILE_IN_NONE_HMI_LEVEL,
                SDLAppInterfaceUnregisteredReason.TOO_MANY_REQUESTS,
                SDLAppInterfaceUnregisteredReason.DRIVER_DISTRACTION_VIOLATION,
                SDLAppInterfaceUnregisteredReason.MASTER_RESET,
                SDLAppInterfaceUnregisteredReason.FACTORY_DEFAULTS,
                SDLAppInterfaceUnregisteredReason.APP_UNAUTHORIZED,
                nil];
    }
    return SDLAppInterfaceUnregisteredReason_values;
}

+(SDLAppInterfaceUnregisteredReason*) USER_EXIT {
    	if (SDLAppInterfaceUnregisteredReason_USER_EXIT == nil) {
        		SDLAppInterfaceUnregisteredReason_USER_EXIT = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"USER_EXIT"];
    	}
    	return SDLAppInterfaceUnregisteredReason_USER_EXIT;
}

+(SDLAppInterfaceUnregisteredReason*) IGNITION_OFF {
    	if (SDLAppInterfaceUnregisteredReason_IGNITION_OFF == nil) {
        		SDLAppInterfaceUnregisteredReason_IGNITION_OFF = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"IGNITION_OFF"];
    	}
    	return SDLAppInterfaceUnregisteredReason_IGNITION_OFF;
}

+(SDLAppInterfaceUnregisteredReason*) BLUETOOTH_OFF {
    	if (SDLAppInterfaceUnregisteredReason_BLUETOOTH_OFF == nil) {
        		SDLAppInterfaceUnregisteredReason_BLUETOOTH_OFF = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"BLUETOOTH_OFF"];
    	}
    	return SDLAppInterfaceUnregisteredReason_BLUETOOTH_OFF;
}

+(SDLAppInterfaceUnregisteredReason*) USB_DISCONNECTED {
    	if (SDLAppInterfaceUnregisteredReason_USB_DISCONNECTED == nil) {
        		SDLAppInterfaceUnregisteredReason_USB_DISCONNECTED = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"USB_DISCONNECTED"];
    	}
    	return SDLAppInterfaceUnregisteredReason_USB_DISCONNECTED;
}

+(SDLAppInterfaceUnregisteredReason*) REQUEST_WHILE_IN_NONE_HMI_LEVEL {
    	if (SDLAppInterfaceUnregisteredReason_REQUEST_WHILE_IN_NONE_HMI_LEVEL == nil) {
        		SDLAppInterfaceUnregisteredReason_REQUEST_WHILE_IN_NONE_HMI_LEVEL = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"REQUEST_WHILE_IN_NONE_HMI_LEVEL"];
    	}
    	return SDLAppInterfaceUnregisteredReason_REQUEST_WHILE_IN_NONE_HMI_LEVEL;
}

+(SDLAppInterfaceUnregisteredReason*) TOO_MANY_REQUESTS {
    	if (SDLAppInterfaceUnregisteredReason_TOO_MANY_REQUESTS == nil) {
        		SDLAppInterfaceUnregisteredReason_TOO_MANY_REQUESTS = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"TOO_MANY_REQUESTS"];
    	}
    	return SDLAppInterfaceUnregisteredReason_TOO_MANY_REQUESTS;
}

+(SDLAppInterfaceUnregisteredReason*) DRIVER_DISTRACTION_VIOLATION {
    	if (SDLAppInterfaceUnregisteredReason_DRIVER_DISTRACTION_VIOLATION == nil) {
        		SDLAppInterfaceUnregisteredReason_DRIVER_DISTRACTION_VIOLATION = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"DRIVER_DISTRACTION_VIOLATION"];
    	}
    	return SDLAppInterfaceUnregisteredReason_DRIVER_DISTRACTION_VIOLATION;
}

+(SDLAppInterfaceUnregisteredReason*) MASTER_RESET {
    	if (SDLAppInterfaceUnregisteredReason_MASTER_RESET == nil) {
        		SDLAppInterfaceUnregisteredReason_MASTER_RESET = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"MASTER_RESET"];
    	}
    	return SDLAppInterfaceUnregisteredReason_MASTER_RESET;
}

+(SDLAppInterfaceUnregisteredReason*) FACTORY_DEFAULTS {
    	if (SDLAppInterfaceUnregisteredReason_FACTORY_DEFAULTS == nil) {
        		SDLAppInterfaceUnregisteredReason_FACTORY_DEFAULTS = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"FACTORY_DEFAULTS"];
    	}
    	return SDLAppInterfaceUnregisteredReason_FACTORY_DEFAULTS;
}

+(SDLAppInterfaceUnregisteredReason*) APP_UNAUTHORIZED {
    if (SDLAppInterfaceUnregisteredReason_APP_UNAUTHORIZED == nil) {
        SDLAppInterfaceUnregisteredReason_APP_UNAUTHORIZED = [[SDLAppInterfaceUnregisteredReason alloc] initWithValue:@"APP_UNAUTHORIZED"];
    }
    return SDLAppInterfaceUnregisteredReason_APP_UNAUTHORIZED;
}

@end
