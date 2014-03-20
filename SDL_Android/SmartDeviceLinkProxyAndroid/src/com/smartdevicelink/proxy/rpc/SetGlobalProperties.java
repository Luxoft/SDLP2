package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;
/**
 * Sets value(s) for the specified global property(ies)
 * <p>
 * Function Group: Base <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUND</b>
 * </p>
 * 
 * @since SmartDeviceLink 1.0
 * @see ResetGlobalProperties
 */
public class SetGlobalProperties extends RPCRequest {
	/**
	 * Constructs a new SetGlobalProperties object
	 */
    public SetGlobalProperties() {
        super("SetGlobalProperties");
    }
	/**
	 * Constructs a new SetGlobalProperties object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */    
    public SetGlobalProperties(Hashtable hash) {
        super(hash);
    }
	/**
	 * Gets a Vector<TTSChunk> for Help Prompt representing Array of one or more
	 * TTSChunk elements specifying the help prompt used in an interaction
	 * started by PTT
	 * 
	 * @return Vector<TTSChunk> -an Array of one or more TTSChunk elements
	 *         specifying the help prompt used in an interaction started by PTT
	 */    
    public Vector<TTSChunk> getHelpPrompt() {
    	if (parameters.get(Names.helpPrompt) instanceof Vector<?>) {
	        Vector<?> list = (Vector<?>)parameters.get(Names.helpPrompt);
	        if (list != null && list.size() > 0) {
	            Object obj = list.get(0);
	            if (obj instanceof TTSChunk) {
	                return (Vector<TTSChunk>) list;
	            } else if (obj instanceof Hashtable) {
	                Vector<TTSChunk> newList = new Vector<TTSChunk>();
	                for (Object hashObj : list) {
	                    newList.add(new TTSChunk((Hashtable)hashObj));
	                }
	                return newList;
	            }
	        }
    	}
	    return null;
    }
	/**
	 * Sets a Vector<TTSChunk> for Help Prompt that Array of one or more
	 * TTSChunk elements specifying the help prompt used in an interaction
	 * started by PTT
	 * 
	 * @param helpPrompt
	 *            a Vector<TTSChunk> of one or more TTSChunk elements
	 *            <p>
	 *            <b>Notes: </b>
	 *            <ul>
	 *            <li>Array must have at least one element</li>
	 *            <li>Only optional it timeoutPrompt has been specified</li>
	 *            </ul>
	 */    
    public void setHelpPrompt(Vector<TTSChunk> helpPrompt) {
        if (helpPrompt != null) {
            parameters.put(Names.helpPrompt, helpPrompt);
        }
    }
	/**
	 * Gets a Vector<TTSChunk> for Timeout Prompt representing Array of one or
	 * more TTSChunk elements specifying the help prompt used in an interaction
	 * started by PTT
	 * 
	 * @return Vector<TTSChunk> -an Array of one or more TTSChunk elements
	 *         specifying the help prompt used in an interaction started by PTT
	 */    
    public Vector<TTSChunk> getTimeoutPrompt() {
        if (parameters.get(Names.timeoutPrompt) instanceof Vector<?>) {
	    	Vector<?> list = (Vector<?>)parameters.get(Names.timeoutPrompt);
	        if (list != null && list.size() > 0) {
	            Object obj = list.get(0);
	            if (obj instanceof TTSChunk) {
	                return (Vector<TTSChunk>) list;
	            } else if (obj instanceof Hashtable) {
	                Vector<TTSChunk> newList = new Vector<TTSChunk>();
	                for (Object hashObj : list) {
	                    newList.add(new TTSChunk((Hashtable)hashObj));
	                }
	                return newList;
	            }
	        }
        }
        return null;
    }
	/**
	 * Sets a Vector<TTSChunk> for Timeout Prompt representing Array of one or
	 * more TTSChunk elements specifying the help prompt used in an interaction
	 * started by PTT
	 * 
	 */    
    public void setTimeoutPrompt(Vector<TTSChunk> timeoutPrompt) {
        if (timeoutPrompt != null) {
            parameters.put(Names.timeoutPrompt, timeoutPrompt);
        }
    }

	/**
	 * Gets a voice recognition Help Title
	 * 
	 * @return String - a String value representing the text, which is shown as
	 *         title of the VR help screen used in an interaction started by PTT
	 * @since SmartDeviceLink 2.0
	 */
    public String getVrHelpTitle() {
        return (String) parameters.get(Names.vrHelpTitle);
    }

	/**
	 * Sets a voice recognition Help Title
	 * 
	 * @param vrHelpTitle
	 *            a String value representing a voice recognition Help Title
	 *            <p>
	 *            <b>Notes: </b><br/>
	 *            <ul>
	 *            <li>If omitted on supported displays, the default SMARTDEVICELINK help
	 *            title will be used</li>
	 *            <li>If omitted and one or more vrHelp items are provided, the
	 *            request will be rejected.</li>
	 *            <li>String Maxlength = 500</li>
	 *            </ul>
	 * @since SmartDeviceLink 2.0
	 */
    public void setVrHelpTitle(String vrHelpTitle) {
        if (vrHelpTitle != null) {
            parameters.put(Names.vrHelpTitle, vrHelpTitle);
        } else {
        	parameters.remove(Names.vrHelpTitle);
        }
    }

	/**
	 * Gets items listed in the VR help screen used in an interaction started by
	 * PTT
	 * 
	 * @return Vector<VrHelpItem> - a Vector value representing items listed in
	 *         the VR help screen used in an interaction started by PTT
	 * @since SmartDeviceLink 2.0
	 */
    public Vector<VrHelpItem> getVrHelp() {
        if (parameters.get(Names.vrHelp) instanceof Vector<?>) {
	    	Vector<?> list = (Vector<?>)parameters.get(Names.vrHelp);
	        if (list != null && list.size() > 0) {
	            Object obj = list.get(0);
	            if (obj instanceof VrHelpItem) {
	                return (Vector<VrHelpItem>) list;
	            } else if (obj instanceof Hashtable) {
	                Vector<VrHelpItem> newList = new Vector<VrHelpItem>();
	                for (Object hashObj : list) {
	                    newList.add(new VrHelpItem((Hashtable)hashObj));
	                }
	                return newList;
	            }
	        }
        }
        return null;
    }

	/**
	 * Sets the items listed in the VR help screen used in an interaction
	 * started by PTT
	 * 
	 * @param vrHelp
	 *            a Vector value representing items listed in the VR help screen
	 *            used in an interaction started by PTT
	 *            <p>
	 *            <b>Notes: </b><br/>
	 *            <ul>
	 *            <li>If omitted on supported displays, the default SmartDeviceLink VR
	 *            help / What Can I Say? screen will be used</li>
	 *            <li>If the list of VR Help Items contains nonsequential
	 *            positions (e.g. [1,2,4]), the RPC will be rejected</li>
	 *            <li>If omitted and a vrHelpTitle is provided, the request
	 *            will be rejected</li>
	 *            <li>Array Minsize: = 1</li>
	 *            <li>Array Maxsize = 100</li>
	 *            </ul>
	 * @since SmartDeviceLink 2.0
	 */
    public void setVrHelp(Vector<VrHelpItem> vrHelp) {
        if (vrHelp != null) {
            parameters.put(Names.vrHelp, vrHelp);
        } else {
        	parameters.remove(Names.vrHelp);
        }
    }
}
