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

#import "Test41.h"

@interface Test41() {
    id<RequestsSender> mProxy;
}
- (bool) sendAppState:(SDLMobileAppState*)state;
@end

@implementation Test41

-(bool) runTest:(id<RequestsSender>)proxy {
    mProxy = proxy;
    if (![self loadProfile:ECHO_PROFILE viaSender:proxy expectedSuccess:YES]) {
        return NO;
    }
    if (![self sendAppState: [SDLMobileAppState MOBILE_APP_BACKGROUND]]) {
        mProxy = nil;
        return NO;
    }
    
    SDLOnProfileToAppMessage * response = [self waitForMessageFromProfile:ECHO_PROFILE];
    NSData * data = [response messageData];
    NSString * dataStr = [[NSString alloc] initWithBytes:[data bytes] length:[data length] encoding:NSUTF8StringEncoding];
    if (![dataStr isEqualToString: @"MOBILE_APP_BACKGROUND"])
    {
        mProxy = nil;
        return NO;
    }
    if (![self sendAppState: [SDLMobileAppState MOBILE_APP_FOREGROUND]]) {
        mProxy = nil;
        return NO;
    }
    response = [self waitForMessageFromProfile:ECHO_PROFILE];
    data = [response messageData];
    dataStr = [[NSString alloc] initWithBytes:[data bytes] length:[data length] encoding:NSUTF8StringEncoding];
    if (![dataStr isEqualToString: @"MOBILE_APP_FOREGROUND"])
    {
        mProxy = nil;
        return NO;
    }
    if (![self sendAppState: [SDLMobileAppState MOBILE_APP_LOCK_SCREEN]]) {
        mProxy = nil;
        return NO;
    }
    response = [self waitForMessageFromProfile:ECHO_PROFILE];
    data = [response messageData];
    dataStr = [[NSString alloc] initWithBytes:[data bytes] length:[data length] encoding:NSUTF8StringEncoding];
    if (![dataStr isEqualToString: @"MOBILE_APP_LOCK_SCREEN"])
    {
        mProxy = nil;
        return NO;
    }
    mProxy = nil;
    return [self unloadProfile:ECHO_PROFILE viaSender:proxy expectedSuccess:YES];
}


- (bool) sendAppState:(SDLMobileAppState*)state {
    SDLAppStateChanged * req = [SDLRPCRequestFactory buildAppStateChangedWithProfileName:ECHO_PROFILE
                                                                        applicationState:state
                                                                           correlationID:[Test getAndIncCorrelationID]];
    return [self waitForResponse:req viaSender:mProxy expectedSuccess:YES];
}


-(NSInteger) getId {
    return 41;
}
@end
