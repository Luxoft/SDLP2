//  GetDTCsViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface GetDTCsViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *ecunametext;
    
}

-(IBAction)sendRPC:(id)sender;

@end
