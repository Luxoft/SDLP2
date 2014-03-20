//  SDLEnum.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@interface SDLEnum : NSObject {
	NSString* value;
}

-(id) initWithValue:(NSString*) value;

@property(readonly) NSString* value;

@end
