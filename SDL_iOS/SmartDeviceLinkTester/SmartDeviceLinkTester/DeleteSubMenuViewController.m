//  DeleteSubMenuViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "DeleteSubMenuViewController.h"

@interface DeleteSubMenuViewController ()

-(AddMenuOption *)returnAddMenuOptionWithMenuName:(NSString *)menuName;
-(void)updateTable;


@end

@implementation DeleteSubMenuViewController

-(void)updateTable {
    [subMenuTable reloadData];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSString *chosenSubMenu =  cell.textLabel.text;
    
    AddMenuOption *amo = [self returnAddMenuOptionWithMenuName:chosenSubMenu];
    
    [[SmartDeviceLinkTester getInstance] deleteSubMenuPressedwithID:[amo menuID]];
    
    [subMenuIssued removeObject:amo];
    [self updateTable];
}


-(AddMenuOption *)returnAddMenuOptionWithMenuName:(NSString *)menuName {
    for (int i = 0; i < [subMenuIssued count]; i++) {
        if ([menuName isEqualToString:[[subMenuIssued objectAtIndex:i] menuName]]) {
            return [subMenuIssued objectAtIndex:i];
        }
    }
    return [subMenuIssued lastObject];
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Select a Sub Menu";
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [subMenuIssued count]; 
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) { 
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease]; 
    } 
    
    // Configure the cell. 
    cell.textLabel.text = [NSString stringWithFormat:@"%@",[[subMenuIssued objectAtIndex:indexPath.row] menuName]]; 
    
    return cell;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"DeleteSubMenu";
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateTable) name:@"AddSubMenuRequest" object:nil];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    subMenuTable.delegate = self;
    subMenuTable.dataSource = self;
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
