//  UnsubscribeVehicleDataViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SubscribeVehicleDataViewController.h"

@interface UnsubscribeVehicleDataViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    
    IBOutlet UITableView *vehicleDataTable;
    
}

@end
