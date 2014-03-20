package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

/**
 * Show Constant TBT Response is sent, when ShowConstantTBT has been called
 * 
 * @since SmartDeviceLink 2.0
 */
public class ShowConstantTBTResponse extends RPCResponse {

	/**
	 * Constructs a new ShowConstantTBTResponse object
	 */
    public ShowConstantTBTResponse() {
        super("ShowConstantTBT");
    }

	/**
	 * Constructs a new ShowConstantTBTResponse object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public ShowConstantTBTResponse(Hashtable hash) {
        super(hash);
    }
}