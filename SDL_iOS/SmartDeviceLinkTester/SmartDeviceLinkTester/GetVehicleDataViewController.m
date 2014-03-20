//  GetVehicleDataViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "GetVehicleDataViewController.h"
#import "AppDelegate.h"

@interface GetVehicleDataViewController ()

@end

@implementation GetVehicleDataViewController

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *chosenVehicleData =  cell.textLabel.text;
    
    SDLGetVehicleData *req = [[SDLGetVehicleData alloc] init];
    
    if ([chosenVehicleData isEqualToString:@"GPS"]) {
        req.gps = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"Speed"]) {
        req.speed = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"RPM"]) {
        req.rpm = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"FuelLevel"]) {
        req.fuelLevel = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"FuelLevelState"]) {
        req.fuelLevelState = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"InstantFuelConsuption"]) {
        req.instantFuelConsumption = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"ExternalTemperature"]) {
        req.externalTemperature = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"VIN"]) {
        req.vin = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"PRNDL"]) {
        req.prndl = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"TirePressure"]) {
        req.tirePressure = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"Odometer"]) {
        req.odometer = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"BeltStatus"]) {
        req.beltStatus = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"BodyInformation"]) {
        req.bodyInformation = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"DeviceStatus"]) {
        req.deviceStatus = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"DriverBraking"]) {
        req.driverBraking = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"WiperStatus"]) {
        req.wiperStatus = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"HeadLampStatus"]) {
        req.headLampStatus = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"EngineTorque"]) {
        req.engineTorque = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"AccPedalPosition"]) {
        req.accPedalPosition = [NSNumber numberWithBool:true];
    }
    else if ([chosenVehicleData isEqualToString:@"SteeringWheelAngle"]) {
        req.steeringWheelAngle = [NSNumber numberWithBool:true];
    }
    
    req.correlationID = [[SmartDeviceLinkTester getInstance] getNextCorrID];
    [[SmartDeviceLinkTester getInstance] sendAndPostRPCMessage:req];
        
    //Go Back To RPC List View
    [self.navigationController popToRootViewControllerAnimated:YES];
    
    //Go To Console View
    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    appDelegate.tabBarController.selectedViewController = [appDelegate.tabBarController.viewControllers objectAtIndex:1];
    
    [req release];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Select Vehicle Data";
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [vehicleDataList count]; 
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) { 
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease]; 
    } 
    
    // Configure the cell. 
    cell.textLabel.text = [vehicleDataList objectAtIndex:indexPath.row]; 
    
    return cell;
}



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"GetVehicleData";
        
    NSArray *tempArray = [NSArray arrayWithObjects: @"GPS",
                          @"Speed",
                          @"RPM",
                          @"FuelLevel",
                          @"FuelLevelState",
                          @"InstantFuelConsuption",
                          @"ExternalTemperature",
                          @"VIN",
                          @"PRNDL",
                          @"TirePressure",
                          @"Odometer",
                          @"BeltStatus",
                          @"BodyInformation",
                          @"DeviceStatus",
                          @"DriverBraking",
                          @"WiperStatus",
                          @"HeadLampStatus",
                          @"EngineTorque",
                          @"AccPedalPosition",
                          @"SteeringWheelAngle",
                          nil];
    
    vehicleDataList = [[NSMutableArray alloc] init];
    [vehicleDataList addObjectsFromArray:tempArray];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)dealloc {
    [vehicleDataList release];
    [super dealloc];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    vehicleDataTable.delegate = self;
    vehicleDataTable.dataSource = self;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
