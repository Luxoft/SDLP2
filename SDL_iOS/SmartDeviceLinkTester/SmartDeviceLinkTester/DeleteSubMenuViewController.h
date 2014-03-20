//  DeleteSubMenuViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "AddSubMenuViewController.h"
#import "AddMenuOption.h"

@interface DeleteSubMenuViewController : UIViewController <UITableViewDelegate, UITableViewDataSource> {
    
    IBOutlet UITableView *subMenuTable;
    
}



@end
