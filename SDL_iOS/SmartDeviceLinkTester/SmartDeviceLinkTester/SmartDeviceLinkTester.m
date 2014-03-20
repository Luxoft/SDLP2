//  SmartDeviceLinkTester.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SmartDeviceLinkTester.h"


#define PREFS_FIRST_RUN @"firstRun"

#define PREFS_PROTOCOL @"protocol"
#define PREFS_IPADDRESS @"ipaddress"
#define PREFS_PORT @"port"

#define PREFS_APPNAME @"appname"
#define PREFS_APPID @"appid"
#define PREFS_TTSNAME @"ttsname"
#define PREFS_TTSTYPE @"ttstype"
#define PREFS_VRSYNONYMS @"vrsynonyms"
#define PREFS_TYPE @"type"
#define PREFS_LANGUAGE @"language"
#define PREFS_HMILANGUAGE @"hmilanguage"


@implementation SmartDeviceLinkTester
{
    SDLTcpDiscoverer * _discoverer;
}


static SmartDeviceLinkTester* testerInstance;

+ (SmartDeviceLinkTester*) getInstance
{
	@synchronized(self)
	{
		if (testerInstance == nil)
			testerInstance = [[self alloc] init];
	}
	return testerInstance;
}

-(void) sendAndPostRPCMessage:(SDLRPCRequest *)rpcMsg {
    [self.proxy sendRPCRequest:rpcMsg];
    [self postToConsoleLog:rpcMsg];
}

-(NSNumber*) getNextCorrID {
    autoIncCorrID++;
    return [NSNumber numberWithInt:autoIncCorrID];
}

-(void) postToConsoleLog:(id) object {
    [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"consoleLog" object:object]];
}

-(void) incCmdID {
    cmdID++;
}

-(int) getCmdID {
    return cmdID;
}


// =====================================
//// RPC Function Calls
// =====================================

-(void) buildEndAudioPassThru{
    
    SDLEndAudioPassThru *req = [SDLRPCRequestFactory buildEndAudioPassThruWithCorrelationID:[NSNumber numberWithInt:autoIncCorrID++]];

    [self sendAndPostRPCMessage:req];  
}

