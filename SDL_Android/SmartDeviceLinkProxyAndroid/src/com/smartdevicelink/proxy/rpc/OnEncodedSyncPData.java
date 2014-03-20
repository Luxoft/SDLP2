package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCNotification;
import com.smartdevicelink.proxy.constants.Names;


public class OnEncodedSyncPData extends RPCNotification {

	public OnEncodedSyncPData() {
        super("OnEncodedSyncPData");
    }

    public OnEncodedSyncPData(Hashtable hash) {
        super(hash);
    }
  
    public Vector<String> getData() {
    	if (parameters.get(Names.data) instanceof Vector<?>) {
    		Vector<?> list = (Vector<?>)parameters.get(Names.data);
    		if (list != null && list.size()>0) {
        		Object obj = list.get(0);
        		if (obj instanceof String) {
        			return (Vector<String>)list;
        		}
    		}
    	}
        return null;
    }
  
    public void setData(Vector<String> data) {
        if (data != null) {
            parameters.put(Names.data, data);
        } else {
			parameters.remove(Names.data);
        }
    }
    public String getUrl(){
        return (String) parameters.get(Names.URL);
    }
    public void setUrl(String url) {
    	if (url != null) {
    		parameters.put(Names.URL, url);
    	} else {
			parameters.remove(Names.URL);
        }
    }
    public Integer getTimeout(){
    	if (parameters.get(Names.Timeout) instanceof Integer) {
    		return (Integer)parameters.get(Names.Timeout);
    	}
    	return null;
    }
    public void setTimeout(Integer timeout) {
        if (timeout != null) {
            parameters.put(Names.timeout, timeout);
        } else {
        	parameters.remove(Names.timeout);
        }
    }
}
