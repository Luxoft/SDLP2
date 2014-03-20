package com.smartdevicelink.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.exception.SmartDeviceLinkExceptionCause;
import com.smartdevicelink.marshal.JsonRPCMarshaller;
import com.smartdevicelink.messageDispatcher.IDispatchingStrategy;
import com.smartdevicelink.messageDispatcher.IncomingProtocolMessageComparitor;
import com.smartdevicelink.messageDispatcher.InternalProxyMessageComparitor;
import com.smartdevicelink.messageDispatcher.OutgoingProtocolMessageComparitor;
import com.smartdevicelink.messageDispatcher.ProxyMessageDispatcher;
import com.smartdevicelink.protocol.ProtocolMessage;
import com.smartdevicelink.protocol.SmartDeviceLinkProtocol;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.protocol.enums.MessageType;
import com.smartdevicelink.protocol.enums.SessionType;
import com.smartdevicelink.proxy.callbacks.InternalProxyMessage;
import com.smartdevicelink.proxy.callbacks.OnError;
import com.smartdevicelink.proxy.callbacks.OnProxyClosed;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.interfaces.IProxyListenerBase;
import com.smartdevicelink.proxy.interfaces.IProxyListenerProfileManager;
import com.smartdevicelink.proxy.rpc.*;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.AudioStreamingState;
import com.smartdevicelink.proxy.rpc.enums.AudioType;
import com.smartdevicelink.proxy.rpc.enums.BitsPerSample;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.GlobalProperty;
import com.smartdevicelink.proxy.rpc.enums.HMILevel;
import com.smartdevicelink.proxy.rpc.enums.HmiZoneCapabilities;
import com.smartdevicelink.proxy.rpc.enums.ImageType;
import com.smartdevicelink.proxy.rpc.enums.InteractionMode;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.MobileAppState;
import com.smartdevicelink.proxy.rpc.enums.SamplingRate;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.enums.SmartDeviceLinkConnectionState;
import com.smartdevicelink.proxy.rpc.enums.SmartDeviceLinkDisconnectedReason;
import com.smartdevicelink.proxy.rpc.enums.SmartDeviceLinkInterfaceAvailability;
import com.smartdevicelink.proxy.rpc.enums.SystemContext;
import com.smartdevicelink.proxy.rpc.enums.TextAlignment;
import com.smartdevicelink.proxy.rpc.enums.UpdateMode;
import com.smartdevicelink.proxy.rpc.enums.VrCapabilities;
import com.smartdevicelink.proxy.rpc.pm.AddProfile;
import com.smartdevicelink.proxy.rpc.pm.AddProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.AppStateChanged;
import com.smartdevicelink.proxy.rpc.pm.AppStateChangedResponse;
import com.smartdevicelink.proxy.rpc.pm.LoadProfile;
import com.smartdevicelink.proxy.rpc.pm.LoadProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.OnProfileClosed;
import com.smartdevicelink.proxy.rpc.pm.OnSendProfileToAppMessage;
import com.smartdevicelink.proxy.rpc.pm.ProfileBinaryPacketizer;
import com.smartdevicelink.proxy.rpc.pm.RemoveProfile;
import com.smartdevicelink.proxy.rpc.pm.RemoveProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.SendAppToProfileMessage;
import com.smartdevicelink.proxy.rpc.pm.SendAppToProfileMessageResponse;
import com.smartdevicelink.proxy.rpc.pm.UnloadProfile;
import com.smartdevicelink.proxy.rpc.pm.UnloadProfileResponse;
import com.smartdevicelink.smartDeviceLinkConnection.IsmartDeviceLinkConnectionListener;
import com.smartdevicelink.smartDeviceLinkConnection.smartDeviceLinkConnection;
import com.smartdevicelink.trace.SmartDeviceLinkTrace;
import com.smartdevicelink.trace.TraceDeviceInfo;
import com.smartdevicelink.trace.enums.InterfaceActivityDirection;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.SiphonServer;
import com.smartdevicelink.transport.TransportType;
import com.smartdevicelink.util.DebugTool;
import com.smartdevicelink.util.MainThreadInvocationHandler;

public abstract class SmartDeviceLinkProxyBase<proxyListenerType extends IProxyListenerBase> {
	// Used for calls to Android Log class.
	public static final String TAG = "SmartDeviceLinkProxy";
	private static final String SMARTDEVICELINK_LIB_TRACE_KEY = "42baba60-eb57-11df-98cf-0800200c9a66";
	private static final int PROX_PROT_VER_ONE = 1;
	
	private smartDeviceLinkConnection _smartDeviceLinkConnection;
	private proxyListenerType _proxyListener = null;
	
	protected Service _appService = null;
	
	// Protected Correlation IDs
	private final int 	REGISTER_APP_INTERFACE_CORRELATION_ID = 65529,
						UNREGISTER_APP_INTERFACE_CORRELATION_ID = 65530,
						POLICIES_CORRELATION_ID = 65535;
	
	// SmartDeviceLinkhronization Objects
	private static final Object CONNECTION_REFERENCE_LOCK = new Object(),
								INCOMING_MESSAGE_QUEUE_THREAD_LOCK = new Object(),
								OUTGOING_MESSAGE_QUEUE_THREAD_LOCK = new Object(),
								INTERNAL_MESSAGE_QUEUE_THREAD_LOCK = new Object(),
								APP_INTERFACE_REGISTERED_LOCK = new Object();
		
	// RPC Session ID
	private byte _rpcSessionID = 0;
	
	private int iFileCount = 0;

	// Device Info for logging
	private TraceDeviceInfo _traceDeviceInterrogator = null;
		
	// Declare Queuing Threads
	private ProxyMessageDispatcher<ProtocolMessage> _incomingProxyMessageDispatcher;
	private ProxyMessageDispatcher<ProtocolMessage> _outgoingProxyMessageDispatcher;
	private ProxyMessageDispatcher<InternalProxyMessage> _internalProxyMessageDispatcher;
	
	// Flag indicating if callbacks should be called from UIThread
	private Boolean _callbackToUIThread = false;
	// UI Handler
	private Handler _mainUIHandler = null; 
	
	// SmartDeviceLinkProxy Advanced Lifecycle Management
	protected Boolean _advancedLifecycleManagementEnabled = false;
	// Parameters passed to the constructor from the app to register an app interface
	private String _applicationName = null;
	private Vector<TTSChunk> _ttsName = null;
	private String _ngnMediaScreenAppName = null;
	private Boolean _isMediaApp = null;
	private Language _smartDeviceLinkLanguageDesired = null;
	private Language _hmiDisplayLanguageDesired = null;
	private Vector<AppHMIType> _appType = null;
	private String _appID = null;
	private String _autoActivateIdDesired = null;
	private smartdevicelinkMsgVersion _smartDeviceLinkMsgVersionRequest = null;
	private Vector<String> _vrSynonyms = null;
	
	/**
	 * Contains current configuration for the transport that was selected during 
	 * construction of this object
	 */
	private BaseTransportConfig _transportConfig = null;
	// Proxy State Variables
	protected Boolean _appInterfaceRegisterd = false;
	private Boolean _haveReceivedFirstNonNoneHMILevel = false;
	protected Boolean _haveReceivedFirstFocusLevel = false;
	protected Boolean _haveReceivedFirstFocusLevelFull = false;
	protected Boolean _proxyDisposed = false;
	protected SmartDeviceLinkConnectionState _smartDeviceLinkConnectionState = null;
	protected SmartDeviceLinkInterfaceAvailability _smartDeviceLinkIntefaceAvailablity = null;
	protected HMILevel _hmiLevel = null;
	private HMILevel _priorHmiLevel = null;
	protected AudioStreamingState _audioStreamingState = null;
	private AudioStreamingState _priorAudioStreamingState = null;
	protected SystemContext _systemContext = null;
	// Variables set by RegisterAppInterfaceResponse
	protected smartdevicelinkMsgVersion _smartDeviceLinkMsgVersion = null;
	protected String _autoActivateIdReturned = null;
	protected Language _smartDeviceLinkLanguage = null;
	protected Language _hmiDisplayLanguage = null;
	protected DisplayCapabilities _displayCapabilities = null;
	protected Vector<ButtonCapabilities> _buttonCapabilities = null;
	protected Vector<SoftButtonCapabilities> _softButtonCapabilities = null;
	protected PresetBankCapabilities _presetBankCapabilities = null;
	protected Vector<HmiZoneCapabilities> _hmiZoneCapabilities = null;
	protected Vector<SpeechCapabilities> _speechCapabilities = null;
	protected Vector<VrCapabilities> _vrCapabilities = null;
	protected VehicleType _vehicleType = null;
	protected Boolean firstTimeFull = true;
	protected String _proxyVersionInfo = null;
	
	protected byte _sdlproVersion = 1;

    private IProxyListenerProfileManager mProfileManagerCallbacksProxy;
	
	// Interface broker
	private SmartDeviceLinkInterfaceBroker _interfaceBroker = null;
	
	// Private Class to Interface with smartDeviceLinkConnection
	private class SmartDeviceLinkInterfaceBroker implements IsmartDeviceLinkConnectionListener {
		
