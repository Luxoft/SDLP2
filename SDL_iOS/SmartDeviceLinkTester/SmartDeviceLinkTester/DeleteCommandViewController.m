//  DeleteCommandViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "DeleteCommandViewController.h"

@interface DeleteCommandViewController ()

-(AddMenuOption *)returnAddMenuOptionWithMenuName:(NSString *)menuName;
-(void)updateTable;

@end

@implementation DeleteCommandViewController

-(void)updateTable {
//    [SDLDebugTool logInfo:@"reloadData with Table"];
    [commandsTable reloadData];
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *chosenCommand =  cell.textLabel.text;
    
    AddMenuOption *amo = [self returnAddMenuOptionWithMenuName:chosenCommand];
    
//    [SDLDebugTool logInfo:@"Menu Option Selected = %@", [amo menuName]];
    
    [[SmartDeviceLinkTester getInstance] deleteCommandPressed:[amo menuID]];
     
    [commandsIssued removeObject:amo];
    [self updateTable];
}

     
-(AddMenuOption *)returnAddMenuOptionWithMenuName:(NSString *)menuName {
    for (int i = 0; i < [commandsIssued count]; i++) {
        if ([menuName isEqualToString:[[commandsIssued objectAtIndex:i] menuName]]) {
            return [commandsIssued objectAtIndex:i];
        }
             
    }
    return [commandsIssued lastObject];
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Select a Command";
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [commandsIssued count]; 
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) { 
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease]; 
    }
    
    @try {
        // Configure the cell. 
        cell.textLabel.text = [NSString stringWithFormat:@"%@",[[commandsIssued objectAtIndex:indexPath.row] menuName]]; 
    }
    @catch (NSException *exception) {

    }
    @finally {

    }
    
    return cell;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"DeleteCommand";
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateTable) name:@"AddCommandRequest" object:nil];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    commandsTable.delegate = self;
    commandsTable.dataSource = self;
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
