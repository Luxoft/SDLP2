package com.smartdevicelinktester.logmessages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMessage {
	
	private String date;
	
	public LogMessage() {
		date = new SimpleDateFormat("hh:mm:ss SSSS").format(new Date(System.currentTimeMillis()));
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
