//  DeleteCommandViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"
#import "AddCommandViewController.h"
#import "AddMenuOption.h"

@interface DeleteCommandViewController : UIViewController <UITableViewDelegate, UITableViewDataSource> {
    
    IBOutlet UITableView *commandsTable;
    
}


@end
