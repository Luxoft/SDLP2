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

#import "ApplicationStateChangedViewController.h"
#import "SmartDeviceLinkTester.h"


@interface ApplicationStateChangedViewController ()

@end

@implementation ApplicationStateChangedViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [[self profileNameInput] setDelegate:self];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

- (void)dealloc {
    [_profileNameInput release];
    [_applicationStateControl release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setProfileNameInput:nil];
    [self setApplicationStateControl:nil];
    [super viewDidUnload];
}

- (IBAction)onSendPRCClicked:(id)sender {
    int i = [[self applicationStateControl] selectedSegmentIndex];
    SDLMobileAppState * appState = [[SDLMobileAppState values] objectAtIndex: i];
    SDLAppStateChanged * req = [SDLRPCRequestFactory buildAppStateChangedWithProfileName:[[self profileNameInput] text]
                                                                        applicationState:appState
                                                                           correlationID:@(100500)];
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage: req];
}
@end
