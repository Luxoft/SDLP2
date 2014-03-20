/*
 *
 * SDLP SDK
 * Cross Platform Application Communication Stack for In-Vehicle Applications
 *
 * Copyright (C) 2013, Luxoft Professional Corp., member of IBS group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; version 2.1.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 *
 */

#import <SmartDeviceLink/SDLMobileAppState.h>

SDLMobileAppState* SDLMobileAppState_MOBILE_APP_FOREGROUND = nil;
SDLMobileAppState* SDLMobileAppState_MOBILE_APP_BACKGROUND = nil;
SDLMobileAppState* SDLMobileAppState_MOBILE_APP_LOCK_SCREEN = nil;

NSMutableArray* SDLMobileAppState_values = nil;

@implementation SDLMobileAppState

+(SDLMobileAppState*) valueOf:(NSString*) value {
    for (SDLMobileAppState* item in SDLMobileAppState.values) {
        if ([item.value isEqualToString:value]) {
            return item;
        }
    }
    return nil;
}

+(NSMutableArray*) values {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SDLMobileAppState_values = [[NSMutableArray alloc] initWithObjects:
                                    [SDLMobileAppState MOBILE_APP_FOREGROUND],
                                    [SDLMobileAppState MOBILE_APP_BACKGROUND],
                                    [SDLMobileAppState MOBILE_APP_LOCK_SCREEN],nil];
    });
    return SDLMobileAppState_values;
}

+(SDLMobileAppState*) MOBILE_APP_FOREGROUND {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SDLMobileAppState_MOBILE_APP_FOREGROUND = [[SDLMobileAppState alloc] initWithValue: @"MOBILE_APP_FOREGROUND"];
    });
    return SDLMobileAppState_MOBILE_APP_FOREGROUND;
}

+(SDLMobileAppState*) MOBILE_APP_BACKGROUND {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SDLMobileAppState_MOBILE_APP_BACKGROUND = [[SDLMobileAppState alloc] initWithValue: @"MOBILE_APP_BACKGROUND"];
    });
    return SDLMobileAppState_MOBILE_APP_BACKGROUND;
    
}

+(SDLMobileAppState*) MOBILE_APP_LOCK_SCREEN {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SDLMobileAppState_MOBILE_APP_LOCK_SCREEN = [[SDLMobileAppState alloc] initWithValue: @"MOBILE_APP_LOCK_SCREEN"];
    });
    return SDLMobileAppState_MOBILE_APP_LOCK_SCREEN;
}
@end