-(void) deleteSubMenuPressedwithID:(NSNumber *)menuID {
    SDLDeleteSubMenu *req = [SDLRPCRequestFactory buildDeleteSubMenuWithID:menuID correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
}

-(void) performInteractionPressedwithInitialPrompt:(NSArray*)initialChunks initialText:(NSString*)initialText interactionChoiceSetIDList:(NSArray*) interactionChoiceSetIDList helpChunks:(NSArray*)helpChunks timeoutChunks:(NSArray*)timeoutChunks interactionMode:(SDLInteractionMode*) interactionMode timeout:(NSNumber*)timeout {
    
    SDLPerformInteraction *req = [SDLRPCRequestFactory buildPerformInteractionWithInitialChunks:initialChunks initialText:initialText interactionChoiceSetIDList:interactionChoiceSetIDList helpChunks:helpChunks timeoutChunks:timeoutChunks interactionMode:interactionMode timeout:timeout vrHelp:nil correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
}

-(void) unsubscribeButtonPressed:(SDLButtonName *)buttonName {
    SDLUnsubscribeButton *req = [SDLRPCRequestFactory buildUnsubscribeButtonWithName:buttonName correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
}

-(void) subscribeButtonPressed:(SDLButtonName *)buttonName {
    SDLSubscribeButton *req = [SDLRPCRequestFactory buildSubscribeButtonWithName:buttonName correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
   
    [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"SubscribeButtonRequest" object:req]];
    
}

-(void) buildPerformAudioPassThru:(NSString*) initialPrompt audioPassThruDisplayText1:(NSString*) audioPassThruDisplayText1 audioPassThruDisplayText2:(NSString*) audioPassThruDisplayText2 samplingRate:(SDLSamplingRate*) samplingRate maxDuration:(NSNumber*) maxDuration bitsPerSample:(SDLBitsPerSample*) bitsPerSample audioType:(SDLAudioType*) audioType muteAudio:(NSNumber*) muteAudio{
    
    SDLPerformAudioPassThru *req = [SDLRPCRequestFactory buildPerformAudioPassThruWithInitialPrompt:(NSString*) initialPrompt audioPassThruDisplayText1:(NSString*) audioPassThruDisplayText1 audioPassThruDisplayText2:(NSString*) audioPassThruDisplayText2 samplingRate:(SDLSamplingRate*) samplingRate maxDuration:(NSNumber*) maxDuration bitsPerSample:(SDLBitsPerSample*) bitsPerSample audioType:(SDLAudioType*) audioType muteAudio:(NSNumber*) muteAudio correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    audioPassThruData = [[NSMutableData alloc] init];
    
    [self sendAndPostRPCMessage:req]; 
}

-(void) addSubMenuPressedwithID:(NSNumber *)menuID menuName:(NSString *)menuName position:(NSNumber *)position {
    SDLAddSubMenu *req = [SDLRPCRequestFactory buildAddSubMenuWithID:menuID menuName:menuName position:position correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
   
    [[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:@"AddSubMenuRequest" object:req]];
}

-(void) deleteCommandPressed:(NSNumber *)commandID {
    SDLDeleteCommand *req = [SDLRPCRequestFactory buildDeleteCommandWithID:commandID correlationID:[NSNumber numberWithInt:autoIncCorrID++]];
    
    [self sendAndPostRPCMessage:req];
}


// =====================================
// Proxy Life Management Functions
// =====================================

-(void) savePreferences {
	
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    //Set to match settings.bundle defaults on initial app run
    if (![[prefs objectForKey:PREFS_FIRST_RUN] isEqualToString:@"False"]) {
        [prefs setObject:@"False" forKey:PREFS_FIRST_RUN];
        [prefs setObject:@"iap" forKey:PREFS_PROTOCOL];
        [prefs setObject:@"192.168.0.1" forKey:PREFS_IPADDRESS];
        [prefs setObject:@"12345" forKey:PREFS_PORT];
        
        [prefs setObject:@"SmartDeviceLinkTester" forKey:PREFS_APPNAME];
        [prefs setObject:@"123456789" forKey:PREFS_APPID];
        [prefs setObject:@"media" forKey:PREFS_TYPE];
        [prefs setObject:@"SmartDeviceLinkTester" forKey:PREFS_TTSNAME];
        [prefs setObject:@"text" forKey:PREFS_TTSTYPE];
        [prefs setObject:@"SmartDeviceLinkTester, SDLT" forKey:PREFS_VRSYNONYMS];
        [prefs setObject:@"EN-US" forKey:PREFS_LANGUAGE];
        [prefs setObject:@"EN-US" forKey:PREFS_HMILANGUAGE];
    }

	[prefs synchronize];
}

//Populating Display Screen with relevant information upon creation
-(void) initialShow {
    SDLShow* msg = [SDLRPCRequestFactory buildShowWithMainField1:@"SmartDeviceLink" mainField2:@"Tester!" alignment:[SDLTextAlignment CENTERED] correlationID:[self getNextCorrID]];
	msg.mediaTrack = @"SDLT";
    [self sendAndPostRPCMessage:msg];
    
}

-(void)  setupProxy{
    [self savePreferences];
    
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    if ([[prefs objectForKey:PREFS_PROTOCOL] isEqualToString:@"tcpl"]) {
        self.proxy = [SDLProxyFactory buildProxyWithListener: self
                                                   tcpIPAddress: nil
                                                            tcpPort: [prefs objectForKey:PREFS_PORT]];
    } else if ([[prefs objectForKey:PREFS_PROTOCOL] isEqualToString:@"tcps"]) {
        self.proxy = [SDLProxyFactory buildProxyWithListener: self
                                                   tcpIPAddress: [prefs objectForKey:PREFS_IPADDRESS]
                                                             tcpPort: [prefs objectForKey:PREFS_PORT]];
    } else if ([[prefs objectForKey:PREFS_PROTOCOL] isEqualToString:@"tcpa"]) {
        _discoverer = [[SDLTcpDiscoverer alloc] initWithDefaultListener:self];
        [_discoverer performSearch];
    } else
        self.proxy = [SDLProxyFactory buildProxyWithListener: self];
    
    autoIncCorrID = 101;
    cmdID = 1;
}

-(void) cycleProxy {
    [self postToConsoleLog:@"~Cycle Proxy~"];
    [self onProxyClosed];
}

-(void) disposeProxy {
    [self.proxy release];
	self.proxy = nil;
}


// ========================================================
// Implementation Of Required SDLProxyListener Callbacks
// ========================================================

-(void) onOnDriverDistraction:(SDLOnDriverDistraction*) notification {
	[self postToConsoleLog:notification];
}

-(void) onOnHMIStatus:(SDLOnHMIStatus*) notification {
    [self postToConsoleLog:notification];
 
    if (notification.hmiLevel == SDLHMILevel.HMI_FULL) {
        if (firstTimeStartUp == YES) {
            [self initialShow];
            firstTimeStartUp = NO;
        }
    }
}

-(void) onProxyClosed {
    [self postToConsoleLog:@"onProxyClosed"];
    [SDLDebugTool logInfo:@"onProxyClosed"];
	[self disposeProxy];
    
    [self postToConsoleLog:@"~Restart Proxy~"];
    [SDLDebugTool logInfo:@"~Restart Proxy~"];
	[[self init] setupProxy];
}

-(void) onProxyOpened {
    [self postToConsoleLog:@"onProxyOpened"];
    [SDLDebugTool logInfo:@"onProxyOpened"];
    
    firstTimeStartUp = YES;
    
    //Get App Settings
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    
    
    NSNumber* isMediaApp = [NSNumber numberWithBool:NO];
    
    if ([[prefs objectForKey:PREFS_TYPE] isEqualToString:@"media"]) {
        isMediaApp = [NSNumber numberWithBool:YES];
    }
    
    NSMutableArray *ttsName = [[NSMutableArray alloc] init];
    
    if ([[prefs objectForKey:PREFS_TTSTYPE] isEqualToString:@"sapi"]) {
        [ttsName addObject:[SDLTTSChunkFactory buildTTSChunkForString:[prefs objectForKey:PREFS_TTSNAME] type:SDLSpeechCapabilities.SAPI_PHONEMES]];
    }
    else if ([[prefs objectForKey:PREFS_TTSTYPE] isEqualToString:@"lhp"]) {
        [ttsName addObject:[SDLTTSChunkFactory buildTTSChunkForString:[prefs objectForKey:PREFS_TTSNAME] type:SDLSpeechCapabilities.LHPLUS_PHONEMES]];
    }
    else {
        [ttsName addObject:[SDLTTSChunkFactory buildTTSChunkForString:[prefs objectForKey:PREFS_TTSNAME] type:SDLSpeechCapabilities.TEXT]];
    }
    
    SDLRegisterAppInterface* regRequest = [SDLRPCRequestFactory buildRegisterAppInterfaceWithAppName:[prefs objectForKey:PREFS_APPNAME] ttsName:ttsName vrSynonyms:[[[prefs objectForKey:PREFS_VRSYNONYMS] componentsSeparatedByString:@", "] mutableCopy] isMediaApp:isMediaApp languageDesired:[prefs objectForKey:PREFS_LANGUAGE] hmiDisplayLanguageDesired:[prefs objectForKey:PREFS_HMILANGUAGE] appID:[prefs objectForKey:PREFS_APPID]];
    
    [self sendAndPostRPCMessage:regRequest];
}



// ========================================================
// Implementation Of Optional SDLProxyListener Callbacks
// ========================================================

-(void) onAddCommandResponse:(SDLAddCommandResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onAddSubMenuResponse:(SDLAddSubMenuResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onAlertManeuverResponse:(SDLAlertManeuverResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onAlertResponse:(SDLAlertResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onChangeRegistrationResponse:(SDLChangeRegistrationResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onCreateInteractionChoiceSetResponse:(SDLCreateInteractionChoiceSetResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onDeleteCommandResponse:(SDLDeleteCommandResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onDeleteFileResponse:(SDLDeleteFileResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onDeleteInteractionChoiceSetResponse:(SDLDeleteInteractionChoiceSetResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onDeleteSubMenuResponse:(SDLDeleteSubMenuResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onEndAudioPassThruResponse:(SDLEndAudioPassThruResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onError:(NSException*) e {
	[SDLDebugTool logInfo:@"proxy error occurred: %@", e];
}
-(void) onGenericResponse:(SDLGenericResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onGetDTCsResponse:(SDLGetDTCsResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onGetVehicleDataResponse:(SDLGetVehicleDataResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onListFilesResponse:(SDLListFilesResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onOnAppInterfaceUnregistered:(SDLOnAppInterfaceUnregistered*) notification {
	[self postToConsoleLog:notification];
}
-(void) onOnAudioPassThru:(SDLOnAudioPassThru*) notification {
    [self postToConsoleLog:notification];
    
    //Fill Buffer
    NSData *test = [NSData dataWithData:notification.bulkData];
    [audioPassThruData appendData:test];
}
-(void) onOnButtonEvent:(SDLOnButtonEvent*) notification {
    [self postToConsoleLog:notification];
}
-(void) onOnButtonPress:(SDLOnButtonPress*) notification {
    [self postToConsoleLog:notification];
}
-(void) onOnCommand:(SDLOnCommand*) notification {
	[self postToConsoleLog:notification];
}
-(void) onOnLanguageChange:(SDLOnLanguageChange*) notification {
    [self postToConsoleLog:notification];
}
-(void) onOnPermissionsChange:(SDLOnPermissionsChange*) notification {
    [self postToConsoleLog:notification];
}
-(void) onOnTBTClientState:(SDLOnTBTClientState*) notification {
	[self postToConsoleLog:notification];
}
-(void) onOnVehicleData:(SDLOnVehicleData*) notification {
    [self postToConsoleLog:notification];
}
-(void) onPerformAudioPassThruResponse:(SDLPerformAudioPassThruResponse*) response {
    [self postToConsoleLog:response];
    
    //Write Data To File
    NSData *dataToWrite = [NSData dataWithData:audioPassThruData];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *savePath = [documentsDirectory stringByAppendingPathComponent:@"apt.pcm"];
    [dataToWrite writeToFile:savePath atomically:NO];
    
    [audioPassThruData release];
}
-(void) onPerformInteractionResponse:(SDLPerformInteractionResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onPutFileResponse:(SDLPutFileResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onReadDIDResponse:(SDLReadDIDResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onRegisterAppInterfaceResponse:(SDLRegisterAppInterfaceResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onResetGlobalPropertiesResponse:(SDLResetGlobalPropertiesResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onScrollableMessageResponse:(SDLScrollableMessageResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onSetAppIconResponse:(SDLSetAppIconResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onSetDisplayLayoutResponse:(SDLSetDisplayLayoutResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onSetGlobalPropertiesResponse:(SDLSetGlobalPropertiesResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onSetMediaClockTimerResponse:(SDLSetMediaClockTimerResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onShowConstantTBTResponse:(SDLShowConstantTBTResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onShowResponse:(SDLShowResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onSliderResponse:(SDLSliderResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onSpeakResponse:(SDLSpeakResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onSubscribeButtonResponse:(SDLSubscribeButtonResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onSubscribeVehicleDataResponse:(SDLSubscribeVehicleDataResponse*) response {
    [self postToConsoleLog:response];
}
-(void) onUnregisterAppInterfaceResponse:(SDLUnregisterAppInterfaceResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onUnsubscribeButtonResponse:(SDLUnsubscribeButtonResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onUnsubscribeVehicleDataResponse:(SDLUnsubscribeVehicleDataResponse*) response {
	[self postToConsoleLog:response];
}
-(void) onUpdateTurnListResponse:(SDLUpdateTurnListResponse*) response {
    [self postToConsoleLog:response];
}
- (void) onOnProfileToAppMessage: (SDLOnProfileToAppMessage*) notification
{
    [self postToConsoleLog:notification];
}
- (void) onOnProfileUnloaded: (SDLOnProfileUnloaded*) notification
{
    [self postToConsoleLog:notification];
}

- (void) onAddProfileResponse: (SDLAddProfileResponse*) resp
{
    [self postToConsoleLog:resp];
}
- (void) onAppStateChangedResponse: (SDLAppStateChangedResponse*) resp
{
    [self postToConsoleLog:resp];
}
- (void) onLoadProfileResponse: (SDLLoadProfileResponse*) resp
{
    [self postToConsoleLog:resp];
}
- (void) onRemoveProfileResponse: (SDLRemoveProfileResponse*) resp
{
    [self postToConsoleLog:resp];
}
- (void) onUnloadProfileResponse: (SDLUnloadProfileResponse*) resp
{
    [self postToConsoleLog:resp];
}
- (void) onSendAppToProfileMessageResponse: (SDLSendAppToProfileMessageResponse*) resp
{
    [self postToConsoleLog:resp];
}

-(void) onUserSelectedDeviceWithIP:(NSString*) ipaddress tcpPort:(NSString*) port
{
    self.proxy = [SDLProxyFactory buildProxyWithListener:self
                                            tcpIPAddress:ipaddress
                                                 tcpPort:port];
}

-(void) onUserCanceledAlert
{
    [_discoverer performSearch];
}

-(void) onFoundNothing
{
    [_discoverer performSearch];
}
@end
