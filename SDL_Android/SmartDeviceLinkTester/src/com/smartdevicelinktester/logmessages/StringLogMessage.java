package com.smartdevicelinktester.logmessages;

import android.graphics.Color;


public class StringLogMessage extends LogMessage {
	
	private String message;
	private int iColor = Color.WHITE;
	private String sData = "";
	
	public StringLogMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setColor(int theColor) {
		this.iColor = theColor;
	}

	public int getColor() {
		return iColor;
	}	
	
	public void setData(String theData) {
		this.sData = theData;
	}

	public String getData() {
		return sData;
	}	

}
