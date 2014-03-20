//  AddCommandViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "AddCommandViewController.h"
#import "AppDelegate.h"

@interface AddCommandViewController ()

@end

@implementation AddCommandViewController


-(IBAction)sendRPC:(id)sender{
  
    NSArray *voiceCommand = nil;
    NSString *menuName = nil;
    NSString *menuNameTable = nil;
    if ([[menuNameText text] isEqualToString:@""]) {
        menuNameTable = [[NSString stringWithFormat:@"[%@]", [addVRText text]] retain];
     
    }
    else {
        menuName = [menuNameText text];
        menuNameTable = menuName;
    }
    
    if (![[addVRText text] isEqualToString:@""]) {
        voiceCommand = [NSArray arrayWithObject:[addVRText text]];
    }
    
    NSNumber *parentID = nil;
    if (![[parentIDText text] isEqualToString:@""]) {
        parentID = [NSNumber numberWithInt:[[parentIDText text] intValue]];
    }
    
    NSNumber *position = nil;
    if (![[positionText text] isEqualToString:@""]) {
        position = [NSNumber numberWithInt:[[positionText text] intValue]];
    }
    
    [commandsIssued addObject:[[[AddMenuOption alloc] initWithMenuName:menuNameTable menuId:[NSNumber numberWithInt:[[SmartDeviceLinkTester getInstance] getCmdID]]] autorelease]];
    
    if (iconControl.selectedSegmentIndex == 0) {
        SDLAddCommand *req = [SDLRPCRequestFactory buildAddCommandWithID:[NSNumber numberWithInt:[[SmartDeviceLinkTester getInstance] getCmdID]] menuName:menuName parentID:parentID position:position vrCommands:voiceCommand iconValue:[iconText text] iconType:nil correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];

    }
    else if (iconControl.selectedSegmentIndex == 1) {
        SDLAddCommand *req = [SDLRPCRequestFactory buildAddCommandWithID:[NSNumber numberWithInt:[[SmartDeviceLinkTester getInstance] getCmdID]] menuName:menuName parentID:parentID position:position vrCommands:voiceCommand iconValue:[iconText text] iconType:[SDLImageType STATIC] correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
        
    }
    else {
        SDLAddCommand *req = [SDLRPCRequestFactory buildAddCommandWithID:[NSNumber numberWithInt:[[SmartDeviceLinkTester getInstance] getCmdID]] menuName:menuName parentID:parentID position:position vrCommands:voiceCommand iconValue:[iconText text] iconType:[SDLImageType DYNAMIC] correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
        
        [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];

    }
    
    [[SmartDeviceLinkTester getInstance] incCmdID];
    
    [menuNameText setText:@""];
    [addVRText setText:@""];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"AddCommand";
    
    commandsIssued = [[NSMutableArray alloc] init];
    
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

- (void)viewDidLoad
{
    [super viewDidLoad];
    menuNameText.delegate = self;
    addVRText.delegate = self;
    parentIDText.delegate = self;
    positionText.delegate = self;
    iconText.delegate = self;
}

-(void) dealloc {
    [commandsIssued release];
    [super dealloc];
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
