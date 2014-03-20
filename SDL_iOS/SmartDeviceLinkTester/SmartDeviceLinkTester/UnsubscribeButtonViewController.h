//  UnsubscribeButtonViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SubscribeButtonViewController.h"

@interface UnsubscribeButtonViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    
    IBOutlet UITableView *buttonTable;
    
}

@end
