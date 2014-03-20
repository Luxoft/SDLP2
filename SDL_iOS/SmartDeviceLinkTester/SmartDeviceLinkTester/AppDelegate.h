//  AppDelegate.h
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import <UIKit/UIKit.h>

#import "RPCTestViewController.h"
#import "ConsoleViewController.h"


@interface AppDelegate : UIResponder <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIViewController *rpcTestViewController;
    ConsoleViewController *consoleViewController;
    
    UINavigationController *navController;
}
  
@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) UITabBarController *tabBarController;


@end



