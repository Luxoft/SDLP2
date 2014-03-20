package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

public class SyncPData extends RPCRequest {
	public SyncPData() {
        super("SyncPData");
    }
    public SyncPData(Hashtable hash) {
        super(hash);
    }
    public void setSyncPData(byte[] syncPData) {
        if (syncPData != null) {
            parameters.put(Names.bulkData, syncPData);
        } else {
        	parameters.remove(Names.bulkData);
        }
    }
    public byte[] getSyncPData() {
        return (byte[]) parameters.get(Names.bulkData);
    }
}