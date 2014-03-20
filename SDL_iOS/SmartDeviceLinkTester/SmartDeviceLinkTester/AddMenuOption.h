//  AddMenuOption.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>

@interface AddMenuOption : NSObject {
    NSString *menuName;
    NSNumber *menuID;
    
}

-(id) initWithMenuName:(NSString *)menuName menuId:(NSNumber *)menuID;

@property (nonatomic, retain) NSString *menuName;
@property (nonatomic, retain) NSNumber *menuID;

@end
