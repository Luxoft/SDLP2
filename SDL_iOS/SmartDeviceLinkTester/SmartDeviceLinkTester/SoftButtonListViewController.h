//  softButtonListViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SoftButtonListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
    
    IBOutlet UITableView *buttonTable;
    
    NSMutableArray *sbList;
    
}

-(IBAction) editTable:(id)sender;

-(void) updateList:(NSMutableArray *) list;


@end
