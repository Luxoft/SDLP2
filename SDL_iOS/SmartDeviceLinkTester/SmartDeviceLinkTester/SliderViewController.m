//  SliderViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SliderViewController.h"
#import "AppDelegate.h"

@interface SliderViewController ()

@end

@implementation SliderViewController


-(IBAction)sendRPC:(id)sender {
    
    SDLSlider *req = [[[SDLSlider alloc] init] autorelease];
    
    if ([dynamicfooterswitch isOn]){
        req = [SDLRPCRequestFactory buildSliderStaticFooterWithNumTicks:[NSNumber numberWithInt:[[numtickstext text] intValue]] position:[NSNumber numberWithInt:[[positiontext text] intValue]] sliderHeader:[headertext text] sliderFooter:[footertext text] timeout:[NSNumber numberWithInt:[[timeouttext text] intValue]] correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    }
    else {
        //Use static for now
        req = [SDLRPCRequestFactory buildSliderStaticFooterWithNumTicks:[NSNumber numberWithInt:[[numtickstext text] intValue]] position:[NSNumber numberWithInt:[[positiontext text] intValue]] sliderHeader:[headertext text] sliderFooter:[footertext text] timeout:[NSNumber numberWithInt:[[timeouttext text] intValue]] correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    }
    
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
    self.title = @"Slider";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    numtickstext.delegate = self;
    positiontext.delegate = self;
    headertext.delegate = self;
    footertext.delegate = self;
    timeouttext.delegate = self;
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
