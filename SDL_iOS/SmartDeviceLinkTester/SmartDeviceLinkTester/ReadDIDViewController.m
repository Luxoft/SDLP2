//  ReadDIDViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "ReadDIDViewController.h"
#import "AppDelegate.h"

@interface ReadDIDViewController ()

@end

@implementation ReadDIDViewController


-(IBAction)sendRPC:(id)sender {
    
    NSArray *tempStringArray = [[didloctext text] componentsSeparatedByString:@" "];

    NSMutableArray *tempNumberArray = [[NSMutableArray alloc] init];
    
    for (NSString *string in tempStringArray) {
        [tempNumberArray addObject:[NSNumber numberWithInt:[string intValue]]];
    }
    
    SDLReadDID *req = [SDLRPCRequestFactory buildReadDIDWithECUName:[NSNumber numberWithInt:[[ecunametext text] intValue]] didLocation:tempNumberArray correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [tempNumberArray release];
    
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
    self.title = @"ReadDID";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    ecunametext.delegate = self;
    didloctext.delegate = self;
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
