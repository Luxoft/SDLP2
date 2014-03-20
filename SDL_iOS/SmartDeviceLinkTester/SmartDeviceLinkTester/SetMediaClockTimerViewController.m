//  SetMediaClockTimerViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SetMediaClockTimerViewController.h"
#import "AppDelegate.h"

@interface SetMediaClockTimerViewController ()

@end

@implementation SetMediaClockTimerViewController

-(IBAction)sendRPC:(id)sender {
   
    SDLUpdateMode *ua;
    if (updateControl.selectedSegmentIndex == 0) {
        ua = [SDLUpdateMode COUNTUP];
    }
    else if (updateControl.selectedSegmentIndex == 1) {
        ua = [SDLUpdateMode COUNTDOWN];
    }
    else if (updateControl.selectedSegmentIndex == 2) {
        ua = [SDLUpdateMode PAUSE];
    }
    else if (updateControl.selectedSegmentIndex == 3) {
        ua = [SDLUpdateMode RESUME];
    }
    else {
        ua = [SDLUpdateMode CLEAR];
    }
    
    SDLSetMediaClockTimer *req = [SDLRPCRequestFactory buildSetMediaClockTimerWithHours:[NSNumber numberWithInt:[[hoursText text] intValue]] minutes:[NSNumber numberWithInt:[[minutesText text] intValue]] seconds:[NSNumber numberWithInt:[[secondsText text] intValue]] updateMode:ua correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
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
    self.title = @"SetMediaClockTimer";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    hoursText.delegate = self;
    minutesText.delegate = self;
    secondsText.delegate = self;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
