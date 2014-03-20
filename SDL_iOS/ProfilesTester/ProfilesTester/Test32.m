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

#import "Test32.h"

@implementation Test32

-(bool)runTest:(id<RequestsSender>)proxy {
    if(![self addProfile:ECHO_LIB_NAME
               viaSender:proxy
              forProfile:ECHO_PROFILE
         expectedSuccess:YES]) {
        return NO;
    }
    return [self removeProfile:ECHO_PROFILE viaSender:proxy expectedSuccess:YES];
}

-(NSInteger) getId {
    return 32;
}

@end
