//  SDLProxyListener.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLAddCommandResponse.h>
#import <SmartDeviceLink/SDLAddSubMenuResponse.h>
#import <SmartDeviceLink/SDLAlertManeuverResponse.h>
#import <SmartDeviceLink/SDLAlertResponse.h>
#import <SmartDeviceLink/SDLChangeRegistrationResponse.h>
#import <SmartDeviceLink/SDLCreateInteractionChoiceSetResponse.h>
#import <SmartDeviceLink/SDLDeleteCommandResponse.h>
#import <SmartDeviceLink/SDLDeleteFileResponse.h>
#import <SmartDeviceLink/SDLDeleteInteractionChoiceSetResponse.h>
#import <SmartDeviceLink/SDLDeleteSubMenuResponse.h>
#import <SmartDeviceLink/SDLEndAudioPassThruResponse.h>
#import <SmartDeviceLink/SDLGenericResponse.h>
#import <SmartDeviceLink/SDLGetDTCsResponse.h>
#import <SmartDeviceLink/SDLGetVehicleDataResponse.h>
#import <SmartDeviceLink/SDLListFilesResponse.h>
#import <SmartDeviceLink/SDLOnAppInterfaceUnregistered.h>
#import <SmartDeviceLink/SDLOnAudioPassThru.h>
#import <SmartDeviceLink/SDLOnButtonEvent.h>
#import <SmartDeviceLink/SDLOnButtonPress.h>
#import <SmartDeviceLink/SDLOnCommand.h>
#import <SmartDeviceLink/SDLOnDriverDistraction.h>
#import <SmartDeviceLink/SDLOnHMIStatus.h>
#import <SmartDeviceLink/SDLOnLanguageChange.h>
#import <SmartDeviceLink/SDLOnPermissionsChange.h>
#import <SmartDeviceLink/SDLOnTBTClientState.h>
#import <SmartDeviceLink/SDLOnVehicleData.h>
#import <SmartDeviceLink/SDLPerformAudioPassThruResponse.h>
#import <SmartDeviceLink/SDLPerformInteractionResponse.h>
#import <SmartDeviceLink/SDLPutFileResponse.h>
#import <SmartDeviceLink/SDLReadDIDResponse.h>
#import <SmartDeviceLink/SDLRegisterAppInterfaceResponse.h>
#import <SmartDeviceLink/SDLResetGlobalPropertiesResponse.h>
#import <SmartDeviceLink/SDLScrollableMessageResponse.h>
#import <SmartDeviceLink/SDLSetAppIconResponse.h>
#import <SmartDeviceLink/SDLSetDisplayLayoutResponse.h>
#import <SmartDeviceLink/SDLSetGlobalPropertiesResponse.h>
#import <SmartDeviceLink/SDLSetMediaClockTimerResponse.h>
#import <SmartDeviceLink/SDLShowConstantTBTResponse.h>
#import <SmartDeviceLink/SDLShowResponse.h>
#import <SmartDeviceLink/SDLSliderResponse.h>
#import <SmartDeviceLink/SDLSpeakResponse.h>
#import <SmartDeviceLink/SDLSubscribeButtonResponse.h>
#import <SmartDeviceLink/SDLSubscribeVehicleDataResponse.h>
#import <SmartDeviceLink/SDLUnregisterAppInterfaceResponse.h>
#import <SmartDeviceLink/SDLUnsubscribeButtonResponse.h>
#import <SmartDeviceLink/SDLUnsubscribeVehicleDataResponse.h>
#import <SmartDeviceLink/SDLUpdateTurnListResponse.h>


#import <SmartDeviceLink/SDLAddProfileResponse.h>
#import <SmartDeviceLink/SDLRemoveProfileResponse.h>
#import <SmartDeviceLink/SDLLoadProfileResponse.h>
#import <SmartDeviceLink/SDLUnloadProfileResponse.h>
#import <SmartDeviceLink/SDLSendAppToProfileMessageResponse.h>
#import <SmartDeviceLink/SDLAppStateChangedResponse.h>
#import <SmartDeviceLink/SDLOnProfileToAppMessage.h>
#import <SmartDeviceLink/SDLOnProfileUnloaded.h>

@protocol SDLProxyListener

-(void) onOnDriverDistraction:(SDLOnDriverDistraction*) notification;
-(void) onOnHMIStatus:(SDLOnHMIStatus*) notification;
-(void) onProxyClosed;
-(void) onProxyOpened;

@optional

