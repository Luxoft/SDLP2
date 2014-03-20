/**
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
package com.smartdevicelink.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.os.Handler;
import android.os.Looper;

/**
 * Proxy created with this handler will pass method calls to the UI thread, if
 * the flag isOnMainThread is set to true (otherwise methods will be invoked
 * synchronously on the caller thread). All method calls will return
 * <code>null</code>, <code>0</code> , or <code>false</code> depending on the
 * signature (actual return value is ignored).
 */
public class MainThreadInvocationHandler implements InvocationHandler {
    private Object mTarget;
    private boolean mIsOnMainThread;
    private Handler mHandler;

    public MainThreadInvocationHandler(Object target, boolean isOnMainThread) {
        mTarget = target;
        mIsOnMainThread = isOnMainThread;
        if (isOnMainThread) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        if (mIsOnMainThread && (!Looper.getMainLooper().equals(Looper.myLooper()))) {
            mHandler.post(new Runnable() {
                public void run() {
                    try {
                        method.invoke(mTarget, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            try {
                method.invoke(mTarget, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getDefaultReturnValue(method);
    }

    private Object getDefaultReturnValue(Method m) {
        if (!m.getReturnType().isPrimitive() || m.getReturnType() == void.class) {
            return null;
        }
        if (m.getReturnType() == boolean.class) {
            return Boolean.valueOf(false);
        }
        if (m.getReturnType() == short.class) {
            return Short.valueOf((short) 0);
        }
        if (m.getReturnType() == int.class) {
            return Integer.valueOf(0);
        }
        if (m.getReturnType() == long.class) {
            return Long.valueOf(0);
        }
        if (m.getReturnType() == double.class) {
            return Double.valueOf(.0);
        }
        if (m.getReturnType() == byte.class) {
            return Byte.valueOf((byte) 0);
        }
        if (m.getReturnType() == char.class) {
            return Character.valueOf((char) 0);
        }
        if (m.getReturnType() == float.class) {
            return Float.valueOf(0.f);
        }
        throw new Error("Unknown return type: " + m.getReturnType());
    }
}
