package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

/**
 * Sub scribeButton Response is sent, when SubscribeButton has been called
 * 
 * @since SmartDeviceLink 1.0
 */
public class SubscribeButtonResponse extends RPCResponse {

	/**
	 * Constructs a new SubscribeButtonResponse object
	 */
    public SubscribeButtonResponse() {
        super("SubscribeButton");
    }

	/**
	 * Constructs a new SubscribeButtonResponse object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public SubscribeButtonResponse(Hashtable hash) {
        super(hash);
    }
}