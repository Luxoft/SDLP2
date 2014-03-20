package com.smartdevicelink.exception;


public class SmartDeviceLinkException extends Exception {
	
	private static final long serialVersionUID = 5922492291870772815L;
	
	protected Throwable detail = null;
	private SmartDeviceLinkExceptionCause _SmartDeviceLinkExceptionCause = null;
	
	public SmartDeviceLinkException(String msg, SmartDeviceLinkExceptionCause exceptionCause) {
		super(msg);
		_SmartDeviceLinkExceptionCause = exceptionCause;
	}
	
	public SmartDeviceLinkException(String msg, Throwable ex, SmartDeviceLinkExceptionCause exceptionCause) {
		super(msg + " --- Check inner exception for diagnostic details");
		detail = ex;
		_SmartDeviceLinkExceptionCause = exceptionCause;
	}
	
	public SmartDeviceLinkException(Throwable ex) {
		super(ex.getMessage());
		detail = ex;
	}
	
	public SmartDeviceLinkExceptionCause getSmartDeviceLinkExceptionCause() {
		return _SmartDeviceLinkExceptionCause;
	}
	
	public Throwable getInnerException() {
		return detail;
	}
	
	public String toString() {
		String ret = this.getClass().getName();
		ret += ": " + this.getMessage();
		if(this.getSmartDeviceLinkExceptionCause() != null){
			ret += "\nSmartDeviceLinkExceptionCause: " + this.getSmartDeviceLinkExceptionCause().name();
		}
		if (detail != null) {
			ret += "\nnested: " + detail.toString();
			detail.printStackTrace();
		}
		return ret;
	}
}
