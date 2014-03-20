//  SDLTextFieldName.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLTextFieldName : SDLEnum {}

+(SDLTextFieldName*) valueOf:(NSString*) value;
+(NSMutableArray*) values;

+(SDLTextFieldName*) mainField1;
+(SDLTextFieldName*) mainField2;
+(SDLTextFieldName*) mainField3;
+(SDLTextFieldName*) mainField4;
+(SDLTextFieldName*) statusBar;
+(SDLTextFieldName*) mediaClock;
+(SDLTextFieldName*) mediaTrack;
+(SDLTextFieldName*) alertText1;
+(SDLTextFieldName*) alertText2;
+(SDLTextFieldName*) alertText3;
+(SDLTextFieldName*) scrollableMessageBody;
+(SDLTextFieldName*) initialInteractionText;
+(SDLTextFieldName*) navigationText1;
+(SDLTextFieldName*) navigationText2;
+(SDLTextFieldName*) ETA;
+(SDLTextFieldName*) totalDistance;
+(SDLTextFieldName*) audioPassThruDisplayText1;
+(SDLTextFieldName*) audioPassThruDisplayText2;
+(SDLTextFieldName*) sliderHeader;
+(SDLTextFieldName*) sliderFooter;

@end
