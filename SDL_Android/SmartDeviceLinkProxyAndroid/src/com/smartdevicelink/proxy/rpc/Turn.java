package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;

/**
 * Describes a navigation turn including an optional icon
 * <p><b>Parameter List
 * <table border="1" rules="all">
 * 		<tr>
 * 			<th>Name</th>
 * 			<th>Type</th>
 * 			<th>Description</th>
 * 			<th>SmartDeviceLink Ver. Available</th>
 * 		</tr>
 * 		<tr>
 * 			<td>navigationText</td>
 * 			<td>String</td>
 * 			<td>Text to describe the turn (e.g. streetname)
 *				 <ul>
 *					<li>Maxlength = 500</li>
 *				 </ul> 
 * 			</td>
 * 			<td>SmartDeviceLink 2.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>turnIcon</td>
 * 			<td>Image</td>
 * 			<td>Image to be shown for a turn
 * 			</td>
 * 			<td>SmartDeviceLink 2.0</td>
 * 		</tr>
 *  </table>
 * @since SmartDeviceLink 2.0
 */
public class Turn extends RPCStruct {

	/**
	 * Constructs a newly allocated Turn object
	 */
	public Turn() { }
	
	/**
	 * Constructs a newly allocated Turn object indicated by the Hashtable parameter
	 * @param hash The Hashtable to use
	 */
    public Turn(Hashtable hash) {
        super(hash);
    }
    
    /**
     * set the text to describe the turn (e.g. streetname)
     * @param navigationText the text to describe the turn (e.g. streetname)
     */
    public void setNavigationText(String navigationText) {
        if (navigationText != null) {
            store.put(Names.navigationText, navigationText);
        } else {
        	store.remove(Names.navigationText);
        }
    }
    
    /**
     * get the text to describe the turn (e.g. streetname)
     * @return the text to describe the turn (e.g. streetname)
     */
    public String getNavigationText() {
        return (String) store.get(Names.navigationText);
    }
    
    /**
     * set Image to be shown for a turn
     * @param turnIcon the image to be shown for a turn
     */
    public void setTurnIcon(Image turnIcon) {
        if (turnIcon != null) {
        	store.put(Names.turnIcon, turnIcon);
        } else {
        	store.remove(Names.turnIcon);
        }
    }
    
    /**
     * get the image to be shown for a turn
     * @return the image to be shown for a turn
     */
    public Image getTurnIcon() {
    	Object obj = store.get(Names.turnIcon);
        if (obj instanceof Image) {
            return (Image) obj;
        } else {
        	return new Image((Hashtable) obj);
        }
    }
}
