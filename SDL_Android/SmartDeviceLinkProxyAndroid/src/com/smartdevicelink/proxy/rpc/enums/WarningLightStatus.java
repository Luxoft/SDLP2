package com.smartdevicelink.proxy.rpc.enums;

/**
 * Reflects the status of a cluster instrument warning light.
 * @since SmartDeviceLink 2.0
 */
public enum WarningLightStatus {
	/**
	 * Warninglight Off
	 */
	OFF,
	/**
	 * Warninglight On
	 */
	ON,
	/**
	 * Warninglight is flashing
	 */
	FLASH;

    /**
     * Convert String to WarningLightStatus
     * @param value String
     * @return WarningLightStatus
     */    
    public static WarningLightStatus valueForString(String value) {
        return valueOf(value);
    }
}
