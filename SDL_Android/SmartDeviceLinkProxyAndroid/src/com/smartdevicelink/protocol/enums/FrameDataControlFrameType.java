package com.smartdevicelink.protocol.enums;

import java.util.Vector;

import com.smartdevicelink.util.ByteEnumer;

public class FrameDataControlFrameType extends ByteEnumer {
	private static Vector theList = new Vector();
	public static Vector getList() { return theList; } 

	private byte _i = 0x00;

	protected FrameDataControlFrameType(byte value, String name) {super(value, name);}
	public final static FrameDataControlFrameType StartSession = new FrameDataControlFrameType((byte)0x01, "StartSession");
	public final static FrameDataControlFrameType StartSessionACK = new FrameDataControlFrameType((byte)0x02, "StartSessionACK");
	public final static FrameDataControlFrameType StartSessionNACK = new FrameDataControlFrameType((byte)0x03, "StartSessionNACK");
	public final static FrameDataControlFrameType EndSession = new FrameDataControlFrameType((byte)0x04, "EndSession");

	static {
		theList.addElement(StartSession);
		theList.addElement(StartSessionACK);
		theList.addElement(StartSessionNACK);
		theList.addElement(EndSession);	
	}

	public static FrameDataControlFrameType valueOf(String passedButton) {
		return (FrameDataControlFrameType) get(theList, passedButton);
	} // end-method

	public static FrameDataControlFrameType[] values() {
		return (FrameDataControlFrameType[]) theList.toArray();
	} // end-method
} // end-class