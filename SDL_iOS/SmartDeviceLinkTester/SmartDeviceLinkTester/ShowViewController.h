//  ShowViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface ShowViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
 
    IBOutlet UITextField *line1text;
    IBOutlet UITextField *line2text;
    IBOutlet UITextField *line3text;
    IBOutlet UITextField *line4text;
    IBOutlet UITextField *statusbartext;
    IBOutlet UITextField *mediaclocktext;
    IBOutlet UITextField *mediatracktext;
    IBOutlet UITextView *custompresetsview;
    IBOutlet UISwitch *sbSwitch;
    
    IBOutlet UISegmentedControl *textalignment;
    
    SoftButtonListViewController *softButtonListViewController;
    
    NSMutableArray *sbList;
}

-(IBAction)sendRPC:(id)sender;
-(IBAction)softButtons:(id)sender;

@end
