//  CreateInteractionChoiceSetViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "CreateInteractionChoiceSetViewController.h"
#import "AppDelegate.h"

@interface CreateInteractionChoiceSetViewController ()

@end

@implementation CreateInteractionChoiceSetViewController

-(IBAction)sendRPC:(id)sender {
    NSString *choicestring = [choiceSetView text];
    NSArray *tempchoices = [choicestring componentsSeparatedByString:@", "];
    
    NSMutableArray *choices = [[NSMutableArray alloc] init];
    for (int i = 0; i < [tempchoices count]; i++) {
        SDLChoice *choice = [[SDLChoice alloc] init];
        choice.menuName = [tempchoices objectAtIndex:i];
        choice.choiceID = [NSNumber numberWithInt: choiceID++];
        choice.vrCommands = [[NSArray arrayWithObject:[tempchoices objectAtIndex:i]] mutableCopy];
        [choices addObject:choice];
        [choice release];
    }
    
    SDLCreateInteractionChoiceSet *req = [SDLRPCRequestFactory buildCreateInteractionChoiceSetWithID:[NSNumber numberWithInt:[[idText text] intValue]] choiceSet:choices correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    
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

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"CreateInteractionChoiceSet";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    choiceSetView.delegate = self;
    idText.delegate = self;
    [[choiceSetView layer] setCornerRadius:10];
    choiceID = 0;
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
