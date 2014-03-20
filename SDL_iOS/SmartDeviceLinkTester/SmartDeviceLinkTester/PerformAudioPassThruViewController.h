//  PerformAudioPassThruViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface PerformAudioPassThruViewController : UIViewController <UITextFieldDelegate> {
 
    IBOutlet UITextField *prompttext;
    IBOutlet UITextField *line1text;
    IBOutlet UITextField *line2text;
    IBOutlet UITextField *sampleratetext;
    IBOutlet UITextField *durtext;
    IBOutlet UITextField *bitspersampletext;
    IBOutlet UITextField *audiotypetext;
    IBOutlet UITextField *mutetext;
    
}

-(IBAction)sendRPC:(id)sender;

@end
