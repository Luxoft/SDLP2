//  SoftButtonEditViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#define tagTypeText 100
#define tagImageTypeText 101
#define tagSystemActionText 102

#import "SoftButtonEditViewController.h"

@implementation SoftButtonEditViewController

@synthesize softButton;
@synthesize pickerArray;


- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"Edit";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    sbIDText.delegate = self;
    textText.delegate = self;
    imageText.delegate = self;
    systemActionText.delegate = self;
    
    sbIDText.text = [softButton.softButtonID stringValue];
    textText.text = softButton.text;
    imageText.text = softButton.image.value;
    isHighlightedSwitch.on = [softButton.isHighlighted boolValue];
    systemActionText.text = [softButton.systemAction value];
    
    textPicker = [[UIPickerView alloc] init];
    textPicker.dataSource = self;
    textPicker.delegate = self;
    textPicker.showsSelectionIndicator = YES;
    
    systemActionText.inputView = textPicker;
    systemActionText.inputAccessoryView = [self doneBar];
    
    if ([[softButton.type value] isEqualToString:@"TEXT"]) {
        buttonType.selectedSegmentIndex = 0;
    }
    else if ([[softButton.type value] isEqualToString:@"IMAGE"]) {
        buttonType.selectedSegmentIndex = 1;
    }
    else {
        buttonType.selectedSegmentIndex = 2;
    }
    
    if ([[[softButton.image imageType] value] isEqualToString:@"STATIC"]) {
        imageType.selectedSegmentIndex = 1;
    }
    else if ([[[softButton.image imageType] value] isEqualToString:@"DYNAMIC"]) {
        imageType.selectedSegmentIndex = 2;
    }
    else {
        imageType.selectedSegmentIndex = 0;
    }
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    
    currentTag = textField.tag;
        
    switch (currentTag) {        
        case tagSystemActionText:
        {
            self.pickerArray = @[@"DEFAULT_ACTION", @"STEAL_FOCUS", @"KEEP_CONTEXT"];
            [textPicker reloadAllComponents];
            [textPicker selectRow:[self.pickerArray indexOfObject:systemActionText.text] inComponent:0 animated:YES];
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
        case tagSystemActionText:
        {
            systemActionText.text = [textPicker.delegate pickerView:pickerView titleForRow:row forComponent:component];
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

-(void)viewDidDisappear:(BOOL)animated
{
    softButton.softButtonID = [NSNumber numberWithInt:[sbIDText.text intValue]];
    softButton.text = textText.text;
    softButton.isHighlighted = [NSNumber numberWithBool:isHighlightedSwitch.isOn];
    softButton.systemAction = [SDLSystemAction valueOf:systemActionText.text];
    
    if (imageType.selectedSegmentIndex == 0) {
        softButton.image = nil;
    }
    else if (imageType.selectedSegmentIndex == 1) {
        softButton.image = [[[SDLImage alloc] init] autorelease];
        softButton.image.imageType = [SDLImageType STATIC];
        softButton.image.value = imageText.text;
    }
    else {
        softButton.image = [[[SDLImage alloc] init] autorelease];
        softButton.image.imageType = [SDLImageType DYNAMIC];
        softButton.image.value = imageText.text;
    }
    
    if (buttonType.selectedSegmentIndex == 0) {
        softButton.type = [SDLSoftButtonType TEXT];
    }
    else if (buttonType.selectedSegmentIndex == 1) {
        softButton.type = [SDLSoftButtonType IMAGE];
    }
    else {
        softButton.type = [SDLSoftButtonType BOTH];
    }
    
    
        
    
    
    
}

@end


