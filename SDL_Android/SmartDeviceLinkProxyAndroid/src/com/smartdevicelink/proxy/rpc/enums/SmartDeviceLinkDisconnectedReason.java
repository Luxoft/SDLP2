package com.smartdevicelink.proxy.rpc.enums;

public enum SmartDeviceLinkDisconnectedReason {
	USER_EXIT,
    IGNITION_OFF,
    BLUETOOTH_OFF,
    USB_DISCONNECTED,
    REQUEST_WHILE_IN_NONE_HMI_LEVEL,
    TOO_MANY_REQUESTS,
    DRIVER_DISTRACTION_VIOLATION,
    LANGUAGE_CHANGE,
    MASTER_RESET,
    FACTORY_DEFAULTS,
    TRANSPORT_ERROR,
    APPLICATION_REQUESTED_DISCONNECT,
    DEFAULT;
	
	public static SmartDeviceLinkDisconnectedReason valueForString(String value) {
        return valueOf(value);
    }
	
	public static SmartDeviceLinkDisconnectedReason convertAppInterfaceUnregisteredReason(AppInterfaceUnregisteredReason reason) {
		
		SmartDeviceLinkDisconnectedReason returnReason = SmartDeviceLinkDisconnectedReason.DEFAULT;
		
		switch(reason) {
			case USER_EXIT:
				returnReason = SmartDeviceLinkDisconnectedReason.USER_EXIT;
			case IGNITION_OFF:
				returnReason = SmartDeviceLinkDisconnectedReason.IGNITION_OFF;
			case BLUETOOTH_OFF:
				returnReason = SmartDeviceLinkDisconnectedReason.BLUETOOTH_OFF;
			case USB_DISCONNECTED:
				returnReason = SmartDeviceLinkDisconnectedReason.USB_DISCONNECTED;
			case REQUEST_WHILE_IN_NONE_HMI_LEVEL:
				returnReason = SmartDeviceLinkDisconnectedReason.REQUEST_WHILE_IN_NONE_HMI_LEVEL;
			case TOO_MANY_REQUESTS:
				returnReason = SmartDeviceLinkDisconnectedReason.TOO_MANY_REQUESTS;
			case DRIVER_DISTRACTION_VIOLATION:
				returnReason = SmartDeviceLinkDisconnectedReason.DRIVER_DISTRACTION_VIOLATION;
			case LANGUAGE_CHANGE:
				returnReason = SmartDeviceLinkDisconnectedReason.LANGUAGE_CHANGE;
			case MASTER_RESET:
				returnReason = SmartDeviceLinkDisconnectedReason.MASTER_RESET;
			case FACTORY_DEFAULTS:
				returnReason = SmartDeviceLinkDisconnectedReason.FACTORY_DEFAULTS;
		}
		
		return returnReason;
	}
}
