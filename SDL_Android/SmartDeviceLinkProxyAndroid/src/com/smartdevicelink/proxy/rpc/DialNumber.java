package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;

public class DialNumber extends RPCRequest {

    public DialNumber() {
        super("DialNumber");
    }
    public DialNumber(Hashtable hash) {
        super(hash);
    }
    public String getNumber() {
        return (String) parameters.get(Names.number);
    }
    public void setNumber(String number) {
        if (number != null) {
            parameters.put(Names.number, number);
        } else {
        	parameters.remove(Names.number);
        }
    }
}