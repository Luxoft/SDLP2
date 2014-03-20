package com.smartdevicelinktester.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.smartdevicelinktester.activity.SmartDeviceLinkTester;
import com.smartdevicelinktester.adapters.logAdapter;
import com.smartdevicelinktester.constants.Const;
import com.smartdevicelinktester.logmessages.RPCLogMessage;
import com.smartdevicelinktester.logmessages.StringLogMessage;
import com.smartdevicelinktester.module.ModuleTest;
import com.smartdevicelinktester.receivers.smartdevicelinkReceiver;
import com.smartdevicelinktester.R;
import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.exception.SmartDeviceLinkExceptionCause;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.SmartDeviceLinkProxyALM;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.interfaces.IProxyListenerALM;
import com.smartdevicelink.proxy.rpc.AddCommand;
import com.smartdevicelink.proxy.rpc.AddCommandResponse;
import com.smartdevicelink.proxy.rpc.AddSubMenuResponse;
import com.smartdevicelink.proxy.rpc.AlertManeuverResponse;
import com.smartdevicelink.proxy.rpc.AlertResponse;
import com.smartdevicelink.proxy.rpc.ChangeRegistrationResponse;
import com.smartdevicelink.proxy.rpc.CreateInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteCommandResponse;
import com.smartdevicelink.proxy.rpc.DeleteFileResponse;
import com.smartdevicelink.proxy.rpc.DeleteInteractionChoiceSetResponse;
import com.smartdevicelink.proxy.rpc.DeleteSubMenuResponse;
import com.smartdevicelink.proxy.rpc.DialNumberResponse;
import com.smartdevicelink.proxy.rpc.EndAudioPassThruResponse;
import com.smartdevicelink.proxy.rpc.GenericResponse;
import com.smartdevicelink.proxy.rpc.GetDTCsResponse;
import com.smartdevicelink.proxy.rpc.GetVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.ListFilesResponse;
import com.smartdevicelink.proxy.rpc.MenuParams;
import com.smartdevicelink.proxy.rpc.OnAudioPassThru;
import com.smartdevicelink.proxy.rpc.OnButtonEvent;
import com.smartdevicelink.proxy.rpc.OnButtonPress;
import com.smartdevicelink.proxy.rpc.OnCommand;
import com.smartdevicelink.proxy.rpc.OnDriverDistraction;
import com.smartdevicelink.proxy.rpc.OnHMIStatus;
import com.smartdevicelink.proxy.rpc.OnLanguageChange;
import com.smartdevicelink.proxy.rpc.OnPermissionsChange;
import com.smartdevicelink.proxy.rpc.OnTBTClientState;
import com.smartdevicelink.proxy.rpc.OnVehicleData;
import com.smartdevicelink.proxy.rpc.PerformAudioPassThruResponse;
import com.smartdevicelink.proxy.rpc.PerformInteractionResponse;
import com.smartdevicelink.proxy.rpc.PutFile;
import com.smartdevicelink.proxy.rpc.PutFileResponse;
import com.smartdevicelink.proxy.rpc.ReadDIDResponse;
import com.smartdevicelink.proxy.rpc.ResetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.ScrollableMessageResponse;
import com.smartdevicelink.proxy.rpc.SetAppIcon;
import com.smartdevicelink.proxy.rpc.SetAppIconResponse;
import com.smartdevicelink.proxy.rpc.SetDisplayLayoutResponse;
import com.smartdevicelink.proxy.rpc.SetGlobalPropertiesResponse;
import com.smartdevicelink.proxy.rpc.SetMediaClockTimerResponse;
import com.smartdevicelink.proxy.rpc.Show;
import com.smartdevicelink.proxy.rpc.ShowConstantTBTResponse;
import com.smartdevicelink.proxy.rpc.ShowResponse;
import com.smartdevicelink.proxy.rpc.SliderResponse;
import com.smartdevicelink.proxy.rpc.SpeakResponse;
import com.smartdevicelink.proxy.rpc.SubscribeButton;
import com.smartdevicelink.proxy.rpc.SubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.TTSChunk;
import com.smartdevicelink.proxy.rpc.UnsubscribeButtonResponse;
import com.smartdevicelink.proxy.rpc.UnsubscribeVehicleDataResponse;
import com.smartdevicelink.proxy.rpc.UpdateTurnListResponse;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.HMILevel;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.pm.AddProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.AppStateChangedResponse;
import com.smartdevicelink.proxy.rpc.pm.LoadProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.OnProfileClosed;
import com.smartdevicelink.proxy.rpc.pm.OnSendProfileToAppMessage;
import com.smartdevicelink.proxy.rpc.pm.RemoveProfileResponse;
import com.smartdevicelink.proxy.rpc.pm.SendAppToProfileMessageResponse;
import com.smartdevicelink.proxy.rpc.pm.UnloadProfileResponse;
import com.smartdevicelink.transport.TCPTransportConfig;


