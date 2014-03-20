//  SmartDeviceLinkTester.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

@interface SmartDeviceLinkTester : NSObject <SDLProxyListener, SDLTcpDiscovererDefaultListenerDelegate> {
    
    int autoIncCorrID;
    int cmdID;
    BOOL firstTimeStartUp;
    NSMutableData *audioPassThruData;
}

@property(nonatomic, retain) SDLProxy* proxy;

+(SmartDeviceLinkTester*) getInstance;
-(NSNumber*) getNextCorrID;
-(void) sendAndPostRPCMessage:(SDLRPCRequest *)rpcMsg;
-(void) postToConsoleLog:(id) object;

-(int) getCmdID;
-(void) incCmdID;

-(void) setupProxy;
-(void) cycleProxy;


// =====================================
//// RPC Function Calls
// =====================================
-(void) buildEndAudioPassThru;

-(void) deleteSubMenuPressedwithID:(NSNumber *)menuID;

-(void) performInteractionPressedwithInitialPrompt:(NSArray*)initialChunks initialText:(NSString*)initialText interactionChoiceSetIDList:(NSArray*) interactionChoiceSetIDList helpChunks:(NSArray*)helpChunks timeoutChunks:(NSArray*)timeoutChunks interactionMode:(SDLInteractionMode*) interactionMode timeout:(NSNumber*)timeout;

-(void) unsubscribeButtonPressed:(SDLButtonName *)buttonName;

-(void) subscribeButtonPressed:(SDLButtonName *)buttonName;

-(void) buildPerformAudioPassThru:(NSString*) initialPrompt audioPassThruDisplayText1:(NSString*) audioPassThruDisplayText1 audioPassThruDisplayText2:(NSString*) audioPassThruDisplayText2 samplingRate:(SDLSamplingRate*) samplingRate maxDuration:(NSNumber*) maxDuration bitsPerSample:(SDLBitsPerSample*) bitsPerSample audioType:(SDLAudioType*) audioType muteAudio:(NSNumber*) muteAudio;

-(void) addSubMenuPressedwithID:(NSNumber *)menuID menuName:(NSString *)menuName position:(NSNumber *)position;

-(void) deleteCommandPressed:(NSNumber *)cmdID;


@end


