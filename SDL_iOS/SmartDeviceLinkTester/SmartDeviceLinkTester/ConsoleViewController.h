//  ConsoleViewController.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SmartDeviceLink.h>

@interface ConsoleViewController : UIViewController <SDLDebugToolConsole> {
    UITableView* consoleView;
    SDLConsoleController *consoleController;
}

@property (nonatomic, retain) IBOutlet UITableView* consoleView;


@end
