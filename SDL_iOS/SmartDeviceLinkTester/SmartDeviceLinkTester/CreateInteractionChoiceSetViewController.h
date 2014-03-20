//  CreateInteractionChoiceSetViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"

@interface CreateInteractionChoiceSetViewController : UIViewController <UITextViewDelegate, UITextFieldDelegate> {
    
    IBOutlet UITextView *choiceSetView;
    IBOutlet UITextField *idText;
    
    int choiceID;
    
}

-(IBAction)sendRPC:(id)sender;

@end
