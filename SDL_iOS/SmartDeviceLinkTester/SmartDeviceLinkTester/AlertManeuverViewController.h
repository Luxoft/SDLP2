//  AlertManeuverViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "SoftButtonListViewController.h"

@interface AlertManeuverViewController : UIViewController <UITextViewDelegate> {
 
    IBOutlet UITextView *ttsView;
    
    SoftButtonListViewController *softButtonListViewController;
    
    NSMutableArray *sbList;

}

-(IBAction)sendRPC:(id)sender;
-(IBAction)softButtons:(id)sender;

@end
