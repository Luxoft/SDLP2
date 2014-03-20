//  SetAppIconViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SetAppIconViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *nametext;

}

-(IBAction)sendRPC:(id)sender;

@end
