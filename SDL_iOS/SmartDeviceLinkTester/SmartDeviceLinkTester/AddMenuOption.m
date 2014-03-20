//  AddMenuOption.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "AddMenuOption.h"

@implementation AddMenuOption
@synthesize menuName;
@synthesize menuID;

-(id) initWithMenuName:(NSString *)mName menuId:(NSNumber *)mID {
    
    self = [super init];
    if (self) {
        menuName = mName;
        menuID = mID;
    }
    return self;
}


@end
