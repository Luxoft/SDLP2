package com.smartdevicelink.proxy;

import android.telephony.TelephonyManager;

public class SmartDeviceLinkProxyConfigurationResources {
	private String _smartDeviceLinkConfigurationFilePath;
	private TelephonyManager _telephonyManager;
	
	public SmartDeviceLinkProxyConfigurationResources() {
		this(null, null);
	}
	
	public SmartDeviceLinkProxyConfigurationResources(String smartDeviceLinkConfigurationFilePath, 
			TelephonyManager telephonyManager) {
		_smartDeviceLinkConfigurationFilePath = smartDeviceLinkConfigurationFilePath;
		_telephonyManager = telephonyManager;
	}
	
	public void setSmartDeviceLinkConfigurationFilePath(String smartDeviceLinkConfigurationFilePath) {
		_smartDeviceLinkConfigurationFilePath = smartDeviceLinkConfigurationFilePath;
	}
	
	public String getSmartDeviceLinkConfigurationFilePath() {
		return _smartDeviceLinkConfigurationFilePath;
	}
	
	public void setTelephonyManager(TelephonyManager telephonyManager) {
		_telephonyManager = telephonyManager;
	}
	
	public TelephonyManager getTelephonyManager() {
		return _telephonyManager;
	}
}
