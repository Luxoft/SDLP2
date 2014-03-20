package com.smartdevicelink.proxy.rpc.enums;
/**
 * Contains information about TTS capabilities on the SMARTDEVICELINK platform.
 * 
 * @since SmartDeviceLink 1.0
 */
public enum SpeechCapabilities {
	/**
	 * The SMARTDEVICELINK platform can speak text phrases.
	 * 
	 * @since SmartDeviceLink 1.0
	 */	
    TEXT,
    SAPI_PHONEMES,
    LHPLUS_PHONEMES,
    PRE_RECORDED,
    SILENCE;

    public static SpeechCapabilities valueForString(String value) {
        return valueOf(value);
    }
}
