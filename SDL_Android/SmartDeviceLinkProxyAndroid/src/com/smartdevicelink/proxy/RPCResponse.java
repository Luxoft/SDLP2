/**
 * 
 */
package com.smartdevicelink.proxy;

import java.util.Hashtable;

import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.util.DebugTool;

/**
 * Result sent by SMARTDEVICELINK after an RPC is processed, consists of four parts: 
 * <ul>
 * <li>
 * CorrelationID:
 * <ul>
 * An integer value correlating the response to the corresponding request.
 * </ul>
 * </li> <li>Success:
 * <ul>
 * A Boolean indicating whether the original request was successfully processed.
 * </ul>
 * </li> <li>ResultCode:
 * <ul>
 * 
 * The result code provides additional information about a response returning a
 * failed outcome.
 * <br>
 * 
 * Any response can have at least one, or possibly more, of the following result
 * code values: SUCCESS, INVALID_DATA, OUT_OF_MEMORY, TOO_MANY_PENDING_REQUESTS,
 * APPLICATION_NOT_REGISTERED, GENERIC_ERROR,REJECTED.
 * <br>
 * 
 * Any additional result codes for a given operation can be found in related
 * RPCs
 * <br>
 * </ul>
 * </li> <li>Info:
 * <ul>
 * A string of text representing additional information returned from SMARTDEVICELINK. This
 * could be useful in debugging.
 * </ul>
 * </li>
 * </ul>
 */
public class RPCResponse extends RPCMessage {
	/**
	*<p>Constructs a newly allocated RPCResponse object using function name</p>
	*@param functionName a string that indicates the function's name
	*/
	public RPCResponse(String functionName) {
		super(functionName, "response");
	}
	/**
     *<p>Constructs a newly allocated RPCResponse object indicated by the Hashtable parameter</p>
     *@param hash The Hashtable to use
     */   
	public RPCResponse(Hashtable hash) {
		super(hash);
	}
	/**
     *<p>Constructs a newly allocated RPCResponse object using a RPCMessage object</p>
     *@param rpcMsg The {@linkplain RPCMessage} to use
     */   
	public RPCResponse(RPCMessage rpcMsg) {
		super(rpcMsg);
	}

	/**
	 * <p>
	 * Returns correlationID the ID of the request
	 * </p>
	 * 
	 * @return int  the ID of the request
	 */
	public Integer getCorrelationID() {
		return (Integer)function.get(Names.correlationID);
	}
	
	/**
	 * <p>
	 * Set the correlationID
	 * </p>
	 * 
	 * @param correlationID
	 *            the ID of the response
	 */
	public void setCorrelationID(Integer correlationID) {
		if (correlationID != null) {
            function.put(Names.correlationID, correlationID );
        } else if (parameters.contains(Names.correlationID)){
        	function.remove(Names.correlationID);
        }
	}
	/**
	 * <p>
	 * Returns Success whether the request is successfully processed
	 * </p>
	 * 
	 * @return Boolean  the status of whether the request is successfully done
	 */
	public Boolean getSuccess() {
        return (Boolean) parameters.get( Names.success );
    }
	/**
	 * <p>
	 * Set the Success status
	 * </p>
	 * 
	 * @param success
	 *             whether the request is successfully processed
	 */
    public void setSuccess( Boolean success ) {
        if (success != null) {
            parameters.put(Names.success, success );
        }
    }
	/**
	 * <p>
	 * Returns ResultCode additional information about a response returning a failed outcome
	 * </p>
	 * 
	 * @return {@linkplain Result}  the status of whether the request is successfully done
	 */
    public Result getResultCode() {
        Object obj = parameters.get(Names.resultCode);
        if (obj instanceof Result) {
            return (Result) obj;
        } else if (obj instanceof String) {
            Result theCode = null;
            try {
                theCode = Result.valueForString((String) obj);
            } catch (Exception e) {
                DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.resultCode, e);
            }
            return theCode;
        }
        return null;
    }
	/**
	 * <p>
	 * Set the additional information about a response returning a failed outcome
	 * </p>
	 * 
	 * @param resultCode
	 *             whether the request is successfully processed
	 */
    public void setResultCode( Result resultCode ) {
        if (resultCode != null) {
            parameters.put(Names.resultCode, resultCode );
        }
    }
	/**
	 * <p>
	 * Returns a string of text representing additional information returned from SMARTDEVICELINK
	 * </p>
	 * 
	 * @return String  A string of text representing additional information returned from SMARTDEVICELINK
	 */
    public String getInfo() {
        return (String) parameters.get( Names.info );
    }
	/**
	 * <p>
	 * Set a string of text representing additional information returned from SMARTDEVICELINK
	 * </p>
	 * 
	 * @param info
	 *             a string of text representing additional information returned from SMARTDEVICELINK
	 */
    public void setInfo( String info ) {
        if (info != null) {
            parameters.put(Names.info, info );
        }
    }
}
