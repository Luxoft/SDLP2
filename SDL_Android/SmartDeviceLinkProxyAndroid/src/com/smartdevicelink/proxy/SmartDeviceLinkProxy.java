package com.smartdevicelink.proxy;

import java.util.Vector;

import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.exception.SmartDeviceLinkExceptionCause;
import com.smartdevicelink.proxy.rpc.smartdevicelinkMsgVersion;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.trace.SmartDeviceLinkTrace;
import com.smartdevicelink.transport.BTTransportConfig;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.TransportType;
@Deprecated
public class SmartDeviceLinkProxy extends SmartDeviceLinkProxyBase<IProxyListener> {
	
	private static final String SMARTDEVICELINK_LIB_TRACE_KEY = "42baba60-eb57-11df-98cf-0800200c9a66";
	private static final String SMARTDEVICELINK_LIB_PRIVATE_TOKEN = "{DAE1A88C-6C16-4768-ACA5-6F1247EA01C2}";

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener) throws SmartDeviceLinkException {
		super(	listener, 
				/*application context*/null, 
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ true,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param smartDeviceLinkProxyConfigurationResources
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources) 
		throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources, 
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ true,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, SmartDeviceLinkProxyConfigurationResources.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, boolean callbackToUIThread) throws SmartDeviceLinkException {
		super(	listener,  
				/*smartDeviceLink proxy configuration resources*/null,
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				callbackToUIThread,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, callBackToUIThread.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources 
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			boolean callbackToUIThread) throws SmartDeviceLinkException {
		super(	listener,  
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				callbackToUIThread,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, callBackToUIThread.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/********************************************** TRANSPORT SWITCHING SUPPORT *****************************************/

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param transportConfig Initial configuration for transport.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				/*application context*/null, 
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ true,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param smartDeviceLinkProxyConfigurationResources
	 * @param transportConfig Initial configuration for transport.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
					BaseTransportConfig transportConfig) 
		throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources, 
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ true,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, SmartDeviceLinkProxyConfigurationResources.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @param transportConfig Initial configuration for transport.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, boolean callbackToUIThread, BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener,  
				/*smartDeviceLink proxy configuration resources*/null,
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				callbackToUIThread,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, callBackToUIThread.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources 
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @param transportConfig Initial configuration for transport.
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public SmartDeviceLinkProxy(IProxyListener listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			boolean callbackToUIThread, BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener,  
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/false, 
				/*app name*/ null,
				/*TTS Name*/null,
				/*ngn media screen app name*/null,
				/*vr synonyms*/null,
				/*is media app*/ null,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/null,
				/*HMI Display Language Desired*/null,
				/*App Type*/null,
				/*App ID*/null,
				/*autoActivateID*/null,
				callbackToUIThread,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxy instance passing in: IProxyListener, callBackToUIThread.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
		
	/******************** Public Helper Methods *************************/
	
	
	/**
	 *  Sends a RegisterAppInterface RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *  
	 *  @param smartDeviceLinkMsgVersion
	 *  @param appName
	 *  @param ngnMediaScreenAppName
	 *  @param vrSynonyms
	 *  @param isMediaApp
	 *  @param languageDesired
	 *  @param autoActivateID
	 *  @param correlationID
	 *  
	 *  @throws SmartDeviceLinkException
	 */
	@Deprecated
	public void registerAppInterface(
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, String appName, String ngnMediaScreenAppName,
			Vector<String> vrSynonyms, Boolean isMediaApp, Language languageDesired, Language hmiDisplayLanguageDesired,
			String appID, String autoActivateID, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This SmartDeviceLinkProxy object has been disposed, it is no long capable of sending requests.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		registerAppInterfacePrivate(
				smartDeviceLinkMsgVersion, 
				appName,
				null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp, 
				languageDesired,
				hmiDisplayLanguageDesired,
				null,
				appID,
				autoActivateID,
				correlationID);
	}
	
	/**
	 * Sends a RegisterAppInterface RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param appName
	 * @param isMediaApp
	 * @param autoActivateID
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public void registerAppInterface(
			String appName, Boolean isMediaApp, String appID, String autoActivateID, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		registerAppInterface(
				/*smartDeviceLinkMsgVersion*/null, 
				appName,
				/*ngnMediaScreenAppName*/null,
				/*vrSynonyms*/null,
				isMediaApp, 
				/*languageDesired*/null,
				/*hmiDisplayLanguageDesired*/null,
				appID,
				autoActivateID,
				correlationID);
	}
	
	/**
	 * Sends a RegisterAppInterface RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param appName
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public void registerAppInterface(String appName, String appID, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		registerAppInterface(appName, false, appID, "", correlationID);
	}
	
	/**
	 * Sends an UnregisterAppInterface RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	@Deprecated
	public void unregisterAppInterface(Integer correlationID) 
			throws SmartDeviceLinkException {		
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This SmartDeviceLinkProxy object has been disposed, it is no long capable of executing methods.", 
										SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}		
				
		unregisterAppInterfacePrivate(correlationID);
	}
	
	/**
	 * Returns is isConnected state of the SMARTDEVICELINK transport.
	 * 
	 * @return Boolean isConnected
	 */
	@Deprecated
	public Boolean getIsConnected() {
		return super.getIsConnected();
	}

}
