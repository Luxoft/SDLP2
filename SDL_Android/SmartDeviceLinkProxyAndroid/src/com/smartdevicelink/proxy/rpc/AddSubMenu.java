package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

/**
 * Add a SubMenu to the Command Menu
 * <p>
 * A SubMenu can only be added to the Top Level Menu (i.e.a SunMenu cannot be
 * added to a SubMenu), and may only contain commands as children
 * <p>
 * <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUD</b>
 * </p>
 * 
 * @since SmartDeviceLink 1.0
 * @see DeleteSubMenu
 * @see AddCommand
 * @see DeleteCommand
 */
public class AddSubMenu extends RPCRequest {

	/**
	 * Constructs a new AddSubMenu object
	 */
	public AddSubMenu() {
        super("AddSubMenu");
    }
	/**
	 * Constructs a new AddSubMenu object indicated by the Hashtable parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public AddSubMenu(Hashtable hash) {
        super(hash);
    }
	/**
	 * Returns an <i>Integer</i> object representing the Menu ID that identifies
	 * a sub menu
	 * <p>
	 * 
	 * @return Integer -an integer representing the Menu ID that identifies a sub
	 *         menu
	 */
    public Integer getMenuID() {
        return (Integer) parameters.get( Names.menuID );
    }
	/**
	 * Sets a Menu ID that identifies a sub menu. This value is used in
	 * {@linkplain AddCommand} to which SubMenu is the parent of the command
	 * being added
	 * <p>
	 * 
	 * @param menuID
	 *            an integer object representing a Menu ID
	 *            <p>
	 *            <b>Notes:</b> Min Value: 0; Max Value: 2000000000
	 */    
    public void setMenuID( Integer menuID ) {
        if (menuID != null) {
            parameters.put(Names.menuID, menuID );
        }
    }
	/**
	 * Returns an <i>Integer</i> object representing the position of menu
	 * <p>
	 * 
	 * @return Integer -the value representing the relative position of menus
	 */    
    public Integer getPosition() {
        return (Integer) parameters.get( Names.position );
    }
	/**
	 * Sets a position of menu
	 * 
	 * @param position
	 *            An Integer object representing the position within the items
	 *            of the top level Command Menu. 0 will insert at the front, 1
	 *            will insert after the first existing element, etc. Position of
	 *            any submenu will always be located before the return and exit
	 *            options
	 *            <p>
	 *            <b>Notes: </b><br/>
	 *            <ul>
	 *            <li>
	 *            Min Value: 0; Max Value: 1000</li>
	 *            <li>If position is greater or equal than the number of items
	 *            on top level, the sub menu will be appended by the end</li>
	 *            <li>If this parameter is omitted, the entry will be added at
	 *            the end of the list</li>
	 *            </ul>
	 */    
    public void setPosition( Integer position ) {
        if (position != null) {
            parameters.put(Names.position, position );
        }
    }
	/**
	 * Returns String which is displayed representing this submenu item
	 * 
	 * @return String -a Submenu item's name
	 */
    public String getMenuName() {
        return (String) parameters.get( Names.menuName );
    }
	/**
	 * Sets a menuName which is displayed representing this submenu item
	 * 
	 * @param menuName
	 *            String which will be displayed representing this submenu item
	 */    
    public void setMenuName( String menuName ) {
        if (menuName != null) {
            parameters.put(Names.menuName, menuName );
        }
    }
}
