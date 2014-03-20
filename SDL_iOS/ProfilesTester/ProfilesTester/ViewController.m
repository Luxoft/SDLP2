/*
 *
 * SDLP SDK
 * Cross Platform Application Communication Stack for In-Vehicle Applications
 *
 * Copyright (C) 2013, Luxoft Professional Corp., member of IBS group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; version 2.1.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 *
 */

#import <SmartDeviceLink/SmartDeviceLink.h>

#import "ViewController.h"
#import "TableLogger.h"
#import "Test.h"
#import "TestSuite.h"

@interface ViewController () {
    SDLProxy * mProxy;
    SDLTcpDiscoverer * mDiscoverer;
    
    TableLogger * mMessagesLogger;
    TableLogger * mResultsLogger;
    
    Test * mCurrentTest;
    NSArray * mTests;
}
-(void)logMessage:(SDLRPCMessage*)message;

-(void)logTest:(int)testID start:(bool)isStart;
-(void)logTestResult:(int)testID sucess:(bool)isSuccess;

-(void)scrollToBottom:(UITableView*)view itemsCount:(NSInteger)itemsCount;
@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    mTests = [TestSuite getTestList];
    mDiscoverer = [[SDLTcpDiscoverer alloc] initWithDefaultListener:self];
    [mDiscoverer performSearch];
    mMessagesLogger = [[TableLogger alloc] initWithName: @"Message log"];
    mResultsLogger = [[TableLogger alloc] initWithName: @"Test results"];
    [[self messagesTable] setDataSource: mMessagesLogger];
    [[self messagesTable] setDelegate: mMessagesLogger];
    [[self resultsTable] setDataSource: mResultsLogger];
    [[self resultsTable] setDelegate: mResultsLogger];
    [[self runTestsButton] setEnabled: NO];
}

- (IBAction)runTestsClicked:(id)sender {
    [sender setEnabled: NO];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        for (id test in mTests) {
            @autoreleasepool {
                [self logTest: [test getId] start:true];
                mCurrentTest = test;
                bool testResuls = [test runTest: self];
                [self logTestResult: [test getId] sucess: testResuls];
                [self logTest: [test getId] start:false];
                if (!testResuls) {
                    break;
                }
            }
        }
    });
}

-(void)onFoundNothing {
    [mDiscoverer performSearch];
}

-(void)onUserCanceledAlert {
    [mDiscoverer performSearch];
}

-(void)onUserSelectedDeviceWithIP:(NSString *)ipaddress tcpPort:(NSString *)port {
    NSLog(@"onUserSelectedDeviceWithIP: %@, %@", ipaddress, port);
    mProxy = [SDLProxyFactory buildProxyWithListener: self
                                        tcpIPAddress: ipaddress
                                             tcpPort: port];
}

-(void) onRegisterAppInterfaceResponse:(SDLRegisterAppInterfaceResponse*) response {
    [[self runTestsButton] setEnabled: YES];
}

-(void) onProxyOpened {
    NSLog(@"onProxyOpened");
    SDLRegisterAppInterface* regRequest = [SDLRPCRequestFactory buildRegisterAppInterfaceWithAppName:@"Profiles Tester"
                                                                                          isMediaApp:@(NO)
                                                                                     languageDesired:[SDLLanguage EN_US]
                                                                                               appID:@"12345"];
    regRequest.isMediaApplication = [NSNumber numberWithBool:YES];
    regRequest.ngnMediaScreenAppName = nil;
	regRequest.vrSynonyms = nil;
    [mProxy sendRPCRequest:regRequest];
}

-(void) onProxyClosed {
    NSLog(@"onProxyClosed");
    exit(0);
}

-(void) onError:(NSException*) e {
    NSLog(@"On Error!");
}


-(void) onOnProfileToAppMessage: (SDLOnProfileToAppMessage*) notification {
    [mCurrentTest onOnProfileToAppMessage: notification];
    [self logMessage: notification];
}

-(void) onOnProfileUnloaded:(SDLOnProfileUnloaded *)notification {
    [mCurrentTest onOnProfileUnloaded: notification];
    [self logMessage: notification];
}

-(void) onAddProfileResponse: (SDLAddProfileResponse*) response {
    [mCurrentTest onAddProfileResponse: response];
}

-(void) onAppStateChangedResponse: (SDLAppStateChangedResponse*) response {
    [mCurrentTest onAppStateChangedResponse: response];
    [self logMessage: response];
}
-(void) onLoadProfileResponse: (SDLLoadProfileResponse*) response {
    [mCurrentTest onLoadProfileResponse: response];
    [self logMessage: response];
}
-(void) onRemoveProfileResponse: (SDLRemoveProfileResponse*) response {
    [mCurrentTest onRemoveProfileResponse: response];
    [self logMessage: response];
}
 
