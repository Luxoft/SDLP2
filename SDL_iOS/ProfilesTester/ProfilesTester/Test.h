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

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SmartDeviceLink.h>
#import "RequestsSender.h"

#include "ProfileNames.h"

@interface Test : NSObject<SDLProxyListener>

-(bool) runTest:(id<RequestsSender>)proxy;

-(NSInteger) getId;

-(SDLRPCResponse*) waitForResponse: (NSInteger)correlationID;

-(SDLRPCNotification*) waitForNotification;

-(bool)waitForResponse:(SDLRPCRequest*)req viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess;

+(NSNumber*)getAndIncCorrelationID;

-(bool)loadProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess;
-(bool)unloadProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess;
-(bool)sendMessage:(NSData*)message toProfile:(NSString*)profileName  viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess;
-(SDLOnProfileToAppMessage*)waitForMessageFromProfile:(NSString*)profileName;

-(bool)addProfile:(NSString*)filePath viaSender:(id<RequestsSender>)proxy forProfile:(NSString*)profileName expectedSuccess:(bool)isSuccess;
-(bool)removeProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess;
@end
