package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

/**
 * Update Turn List Response is sent, when UpdateTurnList has been called
 * 
 * @since SmartDeviceLink 2.0
 */
public class UpdateTurnListResponse extends RPCResponse {

	/**
	 * Constructs a new UpdateTurnListResponse object
	 */
    public UpdateTurnListResponse() {
        super("UpdateTurnList");
    }

	/**
	 * Constructs a new UpdateTurnListResponse object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public UpdateTurnListResponse(Hashtable hash) {
        super(hash);
    }
}