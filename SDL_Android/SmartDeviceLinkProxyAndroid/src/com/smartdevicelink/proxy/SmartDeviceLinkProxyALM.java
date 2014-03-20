package com.smartdevicelink.proxy;

import java.util.Vector;

import android.app.Service;

import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.exception.SmartDeviceLinkExceptionCause;
import com.smartdevicelink.proxy.Version;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.rpc.ButtonCapabilities;
import com.smartdevicelink.proxy.rpc.DisplayCapabilities;
import com.smartdevicelink.proxy.rpc.PresetBankCapabilities;
import com.smartdevicelink.proxy.rpc.SoftButtonCapabilities;
import com.smartdevicelink.proxy.rpc.smartdevicelinkMsgVersion;
import com.smartdevicelink.proxy.rpc.TTSChunk;
import com.smartdevicelink.proxy.rpc.VehicleType;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.HmiZoneCapabilities;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.enums.SmartDeviceLinkDisconnectedReason;
import com.smartdevicelink.proxy.rpc.enums.VrCapabilities;
import com.smartdevicelink.trace.SmartDeviceLinkTrace;
import com.smartdevicelink.transport.BTTransportConfig;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.TransportType;

public class SmartDeviceLinkProxyALM extends SmartDeviceLinkProxyBase<IProxyListenerALM> {
	
