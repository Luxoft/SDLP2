package com.smartdevicelink.protocol;

import com.smartdevicelink.protocol.enums.*;

public interface IProtocolListener {
	// Called to indicate that these bytes are to be sent as part of a message.
	// This call includes the part of the message.
	void onProtocolMessageBytesToSend(byte[] msgBytes, int offset, int length);

	// Called to indicate that a complete message (RPC, BULK, etc.) has been
	// received.  This call includes the message.
	void onProtocolMessageReceived(ProtocolMessage msg);

	// Called to indicate that a protocol session has been started (from either side)
	void onProtocolSessionStarted(SessionType sessionType, byte sessionID, byte version, String correlationID);

	// Called to indicate that a protocol session has ended (from either side)
	void onProtocolSessionEnded(SessionType sessionType, byte sessionID, String correlationID /*, String info, Exception ex*/);

	// Called to indicate that a protocol error was detected in received data.
	void onProtocolError(String info, Exception e);
} // end-interfCe