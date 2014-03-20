package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.util.DebugTool;

/**
 * Used to push a binary data onto the SMARTDEVICELINK module from a mobile device, such as
 * icons and album art
 * <p>
 * 
 * @since SmartDeviceLink 2.0
 * @see DeleteFile
 * @see ListFiles
 */
public class PutFile extends RPCRequest {

	/**
	 * Constructs a new PutFile object
	 */
    public PutFile() {
        super("PutFile");
    }

	/**
	 * Constructs a new PutFile object indicated by the Hashtable parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public PutFile(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets a file reference name
	 * 
	 * @param smartDeviceLinkFileName
	 *            a String value representing a file reference name
	 *            <p>
	 *            <b>Notes: </b>Maxlength=500
	 */
    public void setSmartDeviceLinkFileName(String smartDeviceLinkFileName) {
        if (smartDeviceLinkFileName != null) {
            parameters.put(Names.smartDeviceLinkFileName, smartDeviceLinkFileName);
        } else {
        	parameters.remove(Names.smartDeviceLinkFileName);
        }
    }

	/**
	 * Gets a file reference name
	 * 
	 * @return String - a String value representing a file reference name
	 */
    public String getSmartDeviceLinkFileName() {
        return (String) parameters.get(Names.smartDeviceLinkFileName);
    }

	/**
	 * Sets file type
	 * 
	 * @param fileType
	 *            a FileType value representing a selected file type
	 */
    public void setFileType(FileType fileType) {
        if (fileType != null) {
            parameters.put(Names.fileType, fileType);
        } else {
        	parameters.remove(Names.fileType);
        }
    }

	/**
	 * Gets a file type
	 * 
	 * @return FileType -a FileType value representing a selected file type
	 */
    public FileType getFileType() {
        Object obj = parameters.get(Names.fileType);
        if (obj instanceof FileType) {
            return (FileType) obj;
        } else if (obj instanceof String) {
        	FileType theCode = null;
            try {
                theCode = FileType.valueForString((String) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.fileType, e);
            }
            return theCode;
        }
        return null;
    }

	/**
	 * Sets a value to indicates if the file is meant to persist between
	 * sessions / ignition cycles. If set to TRUE, then the system will aim to
	 * persist this file through session / cycles. While files with this
	 * designation will have priority over others, they are subject to deletion
	 * by the system at any time. In the event of automatic deletion by the
	 * system, the app will receive a rejection and have to resend the file. If
	 * omitted, the value will be set to false
	 * <p>
	 * 
	 * @param persistentFile
	 *            a Boolean value
	 */
    public void setPersistentFile(Boolean persistentFile) {
        if (persistentFile != null) {
            parameters.put(Names.persistentFile, persistentFile);
        } else {
        	parameters.remove(Names.persistentFile);
        }
    }

	/**
	 * Gets a value to Indicates if the file is meant to persist between
	 * sessions / ignition cycles
	 * 
	 * @return Boolean -a Boolean value to indicates if the file is meant to
	 *         persist between sessions / ignition cycles
	 */
    public Boolean getPersistentFile() {
        return (Boolean) parameters.get(Names.persistentFile);
    }
    public void setFileData(byte[] fileData) {
        if (fileData != null) {
            parameters.put(Names.bulkData, fileData);
        } else {
        	parameters.remove(Names.bulkData);
        }
    }
    public byte[] getFileData() {
        return (byte[]) parameters.get(Names.bulkData);
    }
}
