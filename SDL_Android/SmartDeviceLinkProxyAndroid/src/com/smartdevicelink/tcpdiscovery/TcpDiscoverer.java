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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.smartdevicelink.tcpdiscovery.DiscoveredDevice.ConnectionType;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

/**
 * Discovery is performed on the background thread. It lasts no more than
 * SO_TIMEOUT_MILLIS + DISCOVERY_TIMEOUT_MILLIS milliseconds. When discovery is
 * completed, TcpDiscovererCallback will receive either a list of discovered
 * devices (onFoundDevices), or a notification that no devices have been
 * discovered (onFoundNothing)
 */
public class TcpDiscoverer {
    private static final int UDP_PORT = 45678;
    public static final int SO_TIMEOUT_MILLIS = 500;
    public static final int DISCOVERY_TIMEOUT_MILLIS = 1500;
    private static final int BUFFER_SIZE = 1024;
    private static final String HANDSHAKE_TEXT = "SMARTDEVICELINK\0";
    private static final int NAME_LENGTH = 32;
    private static final int UUID_LENGTH = 36;
    private static final int IP_LENGTH = 16;
    private final static String WLAN_IFACE = "wlan";
    private static final int UI_THREAD_POST_DELAY_MILLIS = 10;
    private static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;
    private static final int MIN_PACKET_SIZE = HANDSHAKE_TEXT.length() + SIZEOF_INT;

    private static final String TAG = TcpDiscoverer.class.getName();

    private TcpDiscovererCallback mCallback;
    private Thread mSearchThread;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean mHasMulticastPermission;

    private Context mContext;

    /**
     * Creates a new instance with provided callback
     * 
     * @param callback
     */
    public TcpDiscoverer(Context context, TcpDiscovererCallback callback) {
        mCallback = callback;
        mContext = context.getApplicationContext();
        mHasMulticastPermission = checkMulticastPermission(context);
    }

    /**
     * Creates a new instance with TcpDiscovererCallbackDefaultImpl as a
     * callback
     * 
     * @param context
     *            if it gets destroyed, TcpDiscovererCallbackDefaultImpl's
     *            dismissDialog should be called
     * @param delegate
     * 
     * @deprecated use {@link TcpDiscoverer(Activity, TcpDiscovererCallbackDefaultDelegate) instead}}
     */
    @Deprecated
    public TcpDiscoverer(Activity context, TcpDiscovererCallbackDefaultImplDelegate delegate) {
        mCallback = new TcpDiscovererCallbackDefaultImpl(context, delegate);
        mContext = context.getApplicationContext();
        mHasMulticastPermission = checkMulticastPermission(context);
    }


    public TcpDiscoverer(Activity context, TcpDiscovererCallbackDefaultDelegate delegate) {
        mCallback = new TcpDiscovererCallbackDefaultImpl(context, delegate);
        mContext = context.getApplicationContext();
        mHasMulticastPermission = checkMulticastPermission(context);
    }
    
    /**
     * @return TcpDiscovererCallback (if the default implementation is used it
     *         may be necessary to access it - for example if the activity
     *         context gets destroyed its dialog should be dismissed)
     */
    public TcpDiscovererCallback getCallback() {
        return mCallback;
    }

    /**
     * Starts a search on a background thread (this call returns immediately) if
     * it is not in progress already
     * 
     * @return true if the search has been started, false otherwise (when
     *         another search is already in progress)
     */
    public boolean performSearch() {
        if (mSearchThread == null || !mSearchThread.isAlive()) {
            mSearchThread = new Thread(mSearchLoop);
            mSearchThread.start();
            return true;
        }
        Log.w(TAG, "Discovery is still in progress!");
        return false;
    }

