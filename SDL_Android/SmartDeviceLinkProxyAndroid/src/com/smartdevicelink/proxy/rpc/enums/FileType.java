package com.smartdevicelink.proxy.rpc.enums;

/**
 * Enumeration listing possible file tpyes.
 * @since SmartDeviceLink 2.0
 */
public enum FileType {
	/**
	 * BMP
	 */
    GRAPHIC_BMP,
    /**
     * JPEG
     */
    GRAPHIC_JPEG,
    /**
     * PNG
     */
    GRAPHIC_PNG,
    /**
     * WAVE
     */
    AUDIO_WAVE,
    /**
     * MP3
     */
    AUDIO_MP3;

    /**
     * Convert String to FileType
     * @param value String
     * @return FileType
     */      
    public static FileType valueForString(String value) {
        return valueOf(value);
    }
}
