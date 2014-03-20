//  SpeakViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SpeakViewController : UIViewController <UITextFieldDelegate> {
    
    IBOutlet UITextField *speechField1;
    IBOutlet UITextField *speechField2;
    IBOutlet UITextField *speechField3;
    IBOutlet UITextField *speechField4;
    IBOutlet UISegmentedControl *speechType1;
    IBOutlet UISegmentedControl *speechType2;
    IBOutlet UISegmentedControl *speechType3;
    IBOutlet UISegmentedControl *speechType4;
    
}

-(IBAction)sendRPC:(id)sender;

@end
