//  ScrollableMessageViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface ScrollableMessageViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
 
    IBOutlet UITextView *messageview;
    IBOutlet UITextField *timeouttext;
    IBOutlet UISwitch *sbSwitch;
    
    SoftButtonListViewController *softButtonListViewController;
}

-(IBAction)sendRPC:(id)sender;
-(IBAction)softButtons:(id)sender;

@end
