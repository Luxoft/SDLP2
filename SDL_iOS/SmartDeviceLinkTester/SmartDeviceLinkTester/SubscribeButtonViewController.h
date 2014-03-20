//  SubscribeButtonViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"

@interface SubscribeButtonViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    
    NSMutableArray *buttonList;
    IBOutlet UITableView *buttonTable;
    
}

@end

NSMutableArray *selectedButtons;