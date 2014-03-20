package com.smartdevicelinktester.logmessages;

import com.smartdevicelink.proxy.RPCMessage;

public class RPCLogMessage extends LogMessage{
	
	private RPCMessage message;
	
	public RPCLogMessage(RPCMessage rpc) {
		 message = rpc;
	}

	public RPCMessage getMessage() {
		return message;
	}

	public void setMessage(RPCMessage message) {
		this.message = message;
	}
	
	

}
