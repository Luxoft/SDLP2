//  RPCTestViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <AVFoundation/AVFoundation.h>

#import "RPCTestViewController.h"
#import "AppDelegate.h"


@interface RPCTestViewController()
{
    NSMutableDictionary * pmViewControllers;
}
@property (nonatomic, strong) AVAudioPlayer *audioPlayer;
@property (nonatomic, assign) BOOL isMusicPlaying;

@end


@implementation RPCTestViewController

@synthesize isMusicPlaying;

- (id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {
        pmViewControllers = [[NSMutableDictionary alloc] initWithCapacity:6];
        NSArray *tempArray = [NSArray arrayWithObjects:
                              @"AddProfile",
                              @"RemoveProfile",
                              @"LoadProfile",
                              @"UnloadProfile",
                              @"SendAppToProfileMessage",
                              @"AppStateChanged",
                              @"AddCommand",
                              @"AddSubMenu",
                              @"Alert",
                              @"AlertManeuver",
                              @"ChangeRegistration",
                              @"CreateInteractionChoiceSet",
                              @"DeleteCommand",
                              @"DeleteInteractionChoiceSet",
                              @"DeleteSubMenu",
                              @"EndAudioPassThru",
                              @"GetDTCs",
                              @"GetVehicleData",
                              @"PerformAudioPassThru",
                              @"PerformInteraction",
                              @"ReadDID",
                              @"ResetGlobalProperties",
                              @"SetAppIcon",
                              @"SetGlobalProperties",
                              @"SetMediaClockTimer",
                              @"ScrollableMessage",
                              @"Slider",
                              @"Speak",
                              @"Show",
                              @"ShowConstantTBT",
                              @"SubscribeButton",
                              @"SubscribeVehicleData",
                              @"UpdateTurnList",
                              @"UnregisterAppInterface",
                              @"UnsubscribeButton",
                              @"UnsubscribeVehicleData",
                              @"~CycleProxy~",
                              @"~GetProxyVersion~",
                              @"~Play/Pause Audio~",
                              nil];
        
        rpclist = [[NSMutableArray alloc] init];
        [rpclist addObjectsFromArray:tempArray];
        
        
        alertvc = [[AlertViewController alloc] initWithNibName:@"AlertViewController" bundle:nil];
        speakvc = [[SpeakViewController alloc] initWithNibName:@"SpeakViewController" bundle:nil];
        showvc = [[ShowViewController alloc] initWithNibName:@"ShowViewController" bundle:nil];
        subscribebuttonvc = [[SubscribeButtonViewController alloc] initWithNibName:@"SubscribeButtonViewController" bundle:nil];
        unsubscribebuttonvc = [[UnsubscribeButtonViewController alloc] initWithNibName:@"UnsubscribeButtonViewController" bundle:nil];
        addcommandvc = [[AddCommandViewController alloc] initWithNibName:@"AddCommandViewController" bundle:nil];
        deletecommandvc = [[DeleteCommandViewController alloc] initWithNibName:@"DeleteCommandViewController" bundle:nil];
        addsubmenuvc = [[AddSubMenuViewController alloc] initWithNibName:@"AddSubMenuViewController" bundle:nil];
        deletesubmenuvc = [[DeleteSubMenuViewController alloc] initWithNibName:@"DeleteSubMenuViewController" bundle:nil];
        setglobalpropertiesvc = [[SetGlobalPropertiesViewController alloc] initWithNibName:@"SetGlobalPropertiesViewController" bundle:nil];
        resetglobalpropertiesvc = [[ResetGlobalPropertiesViewController alloc] initWithNibName:@"ResetGlobalPropertiesViewController" bundle:nil];
        setmediaclocktimervc = [[SetMediaClockTimerViewController alloc] initWithNibName:@"SetMediaClockTimerViewController" bundle:nil];
        createinteractionchoicesetvc = [[CreateInteractionChoiceSetViewController alloc] initWithNibName:@"CreateInteractionChoiceSetViewController" bundle:nil];
        deleteinteractionchoicesetvc = [[DeleteInteractionChoiceSetViewController alloc] initWithNibName:@"DeleteInteractionChoiceSetViewController" bundle:nil];
        performinteractionvc = [[PerformInteractionViewController alloc] initWithNibName:@"PerformInteractionViewController" bundle:nil];
        slidervc = [[SliderViewController alloc] initWithNibName:@"SliderViewController" bundle:nil];
        scrollablemessagevc = [[ScrollableMessageViewController alloc] initWithNibName:@"ScrollableMessageViewController" bundle:nil];
        changeregistrationvc = [[ChangeRegistrationViewController alloc] initWithNibName:@"ChangeRegistrationViewController" bundle:nil];
        setappiconvc = [[SetAppIconViewController alloc] initWithNibName:@"SetAppIconViewController" bundle:nil];
        performaudiopassthruvc = [[PerformAudioPassThruViewController alloc] initWithNibName:@"PerformAudioPassThruViewController" bundle:nil];
        endaudiopassthruvc = [[EndAudioPassThruViewController alloc] initWithNibName:@"EndAudioPassThruViewController" bundle:nil];
        subscribevehicledatavc = [[SubscribeVehicleDataViewController alloc] initWithNibName:@"SubscribeVehicleDataViewController" bundle:nil];
        unsubscribevehicledatavc = [[UnsubscribeVehicleDataViewController alloc] initWithNibName:@"UnsubscribeVehicleDataViewController" bundle:nil];
        getvehicledatavc = [[GetVehicleDataViewController alloc] initWithNibName:@"GetVehicleDataViewController" bundle:nil];
        readdidvc = [[ReadDIDViewController alloc] initWithNibName:@"ReadDIDViewController" bundle:nil];
        getdtcsvc = [[GetDTCsViewController alloc] initWithNibName:@"GetDTCsViewController" bundle:nil];
        showconstanttbtvc = [[ShowConstantTBTViewController alloc] initWithNibName:@"ShowConstantTBTViewController" bundle:nil];
        alertmaneuvervc = [[AlertManeuverViewController alloc] initWithNibName:@"AlertManeuverViewController" bundle:nil];
        updateturnlistvc = [[UpdateTurnListViewController alloc] initWithNibName:@"UpdateTurnListViewController" bundle:nil];
        unregisterappinterfacevc = [[UnregisterAppInterfaceViewController alloc] initWithNibName:@"UnregisterAppInterfaceViewController" bundle:nil];
        
        addProfile = [[AddProfileViewController alloc] initWithNibName:@"LoadProfileViewController" bundle:nil];
        removeProfile = [[RemoveProfileViewController alloc] initWithNibName:@"LoadProfileViewController" bundle:nil];
        loadProfile = [[LoadProfileViewController alloc] initWithNibName:@"LoadProfileViewController" bundle:nil];
        unloadProfile = [[UnloadProfileViewController alloc] initWithNibName:@"LoadProfileViewController" bundle:nil];
        sendMessageToProfile = [[SendMessageToProfileViewController alloc] initWithNibName:@"SendMessageToProfileViewController" bundle:nil];
        applicationStateChanged = [[ApplicationStateChangedViewController alloc] initWithNibName:@"ApplicationStateChangedViewController" bundle:nil];
        [pmViewControllers setObject:addProfile forKey:@"AddProfile"];
        [pmViewControllers setObject:removeProfile forKey:@"RemoveProfile"];
        [pmViewControllers setObject:loadProfile forKey:@"LoadProfile"];
        [pmViewControllers setObject:unloadProfile forKey:@"UnloadProfile"];
        [pmViewControllers setObject:sendMessageToProfile forKey:@"SendAppToProfileMessage"];
        [pmViewControllers setObject:applicationStateChanged forKey:@"AppStateChanged"];
        
        self.title = NSLocalizedString(@"RPC Test", @"RPC Test");
        self.tabBarItem.image = [UIImage imageNamed:@"brief_case"];
    }
    return self;

}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self setIsMusicPlaying:NO];
    
    // Set AudioSession
    NSError *error = nil;
    [[AVAudioSession sharedInstance] setDelegate:self];
    [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategoryPlayback error:&error];
    
    NSURL *path = [[NSBundle mainBundle] URLForResource:@"Sample" withExtension:@"mp3"];
    self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:path error:&error];
    self.audioPlayer.volume = 0.5;
    self.audioPlayer.numberOfLoops = -1;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [rpclist count]; 
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) { 
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease]; 
    } 
    
    // Configure the cell. 
    cell.textLabel.text = [rpclist objectAtIndex:indexPath.row]; 
    
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *selectedrpc =  cell.textLabel.text;
    
    if ([selectedrpc isEqualToString:@"Alert"]) {
        [self.navigationController pushViewController:alertvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"Speak"]) {
        [self.navigationController pushViewController:speakvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"Show"]) {
        [self.navigationController pushViewController:showvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"SubscribeButton"]) {
        [self.navigationController pushViewController:subscribebuttonvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"UnsubscribeButton"]) {
        [self.navigationController pushViewController:unsubscribebuttonvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"AddCommand"]) {
        [self.navigationController pushViewController:addcommandvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"DeleteCommand"]) {
        [self.navigationController pushViewController:deletecommandvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"AddSubMenu"]) {
        [self.navigationController pushViewController:addsubmenuvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"DeleteSubMenu"]) {
        [self.navigationController pushViewController:deletesubmenuvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"SetGlobalProperties"]) {
        [self.navigationController pushViewController:setglobalpropertiesvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"ResetGlobalProperties"]) {
        [self.navigationController pushViewController:resetglobalpropertiesvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"SetMediaClockTimer"]) {
        [self.navigationController pushViewController:setmediaclocktimervc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"CreateInteractionChoiceSet"]) {
        [self.navigationController pushViewController:createinteractionchoicesetvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"DeleteInteractionChoiceSet"]) {
        [self.navigationController pushViewController:deleteinteractionchoicesetvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"PerformInteraction"]) {
        [self.navigationController pushViewController:performinteractionvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"Slider"]) {
        [self.navigationController pushViewController:slidervc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"ScrollableMessage"]) {
        [self.navigationController pushViewController:scrollablemessagevc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"ChangeRegistration"]) {
        [self.navigationController pushViewController:changeregistrationvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"SetAppIcon"]) {
        [self.navigationController pushViewController:setappiconvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"PerformAudioPassThru"]) {
        [self.navigationController pushViewController:performaudiopassthruvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"EndAudioPassThru"]) {
        [self.navigationController pushViewController:endaudiopassthruvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"SubscribeVehicleData"]) {
        [self.navigationController pushViewController:subscribevehicledatavc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"UnsubscribeVehicleData"]) {
        [self.navigationController pushViewController:unsubscribevehicledatavc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"GetVehicleData"]) {
        [self.navigationController pushViewController:getvehicledatavc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"ReadDID"]) {
        [self.navigationController pushViewController:readdidvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"GetDTCs"]) {
        [self.navigationController pushViewController:getdtcsvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"ShowConstantTBT"]) {
        [self.navigationController pushViewController:showconstanttbtvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"AlertManeuver"]) {
        [self.navigationController pushViewController:alertmaneuvervc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"UpdateTurnList"]) {
        [self.navigationController pushViewController:updateturnlistvc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"UnregisterAppInterface"]) {
        [self.navigationController pushViewController:unregisterappinterfacevc animated:YES];
    }
    else if ([selectedrpc isEqualToString:@"~CycleProxy~"]) {
        [[SmartDeviceLinkTester getInstance] cycleProxy];
        
        //Go Back To RPC List View
        [self.navigationController popToRootViewControllerAnimated:YES];
        
        //Go To Console View
        AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
        
    }
    else if ([selectedrpc isEqualToString:@"~GetProxyVersion~"]) {
        [[SmartDeviceLinkTester getInstance] postToConsoleLog:[[[SmartDeviceLinkTester getInstance] proxy] getProxyVersion]];
        
        //Go Back To RPC List View
        [self.navigationController popToRootViewControllerAnimated:YES];
        
        //Go To Console View
        AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
        
    }
    else if ([selectedrpc isEqualToString:@"~Play/Pause Audio~"]) {
        
        if (isMusicPlaying){
            [self setIsMusicPlaying:NO];
            [self.audioPlayer pause];
            [[SmartDeviceLinkTester getInstance] postToConsoleLog:@"Audio Paused"];
        }
        else{
            [self setIsMusicPlaying:YES];
            [self.audioPlayer play];
            [[SmartDeviceLinkTester getInstance] postToConsoleLog:@"Audio Playing"];
        }
        
        //Go Back To RPC List View
        [self.navigationController popToRootViewControllerAnimated:YES];
        
        //Go To Console View
        AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
        
    }
    else
    {
        UIViewController * controller = [pmViewControllers objectForKey: selectedrpc];
        if (controller != nil)
        {
            [self.navigationController pushViewController:controller animated:YES];
        }
    }
}

- (void)loadView
{
    [super loadView];
}

-(void) dealloc {
    [rpclist release];
    [alertvc release];
    [speakvc release];
    [showvc release];
    [subscribebuttonvc release];
    [unsubscribebuttonvc release];
    [addcommandvc release];
    [deletecommandvc release]; 
    [addsubmenuvc release];
    [deletesubmenuvc release];
    [setglobalpropertiesvc release];
    [resetglobalpropertiesvc release];
    [setmediaclocktimervc release];    
    [createinteractionchoicesetvc release];
    [deleteinteractionchoicesetvc release];
    [performinteractionvc release];
    [slidervc release];
    [scrollablemessagevc release];
    [changeregistrationvc release];
    [setappiconvc release];
    [performaudiopassthruvc release];
    [endaudiopassthruvc release];
    [subscribevehicledatavc release];
    [unsubscribevehicledatavc release];
    [getvehicledatavc release];
    [readdidvc release];
    [getdtcsvc release];
    [showconstanttbtvc release];
    [alertmaneuvervc release];
    [updateturnlistvc release];
    [unregisterappinterfacevc release];
 
    [super dealloc];
}





@end