		@Override
		public void onTransportDisconnected(String info) {
			// proxyOnTransportDisconnect is called to alert the proxy that a requested
			// disconnect has completed
			
			if (_advancedLifecycleManagementEnabled) {
				// If ALM, nothing is required to be done here
			} else {
				// If original model, notify app the proxy is closed so it will delete and reinstanciate 
				notifyProxyClosed(info, new SmartDeviceLinkException("Transport disconnected.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE));
			}
		}

		@Override
		public void onTransportError(String info, Exception e) {
			DebugTool.logError("Transport failure: " + info, e);
			
			if (_advancedLifecycleManagementEnabled) {			
				// Cycle the proxy
				cycleProxy(SmartDeviceLinkDisconnectedReason.TRANSPORT_ERROR);
			} else {
				notifyProxyClosed(info, e);
			}
		}

		@Override
		public void onProtocolMessageReceived(ProtocolMessage msg) {
		    Log.i(TAG, "onProtocolMessageReceived : " + new String(msg.getData()));
			
			try {if (msg.getData().length > 0) queueIncomingMessage(msg);}
			catch (Exception e) {}
			
			//TODO iviLink team: Not sure how to fix double incoming messages better
			/*try {if (msg.getBulkData().length > 0) queueIncomingMessage(msg);}
			catch (Exception e) {}*/
		}

		@Override
		public void onProtocolSessionStarted(SessionType sessionType,
				byte sessionID, byte version, String correlationID) {
			if (_sdlproVersion == 1) {
				if (version == 2) setsdlproVersion(version);
			}
			if (sessionType.eq(SessionType.RPC)) {			
				startRPCProtocolSession(sessionID, correlationID);
			} else if (_sdlproVersion == 2) {
				//If version 2 then don't need to specify a Session Type
				startRPCProtocolSession(sessionID, correlationID);
			}  else {
				// Handle other protocol session types here
			}
		}

		@Override
		public void onProtocolSessionEnded(SessionType sessionType,
				byte sessionID, String correlationID) {
			// How to handle protocol session ended?
				// How should protocol session management occur? 
		}

		@Override
		public void onProtocolError(String info, Exception e) {
			passErrorToProxyListener(info, e);
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param listener Type of listener for this proxy base.
	 * @param smartDeviceLinkProxyConfigurationResources Configuration resources for this proxy.
	 * @param enableAdvancedLifecycleManagement Flag that ALM should be enabled or not.
	 * @param appName Client application name.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Media Screen Application name.
	 * @param vrSynonyms List of synonyms.
	 * @param isMediaApp Flag that indicates that client application if media application or not.
	 * @param smartDeviceLinkMsgVersion Version of SmartDeviceLink Message.
	 * @param languageDesired Desired language.
	 * @param hmiDisplayLanguageDesired Desired language for HMI. 
	 * @param appType Type of application.
	 * @param appID Application identifier.
	 * @param autoActivateID Auto activation identifier.
	 * @param callbackToUIThread Flag that indicates that this proxy should send callback to UI thread or not.
	 * @param transportConfig Configuration of transport to be used by underlying connection.
	 * @throws SmartDeviceLinkException
	 */
	protected SmartDeviceLinkProxyBase(proxyListenerType listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			boolean enableAdvancedLifecycleManagement, String appName, Vector<TTSChunk> ttsName, 
			String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, Vector<AppHMIType> appType, String appID, 
			String autoActivateID, boolean callbackToUIThread, BaseTransportConfig transportConfig) 
			throws SmartDeviceLinkException {
		
		setsdlproVersion((byte)PROX_PROT_VER_ONE);
		
		_interfaceBroker = new SmartDeviceLinkInterfaceBroker();
		
		_callbackToUIThread = callbackToUIThread;
		
		if (_callbackToUIThread) {
			_mainUIHandler = new Handler(Looper.getMainLooper());
		}
		
		mProfileManagerCallbacksProxy = (IProxyListenerProfileManager) Proxy.newProxyInstance(getClass().getClassLoader(), 
		        new Class[] {IProxyListenerProfileManager.class}, new MainThreadInvocationHandler(listener, callbackToUIThread));
		
		// Set variables for Advanced Lifecycle Management
		_advancedLifecycleManagementEnabled = enableAdvancedLifecycleManagement;
		_applicationName = appName;
		_ttsName = ttsName;
		_ngnMediaScreenAppName = ngnMediaScreenAppName;
		_isMediaApp = isMediaApp;
		_smartDeviceLinkMsgVersionRequest = smartDeviceLinkMsgVersion;
		_vrSynonyms = vrSynonyms; 
		_smartDeviceLinkLanguageDesired = languageDesired;
		_hmiDisplayLanguageDesired = hmiDisplayLanguageDesired;
		_appType = appType;
		_appID = appID;
		_autoActivateIdDesired = autoActivateID;
		_transportConfig = transportConfig;
		
		// Test conditions to invalidate the proxy
		if (listener == null) {
			throw new IllegalArgumentException("IProxyListener listener must be provided to instantiate SmartDeviceLinkProxy object.");
		}
		if (_advancedLifecycleManagementEnabled) {
			if (_applicationName == null ) {
				throw new IllegalArgumentException("To use SmartDeviceLinkProxyALM, an application name, appName, must be provided");
			}
			if (_applicationName.length() < 1 || _applicationName.length() > 100) {
				throw new IllegalArgumentException("A provided application name, appName, must be between 1 and 100 characters in length.");
			}
			if (_isMediaApp == null) {
				throw new IllegalArgumentException("isMediaApp must not be null when using SmartDeviceLinkProxyALM.");
			}
		}
		
		_proxyListener = listener;
		
		// Get information from smartDeviceLinkProxyConfigurationResources
		TelephonyManager telephonyManager = null;
		if (smartDeviceLinkProxyConfigurationResources != null) {
			telephonyManager = smartDeviceLinkProxyConfigurationResources.getTelephonyManager();
		} 
		
		// Use the telephonyManager to get and log phone info
		if (telephonyManager != null) {
			// Following is not quite thread-safe (because m_traceLogger could test null twice),
			// so we need to fix this, but vulnerability (i.e. two instances of listener) is
			// likely harmless.
			if (_traceDeviceInterrogator == null) {
				_traceDeviceInterrogator = new TraceDeviceInfo(smartDeviceLinkProxyConfigurationResources.getTelephonyManager());
			} // end-if
		} // end-if
		
		// Setup Internal ProxyMessage Dispatcher
		synchronized(INTERNAL_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure internalProxyMessageDispatcher is null
			if (_internalProxyMessageDispatcher != null) {
				_internalProxyMessageDispatcher.dispose();
				_internalProxyMessageDispatcher = null;
			}
			
			_internalProxyMessageDispatcher = new ProxyMessageDispatcher<InternalProxyMessage>("INTERNAL_MESSAGE_DISPATCHER",
					new InternalProxyMessageComparitor(),
					new IDispatchingStrategy<InternalProxyMessage>() {

						@Override
						public void dispatch(InternalProxyMessage message) {
							dispatchInternalMessage((InternalProxyMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromInternalMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromInternalMessageDispatcher(info, ex);
						}			
			});
		}
		
		// Setup Incoming ProxyMessage Dispatcher
		synchronized(INCOMING_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure incomingProxyMessageDispatcher is null
			if (_incomingProxyMessageDispatcher != null) {
				_incomingProxyMessageDispatcher.dispose();
				_incomingProxyMessageDispatcher = null;
			}
			
			_incomingProxyMessageDispatcher = new ProxyMessageDispatcher<ProtocolMessage>("INCOMING_MESSAGE_DISPATCHER",
					new IncomingProtocolMessageComparitor(),
					new IDispatchingStrategy<ProtocolMessage>() {
						@Override
						public void dispatch(ProtocolMessage message) {
							dispatchIncomingMessage((ProtocolMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromIncomingMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromIncomingMessageDispatcher(info, ex);
						}			
			});
		}
		
		// Setup Outgoing ProxyMessage Dispatcher
		synchronized(OUTGOING_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure outgoingProxyMessageDispatcher is null
			if (_outgoingProxyMessageDispatcher != null) {
				_outgoingProxyMessageDispatcher.dispose();
				_outgoingProxyMessageDispatcher = null;
			}
			
			_outgoingProxyMessageDispatcher = new ProxyMessageDispatcher<ProtocolMessage>("OUTGOING_MESSAGE_DISPATCHER",
					new OutgoingProtocolMessageComparitor(),
					new IDispatchingStrategy<ProtocolMessage>() {
						@Override
						public void dispatch(ProtocolMessage message) {
							dispatchOutgoingMessage((ProtocolMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromOutgoingMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromOutgoingMessageDispatcher(info, ex);
						}
			});
		}
		
		// Initialize the proxy
		try {
			initializeProxy();
		} catch (SmartDeviceLinkException e) {
			// Couldn't initialize the proxy 
			// Dispose threads and then rethrow exception
			
			if (_internalProxyMessageDispatcher != null) {
				_internalProxyMessageDispatcher.dispose();
				_internalProxyMessageDispatcher = null;
			}
			if (_incomingProxyMessageDispatcher != null) {
				_incomingProxyMessageDispatcher.dispose();
				_incomingProxyMessageDispatcher = null;
			}
			if (_outgoingProxyMessageDispatcher != null) {
				_outgoingProxyMessageDispatcher.dispose();
				_outgoingProxyMessageDispatcher = null;
			}
			throw e;
		} 
		
		// Trace that ctor has fired
		SmartDeviceLinkTrace.logProxyEvent("SmartDeviceLinkProxy Created, instanceID=" + this.toString(), SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param listener Type of listener for this proxy base.
	 * @param smartDeviceLinkProxyConfigurationResources Configuration resources for this proxy.
	 * @param enableAdvancedLifecycleManagement Flag that ALM should be enabled or not.
	 * @param appName Client application name.
	 * @param ttsName TTS name.
	 * @param ngnMediaScreenAppName Media Screen Application name.
	 * @param vrSynonyms List of synonyms.
	 * @param isMediaApp Flag that indicates that client application if media application or not.
	 * @param smartDeviceLinkMsgVersion Version of SmartDeviceLink Message.
	 * @param languageDesired Desired language.
	 * @param hmiDisplayLanguageDesired Desired language for HMI. 
	 * @param appType Type of application.
	 * @param appID Application identifier.
	 * @param autoActivateID Auto activation identifier.
	 * @param callbackToUIThread Flag that indicates that this proxy should send callback to UI thread or not.
	 * @param preRegister Flag that indicates that this proxy should be pre-registerd or not.
	 * @param transportConfig Configuration of transport to be used by underlying connection.
	 * @throws SmartDeviceLinkException
	 */	
	protected SmartDeviceLinkProxyBase(proxyListenerType listener, SmartDeviceLinkProxyConfigurationResources smartDeviceLinkProxyConfigurationResources, 
			boolean enableAdvancedLifecycleManagement, String appName, Vector<TTSChunk> ttsName, 
			String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, 
			Language languageDesired, Language hmiDisplayLanguageDesired, Vector<AppHMIType> appType, String appID, 
			String autoActivateID, boolean callbackToUIThread, boolean preRegister,
			BaseTransportConfig transportConfig) 
			throws SmartDeviceLinkException {
		
		setsdlproVersion((byte)PROX_PROT_VER_ONE);
		
		if (preRegister) _appInterfaceRegisterd = preRegister;
		
		_interfaceBroker = new SmartDeviceLinkInterfaceBroker();
		
		_callbackToUIThread = callbackToUIThread;
		
		if (_callbackToUIThread) {
			_mainUIHandler = new Handler(Looper.getMainLooper());
		}
		
	    mProfileManagerCallbacksProxy = (IProxyListenerProfileManager) Proxy.newProxyInstance(getClass().getClassLoader(), 
	                new Class[] {IProxyListenerProfileManager.class}, new MainThreadInvocationHandler(listener, callbackToUIThread));
		
		// Set variables for Advanced Lifecycle Management
		_advancedLifecycleManagementEnabled = enableAdvancedLifecycleManagement;
		_applicationName = appName;
		_ttsName = ttsName;
		_ngnMediaScreenAppName = ngnMediaScreenAppName;
		_isMediaApp = isMediaApp;
		_smartDeviceLinkMsgVersionRequest = smartDeviceLinkMsgVersion;
		_vrSynonyms = vrSynonyms; 
		_smartDeviceLinkLanguageDesired = languageDesired;
		_hmiDisplayLanguageDesired = hmiDisplayLanguageDesired;
		_appType = appType;
		_appID = appID;
		_autoActivateIdDesired = autoActivateID;
		_transportConfig = transportConfig;
		
		// Test conditions to invalidate the proxy
		if (listener == null) {
			throw new IllegalArgumentException("IProxyListener listener must be provided to instantiate SmartDeviceLinkProxy object.");
		}
		if (_advancedLifecycleManagementEnabled) {
			if (_applicationName == null ) {
				throw new IllegalArgumentException("To use SmartDeviceLinkProxyALM, an application name, appName, must be provided");
			}
			if (_applicationName.length() < 1 || _applicationName.length() > 100) {
				throw new IllegalArgumentException("A provided application name, appName, must be between 1 and 100 characters in length.");
			}
			if (_isMediaApp == null) {
				throw new IllegalArgumentException("isMediaApp must not be null when using SmartDeviceLinkProxyALM.");
			}
		}
		
		_proxyListener = listener;
		
		// Get information from smartDeviceLinkProxyConfigurationResources
		TelephonyManager telephonyManager = null;
		if (smartDeviceLinkProxyConfigurationResources != null) {
			telephonyManager = smartDeviceLinkProxyConfigurationResources.getTelephonyManager();
		} 
		
		// Use the telephonyManager to get and log phone info
		if (telephonyManager != null) {
			// Following is not quite thread-safe (because m_traceLogger could test null twice),
			// so we need to fix this, but vulnerability (i.e. two instances of listener) is
			// likely harmless.
			if (_traceDeviceInterrogator == null) {
				_traceDeviceInterrogator = new TraceDeviceInfo(smartDeviceLinkProxyConfigurationResources.getTelephonyManager());
			} // end-if
		} // end-if
		
		// Setup Internal ProxyMessage Dispatcher
		synchronized(INTERNAL_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure internalProxyMessageDispatcher is null
			if (_internalProxyMessageDispatcher != null) {
				_internalProxyMessageDispatcher.dispose();
				_internalProxyMessageDispatcher = null;
			}
			
			_internalProxyMessageDispatcher = new ProxyMessageDispatcher<InternalProxyMessage>("INTERNAL_MESSAGE_DISPATCHER",
					new InternalProxyMessageComparitor(),
					new IDispatchingStrategy<InternalProxyMessage>() {

						@Override
						public void dispatch(InternalProxyMessage message) {
							dispatchInternalMessage((InternalProxyMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromInternalMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromInternalMessageDispatcher(info, ex);
						}			
			});
		}
		
		// Setup Incoming ProxyMessage Dispatcher
		synchronized(INCOMING_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure incomingProxyMessageDispatcher is null
			if (_incomingProxyMessageDispatcher != null) {
				_incomingProxyMessageDispatcher.dispose();
				_incomingProxyMessageDispatcher = null;
			}
			
			_incomingProxyMessageDispatcher = new ProxyMessageDispatcher<ProtocolMessage>("INCOMING_MESSAGE_DISPATCHER",
					new IncomingProtocolMessageComparitor(),
					new IDispatchingStrategy<ProtocolMessage>() {
						@Override
						public void dispatch(ProtocolMessage message) {
							dispatchIncomingMessage((ProtocolMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromIncomingMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromIncomingMessageDispatcher(info, ex);
						}			
			});
		}
		
		// Setup Outgoing ProxyMessage Dispatcher
		synchronized(OUTGOING_MESSAGE_QUEUE_THREAD_LOCK) {
			// Ensure outgoingProxyMessageDispatcher is null
			if (_outgoingProxyMessageDispatcher != null) {
				_outgoingProxyMessageDispatcher.dispose();
				_outgoingProxyMessageDispatcher = null;
			}
			
			_outgoingProxyMessageDispatcher = new ProxyMessageDispatcher<ProtocolMessage>("OUTGOING_MESSAGE_DISPATCHER",
					new OutgoingProtocolMessageComparitor(),
					new IDispatchingStrategy<ProtocolMessage>() {
						@Override
						public void dispatch(ProtocolMessage message) {
							dispatchOutgoingMessage((ProtocolMessage)message);
						}
	
						@Override
						public void handleDispatchingError(String info, Exception ex) {
							handleErrorsFromOutgoingMessageDispatcher(info, ex);
						}
	
						@Override
						public void handleQueueingError(String info, Exception ex) {
							handleErrorsFromOutgoingMessageDispatcher(info, ex);
						}
			});
		}
		
		// Initialize the proxy
		try {
			initializeProxy();
		} catch (SmartDeviceLinkException e) {
			// Couldn't initialize the proxy 
			// Dispose threads and then rethrow exception
			
			if (_internalProxyMessageDispatcher != null) {
				_internalProxyMessageDispatcher.dispose();
				_internalProxyMessageDispatcher = null;
			}
			if (_incomingProxyMessageDispatcher != null) {
				_incomingProxyMessageDispatcher.dispose();
				_incomingProxyMessageDispatcher = null;
			}
			if (_outgoingProxyMessageDispatcher != null) {
				_outgoingProxyMessageDispatcher.dispose();
				_outgoingProxyMessageDispatcher = null;
			}
			throw e;
		} 
		
		// Trace that ctor has fired
		SmartDeviceLinkTrace.logProxyEvent("SmartDeviceLinkProxy Created, instanceID=" + this.toString(), SMARTDEVICELINK_LIB_TRACE_KEY);
	}

	private Intent createBroadcastIntent()
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction("com.smartdevicelink.broadcast");
		sendIntent.putExtra("APP_NAME", this._applicationName);
		sendIntent.putExtra("APP_ID", this._appID);
		sendIntent.putExtra("RPC_NAME", "");
		sendIntent.putExtra("TYPE", "");
		sendIntent.putExtra("SUCCESS", true);
		sendIntent.putExtra("CORRID", 0);
		sendIntent.putExtra("FUNCTION_NAME", "");
		sendIntent.putExtra("COMMENT1", "");
		sendIntent.putExtra("COMMENT2", "");
		sendIntent.putExtra("COMMENT3", "");
		sendIntent.putExtra("COMMENT4", "");
		sendIntent.putExtra("COMMENT5", "");
		sendIntent.putExtra("COMMENT6", "");
		sendIntent.putExtra("COMMENT7", "");
		sendIntent.putExtra("COMMENT8", "");
		sendIntent.putExtra("COMMENT9", "");
		sendIntent.putExtra("COMMENT10", "");
		sendIntent.putExtra("DATA", "");
		sendIntent.putExtra("SHOW_ON_UI", true);
		return sendIntent;
	}
	private void updateBroadcastIntent(Intent sendIntent, String sKey, String sValue)
	{
		sendIntent.putExtra(sKey, sValue);		
	}
	private void updateBroadcastIntent(Intent sendIntent, String sKey, boolean bValue)
	{
		sendIntent.putExtra(sKey, bValue);		
	}
	private void updateBroadcastIntent(Intent sendIntent, String sKey, int iValue)
	{
		sendIntent.putExtra(sKey, iValue);		
	}
	
	private void sendBroadcastIntent(Intent sendIntent)
	{
		Service myService = null;		
		if (_proxyListener != null && _proxyListener instanceof Service)
		{
			myService = (Service) _proxyListener;				
		}
		else if (_appService != null)
		{
			myService = _appService;
		}
		else
		{
			return;
		}		
		Context myContext = myService.getApplicationContext();				
		if (myContext != null) myContext.sendBroadcast(sendIntent);		
	}

	/**
	 * send encoded SmartDeviceLink data to Url
	 * @param urlString
	 * @param encodedSyncPData
	 * @param timeout
	 */
	private void sendEncodedSyncPDataToUrl(String urlString, Vector<String> encodedSyncPData, Integer timeout) {		
		
	}

	/**
	 * send encoded SmartDeviceLink data to Url
	 * @param urlString
	 * @param bs
	 * @param timeout
	 */
	private void sendSyncPDataToUrl(String urlString, byte[] bs, Integer timeout) {

	}

	// Test correlationID
	private boolean isCorrelationIDProtected(Integer correlationID) {
		if (correlationID != null && 
				(REGISTER_APP_INTERFACE_CORRELATION_ID == correlationID
						|| UNREGISTER_APP_INTERFACE_CORRELATION_ID == correlationID
						|| POLICIES_CORRELATION_ID == correlationID)) {
			return true;
		}
		
		return false;
	}
	
	// Protected isConnected method to allow legacy proxy to poll isConnected state
	public Boolean getIsConnected() {
		return _smartDeviceLinkConnection.getIsConnected();
	}
	
	/**
	 * Returns whether the application is registered in SMARTDEVICELINK. Note: for testing
	 * purposes, it's possible that the connection is established, but the
	 * application is not registered.
	 * 
	 * @return true if the application is registered in SMARTDEVICELINK
	 */
	public Boolean getAppInterfaceRegistered() {
		return _appInterfaceRegisterd;
	}

	// Function to initialize new proxy connection
	private void initializeProxy() throws SmartDeviceLinkException {		
		// Reset all of the flags and state variables
		_haveReceivedFirstNonNoneHMILevel = false;
		_haveReceivedFirstFocusLevel = false;
		_haveReceivedFirstFocusLevelFull = false;
		_smartDeviceLinkIntefaceAvailablity = SmartDeviceLinkInterfaceAvailability.SMARTDEVICELINK_INTERFACE_UNAVAILABLE;
				
		// Setup smartDeviceLinkConnection
		synchronized(CONNECTION_REFERENCE_LOCK) {
			if (_smartDeviceLinkConnection != null) {
				_smartDeviceLinkConnection.closeConnection(_rpcSessionID);
				_smartDeviceLinkConnection = null;
			}
			_smartDeviceLinkConnection = new smartDeviceLinkConnection(_interfaceBroker, _transportConfig);
			SmartDeviceLinkProtocol protocol = (SmartDeviceLinkProtocol)_smartDeviceLinkConnection.getSmartDeviceLinkProtocolProtocol();
			protocol.setVersion(_sdlproVersion);
		}
		
		synchronized(CONNECTION_REFERENCE_LOCK) {
			if (_smartDeviceLinkConnection != null) {
				_smartDeviceLinkConnection.startTransport();
			}
		}
	}
	
	/**
	 *  Public method to enable the siphon transport
	 */
	public static void enableSiphonDebug() {
		SiphonServer.enableSiphonServer();
	}
	
	/**
	 *  Public method to disable the Siphon Trace Server
	 */
	public static void disableSiphonDebug() {
		SiphonServer.disableSiphonServer();
	}	
	
	/**
	 *  Public method to enable the Debug Tool
	 */
	public static void enableDebugTool() {
		DebugTool.enableDebugTool();
	}
	
	/**
	 *  Public method to disable the Debug Tool
	 */
	public static void disableDebugTool() {
		DebugTool.disableDebugTool();
	}	

	/**
	* Public method to determine Debug Tool enabled
	*/
	public static boolean isDebugEnabled() {
		return DebugTool.isDebugEnabled();
	}	
	
	
	@Deprecated
	public void close() throws SmartDeviceLinkException {
		dispose();
	}
	
	private void cleanProxy(SmartDeviceLinkDisconnectedReason disconnectedReason) throws SmartDeviceLinkException {
		try {
			
			// ALM Specific Cleanup
			if (_advancedLifecycleManagementEnabled) {
				_smartDeviceLinkConnectionState = SmartDeviceLinkConnectionState.SMARTDEVICELINK_DISCONNECTED;
				
				firstTimeFull = true;
			
				// Should we wait for the interface to be unregistered?
				Boolean waitForInterfaceUnregistered = false;
				// Unregister app interface
				synchronized(CONNECTION_REFERENCE_LOCK) {
					if (_appInterfaceRegisterd == true && _smartDeviceLinkConnection != null && _smartDeviceLinkConnection.getIsConnected()) {
						waitForInterfaceUnregistered = true;
						unregisterAppInterfacePrivate(UNREGISTER_APP_INTERFACE_CORRELATION_ID);
					}
				}
				
				// Wait for the app interface to be unregistered
				if (waitForInterfaceUnregistered) {
					synchronized(APP_INTERFACE_REGISTERED_LOCK) {
						try {
							APP_INTERFACE_REGISTERED_LOCK.wait(1000);
						} catch (InterruptedException e) {
							// Do nothing
						}
					}
				}
			}
			
			// Clean up SMARTDEVICELINK Connection
			synchronized(CONNECTION_REFERENCE_LOCK) {
				if (_smartDeviceLinkConnection != null) {
					_smartDeviceLinkConnection.closeConnection(_rpcSessionID);
					_smartDeviceLinkConnection = null;
				}
			}		
		} catch (SmartDeviceLinkException e) {
			throw e;
		} finally {
			SmartDeviceLinkTrace.logProxyEvent("SmartDeviceLinkProxy cleaned.", SMARTDEVICELINK_LIB_TRACE_KEY);
		}
	}
	
	/**
	 * Terminates the App's Interface Registration, closes the transport connection, ends the protocol session, and frees any resources used by the proxy.
	 */
	public void dispose() throws SmartDeviceLinkException
	{		
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		_proxyDisposed = true;
		
		SmartDeviceLinkTrace.logProxyEvent("Application called dispose() method.", SMARTDEVICELINK_LIB_TRACE_KEY);
		
		try{
			// Clean the proxy
			cleanProxy(SmartDeviceLinkDisconnectedReason.APPLICATION_REQUESTED_DISCONNECT);
		
			// Close IncomingProxyMessageDispatcher thread
			synchronized(INCOMING_MESSAGE_QUEUE_THREAD_LOCK) {
				if (_incomingProxyMessageDispatcher != null) {
					_incomingProxyMessageDispatcher.dispose();
					_incomingProxyMessageDispatcher = null;
				}
			}
			
			// Close OutgoingProxyMessageDispatcher thread
			synchronized(OUTGOING_MESSAGE_QUEUE_THREAD_LOCK) {
				if (_outgoingProxyMessageDispatcher != null) {
					_outgoingProxyMessageDispatcher.dispose();
					_outgoingProxyMessageDispatcher = null;
				}
			}
			
			// Close InternalProxyMessageDispatcher thread
			synchronized(INTERNAL_MESSAGE_QUEUE_THREAD_LOCK) {
				if (_internalProxyMessageDispatcher != null) {
					_internalProxyMessageDispatcher.dispose();
					_internalProxyMessageDispatcher = null;
				}
			}
			
			_traceDeviceInterrogator = null;
		} catch (SmartDeviceLinkException e) {
			throw e;
		} finally {
			SmartDeviceLinkTrace.logProxyEvent("SmartDeviceLinkProxy disposed.", SMARTDEVICELINK_LIB_TRACE_KEY);
		}
	} // end-method

	// Method to cycle the proxy, only called in ALM
	protected void cycleProxy(SmartDeviceLinkDisconnectedReason disconnectedReason) {		
		try{
			cleanProxy(disconnectedReason);
			initializeProxy();	
			notifyProxyClosed("SMARTDEVICELINK Proxy Cycled", new SmartDeviceLinkException("SMARTDEVICELINK Proxy Cycled", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_CYCLED));
		} catch (SmartDeviceLinkException e) {
			switch(e.getSmartDeviceLinkExceptionCause()) {
			case BLUETOOTH_DISABLED:
				notifyProxyClosed("Bluetooth is disabled. Bluetooth must be enabled to connect to SMARTDEVICELINK. Reattempt a connection once Bluetooth is enabled.", 
						new SmartDeviceLinkException("Bluetooth is disabled. Bluetooth must be enabled to connect to SMARTDEVICELINK. Reattempt a connection once Bluetooth is enabled.", SmartDeviceLinkExceptionCause.BLUETOOTH_DISABLED));
				break;
			case BLUETOOTH_ADAPTER_NULL:
				notifyProxyClosed("Cannot locate a Bluetooth adapater. A SMARTDEVICELINK connection is impossible on this device until a Bluetooth adapter is added.", 
						new SmartDeviceLinkException("Cannot locate a Bluetooth adapater. A SMARTDEVICELINK connection is impossible on this device until a Bluetooth adapter is added.", SmartDeviceLinkExceptionCause.BLUETOOTH_ADAPTER_NULL));
				break;
			default :
				notifyProxyClosed("Cycling the proxy failed.", e);
				break;
			}
		} catch (Exception e) { 
			notifyProxyClosed("Cycling the proxy failed.", e);
		}
	}

	
	
	/************* Functions used by the Message Dispatching Queues ****************/
	private void dispatchIncomingMessage(ProtocolMessage message) {
		try{
			// Dispatching logic
			if (message.getSessionType().equals(SessionType.RPC)) {
				try {
					if (_sdlproVersion == 1) {
						if (message.getVersion() == 2) setsdlproVersion(message.getVersion());
					}
					
					Hashtable hash = new Hashtable();
					if (_sdlproVersion == 2) {
						Hashtable hashTemp = new Hashtable();
						hashTemp.put(Names.correlationID, message.getCorrID());
						if (message.getJsonSize() > 0) {
							final Hashtable<String, Object> mhash = JsonRPCMarshaller.unmarshall(message.getData());
							//hashTemp.put(Names.parameters, mhash.get(Names.parameters));
							hashTemp.put(Names.parameters, mhash);
						}
						FunctionID functionID = new FunctionID();
						hashTemp.put(Names.function_name, functionID.getFunctionName(message.getFunctionID()));
						if (message.getRPCType() == 0x00) {
							hash.put(Names.request, hashTemp);
						} else if (message.getRPCType() == 0x01) {
							hash.put(Names.response, hashTemp);
						} else if (message.getRPCType() == 0x02) {
							hash.put(Names.notification, hashTemp);
						}
						if (message.getBulkData() != null) hash.put(Names.bulkData, message.getBulkData());
					} else {
						final Hashtable<String, Object> mhash = JsonRPCMarshaller.unmarshall(message.getData());
						hash = mhash;
					}
					handleRPCMessage(hash);							
				} catch (final Exception excp) {
					DebugTool.logError("Failure handling protocol message: " + excp.toString(), excp);
					passErrorToProxyListener("Error handing incoming protocol message.", excp);
				} // end-catch
			} else {
				// Handle other protocol message types here
			}
		} catch (final Exception e) {
			// Pass error to application through listener 
			DebugTool.logError("Error handing proxy event.", e);
			passErrorToProxyListener("Error handing incoming protocol message.", e);
		}
	}
	
	private byte getsdlproVersion() {
		return this._sdlproVersion;
	}
	
	private void setsdlproVersion(byte version) {
		this._sdlproVersion = version;
	}
	
	public String serializeJSON(RPCMessage msg)
	{
		String sReturn = null;		
		try
		{
			sReturn = msg.serializeJSON(getsdlproVersion()).toString(2);
		}
		catch (final Exception e) 
		{
			DebugTool.logError("Error handing proxy event.", e);
			passErrorToProxyListener("Error serializing message.", e);
			return null;
		}
		return sReturn;
	}

	private void handleErrorsFromIncomingMessageDispatcher(String info, Exception e) {
		passErrorToProxyListener(info, e);
	}
	
	private void dispatchOutgoingMessage(ProtocolMessage message) {
		synchronized(CONNECTION_REFERENCE_LOCK) {
			if (_smartDeviceLinkConnection != null) {
				_smartDeviceLinkConnection.sendMessage(message);
			}
		}		
		SmartDeviceLinkTrace.logProxyEvent("SmartDeviceLinkProxy sending Protocol Message: " + message.toString(), SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	private void handleErrorsFromOutgoingMessageDispatcher(String info, Exception e) {
		passErrorToProxyListener(info, e);
	}
	
	void dispatchInternalMessage(final InternalProxyMessage message) {
		try{
			if (message.getFunctionName().equals(Names.OnProxyError)) {
				final OnError msg = (OnError)message;			
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onError(msg.getInfo(), msg.getException());
						}
					});
				} else {
					_proxyListener.onError(msg.getInfo(), msg.getException());
				}
			/**************Start Legacy Specific Call-backs************/
			} else if (message.getFunctionName().equals(Names.OnProxyOpened)) {
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							((IProxyListener)_proxyListener).onProxyOpened();
						}
					});
				} else {
					((IProxyListener)_proxyListener).onProxyOpened();
				}
			} else if (message.getFunctionName().equals(Names.OnProxyClosed)) {
				final OnProxyClosed msg = (OnProxyClosed)message;
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onProxyClosed(msg.getInfo(), msg.getException());
						}
					});
				} else {
					_proxyListener.onProxyClosed(msg.getInfo(), msg.getException());
				}
			/****************End Legacy Specific Call-backs************/
			} else {
				// Diagnostics
				SmartDeviceLinkTrace.logProxyEvent("Unknown RPC Message encountered. Check for an updated version of the SMARTDEVICELINK Proxy.", SMARTDEVICELINK_LIB_TRACE_KEY);
				DebugTool.logError("Unknown RPC Message encountered. Check for an updated version of the SMARTDEVICELINK Proxy.");
			}
			
		SmartDeviceLinkTrace.logProxyEvent("Proxy fired callback: " + message.getFunctionName(), SMARTDEVICELINK_LIB_TRACE_KEY);
		} catch(final Exception e) {
			// Pass error to application through listener 
			DebugTool.logError("Error handing proxy event.", e);
			if (_callbackToUIThread) {
				// Run in UI thread
				_mainUIHandler.post(new Runnable() {
					@Override
					public void run() {
						_proxyListener.onError("Error handing proxy event.", e);
					}
				});
			} else {
				_proxyListener.onError("Error handing proxy event.", e);
			}
		}
	}
	
	private void handleErrorsFromInternalMessageDispatcher(String info, Exception e) {
		DebugTool.logError(info, e);
		// This error cannot be passed to the user, as it indicates an error
		// in the communication between the proxy and the application.
		
		DebugTool.logError("InternalMessageDispatcher failed.", e);
		
		// Note, this is the only place where the _proxyListener should be referenced asmartDeviceLinkhronously,
		// with an error on the internalMessageDispatcher, we have no other reliable way of 
		// communicating with the application.
		notifyProxyClosed("Proxy callback dispatcher is down. Proxy instance is invalid.", e);
		_proxyListener.onError("Proxy callback dispatcher is down. Proxy instance is invalid.", e);
	}
	/************* END Functions used by the Message Dispatching Queues ****************/
	
	// Private sendPRCRequest method. All RPCRequests are funneled through this method after
		// error checking. 
	private void sendRPCRequestPrivate(RPCRequest request) throws SmartDeviceLinkException {
			try {
			SmartDeviceLinkTrace.logRPCEvent(InterfaceActivityDirection.Transmit, request, SMARTDEVICELINK_LIB_TRACE_KEY);
						
			byte[] msgBytes = JsonRPCMarshaller.marshall(request, _sdlproVersion);
	
			ProtocolMessage pm = new ProtocolMessage();
			pm.setData(msgBytes);
			pm.setSessionID(_rpcSessionID);
			pm.setMessageType(MessageType.RPC);
			pm.setSessionType(SessionType.RPC);
			pm.setFunctionID(FunctionID.getFunctionID(request.getFunctionName()));
			pm.setCorrID(request.getCorrelationID());
			if (request.getBulkData() != null) 
				pm.setBulkData(request.getBulkData());
			
			// Queue this outgoing message
			synchronized(OUTGOING_MESSAGE_QUEUE_THREAD_LOCK) {
				if (_outgoingProxyMessageDispatcher != null) {
					_outgoingProxyMessageDispatcher.queueMessage(pm);
				}
			}
		} catch (OutOfMemoryError e) {
			SmartDeviceLinkTrace.logProxyEvent("OutOfMemory exception while sending request " + request.getFunctionName(), SMARTDEVICELINK_LIB_TRACE_KEY);
			throw new SmartDeviceLinkException("OutOfMemory exception while sending request " + request.getFunctionName(), e, SmartDeviceLinkExceptionCause.INVALID_ARGUMENT);
		}
	}
	
	private void handleRPCMessage(Hashtable hash) {
		RPCMessage rpcMsg = new RPCMessage(hash);
		String functionName = rpcMsg.getFunctionName();
		String messageType = rpcMsg.getMessageType();
		
		if (messageType.equals(Names.response)) {			
			SmartDeviceLinkTrace.logRPCEvent(InterfaceActivityDirection.Receive, new RPCResponse(rpcMsg), SMARTDEVICELINK_LIB_TRACE_KEY);

			// Check to ensure response is not from an internal message (reserved correlation ID)
			if (isCorrelationIDProtected((new RPCResponse(hash)).getCorrelationID())) {
				// This is a response generated from an internal message, it can be trapped here
				// The app should not receive a response for a request it did not send
				if ((new RPCResponse(hash)).getCorrelationID() == REGISTER_APP_INTERFACE_CORRELATION_ID 
						&& _advancedLifecycleManagementEnabled 
						&& functionName.equals(Names.RegisterAppInterface)) {
					final RegisterAppInterfaceResponse msg = new RegisterAppInterfaceResponse(hash);
					if (msg.getSuccess()) {
						_appInterfaceRegisterd = true;
					}
					
					//_autoActivateIdReturned = msg.getAutoActivateID();
					/*Place holder for legacy support*/ _autoActivateIdReturned = "8675309";
					_buttonCapabilities = msg.getButtonCapabilities();
					_displayCapabilities = msg.getDisplayCapabilities();
					_softButtonCapabilities = msg.getSoftButtonCapabilities();
					_presetBankCapabilities = msg.getPresetBankCapabilities();
					_hmiZoneCapabilities = msg.getHmiZoneCapabilities();
					_speechCapabilities = msg.getSpeechCapabilities();
					_smartDeviceLinkLanguage = msg.getLanguage();
					_hmiDisplayLanguage = msg.getHmiDisplayLanguage();
					_smartDeviceLinkMsgVersion = msg.getsmartdevicelinkMsgVersion();
					_vrCapabilities = msg.getVrCapabilities();
					_vehicleType = msg.getVehicleType();
					_proxyVersionInfo = msg.getProxyVersionInfo();
					
					String sVersionInfo = "SMARTDEVICELINK Proxy Version: " + _proxyVersionInfo;
					
					Class<?> cls = null;
					try 
					{
						cls = Class.forName(Names.VersionExtend);
						Object t = cls.newInstance();						
						Method meth = cls.getDeclaredMethod("getVersionInfo", null);															
						final Object ver = meth.invoke(t, null);						
						String sValue = ver.toString();					
						sVersionInfo += "\r\n" + "ExtendedLib Version: " + sValue; 						
					} 
					catch (Exception e) 
					{
						//Extended info does not exist
					}
										
					if (!isDebugEnabled()) 
					{
						enableDebugTool();
						DebugTool.logInfo(sVersionInfo, false);
						disableDebugTool();
					}					
					else
						DebugTool.logInfo(sVersionInfo, false);
					
					// Send onSmartDeviceLinkConnected message in ALM
					_smartDeviceLinkConnectionState = SmartDeviceLinkConnectionState.SMARTDEVICELINK_CONNECTED;
					
					// If registerAppInterface failed, exit with OnProxyUnusable
					if (!msg.getSuccess()) {
						notifyProxyClosed("Unable to register app interface. Review values passed to the SMARTDEVICELINK Proxy constructor. RegisterAppInterface result code: ", 
								new SmartDeviceLinkException("Unable to register app interface. Review values passed to the SmartDeviceLinkProxy constructor. RegisterAppInterface result code: " + msg.getResultCode(), SmartDeviceLinkExceptionCause.SMARTDEVICELINK_REGISTRATION_ERROR));
					}
					
					if (_callbackToUIThread) {
						// Run in UI thread
						_mainUIHandler.post(new Runnable() {
							@Override
							public void run() {
								if (_proxyListener instanceof IProxyListener) {
									((IProxyListener)_proxyListener).onRegisterAppInterfaceResponse(msg);
								} else if (_proxyListener instanceof IProxyListenerALM) {
									//((IProxyListenerALM)_proxyListener).onRegisterAppInterfaceResponse(msg);
								}
							}
						});
					} else {
						if (_proxyListener instanceof IProxyListener) {
							((IProxyListener)_proxyListener).onRegisterAppInterfaceResponse(msg);
						} else if (_proxyListener instanceof IProxyListenerALM) {
							//((IProxyListenerALM)_proxyListener).onRegisterAppInterfaceResponse(msg);
						}
					}
				} else if ((new RPCResponse(hash)).getCorrelationID() == POLICIES_CORRELATION_ID 
						&& functionName.equals(Names.OnEncodedSyncPData)) {
					// OnEncodedSyncPData
					final OnEncodedSyncPData msg = new OnEncodedSyncPData(hash);

					// If url is not null, then send to URL
					if (msg.getUrl() != null) {
						// URL has data, attempt to post request to external server
						Thread handleOffboardSmartDeviceLinkTransmissionTread = new Thread() {
							@Override
							public void run() {
								sendEncodedSyncPDataToUrl(msg.getUrl(), msg.getData(), msg.getTimeout());
							}
						};

						handleOffboardSmartDeviceLinkTransmissionTread.start();
					} 
					
				} else if ((new RPCResponse(hash)).getCorrelationID() == POLICIES_CORRELATION_ID 
						&& functionName.equals(Names.OnSyncPData)) {
					// OnSyncPData
					final OnSyncPData msg = new OnSyncPData(hash);

					// If url is not null, then send to URL
					if (msg.getUrl() != null) {
						// URL has data, attempt to post request to external server
						Thread handleOffboardSmartDeviceLinkTransmissionTread = new Thread() {
							@Override
							public void run() {
								sendSyncPDataToUrl(msg.getUrl(), msg.getSyncPData(), msg.getTimeout());
							}
						};

						handleOffboardSmartDeviceLinkTransmissionTread.start();
					} 
				}
				else if ((new RPCResponse(hash)).getCorrelationID() == POLICIES_CORRELATION_ID 
						&& functionName.equals(Names.EncodedSyncPData)) {
					// EncodedSyncPData
					final EncodedSyncPDataResponse msg = new EncodedSyncPDataResponse(hash);
					
					Intent sendIntent = createBroadcastIntent();
					updateBroadcastIntent(sendIntent, "RPC_NAME", Names.EncodedSyncPData);
					updateBroadcastIntent(sendIntent, "TYPE", Names.response);
					updateBroadcastIntent(sendIntent, "SUCCESS", msg.getSuccess());
					updateBroadcastIntent(sendIntent, "COMMENT1", msg.getInfo());
					updateBroadcastIntent(sendIntent, "COMMENT2", msg.getResultCode().toString());
					updateBroadcastIntent(sendIntent, "CORRID", msg.getCorrelationID());
					sendBroadcastIntent(sendIntent);
				}				
				return;
			}
			
			if (functionName.equals(Names.RegisterAppInterface)) {
				final RegisterAppInterfaceResponse msg = new RegisterAppInterfaceResponse(hash);
				if (msg.getSuccess()) {
					_appInterfaceRegisterd = true;
				}

				//_autoActivateIdReturned = msg.getAutoActivateID();
				/*Place holder for legacy support*/ _autoActivateIdReturned = "8675309";
				_buttonCapabilities = msg.getButtonCapabilities();
				_displayCapabilities = msg.getDisplayCapabilities();
				_softButtonCapabilities = msg.getSoftButtonCapabilities();
				_presetBankCapabilities = msg.getPresetBankCapabilities();
				_hmiZoneCapabilities = msg.getHmiZoneCapabilities();
				_speechCapabilities = msg.getSpeechCapabilities();
				_smartDeviceLinkLanguage = msg.getLanguage();
				_hmiDisplayLanguage = msg.getHmiDisplayLanguage();
				_smartDeviceLinkMsgVersion = msg.getsmartdevicelinkMsgVersion();
				_vrCapabilities = msg.getVrCapabilities();
				_vehicleType = msg.getVehicleType();
				_proxyVersionInfo = msg.getProxyVersionInfo();
				
				if (!isDebugEnabled()) 
				{
					enableDebugTool();
					DebugTool.logInfo("SMARTDEVICELINK Proxy Version: " + _proxyVersionInfo);
					disableDebugTool();
				}					
				else
					DebugTool.logInfo("SMARTDEVICELINK Proxy Version: " + _proxyVersionInfo);				
				
				// RegisterAppInterface
				if (_advancedLifecycleManagementEnabled) {
					
					// Send onSmartDeviceLinkConnected message in ALM
					_smartDeviceLinkConnectionState = SmartDeviceLinkConnectionState.SMARTDEVICELINK_CONNECTED;
					
					// If registerAppInterface failed, exit with OnProxyUnusable
					if (!msg.getSuccess()) {
						notifyProxyClosed("Unable to register app interface. Review values passed to the SmartDeviceLinkProxy constructor. RegisterAppInterface result code: ", 
								new SmartDeviceLinkException("Unable to register app interface. Review values passed to the SmartDeviceLinkProxy constructor. RegisterAppInterface result code: " + msg.getResultCode(), SmartDeviceLinkExceptionCause.SMARTDEVICELINK_REGISTRATION_ERROR));
					}
				} else {	
					if (_callbackToUIThread) {
						// Run in UI thread
						_mainUIHandler.post(new Runnable() {
							@Override
							public void run() {
								if (_proxyListener instanceof IProxyListener) {
									((IProxyListener)_proxyListener).onRegisterAppInterfaceResponse(msg);
								} else if (_proxyListener instanceof IProxyListenerALM) {
									//((IProxyListenerALM)_proxyListener).onRegisterAppInterfaceResponse(msg);
								}
							}
						});
					} else {
						if (_proxyListener instanceof IProxyListener) {
							((IProxyListener)_proxyListener).onRegisterAppInterfaceResponse(msg);
						} else if (_proxyListener instanceof IProxyListenerALM) {
							//((IProxyListenerALM)_proxyListener).onRegisterAppInterfaceResponse(msg);
						}
					}
				}
			} else if (functionName.equals(Names.Speak)) {
				// SpeakResponse
				
				final SpeakResponse msg = new SpeakResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onSpeakResponse(msg);
						}
					});
				} else {
					_proxyListener.onSpeakResponse(msg);						
				}
			} else if (functionName.equals(Names.Alert)) {
				// AlertResponse
				
				final AlertResponse msg = new AlertResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onAlertResponse(msg);
						}
					});
				} else {
					_proxyListener.onAlertResponse(msg);						
				}
			} else if (functionName.equals(Names.Show)) {
				// ShowResponse
				
				final ShowResponse msg = new ShowResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onShowResponse((ShowResponse)msg);
						}
					});
				} else {
					_proxyListener.onShowResponse((ShowResponse)msg);						
				}
			} else if (functionName.equals(Names.AddCommand)) {
				// AddCommand
				
				final AddCommandResponse msg = new AddCommandResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onAddCommandResponse((AddCommandResponse)msg);
						}
					});
				} else {
					_proxyListener.onAddCommandResponse((AddCommandResponse)msg);					
				}
			} else if (functionName.equals(Names.DeleteCommand)) {
				// DeleteCommandResponse
				
				final DeleteCommandResponse msg = new DeleteCommandResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onDeleteCommandResponse((DeleteCommandResponse)msg);
						}
					});
				} else {
					_proxyListener.onDeleteCommandResponse((DeleteCommandResponse)msg);					
				}
			} else if (functionName.equals(Names.AddSubMenu)) {
				// AddSubMenu
				
				final AddSubMenuResponse msg = new AddSubMenuResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onAddSubMenuResponse((AddSubMenuResponse)msg);
						}
					});
				} else {
					_proxyListener.onAddSubMenuResponse((AddSubMenuResponse)msg);					
				}
			} else if (functionName.equals(Names.DeleteSubMenu)) {
				// DeleteSubMenu
				
				final DeleteSubMenuResponse msg = new DeleteSubMenuResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onDeleteSubMenuResponse((DeleteSubMenuResponse)msg);
						}
					});
				} else {
					_proxyListener.onDeleteSubMenuResponse((DeleteSubMenuResponse)msg);					
				}
			} else if (functionName.equals(Names.SubscribeButton)) {
				// SubscribeButton
				
				final SubscribeButtonResponse msg = new SubscribeButtonResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onSubscribeButtonResponse((SubscribeButtonResponse)msg);
						}
					});
				} else {
					_proxyListener.onSubscribeButtonResponse((SubscribeButtonResponse)msg);				
				}
			} else if (functionName.equals(Names.UnsubscribeButton)) {
				// UnsubscribeButton
				
				final UnsubscribeButtonResponse msg = new UnsubscribeButtonResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onUnsubscribeButtonResponse((UnsubscribeButtonResponse)msg);
						}
					});
				} else {
					_proxyListener.onUnsubscribeButtonResponse((UnsubscribeButtonResponse)msg);			
				}
			} else if (functionName.equals(Names.SetMediaClockTimer)) {
				// SetMediaClockTimer
				
				final SetMediaClockTimerResponse msg = new SetMediaClockTimerResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onSetMediaClockTimerResponse((SetMediaClockTimerResponse)msg);
						}
					});
				} else {
					_proxyListener.onSetMediaClockTimerResponse((SetMediaClockTimerResponse)msg);		
				}
			} else if (functionName.equals(Names.EncodedSyncPData)) {
				// EncodedSyncPData
				
				final EncodedSyncPDataResponse msg = new EncodedSyncPDataResponse(hash);
				
				Intent sendIntent = createBroadcastIntent();
				updateBroadcastIntent(sendIntent, "RPC_NAME", Names.EncodedSyncPData);
				updateBroadcastIntent(sendIntent, "TYPE", Names.response);
				updateBroadcastIntent(sendIntent, "SUCCESS", msg.getSuccess());
				updateBroadcastIntent(sendIntent, "COMMENT1", msg.getInfo());
				updateBroadcastIntent(sendIntent, "COMMENT2", msg.getResultCode().toString());
				updateBroadcastIntent(sendIntent, "CORRID", msg.getCorrelationID());
				sendBroadcastIntent(sendIntent);

				try
				{
					Class<?> cls = Class.forName(Names.ListenerExtend);
					if (cls.isInstance(_proxyListener))
					{
						final Object t = cls.cast(_proxyListener);
						final Method meth = cls.getDeclaredMethod(Names.onEncodedSyncPDataResponse, new Class[]{EncodedSyncPDataResponse.class});
						if (_callbackToUIThread) {
							// Run in UI thread
							_mainUIHandler.post(new Runnable() {
								@Override
								public void run() {
									try {
										meth.invoke(t, msg);
									}
									catch (Exception e)  {
										e.printStackTrace();
									}
								}
							});
						} else {
							meth.invoke(t, msg);
						}
					}
				}
				catch (Exception e)
				{
					//private lib not present
				}
			} else if (functionName.equals(Names.SyncPData)) {
				// SyncPData
				
				final SyncPDataResponse msg = new SyncPDataResponse(hash);

				Intent sendIntent = createBroadcastIntent();
				updateBroadcastIntent(sendIntent, "RPC_NAME", Names.SyncPData);
				updateBroadcastIntent(sendIntent, "TYPE", Names.response);
				updateBroadcastIntent(sendIntent, "SUCCESS", msg.getSuccess());				
				updateBroadcastIntent(sendIntent, "COMMENT1", msg.getInfo());
				updateBroadcastIntent(sendIntent, "COMMENT2", msg.getResultCode().toString());
				updateBroadcastIntent(sendIntent, "CORRID", msg.getCorrelationID());
				sendBroadcastIntent(sendIntent);				
				try
				{
					Class<?> cls = Class.forName(Names.ListenerExtend);
					if (cls.isInstance(_proxyListener))
					{
						final Object t = cls.cast(_proxyListener);
						final Method meth = cls.getDeclaredMethod(Names.onSyncPDataResponse, new Class[]{SyncPDataResponse.class});
						if (_callbackToUIThread) {
							// Run in UI thread
							_mainUIHandler.post(new Runnable() {
								@Override
								public void run() {
									try {
										meth.invoke(t, msg);
									}
									catch (Exception e)  {
										e.printStackTrace();
									}
								}
							});
						} else {
							meth.invoke(t, msg);
						}
					}
				}
				catch (Exception e)
				{
					//private lib not present
				}
			} else if (functionName.equals(Names.CreateInteractionChoiceSet)) {
				// CreateInteractionChoiceSet
				
				final CreateInteractionChoiceSetResponse msg = new CreateInteractionChoiceSetResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onCreateInteractionChoiceSetResponse((CreateInteractionChoiceSetResponse)msg);
						}
					});
				} else {
					_proxyListener.onCreateInteractionChoiceSetResponse((CreateInteractionChoiceSetResponse)msg);		
				}
			} else if (functionName.equals(Names.DeleteInteractionChoiceSet)) {
				// DeleteInteractionChoiceSet
				
				final DeleteInteractionChoiceSetResponse msg = new DeleteInteractionChoiceSetResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onDeleteInteractionChoiceSetResponse((DeleteInteractionChoiceSetResponse)msg);
						}
					});
				} else {
					_proxyListener.onDeleteInteractionChoiceSetResponse((DeleteInteractionChoiceSetResponse)msg);		
				}
			} else if (functionName.equals(Names.PerformInteraction)) {
				// PerformInteraction
				
				final PerformInteractionResponse msg = new PerformInteractionResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onPerformInteractionResponse((PerformInteractionResponse)msg);
						}
					});
				} else {
					_proxyListener.onPerformInteractionResponse((PerformInteractionResponse)msg);		
				}
			} else if (functionName.equals(Names.SetGlobalProperties)) {
				// SetGlobalPropertiesResponse 
				
				final SetGlobalPropertiesResponse msg = new SetGlobalPropertiesResponse(hash);
				if (_callbackToUIThread) {
						// Run in UI thread
						_mainUIHandler.post(new Runnable() {
							@Override
							public void run() {
								_proxyListener.onSetGlobalPropertiesResponse((SetGlobalPropertiesResponse)msg);
							}
						});
					} else {
						_proxyListener.onSetGlobalPropertiesResponse((SetGlobalPropertiesResponse)msg);		
				}
			} else if (functionName.equals(Names.ResetGlobalProperties)) {
				// ResetGlobalProperties				
				
				final ResetGlobalPropertiesResponse msg = new ResetGlobalPropertiesResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onResetGlobalPropertiesResponse((ResetGlobalPropertiesResponse)msg);
						}
					});
				} else {
					_proxyListener.onResetGlobalPropertiesResponse((ResetGlobalPropertiesResponse)msg);		
				}
			} else if (functionName.equals(Names.UnregisterAppInterface)) {
				// UnregisterAppInterface
				
				_appInterfaceRegisterd = false;
				synchronized(APP_INTERFACE_REGISTERED_LOCK) {
					APP_INTERFACE_REGISTERED_LOCK.notify();
				}
				
				final UnregisterAppInterfaceResponse msg = new UnregisterAppInterfaceResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							if (_proxyListener instanceof IProxyListener) {
								((IProxyListener)_proxyListener).onUnregisterAppInterfaceResponse(msg);
							} else if (_proxyListener instanceof IProxyListenerALM) {
								//((IProxyListenerALM)_proxyListener).onUnregisterAppInterfaceResponse(msg);
							}
						}
					});
				} else {
					if (_proxyListener instanceof IProxyListener) {
						((IProxyListener)_proxyListener).onUnregisterAppInterfaceResponse(msg);
					} else if (_proxyListener instanceof IProxyListenerALM) {
						//((IProxyListenerALM)_proxyListener).onUnregisterAppInterfaceResponse(msg);
					}
				}
				
				notifyProxyClosed("UnregisterAppInterfaceResponse", null);
			} else if (functionName.equals(Names.GenericResponse)) {
				// GenericResponse (Usually and error)
				final GenericResponse msg = new GenericResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onGenericResponse((GenericResponse)msg);
						}
					});
				} else {
					_proxyListener.onGenericResponse((GenericResponse)msg);	
				}
			} else if (functionName.equals(Names.Slider)) {
                // Slider
                final SliderResponse msg = new SliderResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onSliderResponse((SliderResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onSliderResponse((SliderResponse)msg);   
                }
            } else if (functionName.equals(Names.PutFile)) {
                // PutFile
                final PutFileResponse msg = new PutFileResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onPutFileResponse((PutFileResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onPutFileResponse((PutFileResponse)msg);
                }
            } else if (functionName.equals(Names.DeleteFile)) {
                // DeleteFile
                final DeleteFileResponse msg = new DeleteFileResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onDeleteFileResponse((DeleteFileResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onDeleteFileResponse((DeleteFileResponse)msg);   
                }
            } else if (functionName.equals(Names.ListFiles)) {
                // ListFiles
                final ListFilesResponse msg = new ListFilesResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onListFilesResponse((ListFilesResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onListFilesResponse((ListFilesResponse)msg);     
                }
            } else if (functionName.equals(Names.SetAppIcon)) {
                // SetAppIcon
                final SetAppIconResponse msg = new SetAppIconResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onSetAppIconResponse((SetAppIconResponse)msg);
                        }
                    });
                } else {
                        _proxyListener.onSetAppIconResponse((SetAppIconResponse)msg);   
                }
            } else if (functionName.equals(Names.ScrollableMessage)) {
                // ScrollableMessage
                final ScrollableMessageResponse msg = new ScrollableMessageResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onScrollableMessageResponse((ScrollableMessageResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onScrollableMessageResponse((ScrollableMessageResponse)msg);     
                }
            } else if (functionName.equals(Names.ChangeRegistration)) {
                // ChangeLanguageRegistration
                final ChangeRegistrationResponse msg = new ChangeRegistrationResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onChangeRegistrationResponse((ChangeRegistrationResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onChangeRegistrationResponse((ChangeRegistrationResponse)msg);   
                }
            } else if (functionName.equals(Names.SetDisplayLayout)) {
                // SetDisplayLayout
                final SetDisplayLayoutResponse msg = new SetDisplayLayoutResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onSetDisplayLayoutResponse((SetDisplayLayoutResponse)msg);
                        }
                    });
                } else {
                        _proxyListener.onSetDisplayLayoutResponse((SetDisplayLayoutResponse)msg);
                }
            } else if (functionName.equals(Names.PerformAudioPassThru)) {
                // PerformAudioPassThru
                final PerformAudioPassThruResponse msg = new PerformAudioPassThruResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onPerformAudioPassThruResponse((PerformAudioPassThruResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onPerformAudioPassThruResponse((PerformAudioPassThruResponse)msg);       
                }
            } else if (functionName.equals(Names.EndAudioPassThru)) {
                // EndAudioPassThru
                final EndAudioPassThruResponse msg = new EndAudioPassThruResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onEndAudioPassThruResponse((EndAudioPassThruResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onEndAudioPassThruResponse((EndAudioPassThruResponse)msg);
                }
            } else if (functionName.equals(Names.SubscribeVehicleData)) {
            	try {
					Class<?> cls = Class.forName(Names.SubscribeVehicleDataExtend);
					Constructor con = cls.getConstructor(new Class[]{Hashtable.class});															
					final Object msg = con.newInstance(hash);	
					// SubscribeVehicleDataExtend
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onSubscribeVehicleDataResponse((SubscribeVehicleDataResponse)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onSubscribeVehicleDataResponse((SubscribeVehicleDataResponse)msg);   
	                }									
				}                 
            	catch(Exception e) {             	
            	// SubscribeVehicleData
	                final SubscribeVehicleDataResponse msg = new SubscribeVehicleDataResponse(hash);
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onSubscribeVehicleDataResponse((SubscribeVehicleDataResponse)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onSubscribeVehicleDataResponse((SubscribeVehicleDataResponse)msg);       
	                }
            	}
            } else if (functionName.equals(Names.UnsubscribeVehicleData)) {
            	try {
					Class<?> cls = Class.forName(Names.UnsubscribeVehicleDataExtend);
					Constructor con = cls.getConstructor(new Class[]{Hashtable.class});															
					final Object msg = con.newInstance(hash);	
					// UnsubscribeVehicleDataExtend
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onUnsubscribeVehicleDataResponse((UnsubscribeVehicleDataResponse)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onUnsubscribeVehicleDataResponse((UnsubscribeVehicleDataResponse)msg);   
	                }									
				}   
            	catch(Exception e) {                            	            
	            	// UnsubscribeVehicleData
	                final UnsubscribeVehicleDataResponse msg = new UnsubscribeVehicleDataResponse(hash);
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onUnsubscribeVehicleDataResponse((UnsubscribeVehicleDataResponse)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onUnsubscribeVehicleDataResponse((UnsubscribeVehicleDataResponse)msg);   
	                }
                }
            } else if (functionName.equals(Names.GetVehicleData)) {            	            	            	
            	try {
					Class<?> cls = Class.forName(Names.GetVehicleDataExtend);
					Constructor con = cls.getConstructor(new Class[]{Hashtable.class});															
					final Object msg = con.newInstance(hash);	
					// GetVehicleDataExtend
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onGetVehicleDataResponse((GetVehicleDataResponse)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onGetVehicleDataResponse((GetVehicleDataResponse)msg);   
	                }									
				}   
            	catch(Exception e) {
            		// GetVehicleData
                    final GetVehicleDataResponse msg = new GetVehicleDataResponse(hash);
                    if (_callbackToUIThread) {
                        // Run in UI thread
                        _mainUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                _proxyListener.onGetVehicleDataResponse((GetVehicleDataResponse)msg);
                            }
                        });
                    } else {
                        _proxyListener.onGetVehicleDataResponse((GetVehicleDataResponse)msg);   
                    }
			     }            	               
            } else if (functionName.equals(Names.ReadDID)) {
                // ReadDID
                final ReadDIDResponse msg = new ReadDIDResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onReadDIDResponse((ReadDIDResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onReadDIDResponse((ReadDIDResponse)msg); 
                }
            } else if (functionName.equals(Names.GetDTCs)) {
                // GetDTCs
                final GetDTCsResponse msg = new GetDTCsResponse(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            _proxyListener.onGetDTCsResponse((GetDTCsResponse)msg);
                        }
                    });
                } else {
                    _proxyListener.onGetDTCsResponse((GetDTCsResponse)msg); 
                }
            } else if (functionName.equals(Names.AlertManeuver)) {
				// AlertManeuver
				final AlertManeuverResponse msg = new AlertManeuverResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onAlertManeuverResponse((AlertManeuverResponse)msg);
						}
					});
				} else {
					_proxyListener.onAlertManeuverResponse((AlertManeuverResponse)msg);	
				}
			} else if (functionName.equals(Names.ShowConstantTBT)) {
				// ShowConstantTBT
				final ShowConstantTBTResponse msg = new ShowConstantTBTResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onShowConstantTBTResponse((ShowConstantTBTResponse)msg);
						}
					});
				} else {
					_proxyListener.onShowConstantTBTResponse((ShowConstantTBTResponse)msg);	
				}
			} else if (functionName.equals(Names.UpdateTurnList)) {
				// UpdateTurnList
				final UpdateTurnListResponse msg = new UpdateTurnListResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onUpdateTurnListResponse((UpdateTurnListResponse)msg);
						}
					});
				} else {
					_proxyListener.onUpdateTurnListResponse((UpdateTurnListResponse)msg);	
				}
			} else if (functionName.equals(Names.DialNumber)) {
				// DialNumber
				final DialNumberResponse msg = new DialNumberResponse(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onDialNumberResponse((DialNumberResponse)msg);
						}
					});
				} else {
					_proxyListener.onDialNumberResponse((DialNumberResponse)msg);	
				}
            }
		    /******************** Profile management *************************/
            else if (functionName.equals(Names.addProfile)) {
                final AddProfileResponse msg = new AddProfileResponse(hash);
                mProfileManagerCallbacksProxy.onAddProfileResponse(msg);
            } else if (functionName.equals(Names.removeProfile)) {
                final RemoveProfileResponse msg = new RemoveProfileResponse(hash);
                mProfileManagerCallbacksProxy.onRemoveProfileResponse(msg);
            } else if (functionName.equals(Names.loadProfile)) {
                final LoadProfileResponse msg = new LoadProfileResponse(hash);
                mProfileManagerCallbacksProxy.onLoadProfileReponse(msg);
            } else if (functionName.equals(Names.unloadProfile)) {
                final UnloadProfileResponse msg = new UnloadProfileResponse(hash);
                mProfileManagerCallbacksProxy.onUnloadProfileResponse(msg);
            } else if (functionName.equals(Names.sendAppToProfileMessage)) {
                final SendAppToProfileMessageResponse msg = new SendAppToProfileMessageResponse(hash);
                mProfileManagerCallbacksProxy.onSendMessageToProfileResponse(msg);
            } else if (functionName.equals(Names.appStateChanged)) {
                final AppStateChangedResponse msg = new AppStateChangedResponse(hash);
                mProfileManagerCallbacksProxy.onAppStateChangedResponse(msg);
			} else {
				if (_smartDeviceLinkMsgVersion != null) {
					DebugTool.logError("Unrecognized response Message: " + functionName.toString() + 
							"SMARTDEVICELINK Message Version = " + _smartDeviceLinkMsgVersion);
				} else {
					DebugTool.logError("Unrecognized response Message: " + functionName.toString());
				}
			} // end-if
		} else if (messageType.equals(Names.notification)) {
			SmartDeviceLinkTrace.logRPCEvent(InterfaceActivityDirection.Receive, new RPCNotification(rpcMsg), SMARTDEVICELINK_LIB_TRACE_KEY);
			if (functionName.equals(Names.OnHMIStatus)) {
				// OnHMIStatus
				
				final OnHMIStatus msg = new OnHMIStatus(hash);
				msg.setFirstRun(new Boolean(firstTimeFull));
				if (msg.getHmiLevel() == HMILevel.HMI_FULL) firstTimeFull = false;
				
				if (msg.getHmiLevel() != _priorHmiLevel && msg.getAudioStreamingState() != _priorAudioStreamingState) {
					if (_callbackToUIThread) {
						// Run in UI thread
						_mainUIHandler.post(new Runnable() {
							@Override
							public void run() {
								_proxyListener.onOnHMIStatus((OnHMIStatus)msg);
							}
						});
					} else {
						_proxyListener.onOnHMIStatus((OnHMIStatus)msg);
					}
				}				
			} else if (functionName.equals(Names.OnCommand)) {
				// OnCommand
				
				final OnCommand msg = new OnCommand(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnCommand((OnCommand)msg);
						}
					});
				} else {
					_proxyListener.onOnCommand((OnCommand)msg);
				}
			} else if (functionName.equals(Names.OnDriverDistraction)) {
				// OnDriverDistration
				
				final OnDriverDistraction msg = new OnDriverDistraction(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnDriverDistraction(msg);
						}
					});
				} else {
					_proxyListener.onOnDriverDistraction(msg);
				}
			} else if (functionName.equals(Names.OnEncodedSyncPData)) {
				// OnEncodedSyncPData;
				final OnEncodedSyncPData msg = new OnEncodedSyncPData(hash);


				Intent sendIntent = createBroadcastIntent();
				updateBroadcastIntent(sendIntent, "RPC_NAME", Names.OnEncodedSyncPData);							
				updateBroadcastIntent(sendIntent, "TYPE", Names.notification);
				
				// If url is null, then send notification to the app, otherwise, send to URL
				if (msg.getUrl() == null) {
					updateBroadcastIntent(sendIntent, "COMMENT1", "URL is a null value (received)");
					sendBroadcastIntent(sendIntent);					
					try
					{
						Class<?> cls = Class.forName(Names.ListenerExtend);
						if (cls.isInstance(_proxyListener))
						{
							final Object t = cls.cast(_proxyListener);
							final Method meth = cls.getDeclaredMethod(Names.onOnEncodedSyncPData, new Class[]{OnEncodedSyncPData.class});
							if (_callbackToUIThread) {
								// Run in UI thread
								_mainUIHandler.post(new Runnable() {
									@Override
									public void run() {
										try {
											meth.invoke(t, msg);
										}
										catch (Exception e)  {
											e.printStackTrace();
										}
									}
								});
							} else {
								meth.invoke(t, msg);
							}
						}
					}
					catch (Exception e)
					{
						//private lib not present
					}
				} else {
					updateBroadcastIntent(sendIntent, "COMMENT1", "Sending smartDeviceLinkp to cloud: " + msg.getUrl());
					sendBroadcastIntent(sendIntent);				
					
					// URL has data, attempt to post request to external server
					Thread handleOffboardSmartDeviceLinkTransmissionTread = new Thread() {
						@Override
						public void run() {
							sendEncodedSyncPDataToUrl(msg.getUrl(), msg.getData(), msg.getTimeout());
						}
					};

					handleOffboardSmartDeviceLinkTransmissionTread.start();
				}
			} else if (functionName.equals(Names.OnSyncPData)) {
				// OnSyncPData
				final OnSyncPData msg = new OnSyncPData(hash);
				Intent sendIntent = createBroadcastIntent();
				updateBroadcastIntent(sendIntent, "RPC_NAME", Names.OnSyncPData);
				updateBroadcastIntent(sendIntent, "TYPE", Names.notification);
				
				// If url is null, then send notification to the app, otherwise, send to URL
				if (msg.getUrl() == null) {	
					updateBroadcastIntent(sendIntent, "COMMENT1", "URL is a null value (received)");
					sendBroadcastIntent(sendIntent);
					try
					{
						Class<?> cls = Class.forName(Names.ListenerExtend);
						if (cls.isInstance(_proxyListener))
						{
							final Object t = cls.cast(_proxyListener);
							final Method meth = cls.getDeclaredMethod(Names.onOnSyncPData, new Class[]{OnSyncPData.class});
							if (_callbackToUIThread) {
								// Run in UI thread
								_mainUIHandler.post(new Runnable() {
									@Override
									public void run() {
										try {
											meth.invoke(t, msg);
										}
										catch (Exception e)  {
											e.printStackTrace();
										}
									}
								});
							} else {
								meth.invoke(t, msg);
							}
						}
					}
					catch (Exception e)
					{
						//private lib not present
					}
				} else { //url not null, send to url
					updateBroadcastIntent(sendIntent, "COMMENT1", "Sending smartDeviceLinkp to cloud: " + msg.getUrl());
					sendBroadcastIntent(sendIntent);				
					Log.i("pt", "send smartDeviceLinkp to url");
					// URL has data, attempt to post request to external server
					Thread handleOffboardSmartDeviceLinkTransmissionTread = new Thread() {
						@Override
						public void run() {
							sendSyncPDataToUrl(msg.getUrl(), msg.getSyncPData(), msg.getTimeout());
						}
					};

					handleOffboardSmartDeviceLinkTransmissionTread.start();
				}
			} else if (functionName.equals(Names.OnPermissionsChange)) {
				//OnPermissionsChange
				
				final OnPermissionsChange msg = new OnPermissionsChange(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnPermissionsChange(msg);
						}
					});
				} else {
					_proxyListener.onOnPermissionsChange(msg);
				}
			} else if (functionName.equals(Names.OnTBTClientState)) {
				// OnTBTClientState
				
				final OnTBTClientState msg = new OnTBTClientState(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnTBTClientState(msg);
						}
					});
				} else {
					_proxyListener.onOnTBTClientState(msg);
				}
			} else if (functionName.equals(Names.OnButtonPress)) {
				// OnButtonPress
				
				final OnButtonPress msg = new OnButtonPress(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnButtonPress((OnButtonPress)msg);
						}
					});
				} else {
					_proxyListener.onOnButtonPress((OnButtonPress)msg);
				}
			} else if (functionName.equals(Names.OnButtonEvent)) {
				// OnButtonEvent
				
				final OnButtonEvent msg = new OnButtonEvent(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnButtonEvent((OnButtonEvent)msg);
						}
					});
				} else {
					_proxyListener.onOnButtonEvent((OnButtonEvent)msg);
				}
			} else if (functionName.equals(Names.OnLanguageChange)) {
				// OnLanguageChange
				
				final OnLanguageChange msg = new OnLanguageChange(hash);
				if (_callbackToUIThread) {
					// Run in UI thread
					_mainUIHandler.post(new Runnable() {
						@Override
						public void run() {
							_proxyListener.onOnLanguageChange((OnLanguageChange)msg);
						}
					});
				} else {
					_proxyListener.onOnLanguageChange((OnLanguageChange)msg);
				}
			} else if (functionName.equals(Names.OnAudioPassThru)) {				
				// OnAudioPassThru
				final OnAudioPassThru msg = new OnAudioPassThru(hash);
                if (_callbackToUIThread) {
                    // Run in UI thread
                    _mainUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
    						_proxyListener.onOnAudioPassThru((OnAudioPassThru)msg);
                        }
                    });
                } else {
					_proxyListener.onOnAudioPassThru((OnAudioPassThru)msg);
                }				
			} else if (functionName.equals(Names.OnVehicleData)) {               
            	try {
					Class<?> cls = Class.forName(Names.OnVehicleDataExtend);
					Constructor con = cls.getConstructor(new Class[]{Hashtable.class});															
					final Object msg = con.newInstance(hash);	
					// OnVehicleDataExtend
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onOnVehicleData((OnVehicleData)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onOnVehicleData((OnVehicleData)msg);   
	                }									
				}   				
            	catch(Exception e) {
					// OnVehicleData
	                final OnVehicleData msg = new OnVehicleData(hash);
	                if (_callbackToUIThread) {
	                    // Run in UI thread
	                    _mainUIHandler.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            _proxyListener.onOnVehicleData((OnVehicleData)msg);
	                        }
	                    });
	                } else {
	                    _proxyListener.onOnVehicleData((OnVehicleData)msg);
	                }
				} 
			}
			else if (functionName.equals(Names.OnAppInterfaceUnregistered)) {
				// OnAppInterfaceUnregistered
				
				_appInterfaceRegisterd = false;
				synchronized(APP_INTERFACE_REGISTERED_LOCK) {
					APP_INTERFACE_REGISTERED_LOCK.notify();
				}
				
				final OnAppInterfaceUnregistered msg = new OnAppInterfaceUnregistered(hash);
								
				if (_advancedLifecycleManagementEnabled) {
					// This requires the proxy to be cycled
                    if (this.getCurrentTransportType() == TransportType.BLUETOOTH) {
                        cycleProxy(SmartDeviceLinkDisconnectedReason.convertAppInterfaceUnregisteredReason(msg.getReason()));
                    } else {
                        Log.e(this.getClass().getName(), "HandleRPCMessage. No cycle required if transport is TCP");
                    }
                } else {
					if (_callbackToUIThread) {
						// Run in UI thread
						_mainUIHandler.post(new Runnable() {
							@Override
							public void run() {
								((IProxyListener)_proxyListener).onOnAppInterfaceUnregistered(msg);
							}
						});
					} else {
						((IProxyListener)_proxyListener).onOnAppInterfaceUnregistered(msg);
					}
					
					notifyProxyClosed("OnAppInterfaceUnregistered", null);
				}
            }
		    /******************** Profile management *************************/
            else if (functionName.equals(Names.onProfileClosed)) {
                final OnProfileClosed notification = new OnProfileClosed(hash);
                mProfileManagerCallbacksProxy.onProfileClosed(notification);
            } else if (functionName.equals(Names.onReceiveMessageFromProfile)) {
                final OnSendProfileToAppMessage notification = new OnSendProfileToAppMessage(hash);
                mProfileManagerCallbacksProxy.onReceiveMessageFromProfile(notification);
			} else {
				if (_smartDeviceLinkMsgVersion != null) {
					DebugTool.logInfo("Unrecognized notification Message: " + functionName.toString() + 
							" connected to SMARTDEVICELINK using message version: " + _smartDeviceLinkMsgVersion.getMajorVersion() + "." + _smartDeviceLinkMsgVersion.getMinorVersion());
				} else {
					DebugTool.logInfo("Unrecognized notification Message: " + functionName.toString());
				}
			} // end-if
		} // end-if notification
		
		SmartDeviceLinkTrace.logProxyEvent("Proxy received RPC Message: " + functionName, SMARTDEVICELINK_LIB_TRACE_KEY);
	}
	
	/**
	 * Takes an RPCRequest and sends it to SMARTDEVICELINK.  Responses are captured through callback on IProxyListener.  
	 * 
	 * @param request
	 * @throws SmartDeviceLinkException
	 */
	public void sendRPCRequest(RPCRequest request) throws SmartDeviceLinkException {
		if (_proxyDisposed) {
			throw new SmartDeviceLinkException("This object has been disposed, it is no long capable of executing methods.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_DISPOSED);
		}
		
		// Test if request is null
		if (request == null) {
			SmartDeviceLinkTrace.logProxyEvent("Application called sendRPCRequest method with a null RPCRequest.", SMARTDEVICELINK_LIB_TRACE_KEY);
			throw new IllegalArgumentException("sendRPCRequest cannot be called with a null request.");
		}
		
		SmartDeviceLinkTrace.logProxyEvent("Application called sendRPCRequest method for RPCRequest: ." + request.getFunctionName(), SMARTDEVICELINK_LIB_TRACE_KEY);
			
		// Test if smartDeviceLinkConnection is null
		synchronized(CONNECTION_REFERENCE_LOCK) {
			if (_smartDeviceLinkConnection == null || !_smartDeviceLinkConnection.getIsConnected()) {
				SmartDeviceLinkTrace.logProxyEvent("Application attempted to send and RPCRequest without a connected transport.", SMARTDEVICELINK_LIB_TRACE_KEY);
				throw new SmartDeviceLinkException("There is no valid connection to SMARTDEVICELINK. sendRPCRequest cannot be called until SMARTDEVICELINK has been connected.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
			}
		}
		
		// Test for illegal correlation ID
		if (isCorrelationIDProtected(request.getCorrelationID())) {
			
			SmartDeviceLinkTrace.logProxyEvent("Application attempted to use the reserved correlation ID, " + request.getCorrelationID(), SMARTDEVICELINK_LIB_TRACE_KEY);
			throw new SmartDeviceLinkException("Invalid correlation ID. The correlation ID, " + request.getCorrelationID()
					+ " , is a reserved correlation ID.", SmartDeviceLinkExceptionCause.RESERVED_CORRELATION_ID);
		}
		
		// Throw exception if RPCRequest is sent when SMARTDEVICELINK is unavailable 
		if (!_appInterfaceRegisterd && request.getFunctionName() != Names.RegisterAppInterface) {
			
			SmartDeviceLinkTrace.logProxyEvent("Application attempted to send an RPCRequest (non-registerAppInterface), before the interface was registerd.", SMARTDEVICELINK_LIB_TRACE_KEY);
			throw new SmartDeviceLinkException("SMARTDEVICELINK is currently unavailable. RPC Requests cannot be sent.", SmartDeviceLinkExceptionCause.SMARTDEVICELINK_UNAVAILALBE);
		}
				
		if (_advancedLifecycleManagementEnabled) {
			if (		   request.getFunctionName() == Names.RegisterAppInterface
					|| request.getFunctionName() == Names.UnregisterAppInterface) {
				
				SmartDeviceLinkTrace.logProxyEvent("Application attempted to send a RegisterAppInterface or UnregisterAppInterface while using ALM.", SMARTDEVICELINK_LIB_TRACE_KEY);
				throw new SmartDeviceLinkException("The RPCRequest, " + request.getFunctionName() + 
						", is unnallowed using the Advanced Lifecycle Management Model.", SmartDeviceLinkExceptionCause.INCORRECT_LIFECYCLE_MODEL);
			}
		}
		
		sendRPCRequestPrivate(request);
	} // end-method
	
	public void sendRPCRequest(RPCMessage request) throws SmartDeviceLinkException {
		sendRPCRequest((RPCRequest) request);
	}
	
	protected void notifyProxyClosed(final String info, final Exception e) {		
		SmartDeviceLinkTrace.logProxyEvent("NotifyProxyClose", SMARTDEVICELINK_LIB_TRACE_KEY);
		
		OnProxyClosed message = new OnProxyClosed(info, e);
		queueInternalMessage(message);
	}

	private void passErrorToProxyListener(final String info, final Exception e) {
				
		OnError message = new OnError(info, e);
		queueInternalMessage(message);
	}
	
	private void startRPCProtocolSession(byte sessionID, String correlationID) {
		_rpcSessionID = sessionID;
		
		// Set Proxy Lifecyclek Available
		if (_advancedLifecycleManagementEnabled) {
			
			try {
				registerAppInterfacePrivate(
						_smartDeviceLinkMsgVersionRequest,
						_applicationName,
						_ttsName,
						_ngnMediaScreenAppName,
						_vrSynonyms,
						_isMediaApp, 
						_smartDeviceLinkLanguageDesired,
						_hmiDisplayLanguageDesired,
						_appType,
						_appID,
						_autoActivateIdDesired,
						REGISTER_APP_INTERFACE_CORRELATION_ID);
				
			} catch (Exception e) {
				notifyProxyClosed("Failed to register application interface with SMARTDEVICELINK. Check parameter values given to SmartDeviceLinkProxy constructor.", e);
			}
		} else {
			InternalProxyMessage message = new InternalProxyMessage(Names.OnProxyOpened);
			queueInternalMessage(message);
		}
	}
	
	// Queue internal callback message
	private void queueInternalMessage(InternalProxyMessage message) {
		synchronized(INTERNAL_MESSAGE_QUEUE_THREAD_LOCK) {
			if (_internalProxyMessageDispatcher != null) {
				_internalProxyMessageDispatcher.queueMessage(message);
			}
		}
	}
	
	// Queue incoming ProtocolMessage
	private void queueIncomingMessage(ProtocolMessage message) {
		synchronized(INCOMING_MESSAGE_QUEUE_THREAD_LOCK) {
			if (_incomingProxyMessageDispatcher != null) {
				_incomingProxyMessageDispatcher.queueMessage(message);
			}
		}
	}
	
	public void setAppService(Service mService)
	{
		_appService = mService;
	}

	/******************** Public Helper Methods *************************/
	
	/*Begin V1 Enhanced helper*/
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText  -Menu text for optional sub value containing menu parameters.
	 *@param parentID  -Menu parent ID for optional sub value containing menu parameters.
	 *@param position  -Menu position for optional sub value containing menu parameters.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 *@param IconType -Describes whether the image is static or dynamic
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer parentID, Integer position,
			Vector<String> vrCommands, String IconValue, ImageType IconType, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		AddCommand msg = RPCRequestFactory.buildAddCommand(commandID, menuText, parentID, position,
			vrCommands, IconValue, IconType, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param position -Menu position for optional sub value containing menu parameters.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 *@param IconType -Describes whether the image is static or dynamic
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer position,
			Vector<String> vrCommands, String IconValue, ImageType IconType, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, position, vrCommands, IconValue, IconType, correlationID);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param position -Menu position for optional sub value containing menu parameters.
	 *@param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 *@param IconType -Describes whether the image is static or dynamic
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer position, String IconValue, ImageType IconType,
			Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, position, null, IconValue, IconType, correlationID);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 *@param IconType -Describes whether the image is static or dynamic
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, String IconValue, ImageType IconType, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, null, null, IconValue, IconType, correlationID);
	}
	
	/**
	 * Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param commandID -Unique command ID of the command to add.
	 * @param menuText -Menu text for optional sub value containing menu parameters.
	 * @param vrCommands -VR synonyms for this AddCommand.
	 * @param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 * @param IconType -Describes whether the image is static or dynamic
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Vector<String> vrCommands, String IconValue, ImageType IconType, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, null, vrCommands, IconValue, IconType, correlationID);
	}
	
	/**
	 * Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param commandID -Unique command ID of the command to add.
	 * @param vrCommands -VR synonyms for this AddCommand.
	 * @param IconValue -A static hex icon value or the binary image file name identifier (sent by the PutFile RPC).
	 * @param IconType -Describes whether the image is static or dynamic
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			Vector<String> vrCommands, String IconValue, ImageType IconType, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, null, null, null, vrCommands, IconValue, IconType, correlationID);
	}

	/*End V1 Enhanced helper*/
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param parentID  -Menu parent ID for optional sub value containing menu parameters.
	 *@param position  -Menu position for optional sub value containing menu parameters.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer parentID, Integer position,
			Vector<String> vrCommands, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		AddCommand msg = RPCRequestFactory.buildAddCommand(commandID, menuText, parentID, position,
			vrCommands, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param position  -Menu position for optional sub value containing menu parameters.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer position,
			Vector<String> vrCommands, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, position, vrCommands, correlationID);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param position  -Menu position for optional sub value containing menu parameters.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer position,
			Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, position, null, correlationID);
	}
	
	/**
	 *Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 *
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Integer correlationID) 
			throws SmartDeviceLinkException {
		Vector<String> vrCommands = null;
		
		addCommand(commandID, menuText, null, null, vrCommands, correlationID);
	}
	
	/**
	 * Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 *@param commandID -Unique command ID of the command to add.
	 *@param menuText -Menu text for optional sub value containing menu parameters.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			String menuText, Vector<String> vrCommands, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, menuText, null, null, vrCommands, correlationID);
	}
	
	/**
	 * Sends an AddCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 *@param commandID -Unique command ID of the command to add.
	 *@param vrCommands -VR synonyms for this AddCommand.
	 *@param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 *@throws SmartDeviceLinkException
	 */
	public void addCommand(Integer commandID,
			Vector<String> vrCommands, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		addCommand(commandID, null, null, null, vrCommands, correlationID);
	}
		
	
	/**
	 * Sends an AddSubMenu RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param menuID -Unique ID of the sub menu to add.
	 * @param menuName -Text to show in the menu for this sub menu.
	 * @param position -Position within the items that are are at top level of the in application menu.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void addSubMenu(Integer menuID, String menuName,
			Integer position, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		AddSubMenu msg = RPCRequestFactory.buildAddSubMenu(menuID, menuName,
				position, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends an AddSubMenu RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param menuID -Unique ID of the sub menu to add.
	 * @param menuName -Text to show in the menu for this sub menu.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void addSubMenu(Integer menuID, String menuName,
			Integer correlationID) throws SmartDeviceLinkException {
		
		addSubMenu(menuID, menuName, null, correlationID);
	}
	
	/**
	 * Sends an EncodedData RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param data -Contains base64 encoded string of SmartDeviceLinkP packets.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void encodedSyncPData(Vector<String> data, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		Log.i("pt", "encodedSyncPData() giving to smartDeviceLink");
		EncodedSyncPData msg = RPCRequestFactory.buildEncodedSyncPData(data, correlationID);
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a Data RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param data 
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void SyncPData(byte[] data, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		Log.i("pt", "SyncPData() giving to smartDeviceLink");
		SyncPData msg = RPCRequestFactory.buildSyncPData(data, correlationID);
		sendRPCRequest(msg);
	}

	/*Begin V1 Enhanced helper*/	
	
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText -The text to speech message in the form of a string.
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param alertText3 -The optional third line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param softButtons -A list of App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String ttsText, String alertText1,
			String alertText2, String alertText3, Boolean playTone, Integer duration, Vector<SoftButton> softButtons,
			Integer correlationID) throws SmartDeviceLinkException {

		Alert msg = RPCRequestFactory.buildAlert(ttsText, alertText1, alertText2, alertText3, playTone, duration, softButtons, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsChunks -Text/phonemes to speak in the form of ttsChunks.
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param alertText3 -The optional third line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param softButtons -A list of App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(Vector<TTSChunk> ttsChunks,
			String alertText1, String alertText2, String alertText3, Boolean playTone,
			Integer duration, Vector<SoftButton> softButtons, Integer correlationID) throws SmartDeviceLinkException {
		
		Alert msg = RPCRequestFactory.buildAlert(ttsChunks, alertText1, alertText2, alertText3, playTone, duration, softButtons, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText -The text to speech message in the form of a string.
	 * @param playTone -Defines if tone should be played.
	 * @param softButtons -A list of App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String ttsText, Boolean playTone, Vector<SoftButton> softButtons,
			Integer correlationID) throws SmartDeviceLinkException {
		
		alert(ttsText, null, null, null, playTone, null, softButtons, correlationID);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param chunks -A list of text/phonemes to speak in the form of ttsChunks.
	 * @param playTone -Defines if tone should be played.
	 * @param softButtons -A list of App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(Vector<TTSChunk> chunks, Boolean playTone, Vector<SoftButton> softButtons,
			Integer correlationID) throws SmartDeviceLinkException {
		
		alert(chunks, null, null, null, playTone, null, softButtons, correlationID);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param alertText3 -The optional third line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param softButtons -A list of App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String alertText1, String alertText2, String alertText3,
			Boolean playTone, Integer duration, Vector<SoftButton> softButtons, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		alert((Vector<TTSChunk>)null, alertText1, alertText2, alertText3, playTone, duration, softButtons, correlationID);
	}
		
	/*End V1 Enhanced helper*/
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText -The text to speech message in the form of a string.
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String ttsText, String alertText1,
			String alertText2, Boolean playTone, Integer duration,
			Integer correlationID) throws SmartDeviceLinkException {

		Alert msg = RPCRequestFactory.buildAlert(ttsText, alertText1, alertText2, 
				playTone, duration, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsChunks -A list of text/phonemes to speak in the form of ttsChunks.
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(Vector<TTSChunk> ttsChunks,
			String alertText1, String alertText2, Boolean playTone,
			Integer duration, Integer correlationID) throws SmartDeviceLinkException {
		
		Alert msg = RPCRequestFactory.buildAlert(ttsChunks, alertText1, alertText2, playTone,
				duration, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText -The text to speech message in the form of a string.
	 * @param playTone -Defines if tone should be played.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String ttsText, Boolean playTone,
			Integer correlationID) throws SmartDeviceLinkException {
		
		alert(ttsText, null, null, playTone, null, correlationID);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param chunks -A list of text/phonemes to speak in the form of ttsChunks.
	 * @param playTone -Defines if tone should be played.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(Vector<TTSChunk> chunks, Boolean playTone,
			Integer correlationID) throws SmartDeviceLinkException {
		
		alert(chunks, null, null, playTone, null, correlationID);
	}
	
	/**
	 * Sends an Alert RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param alertText1 -The first line of the alert text field.
	 * @param alertText2 -The second line of the alert text field.
	 * @param playTone -Defines if tone should be played.
	 * @param duration -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void alert(String alertText1, String alertText2,
			Boolean playTone, Integer duration, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		alert((Vector<TTSChunk>)null, alertText1, alertText2, playTone, duration, correlationID);
	}
	
	/**
	 * Sends a CreateInteractionChoiceSet RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param choiceSet
	 * @param interactionChoiceSetID
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void createInteractionChoiceSet(
			Vector<Choice> choiceSet, Integer interactionChoiceSetID,
			Integer correlationID) throws SmartDeviceLinkException {
		
		CreateInteractionChoiceSet msg = RPCRequestFactory.buildCreateInteractionChoiceSet(
				choiceSet, interactionChoiceSetID, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a DeleteCommand RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param commandID -ID of the command(s) to delete.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void deleteCommand(Integer commandID,
			Integer correlationID) throws SmartDeviceLinkException {
		
		DeleteCommand msg = RPCRequestFactory.buildDeleteCommand(commandID, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a DeleteInteractionChoiceSet RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param interactionChoiceSetID -ID of the interaction choice set to delete.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void deleteInteractionChoiceSet(
			Integer interactionChoiceSetID, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		DeleteInteractionChoiceSet msg = RPCRequestFactory.buildDeleteInteractionChoiceSet(
				interactionChoiceSetID, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a DeleteSubMenu RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param menuID -The menuID of the submenu to delete.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse
	 * @throws SmartDeviceLinkException
	 */
	public void deleteSubMenu(Integer menuID,
			Integer correlationID) throws SmartDeviceLinkException {
		
		DeleteSubMenu msg = RPCRequestFactory.buildDeleteSubMenu(menuID, correlationID);

		sendRPCRequest(msg);
	}
	
	
	
	/*Begin V1 Enhanced helper*/
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetID -Interaction choice set IDs to use with an interaction.
	 * @param vrHelp -Suggested VR Help Items to display on-screen during Perform Interaction.      	
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Integer interactionChoiceSetID, Vector<VrHelpItem> vrHelp,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(initPrompt,
				displayText, interactionChoiceSetID, vrHelp, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetID -Interaction choice set IDs to use with an interaction.
	 * @param helpPrompt -Help text that is spoken when a user speaks "help" during the interaction.
	 * @param timeoutPrompt -Timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param vrHelp -Suggested VR Help Items to display on-screen during Perform Interaction.      	
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Integer interactionChoiceSetID,
			String helpPrompt, String timeoutPrompt,
			InteractionMode interactionMode, Integer timeout, Vector<VrHelpItem> vrHelp,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(
				initPrompt, displayText, interactionChoiceSetID,
				helpPrompt, timeoutPrompt, interactionMode, 
				timeout, vrHelp, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetIDList -A list of interaction choice set IDs to use with an interaction.
	 * @param helpPrompt -Help text that is spoken when a user speaks "help" during the interaction.
	 * @param timeoutPrompt -Timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param vrHelp -Suggested VR Help Items to display on-screen during Perform Interaction.      	
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Vector<Integer> interactionChoiceSetIDList,
			String helpPrompt, String timeoutPrompt,
			InteractionMode interactionMode, Integer timeout, Vector<VrHelpItem> vrHelp,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(initPrompt,
				displayText, interactionChoiceSetIDList,
				helpPrompt, timeoutPrompt, interactionMode, timeout, vrHelp,
				correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initChunks -A list of text/phonemes to speak for the initial prompt in the form of ttsChunks.
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetIDList -A list of interaction choice set IDs to use with an interaction.
	 * @param helpChunks -A list of text/phonemes to speak for the help text that is spoken when a user speaks "help" during the interaction.
	 * @param timeoutChunks A list of text/phonems to speak for the timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param vrHelp -Suggested VR Help Items to display on-screen during Perform Interaction.      	
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(
			Vector<TTSChunk> initChunks, String displayText,
			Vector<Integer> interactionChoiceSetIDList,
			Vector<TTSChunk> helpChunks, Vector<TTSChunk> timeoutChunks,
			InteractionMode interactionMode, Integer timeout, Vector<VrHelpItem> vrHelp,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(
				initChunks, displayText, interactionChoiceSetIDList,
				helpChunks, timeoutChunks, interactionMode, timeout,vrHelp,
				correlationID);
		
		sendRPCRequest(msg);
	}
	
	/*End V1 Enhanced*/
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetID -Interaction choice set IDs to use with an interaction.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Integer interactionChoiceSetID,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(initPrompt,
				displayText, interactionChoiceSetID, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetID -Interaction choice set IDs to use with an interaction.
	 * @param helpPrompt -Help text that is spoken when a user speaks "help" during the interaction. 
	 * @param timeoutPrompt -Timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Integer interactionChoiceSetID,
			String helpPrompt, String timeoutPrompt,
			InteractionMode interactionMode, Integer timeout,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(
				initPrompt, displayText, interactionChoiceSetID,
				helpPrompt, timeoutPrompt, interactionMode, 
				timeout, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initPrompt -Intial prompt spoken to the user at the start of an interaction.      	
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetIDList -A list of interaction choice set IDs to use with an interaction.
	 * @param helpPrompt -Help text that is spoken when a user speaks "help" during the interaction. 
	 * @param timeoutPrompt -Timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(String initPrompt,
			String displayText, Vector<Integer> interactionChoiceSetIDList,
			String helpPrompt, String timeoutPrompt,
			InteractionMode interactionMode, Integer timeout,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(initPrompt,
				displayText, interactionChoiceSetIDList,
				helpPrompt, timeoutPrompt, interactionMode, timeout,
				correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a PerformInteraction RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initChunks -A list of text/phonemes to speak for the initial prompt in the form of ttsChunks.
	 * @param displayText -Text to be displayed first.
	 * @param interactionChoiceSetIDList -A list of interaction choice set IDs to use with an interaction.
	 * @param helpChunks -A list of text/phonemes to speak for the help text that is spoken when a user speaks "help" during the interaction.
	 * @param timeoutChunks A list of text/phonems to speak for the timeout text that is spoken when a VR interaction times out.
	 * @param interactionMode - The method in which the user is notified and uses the interaction (Manual,VR,Both).
	 * @param timeout -Timeout in milliseconds.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void performInteraction(
			Vector<TTSChunk> initChunks, String displayText,
			Vector<Integer> interactionChoiceSetIDList,
			Vector<TTSChunk> helpChunks, Vector<TTSChunk> timeoutChunks,
			InteractionMode interactionMode, Integer timeout,
			Integer correlationID) throws SmartDeviceLinkException {
		
		PerformInteraction msg = RPCRequestFactory.buildPerformInteraction(
				initChunks, displayText, interactionChoiceSetIDList,
				helpChunks, timeoutChunks, interactionMode, timeout,
				correlationID);
		
		sendRPCRequest(msg);
	}
	
	// Protected registerAppInterface used to ensure only non-ALM applications call
	// reqisterAppInterface
	protected void registerAppInterfacePrivate(
			smartdevicelinkMsgVersion smartDeviceLinkMsgVersion, String appName, Vector<TTSChunk> ttsName,
			String ngnMediaScreenAppName, Vector<String> vrSynonyms, Boolean isMediaApp, 
			Language languageDesired, Language hmiDisplayLanguageDesired, Vector<AppHMIType> appType,
			String appID, String autoActivateID, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		RegisterAppInterface msg = RPCRequestFactory.buildRegisterAppInterface(
				smartDeviceLinkMsgVersion, appName, ttsName, ngnMediaScreenAppName, vrSynonyms, isMediaApp, 
				languageDesired, hmiDisplayLanguageDesired, appType, appID, correlationID);

		sendRPCRequestPrivate(msg);
	}
	
	/*Begin V1 Enhanced helper function*/

	/**
	 * Sends a SetGlobalProperties RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param helpPrompt
	 * @param timeoutPrompt
	 * @param vrHelpTitle
	 * @param vrHelp
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void setGlobalProperties(
			String helpPrompt, String timeoutPrompt, String vrHelpTitle, Vector<VrHelpItem> vrHelp, Integer correlationID) 
		throws SmartDeviceLinkException {
		
		SetGlobalProperties req = RPCRequestFactory.buildSetGlobalProperties(helpPrompt, 
				timeoutPrompt, vrHelpTitle, vrHelp, correlationID);
		
		sendRPCRequest(req);
	}
	
	/**
	 * Sends a SetGlobalProperties RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param helpChunks
	 * @param timeoutChunks
	 * @param vrHelpTitle
	 * @param vrHelp
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void setGlobalProperties(
			Vector<TTSChunk> helpChunks, Vector<TTSChunk> timeoutChunks, String vrHelpTitle, Vector<VrHelpItem> vrHelp,
			Integer correlationID) throws SmartDeviceLinkException {
		
		SetGlobalProperties req = RPCRequestFactory.buildSetGlobalProperties(
				helpChunks, timeoutChunks, vrHelpTitle, vrHelp, correlationID);

		sendRPCRequest(req);
	}

	/*End V1 Enhanced helper function*/	
	
	/**
	 * Sends a SetGlobalProperties RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param helpPrompt
	 * @param timeoutPrompt
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void setGlobalProperties(
			String helpPrompt, String timeoutPrompt, Integer correlationID) 
		throws SmartDeviceLinkException {
		
		SetGlobalProperties req = RPCRequestFactory.buildSetGlobalProperties(helpPrompt, 
				timeoutPrompt, correlationID);
		
		sendRPCRequest(req);
	}
	
	/**
	 * Sends a SetGlobalProperties RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param helpChunks
	 * @param timeoutChunks
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void setGlobalProperties(
			Vector<TTSChunk> helpChunks, Vector<TTSChunk> timeoutChunks,
			Integer correlationID) throws SmartDeviceLinkException {
		
		SetGlobalProperties req = RPCRequestFactory.buildSetGlobalProperties(
				helpChunks, timeoutChunks, correlationID);

		sendRPCRequest(req);
	}
	
	public void resetGlobalProperties(Vector<GlobalProperty> properties,
			Integer correlationID) throws SmartDeviceLinkException {
		
		ResetGlobalProperties req = new ResetGlobalProperties();
		
		req.setCorrelationID(correlationID);
		req.setProperties(properties);
		
		sendRPCRequest(req);
	}
	                                                        
	
	/**
	 * Sends a SetMediaClockTimer RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param updateMode
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void setMediaClockTimer(Integer hours,
			Integer minutes, Integer seconds, UpdateMode updateMode,
			Integer correlationID) throws SmartDeviceLinkException {

		SetMediaClockTimer msg = RPCRequestFactory.buildSetMediaClockTimer(hours,
				minutes, seconds, updateMode, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Pauses the media clock. Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void pauseMediaClockTimer(Integer correlationID) 
			throws SmartDeviceLinkException {

		SetMediaClockTimer msg = RPCRequestFactory.buildSetMediaClockTimer(0,
				0, 0, UpdateMode.PAUSE, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Resumes the media clock. Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void resumeMediaClockTimer(Integer correlationID) 
			throws SmartDeviceLinkException {

		SetMediaClockTimer msg = RPCRequestFactory.buildSetMediaClockTimer(0,
				0, 0, UpdateMode.RESUME, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Clears the media clock. Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	 */
	public void clearMediaClockTimer(Integer correlationID) 
			throws SmartDeviceLinkException {

		Show msg = RPCRequestFactory.buildShow(null, null, null, "     ", null, null, correlationID);

		sendRPCRequest(msg);
	}
		
	/*Begin V1 Enhanced helper*/
	/**
	 * Sends a Show RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param mainText1 -Text displayed in a single or upper display line.
	 * @param mainText2 -Text displayed on the second display line.
	 * @param mainText3 -Text displayed on the second "page" first display line.
	 * @param mainText4 -Text displayed on the second "page" second display line.
	 * @param statusBar
	 * @param mediaClock -Text value for MediaClock field.
	 * @param mediaTrack -Text displayed in the track field.
	 * @param graphic -Image struct determining whether static or dynamic image to display in app.
	 * @param softButtons -App defined SoftButtons.
	 * @param customPresets -App labeled on-screen presets.
	 * @param alignment -Specifies how mainText1 and mainText2s texts should be aligned on display.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void show(String mainText1, String mainText2, String mainText3, String mainText4,
			String statusBar, String mediaClock, String mediaTrack,
			Image graphic, Vector<SoftButton> softButtons, Vector <String> customPresets,
			TextAlignment alignment, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		Show msg = RPCRequestFactory.buildShow(mainText1, mainText2, mainText3, mainText4,
				statusBar, mediaClock, mediaTrack, graphic, softButtons, customPresets,
				alignment, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a Show RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param mainText1 -Text displayed in a single or upper display line.
	 * @param mainText2 -Text displayed on the second display line.
	 * @param mainText3 -Text displayed on the second "page" first display line.
	 * @param mainText4 -Text displayed on the second "page" second display line.
	 * @param graphic -Image struct determining whether static or dynamic image to display in app.
	 * @param softButtons -App defined SoftButtons.
	 * @param customPresets -App labeled on-screen presets.
	 * @param alignment -Specifies how mainText1 and mainText2s texts should be aligned on display.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void show(String mainText1, String mainText2, String mainText3, String mainText4,
			Image graphic, Vector<SoftButton> softButtons, Vector <String> customPresets,
			TextAlignment alignment, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		show(mainText1, mainText2, mainText3, mainText4, null, null, null, graphic, softButtons, customPresets, alignment, correlationID);
	}		
	/*End V1 Enhanced helper*/
	
	/**
	 * Sends a Show RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param mainText1 -Text displayed in a single or upper display line.
	 * @param mainText2 -Text displayed on the second display line.
	 * @param statusBar
	 * @param mediaClock -Text value for MediaClock field.
	 * @param mediaTrack -Text displayed in the track field.
	 * @param alignment -Specifies how mainText1 and mainText2s texts should be aligned on display.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void show(String mainText1, String mainText2,
			String statusBar, String mediaClock, String mediaTrack,
			TextAlignment alignment, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		Show msg = RPCRequestFactory.buildShow(mainText1, mainText2,
				statusBar, mediaClock, mediaTrack,
				alignment, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a Show RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param mainText1 -Text displayed in a single or upper display line.
	 * @param mainText2 -Text displayed on the second display line.
	 * @param alignment -Specifies how mainText1 and mainText2s texts should be aligned on display.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void show(String mainText1, String mainText2,
			TextAlignment alignment, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		show(mainText1, mainText2, null, null, null, alignment, correlationID);
	}
	
	/**
	 * Sends a Speak RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText -The text to speech message in the form of a string.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void speak(String ttsText, Integer correlationID) 
			throws SmartDeviceLinkException {
		
		Speak msg = RPCRequestFactory.buildSpeak(TTSChunkFactory.createSimpleTTSChunks(ttsText),
				correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a Speak RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsChunks -Text/phonemes to speak in the form of ttsChunks.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void speak(Vector<TTSChunk> ttsChunks,
			Integer correlationID) throws SmartDeviceLinkException {

		Speak msg = RPCRequestFactory.buildSpeak(ttsChunks, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Sends a SubscribeButton RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param buttonName -Name of the button to subscribe.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void subscribeButton(ButtonName buttonName,
			Integer correlationID) throws SmartDeviceLinkException {

		SubscribeButton msg = RPCRequestFactory.buildSubscribeButton(buttonName,
				correlationID);

		sendRPCRequest(msg);
	}
	
	// Protected unregisterAppInterface used to ensure no non-ALM app calls
	// unregisterAppInterface.
	protected void unregisterAppInterfacePrivate(Integer correlationID) 
		throws SmartDeviceLinkException {

		UnregisterAppInterface msg = 
				RPCRequestFactory.buildUnregisterAppInterface(correlationID);
		
		sendRPCRequestPrivate(msg);
	}
	
	/**
	 * Sends an UnsubscribeButton RPCRequest to SMARTDEVICELINK. Responses are captured through callback on IProxyListener.
	 * 
	 * @param buttonName -Name of the button to unsubscribe.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	 */
	public void unsubscribeButton(ButtonName buttonName, 
			Integer correlationID) throws SmartDeviceLinkException {

		UnsubscribeButton msg = RPCRequestFactory.buildUnsubscribeButton(
				buttonName, correlationID);

		sendRPCRequest(msg);
	}
	
	/**
	 * Creates a choice to be added to a choiceset. Choice has both a voice and a visual menu component.
	 * 
	 * @param choiceID -Unique ID used to identify this choice (returned in callback).
	 * @param choiceMenuName -Text name displayed for this choice.
	 * @param choiceVrCommands -Vector of vrCommands used to select this choice by voice. Must contain
	 * 			at least one non-empty element.
	 * @return Choice created. 
	 * @throws SmartDeviceLinkException 
	 */
	public Choice createChoiceSetChoice(Integer choiceID, String choiceMenuName,
			Vector<String> choiceVrCommands) {		
		Choice returnChoice = new Choice();
		
		returnChoice.setChoiceID(choiceID);
		returnChoice.setMenuName(choiceMenuName);
		returnChoice.setVrCommands(choiceVrCommands);
		
		return returnChoice;
	}
	
	/**
	 * Starts audio pass thru session. Responses are captured through callback on IProxyListener.
	 * 
	 * @param initialPrompt -SMARTDEVICELINK will speak this prompt before opening the audio pass thru session.
	 * @param audioPassThruDisplayText1 -First line of text displayed during audio capture.
	 * @param audioPassThruDisplayText2 -Second line of text displayed during audio capture.
	 * @param samplingRate -Allowable values of 8 khz or 16 or 22 or 44 khz.
	 * @param maxDuration -The maximum duration of audio recording in milliseconds.
	 * @param bitsPerSample -Specifies the quality the audio is recorded. Currently 8 bit or 16 bit.
	 * @param audioType -Specifies the type of audio data being requested.
	 * @param muteAudio -Defines if the current audio source should be muted during the APT session.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException 
	 */
	public void performaudiopassthru(String initialPrompt, String audioPassThruDisplayText1, String audioPassThruDisplayText2,
			  SamplingRate samplingRate, Integer maxDuration, BitsPerSample bitsPerSample,
			  AudioType audioType, Boolean muteAudio, Integer correlationID) throws SmartDeviceLinkException {		

		PerformAudioPassThru msg = RPCRequestFactory.BuildPerformAudioPassThru(initialPrompt, audioPassThruDisplayText1, audioPassThruDisplayText2, 
																				samplingRate, maxDuration, bitsPerSample, audioType, muteAudio, correlationID);
		sendRPCRequest(msg);
	}

	/**
	 * Ends audio pass thru session. Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID
	 * @throws SmartDeviceLinkException 
	 */
	public void endaudiopassthru(Integer correlationID) throws SmartDeviceLinkException 
	{
		EndAudioPassThru msg = RPCRequestFactory.BuildEndAudioPassThru(correlationID);		
		sendRPCRequest(msg);
	}
	
	/**
	 *     Subscribes for specific published data items.  The data will be only sent if it has changed.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param gps -Subscribes to GPS data.
	 * @param speed -Subscribes to vehicle speed data in kilometers per hour.
	 * @param rpm -Subscribes to number of revolutions per minute of the engine.
	 * @param fuelLevel -Subscribes to fuel level in the tank (percentage).
	 * @param fuelLevel_State -Subscribes to fuel level state.
	 * @param instantFuelConsumption -Subscribes to instantaneous fuel consumption in microlitres.
	 * @param externalTemperature -Subscribes to the external temperature in degrees celsius.
	 * @param prndl -Subscribes to PRNDL data that houses the selected gear.
	 * @param tirePressure -Subscribes to the TireStatus data containing status and pressure of tires. 
	 * @param odometer -Subscribes to Odometer data in km.
	 * @param beltStatus -Subscribes to status of the seat belts.
	 * @param bodyInformation -Subscribes to body information including power modes.
	 * @param deviceStatus -Subscribes to device status including signal and battery strength.
	 * @param driverBraking -Subscribes to the status of the brake pedal.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/
	public void subscribevehicledata(boolean gps, boolean speed, boolean rpm, boolean fuelLevel, boolean fuelLevel_State,
									 boolean instantFuelConsumption, boolean externalTemperature, boolean prndl, boolean tirePressure,						
									 boolean odometer, boolean beltStatus, boolean bodyInformation, boolean deviceStatus,
									 boolean driverBraking, Integer correlationID) throws SmartDeviceLinkException
	{
		SubscribeVehicleData msg = RPCRequestFactory.BuildSubscribeVehicleData(gps, speed, rpm, fuelLevel, fuelLevel_State, instantFuelConsumption, externalTemperature, prndl, tirePressure, 
																				odometer, beltStatus, bodyInformation, deviceStatus, driverBraking, correlationID);
		
		sendRPCRequest(msg);
	}
	
	/**
	 *     Unsubscribes for specific published data items.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param gps -Unsubscribes to GPS data.
	 * @param speed -Unsubscribes to vehicle speed data in kilometers per hour.
	 * @param rpm -Unsubscribes to number of revolutions per minute of the engine.
	 * @param fuelLevel -Unsubscribes to fuel level in the tank (percentage).
	 * @param fuelLevel_State -Unsubscribes to fuel level state.
	 * @param instantFuelConsumption -Unsubscribes to instantaneous fuel consumption in microlitres.
	 * @param externalTemperature -Unsubscribes to the external temperature in degrees celsius.
	 * @param prndl -Unsubscribes to PRNDL data that houses the selected gear.
	 * @param tirePressure -Unsubscribes to the TireStatus data containing status and pressure of tires. 
	 * @param odometer -Unsubscribes to Odometer data in km.
	 * @param beltStatus -Unsubscribes to status of the seat belts.
	 * @param bodyInformation -Unsubscribes to body information including power modes.
	 * @param deviceStatus -Unsubscribes to device status including signal and battery strength.
	 * @param driverBraking -Unsubscribes to the status of the brake pedal.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/

	public void unsubscribevehicledata(boolean gps, boolean speed, boolean rpm, boolean fuelLevel, boolean fuelLevel_State,
			 						   boolean instantFuelConsumption, boolean externalTemperature, boolean prndl, boolean tirePressure,
			 						   boolean odometer, boolean beltStatus, boolean bodyInformation, boolean deviceStatus,
			 						   boolean driverBraking, Integer correlationID) throws SmartDeviceLinkException
	{
		UnsubscribeVehicleData msg = RPCRequestFactory.BuildUnsubscribeVehicleData(gps, speed, rpm, fuelLevel, fuelLevel_State, instantFuelConsumption, externalTemperature, prndl, tirePressure,
																					odometer, beltStatus, bodyInformation, deviceStatus, driverBraking, correlationID);
		sendRPCRequest(msg);
	}


	/**
	 *     Performs a Non periodic vehicle data read request.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param gps -Performs an ad-hoc request for GPS data.
	 * @param speed -Performs an ad-hoc request for vehicle speed data in kilometers per hour.
	 * @param rpm -Performs an ad-hoc request for number of revolutions per minute of the engine.
	 * @param fuelLevel -Performs an ad-hoc request for fuel level in the tank (percentage).
	 * @param fuelLevel_State -Performs an ad-hoc request for fuel level state.
	 * @param instantFuelConsumption -Performs an ad-hoc request for instantaneous fuel consumption in microlitres.
	 * @param externalTemperature -Performs an ad-hoc request for the external temperature in degrees celsius.
	 * @param vin -Performs an ad-hoc request for the Vehicle identification number
	 * @param prndl -Performs an ad-hoc request for PRNDL data that houses the selected gear.
	 * @param tirePressure -Performs an ad-hoc request for the TireStatus data containing status and pressure of tires. 
	 * @param odometer -Performs an ad-hoc request for Odometer data in km.
	 * @param beltStatus -Performs an ad-hoc request for status of the seat belts.
	 * @param bodyInformation -Performs an ad-hoc request for  body information including power modes.
	 * @param deviceStatus -Performs an ad-hoc request for device status including signal and battery strength.
	 * @param driverBraking -Performs an ad-hoc request for the status of the brake pedal.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/
	public void getvehicledata(boolean gps, boolean speed, boolean rpm, boolean fuelLevel, boolean fuelLevel_State,
			 				   boolean instantFuelConsumption, boolean externalTemperature, boolean vin, boolean prndl, boolean tirePressure,
			 				   boolean odometer, boolean beltStatus, boolean bodyInformation, boolean deviceStatus,
			 				   boolean driverBraking, Integer correlationID) throws SmartDeviceLinkException
	{
	
		GetVehicleData msg = RPCRequestFactory.BuildGetVehicleData(gps, speed, rpm, fuelLevel, fuelLevel_State, instantFuelConsumption, externalTemperature, vin, prndl, tirePressure, odometer,
																   beltStatus, bodyInformation, deviceStatus, driverBraking, correlationID);
		sendRPCRequest(msg);
	}


	/**
	 *     Non periodic vehicle data read request
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param ecuName -Name of ECU.
	 * @param didLocation -Raw data from vehicle data DID location(s)
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/

	public void readdid(Integer ecuName, Vector<Integer> didLocation, Integer correlationID) throws SmartDeviceLinkException
	{
		ReadDID msg = RPCRequestFactory.BuildReadDID(ecuName, didLocation, correlationID);		
		sendRPCRequest(msg);		
	}


	/**
	 *     Vehicle module diagnostic trouble code request.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param ecuName -Name of ECU.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void getdtcs(Integer ecuName, Integer correlationID) throws SmartDeviceLinkException
	{
		GetDTCs msg = RPCRequestFactory.BuildGetDTCs(ecuName, correlationID);		
		sendRPCRequest(msg);
	}
	
	/**
	 *     Creates a full screen overlay containing a large block of formatted text that can be scrolled with up to 8 SoftButtons defined.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param scrollableMessageBody -Body of text that can include newlines and tabs.
	 * @param timeout -App defined timeout.  Indicates how long of a timeout from the last action.
	 * @param softButtons -App defined SoftButtons.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/		
	public void scrollablemessage(String scrollableMessageBody, Integer timeout, Vector<SoftButton> softButtons, Integer correlationID) throws SmartDeviceLinkException
	{
		ScrollableMessage msg = RPCRequestFactory.BuildScrollableMessage(scrollableMessageBody, timeout, softButtons, correlationID);		
		sendRPCRequest(msg);
	}


	/**
	 *     Creates a full screen or pop-up overlay (depending on platform) with a single user controlled slider.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param numTicks -Number of selectable items on a horizontal axis.
	 * @param position -Initial position of slider control (cannot exceed numTicks).
	 * @param sliderHeader -Text header to display.
	 * @param sliderFooter - Text footer to display (meant to display min/max threshold descriptors).
	 * @param timeout -App defined timeout.  Indicates how long of a timeout from the last action.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void slider(Integer numTicks, Integer position, String sliderHeader, Vector<String> sliderFooter, Integer timeout, Integer correlationID) throws SmartDeviceLinkException
	{
		Slider msg = RPCRequestFactory.BuildSlider(numTicks, position, sliderHeader, sliderFooter, timeout, correlationID);		
		sendRPCRequest(msg);		
	}

	
	/**	
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param navigationText1
	 * @param navigationText2
	 * @param eta
	 * @param totalDistance
	 * @param turnIcon
	 * @param distanceToManeuver
	 * @param distanceToManeuverScale
	 * @param maneuverComplete
	 * @param softButtons
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	*/		
	public void showconstanttbt(String navigationText1, String navigationText2, String eta, String totalDistance, Image turnIcon, Double distanceToManeuver,
			   					Double distanceToManeuverScale, boolean maneuverComplete, Vector <SoftButton> softButtons, Integer correlationID) throws SmartDeviceLinkException
	{
		ShowConstantTBT msg = RPCRequestFactory.BuildShowConstantTBT(navigationText1, navigationText2, eta, totalDistance, turnIcon, distanceToManeuver, 
																					distanceToManeuverScale, maneuverComplete, softButtons, correlationID);
		sendRPCRequest(msg);
	}


	/**
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param ttsText
	 * @param softButtons
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	*/
	public void alertmaneuver(String ttsText, Vector<SoftButton> softButtons, Integer correlationID) throws SmartDeviceLinkException
	{
		AlertManeuver msg = RPCRequestFactory.BuildAlertManeuver(ttsText, softButtons, correlationID);
		sendRPCRequest(msg);
	}	
	

	/**
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param turnList
	 * @param softButtons
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	*/
	public void updateturnlist(Vector<Turn> turnList, Vector<SoftButton> softButtons, Integer correlationID) throws SmartDeviceLinkException
	{
		UpdateTurnList msg = RPCRequestFactory.BuildUpdateTurnList(turnList, softButtons, correlationID);		
		sendRPCRequest(msg);
	}
	
	/**
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param language
	 * @param hmiDisplayLanguage
	 * @param correlationID
	 * @throws SmartDeviceLinkException
	*/	
	public void changeregistration(Language language, Language hmiDisplayLanguage, Integer correlationID) throws SmartDeviceLinkException
	{
		ChangeRegistration msg = RPCRequestFactory.BuildChangeRegistration(language, hmiDisplayLanguage, correlationID);
		sendRPCRequest(msg);
	}
	

	/**
	 *     Used to push a binary data onto the SMARTDEVICELINK module from a mobile device, such as icons and album art.  Not supported on first generation SMARTDEVICELINK vehicles.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param smartDeviceLinkFileName -File reference name.
	 * @param fileType -Selected file type.
	 * @param persistentFile -Indicates if the file is meant to persist between sessions / ignition cycles.
	 * @param fileData
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void putfile(String smartDeviceLinkFileName, FileType fileType, Boolean persistentFile, byte[] fileData, Integer correlationID) throws SmartDeviceLinkException 
	{
		PutFile msg = RPCRequestFactory.buildPutFile(smartDeviceLinkFileName, fileType, persistentFile, fileData, correlationID);
		sendRPCRequest(msg);
	}
	
	/**
	 *     Used to delete a file resident on the SMARTDEVICELINK module in the app's local cache.  Not supported on first generation SMARTDEVICELINK vehicles.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param smartDeviceLinkFileName -File reference name.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void deletefile(String smartDeviceLinkFileName, Integer correlationID) throws SmartDeviceLinkException 
	{
		DeleteFile msg = RPCRequestFactory.buildDeleteFile(smartDeviceLinkFileName, correlationID);
		sendRPCRequest(msg);
	}
	
	/**
	 *     Requests the current list of resident filenames for the registered app.  Not supported on first generation SMARTDEVICELINK vehicles.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void listfiles(Integer correlationID) throws SmartDeviceLinkException
	{
		ListFiles msg = RPCRequestFactory.buildListFiles(correlationID);
		sendRPCRequest(msg);
	}

	/**
	 *     Used to set existing local file on SMARTDEVICELINK as the app's icon.  Not supported on first generation SMARTDEVICELINK vehicles.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param smartDeviceLinkFileName -File reference name.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void setappicon(String smartDeviceLinkFileName, Integer correlationID) throws SmartDeviceLinkException 
	{
		SetAppIcon msg = RPCRequestFactory.buildSetAppIcon(smartDeviceLinkFileName, correlationID);
		sendRPCRequest(msg);
	}
	
	/**
	 *     Set an alternate display layout. If not sent, default screen for given platform will be shown.
	 *     Responses are captured through callback on IProxyListener.
	 * 
	 * @param displayLayout -Predefined or dynamically created screen layout.
	 * @param correlationID -A unique ID that correlates each RPCRequest and RPCResponse.
	 * @throws SmartDeviceLinkException
	*/	
	public void setdisplaylayout(String displayLayout, Integer correlationID) throws SmartDeviceLinkException
	{
		SetDisplayLayout msg = RPCRequestFactory.BuildSetDisplayLayout(displayLayout, correlationID);
		sendRPCRequest(msg);
	}

    /******************** Profile management *************************/

    public void loadProfile(String profileId, Integer correlationID) throws SmartDeviceLinkException {
        if (profileId == null || correlationID == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        LoadProfile msg = RPCRequestFactory.buildLoadProfile(profileId, correlationID);
        sendRPCRequest(msg);
    }

    public void unloadProfile(String profileId, Integer correlationID) throws SmartDeviceLinkException {
        if (profileId == null || correlationID == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        UnloadProfile msg = RPCRequestFactory.buildUnloadProfile(profileId, correlationID);
        sendRPCRequest(msg);
    }

    public Integer addProfile(String profileId, byte[] profileBinData, Integer correlationID)
            throws SmartDeviceLinkException {
        if (profileBinData == null || profileId == null || correlationID == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        List<AddProfile> requests = ProfileBinaryPacketizer.createAddProfileRequests(profileBinData, profileId, correlationID);
        if (requests != null && requests.size() > 0) {
            for (AddProfile request : requests) {
                sendRPCRequest(request);
            }
        }
        
        return correlationID + requests.size() - 1;
    }

    public void removeProfile(String profileId, Integer correlationID) throws SmartDeviceLinkException {
        if (profileId == null || correlationID == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        RemoveProfile msg = RPCRequestFactory.buildRemoveProfile(profileId, correlationID);
        sendRPCRequest(msg);
    }

    public void appStateChanged(String profileId, MobileAppState state, Integer correlationID)
            throws SmartDeviceLinkException {
        if (profileId == null || correlationID == null || state == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        AppStateChanged msg = RPCRequestFactory.buildAppStateChanged(profileId, state, correlationID);
        sendRPCRequest(msg);
    }

    public void sendAppToProfileMessage(String profileId, byte[] message, Integer correlationID)
            throws SmartDeviceLinkException {
        if (profileId == null || correlationID == null || message == null) {
            throw new SmartDeviceLinkException("Invalid parameters provided!",
                    SmartDeviceLinkExceptionCause.INVALID_RPC_PARAMETER);
        }
        SendAppToProfileMessage msg = RPCRequestFactory.buildSendAppToProfileMessage(profileId, message, correlationID);
        sendRPCRequest(msg);
    }
	
	/******************** END Public Helper Methods *************************/
	
	/**
	 * Gets type of transport currently used by this SmartDeviceLinkProxy.
	 * 
	 * @return One of TransportType enumeration values.
	 * 
	 * @see TransportType
	 */
	public TransportType getCurrentTransportType() throws IllegalStateException {
		if (_smartDeviceLinkConnection  == null) {
			throw new IllegalStateException("Incorrect state of SmartDeviceLinkProxyBase: Calling for getCurrentTransportType() while connection is not initialized");
		}
			
		return _smartDeviceLinkConnection.getCurrentTransportType();
	}
	
} // end-class