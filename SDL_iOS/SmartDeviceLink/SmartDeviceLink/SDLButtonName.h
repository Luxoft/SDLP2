//  SDLButtonName.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLButtonName : SDLEnum {}

+(SDLButtonName*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLButtonName*) OK;
+(SDLButtonName*) SEEKLEFT;
+(SDLButtonName*) SEEKRIGHT;
+(SDLButtonName*) TUNEUP;
+(SDLButtonName*) TUNEDOWN;
+(SDLButtonName*) PRESET_0;
+(SDLButtonName*) PRESET_1;
+(SDLButtonName*) PRESET_2;
+(SDLButtonName*) PRESET_3;
+(SDLButtonName*) PRESET_4;
+(SDLButtonName*) PRESET_5;
+(SDLButtonName*) PRESET_6;
+(SDLButtonName*) PRESET_7;
+(SDLButtonName*) PRESET_8;
+(SDLButtonName*) PRESET_9;
+(SDLButtonName*) CUSTOM_BUTTON;

@end
