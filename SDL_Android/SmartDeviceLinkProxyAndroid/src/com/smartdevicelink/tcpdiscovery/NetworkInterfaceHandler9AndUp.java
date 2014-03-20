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

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * Implementation compatible with API level 9 and newer
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NetworkInterfaceHandler9AndUp implements NetworkInterfaceHandler {
    private final static String GET_BRD_METHOD_NAME = "getBroadcast";
    private final static String GET_ADDR_METHOD_NAME = "getAddress";
    private final static Class<?>[] NO_PARAMS = null;
    private final static String TAG = NetworkInterfaceHandler9AndUp.class.getName();

    @Override
    public List<InetAddress> getBroadcastAdresses(Context context) {
        Method m;
        try {
            m = InterfaceAddress.class.getMethod(GET_BRD_METHOD_NAME, NO_PARAMS);
            return getAddresses(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new LinkedList<InetAddress>();
        }
    }

    @Override
    public List<InetAddress> getLocalAddresses(Context context) {
        Method m;
        try {
            m = InterfaceAddress.class.getMethod(GET_ADDR_METHOD_NAME, NO_PARAMS);
            return getAddresses(m);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return new LinkedList<InetAddress>();
        }
    }

    private List<InetAddress> getAddresses(Method accessorMethod) {
        List<InetAddress> result = new LinkedList<InetAddress>();
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
            return result;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface currentInterface = networkInterfaces.nextElement();
            String currentIfaceName = currentInterface.getDisplayName();
            List<InterfaceAddress> ifaceAddresses = currentInterface.getInterfaceAddresses();
            for (InterfaceAddress ifaceAddr : ifaceAddresses) {
                try {
                    InetAddress addressToAdd = (InetAddress) accessorMethod.invoke(ifaceAddr, (Object[]) null);
                    if (addressToAdd == null) {
                        Log.v(TAG, accessorMethod.getName() + " : null addr on iface " + currentIfaceName);
                        continue;
                    }
                    if (addressToAdd.isLoopbackAddress()) {
                        Log.v(TAG, accessorMethod.getName() + " : loopback addr on iface" + currentIfaceName);
                        continue;
                    }
                    result.add(addressToAdd);
                    Log.v(TAG, accessorMethod.getName() + " : added " + addressToAdd.toString() + " on iface "
                            + currentIfaceName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
