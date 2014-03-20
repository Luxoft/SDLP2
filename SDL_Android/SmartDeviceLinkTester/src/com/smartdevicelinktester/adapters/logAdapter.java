package com.smartdevicelinktester.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.smartdevicelinktester.logmessages.LogMessage;
import com.smartdevicelinktester.logmessages.StringLogMessage;

public class logAdapter extends MessageAdapter {
	private String logTag;
	boolean fullUIDebug;
	Activity activity;

	public logAdapter(String logTag, boolean fullUIDebug, Activity activity, int textViewResourceId, ArrayList<LogMessage> items) {
		super(activity, textViewResourceId, items);
		this.activity = activity;
		this.logTag = logTag;
		this.fullUIDebug = fullUIDebug;
	}
	
    private void addMessageToUI(final LogMessage m) {    	
    	activity.runOnUiThread(new Runnable() {
			public void run() { addMessage(m); }
		});
    }
    
    public void logMessage (final LogMessage m) {
		logMessage(m, Log.INFO);
    	if(fullUIDebug) addMessageToUI(m);
	}
    public void logMessage (final LogMessage m, Boolean addToUI) {
    	logMessage(m, Log.INFO);		
    	addMessageToUI(m);
    }
    public void logMessage (final LogMessage m, Integer type) {
    	if (m instanceof StringLogMessage) {
    		switch(type) {
	    		case Log.DEBUG:
	    			Log.d(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.ERROR:
	    			Log.e(logTag,((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.VERBOSE:
	    			Log.v(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.WARN:
	    			Log.w(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		default:
	    			Log.i(logTag, ((StringLogMessage) m).getMessage());
	    			break;
    		}
    	}
    	if (fullUIDebug) addMessageToUI(m);
	}
    public void logMessage (final LogMessage m, Integer type, Boolean addToUI) {
    	if (m instanceof StringLogMessage) {
    		switch(type) {
	    		case Log.DEBUG:
	    			Log.d(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.ERROR:
	    			Log.e(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.VERBOSE:
	    			Log.v(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.WARN:
	    			Log.w(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		default:
	    			Log.i(logTag, ((StringLogMessage) m).getMessage());
	    			break;
    		}
    	}
    	if (addToUI) addMessageToUI(m);
	}
    public void logMessage (final LogMessage m, Integer type, Throwable tr) {
    	if (m instanceof StringLogMessage) {
    		switch(type) {
	    		case Log.DEBUG:
	    			Log.d(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.ERROR:
	    			Log.e(logTag, ((StringLogMessage) m).getMessage(), tr);
	    			break;
	    		case Log.VERBOSE:
	    			Log.v(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.WARN:
	    			Log.w(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		default:
	    			Log.i(logTag, ((StringLogMessage) m).getMessage());
	    			break;
    		}
    	}
    	if (fullUIDebug) addMessageToUI(m);
	}
    public void logMessage (final LogMessage m, Integer type, Throwable tr, Boolean addToUI) {
    	if (m instanceof StringLogMessage) {
    		switch(type) {
	    		case Log.DEBUG:
	    			Log.d(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.ERROR:
	    			Log.e(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.VERBOSE:
	    			Log.v(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		case Log.WARN:
	    			Log.w(logTag, ((StringLogMessage) m).getMessage());
	    			break;
	    		default:
	    			Log.i(logTag, ((StringLogMessage) m).getMessage());
	    			break;
    		}
    	}
    	if (addToUI) addMessageToUI(m);
	}
}