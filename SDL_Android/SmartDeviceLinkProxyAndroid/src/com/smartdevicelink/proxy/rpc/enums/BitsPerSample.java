package com.smartdevicelink.proxy.rpc.enums;

import java.util.EnumSet;

/**
 * Describes different bit depth options for PerformAudioPassThru
 * 
 */
public enum BitsPerSample {
	/**
	 * 8 bits per sample
	 * 
	 * @since SmartDeviceLink 2.0
	 */
	_8_BIT("8_BIT"),
	/**
	 * 16 bits per sample
	 * 
	 * @since SmartDeviceLink 2.0
	 */
	_16_BIT("16_BIT");

    String internalName;
    
    private BitsPerSample(String internalName) {
        this.internalName = internalName;
    }
    
    public String toString() {
        return this.internalName;
    }
    
    public static BitsPerSample valueForString(String value) {       	
    	for (BitsPerSample anEnum : EnumSet.allOf(BitsPerSample.class)) {
            if (anEnum.toString().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