public class ProxyService extends Service implements IProxyListenerALM {
	static final String TAG = "SmartDeviceLinkTester";
	private Integer autoIncCorrId = 1;
	
	private static final String ICON_smartdevicelink_FILENAME = "icon.png";
	private static final String ICON_FILENAME_SUFFIX = ".png";
	
	private static final int XML_TEST_COMMAND = 100;

	private static SmartDeviceLinkTester _mainInstance;	
	private static ProxyService _instance;
	private static SmartDeviceLinkProxyALM _smartdevicelinkProxy;
	private static logAdapter _msgAdapter;
	private ModuleTest _testerMain;
	private BluetoothAdapter mBtAdapter;
	private MediaPlayer embeddedAudioPlayer;
	private Boolean playingAudio = false;
	protected smartdevicelinkReceiver mediaButtonReceiver;
	
	private boolean firstHMIStatusChange = true;
	private HMILevel prevHMILevel = HMILevel.HMI_NONE;
	
	private static boolean waitingForResponse = false;
	
	public void onCreate() {
		super.onCreate();
		
		IntentFilter mediaIntentFilter = new IntentFilter();
		mediaIntentFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
		
		mediaButtonReceiver = new smartdevicelinkReceiver();
		registerReceiver(mediaButtonReceiver, mediaIntentFilter);
		
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.onCreate()"), Log.INFO);
		else Log.i(TAG, "ProxyService.onCreate()");
		
		_instance = this;
	}
	
	public void showLockMain() {
		if(SmartDeviceLinkTester.getInstance() == null) {
			Intent i = new Intent(this, SmartDeviceLinkTester.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.onStartCommand()"), Log.INFO);
		else Log.i(TAG, "ProxyService.onStartCommand()");
		
		startProxyIfNetworkConnected();
		
        setCurrentActivity(SmartDeviceLinkTester.getInstance());
			
        return START_STICKY;
	}

	/**
	 * Function checks if WiFi enabled.
	 * Manifest permission is required:
	 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * 
	 * @return true if enabled
	 */
	private boolean hasWiFiConnection() {
		boolean result = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			if (netInfo != null) {
				for (NetworkInfo ni : netInfo) {
					if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
						Log.d(TAG, ni.getTypeName());
						if (ni.isConnected()) {
							Log.d(TAG,
									"ProxyService().hasWiFiConnection(): wifi conncetion found");
							result = true;
						}
					}
				}
			}
		}
		return result;
	}

	private void startProxyIfNetworkConnected() {
		final SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME,
				MODE_PRIVATE);
		final int transportType = prefs.getInt(
				Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);

