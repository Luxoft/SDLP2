/*
 *
 * SDLP SDK
 * Cross Platform Application Communication Stack for In-Vehicle Applications
 *
 * Copyright (C) 2013, Luxoft Professional Corp., member of IBS group
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; version 2.1.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 *
 */

/**
 * @ingroup SDLP TCP Device discovery
 * @author Elena Bratanova <ebratanova@luxoft.com>
 * @date 30.06.2013
 */
package com.smartdevicelink.tcpdiscovery;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

public class DiscoveredDevice {
    public enum ConnectionType {
        WLAN, USB
    }
    
    private final static String WLAN_IFACE_NAME = "wlan0";
    private final static String USB_IFACE_NAME = "usb0";

    private String mPort;
    private String mIP;
    private String mLocalIP;
    private String mName;
    private String mUUID;
    private ConnectionType mConnectionType;

    private String mString;

    public String getPort() {
        return mPort;
    }

    public String getIP() {
        return mIP;
    }

    public String getLocalIP() {
        return mLocalIP;
    }

    public String getName() {
        return mName;
    }

    public String getUUID() {
        return mUUID;
    }

    public ConnectionType getConnectionType() {
        return mConnectionType;
    }

    public DiscoveredDevice(String ip, int port, String name, String UUID, ConnectionType type, String localIP) {
        mIP = ip;
        mPort = "" + port;
        mName = name;
        mUUID = UUID;
        mConnectionType = type;
        mLocalIP = localIP;
        if (name != null && name.length() != 0) {
            mString = name + (type == ConnectionType.WLAN ? "@Wi-Fi" : "@USB");
        } else {
            mString = "[" + mIP + ":" + mPort + "]";
        }
        if (localIP == null || localIP.length() == 0) {
            try {
                if (mConnectionType == ConnectionType.WLAN) {
                    mLocalIP = getLocalAddressForIface(WLAN_IFACE_NAME);
                } else {
                    mLocalIP = getLocalAddressForIface(USB_IFACE_NAME);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mLocalIP = "";
            }
        }
    }

    public DiscoveredDevice(String ip, String port) {
        mIP = ip;
        mPort = port;
        mName = "";
        mUUID = "";
        mString = "[" + mIP + ":" + mPort + "]";
        mConnectionType = ConnectionType.WLAN;
        try {
            mLocalIP = getLocalAddressForIface(WLAN_IFACE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            mLocalIP = "";
        }
    }

    private String getLocalAddressForIface(String ifaceName) throws Exception {
        NetworkInterface iface = NetworkInterface.getByName(ifaceName);
        Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if (InetAddressUtils.isIPv4Address(inetAddress.toString())) {
                return inetAddress.toString();
            }
        }
        return "";
    }

    public DiscoveredDevice(String ip, int port) {
        this(ip, "" + port);
    }

    @Override
    public String toString() {
        return mString;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DiscoveredDevice)) {
            return false;
        }
        DiscoveredDevice otherInstance = (DiscoveredDevice) o;
        return otherInstance.getIP().equals(mIP) && otherInstance.getPort().equals(mPort);
    }
}
