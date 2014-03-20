//  ChangeRegistrationViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#define tagLangText 100
#define tagHMILangText 101

#import "ChangeRegistrationViewController.h"
#import "AppDelegate.h"

@interface ChangeRegistrationViewController ()

@end

@implementation ChangeRegistrationViewController

@synthesize pickerArray;

-(IBAction)sendRPC:(id)sender {
    
    SDLChangeRegistration *req = [SDLRPCRequestFactory buildChangeRegistrationWithLanguage:[SDLLanguage valueOf:[langtext text]] hmiDisplayLanguage:[SDLLanguage valueOf:[hmilangtext text]] correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"ChangeRegistration";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    langtext.delegate = self;
    hmilangtext.delegate = self;
    
    textPicker = [[UIPickerView alloc] init];
    textPicker.dataSource = self;
    textPicker.delegate = self;
    textPicker.showsSelectionIndicator = YES;
    
    langtext.inputView = textPicker;
    langtext.inputAccessoryView = [self doneBar];
    
    hmilangtext.inputView = textPicker;
    hmilangtext.inputAccessoryView = [self doneBar];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    currentTag = textField.tag;
    
    switch (currentTag) {
        case tagLangText:
        {
            self.pickerArray = @[@"EN-US",
                                 @"ES-MX",
                                 @"FR-CA",
                                 @"DE-DE",
                                 @"ES-ES",
                                 @"EN-GB",
                                 @"RU-RU",
                                 @"TR-TR",
                                 @"PL-PL",
                                 @"FR-FR",
                                 @"IT-IT",
                                 @"SV-SE",
                                 @"PT-PT",
                                 @"NL-NL",
                                 @"EN-AU",
                                 @"ZH-CN",
                                 @"ZH-TW",
                                 @"JA-JP",
                                 @"AR-SA",
                                 @"KO-KR",
                                 @"PT-BR",
                                 @"CS-CZ",
                                 @"DA-DK",
                                 @"NO-NO"];
            [textPicker reloadAllComponents];
            break;
        }
        case tagHMILangText:
        {
            self.pickerArray = @[@"EN-US",
                                 @"ES-MX",
                                 @"FR-CA",
                                 @"DE-DE",
                                 @"ES-ES",
                                 @"EN-GB",
                                 @"RU-RU",
                                 @"TR-TR",
                                 @"PL-PL",
                                 @"FR-FR",
                                 @"IT-IT",
                                 @"SV-SE",
                                 @"PT-PT",
                                 @"NL-NL",
                                 @"EN-AU",
                                 @"ZH-CN",
                                 @"ZH-TW",
                                 @"JA-JP",
                                 @"AR-SA",
                                 @"KO-KR",
                                 @"PT-BR",
                                 @"CS-CZ",
                                 @"DA-DK",
                                 @"NO-NO"];
            [textPicker reloadAllComponents];
            break;
        }
        default:
            break;
    }
}


#pragma mark - UIPickerViewDataSource

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return [self.pickerArray count];
}

#pragma mark - UIPickerViewDelegate

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    return [pickerArray objectAtIndex:row];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    
    switch (currentTag) {
        case tagLangText:
        {
            langtext.text = [textPicker.delegate pickerView:pickerView titleForRow:row forComponent:component];
            break;
        }
        case tagHMILangText:
        {
            hmilangtext.text = [textPicker.delegate pickerView:pickerView titleForRow:row forComponent:component];
            break;
        }
        default:
            break;
    }
}

#pragma mark - Miscellaneous

- (UIToolbar*)doneBar {
    UIToolbar *toolBar = [[UIToolbar alloc] init];
    toolBar.barStyle = UIBarStyleBlackTranslucent;
    [toolBar sizeToFit];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc]
                                          initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                          target:nil
                                          action:nil];
    
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"
                                                                   style:UIBarButtonItemStyleBordered
                                                                  target:self
                                                                  action:@selector(doneButtonPressed)];
    
    [toolBar setItems:[NSArray arrayWithObjects:flexibleSpaceLeft, doneButton, nil]];
    
    return toolBar;
}

- (void) doneButtonPressed {
    [self.view endEditing:YES];
}

@end
