//  SDLConsoleController.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLConsoleController.h>

#import <SmartDeviceLink/SDLJSONEncoder.h>
#import <SmartDeviceLink/SDLRPCResponse.h>

@implementation SDLConsoleController

@synthesize messageList;

-(id) initWithTableView:(UITableView*) tableView {
    if (self = [super initWithStyle:UITableViewStylePlain]) {
        self.tableView = tableView;
        self.tableView.delegate = self;
        self.tableView.dataSource = self;
        [self viewDidLoad];
    }
    return self;
}


-(void) append:(id) toAppend {
	//Insert the new data
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    
    [dictionary setObject:toAppend forKey:@"object"];
    [dictionary setObject:[NSDate date] forKey:@"date"];
    
	[messageList addObject:dictionary];
	NSIndexPath *newIndex = [NSIndexPath indexPathForRow:(messageList.count - 1) inSection:0];
	[self.tableView insertRowsAtIndexPaths:[NSArray arrayWithObject:newIndex] withRowAnimation:UITableViewRowAnimationBottom];
    
	//If we were at the bottom, scroll to the new bottom.
	if (atBottom) {
		[self.tableView scrollToRowAtIndexPath:newIndex atScrollPosition:UITableViewScrollPositionBottom animated:YES];
	}
}

-(BOOL) isLastRowVisible {
    if (messageList.count == 0) {
		return YES;
	} else {
		NSIndexPath *lastIndex = [NSIndexPath indexPathForRow:(messageList.count - 1) inSection:0];
		
		NSArray* visibleRowIndexes = [self.tableView indexPathsForVisibleRows];
		for (NSIndexPath* aPath in visibleRowIndexes) {
			if ([aPath compare:lastIndex] == NSOrderedSame) {
				return YES;
			}
		}
	}
    return NO;
    
}

-(void) appendString:(NSString*) toAppend {
	[self append:toAppend];
}
-(void) appendMessage:(SDLRPCMessage*) toAppend{
	[self append:toAppend];
}

#pragma mark -
#pragma mark View lifecycle


- (void)viewDidLoad {
    [super viewDidLoad];
    atBottom = YES;
	
	messageList = [[NSMutableArray alloc] initWithCapacity:100];
    dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"hh:mm:ss.SSS";
}

#pragma mark -
#pragma mark Scroll View Delegate

-(void) updateWhetherScrolledToBottom {
    if ([self isLastRowVisible]) {
        atBottom = YES;
    } else {
        atBottom = NO;
    }
}

-(void) scrollViewDidEndDecelerating:(UIScrollView*) scrollView {
    [self updateWhetherScrolledToBottom];
}

-(void) scrollViewDidEndDragging:(UIScrollView*) scrollView willDecelerate:(BOOL) willDecelerate {
    [self updateWhetherScrolledToBottom];
}

#pragma mark -
#pragma mark Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return messageList.count;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
    }
    
    NSDictionary *currentDictionary = [messageList objectAtIndex:indexPath.row];
    id msg = [currentDictionary objectForKey:@"object"];

    NSString* tempdetail = [@"Time: " stringByAppendingString:[dateFormatter stringFromDate:[currentDictionary objectForKey:@"date"]]];
    
    if ([msg isKindOfClass:SDLRPCMessage.class]) {
		SDLRPCMessage *rpc = msg;
		
		NSString* title = [NSString stringWithFormat:@"%@ (%@)", rpc.name, rpc.messageType];
        cell.textLabel.text = title;
        
		if ([rpc.messageType isEqualToString:@"response"]) {
			SDLRPCResponse* response = (SDLRPCResponse*) rpc;
            NSString* detail = [NSString stringWithFormat:@"%@ - %@", tempdetail, [response resultCode]];
            cell.detailTextLabel.text = detail;
		}
        else {
			cell.detailTextLabel.text = tempdetail;
		}
		
	} else {
		cell.textLabel.text = msg;
        cell.detailTextLabel.text = tempdetail;
	}
    
    return cell;
}

#pragma mark -
#pragma mark Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary *currentDictionary = [messageList objectAtIndex:indexPath.row];
    id obj = [currentDictionary objectForKey:@"object"];

	NSString* alertText = nil;
	if ([obj isKindOfClass:SDLRPCMessage.class]) {
        
		SDLRPCMessage *rpc = obj;
        
		NSData *jsonData = [[SDLJsonEncoder instance] encodeDictionary:[rpc serializeAsDictionary:2]];
		alertText = [[[NSString alloc] initWithBytes:jsonData.bytes length:jsonData.length encoding:NSUTF8StringEncoding] autorelease];
				
	} else {
		alertText = [NSString stringWithFormat:@"%@",obj];
	}
	
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"RPCMessage" message:alertText delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alertView show];
	[alertView release];
    
	[self.tableView deselectRowAtIndexPath:indexPath animated:YES];
	
}

-(void) logInfo:(NSString*) info {
	[self appendString:info];
}

-(void) logException:(NSException*) ex withMessage:(NSString*) message {
	[self appendString:message];
	[self append:[ex description]];
}
	 
#pragma mark -
#pragma mark Memory management

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Relinquish ownership any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    // Relinquish ownership of anything that can be recreated in viewDidLoad or on demand.
    // For example: self.myOutlet = nil;
    [messageList release];
    [dateFormatter release];
}

- (void)dealloc {
    [super dealloc];
}

@end
