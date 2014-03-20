//  SDLRPCRequestFactory.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLRPCRequestFactory.h>

#import <SmartDeviceLink/SDLMenuParams.h>
#import <SmartDeviceLink/SDLTTSChunkFactory.h>

#include <SmartDeviceLink/ProfileBinaryPacketizer.h>

#define NGN_MEDIA_SCREEN_APP_NAME_MAX_LENGTH 5

@implementation SDLRPCRequestFactory

//***** AddCommand *****
+(SDLAddCommand*) buildAddCommandWithID:(NSNumber*) cmdID menuName:(NSString*) menuName parentID:(NSNumber*) parentID position:(NSNumber*) position vrCommands:(NSArray*) vrCommands iconValue:(NSString*) iconValue iconType:(SDLImageType*) iconType correlationID:(NSNumber*) correlationID {
	
    SDLAddCommand* msg = [[[SDLAddCommand alloc] init] autorelease];
	msg.correlationID = correlationID;
    
	msg.cmdID = cmdID;
    
	if (menuName != nil || parentID != nil || position != nil) {
		SDLMenuParams* menuParams = [[[SDLMenuParams alloc] init] autorelease];
		menuParams.menuName = menuName;
		menuParams.parentID = parentID;
		menuParams.position = position;
		msg.menuParams = menuParams;
	}
	msg.vrCommands = [[vrCommands mutableCopy] autorelease];
    
    if (iconValue != nil || iconType != nil) {
        SDLImage* icon = [[[SDLImage alloc] init] autorelease];
		icon.value = iconValue;
		icon.imageType = iconType;
		msg.cmdIcon = icon;
    }
    
	return msg;
}

+(SDLAddCommand*) buildAddCommandWithID:(NSNumber*) cmdID menuName:(NSString*) menuName vrCommands:(NSArray*) vrCommands correlationID:(NSNumber*) correlationID {
	
    return [SDLRPCRequestFactory buildAddCommandWithID:cmdID menuName:menuName parentID:nil position:nil vrCommands:vrCommands iconValue:nil iconType:nil correlationID:correlationID];
}

+(SDLAddCommand*) buildAddCommandWithID:(NSNumber*) cmdID vrCommands:(NSArray*) vrCommands correlationID:(NSNumber*) correlationID {
	
    return [SDLRPCRequestFactory buildAddCommandWithID:cmdID menuName:nil vrCommands:vrCommands correlationID:correlationID];
}
//*****


