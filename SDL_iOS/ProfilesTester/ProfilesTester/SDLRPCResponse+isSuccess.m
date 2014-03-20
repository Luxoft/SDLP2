//
//  SDLRPCResponse+isSuccess.m
//  ProfilesTester
//
//  Created by Ivilink Tsc on 14/03/14.
//  Copyright (c) 2014 Ivilink Tsc. All rights reserved.
//

#import "SDLRPCResponse+isSuccess.h"

@implementation SDLRPCResponse (isSuccess)

- (BOOL)isSuccess
{
    return [[self success] boolValue];
}

@end
