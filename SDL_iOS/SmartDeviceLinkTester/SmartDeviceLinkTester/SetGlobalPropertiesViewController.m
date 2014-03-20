//  SetGlobalPropertiesViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SetGlobalPropertiesViewController.h"
#import "AppDelegate.h"

@interface SetGlobalPropertiesViewController ()

@end

@implementation SetGlobalPropertiesViewController


-(IBAction)sendRPC:(id)sender {
    
    NSString* strHelpText = [NSString stringWithString:[helpText text]];
    NSString* strTimeoutText = [NSString stringWithString:[timeoutText text]];
    NSString* strVRHelpText = [NSString stringWithString:[vrhelpText text]];
    
    SDLVrHelpItem *vrHelpItem = [[[SDLVrHelpItem alloc] init] autorelease];
    SDLImage *image = [[[SDLImage alloc] init] autorelease];
    image.imageType = [SDLImageType STATIC];
    image.value = [vrhelpitemimagenumberText text];
    vrHelpItem.image = image;
    vrHelpItem.text = [vrhelpitemText text];
    vrHelpItem.position = [NSNumber numberWithInt:[[vrhelpitemnumberText text] intValue]];
    
    
    NSArray* arrVRHelpItemText = [[NSMutableArray alloc] initWithObjects:vrHelpItem, nil];
    
    if(![helpSwitch isOn])
        strHelpText = nil;
        
    if(![timeoutSwitch isOn])
        strTimeoutText  = nil;
    
    if(![vrhelpSwitch isOn])
        strVRHelpText = nil;
    
    if(![vrhelpitemSwitch isOn])
        arrVRHelpItemText = nil;
    
    SDLSetGlobalProperties *req = [SDLRPCRequestFactory buildSetGlobalPropertiesWithHelpText:strHelpText timeoutText:strTimeoutText vrHelpTitle:strVRHelpText vrHelp:arrVRHelpItemText correlationID: [[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"SetGlobalProperties";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
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

- (void)viewDidLoad
{
    [super viewDidLoad];
    helpText.delegate = self;
    timeoutText.delegate = self;
    vrhelpText.delegate = self;
    vrhelpitemText.delegate = self;
    
    [[helpText layer] setCornerRadius:10];
    [[timeoutText layer] setCornerRadius:10];
    [[vrhelpText layer] setCornerRadius:10];
    [[vrhelpitemText layer] setCornerRadius:10];
    [[vrhelpitemnumberText layer] setCornerRadius:10];
    [[vrhelpitemimagenumberText layer] setCornerRadius:10];    
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