-(void) onAddCommandResponse:(SDLAddCommandResponse*) response;
-(void) onAddSubMenuResponse:(SDLAddSubMenuResponse*) response;
-(void) onAlertManeuverResponse:(SDLAlertManeuverResponse*) response;
-(void) onAlertResponse:(SDLAlertResponse*) response;
-(void) onChangeRegistrationResponse:(SDLChangeRegistrationResponse*) response;
-(void) onCreateInteractionChoiceSetResponse:(SDLCreateInteractionChoiceSetResponse*) response;
-(void) onDeleteCommandResponse:(SDLDeleteCommandResponse*) response;
-(void) onDeleteFileResponse:(SDLDeleteFileResponse*) response;
-(void) onDeleteInteractionChoiceSetResponse:(SDLDeleteInteractionChoiceSetResponse*) response;
-(void) onDeleteSubMenuResponse:(SDLDeleteSubMenuResponse*) response;
-(void) onEndAudioPassThruResponse:(SDLEndAudioPassThruResponse*) response;
-(void) onError:(NSException*) e;
-(void) onGenericResponse:(SDLGenericResponse*) response;
-(void) onGetDTCsResponse:(SDLGetDTCsResponse*) response;
-(void) onGetVehicleDataResponse:(SDLGetVehicleDataResponse*) response;
-(void) onListFilesResponse:(SDLListFilesResponse*) response;
-(void) onOnAppInterfaceUnregistered:(SDLOnAppInterfaceUnregistered*) notification;
-(void) onOnAudioPassThru:(SDLOnAudioPassThru*) notification;
-(void) onOnButtonEvent:(SDLOnButtonEvent*) notification;
-(void) onOnButtonPress:(SDLOnButtonPress*) notification;
-(void) onOnCommand:(SDLOnCommand*) notification;
-(void) onOnLanguageChange:(SDLOnLanguageChange*) notification;
-(void) onOnPermissionsChange:(SDLOnPermissionsChange*) notification;
-(void) onOnTBTClientState:(SDLOnTBTClientState*) notification;
-(void) onOnVehicleData:(SDLOnVehicleData*) notification;
-(void) onPerformAudioPassThruResponse:(SDLPerformAudioPassThruResponse*) response;
-(void) onPerformInteractionResponse:(SDLPerformInteractionResponse*) response;
-(void) onPutFileResponse:(SDLPutFileResponse*) response;
-(void) onReadDIDResponse:(SDLReadDIDResponse*) response;
-(void) onRegisterAppInterfaceResponse:(SDLRegisterAppInterfaceResponse*) response;
-(void) onResetGlobalPropertiesResponse:(SDLResetGlobalPropertiesResponse*) response;
-(void) onScrollableMessageResponse:(SDLScrollableMessageResponse*) response;
-(void) onSetAppIconResponse:(SDLSetAppIconResponse*) response;
-(void) onSetDisplayLayoutResponse:(SDLSetDisplayLayoutResponse*) response;
-(void) onSetGlobalPropertiesResponse:(SDLSetGlobalPropertiesResponse*) response;
-(void) onSetMediaClockTimerResponse:(SDLSetMediaClockTimerResponse*) response;
-(void) onShowConstantTBTResponse:(SDLShowConstantTBTResponse*) response;
-(void) onShowResponse:(SDLShowResponse*) response;
-(void) onSliderResponse:(SDLSliderResponse*) response;
-(void) onSpeakResponse:(SDLSpeakResponse*) response;
-(void) onSubscribeButtonResponse:(SDLSubscribeButtonResponse*) response;
-(void) onSubscribeVehicleDataResponse:(SDLSubscribeVehicleDataResponse*) response;
-(void) onUnregisterAppInterfaceResponse:(SDLUnregisterAppInterfaceResponse*) response;
-(void) onUnsubscribeButtonResponse:(SDLUnsubscribeButtonResponse*) response;
-(void) onUnsubscribeVehicleDataResponse:(SDLUnsubscribeVehicleDataResponse*) response;
-(void) onUpdateTurnListResponse:(SDLUpdateTurnListResponse*) response;


// PROFILES
- (void) onOnProfileToAppMessage: (SDLOnProfileToAppMessage*) notification;
- (void) onOnProfileUnloaded: (SDLOnProfileUnloaded*) notification;

- (void) onAddProfileResponse: (SDLAddProfileResponse*) resp;
- (void) onAppStateChangedResponse: (SDLAppStateChangedResponse*) resp;
- (void) onLoadProfileResponse: (SDLLoadProfileResponse*) resp;
- (void) onRemoveProfileResponse: (SDLRemoveProfileResponse*) resp;
- (void) onUnloadProfileResponse: (SDLUnloadProfileResponse*) resp;
- (void) onSendAppToProfileMessageResponse: (SDLSendAppToProfileMessageResponse*) resp;

@end
