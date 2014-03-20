//  UpdateTurnListViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "UpdateTurnListViewController.h"
#import "AppDelegate.h"

@interface UpdateTurnListViewController ()

@end

@implementation UpdateTurnListViewController

NSMutableArray *sbList;

-(IBAction)sendRPC:(id)sender {
    
    NSArray *tempTurns = [[turnView text] componentsSeparatedByString:@", "];
    NSArray *tempIcons = [[iconView text] componentsSeparatedByString:@", "];
                                    
    NSMutableArray *tempTurnArray = [[NSMutableArray alloc] init];
    
    int i = 0;
    for (NSString *string in tempTurns) {
        SDLTurn *currentTurn = [[SDLTurn alloc] init];
        SDLImage *currentImage = [[SDLImage alloc] init];
        currentTurn.navigationText = string;
        currentImage.value = [tempIcons objectAtIndex:i];
        currentImage.imageType = [SDLImageType STATIC];
        currentTurn.turnIcon = currentImage;
        [tempTurnArray addObject:currentTurn];
        i++;
        [currentTurn release];
        [currentImage release];
    }
    
    SDLUpdateTurnList *req = [SDLRPCRequestFactory buildUpdateTurnList:tempTurnArray softButtons:sbList correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
    [tempTurnArray release];
    
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

-(IBAction)softButtons:(id)sender {
    [softButtonListViewController updateList:sbList];
    [self.navigationController pushViewController:softButtonListViewController animated:YES];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"UpdateTurnList";
    
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
    
    turnView.delegate = self;
    iconView.delegate = self;
    
    [[turnView layer] setCornerRadius:10];
    [[iconView layer] setCornerRadius:10];
    
    //Example Soft Buttons
    SDLSoftButton *sb1 = [[[SDLSoftButton alloc] init] autorelease];
    sb1.softButtonID = [NSNumber numberWithInt:5511];
    sb1.text = @"Close";
    sb1.type = [SDLSoftButtonType TEXT];
    sb1.isHighlighted = [NSNumber numberWithBool:false];
    sb1.systemAction = [SDLSystemAction DEFAULT_ACTION];
    
    sbList = [[NSMutableArray alloc] initWithObjects:sb1,nil];
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
