//  ShowConstantTBTViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "ShowConstantTBTViewController.h"
#import "AppDelegate.h"

@interface ShowConstantTBTViewController ()

@end

@implementation ShowConstantTBTViewController

NSMutableArray *sbList;

-(IBAction)sendRPC:(id)sender
{    
    SDLShowConstantTBT *req = [SDLRPCRequestFactory buildShowConstantTBTWithNavigationText1:[line1text text] navigationText2:[line2text text] eta:[etatext text] totalDistance:[disttext text] turnIcon:nil distanceToManeuver:[NSNumber numberWithFloat:[[disttotext text] floatValue]] distanceToManeuverScale:[NSNumber numberWithFloat:[[scaletext text] floatValue]] maneuverComplete:[NSNumber numberWithBool:[completeSwitch isOn]] softButtons:sbList correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
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

-(IBAction)softButtons:(id)sender {
    [softButtonListViewController updateList:sbList];
    [self.navigationController pushViewController:softButtonListViewController animated:YES];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"ShowConstantTBT";
    
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
    
    line1text.delegate = self;
    line2text.delegate = self;
    etatext.delegate = self;
    disttext.delegate = self;
    disttotext.delegate = self;
    scaletext.delegate = self;
    
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5507];
    sb1.text = @"Reply";
    sb1.type = [SDLSoftButtonType TEXT];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction STEAL_FOCUS];
    
    SDLSoftButton *sb2 = [[[SDLSoftButton alloc] init] autorelease];
    sb2.softButtonID = [NSNumber numberWithInt:5508];
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
