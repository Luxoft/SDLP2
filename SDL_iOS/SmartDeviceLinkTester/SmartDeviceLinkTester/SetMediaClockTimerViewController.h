//  SetMediaClockTimerViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SetMediaClockTimerViewController : UIViewController <UITextFieldDelegate> {
    
    IBOutlet UITextField *hoursText;
    IBOutlet UITextField *minutesText;
    IBOutlet UITextField *secondsText;
    
    UIButton *doneButton;
    
    IBOutlet UISegmentedControl *updateControl;
    
}

-(IBAction)sendRPC:(id)sender;

@end
