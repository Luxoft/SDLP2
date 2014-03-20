//  AddCommandViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "AddMenuOption.h"

@interface AddCommandViewController : UIViewController <UITextFieldDelegate> {
    
    IBOutlet UITextField *menuNameText;
    IBOutlet UITextField *addVRText;
    IBOutlet UITextField *parentIDText;
    IBOutlet UITextField *positionText;
    IBOutlet UITextField *iconText;
    
    IBOutlet UISegmentedControl *iconControl;

}

-(IBAction)sendRPC:(id)sender;

@end

NSMutableArray *commandsIssued;