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

#import <SmartDeviceLink/ProfileManagerRequest.h>

#include <SmartDeviceLink/IVINames.h>

@implementation ProfileManagerRequest
-(id) initWithName:(NSString *)functionName {
    self = [super initWithName:functionName];
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    self = [super initWithDictionary:dict];
    return self;
}

-(void) setProfileName:(NSString *)profileName {
    if (profileName != nil) {
        [parameters setObject:profileName forKey:IVI_NAMES_TAG_profileName];
    } else {
        [parameters removeObjectForKey:IVI_NAMES_TAG_profileName];
    }
}

-(NSString*) profileName {
    return [parameters objectForKey:IVI_NAMES_TAG_profileName];
}
@end
