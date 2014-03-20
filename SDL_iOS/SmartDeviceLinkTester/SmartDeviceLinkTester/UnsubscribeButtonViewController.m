//  UnsubscribeButtonViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "UnsubscribeButtonViewController.h"

@interface UnsubscribeButtonViewController ()

-(void)updateTable;

@end

@implementation UnsubscribeButtonViewController

-(void)updateTable {
    [buttonTable reloadData];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *chosenButton =  cell.textLabel.text;
    [selectedButtons removeObject:chosenButton];
    SDLButtonName *buttonSelected;
    if ([chosenButton isEqualToString:@"OK"]) {
        buttonSelected = [SDLButtonName OK];
    }
    else if ([chosenButton isEqualToString:@"Seek Left"]) {
        buttonSelected = [SDLButtonName SEEKLEFT];
    }
    else if ([chosenButton isEqualToString:@"Seek Right"]) {
        buttonSelected = [SDLButtonName SEEKRIGHT];
    }
    else if ([chosenButton isEqualToString:@"Tune Up"]) {
        buttonSelected = [SDLButtonName TUNEUP];
    }
    else if ([chosenButton isEqualToString:@"Tune Down"]) {
        buttonSelected = [SDLButtonName TUNEDOWN];
    }
    else if ([chosenButton isEqualToString:@"Preset 0"]) {
        buttonSelected = [SDLButtonName PRESET_0];
    }
    else if ([chosenButton isEqualToString:@"Preset 1"]) {
        buttonSelected = [SDLButtonName PRESET_1];
    }
    else if ([chosenButton isEqualToString:@"Preset 2"]) {
        buttonSelected = [SDLButtonName PRESET_2];
    }
    else if ([chosenButton isEqualToString:@"Preset 3"]) {
        buttonSelected = [SDLButtonName PRESET_3];
    }
    else if ([chosenButton isEqualToString:@"Preset 4"]) {
        buttonSelected = [SDLButtonName PRESET_4];
    }
    else if ([chosenButton isEqualToString:@"Preset 5"]) {
        buttonSelected = [SDLButtonName PRESET_5];
    }
    else if ([chosenButton isEqualToString:@"Preset 6"]) {
        buttonSelected = [SDLButtonName PRESET_6];
    }
    else if ([chosenButton isEqualToString:@"Preset 7"]) {
        buttonSelected = [SDLButtonName PRESET_7];
    }
    else if ([chosenButton isEqualToString:@"Preset 8"]) {
        buttonSelected = [SDLButtonName PRESET_8];
    }
    else {
        buttonSelected = [SDLButtonName PRESET_9];
    }
    
    [[SmartDeviceLinkTester getInstance] unsubscribeButtonPressed:buttonSelected];
    [self updateTable];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Select a Button";
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [selectedButtons count]; 
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) { 
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease]; 
    } 
    
    // Configure the cell. 
    cell.textLabel.text = [selectedButtons objectAtIndex:indexPath.row]; 
    
    return cell;
}




- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"UnsubscribeButton";
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateTable) name:@"SubscribeButtonRequest" object:nil];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    buttonTable.delegate = self;
    buttonTable.dataSource = self;
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
