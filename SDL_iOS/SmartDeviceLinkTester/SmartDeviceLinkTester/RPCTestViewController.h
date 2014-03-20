//  RPCTestViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>

#import "AlertViewController.h"
#import "SpeakViewController.h"
#import "ShowViewController.h"
#import "SubscribeButtonViewController.h"
#import "UnsubscribeButtonViewController.h"
#import "AddCommandViewController.h"
#import "DeleteCommandViewController.h"
#import "AddSubMenuViewController.h"
#import "DeleteSubMenuViewController.h"
#import "SetGlobalPropertiesViewController.h"
#import "ResetGlobalPropertiesViewController.h"
#import "SetMediaClockTimerViewController.h"
#import "CreateInteractionChoiceSetViewController.h"
#import "DeleteInteractionChoiceSetViewController.h"
#import "PerformInteractionViewController.h"
#import "SliderViewController.h"
#import "ScrollableMessageViewController.h"
#import "ChangeRegistrationViewController.h"
#import "SetAppIconViewController.h"
#import "PerformAudioPassThruViewController.h"
#import "EndAudioPassThruViewController.h"
#import "SubscribeVehicleDataViewController.h"
#import "UnsubscribeVehicleDataViewController.h"
#import "GetVehicleDataViewController.h"
#import "ReadDIDViewController.h"
#import "GetDTCsViewController.h"
#import "ShowConstantTBTViewController.h"
#import "AlertManeuverViewController.h"
#import "UpdateTurnListViewController.h"
#import "UnregisterAppInterfaceViewController.h"

#import "AddProfileViewController.h"
#import "RemoveProfileViewController.h"
#import "LoadProfileViewController.h"
#import "UnloadProfileViewController.h"
#import "SendMessageToProfileViewController.h"
#import "ApplicationStateChangedViewController.h"

@interface RPCTestViewController : UITableViewController <AVAudioPlayerDelegate>  {

    NSMutableArray *rpclist;
    
    AlertViewController *alertvc;
    SpeakViewController *speakvc;
    ShowViewController *showvc;
    SubscribeButtonViewController *subscribebuttonvc;
    UnsubscribeButtonViewController *unsubscribebuttonvc;
    AddCommandViewController *addcommandvc;
    DeleteCommandViewController *deletecommandvc;
    AddSubMenuViewController *addsubmenuvc;
    DeleteSubMenuViewController *deletesubmenuvc;
    SetGlobalPropertiesViewController *setglobalpropertiesvc;
    ResetGlobalPropertiesViewController *resetglobalpropertiesvc;
    SetMediaClockTimerViewController *setmediaclocktimervc;
    CreateInteractionChoiceSetViewController *createinteractionchoicesetvc;
    DeleteInteractionChoiceSetViewController *deleteinteractionchoicesetvc;
    PerformInteractionViewController *performinteractionvc;
    SliderViewController *slidervc;
    ScrollableMessageViewController *scrollablemessagevc;
    ChangeRegistrationViewController *changeregistrationvc;
    SetAppIconViewController *setappiconvc;
    PerformAudioPassThruViewController *performaudiopassthruvc;
    EndAudioPassThruViewController *endaudiopassthruvc;
    SubscribeVehicleDataViewController *subscribevehicledatavc;
    UnsubscribeVehicleDataViewController *unsubscribevehicledatavc;
    GetVehicleDataViewController *getvehicledatavc;
    ReadDIDViewController *readdidvc;
    GetDTCsViewController *getdtcsvc;
    ShowConstantTBTViewController *showconstanttbtvc;
    AlertManeuverViewController *alertmaneuvervc;
    UpdateTurnListViewController *updateturnlistvc;
    UnregisterAppInterfaceViewController *unregisterappinterfacevc;
    
    AddProfileViewController * addProfile;
    RemoveProfileViewController * removeProfile;
    LoadProfileViewController * loadProfile;
    UnloadProfileViewController * unloadProfile;
    SendMessageToProfileViewController * sendMessageToProfile;
    ApplicationStateChangedViewController * applicationStateChanged;
    
}

@end
