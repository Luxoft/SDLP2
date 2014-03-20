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

#import "Test21.h"

@implementation Test21

-(bool)runTest:(id<RequestsSender>)proxy {
    if (![self loadProfile:PROFILE_B viaSender:proxy expectedSuccess:YES])
    {
        return NO;
    }
    NSString * msg = @"Helloo!";
    NSData * msgData = [msg dataUsingEncoding: NSUTF8StringEncoding];
    if (![self sendMessage:msgData
                 toProfile:ECHO_PROFILE
                 viaSender:proxy
           expectedSuccess:YES])
    {
        return NO;
    }
    SDLOnProfileToAppMessage * echo = [self waitForMessageFromProfile:ECHO_PROFILE];
    if (!echo) {
        return NO;
    }
    NSString * reply = [echo messageDataToString];
    NSLog(@"reply: %@", reply);
    NSLog(@"reply size: %i", [reply length]);
    if (![reply isEqualToString: msg]) {
        NSLog(@"messages are not equal");
        return NO;
    }
    if (![self unloadProfile:ECHO_PROFILE viaSender:proxy expectedSuccess:YES])
    {
        return NO;
    }
    return YES;
}

-(NSInteger) getId {
    return 21;
}
@end
