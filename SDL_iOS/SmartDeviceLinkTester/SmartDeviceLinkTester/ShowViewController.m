//  ShowViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "ShowViewController.h"
#import "AppDelegate.h"

@implementation ShowViewController

-(IBAction)sendRPC:(id)sender {
    
    SDLTextAlignment *ta;
    if (textalignment.selectedSegmentIndex == 0) {
        ta = [SDLTextAlignment LEFT_ALIGNED];
    }
    else if (textalignment.selectedSegmentIndex == 1) {
        ta = [SDLTextAlignment CENTERED];
    }
    else {
        ta = [SDLTextAlignment RIGHT_ALIGNED];
    }
    
    NSString *customstring = [custompresetsview text];
    NSArray *customarray = nil;
    
    if (![customstring isEqualToString:@""]) {
        customarray = [customstring componentsSeparatedByString:@", "];
    }

    
    if (sbSwitch.isOn) {
        SDLShow *req = [SDLRPCRequestFactory buildShowWithMainField1:[line1text text] mainField2:[line2text text] mainField3:[line3text text] mainField4:[line4text text] statusBar:[statusbartext text] mediaClock:[mediaclocktext text] mediaTrack:[mediatracktext text] alignment:ta graphic:nil softButtons:sbList customPresets:customarray correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    }
    else {
        SDLShow *req = [SDLRPCRequestFactory buildShowWithMainField1:[line1text text] mainField2:[line2text text] mainField3:[line3text text] mainField4:[line4text text] statusBar:[statusbartext text] mediaClock:[mediaclocktext text] mediaTrack:[mediatracktext text] alignment:ta graphic:nil softButtons:nil customPresets:customarray correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    }
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
    
}

-(IBAction)softButtons:(id)sender {
    [softButtonListViewController updateList:sbList];
    [self.navigationController pushViewController:softButtonListViewController animated:YES];
}

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
    self.title = @"Show";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        softButtonListViewController = [[SoftButtonListViewController alloc] initWithNibName:@"SoftButtonListViewController" bundle:nil];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    custompresetsview.delegate = self;
    line1text.delegate = self;
    line2text.delegate = self;
    line3text.delegate = self;
    line4text.delegate = self;
    statusbartext.delegate = self;
    mediaclocktext.delegate = self;
    mediatracktext.delegate = self;
    
    [[custompresetsview layer] setCornerRadius:10];
    
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5502];
    sb1.text = @"One";
    sb1.image = [[[SDLImage alloc] init] autorelease];
    sb1.image.imageType = [SDLImageType STATIC];
    sb1.image.value = @"D";
    sb1.type = [SDLSoftButtonType BOTH];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction KEEP_CONTEXT];
    
    SDLSoftButton *sb2 = [[[SDLSoftButton alloc] init] autorelease];
    sb2.softButtonID = [NSNumber numberWithInt:5503];
    sb2.text = @"Two";
    sb2.image = [[[SDLImage alloc] init] autorelease];
    sb2.image.imageType = [SDLImageType STATIC];
    sb2.image.value = @"9";
    sb2.type = [SDLSoftButtonType IMAGE];
    sb2.isHighlighted = [NSNumber numberWithBool:false];
    sb2.systemAction = [SDLSystemAction STEAL_FOCUS];
    
    SDLSoftButton *sb3 = [[[SDLSoftButton alloc] init] autorelease];
    sb3.softButtonID = [NSNumber numberWithInt:5504];
    sb3.text = @"Three";
    sb3.type = [SDLSoftButtonType TEXT];
    sb3.isHighlighted = [NSNumber numberWithBool:true];
    sb3.systemAction = [SDLSystemAction DEFAULT_ACTION];
    
    sbList = [[NSMutableArray alloc] initWithObjects:sb1,sb2,sb3,nil];
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

@end
