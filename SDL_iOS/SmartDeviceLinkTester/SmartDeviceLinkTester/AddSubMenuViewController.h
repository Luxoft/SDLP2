//  AddSubMenuViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"
#import "AddMenuOption.h"

@interface AddSubMenuViewController : UIViewController <UITextFieldDelegate> {
    
    IBOutlet UITextField *menuNameText;
    IBOutlet UITextField *menuIDText;
    
    IBOutlet UILabel *positionLabel;
    IBOutlet UISlider *positionSlider;
}

-(IBAction)displayPositionSlider:(id)sender;
-(IBAction)sendRPC:(id)sender;

@end

NSMutableArray *subMenuIssued;