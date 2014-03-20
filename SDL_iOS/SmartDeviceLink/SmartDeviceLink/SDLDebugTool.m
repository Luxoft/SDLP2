//  SDLDebugTool.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLDebugTool.h>

#import <SmartDeviceLink/SDLSiphonServer.h>

#define LOG_INFO_ENABLED
#define LOG_ERROR_ENABLED


static NSMutableArray* debugToolConsoleList = nil;

@implementation SDLDebugTool

+(NSMutableArray*) getConsoleList {
	if (debugToolConsoleList == nil) {
		debugToolConsoleList = [[NSMutableArray alloc] initWithCapacity:2];
	}
	return debugToolConsoleList;
}


+(void) addConsole:(NSObject<SDLDebugToolConsole>*) aConsole {
	[[SDLDebugTool getConsoleList] addObject:aConsole];
}

+(void) removeConsole:(NSObject<SDLDebugToolConsole>*) aConsole {
	[[SDLDebugTool getConsoleList] removeObject:aConsole];
}

+(void) logInfo:(NSString*) fmt, ... {
	NSString* toOutRaw = nil;
	
    va_list args;
    va_start(args, fmt);
        
    toOutRaw = [[NSString alloc] initWithFormat:fmt arguments:args];
    
    NSMutableString *toOut = [[NSMutableString alloc] initWithString:@"SDLDebugTool: "];
    [toOut appendString:toOutRaw];
    
    [toOutRaw release];
    
	va_end(args);
	
    [SDLSiphonServer init];
    bool dataLogged = [SDLSiphonServer _siphonNSLogData:toOut];
    
    
#ifdef LOG_INFO_ENABLED
    if(!dataLogged){
        NSLog(@"%@", toOut);
    }
#endif
    
	for (NSObject<SDLDebugToolConsole>* console in debugToolConsoleList) {
		[console logInfo:toOut];
	}
    
    [toOut release];
}

+(void) logException:(NSException*) ex withMessage:(NSString*) fmt, ...  {
	NSString* toOutRaw = nil;
	
	va_list args;
	va_start(args, fmt);

    toOutRaw = [[NSString alloc] initWithFormat:fmt arguments:args];
    
    NSMutableString *toOut = [[NSMutableString alloc] initWithString:@"SDLDebugTool: "];
    [toOut appendString:toOutRaw];
    
    [toOutRaw release];
    
    va_end(args);
    
    [SDLSiphonServer init];
    bool dataLogged = [SDLSiphonServer _siphonNSLogData:toOut];
    if (dataLogged) {
        dataLogged = [SDLSiphonServer _siphonNSLogData:[ex reason]];
    } // end-if
    
	
#ifdef LOG_ERROR_ENABLED
	if (!dataLogged) {
        NSLog(@"%@: %@", toOut, ex);
    }
#endif
	
	for (NSObject<SDLDebugToolConsole>* console in debugToolConsoleList) {
		[console logException:ex withMessage:toOut];
	}
    
    [toOut release];
}

@end
