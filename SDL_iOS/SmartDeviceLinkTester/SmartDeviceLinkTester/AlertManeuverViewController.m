//  AlertManeuverViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "AlertManeuverViewController.h"
#import "AppDelegate.h"

@implementation AlertManeuverViewController

-(IBAction)sendRPC:(id)sender
{
    SDLAlertManeuver *req = [SDLRPCRequestFactory buildAlertManeuverWithTTSChunks:[[ttsView text] componentsSeparatedByString:@", "] softButtons:sbList correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

-(IBAction)softButtons:(id)sender {
    [softButtonListViewController updateList:sbList];
    [self.navigationController pushViewController:softButtonListViewController animated:YES];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"AlertManeuver";
    
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
    
    [[ttsView layer] setCornerRadius:10];
    
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5509];
    sb1.text = @"Reply";
    sb1.type = [SDLSoftButtonType TEXT];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction STEAL_FOCUS];
    
    SDLSoftButton *sb2 = [[[SDLSoftButton alloc] init] autorelease];
    sb2.softButtonID = [NSNumber numberWithInt:5510];
    sb2.text = @"Close";
    sb2.type = [SDLSoftButtonType TEXT];
    sb2.isHighlighted = [NSNumber numberWithBool:false];
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
