//  SpeakViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SpeakViewController.h"
#import "AppDelegate.h"

@interface SpeakViewController ()

@end

@implementation SpeakViewController

-(IBAction)sendRPC:(id)sender {

    NSArray *segConArray = [NSArray arrayWithObjects:speechType1, speechType2, speechType3, speechType4, nil];
    NSMutableArray *capArray = [[NSMutableArray alloc] init];
    
    NSUInteger counter = 0;

    for (UISegmentedControl* currentSegCon in segConArray)
    {
        if (currentSegCon.selectedSegmentIndex == 0) {
            capArray[counter] = [SDLSpeechCapabilities TEXT];
        }
        else if (currentSegCon.selectedSegmentIndex == 1) {
            capArray[counter] = [SDLSpeechCapabilities SAPI_PHONEMES];
        }
        else if (currentSegCon.selectedSegmentIndex == 2) {
            capArray[counter] = [SDLSpeechCapabilities LHPLUS_PHONEMES];
        }
        else if (currentSegCon.selectedSegmentIndex == 3) {
            capArray[counter] = [SDLSpeechCapabilities PRE_RECORDED];
        }
        else {
            capArray[counter] = [SDLSpeechCapabilities SILENCE];
        }
        counter++;
    }
    
    NSMutableArray *ttsChunks = [[NSMutableArray alloc] init];
    
    if (speechField1.text && speechField1.text.length > 0) {
        [ttsChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:speechField1.text type:capArray[0]]];
    }
    if (speechField2.text && speechField2.text.length > 0) {
        [ttsChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:speechField2.text type:capArray[1]]];
    }
    if (speechField3.text && speechField3.text.length > 0) {
        [ttsChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:speechField3.text type:capArray[2]]];
    }
    if (speechField4.text && speechField4.text.length > 0) {
        [ttsChunks addObject:[SDLTTSChunkFactory buildTTSChunkForString:speechField4.text type:capArray[3]]];
    }
    
    SDLSpeak* req = [SDLRPCRequestFactory buildSpeakWithTTSChunks:ttsChunks correlationID:[[SmartDeviceLinkTester getInstance] getNextCorrID]];
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
    
    [capArray release];
    [ttsChunks release];
    
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"Speak";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    speechField1.delegate = self;
    speechField2.delegate = self;
    speechField3.delegate = self;
    speechField4.delegate = self;
    
    NSArray* titlearray = [NSArray arrayWithObjects:@"Text", @"SAPI", @"LH+", @"PreRec", @"Silence", nil];
    
    NSUInteger segmentCounter = 0;
    for (NSString* title in titlearray)
    {
        [speechType1 setTitle:title forSegmentAtIndex:segmentCounter];
        [speechType2 setTitle:title forSegmentAtIndex:segmentCounter];
        [speechType3 setTitle:title forSegmentAtIndex:segmentCounter];
        [speechType4 setTitle:title forSegmentAtIndex:segmentCounter];
        segmentCounter++;
    }
    
}

- (void)viewDidUnload
{
    [speechType1 release];
    speechType1 = nil;
    [speechField1 release];
    speechField1 = nil;
    [speechField2 release];
    speechField2 = nil;
    [speechType2 release];
    speechType2 = nil;
    [speechField3 release];
    speechField3 = nil;
    [speechType3 release];
    speechType3 = nil;
    [speechField4 release];
    speechField4 = nil;
    [speechType4 release];
    speechType4 = nil;
    [super viewDidUnload];
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)dealloc {
    [speechField1 release];
    [speechType1 release];
    [speechField2 release];
    [speechType2 release];
    [speechField3 release];
    [speechType3 release];
    [speechField4 release];
    [speechType4 release];
    [super dealloc];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

@end
