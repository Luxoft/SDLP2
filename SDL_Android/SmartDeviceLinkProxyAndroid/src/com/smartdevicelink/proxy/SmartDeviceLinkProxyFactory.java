package com.smartdevicelink.proxy;

import com.smartdevicelink.exception.SmartDeviceLinkException;
@Deprecated
public class SmartDeviceLinkProxyFactory {
	
	@Deprecated
	public static SmartDeviceLinkProxy buildSmartDeviceLinkProxy(IProxyListener listener) {
		SmartDeviceLinkProxy ret = null;
		try {
			ret = new SmartDeviceLinkProxy(listener);
		} catch (SmartDeviceLinkException e) {
			e.printStackTrace();
		}
		return ret;
	}
}