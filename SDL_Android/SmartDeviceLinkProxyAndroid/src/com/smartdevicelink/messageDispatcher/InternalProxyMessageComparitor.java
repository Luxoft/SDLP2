package com.smartdevicelink.messageDispatcher;

import java.util.Comparator;

import com.smartdevicelink.proxy.callbacks.InternalProxyMessage;

public class InternalProxyMessageComparitor implements Comparator<InternalProxyMessage> {

	@Override
	public int compare(InternalProxyMessage arg0, InternalProxyMessage arg1) {
		// FIFO order
		// TODO change to simple LinkedBlockingQueue
		return arg0.getCreationTime().compareTo(arg1.getCreationTime());
	}
}
