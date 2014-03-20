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

#import "TableLogger.h"
@interface TableLogger()
{
    NSMutableArray * mTableData;
    NSString * mName;
}
@end
@implementation TableLogger

-(id)initWithName:(NSString *)name {
    self = [super init];
    if (self) {
        mName = [name copy];
        mTableData = [[NSMutableArray alloc] init];
    }
    return self;
}

-(void) addMessageToTable:(LoggedMessage*)message
{
    if (message == nil)
    {
        return;
    }
    [mTableData addObject: message];
}

-(NSInteger)numberOfEntries
{
    return [mTableData count];
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [mTableData count];
}

-(NSString*)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return mName;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString* cellID = @"Cell";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier: cellID];
    if (!cell)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellID];
    }
    LoggedMessage * msg = [mTableData objectAtIndex: [indexPath row]];
    [[cell textLabel] setText: [msg tableMessage]];
    [[cell textLabel] setTextColor: [msg tableMessageColor]];
    [[cell textLabel] setAdjustsFontSizeToFitWidth:YES];
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString * message = [[mTableData objectAtIndex: [indexPath row]] popupMessage];
    UIAlertView * alert = [[UIAlertView alloc] initWithTitle:mName message:message delegate:nil cancelButtonTitle:@"Dismiss" otherButtonTitles: nil];
    [alert show];
}
@end
