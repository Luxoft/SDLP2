package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCResponse;

public class SyncPDataResponse  extends RPCResponse {
	public SyncPDataResponse() {
        super("SyncPDataResponse");
    }
    public SyncPDataResponse(Hashtable hash) {
        super(hash);
    }
}