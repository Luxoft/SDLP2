package com.smartdevicelink.proxy.interfaces;

public interface IProxyListenerALM extends IProxyListenerBase {
	// Adds Advanced Life-cycle Management call-backs to the IProxyListenerAbstract interface
	
	/**
	 * **MOVED TO IProxyListenerBase** - onOnHMIStatus() being called indicates that the proxy has entered a state in which the 
	 * application may create SMARTDEVICELINK related resources (addCommands, ChoiceSets). 
	 */
	//public void onOnHMIStatus(OnHMIStatus notification);
	
	/**
	 * **MOVED TO IProxyListenerBase** - onProxyClosed() being called indicates that the app is no longer registered with SMARTDEVICELINK
	 * All resources on SMARTDEVICELINK (addCommands and ChoiceSets) have been deleted and will have to be
	 * recreated upon the next onReadyForInitialization() call-back. 
	 */
	//public void onProxyClosed(String info, Exception e);
	
	/**
	 * **MOVED TO IProxyListenerBase** - onError() being called indicates that the proxy has experienced an unrecoverable error.
	 * A new proxy object must be initiated to reestablish connection with SMARTDEVICELINK.
	 * 
	 * @param info - Any info present about the error that occurred.
	 * @param e - Any exception thrown by the error.
	 */
	//public void onError(String info, Exception e);
	
	/**
	 * **Deprecated** - onSmartDeviceLinkInterfaceAvailable() being called indicates that the proxy now has access to SMARTDEVICELINK's HMI. 
	 * Monitor the onFocusChange call-back to determine which level of HMI is available to the proxy.
	 * 
	 * @param isFirstAvailability - Indicates this is the first onSmartDeviceLinkInterfaceAvailable in this lifecycle.
	 */
	// HMI (Background, Limited, Full) from Unavailable  = onSmartDeviceLinkInterfaceAvailable(Boolean isFirstAvailability);

	/**
	 * **Deprecated** - onSmartDeviceLinkInterfaceUnavailable() being called indicates that the proxy does NOT have access to SMARTDEVICELINK's HIM.
	 */
	// HMI None onSmartDeviceLinkInterfaceUnavailable();
	
	/**
	 * **Deprecated** - ALM HMI states converted back to HMI Levels
	 * 
	 * HMI Full = onSmartDeviceLinkInFocus(Boolean isFirstSmartDeviceLinkInFocus);
	 * HMI Limited = onSmartDeviceLinkInFocusLimited();
	 * HMI Background = onSmartDeviceLinkLostFocus();
	 */
}
