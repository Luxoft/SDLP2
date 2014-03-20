//  PerformInteractionViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"

@interface PerformInteractionViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
    
    IBOutlet UITextView *initPromptText;
    IBOutlet UITextField *initialText;
    IBOutlet UITextField *choiceIDText;
    IBOutlet UITextView *helpText;
    IBOutlet UITextView *timeoutText;
    
    IBOutlet UISegmentedControl *interactionModeControl;
    
    IBOutlet UILabel *timeoutLabel;
    IBOutlet UISlider *timeoutSlider;
    
    IBOutlet UIScrollView *scrollView;
    
}

-(IBAction)displayTimeoutSlider:(id)sender;
-(IBAction)sendRPC:(id)sender;

@end
