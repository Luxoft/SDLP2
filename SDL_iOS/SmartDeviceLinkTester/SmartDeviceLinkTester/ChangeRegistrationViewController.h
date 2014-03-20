//  ChangeRegistrationViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import "SmartDeviceLinkTester.h"

@interface ChangeRegistrationViewController : UIViewController <UITextFieldDelegate, UIPickerViewDataSource, UIPickerViewDelegate> {
 
    IBOutlet UITextField *langtext;
    IBOutlet UITextField *hmilangtext;
    
    UIPickerView *textPicker;
    NSInteger currentTag;

}

-(IBAction)sendRPC:(id)sender;

@property(nonatomic, retain) NSArray *pickerArray;
@property(nonatomic, retain) NSArray *testArray;

@end
