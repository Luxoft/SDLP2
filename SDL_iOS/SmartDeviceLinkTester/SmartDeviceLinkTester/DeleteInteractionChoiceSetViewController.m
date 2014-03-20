//  DeleteInteractionChoiceSetViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "DeleteInteractionChoiceSetViewController.h"
#import "AppDelegate.h"

@interface DeleteInteractionChoiceSetViewController ()

@end

@implementation DeleteInteractionChoiceSetViewController


-(IBAction)sendRPC:(id)sender
{
    SDLDeleteInteractionChoiceSet *req = [SDLRPCRequestFactory buildDeleteInteractionChoiceSetWithID:[NSNumber numberWithInt:[[idText text] intValue]] correlationID: [[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
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
    self.title = @"DeleteInteractionChoiceSet";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    idText.delegate = self;
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
