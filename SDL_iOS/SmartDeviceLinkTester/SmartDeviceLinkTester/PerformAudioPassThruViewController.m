//  PerformAudioPassThruViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "PerformAudioPassThruViewController.h"
#import "AppDelegate.h"

@interface PerformAudioPassThruViewController ()

@end

@implementation PerformAudioPassThruViewController


-(IBAction)sendRPC:(id)sender {
    
    [[SmartDeviceLinkTester getInstance] buildPerformAudioPassThru:[prompttext text] audioPassThruDisplayText1:[line1text text] audioPassThruDisplayText2:[line2text text] samplingRate:[SDLSamplingRate valueOf:[sampleratetext text]] maxDuration:[NSNumber numberWithInt:[[durtext text] intValue]] bitsPerSample:[SDLBitsPerSample valueOf:[bitspersampletext text]] audioType:[SDLAudioType valueOf:[audiotypetext text]] muteAudio:[NSNumber numberWithBool:[[mutetext text] boolValue]]];
    
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
    self.title = @"PerformAudioPassThru";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    prompttext.delegate = self;
    line1text.delegate = self;
    line2text.delegate = self;
    sampleratetext.delegate = self;
    durtext.delegate = self;
    bitspersampletext.delegate = self;
    audiotypetext.delegate = self;
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
