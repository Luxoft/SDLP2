package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.util.DebugTool;

/**
 * Provides information about the capabilities of a SMARTDEVICELINK HMI button.
 * <p><b> Parameter List
 * <table border="1" rules="all">
 * 		<tr>
 * 			<th>Name</th>
 * 			<th>Type</th>
 * 			<th>Description</th>
 * 			<th>SmartDeviceLink Ver. Available</th>
 * 		</tr>
 * 		<tr>
 * 			<td>name</td>
 * 			<td>ButtonName</td>
 * 			<td>The name of theSMARTDEVICELINK HMI button.</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>shortPressAvailable</td>
 * 			<td>Boolean</td>
 * 			<td>The button supports a SHORT press. See ButtonPressMode for more information.</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 *     <tr>
 * 			<td>longPressAvailable</td>
 * 			<td>Boolean</td>
 * 			<td>The button supports a LONG press. See ButtonPressMode for more information.</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 *     <tr>
 * 			<td>upDownAvailable</td>
 * 			<td>Boolean</td>
 * 			<td>The button supports "button down" and "button up". When the button is depressed, the <i>{@linkplain OnButtonEvent}</i> notification will be invoked with a value of BUTTONDOWN.
 *                  <p> When the button is released, the <i>{@linkplain OnButtonEvent}</i> notification will be invoked with a value of BUTTONUP.</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 * </table>
 * @since SmartDeviceLink 1.0
 */
public class ButtonCapabilities extends RPCStruct {
	/**
	 * Constructs a newly allocated ButtonCapabilities object
	 */
    public ButtonCapabilities() { }
    /**
     * Constructs a newly allocated ButtonCapabilities object indicated by the Hashtable parameter
     * @param hash The Hashtable to use
     */
    public ButtonCapabilities(Hashtable hash) {
        super(hash);
    }
    /**
     * Get the name of theSMARTDEVICELINK HMI button.
     * @return ButtonName the name of the Button
     */    
    public ButtonName getName() {
        Object obj = store.get(Names.name);
        if (obj instanceof ButtonName) {
            return (ButtonName) obj;
        } else if (obj instanceof String) {
            ButtonName theCode = null;
            try {
                theCode = ButtonName.valueForString((String) obj);
            } catch (Exception e) {
                DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.name, e);
            }
            return theCode;
        }
        return null;
    }
    /**
     * Set the name of theSMARTDEVICELINK HMI button.
     * @param name the name of button
     */    
    public void setName( ButtonName name ) {
        if (name != null) {
            store.put(Names.name, name );
        }
    }
    /**
     * Whether the button supports a SHORT press. See <i>{@linkplain com.smartdevicelink.proxy.rpc.enums.ButtonPressMode}</i> for more information.
     * @return True if support otherwise False.
     */    
    public Boolean getShortPressAvailable() {
        return (Boolean) store.get( Names.shortPressAvailable );
    }
    /**
     * Set the button supports a SHORT press. See <i>{@linkplain com.smartdevicelink.proxy.rpc.enums.ButtonPressMode}</i> for more information.
     * @param shortPressAvailable True if support otherwise False.
     */    
    public void setShortPressAvailable( Boolean shortPressAvailable ) {
        if (shortPressAvailable != null) {
            store.put(Names.shortPressAvailable, shortPressAvailable );
        }
    }
    /**
     * Whether the button supports a LONG press. See <i>{@linkplain com.smartdevicelink.proxy.rpc.enums.ButtonPressMode}</i> for more information.
     * @return True if support otherwise False.
     */
    public Boolean getLongPressAvailable() {
        return (Boolean) store.get( Names.longPressAvailable );
    }
    /**
     * Set the button supports a LONG press. See <i>{@linkplain com.smartdevicelink.proxy.rpc.enums.ButtonPressMode}</i> for more information.
     * @param longPressAvailable True if support otherwise False.
     */    
    public void setLongPressAvailable( Boolean longPressAvailable ) {
        if (longPressAvailable != null) {
            store.put(Names.longPressAvailable, longPressAvailable );
        }
    }
    /**
     * Whether the button supports "button down" and "button up". When the button is depressed, the <i>{@linkplain OnButtonEvent}</i> notification will be invoked with a value of BUTTONDOWN.
     * @return True if support otherwise False.
     */    
    public Boolean getUpDownAvailable() {
        return (Boolean) store.get( Names.upDownAvailable );
    }
    /**
     * Set the button supports "button down" and "button up". When the button is depressed, the <i>{@linkplain OnButtonEvent}</i> notification will be invoked with a value of BUTTONDOWN.
     * @param upDownAvailable True if support otherwise False.
     */    
    public void setUpDownAvailable( Boolean upDownAvailable ) {
        if (upDownAvailable != null) {
            store.put(Names.upDownAvailable, upDownAvailable );
        }
    }
}
