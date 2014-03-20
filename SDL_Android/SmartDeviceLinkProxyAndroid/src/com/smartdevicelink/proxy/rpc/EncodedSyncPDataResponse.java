package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;


public class EncodedSyncPDataResponse  extends RPCResponse {
	public EncodedSyncPDataResponse() {
        super("EncodedSyncPData");
    }
    public EncodedSyncPDataResponse(Hashtable hash) {
        super(hash);
    }
}