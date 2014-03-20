//  SDLDebugTool.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@protocol SDLDebugToolConsole

-(void) logInfo:(NSString*) info;
-(void) logException:(NSException*) ex withMessage:(NSString*) message;

@end

@interface SDLDebugTool : NSObject {}

+(void) addConsole:(NSObject<SDLDebugToolConsole>*) aConsole;
+(void) removeConsole:(NSObject<SDLDebugToolConsole>*) aConsole;
+(void) logInfo:(NSString*) fmt, ... ;
+(void) logException:(NSException*) ex withMessage:(NSString*) fmt, ... ;

@end
