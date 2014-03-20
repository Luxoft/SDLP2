//  ReadDIDViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

#import "SmartDeviceLinkTester.h"

@interface ReadDIDViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *ecunametext;
    IBOutlet UITextField *didloctext;
    
}

-(IBAction)sendRPC:(id)sender;

@end
