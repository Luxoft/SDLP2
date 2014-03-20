//  SetGlobalPropertiesViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SetGlobalPropertiesViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate> {
    
    IBOutlet UITextView *helpText;
    IBOutlet UITextView *timeoutText;
    IBOutlet UITextView *vrhelpText;
    IBOutlet UITextView *vrhelpitemText;
    IBOutlet UITextView *vrhelpitemnumberText;
    IBOutlet UITextView *vrhelpitemimagenumberText;
    
    IBOutlet UISwitch *helpSwitch;
    IBOutlet UISwitch *timeoutSwitch;
    IBOutlet UISwitch *vrhelpSwitch;
    IBOutlet UISwitch *vrhelpitemSwitch;

}

-(IBAction)sendRPC:(id)sender;

@end