    private boolean checkMulticastPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE,
                context.getPackageName());
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }

    private MulticastLock acquireMulticastLock() {
        MulticastLock result = null;
        if (mContext != null && mHasMulticastPermission) {
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            result = wifi.createMulticastLock(this.toString());
            result.acquire();
        }
        return result;
    }

    private void releaseMulticastLock(MulticastLock lock) {
        if (lock != null) {
            lock.release();
        }
    }

    private Runnable mSearchLoop = new Runnable() {
        @Override
        public void run() {
            long beginTime = SystemClock.uptimeMillis();
            MulticastLock lock = acquireMulticastLock();
            DatagramSocket socket = null;
            NetworkInterfaceHandler addresses = null;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
                addresses = new NetworkInterfaceHandler8();
            } else {
                addresses = new NetworkInterfaceHandler9AndUp();
            }
            try {
                socket = new DatagramSocket(UDP_PORT);
                socket.setBroadcast(true);
                socket.setSoTimeout(SO_TIMEOUT_MILLIS);
                socket.setReuseAddress(true);
                if (socket.getReceiveBufferSize() < BUFFER_SIZE) {
                    Log.w(TAG, "recv bufffer to small, increasing");
                    socket.setReceiveBufferSize(BUFFER_SIZE);
                }
            } catch (SocketException e) {
                e.printStackTrace();
                if (socket != null) {
                    socket.close();
                }
                releaseMulticastLock(lock);
                callFoundNothing();
                return;
            }
            List<DiscoveredDevice> found = new ArrayList<DiscoveredDevice>();
            List<InetAddress> broadcastAddressed = addresses.getBroadcastAdresses(mContext);
            List<InetAddress> localAddresses = addresses.getLocalAddresses(mContext);
            if (broadcastAddressed.isEmpty()) {
                socket.close();
                releaseMulticastLock(lock);
                callFoundNothing();
                return;
            }
            boolean successfulSend = false;
            for (InetAddress brd : broadcastAddressed) {
                DatagramPacket inquryPacket = new DatagramPacket(HANDSHAKE_TEXT.getBytes(), HANDSHAKE_TEXT.length(),
                        brd, UDP_PORT);
                try {
                    socket.send(inquryPacket);
                    successfulSend = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!successfulSend) {
                socket.close();
                releaseMulticastLock(lock);
                callFoundNothing();
                return;
            }
            byte[] buf = new byte[BUFFER_SIZE];
            while (!isTimeout(beginTime)) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                Log.v(TAG, "received packet from address " + packet.getAddress());
                Log.v(TAG, "received packet length " + packet.getLength());
                InetAddress sender = packet.getAddress();
                if (localAddresses.contains(sender)) {
                    Log.v(TAG, "got echo from addr: " + sender);
                    continue;
                }
                if (packet.getLength() < MIN_PACKET_SIZE) {
                    Log.i(TAG, "packet is too short!");
                    continue;
                }
                byte[] data = packet.getData();
                String handshakeCmp = new String(data, 0, HANDSHAKE_TEXT.length());
                Log.v(TAG, "got message: " + handshakeCmp);
                if (!handshakeCmp.equals(HANDSHAKE_TEXT)) {
                    Log.i(TAG, "messages are not equal: got " + handshakeCmp + ", expected " + HANDSHAKE_TEXT);
                    continue;
                }
                ByteBuffer bb = ByteBuffer.wrap(data, HANDSHAKE_TEXT.length(), SIZEOF_INT);
                int port = bb.getInt();
                port = Integer.reverseBytes(port);
                Log.i(TAG, "parsed port: " + port);
                DiscoveredDevice device = null;
                if (packet.getLength() >= (MIN_PACKET_SIZE + NAME_LENGTH + UUID_LENGTH)) {
                    Log.i(TAG, "Extra payload detected");
                    String name;
                    try {
                        name = parseStringFromBuf(data, MIN_PACKET_SIZE, NAME_LENGTH);
                        Log.i(TAG, "Datagram contains extra: name = " + name);
                    } catch (Exception e) {
                        e.printStackTrace();
                        name = "";
                    }
                    String uuid;
                    try {
                        uuid = parseStringFromBuf(data, MIN_PACKET_SIZE + NAME_LENGTH, UUID_LENGTH);
                        Log.i(TAG, "Datagram contains extra: UUID = " + uuid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        uuid = "";
                    }
                    ConnectionType type = ConnectionType.WLAN;
                    String ip = null;
                    if (data.length > MIN_PACKET_SIZE + NAME_LENGTH + UUID_LENGTH) {
                        try {
                            ip = parseStringFromBuf(data, MIN_PACKET_SIZE + NAME_LENGTH + UUID_LENGTH, IP_LENGTH);
                            Log.v(TAG, " Sender IP " + ip);
                            if (ip.length() != 0) {
                                String iface = NetworkInterface.getByInetAddress(InetAddress.getByName(ip))
                                        .getDisplayName();
                                Log.v(TAG, " Sender iface " + iface);
                                if (!iface.contains(WLAN_IFACE)) {
                                    type = ConnectionType.USB;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    device = new DiscoveredDevice(sender.getHostAddress(), port, name, uuid, type, ip);
                } else {
                    device = new DiscoveredDevice(sender.getHostAddress(), port);
                }
                if (!found.contains(device)) {
                    Log.i(TAG, "adding new device to list: " + device);
                    found.add(device);
                } else {
                    Log.i(TAG, "already has this device in list: " + device);
                }
            }
            socket.close();
            releaseMulticastLock(lock);
            if (found.isEmpty()) {
                callFoundNothing();
            } else {
                callFoundDevices(found);
            }
        }
    };

    private String parseStringFromBuf(byte[] buf, int initialPos, int length) throws BufferUnderflowException {
        StringBuilder builder = new StringBuilder("");
        if (initialPos >= buf.length) {
            throw new BufferUnderflowException();
        }
        if (initialPos + length >= buf.length) {
            throw new BufferUnderflowException();
        }
        for (int i = initialPos; i < initialPos + length; i++) {
            byte symbol = buf[i];
            if (symbol == 0) {
                break;
            }
            builder.append((char) symbol);
        }
        return builder.toString();
    }

    private boolean isTimeout(long beginTime) {
        return SystemClock.uptimeMillis() - beginTime >= DISCOVERY_TIMEOUT_MILLIS;
    }

    private void callFoundNothing() {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mCallback.onFoundNothing();
            }
        }, UI_THREAD_POST_DELAY_MILLIS);
    }

    private void callFoundDevices(final List<DiscoveredDevice> ddevices) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                mCallback.onFoundDevices(ddevices);
            }
        }, UI_THREAD_POST_DELAY_MILLIS);
    }
}
