package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

/**
 * Updates the list of next maneuvers, which can be requested by the user
 * pressing the softbutton "Turns" on the Navigation base screen. Three
 * softbuttons are predefined by the system: Up, Down, Close
 * <p>
 * Function Group: Navigation
 * <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUND</b>
 * <p>
 * 
 * @since SmartDeviceLink 2.0
 * @see ShowConstantTBT
 */
public class UpdateTurnList extends RPCRequest {

	/**
	 * Constructs a new UpdateTurnList object
	 */
    public UpdateTurnList() {
        super("UpdateTurnList");
    }

	/**
	 * Constructs a new UpdateTurnList object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public UpdateTurnList(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets a list of turns to be shown to the user
	 * 
	 * @param turnList
	 *            a Vector<Turn> value representing a list of turns to be shown
	 *            to the user
	 *            <p>
	 *            <b>Notes: </b>Minsize=1; Maxsize=100
	 */
    public void setTurnList(Vector<Turn> turnList) {
        if (turnList != null) {
            parameters.put(Names.turnList, turnList);
        } else {
        	parameters.remove(Names.turnList);
        }
    }

	/**
	 * Gets a list of turns to be shown to the user
	 * 
	 * @return Vector<Turn> -a Vector value representing a list of turns
	 */
    public Vector<Turn> getTurnList() {
        if (parameters.get(Names.turnList) instanceof Vector<?>) {
	    	Vector<?> list = (Vector<?>)parameters.get(Names.turnList);
	        if (list != null && list.size() > 0) {
	            Object obj = list.get(0);
	            if (obj instanceof Turn) {
	                return (Vector<Turn>) list;
	            } else if (obj instanceof Hashtable) {
	                Vector<Turn> newList = new Vector<Turn>();
	                for (Object hashObj : list) {
	                    newList.add(new Turn((Hashtable)hashObj));
	                }
	                return newList;
	            }
	        }
        }
        return null;
    }

	/**
	 * Sets soft buttons in the screen. If omitted on supported displays,
	 * app-defined SoftButton will be left blank
	 * <p>
	 * <b>Notes: </b>Minsize=0; Maxsize=1
	 * 
	 * @param softButtons a Vector value
	 */
    public void setSoftButtons(Vector<SoftButton> softButtons) {
        if (softButtons != null) {
            parameters.put(Names.softButtons, softButtons);
        } else {
        	parameters.remove(Names.softButtons);
        }
    }

	/**
	 * Gets soft buttons in the screen
	 * @return Vector -a Vector<SoftButton> value
	 */
    public Vector<SoftButton> getSoftButtons() {
        if (parameters.get(Names.softButtons) instanceof Vector<?>) {
	    	Vector<?> list = (Vector<?>)parameters.get(Names.softButtons);
	        if (list != null && list.size() > 0) {
	            Object obj = list.get(0);
	            if (obj instanceof SoftButton) {
	                return (Vector<SoftButton>) list;
	            } else if (obj instanceof Hashtable) {
	                Vector<SoftButton> newList = new Vector<SoftButton>();
	                for (Object hashObj : list) {
	                    newList.add(new SoftButton((Hashtable)hashObj));
	                }
	                return newList;
	            }
	        }
        }
        return null;
    }
}
