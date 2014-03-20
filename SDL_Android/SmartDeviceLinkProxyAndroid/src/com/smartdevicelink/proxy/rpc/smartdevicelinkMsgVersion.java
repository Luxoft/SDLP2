package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;

/**
 * Specifies the version number of the SMARTDEVICELINK V4 interface. This is used by both the application and SMARTDEVICELINK to declare what interface version each is using.
 * <p><b> Parameter List
 * <table border="1" rules="all">
 * 		<tr>
 * 			<th>Name</th>
 * 			<th>Type</th>
 * 			<th>Description</th>
 * 			<th>SmartDeviceLink Ver. Available</th>
 * 		</tr>
 * 		<tr>
 * 			<td>majorVersion</td>
 * 			<td>Int16</td>
 * 			<td>
 * 					<ul>
 * 					<li>minvalue="1"</li>
 * 				    <li>maxvalue="10"</li>
 *					</ul>
 *			</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 * 		<tr>
 * 			<td>minorVersion</td>
 * 			<td>Int16</td>
 * 			<td>
 * 					<ul>
 * 					<li>minvalue="0"</li>
 * 				    <li>maxvalue="1000"</li>
 *					</ul>
 *			</td>
 * 			<td>SmartDeviceLink 1.0</td>
 * 		</tr>
 * </table> 
 * @since SmartDeviceLink 1.0
 */
public class smartdevicelinkMsgVersion extends RPCStruct {

	/**
	 * Constructs a newly allocated smartdevicelinkMsgVersion object
	 */
	public smartdevicelinkMsgVersion() { }
    /**
     * Constructs a newly allocated smartdevicelinkMsgVersion object indicated by the Hashtable parameter
     * @param hash The Hashtable to use
     */    
	public smartdevicelinkMsgVersion(Hashtable hash) {
        super(hash);
    }
    /**
     * Get major version
     * 					<ul>
     * 					<li>minvalue="1"</li>
     * 				    <li>maxvalue="10"</li>
     *					</ul>
     * @return the major version
     */	
    public Integer getMajorVersion() {
        return (Integer) store.get( Names.majorVersion );
    }
    /**
     * Set major version
     * 					<ul>
     * 					<li>minvalue="1"</li>
     * 				    <li>maxvalue="10"</li>
     *					</ul>
     * @param majorVersion minvalue="1" and maxvalue="10" 
     */    
    public void setMajorVersion( Integer majorVersion ) {
        if (majorVersion != null) {
            store.put(Names.majorVersion, majorVersion );
        }
    }
    /**
     * Get minor version
     * 					<ul>
     * 					<li>minvalue="0"</li>
     * 				    <li>maxvalue="1000"</li>
     *					</ul>
     * @return the minor version
     */    
    public Integer getMinorVersion() {
        return (Integer) store.get( Names.minorVersion );
    }
    /**
     * Set minor version
     * 					<ul>
     * 					<li>minvalue="0"</li>
     * 				    <li>maxvalue="1000"</li>
     *					</ul>
     * @param minorVersion min: 0; max: 1000
     */
    public void setMinorVersion( Integer minorVersion ) {
        if (minorVersion != null) {
            store.put(Names.minorVersion, minorVersion );
        }
    }
}