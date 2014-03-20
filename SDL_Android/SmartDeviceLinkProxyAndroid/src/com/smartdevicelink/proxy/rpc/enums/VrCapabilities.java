package com.smartdevicelink.proxy.rpc.enums;

/**
 * The VR capabilities of the connected SMARTDEVICELINK platform.
 * 
 */
public enum VrCapabilities {
	/**
	 * The SMARTDEVICELINK platform is capable of recognizing spoken text in the current
	 * language.
	 * 
	 * @since SmartDeviceLink 1.0
	 */   
	Text;

    public static VrCapabilities valueForString(String value) {
        if (value.toUpperCase().equals(VrCapabilities.Text.toString().toUpperCase()))
        {
        	return VrCapabilities.Text;
        }
        return null;
    }
}
