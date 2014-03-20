package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.LightSwitchStatus;
import com.smartdevicelink.util.DebugTool;

public class HeadLampStatus extends RPCStruct {

    public HeadLampStatus() {}
    public HeadLampStatus(Hashtable hash) {
        super(hash);
    }
    public void setLightSwitchStatus(LightSwitchStatus lightSwitchStatus) {
        if (lightSwitchStatus != null) {
            store.put(Names.lightSwitchStatus, lightSwitchStatus);
        } else {
        	store.remove(Names.lightSwitchStatus);
        }
    }
    public LightSwitchStatus getLightSwitchStatus() {
        Object obj = store.get(Names.lightSwitchStatus);
        if (obj instanceof LightSwitchStatus) {
            return (LightSwitchStatus) obj;
        } else if (obj instanceof String) {
        	LightSwitchStatus theCode = null;
            try {
                theCode = LightSwitchStatus.valueForString((String) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.lightSwitchStatus, e);
            }
            return theCode;
        }
        return null;
    }
    public void setHighBeamsOn(Boolean highBeamsOn) {
        if (highBeamsOn != null) {
            store.put(Names.highBeamsOn, highBeamsOn);
        } else {
        	store.remove(Names.highBeamsOn);
        }
    }
    public Boolean getHighBeamsOn() {
    	return (Boolean) store.get(Names.highBeamsOn);
    }
}