package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

/**
 * This RPC is used to update the user with navigation information for the
 * constantly shown screen (base screen), but also for the alert type screen
 * <p>
 * Function Group: Navigation
 * <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUND</b>
 * <p>
 * 
 * @since SmartDeviceLink 2.0
 * @see AlertManeuver
 * @see UpdateTurnList
 */
public class ShowConstantTBT extends RPCRequest {

	/**
	 * Constructs a new ShowConstantTBT object
	 */
    public ShowConstantTBT() {
        super("ShowConstantTBT");
    }

	/**
	 * Constructs a new ShowConstantTBT object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public ShowConstantTBT(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets a text for navigation text field 1
	 * 
	 * @param navigationText1
	 *            a String value representing a text for navigation text field 1
	 *            <p>
	 *            <b>Notes: </b>Maxlength=500
	 */
    public void setNavigationText1(String navigationText1) {
        if (navigationText1 != null) {
            parameters.put(Names.navigationText1, navigationText1);
        } else {
        	parameters.remove(Names.navigationText1);
        }
    }

	/**
	 * Gets a text for navigation text field 1
	 * 
	 * @return String -a String value representing a text for navigation text
	 *         field 1
	 */
    public String getNavigationText1() {
        return (String) parameters.get(Names.navigationText1);
    }

	/**
	 * Sets a text for navigation text field 2
	 * 
	 * @param navigationText2
	 *            a String value representing a text for navigation text field 2
	 *            <p>
	 *            <b>Notes: </b>Maxlength=500
	 */
    public void setNavigationText2(String navigationText2) {
        if (navigationText2 != null) {
            parameters.put(Names.navigationText2, navigationText2);
        } else {
        	parameters.remove(Names.navigationText2);
        }
    }

	/**
	 * Gets a text for navigation text field 2
	 * 
	 * @return String -a String value representing a text for navigation text
	 *         field 2
	 */
    public String getNavigationText2() {
        return (String) parameters.get(Names.navigationText2);
    }

	/**
	 * Sets a text field for estimated time of arrival
	 * 
	 * @param eta
	 *            a String value representing a text field for estimated time of
	 *            arrival
	 *            <p>
	 *            <b>Notes: </b>Maxlength=500
	 */
    public void setEta(String eta) {
        if (eta != null) {
            parameters.put(Names.eta, eta);
        } else {
        	parameters.remove(Names.eta);
        }
    }

	/**
	 * Gets a text field for estimated time of arrival
	 * 
	 * @return String -a String value representing a text field for estimated
	 *         time of arrival
	 */
    public String getEta() {
        return (String) parameters.get(Names.eta);
    }

	/**
	 * Sets a text field for total distance
	 * 
	 * @param totalDistance
	 *            a String value representing a text field for total distance
	 *            <p>
	 *            <b>Notes: </b>Maxlength=500
	 */
    public void setTotalDistance(String totalDistance) {
        if (totalDistance != null) {
            parameters.put(Names.totalDistance, totalDistance);
        } else {
        	parameters.remove(Names.totalDistance);
        }
    }

	/**
	 * Gets a text field for total distance
	 * 
	 * @return String -a String value representing a text field for total
	 *         distance
	 */
    public String getTotalDistance() {
        return (String) parameters.get(Names.totalDistance);
    }

	/**
	 * Sets an Image for turnicon
	 * 
	 * @param turnIcon
	 *            an Image value
	 */
    public void setTurnIcon(Image turnIcon) {
        if (turnIcon != null) {
            parameters.put(Names.turnIcon, turnIcon);
        } else {
        	parameters.remove(Names.turnIcon);
        }
    }

	/**
	 * Gets an Image for turnicon
	 * 
	 * @return Image -an Image value representing an Image for turnicon
	 */
    public Image getTurnIcon() {
        Object obj = parameters.get(Names.turnIcon);
        if (obj instanceof Image) {
            return (Image) obj;
        } else {
        	return new Image((Hashtable) obj);
        }
    }

	/**
	 * Sets a Fraction of distance till next maneuver
	 * 
	 * @param distanceToManeuver
	 *            a Double value representing a Fraction of distance till next
	 *            maneuver
	 *            <p>
	 *            <b>Notes: </b>Minvalue=0; Maxvalue=1000000000
	 */
    public void setDistanceToManeuver(Double distanceToManeuver) {
        if (distanceToManeuver != null) {
            parameters.put(Names.distanceToManeuver, distanceToManeuver);
        } else {
        	parameters.remove(Names.distanceToManeuver);
        }
    }

	/**
	 * Gets a Fraction of distance till next maneuver
	 * 
	 * @return Double -a Double value representing a Fraction of distance till
	 *         next maneuver
	 */
    public Double getDistanceToManeuver() {
        return (Double) parameters.get(Names.distanceToManeuver);
    }

	/**
	 * Sets a Distance till next maneuver (starting from) from previous maneuver
	 * 
	 * @param distanceToManeuverScale
	 *            a Double value representing a Distance till next maneuver
	 *            (starting from) from previous maneuver
	 *            <p>
	 *            <b>Notes: </b>Minvalue=0; Maxvalue=1000000000
	 */
    public void setDistanceToManeuverScale(Double distanceToManeuverScale) {
        if (distanceToManeuverScale != null) {
            parameters.put(Names.distanceToManeuverScale, distanceToManeuverScale);
        } else {
        	parameters.remove(Names.distanceToManeuverScale);
        }
    }

	/**
	 * Gets a Distance till next maneuver (starting from) from previous maneuver
	 * 
	 * @return Double -a Double value representing a Distance till next maneuver
	 *         (starting from) from previous maneuver
	 */
    public Double getDistanceToManeuverScale() {
        return (Double) parameters.get(Names.distanceToManeuverScale);
    }

	/**
	 * Sets a maneuver complete flag. If and when a maneuver has completed while
	 * an AlertManeuver is active, the app must send this value set to TRUE in
	 * order to clear the AlertManeuver overlay<br/>
	 * If omitted the value will be assumed as FALSE
	 * <p>
	 * 
	 * @param maneuverComplete
	 *            a Boolean value
	 */
    public void setManeuverComplete(Boolean maneuverComplete) {
        if (maneuverComplete != null) {
            parameters.put(Names.maneuverComplete, maneuverComplete);
        } else {
        	parameters.remove(Names.maneuverComplete);
        }
    }

	/**
	 * Gets a maneuver complete flag
	 * 
	 * @return Boolean -a Boolean value
	 */
    public Boolean getManeuverComplete() {
        return (Boolean) parameters.get(Names.maneuverComplete);
    }

	/**
	 * Sets Three dynamic SoftButtons available (first SoftButton is fixed to
	 * "Turns"). If omitted on supported displays, the currently displayed
	 * SoftButton values will not change
	 * <p>
	 * <b>Notes: </b>Minsize=0; Maxsize=3
	 * 
	 * @param softButtons a Vector<SoftButton> value
	 */
    public void setSoftButtons(Vector<SoftButton> softButtons) {
        if (softButtons != null) {
            parameters.put(Names.softButtons, softButtons);
        } else {
        	parameters.remove(Names.softButtons);
        }
    }

	/**
	 * Gets Three dynamic SoftButtons available (first SoftButton is fixed to
	 * "Turns"). If omitted on supported displays, the currently displayed
	 * SoftButton values will not change
	 * @return Vector<SoftButton> -a Vector<SoftButton> value
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
