//  SDLFunctionID.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@interface SDLFunctionID : NSObject {

    NSDictionary* functionIDs;
}

-(NSString*) getFunctionName:(int) functionID;
-(NSNumber*) getFunctionID:(NSString*) functionName;

@end
