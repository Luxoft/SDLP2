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

import java.util.List;

import com.smartdevicelink.tcpdiscovery.DiscoveredDevice.ConnectionType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Default implementation of the TcpDiscovererCallback - displays a dialog with
 * list of found devices. All user interactions with the dialog are passed to
 * the delegate (see TcpDiscovererCallbackDefaultImplDelegate)
 */
@SuppressWarnings(value = { "deprecation" })
public class TcpDiscovererCallbackDefaultImpl implements TcpDiscovererCallback {

    private TcpDiscovererCallbackDefaultImplDelegate mDelegate;
    private TcpDiscovererCallbackDefaultDelegate mNewDelegate;
    final boolean mIsNewDelegate;
    private Activity mActivityContext;

    private AlertDialog mDialog;

    private List<DiscoveredDevice> mDiscoveredDevices;

    @Deprecated
    public TcpDiscovererCallbackDefaultImpl(Activity context, TcpDiscovererCallbackDefaultImplDelegate delegate) {
        mActivityContext = context;
        mDelegate = delegate;
        mIsNewDelegate = false;
    }

    public TcpDiscovererCallbackDefaultImpl(Activity context, TcpDiscovererCallbackDefaultDelegate delegate) {
        mActivityContext = context;
        mNewDelegate = delegate;
        mIsNewDelegate = true;
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onFoundNothing() {
        if (mIsNewDelegate) {
            mNewDelegate.onDiscovererFoundNothing();
        } else {
            mDelegate.onDiscovererFoundNothing();
        }
    }

    @Override
    public void onFoundDevices(List<DiscoveredDevice> devices) {
        if (devices.size() == 1 && devices.get(0).getConnectionType() == ConnectionType.USB) {
            DiscoveredDevice device = devices.get(0);
            if (mIsNewDelegate) {
                mNewDelegate.onUserSelectedItem(device);
            } else {
                mDelegate.onUserSelectedItem(device.getIP(), device.getPort());
            }
            return;
        }
        mDiscoveredDevices = devices;
        mDialog = createDialog();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivityContext);
        builder.setTitle("Discovered devices:");
        CharSequence[] items = new CharSequence[mDiscoveredDevices.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = mDiscoveredDevices.get(i).toString();
        }
        builder.setItems(items, mOnItemClickListener);
        if (mIsNewDelegate) {
            builder.setNegativeButton(mNewDelegate.getDismissDialogButtonName(), mOnCancelClickListener);
        } else {
            builder.setNegativeButton(mDelegate.getDismissDialogButtonName(), mOnCancelClickListener);
        }
        builder.setCancelable(false);
        return builder.create();
    }

    private DialogInterface.OnClickListener mOnItemClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mDialog.dismiss();
            if (mIsNewDelegate) {
                mNewDelegate.onUserSelectedItem(mDiscoveredDevices.get(which));
            } else {
                mDelegate.onUserSelectedItem(mDiscoveredDevices.get(which).getIP(), mDiscoveredDevices.get(which)
                        .getPort());
            }
        }
    };

    private DialogInterface.OnClickListener mOnCancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mDialog.dismiss();
            if (mIsNewDelegate) {
                mNewDelegate.onUserDismissedDialog();
            } else {
                mDelegate.onUserDismissedDialog();
            }
        }
    };
}
