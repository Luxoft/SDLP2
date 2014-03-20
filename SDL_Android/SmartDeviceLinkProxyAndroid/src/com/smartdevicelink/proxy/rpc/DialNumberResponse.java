package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

public class DialNumberResponse extends RPCResponse {

    public DialNumberResponse() {
        super("DialNumber");
    }
    public DialNumberResponse(Hashtable hash) {
        super(hash);
    }
}