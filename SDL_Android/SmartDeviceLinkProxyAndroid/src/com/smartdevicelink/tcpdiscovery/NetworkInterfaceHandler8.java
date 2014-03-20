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
 */
package com.smartdevicelink.tcpdiscovery;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

/**
 * Implementation compatible with API level 8
 */
public class NetworkInterfaceHandler8 implements NetworkInterfaceHandler {

    private static final int IPv4_LENGTH_IN_BYTES = 4;
    private static final String PERSONAL_HOTSPOT_WLAN_BRD = "192.168.43.255";
    private static final String PERSONAL_HOTSPOT_USB_BRD = "192.168.42.255";
    private static final String PERSONAL_HOTSPOT_LOCAL = "192.168.43.1";
    // for USB local address may vary

    @Override
    public List<InetAddress> getBroadcastAdresses(Context context) {
        List<InetAddress> result = new LinkedList<InetAddress>();
        try {
            result.add(InetAddress.getByName(PERSONAL_HOTSPOT_WLAN_BRD));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            result.add(InetAddress.getByName(PERSONAL_HOTSPOT_USB_BRD));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        InetAddress wlanBrd = getBroadcastAddress(context);
        if (wlanBrd != null) {
            result.add(wlanBrd);
        }
        return result;
    }

    @Override
    public List<InetAddress> getLocalAddresses(Context context) {
        List<InetAddress> result = new LinkedList<InetAddress>();
        try {
            result.add(InetAddress.getByName(PERSONAL_HOTSPOT_LOCAL));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        InetAddress wlanIp = getLocalAddress(context);
        if (wlanIp != null) {
            result.add(wlanIp);
        }
        return result;
    }

    private InetAddress getLocalAddress(Context context) {
        if (context != null) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo networkstate = wifi.getDhcpInfo();
            return getInetFromInteger(networkstate.ipAddress);
        }
        return null;
    }

    private InetAddress getBroadcastAddress(Context context) {
        if (context != null) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo networkstate = wifi.getDhcpInfo();
            return getInetFromInteger(networkstate.ipAddress | (~networkstate.netmask));
        }
        return null;
    }

    private InetAddress getInetFromInteger(int address) {
        ByteBuffer buffer = ByteBuffer.allocate(IPv4_LENGTH_IN_BYTES);
        buffer.putInt(Integer.reverseBytes(address));
        try {
            return Inet4Address.getByAddress(buffer.array());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
