package com.smartdevicelink.proxy.rpc.enums;

/**
 * Identifies the various display types used by SMARTDEVICELINK. See SmartDeviceLink TDK and Head Unit Guide for further information regarding the displays.
 * @since SmartDeviceLink 1.0
 */
public enum DisplayType {
	/**
	 * This display type provides a 2-line x 20 character "dot matrix" display.
	 */	
    CID,
    TYPE2,
    TYPE5,
    /**
     * This display type provides an 8 inch touchscreen display.
     */    
    NGN,
    GEN2_8_DMA,
    GEN2_6_DMA,
    MFD3,
    MFD4,
    MFD5;

    /**
     * Convert String to DisplayType
     * @param value String
     * @return DisplayType
     */
    public static DisplayType valueForString(String value) {
        return valueOf(value);
    }
}
