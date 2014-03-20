package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

/**
 * Used to delete a file resident on the SMARTDEVICELINK module in the app's local cache.
 * Not supported on first generation SMARTDEVICELINK vehicles
 * <p>
 * 
 * @since SmartDeviceLink 2.0
 * @see PutFile
 * @see ListFiles
 */
public class DeleteFile extends RPCRequest {

	/**
	 * Constructs a new DeleteFile object
	 */
    public DeleteFile() {
        super("DeleteFile");
    }

	/**
	 * Constructs a new DeleteFile object indicated by the Hashtable parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public DeleteFile(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets a file reference name
	 * 
	 * @param smartDeviceLinkFileName
	 *            a String value representing a file reference name
	 */
    public void setSmartDeviceLinkFileName(String smartDeviceLinkFileName) {
        if (smartDeviceLinkFileName != null) {
            parameters.put(Names.smartDeviceLinkFileName, smartDeviceLinkFileName);
        } else {
        	parameters.remove(Names.smartDeviceLinkFileName);
        }
    }

	/**
	 * Gets a file reference name
	 * 
	 * @return String -a String value representing a file reference name
	 */
    public String getSmartDeviceLinkFileName() {
        return (String) parameters.get(Names.smartDeviceLinkFileName);
    }
}