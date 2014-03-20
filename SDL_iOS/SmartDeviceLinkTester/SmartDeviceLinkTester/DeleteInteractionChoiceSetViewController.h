//  DeleteInteractionChoiceSetViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface DeleteInteractionChoiceSetViewController : UIViewController <UITextFieldDelegate> {
    
    IBOutlet UITextField *idText;
    
}

-(IBAction)sendRPC:(id)sender;

@end