-(void) onUnloadProfileResponse: (SDLUnloadProfileResponse*) response {
    [mCurrentTest onUnloadProfileResponse: response];
    [self logMessage: response];
}

-(void) onSendAppToProfileMessageResponse: (SDLSendAppToProfileMessageResponse*) response {
    [mCurrentTest onSendAppToProfileMessageResponse: response];
    [self logMessage: response];
}

-(void)sendRequest:(SDLRPCRequest *)request {
    dispatch_sync(dispatch_get_main_queue(), ^{
        [self logMessage: request];
    });
    [mProxy sendRPCRequest: request];
}


-(void)logMessage:(SDLRPCMessage*)message {
    if ([message isKindOfClass:[SDLAddProfile class]]) {
        return;
    }
    UIColor * msgColor;
    if ([message isKindOfClass:[SDLRPCRequest class]]) {
        msgColor = [UIColor purpleColor];
    } else if ([message isKindOfClass: [SDLRPCResponse class]]) {
        msgColor = [UIColor blueColor];
    } else if ([message isKindOfClass: [SDLRPCNotification class]]) {
        msgColor = [UIColor orangeColor];
    } else {
        msgColor = [UIColor redColor];
    }
    NSString * popupStr;
    if ([[message messageType] isEqualToString:@"response"])
    {
        SDLRPCResponse * resp = (SDLRPCResponse*)message;
        popupStr = [NSString stringWithFormat:@"Code: %@, description: %@", [resp resultCode], [resp info]];
    }
    else if ([[message messageType] isEqualToString:@"notification"])
    {
        if ([message isKindOfClass: [SDLOnProfileUnloaded class]])
        {
            popupStr = [NSString stringWithFormat:@"Profile closed: %@", [(ProfileManagerNotification*)message profileName]];
        }
        else
        {
            popupStr = [[NSString alloc] initWithBytes:[[message getBulkData] bytes] length:[[message getBulkData] length] encoding:NSUTF8StringEncoding];
        }
    }
    else
    {
        if ([message isKindOfClass: [SDLSendAppToProfileMessage class]])
        {
            popupStr = [NSString stringWithFormat:@"Profile name: %@, data: %@", [(ProfileManagerRequest*)message profileName],
                        [[NSString alloc] initWithBytes:[[message getBulkData] bytes] length:[[message getBulkData] length] encoding:NSUTF8StringEncoding]];
        }
        else if ([message isKindOfClass: [SDLAppStateChanged class]])
        {
            popupStr = [NSString stringWithFormat:@"Profile name: %@, data: %@", [(ProfileManagerRequest*)message profileName], [(SDLAppStateChanged*)message mobileAppState]];
        }
        else
        {
            popupStr =[NSString stringWithFormat:@"Profile name: %@", [(ProfileManagerRequest*)message profileName]];
        }
    }
    NSString * title = [NSString stringWithFormat: @"%@: %@", [message messageType], [message name]];
    LoggedMessage * msg = [[LoggedMessage alloc] initWithMessage:title
                                                           color:msgColor
                                                        popupMsg:popupStr];
    [mMessagesLogger addMessageToTable: msg];
    [[self messagesTable] reloadData];
}


-(void)logTest:(int)testID start:(bool)isStart {
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString * text = [NSString stringWithFormat:@"Test %i %@!", testID, (isStart ? @"started" : @"ended")];
        LoggedMessage * msg = [[LoggedMessage alloc] initWithMessage:text
                                                               color:[UIColor darkGrayColor]
                                                            popupMsg:text];
        [mMessagesLogger addMessageToTable: msg];
        [[self messagesTable] reloadData];
        [self scrollToBottom:[self messagesTable] itemsCount:[mMessagesLogger numberOfEntries]];
    });
}

-(void)logTestResult:(int)testID sucess:(bool)isSuccess {
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString * text = [NSString stringWithFormat:@"Test %i %@!", testID, (isSuccess ? @"passed" : @"failed")];
        LoggedMessage * msg = [[LoggedMessage alloc] initWithMessage:text
                                                               color: (isSuccess? [UIColor greenColor] : [UIColor redColor])
                                                            popupMsg:text];
        [mResultsLogger addMessageToTable: msg];
        [[self resultsTable] reloadData];
        [self scrollToBottom:[self resultsTable] itemsCount:[mResultsLogger numberOfEntries]];
    });
}


-(void)scrollToBottom:(UITableView*)view itemsCount:(NSInteger)itemsCount {
    [view scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:(itemsCount - 1) inSection:0]
                atScrollPosition:UITableViewScrollPositionBottom
                        animated:YES];
}
@end
