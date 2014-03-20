package com.smartdevicelink.proxy.rpc.enums;

/**
 * Indicates reason why app interface was unregistered. The application is being
 * disconnected by SMARTDEVICELINK.
 * 
 * @since SmartDeviceLink 1.0
 */
public enum AppInterfaceUnregisteredReason {
	USER_EXIT,
	/**
	 * Vehicle ignition turned off.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	IGNITION_OFF,
	/**
	 * Bluetooth was turned off, causing termination of a necessary Bluetooth
	 * connection.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	BLUETOOTH_OFF,
	/**
	 * USB was disconnected, causing termination of a necessary iAP connection.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	USB_DISCONNECTED,
	/**
	 * Application attempted SmartDeviceLink RPC request while {@linkplain HMILevel}
	 * =NONE. App must have HMILevel other than NONE to issue RPC requests or
	 * get notifications or RPC responses.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	REQUEST_WHILE_IN_NONE_HMI_LEVEL,
	/**
	 * Either too many -- or too many per unit of time -- requests were made by
	 * the application.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	TOO_MANY_REQUESTS,
	/**
	 * The application has issued requests which cause driver distraction rules
	 * to be violated.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	DRIVER_DISTRACTION_VIOLATION,
	/**
	 * The user has changed the language in effect on the SMARTDEVICELINK platform to a
	 * language that is incompatible with the language declared by the
	 * application in its RegisterAppInterface request.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	LANGUAGE_CHANGE,
	/**
	 * The user performed a MASTER RESET on the SMARTDEVICELINK platform, causing removal
	 * of a necessary Bluetooth pairing.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	MASTER_RESET,
	/**
	 * The user restored settings to FACTORY DEFAULTS on the SMARTDEVICELINK platform.
	 * 
	 * @since SmartDeviceLink 1.0
	 */
	FACTORY_DEFAULTS, 
	/**
	 * The app is not being authorized by Ford to be connected to SMARTDEVICELINK.
	 * 
	 * @since SmartDeviceLink 2.0
	 */
	APP_UNAUTHORIZED;

    public static AppInterfaceUnregisteredReason valueForString(String value) {
        return valueOf(value);
    }
}
