package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

/**
 * Alert Maneuver Response is sent, when AlertManeuver has been called
 * 
 * @since SmartDeviceLink 2.0
 */
public class AlertManeuverResponse extends RPCResponse {

    public AlertManeuverResponse() {
        super("AlertManeuver");
    }
    public AlertManeuverResponse(Hashtable hash) {
        super(hash);
    }
}