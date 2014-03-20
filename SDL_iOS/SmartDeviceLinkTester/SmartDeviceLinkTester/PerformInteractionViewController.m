//  PerformInteractionViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "PerformInteractionViewController.h"
#import "AppDelegate.h"

@interface PerformInteractionViewController ()

@end

@implementation PerformInteractionViewController

-(IBAction)sendRPC:(id)sender {
    
    NSArray *tempPrompt = [[initPromptText text] componentsSeparatedByString:@", "];
    NSMutableArray *initialPrompt = [[NSMutableArray alloc] init];
    for (int i = 0; i < [tempPrompt count]; i++) {
        [initialPrompt addObject:[SDLTTSChunkFactory buildTTSChunkForString:[tempPrompt objectAtIndex:i] type:[SDLSpeechCapabilities TEXT]]];
    }
    
    NSArray *tempHelp = [[helpText text] componentsSeparatedByString:@", "];
    NSMutableArray *helpChunks = [[NSMutableArray alloc] init];
    for (int i = 0; i < [tempHelp count]; i++) {
        [helpChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:[tempHelp objectAtIndex:i] type:[SDLSpeechCapabilities TEXT]]];
    }
    
    NSArray *tempTimeout = [[timeoutText text] componentsSeparatedByString:@", "];
    NSMutableArray *timeoutChunks = [[NSMutableArray alloc] init];
    for (int i = 0; i < [tempTimeout count]; i++) {
        [timeoutChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:[tempTimeout objectAtIndex:i] type:[SDLSpeechCapabilities TEXT]]];
    }

    SDLInteractionMode *im;
    if (interactionModeControl.selectedSegmentIndex == 0) {
        im = [SDLInteractionMode MANUAL_ONLY];
    }
    else if (interactionModeControl.selectedSegmentIndex == 1) {
        im = [SDLInteractionMode VR_ONLY];
    }
    else {
        im = [SDLInteractionMode BOTH];
    }
    
    [[SmartDeviceLinkTester getInstance] performInteractionPressedwithInitialPrompt:initialPrompt initialText:[initialText text] interactionChoiceSetIDList:[NSArray arrayWithObject:[NSNumber numberWithInt:[[choiceIDText text] intValue]]] helpChunks:helpChunks timeoutChunks:timeoutChunks interactionMode:im timeout:[NSNumber numberWithDouble:(round([timeoutSlider value])*1000)]];
    
    [initialPrompt release];
    [helpChunks release];
    [timeoutChunks release];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}


-(IBAction)displayTimeoutSlider:(id)sender {
    timeoutLabel.text = [NSString stringWithFormat:@"%g",round([timeoutSlider value])];
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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
        self.title = @"PerformInteraction";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
     
    [super viewDidLoad];

    [self.view addSubview: scrollView]; 
    [scrollView setContentSize:CGSizeMake(320, 1080)];
    [scrollView setUserInteractionEnabled:YES];
    
    initPromptText.delegate = self;
    initialText.delegate = self;
    choiceIDText.delegate = self;
    helpText.delegate = self;
    timeoutText.delegate = self;
    
    [[initPromptText layer] setCornerRadius:10];
    [[helpText layer] setCornerRadius:10];
    [[timeoutText layer] setCornerRadius:10];
    
    timeoutSlider.minimumValue = 5;
    timeoutSlider.maximumValue = 10;
    [timeoutSlider setValue:10];
    timeoutLabel.text = @"10";
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
