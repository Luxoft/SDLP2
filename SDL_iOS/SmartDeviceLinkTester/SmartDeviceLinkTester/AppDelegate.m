//  AppDelegate.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "AppDelegate.h"

@implementation AppDelegate

@synthesize window;
@synthesize tabBarController;


- (void)dealloc
{
    [self.window release];
    [self.tabBarController release];
    [super dealloc];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]] autorelease];
    
    // Override point for customization after application launch.
    rpcTestViewController = [[RPCTestViewController alloc] initWithStyle:UITableViewStyleGrouped];
    consoleViewController = [[[ConsoleViewController alloc] initWithNibName:@"ConsoleViewController" bundle:nil] autorelease];

    navController = nil;
    navController = [[UINavigationController alloc] initWithRootViewController:rpcTestViewController];
    

    [rpcTestViewController release];
    
    self.tabBarController = [[[UITabBarController alloc] init] autorelease];
    
    self.tabBarController.viewControllers = [NSArray arrayWithObjects:navController, consoleViewController, nil];
    
    self.tabBarController.selectedIndex = 1;
    self.window.rootViewController = self.tabBarController;
    [self.window makeKeyAndVisible];
    
    [[SmartDeviceLinkTester getInstance] setupProxy];
    
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{        
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    
    [navController release];
    navController = nil;
}


@end
