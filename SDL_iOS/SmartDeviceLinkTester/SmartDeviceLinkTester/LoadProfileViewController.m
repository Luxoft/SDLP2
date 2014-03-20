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
#import "SmartDeviceLinkTester.h"
#import "LoadProfileViewController.h"

@interface LoadProfileViewController ()

@end

@implementation LoadProfileViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [[self profileNameInput] setDelegate: self];
    [self setTitle: @"Load Profile"];
}

- (void)dealloc {
    [_profileNameInput release];
    [super dealloc];
}

- (void)viewDidUnload {
    [self setProfileNameInput:nil];
    [super viewDidUnload];
}

- (IBAction)onSendRPCButtonClicked:(id)sender {
    SDLLoadProfile * req = [SDLRPCRequestFactory buildLoadProfileWithName:[[self profileNameInput] text]
                                                            correlationID:@(100500)];
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
}
@end
