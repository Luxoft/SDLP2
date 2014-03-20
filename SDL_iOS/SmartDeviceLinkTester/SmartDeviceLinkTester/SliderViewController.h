//  SliderViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface SliderViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *numtickstext;
    IBOutlet UITextField *positiontext;
    IBOutlet UITextField *headertext;
    IBOutlet UITextField *footertext;
    IBOutlet UISwitch *dynamicfooterswitch;
    IBOutlet UITextField *timeouttext;
    
}

-(IBAction)sendRPC:(id)sender;

@end
