package com.smartdevicelink.proxy.rpc.enums;

/**
 * Names of the text fields that can appear on a SMARTDEVICELINK display.
 * @since SmartDeviceLink 1.0
 */
public enum TextFieldName {
	/**
	 * The first line of the first set of main fields of the persistent display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
	 */
	mainField1,
    /**
     * The second line of the first set of main fields of the persistent display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
     */	
    mainField2,
    /**
     * The first line of the second set of main fields of the persistent display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
     * @since SmartDeviceLink 2.0
     */
    mainField3,
    /**
     * The second line of the second set of main fields of the persistent display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
     * @since SmartDeviceLink 2.0
     */    
    mainField4,
    /**
     * The status bar on the NGN display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
     */    
    statusBar,
    /**
     * Text value for MediaClock field. Must be properly formatted according to MediaClockFormat. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
	 * <p>This field is commonly used to show elapsed or remaining time in an audio track or audio capture
     */    
    mediaClock,
    /**
     * The track field of NGN type ACMs. This field is only available for media applications on a NGN display. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Show}.
	 * <p>This field is commonly used to show the current track number
     */    
    mediaTrack,
    /**
     * The first line of the alert text field. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Alert}.
     */
    alertText1,
    /**
     * The second line of the alert text field. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Alert}.
     */
    alertText2,
    /**
     * The third line of the alert text field. Applies to {@linkplain com.smartdevicelink.proxy.rpc.Alert}.
     * @since SmartDeviceLink 2.0
     */
    alertText3,
    /**
     * Long form body of text that can include newlines and tabs. Applies to ScrollableMessage TBD
     * @since SmartDeviceLink 2.0
     */
    scrollableMessageBody,
    /**
     * First line suggestion for a user response (in the case of VR enabled interaction).
     * @since SmartDeviceLink 2.0
     */
    initialInteractionText,
    /**
     * First line of navigation text.
     * @since SmartDeviceLink 2.0
     */
    navigationText1,
    /**
     * Second  line of navigation text.
     * @since SmartDeviceLink 2.0
     */
    navigationText2,
    /**
     * Estimated Time of Arrival time for navigation.
     * @since SmartDeviceLink 2.0
     */
    ETA,
    /**
     * Total distance to destination for navigation.
     * @since SmartDeviceLink 2.0
     */
    totalDistance,
    /**
     * First line of text for audio pass thru.
     * @since SmartDeviceLink 2.0
     */
    audioPassThruDisplayText1,
    /**
     * Second line of text for audio pass thru.
     * @since SmartDeviceLink 2.0
     */
    audioPassThruDisplayText2,
    /**
     * Header text for slider.
     * @since SmartDeviceLink 2.0
     */
    sliderHeader,
    /**
     * Footer text for slider
     * @since SmartDeviceLink 2.0
     */
    sliderFooter;
    
	/**
     * Convert String to TextFieldName
     * @param value String
     * @return TextFieldName
     */
    public static TextFieldName valueForString(String value) {
        return valueOf(value);
    }
}
