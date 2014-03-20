package com.smartdevicelink.smartDeviceLinkConnection;

import com.smartdevicelink.protocol.ProtocolMessage;
import com.smartdevicelink.protocol.enums.SessionType;

public interface IsmartDeviceLinkConnectionListener {
	public void onTransportDisconnected(String info);
	
	public void onTransportError(String info, Exception e);
	
	public void onProtocolMessageReceived(ProtocolMessage msg);
	
	public void onProtocolSessionStarted(SessionType sessionType,
			byte sessionID, byte version, String correlationID);
	
	public void onProtocolSessionEnded(SessionType sessionType,
			byte sessionID, String correlationID);
	
	public void onProtocolError(String info, Exception e);
}
