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

#import "TestSuite.h"
#import "Test02.h"
#import "Test03.h"
#import "Test04.h"
#import "Test05.h"
#import "Test09.h"
#import "Test10.h"
#import "Test11.h"
#import "Test14.h"
#import "Test17.h"
#import "Test21.h"
#import "Test22.h"
#import "Test25.h"
#import "Test26.h"
#import "Test30.h"
#import "Test33.h"
#import "Test34.h"
#import "Test36.h"
#import "Test38.h"
#import "Test39.h"
#import "Test41.h"


@implementation TestSuite

+(NSArray*) getTestList {
    return [NSArray arrayWithObjects:
            [[Test02 alloc] init],
            [[Test03 alloc] init],
            [[Test04 alloc] init],
            [[Test05 alloc] init],
            [[Test09 alloc] init],
            [[Test10 alloc] init],
            [[Test11 alloc] init],
            [[Test14 alloc] init],
            [[Test17 alloc] init],
            [[Test21 alloc] init],
            [[Test22 alloc] init],
            [[Test25 alloc] init],
            [[Test26 alloc] init],
            [[Test30 alloc] init],
            [[Test33 alloc] init],
            [[Test34 alloc] init],
            [[Test36 alloc] init],
            [[Test38 alloc] init],
            [[Test39 alloc] init],
            [[Test41 alloc] init],
            nil];
}
@end
