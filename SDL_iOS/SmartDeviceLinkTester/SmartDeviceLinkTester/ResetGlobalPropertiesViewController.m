//  ResetGlobalPropertiesViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "ResetGlobalPropertiesViewController.h"
#import "AppDelegate.h"

@interface ResetGlobalPropertiesViewController ()

@end

@implementation ResetGlobalPropertiesViewController


-(IBAction)sendRPC:(id)sender
{
    UIButton *tempButton = (UIButton *)sender;
    SDLGlobalProperty *globalProperty;
    if ([tempButton.titleLabel.text isEqualToString:@"Reset Help Prompt"]) {
        globalProperty = [SDLGlobalProperty HELPPROMPT];
    }
    else if ([tempButton.titleLabel.text isEqualToString:@"Reset Timeout Prompt"]) {
        globalProperty = [SDLGlobalProperty TIMEOUTPROMPT];
    }
    else if ([tempButton.titleLabel.text isEqualToString:@"VR Help Title"]) {
        globalProperty = [SDLGlobalProperty VRHELPTITLE];
    }
    else {
        globalProperty = [SDLGlobalProperty VRHELPITEMS];
    }
    
    SDLResetGlobalProperties *req = [SDLRPCRequestFactory buildResetGlobalPropertiesWithProperties:[NSArray arrayWithObject:globalProperty] correlationID: [[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"ResetGlobalProperties";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
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
