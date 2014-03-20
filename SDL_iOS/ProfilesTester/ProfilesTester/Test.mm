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

#import "Test.h"

#import "SDLRPCResponse+isSuccess.h"
#include <map>
#include <queue>
#include <dispatch/dispatch.h>

static int correlationID = 200;

@interface Test()
{
    std::map<int, dispatch_semaphore_t> mSemaphoreMap;
    std::map<int, SDLRPCResponse*> mOtherSideMessages;
    NSMutableArray * mNotifications;
    
    dispatch_queue_t mMapsQueue;
    
    dispatch_semaphore_t mNotificationSemaphore;
}

-(void) insertResponseToMap:(SDLRPCResponse*)message;
-(void) insertNotificationToQueue:(SDLRPCNotification*)message;

@end

@implementation Test

-(id) init
{
    self = [super init];
    if (self)
    {
        mMapsQueue = dispatch_queue_create("testqueue", DISPATCH_QUEUE_SERIAL);
        mNotificationSemaphore = dispatch_semaphore_create(0);
        mNotifications = [[NSMutableArray alloc] init];
    }
    return self;
}

-(bool) runTest:(id<RequestsSender>)proxy {
    return false;
}

-(NSInteger) getId {
    return 0;
}

- (void) onProxyClosed
{
    NSAssert(false, @"Unexpected method call");
}

- (void) onOnHMIStatus:(SDLOnHMIStatus *)notification
{
}

- (void) onOnDriverDistraction:(SDLOnDriverDistraction *)notification
{
}

- (void) onProxyOpened
{
    NSAssert(false, @"Unexpected method call");
}

-(bool)waitForResponse:(SDLRPCRequest*)req viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess {
    [proxy sendRequest: req];
    SDLRPCResponse * resp =[self waitForResponse:[[req correlationID] integerValue]];
    if (isSuccess) {
        return [resp isSuccess];
    } else {
        return ![resp isSuccess];
    }
}

-(SDLRPCResponse*) waitForResponse: (NSInteger)correlationID {
    __block SDLRPCResponse * result = nil;
    dispatch_sync(mMapsQueue, ^{
        if (mSemaphoreMap.find(correlationID) == mSemaphoreMap.end()) {
            mSemaphoreMap[correlationID] = dispatch_semaphore_create(0);
        }
    });
    dispatch_semaphore_wait(mSemaphoreMap[correlationID], DISPATCH_TIME_FOREVER);
    dispatch_sync(mMapsQueue, ^{
        std::map<int, dispatch_semaphore_t>::const_iterator iter = mSemaphoreMap.find(correlationID);
        if (iter != mSemaphoreMap.end()) {
            mSemaphoreMap.erase(iter);
        }
        result = mOtherSideMessages[correlationID];
    });
    return result;
}

-(SDLRPCNotification*)waitForNotification {
    __block SDLRPCNotification * noti = nil;
    dispatch_semaphore_wait(mNotificationSemaphore, DISPATCH_TIME_FOREVER);
    dispatch_sync(mMapsQueue, ^{
        noti = [mNotifications lastObject];
        [mNotifications removeLastObject];
    });
    return noti;
}

-(void) onOnProfileToAppMessage: (SDLOnProfileToAppMessage*) notification {
    [self insertNotificationToQueue: notification];
}

-(void) onOnProfileUnloaded:(SDLOnProfileUnloaded *)notification {
    [self insertNotificationToQueue: notification];
}

-(void) onAddProfileResponse: (SDLAddProfileResponse*) response{
    [self insertResponseToMap:response];    
}

-(void) onAppStateChangedResponse: (SDLAppStateChangedResponse*) response{
    [self insertResponseToMap:response];
}

-(void) onLoadProfileResponse: (SDLLoadProfileResponse*) response{
    [self insertResponseToMap:response];
}

-(void) onRemoveProfileResponse: (SDLLoadProfileResponse*) response{
    [self insertResponseToMap:response];
}

