//  softButtonListViewController.m
//  SmartDeviceLinkTester
//  Copyright (c) 2013 Ford Motor Company

#import "SoftButtonListViewController.h"
#import "SoftButtonEditViewController.h"


@implementation SoftButtonListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self.title = @"SoftButtons";
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)dealloc {
    [super dealloc];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIBarButtonItem *addButton = [[UIBarButtonItem alloc] initWithTitle:@"Add/Del" style:UIBarButtonItemStyleBordered target:self action:@selector(editTable:)];
	[self.navigationItem setRightBarButtonItem:addButton];
    
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

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

-(void)viewDidAppear:(BOOL)animated
{
    [buttonTable reloadData];
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	int count = [sbList count];
	if(self.editing) count++;
	return count;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    NSLog(@"Method called %@", [(SDLSoftButton *)[sbList objectAtIndex:indexPath.row] text]);
    
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil)
	{
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    int count = 0;
	if(self.editing && indexPath.row != 0)
		count = 1;
    
	// Set up the cell...
	if(indexPath.row == ([sbList count]) && self.editing)
	{
		cell.textLabel.text = @"Add New Soft Button";
		return cell;
	}
    cell.textLabel.text = [NSString stringWithFormat:@"SBT_%@: %@",
                           [[(SDLSoftButton *)[sbList objectAtIndex:indexPath.row] type] value],
                           [(SDLSoftButton *)[sbList objectAtIndex:indexPath.row] text]];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	
    SoftButtonEditViewController *softButtonEditViewController = [[SoftButtonEditViewController alloc] initWithNibName:@"SoftButtonEditViewController" bundle:nil];
    
    softButtonEditViewController.softButton = [sbList objectAtIndex:indexPath.row];

    [self.navigationController pushViewController:softButtonEditViewController animated:YES];
}

- (IBAction) editTable:(id)sender
{
	if(self.editing)
	{
		[super setEditing:NO animated:NO];
		[buttonTable setEditing:NO animated:NO];
		[buttonTable reloadData];
		[self.navigationItem.rightBarButtonItem setTitle:@"Add/Del"];
		[self.navigationItem.rightBarButtonItem setStyle:UIBarButtonItemStylePlain];
	}
	else
	{
		[super setEditing:YES animated:YES];
		[buttonTable setEditing:YES animated:YES];
		[buttonTable reloadData];
		[self.navigationItem.rightBarButtonItem setTitle:@"Done"];
		[self.navigationItem.rightBarButtonItem setStyle:UIBarButtonItemStyleDone];
	}
}

// The editing style for a row is the kind of button displayed to the left of the cell when in editing mode.
- (UITableViewCellEditingStyle)tableView:(UITableView *)aTableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // No editing style if not editing or the index path is nil.
    if (self.editing == NO || !indexPath) return UITableViewCellEditingStyleNone;
    // Determine the editing style based on whether the cell is a placeholder for adding content or already
    // existing content. Existing content can be deleted.
    if (self.editing && indexPath.row == ([sbList count]))
	{
		return UITableViewCellEditingStyleInsert;
	} else
	{
		return UITableViewCellEditingStyleDelete;
	}
    return UITableViewCellEditingStyleNone;
}

// Update the data model according to edit actions delete or insert.
- (void)tableView:(UITableView *)aTableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle
forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete)
	{
        [sbList removeObjectAtIndex:indexPath.row];
		[buttonTable reloadData];
    } else if (editingStyle == UITableViewCellEditingStyleInsert)
	{
        SDLSoftButton *sb = [[[SDLSoftButton alloc] init] autorelease];
        sb.softButtonID = [NSNumber numberWithInt:5020];
        sb.text = @"New SB";
        sb.type = [SDLSoftButtonType TEXT];
        sb.isHighlighted = [NSNumber numberWithBool:false];
        sb.systemAction = [SDLSystemAction DEFAULT_ACTION];
        [sbList insertObject:sb atIndex:[sbList count]];
		[buttonTable reloadData];
    }
}

-(void)updateList:(NSMutableArray *) list
{
    sbList = list;
}

@end
