//  ScrollableMessageViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "ScrollableMessageViewController.h"
#import "AppDelegate.h"

@interface ScrollableMessageViewController ()

@end

@implementation ScrollableMessageViewController

NSMutableArray *sbList;

-(IBAction)sendRPC:(id)sender
{
    SDLScrollableMessage *req = [[[SDLScrollableMessage alloc] init] autorelease];
    
    if (sbSwitch.isOn) {
        req = [SDLRPCRequestFactory buildScrollableMessage:[messageview text] timeout:[NSNumber numberWithInt:[[timeouttext text] intValue]] softButtons:sbList correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    }
    else {
        req = [SDLRPCRequestFactory buildScrollableMessage:[messageview text] timeout:[NSNumber numberWithInt:[[timeouttext text] intValue]] softButtons:nil correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    }
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
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
    self.title = @"ScrollableMessage";
    
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
    messageview.delegate = self;
    timeouttext.delegate = self;
    
    [[messageview layer] setCornerRadius:10];
    
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5505];
    sb1.text = @"Reply";
    sb1.type = [SDLSoftButtonType TEXT];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction STEAL_FOCUS];
    
    SDLSoftButton *sb2 = [[[SDLSoftButton alloc] init] autorelease];
    sb2.softButtonID = [NSNumber numberWithInt:5506];
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
