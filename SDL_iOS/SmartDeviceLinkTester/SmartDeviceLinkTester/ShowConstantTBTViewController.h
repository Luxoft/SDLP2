//  ShowConstantTBTViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface ShowConstantTBTViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *line1text;
    IBOutlet UITextField *line2text;
    IBOutlet UITextField *etatext;
    IBOutlet UITextField *disttext;
    IBOutlet UITextField *disttotext;
    IBOutlet UITextField *scaletext;
    IBOutlet UISwitch *completeSwitch;
    
    SoftButtonListViewController *softButtonListViewController;
    
}

-(IBAction)sendRPC:(id)sender;
-(IBAction)softButtons:(id)sender;

@end