//***** AddSubMenu *****
+(SDLAddSubMenu*) buildAddSubMenuWithID:(NSNumber*) menuID menuName:(NSString*) menuName position:(NSNumber*) position correlationID:(NSNumber*) correlationID {
	
    SDLAddSubMenu* msg = [[[SDLAddSubMenu alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.menuID = menuID;
	msg.menuName = menuName;
	msg.position = position;
	
	return msg;
}

+(SDLAddSubMenu*) buildAddSubMenuWithID:(NSNumber*) menuID menuName:(NSString*) menuName correlationID:(NSNumber*) correlationID  {
	return [SDLRPCRequestFactory buildAddSubMenuWithID:menuID menuName:menuName position:nil correlationID:correlationID];
}
//*****


//***** Alert *****
+(SDLAlert*) buildAlertWithTTS:(NSString*) ttsText alertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 alertText3:(NSString*) alertText3 playTone:(NSNumber*) playTone duration:(NSNumber*) duration correlationID:(NSNumber*) correlationID {
	
    SDLTTSChunk* simpleChunk = [[[SDLTTSChunk alloc] init] autorelease];
	simpleChunk.text = ttsText;
	simpleChunk.type = SDLSpeechCapabilities.TEXT;
	NSArray* ttsChunks = [NSArray arrayWithObject:simpleChunk];
	
    return [SDLRPCRequestFactory buildAlertWithTTSChunks:ttsChunks alertText1:alertText1 alertText2:alertText2 alertText3:alertText3 playTone:playTone duration:duration softButtons:nil correlationID:correlationID];
}

+(SDLAlert*) buildAlertWithTTS:(NSString*) ttsText alertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 playTone:(NSNumber*) playTone duration:(NSNumber*) duration correlationID:(NSNumber*) correlationID {
	
    return [SDLRPCRequestFactory buildAlertWithTTS:ttsText alertText1:alertText1 alertText2:alertText2 alertText3:nil playTone:playTone duration:duration correlationID:correlationID];
}

+(SDLAlert*) buildAlertWithTTS:(NSString*) ttsText playTone:(NSNumber*) playTone correlationID:(NSNumber*)
correlationID{
	
    return [SDLRPCRequestFactory buildAlertWithTTS:ttsText alertText1:nil alertText2:nil alertText3:nil playTone:playTone duration:nil correlationID:correlationID];
}

//***
+(SDLAlert*) buildAlertWithTTSChunks:(NSArray*) ttsChunks alertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 alertText3:(NSString*) alertText3 playTone:(NSNumber*) playTone duration:(NSNumber*) duration softButtons:(NSArray*) softButtons correlationID:(NSNumber*) correlationID {
	
    SDLAlert* msg = [[[SDLAlert alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.alertText1 = alertText1;
	msg.alertText2 = alertText2;
    msg.alertText3 = alertText3;
	msg.ttsChunks = [[ttsChunks mutableCopy] autorelease];
	msg.playTone = playTone;
	msg.duration = duration;
    msg.softButtons = [[softButtons mutableCopy] autorelease];
	return msg;
}

+(SDLAlert*) buildAlertWithTTSChunks:(NSArray*) ttsChunks playTone:(NSNumber*) playTone correlationID:(NSNumber*) correlationID  {
	return [SDLRPCRequestFactory buildAlertWithTTSChunks:ttsChunks alertText1:nil alertText2:nil alertText3:nil playTone:playTone duration:nil softButtons:nil correlationID:correlationID];
}

//***
+(SDLAlert*) buildAlertWithAlertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 alertText3:(NSString*) alertText3 duration:(NSNumber*) duration softButtons:(NSMutableArray*) softButtons correlationID:(NSNumber*) correlationID  {
	return [SDLRPCRequestFactory buildAlertWithTTSChunks:nil alertText1:alertText1 alertText2:alertText2 alertText3:alertText3 playTone:nil duration:duration softButtons:softButtons correlationID:correlationID];
}

+(SDLAlert*) buildAlertWithAlertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 alertText3:(NSString*) alertText3 duration:(NSNumber*) duration correlationID:(NSNumber*) correlationID  {
	return [SDLRPCRequestFactory buildAlertWithTTSChunks:nil alertText1: alertText1 alertText2:alertText2 alertText3:alertText3 playTone:nil duration:duration softButtons:nil correlationID:correlationID];
}

+(SDLAlert*) buildAlertWithAlertText1:(NSString*) alertText1 alertText2:(NSString*) alertText2 duration:(NSNumber*) duration correlationID:(NSNumber*) correlationID  {
	return [SDLRPCRequestFactory buildAlertWithTTSChunks:nil alertText1: alertText1 alertText2:alertText2 alertText3:nil playTone:nil duration:duration softButtons:nil correlationID:correlationID];
}
//*****


+(SDLAlertManeuver*) buildAlertManeuverWithTTSChunks:(NSMutableArray*) ttsChunks softButtons:(NSMutableArray*) softButtons correlationID:(NSNumber*) correlationID {
    
    SDLAlertManeuver* msg = [[[SDLAlertManeuver alloc] init] autorelease];
    msg.ttsChunks = [[ttsChunks mutableCopy] autorelease];
    msg.softButtons = [[softButtons mutableCopy] autorelease];
    msg.correlationID = correlationID;
    return msg;
}

+(SDLChangeRegistration*) buildChangeRegistrationWithLanguage:(SDLLanguage*) language hmiDisplayLanguage:(SDLLanguage*) hmiDisplayLanguage correlationID:(NSNumber*) correlationID {
	
    SDLChangeRegistration* msg = [[[SDLChangeRegistration alloc] init] autorelease];
    msg.language = language;
    msg.hmiDisplayLanguage = hmiDisplayLanguage;
    msg.correlationID = correlationID;
    
	return msg;
}

+(SDLCreateInteractionChoiceSet*) buildCreateInteractionChoiceSetWithID:(NSNumber*)interactionChoiceSetID choiceSet:(NSArray*) choices correlationID:(NSNumber*) correlationID  {
	
    SDLCreateInteractionChoiceSet *msg = [[[SDLCreateInteractionChoiceSet alloc] init] autorelease];
	msg.interactionChoiceSetID = interactionChoiceSetID;
	msg.choiceSet = [[choices mutableCopy] autorelease];
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLDeleteCommand*) buildDeleteCommandWithID:(NSNumber*) cmdID correlationID:(NSNumber*) correlationID  {
	
    SDLDeleteCommand *msg = [[[SDLDeleteCommand alloc] init] autorelease];
	msg.cmdID = cmdID;
	msg.correlationID = correlationID;
	
    return msg;
}

+(SDLDeleteFile*) buildDeleteFileWithName:(NSString*) syncFileName correlationID:(NSNumber*) correlationID {
    
    SDLDeleteFile* msg = [[[SDLDeleteFile alloc] init] autorelease];
    msg.syncFileName = syncFileName;
    msg.correlationID = correlationID;
    
    return msg;
}

+(SDLListFiles*) buildListFilesWithCorrelationID:(NSNumber*) correlationID {
    
    SDLListFiles* msg = [[[SDLListFiles alloc] init] autorelease];
    msg.correlationID = correlationID;
    
    return msg;
}

+(SDLDeleteInteractionChoiceSet*) buildDeleteInteractionChoiceSetWithID:(NSNumber*)interactionChoiceSetID correlationID:(NSNumber*) correlationID  {
	
    SDLDeleteInteractionChoiceSet *msg = [[[SDLDeleteInteractionChoiceSet alloc] init] autorelease];
	msg.interactionChoiceSetID = interactionChoiceSetID;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLDeleteSubMenu*) buildDeleteSubMenuWithID:(NSNumber*) menuID correlationID:(NSNumber*) correlationID {
	
    SDLDeleteSubMenu *msg = [[[SDLDeleteSubMenu alloc] init] autorelease];
	msg.menuID = menuID;
	msg.correlationID = correlationID;
    
	return msg;
}

+(SDLEndAudioPassThru*) buildEndAudioPassThruWithCorrelationID:(NSNumber*) correlationID {
    
    SDLEndAudioPassThru* msg = [[[SDLEndAudioPassThru alloc] init] autorelease];
    msg.correlationID = correlationID;
    
	return msg;
}

+(SDLGetDTCs*) buildGetDTCsWithECUName:(NSNumber*) ecuName correlationID:(NSNumber*) correlationID {
	
    SDLGetDTCs* msg = [[[SDLGetDTCs alloc] init] autorelease];
    msg.ecuName = ecuName;
	msg.correlationID = correlationID;
    
	return msg;
}

+(SDLGetVehicleData*) buildGetVehicleDataWithGPS:(NSNumber*) gps speed:(NSNumber*) speed rpm:(NSNumber*) rpm fuelLevel:(NSNumber*) fuelLevel fuelLevelState:(NSNumber*) fuelLevelState instantFuelConsumption:(NSNumber*) instantFuelConsumption externalTemperature:(NSNumber*) externalTemperature vin:(NSNumber*) vin prndl:(NSNumber*) prndl tirePressure:(NSNumber*) tirePressure odometer:(NSNumber*) odometer beltStatus:(NSNumber*) beltStatus bodyInformation:(NSNumber*) bodyInformation deviceStatus:(NSNumber*) deviceStatus driverBraking:(NSNumber*) driverBraking wiperStatus:(NSNumber*) wiperStatus headLampStatus:(NSNumber*) headLampStatus engineTorque:(NSNumber*) engineTorque accPedalPosition:(NSNumber*) accPedalPosition steeringWheelAngle:(NSNumber*) steeringWheelAngle correlationID:(NSNumber*) correlationID {
	
    SDLGetVehicleData* msg = [[[SDLGetVehicleData alloc] init] autorelease];
    msg.gps = gps;
	msg.speed = speed;
	msg.rpm = rpm;
	msg.fuelLevel = fuelLevel;
	msg.fuelLevelState = fuelLevelState;
	msg.instantFuelConsumption = instantFuelConsumption;
	msg.externalTemperature = externalTemperature;
	msg.vin = vin;
	msg.prndl = prndl;
	msg.tirePressure = tirePressure;
	msg.odometer = odometer;
	msg.beltStatus = beltStatus;
	msg.bodyInformation = bodyInformation;
	msg.deviceStatus = deviceStatus;
    msg.driverBraking = driverBraking;
    msg.wiperStatus = wiperStatus;
    msg.headLampStatus = headLampStatus;
    msg.engineTorque = engineTorque;
    msg.accPedalPosition = accPedalPosition;
    msg.steeringWheelAngle = steeringWheelAngle;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLPerformAudioPassThru*) buildPerformAudioPassThruWithInitialPrompt:(NSString*) initialPrompt audioPassThruDisplayText1:(NSString*) audioPassThruDisplayText1 audioPassThruDisplayText2:(NSString*) audioPassThruDisplayText2 samplingRate:(SDLSamplingRate*)  samplingRate maxDuration:(NSNumber*) maxDuration bitsPerSample:(SDLBitsPerSample*) bitsPerSample audioType:(SDLAudioType*) audioType muteAudio:(NSNumber*) muteAudio correlationID:(NSNumber*) correlationID {
    
    NSArray* initialChunks = [SDLTTSChunkFactory buildTTSChunksFromSimple:initialPrompt];
    
    SDLPerformAudioPassThru* msg = [[[SDLPerformAudioPassThru alloc] init] autorelease];
	msg.initialPrompt = [[initialChunks mutableCopy] autorelease];
	msg.audioPassThruDisplayText1 = audioPassThruDisplayText1;
	msg.audioPassThruDisplayText2 = audioPassThruDisplayText2;
	msg.samplingRate = samplingRate;
    msg.maxDuration = maxDuration;
    msg.bitsPerSample = bitsPerSample;
    msg.audioType = audioType;
    msg.muteAudio = muteAudio;
    
    return msg;
}


//***** PerformInteraction *****
+(SDLPerformInteraction*) buildPerformInteractionWithInitialChunks:(NSArray*)initialChunks initialText:(NSString*)initialText interactionChoiceSetIDList:(NSArray*) interactionChoiceSetIDList helpChunks:(NSArray*)helpChunks timeoutChunks:(NSArray*)timeoutChunks interactionMode:(SDLInteractionMode*) interactionMode timeout:(NSNumber*)timeout vrHelp:(NSArray*) vrHelp correlationID:(NSNumber*) correlationID  {
	
    SDLPerformInteraction *msg = [[[SDLPerformInteraction alloc] init] autorelease];
	msg.initialPrompt = [[initialChunks mutableCopy] autorelease];
	msg.initialText = initialText;
	msg.interactionChoiceSetIDList = [[interactionChoiceSetIDList mutableCopy] autorelease];
	msg.helpPrompt = [[helpChunks mutableCopy] autorelease];
	msg.timeoutPrompt = [[timeoutChunks mutableCopy] autorelease];
	msg.interactionMode = interactionMode;
	msg.timeout = timeout;
    msg.vrHelp = [[vrHelp mutableCopy] autorelease];
	msg.correlationID = correlationID;
	
	return msg;
}

//***
+(SDLPerformInteraction*) buildPerformInteractionWithInitialPrompt:(NSString*)initialPrompt initialText:(NSString*)initialText interactionChoiceSetIDList:(NSArray*) interactionChoiceSetIDList helpPrompt:(NSString*)helpPrompt timeoutPrompt:(NSString*)timeoutPrompt interactionMode:(SDLInteractionMode*) interactionMode timeout:(NSNumber*)timeout vrHelp:(NSArray*) vrHelp correlationID:(NSNumber*) correlationID  {
	
    NSArray* initialChunks = [SDLTTSChunkFactory buildTTSChunksFromSimple:initialPrompt];
	NSArray* helpChunks = [SDLTTSChunkFactory buildTTSChunksFromSimple:helpPrompt];
	NSArray* timeoutChunks = [SDLTTSChunkFactory buildTTSChunksFromSimple:timeoutPrompt];
	
	return [SDLRPCRequestFactory buildPerformInteractionWithInitialChunks:initialChunks initialText:initialText interactionChoiceSetIDList:interactionChoiceSetIDList helpChunks:helpChunks timeoutChunks:timeoutChunks interactionMode:interactionMode timeout:timeout vrHelp:vrHelp correlationID:correlationID];
}

+(SDLPerformInteraction*) buildPerformInteractionWithInitialPrompt:(NSString*)initialPrompt initialText:(NSString*)initialText interactionChoiceSetID:(NSNumber*) interactionChoiceSetID vrHelp:(NSArray*) vrHelp correlationID:(NSNumber*) correlationID  {
	
    NSArray *interactionChoiceSetIDList = [NSArray arrayWithObject:interactionChoiceSetID];
	NSArray* initialChunks = [SDLTTSChunkFactory buildTTSChunksFromSimple:initialPrompt];
	
	return [SDLRPCRequestFactory buildPerformInteractionWithInitialChunks:initialChunks initialText:initialText interactionChoiceSetIDList:interactionChoiceSetIDList helpChunks:nil timeoutChunks:nil interactionMode:SDLInteractionMode.BOTH timeout:nil vrHelp:vrHelp correlationID:correlationID];
}

+(SDLPerformInteraction*) buildPerformInteractionWithInitialPrompt:(NSString*)initialPrompt initialText:(NSString*)initialText interactionChoiceSetIDList:(NSArray*) interactionChoiceSetIDList helpPrompt:(NSString*)helpPrompt timeoutPrompt:(NSString*)timeoutPrompt interactionMode:(SDLInteractionMode*) interactionMode timeout:(NSNumber*)timeout correlationID:(NSNumber*) correlationID  {
    
	return [SDLRPCRequestFactory buildPerformInteractionWithInitialPrompt:initialPrompt initialText:initialText interactionChoiceSetIDList:interactionChoiceSetIDList helpPrompt:helpPrompt timeoutPrompt:timeoutPrompt interactionMode:interactionMode timeout:timeout vrHelp:nil correlationID:(NSNumber*) correlationID];
}

+(SDLPerformInteraction*) buildPerformInteractionWithInitialPrompt:(NSString*)initialPrompt initialText:(NSString*)initialText interactionChoiceSetID:(NSNumber*) interactionChoiceSetID correlationID:(NSNumber*) correlationID  {
    
	return [SDLRPCRequestFactory buildPerformInteractionWithInitialPrompt:initialPrompt initialText:initialText interactionChoiceSetID:interactionChoiceSetID vrHelp:nil correlationID:correlationID];
}
//*****


+(SDLPutFile*) buildPutFileWithFileName:(NSString*) syncFileName fileType:(SDLFileType*) fileType persisistentFile:(NSNumber*) persistentFile correlationID:(NSNumber*) correlationID {
    
    SDLPutFile* msg = [[[SDLPutFile alloc] init] autorelease];
    msg.syncFileName = syncFileName;
    
    msg.fileType = [[fileType mutableCopy] autorelease];
    msg.persistentFile = persistentFile;
    
    msg.correlationID = correlationID;
    
    return msg;
}

+(SDLReadDID*) buildReadDIDWithECUName:(NSNumber*) ecuName didLocation:(NSArray*) didLocation correlationID:(NSNumber*) correlationID {
	
    SDLReadDID* msg = [[[SDLReadDID alloc] init] autorelease];
    msg.ecuName = ecuName;
	msg.didLocation = [[didLocation mutableCopy] autorelease];
	msg.correlationID = correlationID;
	
	return msg;
}

//***** RegisterAppInterface *****
+(SDLRegisterAppInterface*) buildRegisterAppInterfaceWithAppName:(NSString*) appName ttsName:(NSMutableArray*) ttsName vrSynonyms:(NSMutableArray*) vrSynonyms isMediaApp:(NSNumber*) isMediaApp languageDesired:(SDLLanguage*) languageDesired hmiDisplayLanguageDesired:(SDLLanguage*) hmiDisplayLanguageDesired appID:(NSString*) appID {
    
    SDLRegisterAppInterface* msg = [[[SDLRegisterAppInterface alloc] init] autorelease];
    SDLSyncMsgVersion* version = [[[SDLSyncMsgVersion alloc] init] autorelease];
	version.majorVersion = [NSNumber numberWithInt:1];
	version.minorVersion = [NSNumber numberWithInt:0];
    msg.syncMsgVersion = version;
	msg.appName = appName;
    msg.ttsName = ttsName;
	NSString* ngnMediaAppName = [[appName copy] autorelease];
	if (ngnMediaAppName.length > NGN_MEDIA_SCREEN_APP_NAME_MAX_LENGTH) {
		ngnMediaAppName = [ngnMediaAppName substringToIndex:NGN_MEDIA_SCREEN_APP_NAME_MAX_LENGTH];
	}
	msg.ngnMediaScreenAppName = ngnMediaAppName;
	msg.vrSynonyms = vrSynonyms;
	msg.isMediaApplication = isMediaApp;
    msg.languageDesired = languageDesired;
    msg.hmiDisplayLanguageDesired = hmiDisplayLanguageDesired;
    msg.appID = appID;
    
    msg.correlationID = [NSNumber numberWithInt:1];
    
	return msg;
}

+(SDLRegisterAppInterface*) buildRegisterAppInterfaceWithAppName:(NSString*) appName isMediaApp:(NSNumber*) isMediaApp languageDesired:(SDLLanguage*) languageDesired appID:(NSString*) appID {
    
	NSMutableArray* syns = [NSMutableArray arrayWithObject:appName];
    
    return [SDLRPCRequestFactory buildRegisterAppInterfaceWithAppName:appName ttsName:nil vrSynonyms:syns isMediaApp:isMediaApp languageDesired:languageDesired hmiDisplayLanguageDesired:languageDesired appID:appID];
}

+(SDLRegisterAppInterface*) buildRegisterAppInterfaceWithAppName:(NSString*) appName languageDesired:(SDLLanguage*) languageDesired appID:(NSString*) appID{
    
    return [SDLRPCRequestFactory buildRegisterAppInterfaceWithAppName:appName isMediaApp:[NSNumber numberWithBool:NO] languageDesired:languageDesired appID: appID];
}
//*****


+(SDLResetGlobalProperties*) buildResetGlobalPropertiesWithProperties:(NSArray*) properties correlationID:(NSNumber*) correlationID {
	
    SDLResetGlobalProperties* msg = [[[SDLResetGlobalProperties alloc] init] autorelease];
	msg.properties = [[properties mutableCopy] autorelease];
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLScrollableMessage*) buildScrollableMessage:(NSString*) scrollableMessageBody timeout:(NSNumber*) timeout softButtons:(NSArray*) softButtons correlationID:(NSNumber*) correlationID {
	
    SDLScrollableMessage* msg = [[[SDLScrollableMessage alloc] init] autorelease];
    msg.scrollableMessageBody = scrollableMessageBody;
    msg.timeout = timeout;
    msg.softButtons = [[softButtons mutableCopy] autorelease];
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLSetAppIcon*) buildSetAppIconWithFileName:(NSString*) syncFileName correlationID:(NSNumber*) correlationID {
    
    SDLSetAppIcon* msg = [[[SDLSetAppIcon alloc] init] autorelease];
    msg.syncFileName = syncFileName;
    msg.correlationID = correlationID;
    
    return msg;
}

+(SDLSetDisplayLayout*) buildSetDisplayLayout:(NSString*) displayLayout correlationID:(NSNumber*) correlationID {
    
    SDLSetDisplayLayout* msg = [[[SDLSetDisplayLayout alloc] init] autorelease];
    msg.displayLayout = displayLayout;
    msg.correlationID = correlationID;
    
	return msg;
}


//***** SetGlobalProperties *****
+(SDLSetGlobalProperties*) buildSetGlobalPropertiesWithHelpText:(NSString*) helpText timeoutText:(NSString*) timeoutText vrHelpTitle:(NSString*) vrHelpTitle vrHelp:(NSArray*) vrHelp correlationID:(NSNumber*) correlationID {
    
	SDLSetGlobalProperties* msg = [[[SDLSetGlobalProperties alloc] init] autorelease];
	msg.helpPrompt = [SDLTTSChunkFactory buildTTSChunksFromSimple:helpText];
	msg.timeoutPrompt = [SDLTTSChunkFactory buildTTSChunksFromSimple:timeoutText];
    msg.vrHelpTitle = vrHelpTitle;
    msg.vrHelp = [[vrHelp mutableCopy] autorelease];
    msg.correlationID = correlationID;
	
	return msg;
}

+(SDLSetGlobalProperties*) buildSetGlobalPropertiesWithHelpText:(NSString*) helpText timeoutText:(NSString*) timeoutText correlationID:(NSNumber*) correlationID {
	
    SDLSetGlobalProperties* msg = [[[SDLSetGlobalProperties alloc] init] autorelease];
	msg.helpPrompt = [SDLTTSChunkFactory buildTTSChunksFromSimple:helpText];
	msg.timeoutPrompt = [SDLTTSChunkFactory buildTTSChunksFromSimple:timeoutText];
	msg.correlationID = correlationID;
	
	return msg;
}
//*****


//***** SetMediaClockTimer *****
+(SDLSetMediaClockTimer*) buildSetMediaClockTimerWithHours:(NSNumber*) hours minutes:(NSNumber*) minutes seconds:(NSNumber*) seconds updateMode:(SDLUpdateMode*) updateMode correlationID:(NSNumber*) correlationID  {
	
    SDLSetMediaClockTimer* msg = [[[SDLSetMediaClockTimer alloc] init] autorelease];
	SDLStartTime* startTime = [[[SDLStartTime alloc] init] autorelease];
	startTime.hours = hours;
	startTime.minutes = minutes;
	startTime.seconds = seconds;
	msg.startTime = startTime;
	msg.updateMode = updateMode;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLSetMediaClockTimer*) buildSetMediaClockTimerWithUpdateMode:(SDLUpdateMode*) updateMode correlationID:(NSNumber*) correlationID  {
	
    SDLSetMediaClockTimer* msg = [[[SDLSetMediaClockTimer alloc] init] autorelease];
	msg.updateMode = updateMode;
	msg.correlationID = correlationID;
	
	return msg;
}
//*****


//***** Show *****
+(SDLShow*) buildShowWithMainField1:(NSString*) mainField1 mainField2: (NSString*) mainField2 mainField3: (NSString*) mainField3 mainField4: (NSString*) mainField4 statusBar:(NSString*) statusBar mediaClock:(NSString*) mediaClock mediaTrack:(NSString*) mediaTrack alignment:(SDLTextAlignment*) textAlignment graphic:(SDLImage*) graphic softButtons:(NSArray*) softButtons customPresets:(NSArray*) customPresets correlationID:(NSNumber*) correlationID {
	
    SDLShow* msg = [[[SDLShow alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.mainField1 = mainField1;
	msg.mainField2 = mainField2;
    msg.mainField3 = mainField3;
	msg.mainField4 = mainField4;
	msg.statusBar = statusBar;
	msg.mediaClock = mediaClock;
	msg.mediaTrack = mediaTrack;
	msg.alignment = textAlignment;
    msg.graphic = graphic;
    msg.softButtons = [[softButtons mutableCopy] autorelease];
    msg.customPresets = [[customPresets mutableCopy] autorelease];
    
	return msg;
}

+(SDLShow*) buildShowWithMainField1:(NSString*) mainField1 mainField2: (NSString*) mainField2 statusBar:(NSString*) statusBar mediaClock:(NSString*) mediaClock mediaTrack:(NSString*) mediaTrack alignment:(SDLTextAlignment*) textAlignment correlationID:(NSNumber*) correlationID  {
	
    SDLShow* msg = [[[SDLShow alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.mainField1 = mainField1;
	msg.mainField2 = mainField2;
	msg.statusBar = statusBar;
	msg.mediaClock = mediaClock;
	msg.mediaTrack = mediaTrack;
	msg.alignment = textAlignment;
	
	return msg;
}

+(SDLShow*) buildShowWithMainField1:(NSString*) mainField1 mainField2: (NSString*) mainField2 alignment:(SDLTextAlignment*) alignment correlationID:(NSNumber*) correlationID  {
	
    return [SDLRPCRequestFactory buildShowWithMainField1:mainField1 mainField2:mainField2 statusBar:nil mediaClock:nil mediaTrack:nil alignment:alignment correlationID:correlationID];
}
//*****


+(SDLShowConstantTBT*) buildShowConstantTBTWithNavigationText1:(NSString*) navigationText1 navigationText2:(NSString*) navigationText2 eta:(NSString*) eta totalDistance:(NSString*) totalDistance turnIcon:(SDLImage*) turnIcon distanceToManeuver:(NSNumber*) distanceToManeuver distanceToManeuverScale:(NSNumber*) distanceToManeuverScale maneuverComplete:(NSNumber*) maneuverComplete softButtons:(NSArray*) softButtons correlationID:(NSNumber*) correlationID {
    
    SDLShowConstantTBT* msg = [[[SDLShowConstantTBT alloc] init] autorelease];
    
    msg.navigationText1 = navigationText1;
    msg.navigationText2 = navigationText2;
    msg.eta = eta;
    msg.totalDistance = totalDistance;
    
    msg.turnIcon = [[turnIcon mutableCopy] autorelease];
	
    msg.distanceToManeuver = distanceToManeuver;
    msg.distanceToManeuverScale = distanceToManeuverScale;
    msg.maneuverComplete = maneuverComplete;
    
    msg.softButtons = [[softButtons mutableCopy] autorelease];
    
    msg.correlationID = correlationID;
    
    return msg;
}


//***** Slider *****
+(SDLSlider*) buildSliderDynamicFooterWithNumTicks:(NSNumber*) numTicks position:(NSNumber*) position sliderHeader:(NSString*) sliderHeader sliderFooter:(NSArray*) sliderFooter timeout:(NSNumber*) timeout correlationID:(NSNumber*) correlationID {	SDLSlider* msg = [[[SDLSlider alloc] init] autorelease];
	msg.correlationID = correlationID;
    msg.numTicks = numTicks;
    msg.position = position;
    msg.sliderHeader = sliderHeader;
    msg.sliderFooter = [[sliderFooter mutableCopy] autorelease];
	msg.timeout = timeout;
    
    return msg;
}

+(SDLSlider*) buildSliderStaticFooterWithNumTicks:(NSNumber*) numTicks position:(NSNumber*) position sliderHeader:(NSString*) sliderHeader sliderFooter:(NSString*) sliderFooter timeout:(NSNumber*) timeout correlationID:(NSNumber*) correlationID {
	
	NSArray* sliderFooters = [NSArray arrayWithObject:sliderFooter];
    
    // Populates array with the same footer value for each position
    for (UInt32 i = 1; i < numTicks.unsignedIntegerValue; i++) {
        sliderFooters = [sliderFooters arrayByAddingObject:sliderFooter];
	}
    
    return [SDLRPCRequestFactory buildSliderDynamicFooterWithNumTicks:numTicks position:position sliderHeader:sliderHeader sliderFooter:sliderFooters timeout:timeout correlationID:correlationID];
}
//*****


//***** Speak *****
+(SDLSpeak*) buildSpeakWithTTSChunks:(NSArray*) ttsChunks correlationID:(NSNumber*) correlationID  {
	
    SDLSpeak* msg = [[[SDLSpeak alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.ttsChunks = [[ttsChunks mutableCopy] autorelease];
	
	return msg;
}

//***
+(SDLSpeak*) buildSpeakWithTTS:(NSString*) ttsText correlationID:(NSNumber*) correlationID  {
	
    SDLTTSChunk* simpleChunk = [[[SDLTTSChunk alloc] init] autorelease];
	simpleChunk.text = ttsText;
	simpleChunk.type = SDLSpeechCapabilities.TEXT;
	NSArray* ttsChunks = [NSMutableArray arrayWithObject:simpleChunk];
	
    return [SDLRPCRequestFactory buildSpeakWithTTSChunks:ttsChunks correlationID:correlationID];
	
}
//*****


+(SDLSubscribeButton*) buildSubscribeButtonWithName:(SDLButtonName*) buttonName correlationID:(NSNumber*) correlationID {
	
    SDLSubscribeButton* msg = [[[SDLSubscribeButton alloc] init] autorelease];
	msg.correlationID = correlationID;
	msg.buttonName = buttonName;
	
	return msg;
}

+(SDLSubscribeVehicleData*) buildSubscribeVehicleDataWithGPS:(NSNumber*) gps speed:(NSNumber*) speed rpm:(NSNumber*) rpm fuelLevel:(NSNumber*) fuelLevel fuelLevelState:(NSNumber*) fuelLevelState instantFuelConsumption:(NSNumber*) instantFuelConsumption externalTemperature:(NSNumber*) externalTemperature prndl:(NSNumber*) prndl tirePressure:(NSNumber*) tirePressure odometer:(NSNumber*) odometer beltStatus:(NSNumber*) beltStatus bodyInformation:(NSNumber*) bodyInformation deviceStatus:(NSNumber*) deviceStatus driverBraking:(NSNumber*) driverBraking wiperStatus:(NSNumber*) wiperStatus headLampStatus:(NSNumber*) headLampStatus engineTorque:(NSNumber*) engineTorque accPedalPosition:(NSNumber*) accPedalPosition steeringWheelAngle:(NSNumber*) steeringWheelAngle correlationID:(NSNumber*) correlationID {
	
    SDLSubscribeVehicleData* msg = [[[SDLSubscribeVehicleData alloc] init] autorelease];
    msg.gps = gps;
	msg.speed = speed;
	msg.rpm = rpm;
	msg.fuelLevel = fuelLevel;
	msg.fuelLevelState = fuelLevelState;
	msg.instantFuelConsumption = instantFuelConsumption;
	msg.externalTemperature = externalTemperature;
	msg.prndl = prndl;
	msg.tirePressure = tirePressure;
	msg.odometer = odometer;
	msg.beltStatus = beltStatus;
	msg.bodyInformation = bodyInformation;
	msg.deviceStatus = deviceStatus;
    msg.driverBraking = driverBraking;
    msg.wiperStatus = wiperStatus;
    msg.headLampStatus = headLampStatus;
    msg.engineTorque = engineTorque;
    msg.accPedalPosition = accPedalPosition;
    msg.steeringWheelAngle = steeringWheelAngle;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLUnregisterAppInterface*) buildUnregisterAppInterfaceWithCorrelationID:(NSNumber*) correlationID  {
	
    SDLUnregisterAppInterface* msg = [[[SDLUnregisterAppInterface alloc] init] autorelease];
	msg.correlationID = correlationID;
	
    return msg;
}

+(SDLUnsubscribeButton*) buildUnsubscribeButtonWithName:(SDLButtonName*) buttonName correlationID:(NSNumber*) correlationID {
	
    SDLUnsubscribeButton *msg = [[[SDLUnsubscribeButton alloc] init] autorelease];
	msg.buttonName = buttonName;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLUnsubscribeVehicleData*) buildUnsubscribeVehicleDataWithGPS:(NSNumber*) gps speed:(NSNumber*) speed rpm:(NSNumber*) rpm fuelLevel:(NSNumber*) fuelLevel fuelLevelState:(NSNumber*) fuelLevelState instantFuelConsumption:(NSNumber*) instantFuelConsumption externalTemperature:(NSNumber*) externalTemperature prndl:(NSNumber*) prndl tirePressure:(NSNumber*) tirePressure odometer:(NSNumber*) odometer beltStatus:(NSNumber*) beltStatus bodyInformation:(NSNumber*) bodyInformation deviceStatus:(NSNumber*) deviceStatus driverBraking:(NSNumber*) driverBraking wiperStatus:(NSNumber*) wiperStatus headLampStatus:(NSNumber*) headLampStatus engineTorque:(NSNumber*) engineTorque accPedalPosition:(NSNumber*) accPedalPosition steeringWheelAngle:(NSNumber*) steeringWheelAngle correlationID:(NSNumber*) correlationID {
	
    SDLUnsubscribeVehicleData* msg = [[[SDLUnsubscribeVehicleData alloc] init] autorelease];
    msg.gps = gps;
	msg.speed = speed;
	msg.rpm = rpm;
	msg.fuelLevel = fuelLevel;
	msg.fuelLevelState = fuelLevelState;
	msg.instantFuelConsumption = instantFuelConsumption;
	msg.externalTemperature = externalTemperature;
	msg.prndl = prndl;
	msg.tirePressure = tirePressure;
	msg.odometer = odometer;
	msg.beltStatus = beltStatus;
	msg.bodyInformation = bodyInformation;
	msg.deviceStatus = deviceStatus;
    msg.driverBraking = driverBraking;
    msg.wiperStatus = wiperStatus;
    msg.headLampStatus = headLampStatus;
    msg.engineTorque = engineTorque;
    msg.accPedalPosition = accPedalPosition;
    msg.steeringWheelAngle = steeringWheelAngle;
	msg.correlationID = correlationID;
	
	return msg;
}

+(SDLUpdateTurnList*) buildUpdateTurnList:(NSArray*) turnList softButtons:(NSArray*) softButtons correlationID:(NSNumber*) correlationID {
    
    SDLUpdateTurnList* msg = [[[SDLUpdateTurnList alloc] init] autorelease];
    msg.turnList = [[turnList mutableCopy] autorelease];
    msg.softButtons = [[softButtons mutableCopy] autorelease];
    msg.correlationID = correlationID;
    
    return msg;
}

+(NSArray*) buildAddProfileWithName:(NSString*)name rawData:(NSData*)data correlationID:(NSNumber*) correlationID {
    if (name == nil || data == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    return [ProfileBinaryPacketizer createAddProfileRequestsForData:data forProfile:name withCorrId:[correlationID intValue]];
}

+(NSArray*) buildAddProfileForEmbeddedPath:(NSString*)name profileName: (NSString*)profileName correlationID:(NSNumber*) correlationID {
    if (name == nil || profileName == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    NSString * path = [[NSBundle mainBundle] pathForResource:name ofType:nil];
    NSData * fileData = [NSData dataWithContentsOfFile: path];
    if (fileData == nil) {
        [NSException raise:@"File not found!" format:@"File %@ is not present", name];
    }
    return [SDLRPCRequestFactory buildAddProfileWithName:profileName rawData:fileData correlationID:correlationID];
}

+(SDLRemoveProfile*) buildRemoveProfileWithName:(NSString*)name correlationID:(NSNumber*)correlationID {
    if (name == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    SDLRemoveProfile * msg = [[[SDLRemoveProfile alloc] init] autorelease];
    [msg setProfileName: name];
    [msg setCorrelationID: correlationID];
    return msg;
}

+(SDLLoadProfile*) buildLoadProfileWithName:(NSString*)name correlationID:(NSNumber*)correlationID {
    if (name == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    SDLLoadProfile * msg = [[[SDLLoadProfile alloc] init] autorelease];
    [msg setProfileName: name];
    [msg setCorrelationID: correlationID];
    return msg;
}

+(SDLUnloadProfile*) buildUnloadProfileWithName:(NSString*)name correlationID:(NSNumber*)correlationID {
    if (name == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    SDLUnloadProfile * msg = [[[SDLUnloadProfile alloc] init] autorelease];
    [msg setProfileName: name];
    [msg setCorrelationID: correlationID];
    return msg;
}

+(SDLAppStateChanged*) buildAppStateChangedWithProfileName:(NSString*)name applicationState:(SDLMobileAppState*)state correlationID:(NSNumber*)correlationID {
    if (name == nil || state == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    SDLAppStateChanged * msg = [[[SDLAppStateChanged alloc] init] autorelease];
    [msg setProfileName: name];
    [msg setMobileAppState: state];
    [msg setCorrelationID: correlationID];
    return msg;
}

+(SDLSendAppToProfileMessage*) buildSendAppToProfileMessageWithName: (NSString*)name rawData:(NSData*)data correlationID:(NSNumber*)correlationID {
    if (name == nil || data == nil || correlationID == nil) {
        [NSException raise:@"Invalid params!" format:@"Some of the params are nil!"];
    }
    SDLSendAppToProfileMessage * msg = [[[SDLSendAppToProfileMessage alloc] init] autorelease];
    [msg setProfileName: name];
    [msg setMessageData: data];
    [msg setCorrelationID: correlationID];
    return msg;
}
@end
