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

/**
 * \brief Default implementation of SDLTcpDiscovererDefaultListener; displays a pop-up with list of found devices.
 * \author Elena Bratanova <ebratanova@luxoft.com>
 * \date 28/06/13
 */
#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIKit.h>

#import <SmartDeviceLink/SDLTcpDiscovererDefaultListener.h>
#import "SDLTcpDiscoveredDevice.h"
#import "CustomIOS7AlertView.h"

static const int PADDING = 10;
static const int TABLE_ROW_HEIGHT = 40;
static const int ALERT_WIDTH = 280;
static const int MAX_VISIBLE_ROWS = 4;

@interface SDLTcpDiscovererDefaultListener() {
    id<SDLTcpDiscovererDefaultListenerDelegate> mDelegate;
    NSArray * mListOfDevices;
    CustomIOS7AlertView * mAlert;
    UITableView * mTable;
    UILabel * mLabel;
    UIView * mContainer;
}
- (void)cleanupViews;
@end

@implementation SDLTcpDiscovererDefaultListener

-(id) initWithCallback:(id<SDLTcpDiscovererDefaultListenerDelegate>)callback {
    self = [super init];
    if (self) {
        mDelegate = callback;
        [mDelegate retain];
        mTable = [[UITableView alloc] init];
        [mTable setSeparatorColor: [UIColor colorWithRed:198.0/255.0 green:198.0/255.0 blue:198.0/255.0 alpha:1.0f]];
        [mTable setDelegate: self];
        [mTable setDataSource: self];
        [mTable setScrollEnabled: YES];
        
        mLabel = [[UILabel alloc] initWithFrame: CGRectMake(PADDING, PADDING, ALERT_WIDTH - PADDING * 2, TABLE_ROW_HEIGHT)];
        [mLabel setBackgroundColor: [UIColor clearColor]];
        [mLabel setText: @"Discovered headunits:"];
        [mLabel setTextColor: [UIColor blackColor]];
        [mLabel setFont: [UIFont boldSystemFontOfSize: 16.f]];
        [mLabel setTextAlignment: NSTextAlignmentCenter];
    }
    return self;
}

-(void)dealloc {
    [mListOfDevices release];
    mListOfDevices = nil;
    [mDelegate release];
    mDelegate = nil;
    [mTable release];
    mTable = nil;
    [mLabel release];
    mLabel = nil;
    [mListOfDevices release];
    mListOfDevices = nil;
    [super dealloc];
}

-(void) onFoundDevices:(NSArray *)listOfDevices
{
    NSLog(@"on some devices found");
    if ([listOfDevices count] == 1 && [[[listOfDevices objectAtIndex: 0] adapter] isEqualToString: kUSBTransportName])
    {
        SDLTcpDiscoveredDevice * device = [listOfDevices objectAtIndex: 0];
        [mDelegate onUserSelectedDeviceWithIP: [device ip] tcpPort: [device boundTcpPort]];
        return;
    }
    [mListOfDevices release];
    mListOfDevices = listOfDevices;
    [mListOfDevices retain];
    [mTable reloadData];
    
    mAlert = [[CustomIOS7AlertView alloc] init];
    [mTable setFrame: CGRectMake(PADDING,
                                 TABLE_ROW_HEIGHT + PADDING,
                                 ALERT_WIDTH - PADDING * 2,
                                 ([mListOfDevices count] > MAX_VISIBLE_ROWS ? TABLE_ROW_HEIGHT * MAX_VISIBLE_ROWS : [mListOfDevices count] * TABLE_ROW_HEIGHT) - 1)];
    mContainer = [[UIView alloc] initWithFrame:CGRectMake(0, 0, ALERT_WIDTH, [mTable frame].origin.y  + [mTable frame].size.height + PADDING)];
    [mContainer addSubview: mLabel];
    [mContainer addSubview: mTable];
    [mContainer setBackgroundColor: [UIColor whiteColor]];
    [mAlert setContainerView: mContainer];
    [mAlert setButtonTitles: [NSMutableArray arrayWithObjects: @"Continue searching", nil ]];
    // [mAlert setAlpha: .9f];
    
    [mAlert setOnButtonTouchUpInside:^(CustomIOS7AlertView * alertView, int buttonIndex) {
        [mDelegate onUserCanceledAlert];
        [self cleanupViews];
    }];

    [mAlert show];
    [mTable flashScrollIndicators];
}

-(void) onFoundNothing {
    NSLog(@"on found nothing");
    [mDelegate onFoundNothing];
}

- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell * cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil] autorelease];
    [[cell textLabel] setText:[[mListOfDevices objectAtIndex: indexPath.row] stringDescription]];
    [[cell textLabel] setAdjustsFontSizeToFitWidth: YES];
    [[cell textLabel] setAdjustsLetterSpacingToFitWidth: YES];
    [[cell textLabel] setTextAlignment: NSTextAlignmentCenter];
    return cell;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    [cell setBackgroundColor: [UIColor colorWithRed:250.0/255.0 green:250.0/255.0 blue:250.0/255.0 alpha:1.0f]];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [mListOfDevices count];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return TABLE_ROW_HEIGHT;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * port = [[mListOfDevices objectAtIndex:indexPath.row] boundTcpPort];
    NSString * ip = [[mListOfDevices objectAtIndex:indexPath.row] ip];
    [mDelegate onUserSelectedDeviceWithIP:ip tcpPort:port];
    [self cleanupViews];
    [mListOfDevices release];
    mListOfDevices = nil;
}

- (void)cleanupViews
{
    [mAlert close];
    [mAlert release];
    mAlert = nil;
    [mContainer release];
    mContainer = nil;
}

@end