-(void) onUnloadProfileResponse: (SDLUnloadProfileResponse*) response{
    [self insertResponseToMap:response];
}

-(void) onSendAppToProfileMessageResponse: (SDLSendAppToProfileMessageResponse*) response{
    [self insertResponseToMap:response];
}


-(void) insertResponseToMap:(SDLRPCResponse*)message {
    dispatch_async(mMapsQueue, ^{
        int correlationID = [[message correlationID] intValue];
        if (mSemaphoreMap.find(correlationID) == mSemaphoreMap.end()) {
            mSemaphoreMap[correlationID] = dispatch_semaphore_create(0);
        }
        mOtherSideMessages[correlationID] = message;
        dispatch_semaphore_signal(mSemaphoreMap[correlationID]);
    });
}

-(void) insertNotificationToQueue:(SDLRPCNotification *)message {
    dispatch_async(mMapsQueue, ^{
        [mNotifications addObject: message];
        dispatch_semaphore_signal(mNotificationSemaphore);
    });
}

-(bool)loadProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess{
    SDLLoadProfile *req = [SDLRPCRequestFactory buildLoadProfileWithName:profileName
                                                           correlationID:[Test getAndIncCorrelationID]];
    return [self waitForResponse:req viaSender:proxy expectedSuccess:isSuccess];   
}

-(bool)unloadProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess {
    SDLUnloadProfile * unloadReq = [SDLRPCRequestFactory buildUnloadProfileWithName:profileName correlationID:[Test getAndIncCorrelationID]];
    if (isSuccess)
    {
        bool result = [self waitForResponse:unloadReq viaSender:proxy expectedSuccess:true];
        if (!result) {
            return false;
        }
        SDLRPCNotification * noti = [self waitForNotification];
        NSLog(@"got noti: %@", noti);
        NSLog(@"noti class: %@", [noti class]);
        if (![noti isKindOfClass: [SDLOnProfileUnloaded class]]) {
            return false;
        }
        return [[((SDLOnProfileUnloaded*) noti) profileName] isEqual: profileName];
    } else {
        return [self waitForResponse:unloadReq viaSender:proxy expectedSuccess:false];
    }
}


-(bool)sendMessage:(NSData*)message toProfile:(NSString*)profileName  viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess {
    SDLSendAppToProfileMessage * req = [SDLRPCRequestFactory buildSendAppToProfileMessageWithName:profileName
                                                                                          rawData:message
                                                                                    correlationID:[Test getAndIncCorrelationID]];
    return [self waitForResponse:req viaSender:proxy expectedSuccess:isSuccess];
}

-(SDLOnProfileToAppMessage*)waitForMessageFromProfile:(NSString*)profileName {
    SDLRPCNotification * noti = [self waitForNotification];
    if ([noti isKindOfClass: [SDLOnProfileToAppMessage class]]) {
        return (SDLOnProfileToAppMessage*)noti;
    }
    return nil;
}

-(bool)addProfile:(NSString*)filePath viaSender:(id<RequestsSender>)proxy forProfile:(NSString*)profileName expectedSuccess:(bool)isSuccess
{
    NSArray * addProfile = [SDLRPCRequestFactory buildAddProfileForEmbeddedPath:filePath profileName:profileName correlationID:[Test getAndIncCorrelationID]];
    correlationID += [addProfile count];
    for (id request in addProfile) {
        if (![self waitForResponse:request viaSender:proxy expectedSuccess:isSuccess]) {
            return NO;
        }
    }
    return YES;
}

-(bool)removeProfile:(NSString*)profileName viaSender:(id<RequestsSender>)proxy expectedSuccess:(bool)isSuccess {
    SDLRemoveProfile * req = [SDLRPCRequestFactory buildRemoveProfileWithName:profileName correlationID:[Test getAndIncCorrelationID]];
    return [self waitForResponse:req viaSender:proxy expectedSuccess:isSuccess];
}

+(NSNumber*)getAndIncCorrelationID {
    return [NSNumber numberWithInt: correlationID++];
}

@end
