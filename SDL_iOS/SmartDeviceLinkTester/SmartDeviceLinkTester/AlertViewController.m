//  AlertViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "AlertViewController.h"
#import "AppDelegate.h"

@implementation AlertViewController

-(IBAction)sendRPC:(id)sender {
    
    NSString *ttsstring = [ttsView text];
    NSArray *temp = [ttsstring componentsSeparatedByString:@", "];
    NSMutableArray *ttschunks = [[NSMutableArray alloc] init];
    for (int i = 0; i < [temp count]; i++) {
        [ttschunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:[temp objectAtIndex:i] type:[SDLSpeechCapabilities TEXT]]];
    }
    
    if (sbSwitch.isOn) {
        SDLAlert *req = [SDLRPCRequestFactory buildAlertWithTTSChunks:ttschunks alertText1:[line1Text text] alertText2:[line2Text text] alertText3:[line3Text text] playTone:[NSNumber numberWithBool:[toneSwitch isOn]] duration:[NSNumber numberWithDouble:(round([durationSlider value])*1000)] softButtons:sbList correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    }
    else {
        SDLAlert *req = [SDLRPCRequestFactory buildAlertWithTTSChunks:ttschunks alertText1:[line1Text text] alertText2:[line2Text text] alertText3:[line3Text text] playTone:[NSNumber numberWithBool:[toneSwitch isOn]] duration:[NSNumber numberWithDouble:(round([durationSlider value])*1000)] softButtons:nil correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    }
    
    [ttschunks release];
    
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

-(IBAction)displayDurationSlider:(id)sender {
    durationLabel.text = [NSString stringWithFormat:@"%g",round([durationSlider value])];
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
    self.title = @"Alert";
    
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
    ttsView.delegate = self;
    line1Text.delegate = self;
    line2Text.delegate = self;
    line3Text.delegate = self;
    
    [[ttsView layer] setCornerRadius:10];
    
    durationSlider.minimumValue = 3;
    durationSlider.maximumValue = 10;
    [durationSlider setValue:5];
    durationLabel.text = @"5";
        
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5500];
    sb1.text = @"One";
    sb1.image = [[[SDLImage alloc] init] autorelease];
    sb1.image.imageType = [SDLImageType STATIC];
    sb1.image.value = @"9";
    sb1.type = [SDLSoftButtonType BOTH];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction KEEP_CONTEXT];
    
    SDLSoftButton *sb2 = [[[SDLSoftButton alloc] init] autorelease];
    sb2.softButtonID = [NSNumber numberWithInt:5501];
    sb2.text = @"Two";
    sb2.type = [SDLSoftButtonType TEXT];
    sb2.isHighlighted = [NSNumber numberWithBool:true];
    sb2.systemAction = [SDLSystemAction DEFAULT_ACTION];
    
    sbList = [[NSMutableArray alloc] initWithObjects:sb1,sb2,nil];
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
