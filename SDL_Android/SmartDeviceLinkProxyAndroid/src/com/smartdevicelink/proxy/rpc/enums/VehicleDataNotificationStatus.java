package com.smartdevicelink.proxy.rpc.enums;

/**
 * Reflects the status of a vehicle data notification.
 * @since SmartDeviceLink 2.0
 */
public enum VehicleDataNotificationStatus {
	NOT_SUPPORTED,
	NORMAL,
	ACTIVE;

    /**
     * Convert String to VehicleDataNotificationStatus
     * @param value String
     * @return VehicleDataNotificationStatus
     */    
    public static VehicleDataNotificationStatus valueForString(String value) {
        return valueOf(value);
    }
}