		if (transportType == Const.Transport.KEY_BLUETOOTH) {
			Log.d(TAG, "ProxyService. onStartCommand(). Transport = Bluetooth.");
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBtAdapter != null) {
				if (mBtAdapter.isEnabled()) {
					startProxy();
				}
			}
		} else {
			//TODO: This code is commented out for simulator purposes
			/*
			Log.d(TAG, "ProxyService. onStartCommand(). Transport = WiFi.");
			if (hasWiFiConnection() == true) {
				Log.d(TAG, "ProxyService. onStartCommand(). WiFi enabled.");
				startProxy();
			} else {
				Log.w(TAG,
						"ProxyService. onStartCommand(). WiFi is not enabled.");
			}
			*/
			startProxy();
		}
	}

	public void startProxy() {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.startProxy()"), true);
		else Log.i(TAG, "ProxyService.startProxy()");
		
		if (_smartdevicelinkProxy == null) {
			try {
				SharedPreferences settings = getSharedPreferences(
						Const.PREFS_NAME, 0);
				boolean isMediaApp = settings.getBoolean(
						Const.PREFS_KEY_ISMEDIAAPP,
						Const.PREFS_DEFAULT_ISMEDIAAPP);
				String appName = settings.getString(Const.PREFS_KEY_APPNAME,
						Const.PREFS_DEFAULT_APPNAME);

				String appSynonym1 = settings.getString(Const.PREFS_KEY_APPSYNONYM1,
						Const.PREFS_DEFAULT_APPSYNONYM1);
				
				String appSynonym2 = settings.getString(Const.PREFS_KEY_APPSYNONYM2,
						Const.PREFS_DEFAULT_APPSYNONYM2);
				
				
				String appTTSTextName = settings.getString(Const.PREFS_KEY_APP_TTS_TEXT, Const.PREFS_DEFAULT_APP_TTS_TEXT);
				
				
				String appTTSType = settings.getString(Const.PREFS_KEY_APP_TTS_TYPE, Const.PREFS_DEFAULT_APP_TTS_TYPE);
				
				SpeechCapabilities appTTSTextType = SpeechCapabilities.valueForString(appTTSType);
								
				Vector<TTSChunk> chunks = new Vector<TTSChunk>();
								
				TTSChunk ttsChunks = TTSChunkFactory.createChunk(appTTSTextType, appTTSTextName);				
								
				chunks.add(ttsChunks);				
				
				
				String appId = settings.getString(Const.PREFS_KEY_APPID,
						Const.PREFS_DEFAULT_APPID);
				Language lang = Language.valueOf(settings.getString(
						Const.PREFS_KEY_LANG, Const.PREFS_DEFAULT_LANG));
				Language hmiLang = Language.valueOf(settings.getString(
						Const.PREFS_KEY_HMILANG, Const.PREFS_DEFAULT_HMILANG));
				int transportType = settings.getInt(
						Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);
				String ipAddress = settings.getString(
						Const.Transport.PREFS_KEY_TRANSPORT_IP,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_IP);
				int tcpPort = settings.getInt(
						Const.Transport.PREFS_KEY_TRANSPORT_PORT,
						Const.Transport.PREFS_DEFAULT_TRANSPORT_PORT);
				boolean autoReconnect = settings
						.getBoolean(
								Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
								Const.Transport.PREFS_DEFAULT_TRANSPORT_RECONNECT_DEFAULT);

				
				Vector<String> vrSynonyms = new Vector<String>();
				
				vrSynonyms.add(appSynonym1);
				vrSynonyms.add(appSynonym2);
				
				if (transportType == Const.Transport.KEY_BLUETOOTH) {
					_smartdevicelinkProxy = new SmartDeviceLinkProxyALM((IProxyListenerALM)this,
							/*smartdevicelink proxy configuration resources*/null,
							/*enable advanced lifecycle management true,*/
							appName,
							chunks,
							/*ngn media app*/null,
							/*vr synonyms*/vrSynonyms,
							/*is media app*/isMediaApp,
							/*smartdevicelinkMsgVersion*/null,
							/*language desired*/lang,
							/*HMI Display Language Desired*/hmiLang,
							/*App ID*/appId,
							/*autoActivateID*/null,
							/*callbackToUIThread*/ false,
							/*preRegister*/ false);
				} else {
					_smartdevicelinkProxy = new SmartDeviceLinkProxyALM((IProxyListenerALM)this,
							/*smartdevicelink proxy configuration resources*/null,
							/*enable advanced lifecycle management true,*/
							appName,
							chunks,
							/*ngn media app*/null,
							/*vr synonyms*/null,
							/*is media app*/isMediaApp,
							/*smartdevicelinkMsgVersion*/null,
							/*language desired*/lang,
							/*HMI Display Language Desired*/hmiLang,
							/*App ID*/appId,
							/*autoActivateID*/null,
							/*callbackToUIThre1ad*/ false,
							/*preRegister*/ false,
							new TCPTransportConfig(tcpPort, ipAddress, autoReconnect));
				}
			} catch (SmartDeviceLinkException e) {
				e.printStackTrace();
				//error creating proxy, returned proxy = null
				if (_smartdevicelinkProxy == null){
					stopSelf();
				}
			}
		}
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.startProxy() returning"), Log.INFO);
		else Log.i(TAG, "ProxyService.startProxy() returning");
	}
	
	private boolean getAutoSetAppIconFlag() {
		return getSharedPreferences(Const.PREFS_NAME, 0).getBoolean(
				Const.PREFS_KEY_AUTOSETAPPICON,
				Const.PREFS_DEFAULT_AUTOSETAPPICON);
	}
	
	public void onDestroy() {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.onDestroy()"), Log.INFO);
		else Log.i(TAG, "ProxyService.onDestroy()");
		
		disposesmartdevicelinkProxy();
		_instance = null;
		if (embeddedAudioPlayer != null) embeddedAudioPlayer.release();		
		unregisterReceiver(mediaButtonReceiver);	
		super.onDestroy();
	}
	
	public void disposesmartdevicelinkProxy() {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new StringLogMessage("ProxyService.disposesmartdevicelinkProxy()"), Log.INFO);
		else Log.i(TAG, "ProxyService.disposesmartdevicelinkProxy()");
		
		if (_smartdevicelinkProxy != null) {
			try {
				_smartdevicelinkProxy.dispose();
			} catch (SmartDeviceLinkException e) {
				e.printStackTrace();
			}
			_smartdevicelinkProxy = null;
		}
	}
	
	private void initialize() {
		playingAudio = true;
		playAnnoyingRepetitiveAudio();
		
		try {
			show("SmartDeviceLink", "Tester");
		} catch (SmartDeviceLinkException e) {
			if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
			if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Error sending show"), Log.ERROR, e, true);
			else Log.e(TAG, "Error sending show", e);
		}

		try { 
			subscribeToButton(ButtonName.OK);
			subscribeToButton(ButtonName.SEEKLEFT);
			subscribeToButton(ButtonName.SEEKRIGHT);
			subscribeToButton(ButtonName.TUNEUP);
			subscribeToButton(ButtonName.TUNEDOWN);
			Vector<ButtonName> buttons = new Vector<ButtonName>(Arrays.asList(new ButtonName[] {
					ButtonName.OK, ButtonName.SEEKLEFT, ButtonName.SEEKRIGHT, ButtonName.TUNEUP,
					ButtonName.TUNEDOWN }));
			SmartDeviceLinkTester.getInstance().buttonsSubscribed(buttons);
		} catch (SmartDeviceLinkException e) {
			if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
			if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Error subscribing to buttons"), Log.ERROR, e, true);
			else Log.e(TAG, "Error subscribing to buttons", e);
		}
		
		try {
			addCommand(XML_TEST_COMMAND, new Vector<String>(Arrays.asList(new String[] {"XML Test", "XML"})), "XML Test");
		} catch (SmartDeviceLinkException e) {
			if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
			if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Error adding AddCommands"), Log.ERROR, e, true);
			else Log.e(TAG, "Error adding AddCommands", e);
		}
	}

	private void show(String mainField1, String mainField2) throws SmartDeviceLinkException {
		Show msg = new Show();
		msg.setCorrelationID(nextCorrID());
		msg.setMainField1(mainField1);
		msg.setMainField2(mainField2);
		_msgAdapter.logMessage(new RPCLogMessage(msg), true);
		_smartdevicelinkProxy.sendRPCRequest(msg);
	}

	private void addCommand(Integer cmdId, Vector<String> vrCommands,
			String menuName) throws SmartDeviceLinkException {
		AddCommand addCommand = new AddCommand();
		addCommand.setCorrelationID(nextCorrID());
		addCommand.setCmdID(cmdId);
		addCommand.setVrCommands(vrCommands);
		MenuParams menuParams = new MenuParams();
		menuParams.setMenuName(menuName);
		addCommand.setMenuParams(menuParams);
		_msgAdapter.logMessage(new RPCLogMessage(addCommand), true);
		_smartdevicelinkProxy.sendRPCRequest(addCommand);
	}
	
	private void subscribeToButton(ButtonName buttonName) throws SmartDeviceLinkException {
		SubscribeButton msg = new SubscribeButton();
		msg.setCorrelationID(nextCorrID());
		msg.setButtonName(buttonName);
		_msgAdapter.logMessage(new RPCLogMessage(msg), true);
		_smartdevicelinkProxy.sendRPCRequest(msg);
	}
	
	public void playPauseAnnoyingRepetitiveAudio() {
		if (embeddedAudioPlayer != null && embeddedAudioPlayer.isPlaying()) {
			playingAudio = false;
			pauseAnnoyingRepetitiveAudio();
		} else {
			playingAudio = true;
			playAnnoyingRepetitiveAudio();
		}
	}

	private void playAnnoyingRepetitiveAudio() {
		if (embeddedAudioPlayer == null) {
			embeddedAudioPlayer = MediaPlayer.create(this, R.raw.energy);
			embeddedAudioPlayer.setLooping(true);
		}
		embeddedAudioPlayer.start();
		
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Playing audio"), true);
		else Log.i(TAG, "Playing audio");
	}
	
	public void pauseAnnoyingRepetitiveAudio() {
		if (embeddedAudioPlayer != null && embeddedAudioPlayer.isPlaying()) {
			embeddedAudioPlayer.pause();
			
			if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
			if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Paused audio"), true);
			else Log.i(TAG, "Paused audio");
		}
	}
	
	public static SmartDeviceLinkProxyALM getProxyInstance() {
		return _smartdevicelinkProxy;
	}

	public static ProxyService getInstance() {
		return _instance;
	}
	
	public SmartDeviceLinkTester getCurrentActivity() {
		return _mainInstance;
	}

	public void startModuleTest() {
		_testerMain = new ModuleTest();
	}
	
	public static void waiting(boolean waiting) {
		waitingForResponse = waiting;
	}
	
	public void setCurrentActivity(SmartDeviceLinkTester currentActivity) {
		if (this._mainInstance != null) {
			this._mainInstance.finish();
			this._mainInstance = null;
		}
		
		this._mainInstance = currentActivity;
		// update the _msgAdapter
		_msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
	}
	
	protected int nextCorrID() {
		autoIncCorrId++;
		return autoIncCorrId;
	}

	@Override
	public void onOnHMIStatus(OnHMIStatus notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
		
		switch(notification.getSystemContext()) {
			case SYSCTXT_MAIN:
				break;
			case SYSCTXT_VRSESSION:
				break;
			case SYSCTXT_MENU:
				break;
			default:
				return;
		}
		
		switch(notification.getAudioStreamingState()) {
			case AUDIBLE:
				if (playingAudio) playAnnoyingRepetitiveAudio();
				break;
			case NOT_AUDIBLE:
				pauseAnnoyingRepetitiveAudio();
				break;
			default:
				return;
		}
		
		HMILevel curHMILevel = notification.getHmiLevel();
		if (prevHMILevel != curHMILevel) {
			boolean hmiChange = false;
			boolean hmiFull = false;
			switch(curHMILevel) {
				case HMI_FULL:
					hmiFull = true;
					hmiChange = true;
					break;
				case HMI_LIMITED:
					hmiChange = true;
					break;
				case HMI_BACKGROUND:
					hmiChange = true;
					break;
				case HMI_NONE:
					break;
				default:
					return;
			}
			prevHMILevel = curHMILevel;
			
			if (_smartdevicelinkProxy.getAppInterfaceRegistered()) {
				if (hmiFull) {
					if (firstHMIStatusChange) {
						showLockMain();
						_testerMain = new ModuleTest();
						_testerMain = ModuleTest.getModuleTestInstance();
						initialize();
					}
					else {
						try {
							if (!waitingForResponse && _testerMain != null && _testerMain.getThreadContext() != null) {
								show("SmartDeviceLink", "Tester Ready");
							}
						} catch (SmartDeviceLinkException e) {
							if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
							if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Error sending show"), Log.ERROR, e, true);
							else Log.e(TAG, "Error sending show", e);
						}
					}
				}
				
				if (hmiChange && firstHMIStatusChange && hmiFull) {
					firstHMIStatusChange = false;
										
					InputStream is = null;
					try {
							PutFile putFile = new PutFile();
							putFile.setFileType(FileType.GRAPHIC_PNG);
							putFile.setSmartDeviceLinkFileName(ICON_smartdevicelink_FILENAME);
							putFile.setCorrelationID(nextCorrID());
							putFile.setBulkData(contentsOfResource(R.raw.fiesta));
							_msgAdapter.logMessage(new RPCLogMessage(putFile), true);
							getProxyInstance().sendRPCRequest(putFile);

							if (getAutoSetAppIconFlag()) {
								SetAppIcon setAppIcon = new SetAppIcon();
								setAppIcon.setSmartDeviceLinkFileName(ICON_smartdevicelink_FILENAME);
								setAppIcon.setCorrelationID(nextCorrID());
								_msgAdapter.logMessage(new RPCLogMessage(setAppIcon), true);
								getProxyInstance().sendRPCRequest(setAppIcon);
							}

							// upload turn icons
							sendIconFromResource(R.drawable.turn_left);
							sendIconFromResource(R.drawable.turn_right);
							sendIconFromResource(R.drawable.turn_forward);
							sendIconFromResource(R.drawable.action);
						} catch (SmartDeviceLinkException e) {
							Log.w(TAG, "Failed to set app icon", e);
						}
					}
				}
			}
		}
	/**
	 * Checks and returns if the module testing is in progress.
	 * 
	 * @return true if the module testing is in progress
	 */
	public boolean isModuleTesting() {
		return waitingForResponse && _testerMain.getThreadContext() != null;
	}
	
	/**
	 * Returns the file contents from the specified resource.
	 * 
	 * @param resource Resource id (in res/ directory)
	 * @return The resource file's contents
	 */
	private byte[] contentsOfResource(int resource) {
		InputStream is = null;
		try {
			is = getResources().openRawResource(resource);
			ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
			final int buffersize = 4096;
			final byte[] buffer = new byte[buffersize];
			int available = 0;
			while ((available = is.read(buffer)) >= 0) {
				os.write(buffer, 0, available);
			}
			return os.toByteArray();
		} catch (IOException e) {
			Log.w(TAG, "Can't read icon file", e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendIconFromResource(int resource) throws SmartDeviceLinkException {
		PutFile putFile = new PutFile();
		putFile.setFileType(FileType.GRAPHIC_PNG);
		putFile.setSmartDeviceLinkFileName(getResources().getResourceEntryName(resource)
				+ ICON_FILENAME_SUFFIX);
		putFile.setCorrelationID(nextCorrID());
		putFile.setBulkData(contentsOfResource(resource));
		_msgAdapter.logMessage(new RPCLogMessage(putFile), true);
		getProxyInstance().sendRPCRequest(putFile);
	}
	
	@Override
	public void onOnCommand(OnCommand notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
		
		switch(notification.getCmdID())
		{
			case XML_TEST_COMMAND:
				_testerMain.restart();
				break;
			default:
				break;
		}
	}

	@Override
	public void onProxyClosed(String info, Exception e) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("onProxyClosed: " + info), Log.ERROR, e);
		else Log.e(TAG, "onProxyClosed: " + info, e);
		
		boolean wasConnected = !firstHMIStatusChange;
		firstHMIStatusChange = true;
		prevHMILevel = HMILevel.HMI_NONE;
		
		if (wasConnected) {
			final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
			if (mainActivity != null) {
				mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mainActivity.onProxyClosed();
					}
				});
			} else {
				Log.w(TAG, "mainActivity not found");
			}
		}
		
		if (!isModuleTesting()) {
			if(((SmartDeviceLinkException) e).getSmartDeviceLinkExceptionCause() != SmartDeviceLinkExceptionCause.SMARTDEVICELINK_PROXY_CYCLED
					&& ((SmartDeviceLinkException) e).getSmartDeviceLinkExceptionCause() != SmartDeviceLinkExceptionCause.BLUETOOTH_DISABLED) {
				reset();
			}
		}
	}
	
	public void reset(){
	   try {
		   _smartdevicelinkProxy.resetProxy();
		} catch (SmartDeviceLinkException e1) {
			e1.printStackTrace();
			//something goes wrong, & the proxy returns as null, stop the service.
			//do not want a running service with a null proxy
			if (_smartdevicelinkProxy == null){
				stopSelf();
			}
		}
	}
	
	/**
	 * Restarting SmartDeviceLinkProxyALM. For example after changing transport type
	 */
	public void restart() {
		Log.i(TAG, "ProxyService.Restart SmartDeviceLinkProxyALM.");
		disposesmartdevicelinkProxy();
		startProxyIfNetworkConnected();
	}
	
	@Override
	public void onError(String info, Exception e) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
 		if (_msgAdapter != null) {
 			_msgAdapter.logMessage(new StringLogMessage("******onProxyError******"), Log.ERROR);
 			_msgAdapter.logMessage(new StringLogMessage("ERROR: " + info), Log.ERROR, e);
		} else {
			Log.e(TAG, "******onProxyError******");
			Log.e(TAG, "ERROR: " + info, e);
 		}
	}
	
	/*********************************
	** smartdevicelink SmartDeviceLink Base Callback's **
	*********************************/
	@Override
	public void onAddSubMenuResponse(AddSubMenuResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final boolean success = response.getSuccess();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onAddSubMenuResponse(success);
			}
		});
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onCreateInteractionChoiceSetResponse(CreateInteractionChoiceSetResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final boolean success = response.getSuccess();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onCreateChoiceSetResponse(success);
			}
		});
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onDeleteCommandResponse(DeleteCommandResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null) _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onDeleteInteractionChoiceSetResponse(DeleteInteractionChoiceSetResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final boolean success = response.getSuccess();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onDeleteChoiceSetResponse(success);
			}
		});
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onDeleteSubMenuResponse(DeleteSubMenuResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final boolean success = response.getSuccess();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onDeleteSubMenuResponse(success);
			}
		});
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onResetGlobalPropertiesResponse(ResetGlobalPropertiesResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSetMediaClockTimerResponse(SetMediaClockTimerResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSpeakResponse(SpeakResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSubscribeButtonResponse(SubscribeButtonResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onUnsubscribeButtonResponse(UnsubscribeButtonResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onOnDriverDistraction(OnDriverDistraction notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
	}
	@Override
	public void onGenericResponse(GenericResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}

	/*********************************
	** smartdevicelink SmartDeviceLink Soft Button Image Callback's **
	*********************************/
	@Override
	public void onPutFileResponse(PutFileResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onDeleteFileResponse(DeleteFileResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onListFilesResponse(ListFilesResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSetAppIconResponse(SetAppIconResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onOnButtonEvent(OnButtonEvent notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
	}
	@Override
	public void onOnButtonPress(OnButtonPress notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
		
		switch(notification.getButtonName())
		{
			case OK:
				playPauseAnnoyingRepetitiveAudio();
				break;
			case SEEKLEFT:
				break;
			case SEEKRIGHT:
				break;
			case TUNEUP:
				break;
			case TUNEDOWN:
				break;
			default:
				break;
		}
	}
	
	/*********************************
	** smartdevicelink SmartDeviceLink Updated Callback's **
	*********************************/
	@Override
	public void onAddCommandResponse(AddCommandResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onAlertResponse(AlertResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}	
	@Override
	public void onPerformInteractionResponse(PerformInteractionResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSetGlobalPropertiesResponse(SetGlobalPropertiesResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onShowResponse(ShowResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}

	/*********************************
	** smartdevicelink SmartDeviceLink New Callback's **
	*********************************/
	@Override
	public void onSliderResponse(SliderResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onScrollableMessageResponse(ScrollableMessageResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onChangeRegistrationResponse(ChangeRegistrationResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onSetDisplayLayoutResponse(SetDisplayLayoutResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onOnLanguageChange(OnLanguageChange notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
	}

	/*********************************
	** smartdevicelink SmartDeviceLink Audio Pass Thru Callback's **
	*********************************/
	@Override
	public void onPerformAudioPassThruResponse(PerformAudioPassThruResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final Result result = response.getResultCode();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onPerformAudioPassThruResponse(result);
			}
		});
	}
	@Override
	public void onEndAudioPassThruResponse(EndAudioPassThruResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final Result result = response.getResultCode();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onEndAudioPassThruResponse(result);
			}
		});
	}
	@Override
	public void onOnAudioPassThru(OnAudioPassThru notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
		
		final SmartDeviceLinkTester mainActivity = SmartDeviceLinkTester.getInstance();
		final byte[] aptData = notification.getAPTData();
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mainActivity.onAudioPassThru(aptData);
			}
		});
	}

	/*********************************
	** smartdevicelink SmartDeviceLink Vehicle Data Callback's **
	*********************************/
	@Override
	public void onSubscribeVehicleDataResponse(SubscribeVehicleDataResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);				
				
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onUnsubscribeVehicleDataResponse(UnsubscribeVehicleDataResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
						
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onGetVehicleDataResponse(GetVehicleDataResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
							
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onReadDIDResponse(ReadDIDResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onGetDTCsResponse(GetDTCsResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onOnVehicleData(OnVehicleData notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
		
	}
	
	/*********************************
	** smartdevicelink SmartDeviceLink TBT Callback's **
	*********************************/
	@Override
	public void onShowConstantTBTResponse(ShowConstantTBTResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onAlertManeuverResponse(AlertManeuverResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onUpdateTurnListResponse(UpdateTurnListResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	@Override
	public void onOnTBTClientState(OnTBTClientState notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
	}

	/*********************************
	** smartdevicelink SmartDeviceLink Policies Callback's **
	*********************************/
	@Override
	public void onOnPermissionsChange(OnPermissionsChange notification) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(notification), true);
		else Log.i(TAG, "" + notification);
	}
	
	@Override
	public void onDialNumberResponse(DialNumberResponse response) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)  _msgAdapter.logMessage(new RPCLogMessage(response), true);
		else Log.i(TAG, "" + response);
		
		if (isModuleTesting()) {
			ModuleTest.responses.add(new Pair<Integer, Result>(response.getCorrelationID(), response.getResultCode()));
			synchronized (_testerMain.getThreadContext()) { _testerMain.getThreadContext().notify();};
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
		if (_msgAdapter != null)_msgAdapter.logMessage(new StringLogMessage("Service on Bind"));
		else Log.i(TAG, "Service on Bind");
		return new Binder();
	}
	
    /*********************************
    ** SDLP Profile Manager Callbacks **
    *********************************/
    @Override
    public void onAddProfileResponse(AddProfileResponse response) {
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onRemoveProfileResponse(RemoveProfileResponse response) {
        logProfileManagerEvent(response);
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onLoadProfileReponse(LoadProfileResponse response) {
        logProfileManagerEvent(response);
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onUnloadProfileResponse(UnloadProfileResponse response) {
        logProfileManagerEvent(response);
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onSendMessageToProfileResponse(SendAppToProfileMessageResponse response) {
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onAppStateChangedResponse(AppStateChangedResponse response) {
        logProfileManagerEvent(response);
        _mainInstance.handlePMMessage(response);
    }

    @Override
    public void onProfileClosed(OnProfileClosed notification) {
        logProfileManagerEvent(notification);
        _mainInstance.handlePMMessage(notification);
        try {
            _smartdevicelinkProxy.show("SmartDeviceLink", "Tester", null, null, null, null, nextCorrID());
        } catch (SmartDeviceLinkException e) {
            if (_msgAdapter == null) _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
            if (_msgAdapter != null) _msgAdapter.logMessage(
                    new StringLogMessage("Error sending show"), Log.ERROR, e, true);
            else Log.e(TAG, "Error sending show", e);
        }
    }

    @Override
    public void onReceiveMessageFromProfile(final OnSendProfileToAppMessage notification) {
        _mainInstance.handlePMMessage(notification);
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            public void run(){
                Toast.makeText(ProxyService.this, 
                        "Received: " + new String(notification.getMessage()), Toast.LENGTH_LONG).show();
            }
        });        
    }

    private void logProfileManagerEvent(RPCMessage msg) {
        if (_msgAdapter == null) {
            _msgAdapter = SmartDeviceLinkTester.getMessageAdapter();
        }
        if (_msgAdapter != null) {
            _msgAdapter.logMessage(new RPCLogMessage(msg), true);
        }
    }
}
