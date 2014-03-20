package com.smartdevicelink.proxy.interfaces;

public interface IProfileManagerListener extends IProxyListenerProfileManager {
    /**
     * Called after instead of onOnHmiStatus callback
     */
    public void onLinkUp();

    /**
     * Called instead of onProxyClosed
     */
    public void onLinkDown();
}
