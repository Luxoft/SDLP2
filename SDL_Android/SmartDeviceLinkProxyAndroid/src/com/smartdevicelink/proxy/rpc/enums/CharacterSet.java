package com.smartdevicelink.proxy.rpc.enums;
/**
 * Character sets supported by SMARTDEVICELINK.
 * @since SmartDeviceLink 1.0
 */
public enum CharacterSet {
    TYPE2SET,
    TYPE5SET,
    CID1SET,
    CID2SET;

    /**
     * Convert String to CharacterSet
     * @param value String
     * @return CharacterSet
     */
    public static CharacterSet valueForString(String value) {
        return valueOf(value);
    }
}
