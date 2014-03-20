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

/**
 * \brief Structure for storing info (ip and port) about discovered devices.
 * \author Elena Bratanova <ebratanova@luxoft.com>
 * \date 28/06/13
 */

#import <SmartDeviceLink/SDLTcpDiscoveredDevice.h>

@implementation SDLTcpDiscoveredDevice

-(NSString*) stringDescription {
    if ([self name] != nil)
    {
        if ([self adapter] != nil)
        {
            return [NSString stringWithFormat: @"%@@%@", [self name], [self adapter]];
        }
        return [NSString stringWithFormat: @"%@", [self name]];
    }
    else
    {
        if ([self adapter] != nil)
        {
            return [NSString stringWithFormat: @"[%@:%@]@%@", [self ip], [self boundTcpPort], [self adapter]];
        }
        return [NSString stringWithFormat: @"[%@:%@]", [self ip], [self boundTcpPort]];
    }
}

- (void) dealloc
{
    [_ip release];
    [_boundTcpPort release];
    [_name release];
    [_uuid release];
    [_localIP release];
    [_adapter release];
    [super dealloc];
}
@end
