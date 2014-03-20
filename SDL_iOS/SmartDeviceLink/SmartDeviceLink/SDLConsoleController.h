//  SDLConsoleController.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SDLDebugTool.h>

#import <SmartDeviceLink/SDLRPCMessage.h>

@interface SDLConsoleController : UITableViewController <SDLDebugToolConsole> {
	NSMutableArray* messageList;
    BOOL atBottom;
    NSDateFormatter* dateFormatter;
}

@property (readonly) NSMutableArray *messageList;

-(id) initWithTableView:(UITableView*) tableView;

-(void) appendString:(NSString*) toAppend;
-(void) appendMessage:(SDLRPCMessage*) toAppend;

@end
