//  SoftButtonEditViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

@interface SoftButtonEditViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate, UIPickerViewDataSource, UIPickerViewDelegate> {
    
    IBOutlet UITextField *sbIDText;
    IBOutlet UITextField *textText;
    IBOutlet UITextField *imageText;
    IBOutlet UISwitch *isHighlightedSwitch;
    IBOutlet UITextField *systemActionText;
    
    IBOutlet UISegmentedControl *buttonType;
    IBOutlet UISegmentedControl *imageType;
    
    UIPickerView *textPicker;
    NSInteger currentTag;
    
}


@property(nonatomic, retain) SDLSoftButton *softButton;
@property(nonatomic, retain) NSArray *pickerArray;

@end