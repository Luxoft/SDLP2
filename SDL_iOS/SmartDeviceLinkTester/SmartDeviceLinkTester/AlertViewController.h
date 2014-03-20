//  AlertViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface AlertViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
    
    IBOutlet UITextView *ttsView;
    IBOutlet UITextField *line1Text;
    IBOutlet UITextField *line2Text;
    IBOutlet UITextField *line3Text;
    IBOutlet UISwitch *toneSwitch;
    IBOutlet UISwitch *sbSwitch;
    
    IBOutlet UILabel *durationLabel;
    IBOutlet UISlider *durationSlider;
    
    SoftButtonListViewController *softButtonListViewController;
    
    NSMutableArray *sbList;
}

-(IBAction)sendRPC:(id)sender;
-(IBAction)displayDurationSlider:(id)sender;
-(IBAction)softButtons:(id)sender;


@end
