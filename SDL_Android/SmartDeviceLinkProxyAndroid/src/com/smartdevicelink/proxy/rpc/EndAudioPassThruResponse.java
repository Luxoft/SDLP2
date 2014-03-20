package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

/**
 * End Audio Pass Thru Response is sent, when EndAudioPassThru has been called
 * 
 * @since SmartDeviceLink 2.0
 */
public class EndAudioPassThruResponse extends RPCResponse {

	/**
	 * Constructs a new EndAudioPassThruResponse object
	 */
    public EndAudioPassThruResponse() {
        super("EndAudioPassThru");
    }
    public EndAudioPassThruResponse(Hashtable hash) {
        super(hash);
    }
}