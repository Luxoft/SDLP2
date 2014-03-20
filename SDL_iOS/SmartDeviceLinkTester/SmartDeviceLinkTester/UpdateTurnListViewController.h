//  UpdateTurnListViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface UpdateTurnListViewController : UIViewController <UITextViewDelegate> {
 
    IBOutlet UITextView *turnView;
    IBOutlet UITextView *iconView;
    
    SoftButtonListViewController *softButtonListViewController;
}

-(IBAction)sendRPC:(id)sender;
-(IBAction)softButtons:(id)sender;

@end
