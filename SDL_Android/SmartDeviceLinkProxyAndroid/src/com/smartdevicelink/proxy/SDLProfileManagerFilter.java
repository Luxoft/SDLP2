package com.smartdevicelink.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.smartdevicelink.proxy.interfaces.IProfileManagerListener;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.interfaces.IProxyListenerProfileManager;

/**
 * @brief Filters out all responses and notifications not related to Profile
 *        Manager
 */
public class SDLProfileManagerFilter implements InvocationHandler {
    private static final Class<?>[] mClassArray = { IProxyListenerALM.class };

    /**
     * @param target
     * @param forceUiThreadInvokation
     *            true means that all callbacks will be executed on the UI
     *            thread
     * @return IProxyListener that ignores all unrelated to PM events, and
     *         passes all PM events to target
     */
    public static IProxyListenerALM buildFilter(IProfileManagerListener target, boolean forceUiThreadInvokation) {
        return (IProxyListenerALM) Proxy.newProxyInstance(SDLProfileManagerFilter.class.getClassLoader(), mClassArray,
                new SDLProfileManagerFilter(target, forceUiThreadInvokation));
    }

    private final static String TAG = SDLProfileManagerFilter.class.getName();
    private IProfileManagerListener mTarget;

    private boolean mLinkIsUp = false;
    private boolean mForceUiThread = false;

    private Method mLinkUp;
    private Method mLinkDown;
    private Handler mHandler;

    public SDLProfileManagerFilter(IProfileManagerListener target) {
        mTarget = target;
        try {
            mLinkUp = IProfileManagerListener.class.getMethod("onLinkUp", (Class<?>[]) null);
            mLinkDown = IProfileManagerListener.class.getMethod("onLinkDown", (Class<?>[]) null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public SDLProfileManagerFilter(IProfileManagerListener target, boolean forceMainThread) {
        this(target);
        mForceUiThread = forceMainThread;
        if (mForceUiThread) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.v(TAG, "Method invoked: " + method.getName());
        try {
            if (isMethodFromProfManCallbacksApi(method)) {
                return callMethod(method, args);
            } else if (method.getName().equals("onOnHMIStatus")) {
                if (!mLinkIsUp) {
                    callMethod(mLinkUp, null);
                    mLinkIsUp = true;
                }
                return getDefaultReturnValue(method);
            } else if (method.getName().equals("onProxyClosed") || method.getName().equals("onError")) {
                mLinkIsUp = false;
                callMethod(mLinkDown, null);
                return getDefaultReturnValue(method);
            } else {
                Log.i(TAG, "Method invoked: " + method.getName() + " is ignored");
                return getDefaultReturnValue(method);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultReturnValue(method);
        }
    }

    private Object callMethod(final Method m, final Object[] args) {
        if (mForceUiThread && !Looper.getMainLooper().equals(Looper.myLooper())) {
            mHandler.post(new Runnable() {
                public void run() {
                    try {
                        m.invoke(mTarget, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return getDefaultReturnValue(m);
        } else {
            try {
                return m.invoke(mTarget, args);
            } catch (Exception e) {
                e.printStackTrace();
                return getDefaultReturnValue(m);
            }
        }
    }

    private boolean isMethodFromProfManCallbacksApi(Method m) {
        return m.getDeclaringClass().equals(IProxyListenerProfileManager.class);
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
