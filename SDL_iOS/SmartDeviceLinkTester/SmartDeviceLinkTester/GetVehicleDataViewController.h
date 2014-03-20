//  GetVehicleDataViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface GetVehicleDataViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    
    NSMutableArray *vehicleDataList;
    IBOutlet UITableView *vehicleDataTable;
    
}


@end