	private static final String SMARTDEVICELINK_LIB_TRACE_KEY = "42baba60-eb57-11df-98cf-0800200c9a66";
	private static final String SMARTDEVICELINK_LIB_PRIVATE_TOKEN = "{DAE1A88C-6C16-4768-ACA5-6F1247EA01C2}";
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK
	 * 
	 * Takes advantage of the advanced lifecycle management.
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName - Name of the application displayed on SMARTDEVICELINK. 
	 * @param isMediaApp - Indicates if the app is a media application.
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, Boolean isMediaApp, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				/*ngn media app*/null,
				/*vr synonyms*/null,
				/*is media app*/isMediaApp,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ false,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, appName, and isMediaApp.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName - Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName - Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms - A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp - Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion - Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired - Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID - ID used to re-register previously registered application.
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, String ngnMediaScreenAppName, 
			Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID, 
			String autoActivateID) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				/*callbackToUIThread*/ false,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, appName, ngnMediaScreenAppName, " +
				"vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, and autoActivateID.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName - Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName - Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms - A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp - Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion - Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired - Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID - ID used to re-register previously registered application.
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, 
			Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, 
			Language hmiDisplayLanguageDesired, String appID, String autoActivateID) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				/*callbackToUIThread*/ false,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, and autoActivateID.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName - Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName - Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms - A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp - Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion - Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired - Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID - ID used to re-register previously registered application.
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, String ngnMediaScreenAppName, 
			Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID, 
			String autoActivateID, boolean callbackToUIThread) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"and callbackToUIThread", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener - Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName - Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName - Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms - A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp - Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion - Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired - Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID - ID used to re-register previously registered application.
	 * @param callbackToUIThread - If true, all callbacks will occur on the UI thread.
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, 
			boolean callbackToUIThread) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"and callbackToUIThread", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/********************************************** TRANSPORT SWITCHING SUPPORT *****************************************/

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management.
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName Name of the application displayed on SMARTDEVICELINK. 
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param transportConfig Initial configuration for transport.
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, Boolean isMediaApp, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID,
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				/*ngn media app*/null,
				/*vr synonyms*/null,
				/*is media app*/isMediaApp,
				/*smartDeviceLinkMsgVersion*/null,
				/*language desired*/languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				/*autoActivateID*/null,
				/*callbackToUIThread*/ false,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, appName, and isMediaApp.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, String ngnMediaScreenAppName, 
			Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID, 
			String autoActivateID, TransportType transportType, BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				/*callbackToUIThread*/ false,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, appName, ngnMediaScreenAppName, " +
				"vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, and autoActivateID.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, 
			Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, 
			Language hmiDisplayLanguageDesired, String appID, String autoActivateID,
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources, 
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				/*callbackToUIThread*/ false,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, and autoActivateID.", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters. 
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands to 
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, String ngnMediaScreenAppName, 
			Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, String appID, 
			String autoActivateID, boolean callbackToUIThread, 
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLink proxy configuration resources*/null,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"and callbackToUIThread", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK. 
	 * @param appName Name of the application displayed on SMARTDEVICELINK. 
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle. 
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, 
			boolean callbackToUIThread, BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"and callbackToUIThread", SMARTDEVICELINK_LIB_TRACE_KEY);
	}

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister,
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*TTS Name*/null,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appID Identifier of the client application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @throws SmartDeviceLinkException
	 */	
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, Boolean isMediaApp,Language languageDesired, Language hmiDisplayLanguageDesired,
						String appID, boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException 
	{
		super(	listener, 
			/*smartDeviceLinkProxyConfigurationResources*/null,
			/*enable advanced lifecycle management*/true, 
			appName,
			/*ttsName*/null,
			/*ngnMediaScreenAppName*/null,
			/*vrSynonyms*/null,
			isMediaApp,
			/*smartDeviceLinkMsgVersion*/null,
			languageDesired,
			hmiDisplayLanguageDesired,
			/*App Type*/null,
			/*App ID*/appID,
			/*autoActivateID*/null,
			callbackToUIThread,
			preRegister,
			new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, " +
			"appName, isMediaApp, languageDesired, hmiDisplayLanguageDesired" + "callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param appID Identifier of the client application.
	 * @throws SmartDeviceLinkException
	 */		
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, Boolean isMediaApp,String appID) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLinkProxyConfigurationResources*/null,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*ttsName*/null,
				/*ngnMediaScreenAppName*/null,
				/*vrSynonyms*/null,
				isMediaApp,
				/*smartDeviceLinkMsgVersion*/null,
				/*languageDesired*/null,
				/*hmiDisplayLanguageDesired*/null,
				/*App Type*/null,
				/*App ID*/appID,
				/*autoActivateID*/null,
				false,
				false,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, " +
				"appName, isMediaApp, appID", SMARTDEVICELINK_LIB_TRACE_KEY);
	}

	
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param appID Identifier of the client application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @throws SmartDeviceLinkException
	 */		
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, String appName, Boolean isMediaApp,String appID, 
						 boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException {
		super(	listener, 
				/*smartDeviceLinkProxyConfigurationResources*/null,
				/*enable advanced lifecycle management*/true, 
				appName,
				/*ttsName*/null,
				/*ngnMediaScreenAppName*/null,
				/*vrSynonyms*/null,
				isMediaApp,
				/*smartDeviceLinkMsgVersion*/null,
				/*languageDesired*/null,
				/*hmiDisplayLanguageDesired*/null,
				/*App Type*/null,
				/*App ID*/appID,
				/*autoActivateID*/null,
				callbackToUIThread,
				preRegister,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, " +
				"appName, isMediaApp, " + "callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}

	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * @param appService Reference to the apps service object. 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @throws SmartDeviceLinkException
	 */	
	public SmartDeviceLinkProxyALM(Service appService, IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, Vector<TTSChunk> ttsName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				ttsName,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				new BTTransportConfig());
		
				this.setAppService(appService);
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @throws SmartDeviceLinkException
	 */	
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, Vector<TTSChunk> ttsName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				ttsName,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}

		
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, Vector<TTSChunk> ttsName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister,
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				ttsName,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/null,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}		
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI. 
	 * @param appType Type of application. 
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @throws SmartDeviceLinkException
	 */	
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, Vector<TTSChunk> ttsName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, 
			Vector<AppHMIType> appType, String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				ttsName,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/appType,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				new BTTransportConfig());
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using legacy constructor for BT transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, appType, appID, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}	
	
	/**
	 * Constructor for the SmartDeviceLinkProxy object, the proxy for communicating between the App and SMARTDEVICELINK via specified transport.
	 * 
	 * Takes advantage of the advanced lifecycle management. 
	 * 
	 * @param listener Reference to the object in the App listening to callbacks from SMARTDEVICELINK.
	 * @param smartDeviceLinkProxyConfigurationResources Proxy configuration resources.
	 * @param appName Name of the application displayed on SMARTDEVICELINK.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Name of the application displayed on SMARTDEVICELINK for Navigation equipped 
	 * vehicles. Limited to five characters.
	 * @param vrSynonyms A vector of strings, all of which can be used as voice commands too
	 * @param isMediaApp Indicates if the app is a media application.
	 * @param smartDeviceLinkMsgVersion Indicates the version of SMARTDEVICELINK SmartDeviceLink Messages desired. Must be less than
	 * or equal to the version of SMARTDEVICELINK SmartDeviceLink running on the vehicle.
	 * @param languageDesired Indicates the language desired for the SMARTDEVICELINK interface.
	 * @param hmiDisplayLanguageDesired Desired language in HMI.
	 * @param appType Type of application.
	 * @param appID Identifier of the client application.
	 * @param autoActivateID ID used to re-register previously registered application.
	 * @param callbackToUIThread If true, all callbacks will occur on the UI thread.
	 * @param preRegister Flag that indicates that client should be pre-registred or not
	 * @param transportConfig Initial configuration for transport. 
	 * @throws SmartDeviceLinkException
	 */
	public SmartDeviceLinkProxyALM(IProxyListenerALM listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			String appName, Vector<TTSChunk> ttsName, String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, Language languageDesired, Language hmiDisplayLanguageDesired, Vector<AppHMIType> appType, 
			String appID, String autoActivateID, boolean callbackToUIThread, boolean preRegister,
			BaseTransportConfig transportConfig) throws SmartDeviceLinkException {
		super(	listener, 
				smartDeviceLinkProxyConfigurationResources,
				/*enable advanced lifecycle management*/true, 
				appName,
				ttsName,
				ngnMediaScreenAppName,
				vrSynonyms,
				isMediaApp,
				smartDeviceLinkMsgVersion,
				languageDesired,
				/*HMI Display Language Desired*/hmiDisplayLanguageDesired,
				/*App Type*/appType,
				/*App ID*/appID,
				autoActivateID,
				callbackToUIThread,
				preRegister,
				transportConfig);
		
		SmartDeviceLinkTrace.logProxyEvent("Application constructed SmartDeviceLinkProxyALM (using new constructor with specified transport) instance passing in: IProxyListener, smartDeviceLinkProxyConfigurationResources, " +
				"appName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, smartDeviceLinkMsgVersion, languageDesired, appType, appID, autoActivateID, " +
				"callbackToUIThread and version", SMARTDEVICELINK_LIB_TRACE_KEY);
	}	
	/***************************************** END OF TRANSPORT SWITCHING SUPPORT ***************************************/
	
	// Allow applications using ALM to reset the proxy (dispose and reinstantiate)
	/**
	 * Disconnects the application from SMARTDEVICELINK, then recreates the transport such that
	 * the next time a SMARTDEVICELINK unit discovers applications, this application will be
	 * available.
	 */
	public void resetProxy() throws SmartDeviceLinkException {
		super.cycleProxy(SmartDeviceLinkDisconnectedReason.APPLICATION_REQUESTED_DISCONNECT);
	}
	
	/********* Getters for values returned by RegisterAppInterfaceResponse **********/
	
	/**
	 * Gets buttonCapabilities set when application interface is registered.
	 * 
	 * @return buttonCapabilities
	 * @throws SmartDeviceLinkException
	 */
	public Vector<ButtonCapabilities> getButtonCapabilities() throws SmartDeviceLinkException{
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the buttonCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _buttonCapabilities;
	}
	
	/**
	 * Gets getSoftButtonCapabilities set when application interface is registered.
	 * 
	 * @return softButtonCapabilities 
	 * @throws SmartDeviceLinkException
	 */
	public Vector<SoftButtonCapabilities> getSoftButtonCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is not connected. Unable to get the softButtonCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _softButtonCapabilities;
	}
	
	/**
	 * Gets getPresetBankCapabilities set when application interface is registered.
	 * 
	 * @return presetBankCapabilities 
	 * @throws SmartDeviceLinkException
	 */
	public PresetBankCapabilities getPresetBankCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is not connected. Unable to get the presetBankCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _presetBankCapabilities;
	}
	
	/**
	 * Gets the current version information of the proxy.
	 * 
	 * @return String
	 * @throws SmartDeviceLinkException
	 */
	public String getProxyVersionInfo() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}		

		if (Version.VERSION != null)
				return  Version.VERSION;
		
		return null;
	}

	
	
	/**
	 * Gets displayCapabilities set when application interface is registered.
	 * 
	 * @return displayCapabilities
	 * @throws SmartDeviceLinkException
	 */
	public DisplayCapabilities getDisplayCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the displayCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _displayCapabilities;
	}
	
	/**
	 * Gets hmiZoneCapabilities set when application interface is registered.
	 * 
	 * @return hmiZoneCapabilities
	 * @throws SmartDeviceLinkException
	 */
	public Vector<HmiZoneCapabilities> getHmiZoneCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the hmiZoneCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _hmiZoneCapabilities;
	}
	
	/**
	 * Gets speechCapabilities set when application interface is registered.
	 * 
	 * @return speechCapabilities
	 * @throws SmartDeviceLinkException
	 */
	public Vector<SpeechCapabilities> getSpeechCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the speechCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		
		return _speechCapabilities;
	}
	
	/**
	 * Gets smartDeviceLinkLanguage set when application interface is registered.
	 * 
	 * @return smartDeviceLinkLanguage
	 * @throws SmartDeviceLinkException
	 */
	public Language getSmartDeviceLinkLanguage() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the smartDeviceLinkLanguage.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _smartDeviceLinkLanguage;
	}
	
	/**
	 * Gets getHmiDisplayLanguage set when application interface is registered.
	 * 
	 * @return hmiDisplayLanguage 
	 * @throws SmartDeviceLinkException
	 */
	public Language getHmiDisplayLanguage() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is not connected. Unable to get the hmiDisplayLanguage.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _hmiDisplayLanguage;
	}
	
	/**
	 * Gets smartDeviceLinkMsgVersion set when application interface is registered.
	 * 
	 * @return smartDeviceLinkMsgVersion
	 * @throws SmartDeviceLinkException
	 */
	public smartdevicelinkMsgVersion getsmartdevicelinkMsgVersion() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the smartDeviceLinkMsgVersion.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _smartDeviceLinkMsgVersion;
	}
	
	/**
	 * Gets vrCapabilities set when application interface is registered.
	 * 
	 * @return vrCapabilities
	 * @throws SmartDeviceLinkException
	 */
	public Vector<VrCapabilities> getVrCapabilities() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is unavailable. Unable to get the vrCapabilities.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _vrCapabilities;
	}
	
	/**
	 * Gets getVehicleType set when application interface is registered.
	 * 
	 * @return vehicleType 
	 * @throws SmartDeviceLinkException
	 */
	public VehicleType getVehicleType() throws SmartDeviceLinkException {
		// Test if proxy has been disposed
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test SMARTDEVICELINK availability 
		if (!_appInterfaceRegisterd) {
			throw new SmartDeviceLinkException("SMARTDEVICELINK is not connected. Unable to get the vehicleType.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
		return _vehicleType;
	}
}
