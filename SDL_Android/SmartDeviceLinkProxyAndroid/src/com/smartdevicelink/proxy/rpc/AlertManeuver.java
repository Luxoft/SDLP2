package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

/**
 * This will bring up an alert with information related to the next navigation
 * maneuver including potential voice navigation instructions. Shown information
 * will be taken from the ShowConstantTBT function
 * <p>
 * Function Group: Navigation
 * <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUND</b>
 * <p>
 * 
 * @since SmartDeviceLink 2.0
 * @see ShowConstantTBT
 */
public class AlertManeuver extends RPCRequest {

	/**
	 * Constructs a new AlertManeuver object
	 */
    public AlertManeuver() {
        super("AlertManeuver");
    }

	/**
	 * Constructs a new AlertManeuver object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public AlertManeuver(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets an array of text chunks of type TTSChunk
	 * 
	 * @param ttsChunks
	 *            a Vector<TTSChunk> value representing an array of text chunks
	 *            of type TTSChunk
	 *            <p>
	 *            <b>Notes: </b>Minsize=1; Maxsize=100
	 */
    public void setTtsChunks(Vector<TTSChunk> ttsChunks) {
        if (ttsChunks != null) {
            parameters.put(Names.ttsChunks, ttsChunks);
        } else {
        	parameters.remove(Names.ttsChunks);
        }
    }

	/**
	 * Gets an array of text chunks of type TTSChunk
	 * 
	 * @return Vector<TTSChunk> -a Vector<TTSChunk> value representing an array
	 *         of text chunks of type TTSChunk
	 */
    public Vector<TTSChunk> getTtsChunks() {
        if (parameters.get(Names.ttsChunks) instanceof Vector<?>) {
	    	Vector<?> list = (Vector<?>)parameters.get(Names.ttsChunks);
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
	 * Sets soft buttons. If omitted on supported displays, only the system
	 * defined "Close" SoftButton will be displayed
	 * 
	 * @param softButtons
	 *            a Vector<SoftButton> value
	 *            <p>
	 *            <b>Notes: </b>Minsize=0; Maxsize=3
	 */
    public void setSoftButtons(Vector<SoftButton> softButtons) {
        if (softButtons != null) {
            parameters.put(Names.softButtons, softButtons);
        } else {
        	parameters.remove(Names.softButtons);
        }
    }

	/**
	 * Gets a Vector<SoftButton>
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
