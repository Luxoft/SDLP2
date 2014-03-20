package com.smartdevicelinktester.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdevicelinktester.adapters.logAdapter;
import com.smartdevicelinktester.constants.Const;
import com.smartdevicelinktester.constants.smartdevicelinkSubMenu;
import com.smartdevicelinktester.logmessages.LogMessage;
import com.smartdevicelinktester.logmessages.RPCLogMessage;
import com.smartdevicelinktester.logmessages.StringLogMessage;
import com.smartdevicelinktester.module.ModuleTest;
import com.smartdevicelinktester.pmtests.ProfileManagerTests;
import com.smartdevicelinktester.service.ProxyService;
import com.smartdevicelinktester.R;
import com.smartdevicelink.exception.SmartDeviceLinkException;
import com.smartdevicelink.proxy.RPCMessage;
import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.RPCRequestFactory;
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.SmartDeviceLinkProxyALM;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.AddCommand;
import com.smartdevicelink.proxy.rpc.AddSubMenu;
import com.smartdevicelink.proxy.rpc.Alert;
import com.smartdevicelink.proxy.rpc.AlertManeuver;
import com.smartdevicelink.proxy.rpc.ButtonCapabilities;
import com.smartdevicelink.proxy.rpc.ChangeRegistration;
import com.smartdevicelink.proxy.rpc.Choice;
import com.smartdevicelink.proxy.rpc.CreateInteractionChoiceSet;
import com.smartdevicelink.proxy.rpc.DeleteCommand;
import com.smartdevicelink.proxy.rpc.DeleteFile;
import com.smartdevicelink.proxy.rpc.DeleteInteractionChoiceSet;
import com.smartdevicelink.proxy.rpc.DeleteSubMenu;
import com.smartdevicelink.proxy.rpc.DialNumber;
import com.smartdevicelink.proxy.rpc.DisplayCapabilities;
import com.smartdevicelink.proxy.rpc.EndAudioPassThru;
import com.smartdevicelink.proxy.rpc.GetDTCs;
import com.smartdevicelink.proxy.rpc.GetVehicleData;
import com.smartdevicelink.proxy.rpc.Image;
import com.smartdevicelink.proxy.rpc.ListFiles;
import com.smartdevicelink.proxy.rpc.MenuParams;
import com.smartdevicelink.proxy.rpc.OnAudioPassThru;
import com.smartdevicelink.proxy.rpc.PerformAudioPassThru;
import com.smartdevicelink.proxy.rpc.PerformInteraction;
import com.smartdevicelink.proxy.rpc.PutFile;
import com.smartdevicelink.proxy.rpc.ReadDID;
import com.smartdevicelink.proxy.rpc.ResetGlobalProperties;
import com.smartdevicelink.proxy.rpc.ScrollableMessage;
import com.smartdevicelink.proxy.rpc.SetAppIcon;
import com.smartdevicelink.proxy.rpc.SetGlobalProperties;
import com.smartdevicelink.proxy.rpc.SetMediaClockTimer;
import com.smartdevicelink.proxy.rpc.Show;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleData;
import com.smartdevicelink.proxy.rpc.UnsubscribeVehicleData;
import com.smartdevicelink.proxy.rpc.ShowConstantTBT;
import com.smartdevicelink.proxy.rpc.Slider;
import com.smartdevicelink.proxy.rpc.SoftButton;
import com.smartdevicelink.proxy.rpc.Speak;
import com.smartdevicelink.proxy.rpc.StartTime;
import com.smartdevicelink.proxy.rpc.SubscribeButton;
import com.smartdevicelink.proxy.rpc.TTSChunk;
import com.smartdevicelink.proxy.rpc.Turn;
import com.smartdevicelink.proxy.rpc.UnregisterAppInterface;
import com.smartdevicelink.proxy.rpc.UnsubscribeButton;
import com.smartdevicelink.proxy.rpc.UpdateTurnList;
import com.smartdevicelink.proxy.rpc.VehicleType;
import com.smartdevicelink.proxy.rpc.VrHelpItem;
import com.smartdevicelink.proxy.rpc.enums.AudioType;
import com.smartdevicelink.proxy.rpc.enums.BitsPerSample;
import com.smartdevicelink.proxy.rpc.enums.ButtonName;
import com.smartdevicelink.proxy.rpc.enums.DisplayType;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.GlobalProperty;
import com.smartdevicelink.proxy.rpc.enums.HmiZoneCapabilities;
import com.smartdevicelink.proxy.rpc.enums.ImageType;
import com.smartdevicelink.proxy.rpc.enums.InteractionMode;
import com.smartdevicelink.proxy.rpc.enums.Language;
import com.smartdevicelink.proxy.rpc.enums.MobileAppState;
import com.smartdevicelink.proxy.rpc.enums.Result;
import com.smartdevicelink.proxy.rpc.enums.SamplingRate;
import com.smartdevicelink.proxy.rpc.enums.SoftButtonType;
import com.smartdevicelink.proxy.rpc.enums.SpeechCapabilities;
import com.smartdevicelink.proxy.rpc.enums.SystemAction;
import com.smartdevicelink.proxy.rpc.enums.TextAlignment;
import com.smartdevicelink.proxy.rpc.enums.UpdateMode;
import com.smartdevicelink.proxy.rpc.enums.VrCapabilities;
import com.smartdevicelink.tcpdiscovery.TcpDiscoverer;
import com.smartdevicelink.tcpdiscovery.TcpDiscovererCallbackDefaultImplDelegate;
import com.smartdevicelink.transport.TransportType;


public class SmartDeviceLinkTester extends Activity implements OnClickListener, TcpDiscovererCallbackDefaultImplDelegate {
	private static final String VERSION = "$Version:$";
	private static final String logTag = "SmartDeviceLinkTester";
	
	private static final String ButtonSubscriptions = "ButtonSubscriptions";

	private static final String SubscribeVehicleData = "SubscribeVehicleData";
	private static final String UnsubscribeVehicleData = "UnsubscribeVehicleData";
	/**
	 * The name of the file where all the data coming with
	 * {@link OnAudioPassThru} notifications is saved. The root directory is the
	 * external storage.
	 */
	private static final String AUDIOPASSTHRU_OUTPUT_FILE_PCM = "audiopassthru.pcm";
	private static final String AUDIOPASSTHRU_OUTPUT_FILE_WAV = "audiopassthru.wav";

	private static final int ALERT_MAXSOFTBUTTONS = 4;
	private static final int SCROLLABLEMESSAGE_MAXSOFTBUTTONS = 8;
	private static final int SHOW_MAXSOFTBUTTONS = 8;
	private static final int ALERTMANEUVER_MAXSOFTBUTTONS = 3;
	private static final int SHOWCONSTANTTBT_MAXSOFTBUTTONS = 3;
	private static final int UPDATETURNLIST_MAXSOFTBUTTONS = 1;
	private static final int WAV = 0;
	private static final int PCM = 1;

	private static final int REQUEST_PUTFILE_OPEN = 42;
	
	private static final int PUTFILE_MAXFILESIZE = 4 * 1024 * 1024; // 4MB

    private static SmartDeviceLinkTester _activity;
    private static ArrayList<LogMessage> _logMessages = new ArrayList<LogMessage>();
	private static logAdapter _msgAdapter;	
	private ModuleTest _testerMain;
	private static byte[] _ESN;
	
	private static ProfileManagerTests _profileManagerTests;
	private TcpDiscoverer mDiscoverer;
	    
	private ScrollView _scroller = null;
	private ListView _listview = null;
	
	private ArrayAdapter<smartdevicelinkSubMenu> _submenuAdapter = null;
	private ArrayAdapter<Integer> _commandAdapter = null;
	private Map<Integer, Integer> _commandIdToParentSubmenuMap = null;
	private ArrayAdapter<Integer> _choiceSetAdapter = null;
	private ArrayAdapter<String> _putFileAdapter = null;
	private static String sProtocolInfo = "Information currently unavailable";
	
	private static final int CHOICESETID_UNSET = -1;
	/**
	 * Latest choiceSetId, required to add it to the adapter when a successful
	 * CreateInteractionChoiceSetResponse comes.
	 */
	private int _latestCreateChoiceSetId = CHOICESETID_UNSET;
	/**
	 * Latest choiceSetId, required to delete it from the adapter when a
	 * successful DeleteInteractionChoiceSetResponse comes.
	 */
	private int _latestDeleteChoiceSetId = CHOICESETID_UNSET;
	/**
	 * Latest smartdevicelinkSubMenu, required to delete the submenu from the adapter
	 * when a successful DeleteSubMenuResponse comes.
	 */
	private smartdevicelinkSubMenu _latestDeleteSubmenu = null;
	/**
	 * Latest smartdevicelinkSubMenu, required to add the submenu from the adapter when a
	 * successful AddSubMenuResponse comes.
	 */
	private smartdevicelinkSubMenu _latestAddSubmenu = null;
	
	/** List of function names supported in protocol v1. */
	private Vector<String> v1Functions = null;

	private int autoIncCorrId = 101;
	private int autoIncChoiceSetId = 1;
	private int autoIncChoiceSetIdCmdId = 1;
	private int itemcmdID = 1;
	private int submenucmdID = 1000;

	private ArrayAdapter<ButtonName> _buttonAdapter = null;
	private boolean[] isButtonSubscribed = null;

	private ArrayAdapter<VehicleSubscribe> _vehicleSubscribeData = null;
	private ArrayAdapter<GetVehicleType> _getVehicleType = null;
	
	/**
	 * List of soft buttons for current function. Passed between
	 * {@link SoftButtonsListActivity} and this activity.
	 */
	private Vector<SoftButton> currentSoftButtons;
	/**
	 * The Include Soft Buttons checkbox in the current dialog. Kept here to
	 * check it when the user has explicitly set the soft buttons.
	 */
	private CheckBox chkIncludeSoftButtons;
	/**
	 * Reference to PutFile dialog's local filename text field, so that the
	 * filename is set after choosing.
	 */
	private EditText txtLocalFileName;

	/**
	 * Stores the number of selections of each message to sort them by
	 * most-popular usage.
	 */
	private Map<String, Integer> messageSelectCount;
	private static final String MSC_PREFIX = "msc_";

	/** The output stream to write audioPassThru data. */
	private OutputStream audioPassThruOutStream = null;
	/** Media player for the stream of audio pass thru. */
	private MediaPlayer audioPassThruMediaPlayer = null;
	/**
	 * The most recent sent PerformAudioPassThru message, saved in case we need
	 * to retry the request.
	 */
	private PerformAudioPassThru latestPerformAudioPassThruMsg = null;

	/** Autoincrementing id for new softbuttons. */
	private static int autoIncSoftButtonId = 5500;

	/**
	 * In onCreate() specifies if it is the first time the activity is created
	 * during this app launch.
	 */
	private static boolean isFirstActivityRun = true;
	
    private int mySampleRate = 0;
    private int myBitsPerSample = 0;    
    private int iByteCount = 0;
	private boolean bSaveWave = false;

	public enum VehicleSubscribe {
		gps, 
		speed, 
		rpm, 
		fuelLevel, 
		fuelLevel_State, 
		instantFuelConsumption,
		externalTemperature, 
		prndl, 
		tirePressure, 
		odometer, 
		beltStatus, 
		bodyInformation,
		deviceStatus, 
		driverBraking,
		eCallInfo,
		airBagStatus,
		emergencyEvent,
		clusterModeStatus,
		myKey;
			public static VehicleSubscribe valueForString(String value) {
	        return valueOf(value);
	    }
	}
	
	
	public enum GetVehicleType {
		gps, 
		speed, 
		rpm, 
		fuelLevel, 
		fuelLevel_State, 
		instantFuelConsumption,
		externalTemperature, 
		vin,
		prndl, 
		tirePressure, 
		odometer, 
		beltStatus, 
		bodyInformation,
		deviceStatus, 
		driverBraking,
		eCallInfo,
		airBagStatus,
		emergencyEvent,
		clusterModeStatus,
		myKey;
			public static GetVehicleType valueForString(String value) {
	        return valueOf(value);
	    }
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
    	_activity = this;

		//_msgAdapter.logMessage("SmartDeviceLinkTester activity was started.", Log.DEBUG);

		setContentView(R.layout.main);
		_scroller = (ScrollView) findViewById(R.id.scrollConsole);

		((Button) findViewById(R.id.btnSendMessage)).setOnClickListener(this);
		((Button) findViewById(R.id.btnPlayPause)).setOnClickListener(this);
	    ((Button) findViewById(R.id.btnSendPMMessage)).setOnClickListener(this);
	    ((Button) findViewById(R.id.btnPMTests)).setOnClickListener(this);
		
		resetAdapters();

		_getVehicleType = new ArrayAdapter<GetVehicleType>(this,
				android.R.layout.simple_spinner_item, GetVehicleType.values());
		_getVehicleType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		_vehicleSubscribeData = new ArrayAdapter<VehicleSubscribe>(this,
				android.R.layout.simple_spinner_item, VehicleSubscribe.values());
		_vehicleSubscribeData
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		
		_listview = (ListView) findViewById(R.id.messageList);
		_msgAdapter = new logAdapter(logTag, false, this, R.layout.row, _logMessages);
		_profileManagerTests = new ProfileManagerTests(_msgAdapter, this);
		
		_listview.setClickable(true);
		_listview.setAdapter(_msgAdapter);
		_listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		_listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object listObj = parent.getItemAtPosition(position);
				if (listObj instanceof RPCLogMessage) {
					RPCMessage message = ((RPCLogMessage) listObj).getMessage();
					AlertDialog.Builder builder = new AlertDialog.Builder(SmartDeviceLinkTester.this);
					String rawJSON = "";
					
					Integer corrId = -1;
					if (message instanceof RPCRequest) {
						corrId = ((RPCRequest) message).getCorrelationID();
					} else if (message instanceof RPCResponse) {
						corrId = ((RPCResponse) message).getCorrelationID();
					}
					
					rawJSON = ProxyService.getInstance().getProxyInstance().serializeJSON(message);
					if (rawJSON != null)
					{
						builder.setTitle("Raw JSON" + (corrId != -1 ? " (Corr ID " + corrId + ")" : ""));					
					}
					else
					{
						try {
								rawJSON = message.getFunctionName() + 
								" (" + message.getMessageType() + ")";
							} 
						catch (Exception e1) 
						{
							rawJSON = "Undefined";
						}
					}					
					builder.setMessage(rawJSON);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog ad = builder.create();
					ad.show();
				} else if (listObj instanceof StringLogMessage){
					AlertDialog.Builder builder = new AlertDialog.Builder(SmartDeviceLinkTester.this);
					
					String sMessageText = ((StringLogMessage) listObj).getData();					
					if (sMessageText.equals(""))
					{
						sMessageText = ((StringLogMessage) listObj).getMessage();
					}															
					builder.setMessage(sMessageText);					
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					AlertDialog ad = builder.create();
					ad.show();
				}
			}
		});
		
		if (isFirstActivityRun) {
			selectProtocolUI();
		} else {
			startsmartdevicelinkProxy();
		}

		loadMessageSelectCount();
		
		isFirstActivityRun = false;		
	}

	private void loadMessageSelectCount() {
		SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME, 0);
		messageSelectCount = new Hashtable<String, Integer>();
		for (Entry<String, ?> entry : prefs.getAll().entrySet()) {
			if (entry.getKey().startsWith(MSC_PREFIX)) {
				messageSelectCount.put(
						entry.getKey().substring(MSC_PREFIX.length()),
						(Integer) entry.getValue());
			}
		}
	}

	private void saveMessageSelectCount() {
		if (messageSelectCount == null) {
			return;
		}

		SharedPreferences.Editor editor = getSharedPreferences(
				Const.PREFS_NAME, 0).edit();
		for (Entry<String, Integer> entry : messageSelectCount.entrySet()) {
			editor.putInt(MSC_PREFIX + entry.getKey(), entry.getValue());
		}
		editor.commit();
	}

	private void clearMessageSelectCount() {
		messageSelectCount = new Hashtable<String, Integer>();
		SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		for (Entry<String, ?> entry : prefs.getAll().entrySet()) {
			if (entry.getKey().startsWith(MSC_PREFIX)) {
				editor.remove(entry.getKey());
			}
		}
		editor.commit();
	}

	/**
	 * Shows a dialog where the user can select connection features (protocol
	 * version, media flag, app name, language, HMI language, and transport
	 * settings). Starts the proxy after selecting.
	 */
	private void selectProtocolUI() {
		Context context = this;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.selectprotocol,
				(ViewGroup) findViewById(R.id.selectprotocol_Root));

		ArrayAdapter<Language> langAdapter = new ArrayAdapter<Language>(this,
				android.R.layout.simple_spinner_item, Language.values());
		langAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);			
		
		final CheckBox mediaCheckBox = (CheckBox) view
				.findViewById(R.id.selectprotocol_checkMedia);
		final EditText appNameEditText = (EditText) view
				.findViewById(R.id.selectprotocol_appName);
		final EditText appSynonym1EditText = (EditText) view
				.findViewById(R.id.selectprotocol_appSynonym1);
		final EditText appSynonym2EditText = (EditText) view
				.findViewById(R.id.selectprotocol_appSynonym2);			
		
		
		final EditText appTTSNameEditText = (EditText) view
				.findViewById(R.id.txtTTSName);	
		

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
		ArrayAdapter<SpeechCapabilities> speechSpinnerAdapter = new ArrayAdapter<SpeechCapabilities>(adapter.getContext(), android.R.layout.simple_spinner_item, SpeechCapabilities.values()); 		
		final Spinner spnSpeakType1 = (Spinner) view.findViewById(R.id.spnTTSType);							
		spnSpeakType1.setAdapter(speechSpinnerAdapter);		
		
		
		
		final EditText appIdEditText = (EditText)view
				.findViewById(R.id.selectprotocol_appId);
		final EditText esnEditText = (EditText)view
				.findViewById(R.id.selectprotocol_esn);
		final Spinner langSpinner = (Spinner) view
				.findViewById(R.id.selectprotocol_lang);
		final Spinner hmiLangSpinner = (Spinner) view
				.findViewById(R.id.selectprotocol_hmiLang);
		final RadioGroup transportGroup = (RadioGroup) view
				.findViewById(R.id.selectprotocol_radioGroupTransport);
		final EditText ipAddressEditText = (EditText) view
				.findViewById(R.id.selectprotocol_ipAddr);
		final EditText tcpPortEditText = (EditText) view
				.findViewById(R.id.selectprotocol_tcpPort);
		final CheckBox autoReconnectCheckBox = (CheckBox) view
				.findViewById(R.id.selectprotocol_checkAutoReconnect);
		final CheckBox autoSetAppIconCheckBox = (CheckBox) view
				.findViewById(R.id.selectprotocol_checkAutoSetAppIcon);

		ipAddressEditText.setEnabled(false);
		tcpPortEditText.setEnabled(false);
		autoReconnectCheckBox.setEnabled(false);

		transportGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						boolean transportOptionsEnabled = checkedId == R.id.selectprotocol_radioWiFi;
						ipAddressEditText.setEnabled(transportOptionsEnabled);
						tcpPortEditText.setEnabled(transportOptionsEnabled);
						autoReconnectCheckBox
								.setEnabled(transportOptionsEnabled);
					}
				});

		langSpinner.setAdapter(langAdapter);
		hmiLangSpinner.setAdapter(langAdapter);

		// display current configs
		final SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME,
				0);
		boolean isMedia = prefs.getBoolean(Const.PREFS_KEY_ISMEDIAAPP,
				Const.PREFS_DEFAULT_ISMEDIAAPP);
		String appName = prefs.getString(Const.PREFS_KEY_APPNAME,
				Const.PREFS_DEFAULT_APPNAME);		
		String appSynonym1 = prefs.getString(Const.PREFS_KEY_APPSYNONYM1,
				Const.PREFS_DEFAULT_APPSYNONYM1);
		String appSynonym2 = prefs.getString(Const.PREFS_KEY_APPSYNONYM2,
				Const.PREFS_DEFAULT_APPSYNONYM2);
		String appTTSTextName  = prefs.getString(Const.PREFS_KEY_APP_TTS_TEXT,
				Const.PREFS_DEFAULT_APP_TTS_TEXT);		
		
		SpeechCapabilities appTTSTextType = SpeechCapabilities.valueForString(prefs.getString(Const.PREFS_KEY_APP_TTS_TYPE, Const.PREFS_DEFAULT_APP_TTS_TYPE));		
		
		String appId = prefs.getString(Const.PREFS_KEY_APPID,
				Const.PREFS_DEFAULT_APPID);
		String esn = prefs.getString(Const.PREFS_KEY_ESN,
				Const.PREFS_DEFAULT_ESN);
		Language lang = Language.valueOf(prefs.getString(Const.PREFS_KEY_LANG,
				Const.PREFS_DEFAULT_LANG));
		Language hmiLang = Language.valueOf(prefs.getString(
				Const.PREFS_KEY_HMILANG, Const.PREFS_DEFAULT_HMILANG));
		int transportType = prefs.getInt(
				Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_TYPE);
		String ipAddress = prefs.getString(
				Const.Transport.PREFS_KEY_TRANSPORT_IP,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_IP);
		int tcpPort = prefs.getInt(Const.Transport.PREFS_KEY_TRANSPORT_PORT,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_PORT);
		boolean autoReconnect = prefs.getBoolean(
				Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
				Const.Transport.PREFS_DEFAULT_TRANSPORT_RECONNECT_DEFAULT);
		boolean autoSetAppIcon = prefs.getBoolean(
				Const.PREFS_KEY_AUTOSETAPPICON,
				Const.PREFS_DEFAULT_AUTOSETAPPICON);		
			
		mediaCheckBox.setChecked(isMedia);
		appNameEditText.setText(appName);
		appSynonym1EditText.setText(appSynonym1);
		appSynonym2EditText.setText(appSynonym2);
		appTTSNameEditText.setText(appTTSTextName);
		appIdEditText.setText(appId);
		esnEditText.setText(esn);
		spnSpeakType1.setSelection(speechSpinnerAdapter.getPosition(appTTSTextType));
		langSpinner.setSelection(langAdapter.getPosition(lang));
		hmiLangSpinner.setSelection(langAdapter.getPosition(hmiLang));
		transportGroup
				.check(transportType == Const.Transport.KEY_TCP ? R.id.selectprotocol_radioWiFi
						: R.id.selectprotocol_radioBT);
		ipAddressEditText.setText(ipAddress);
		tcpPortEditText.setText(String.valueOf(tcpPort));
		autoReconnectCheckBox.setChecked(autoReconnect);
		autoSetAppIconCheckBox.setChecked(autoSetAppIcon);

		new AlertDialog.Builder(context)
				.setTitle("Please select protocol properties")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						boolean isMedia = mediaCheckBox.isChecked();
						String appName = appNameEditText.getText().toString();
						String appSynonym1 = appSynonym1EditText.getText().toString();
						String appSynonym2 = appSynonym2EditText.getText().toString();
						String appTTSTextName = appTTSNameEditText.getText().toString();
						String appTTSType = ((SpeechCapabilities) spnSpeakType1.getSelectedItem()).name();
						String appId = appIdEditText.getText().toString();
						String esn = esnEditText.getText().toString();
						String lang = ((Language) langSpinner.getSelectedItem())
								.name();
						String hmiLang = ((Language) hmiLangSpinner
								.getSelectedItem()).name();
                        int transportType = 0;
                        switch(transportGroup.getCheckedRadioButtonId()){
                        case R.id.selectprotocol_radioWiFi:
                            transportType = Const.Transport.KEY_TCP;
                            break;
                        case R.id.selectprotocol_radioWiFiAuto:
                            transportType = Const.Transport.KEY_TCP_AUTO;
                            break;
                        case R.id.selectprotocol_radioBT:
                            transportType = Const.Transport.KEY_BLUETOOTH;
                            break;
                        }
						String ipAddress = ipAddressEditText.getText()
								.toString();
						int tcpPort = Integer.parseInt(tcpPortEditText
								.getText().toString());
						boolean autoReconnect = autoReconnectCheckBox
								.isChecked();
						boolean autoSetAppIcon = autoSetAppIconCheckBox
								.isChecked();

						// save the configs
						boolean success = prefs
								.edit()
								.putBoolean(Const.PREFS_KEY_ISMEDIAAPP, isMedia)
								.putString(Const.PREFS_KEY_APPNAME, appName)
								.putString(Const.PREFS_KEY_APPSYNONYM1, appSynonym1)
								.putString(Const.PREFS_KEY_APPSYNONYM2, appSynonym2)
								.putString(Const.PREFS_KEY_APP_TTS_TEXT, appTTSTextName)
								.putString(Const.PREFS_KEY_APP_TTS_TYPE, appTTSType)
								.putString(Const.PREFS_KEY_APPID, appId)
								.putString(Const.PREFS_KEY_ESN, esn)
								.putString(Const.PREFS_KEY_LANG, lang)
								.putString(Const.PREFS_KEY_HMILANG, hmiLang)
								.putInt(Const.Transport.PREFS_KEY_TRANSPORT_TYPE,
								        transportType == Const.Transport.KEY_TCP_AUTO 
								        ? Const.Transport.KEY_TCP : transportType)
								.putString(
										Const.Transport.PREFS_KEY_TRANSPORT_IP,
										ipAddress)
								.putInt(Const.Transport.PREFS_KEY_TRANSPORT_PORT,
										tcpPort)
								.putBoolean(
										Const.Transport.PREFS_KEY_TRANSPORT_RECONNECT,
										autoReconnect)
								.putBoolean(Const.PREFS_KEY_AUTOSETAPPICON,
										autoSetAppIcon).commit();
						if (success)
						{
							sProtocolInfo  = "Name: " + appName + "\r\n";
							sProtocolInfo += "Media: " + (isMedia ? "true" : "false") + "\r\n";		
							sProtocolInfo += "App ID: " + appId + "\r\n" + "\r\n";
							sProtocolInfo += "Synonym1: " + appSynonym1 + "\r\n";
							sProtocolInfo += "Synonym2: " + appSynonym2 + "\r\n";
							sProtocolInfo += "TTSTextName: " + appTTSTextName + "\r\n";
							
							sProtocolInfo += "ESN: " + esn + "\r\n";
							sProtocolInfo += "Language: " + prefs.getString(Const.PREFS_KEY_LANG, Const.PREFS_DEFAULT_LANG) + "\r\n";
							sProtocolInfo += "HMI Language: " + prefs.getString(Const.PREFS_KEY_HMILANG, Const.PREFS_DEFAULT_HMILANG) + "\r\n" + "\r\n";
							
							sProtocolInfo += "Transport: " + (transportType == Const.Transport.KEY_TCP ? "WiFi" : "BT") + "\r\n";
							sProtocolInfo += "IP Address: " + ipAddress + "\r\n";
							sProtocolInfo += "Port: " + tcpPort + "\r\n";
							sProtocolInfo += "Auto Reconnect: " + (autoReconnect ? "true" : "false") + "\r\n";
							sProtocolInfo += "Auto Set App Icon: " + (autoSetAppIcon ? "true" : "false") + "\r\n";
						}						
						else {
							Log.w(logTag,
									"Can't save selected protocol properties");
						}

	                    if (transportType == Const.Transport.KEY_TCP_AUTO) {
	                        mDiscoverer = new TcpDiscoverer(SmartDeviceLinkTester.this, SmartDeviceLinkTester.this);
	                        mDiscoverer.performSearch();
	                    } else {
	                        startsmartdevicelinkProxy();
	                    }
					}
				}).setView(view).show();
	}

	/** Starts the smartdevicelink proxy at startup after selecting protocol features. */
	private void startsmartdevicelinkProxy() {
		// Publish an SDP record and create a smartdevicelink proxy.
		// startsmartdevicelinkProxyService();
		if (ProxyService.getInstance() == null) {
			Intent startIntent = new Intent(SmartDeviceLinkTester._activity, ProxyService.class);
			startService(startIntent);
			// bindService(startIntent, this, Context.BIND_AUTO_CREATE);
		} else {
			// need to get the instance and add myself as a listener
			ProxyService.getInstance().setCurrentActivity(SmartDeviceLinkTester._activity);
		}
	}

	/**
	 * Initializes/resets the adapters keeping created submenus, interaction
	 * choice set ids, etc.
	 */
	private void resetAdapters() {
		// set up storage for subscription records
		// ignoring the last CUSTOM_NAME value
		List<ButtonName> subscribableButtonNames = Arrays.asList(ButtonName.values()).
				subList(0, ButtonName.values().length - 1);
		isButtonSubscribed = new boolean[subscribableButtonNames.size()];
		_buttonAdapter = new ArrayAdapter<ButtonName>(this,
				android.R.layout.select_dialog_multichoice, subscribableButtonNames) {
			public View getView(int position, View convertView, ViewGroup parent) {
				CheckedTextView ret = (CheckedTextView) super.getView(position,
						convertView, parent);
				ret.setChecked(isButtonSubscribed[position]);
				return ret;
			}
		};
		
		_submenuAdapter = new ArrayAdapter<smartdevicelinkSubMenu>(this,
				android.R.layout.select_dialog_item);
		_submenuAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Add top level menu with parent ID zero
		smartdevicelinkSubMenu sm = new smartdevicelinkSubMenu();
		sm.setName("Top Level Menu");
		sm.setSubMenuId(0);
		addSubMenuToList(sm);

		_commandAdapter = new ArrayAdapter<Integer>(this,
				android.R.layout.select_dialog_item);
		_commandAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		_commandIdToParentSubmenuMap = new Hashtable<Integer, Integer>();

		_choiceSetAdapter = new ArrayAdapter<Integer>(this,
				android.R.layout.select_dialog_item);
		_choiceSetAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		_putFileAdapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item);
		_putFileAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/** Displays the current protocol properties. */
	private void showProtocolProperties() {

		showDialog("Protocol Properties", sProtocolInfo);
	}

	/** Displays the current protocol properties. */	
	protected void onDestroy() {
		super.onDestroy();
		//endsmartdevicelinkProxyInstance();
		saveMessageSelectCount();
		_activity = null;
		ProxyService service = ProxyService.getInstance();
		if (service != null) {
			service.setCurrentActivity(null);
		}
		closeAudioPassThruStream();
		closeAudioPassThruMediaPlayer();
	}
	
	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
			case 1:
				builder.setTitle("Raw JSON");
				builder.setMessage("This is the raw JSON message here");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog dialog1 = builder.create();
				dialog = dialog1;
				break;
			case 2:
				break;
			default:
				dialog = null;
		}
		return dialog;
	}
	private final int PROXY_START = R.id.proxystart;
	private final int XML_TEST = R.id.xmltest;
	private final int MNU_TOGGLE_CONSOLE = R.id.toggleconsole;
	private final int MNU_CLEAR = R.id.clearmessages;
	private final int MNU_EXIT = R.id.exit;
	private final int MNU_TOGGLE_MEDIA = 12;
	private final int MNU_UNREGISTER = R.id.unregister;	
	private final int MNU_APP_VERSION = R.id.appversion;
	private final int MNU_CLEAR_FUNCTIONS_USAGE = R.id.resetfunctionusage;
	private final int MNU_WAKELOCK = 18;
	private final int MNU_RAI_INFO = R.id.raiinformation;
	private final int MNU_SHOW_PROP = R.id.showprotocolproperties;
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return result;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	private boolean getIsMedia() {
		return getSharedPreferences(Const.PREFS_NAME, 0).getBoolean(
				Const.PREFS_KEY_ISMEDIAAPP, Const.PREFS_DEFAULT_ISMEDIAAPP);
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
        case PROXY_START:
	        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	        if (!mBtAdapter.isEnabled()) mBtAdapter.enable();
	        
	        if (ProxyService.getInstance() == null) {
                Intent startIntent = new Intent(this, ProxyService.class);
                startService(startIntent);
	        } else {
                ProxyService.getInstance().setCurrentActivity(this);
	        }
	        
	        if (ProxyService.getInstance().getProxyInstance() != null) {
                try {
                        ProxyService.getInstance().getProxyInstance().resetProxy();
                } catch (SmartDeviceLinkException e) {}
	        }
	        
	        if (!mBtAdapter.isDiscovering()) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
	        }
			return true;

		case XML_TEST:
			if (_testerMain != null) {
				_testerMain.restart();
				Toast.makeText(getApplicationContext(), "start your engines", Toast.LENGTH_SHORT).show();
			}else {
				ProxyService.getInstance().startModuleTest();
				_testerMain.restart();
				Toast.makeText(getApplicationContext(), "Start the app on smartdevicelink first", Toast.LENGTH_LONG).show();
			}
			break;
		case MNU_EXIT:
			exitApp();
			break;
		case MNU_TOGGLE_CONSOLE:
			if (_scroller.getVisibility() == ScrollView.VISIBLE) {
				_scroller.setVisibility(ScrollView.GONE);
				_listview.setVisibility(ListView.VISIBLE);
			} else {
				_scroller.setVisibility(ScrollView.VISIBLE);
				_listview.setVisibility(ListView.GONE);
			}
			return true;
		case MNU_CLEAR:
			_msgAdapter.clear();
			return true;
		case MNU_TOGGLE_MEDIA:
			SharedPreferences settings = getSharedPreferences(Const.PREFS_NAME, 0);
			boolean isMediaApp = settings.getBoolean(Const.PREFS_KEY_ISMEDIAAPP, Const.PREFS_DEFAULT_ISMEDIAAPP);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Const.PREFS_KEY_ISMEDIAAPP, !isMediaApp);

			// Don't forget to commit your edits!!!
			editor.commit();
			//super.finish();
			return true;
		case MNU_UNREGISTER:
			/*
			endsmartdevicelinkProxyInstance();
        	startsmartdevicelinkProxyService();
        	*/		
			if (ProxyService.getInstance() == null) {
	    			Intent startIntent = new Intent(this, ProxyService.class);
	    			startService(startIntent);
	    			//bindService(startIntent, this, Context.BIND_AUTO_CREATE);
	    		} else {
	    			// need to get the instance and add myself as a listener
	    			ProxyService.getInstance().setCurrentActivity(this);
	    		}
	        if (ProxyService.getInstance().getProxyInstance() != null) {
				try {
					ProxyService.getInstance().getProxyInstance().resetProxy();
				} catch (SmartDeviceLinkException e) {}
	        }
			return true;
		case MNU_APP_VERSION: {
			showAppVersion();
			break;
		}
		case MNU_RAI_INFO: {
			showRAIInfo();
			break;
		}
		case MNU_SHOW_PROP: {
			showProtocolProperties();
			break;
		}
		case MNU_CLEAR_FUNCTIONS_USAGE:
			clearMessageSelectCount();
			break;
		case MNU_WAKELOCK:
			toggleDisableLock();
			break;
		}

		return false;
	}
	
	/**
	 * Toggles the current state of the disable lock when testing flag, and
	 * writes it to the preferences.
	 */
	private void toggleDisableLock() {
		SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME, 0);
		boolean disableLock = prefs.getBoolean(
				Const.PREFS_KEY_DISABLE_LOCK_WHEN_TESTING,
				Const.PREFS_DEFAULT_DISABLE_LOCK_WHEN_TESTING);
		disableLock = !disableLock;
		prefs.edit()
				.putBoolean(Const.PREFS_KEY_DISABLE_LOCK_WHEN_TESTING,
						disableLock).commit();
	}

	/**
	 * Returns the current state of the disable lock when testing flag.
	 * 
	 * @return true if the screen lock is disabled
	 */
	public boolean getDisableLockFlag() {
		return getSharedPreferences(Const.PREFS_NAME, 0).getBoolean(
				Const.PREFS_KEY_DISABLE_LOCK_WHEN_TESTING,
				Const.PREFS_DEFAULT_DISABLE_LOCK_WHEN_TESTING);
	}

	/** Closes the activity and stops the proxy service. */
	private void exitApp() {
		stopService(new Intent(this, ProxyService.class));
		finish();
		saveMessageSelectCount();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}, 1000);
	}

	private String getAssetsContents(String filename, String defaultString) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(getAssets().open(
					filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
		} catch (IOException e) {
			Log.d(logTag, "Can't open file with build info", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return builder.length() > 0 ? builder.toString().trim() : defaultString;
	}

	private void showAppVersion() {
		String appVersion;
		try {
			appVersion = getPackageManager()
					.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.d(logTag, "Can't get package info", e);
			appVersion = "Unknown";
		}
		String buildInfo = getAssetsContents("build.info",
				"Build info not available");
		String changelog = getAssetsContents("CHANGELOG.txt",
				"Changelog not available");
		
		String myVersion = null; 		
		try {
			if (ProxyService.getProxyInstance() != null)
			{
				myVersion = "Proxy Lib Ver: " + ProxyService.getProxyInstance().getProxyVersionInfo();
			}
		} catch (SmartDeviceLinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (myVersion == null)
		{
			myVersion = "Proxy Lib Version Info not available";
		}
		
		new AlertDialog.Builder(this)
				.setTitle("App version")
				.setMessage(
						appVersion + ", " + buildInfo + "\n\nCHANGELOG:\n"
								+ changelog +"\n\n" + myVersion + "\n")
				.setNeutralButton(android.R.string.ok, null).create().show();
	}

	
	private void showDialog(String sTitle, String sBody)
	{
		new AlertDialog.Builder(this)
		.setTitle(sTitle)
		.setMessage(sBody)
		.setNeutralButton(android.R.string.ok, null).create().show();
	}
	
	private void showRAIInfo() 
	{			 	
		SmartDeviceLinkProxyALM myProxy = ProxyService.getProxyInstance();
		
		DisplayCapabilities theDisplay = null;		
		String sDisplay = "Information unavailable";
		
		Vector<ButtonCapabilities> vButtons = null;
		ButtonCapabilities theButton = null;
		ButtonName theButtonName = null;
		String sButtons = "Information unavailable";
						
		Vector<HmiZoneCapabilities> vHMIZone = null;
		HmiZoneCapabilities theHMIZone = null;
		String sHMIZone = "Information unavailable";		
		
		Vector<SpeechCapabilities> vSpeech = null;
		SpeechCapabilities theSpeech = null;
		String sSpeech = "Information unavailable";
		
		Language thesmartdevicelinkLanguage = null;
		String ssmartdevicelinkLanguage = "Information unavailable";
		
		Language theHMIDispLanguage = null;
		String sHMIDispLanguage = "Information unavailable";
		
		Vector<VrCapabilities> vVR = null;
		VrCapabilities theVR = null;
		String sVR = "Information unavailable";
		
		VehicleType theVehicleType = null;
		String sMake  = "Information unavailable";
		String sModel = "Information unavailable";
		String sYear  = "Information unavailable";
		
		String sOutput;	
		try 
		{
			if (myProxy != null)
			{				
				boolean bIsRegistered = myProxy.getAppInterfaceRegistered();			
				if (!bIsRegistered) 
				{
					showDialog("RAI Information", "Information currently unavailable");
					return;	
				}
				theDisplay 			= myProxy.getDisplayCapabilities();
				vButtons   			= myProxy.getButtonCapabilities();
				vHMIZone   			= myProxy.getHmiZoneCapabilities();	
				vSpeech	   			= myProxy.getSpeechCapabilities();
				thesmartdevicelinkLanguage 	= myProxy.getSmartDeviceLinkLanguage();
				theHMIDispLanguage	= myProxy.getHmiDisplayLanguage();
				vVR					= myProxy.getVrCapabilities();
				theVehicleType		= myProxy.getVehicleType();
			}
			else
			{
				showDialog("RAI Information", "Information currently unavailable");
				return;
			}
		} 
		catch (SmartDeviceLinkException e) 
		{
			e.printStackTrace();			
			showDialog("RAI Information", "Information currently unavailable");
			return;			
		}
				
		if (theDisplay != null)
		{
			DisplayType mDisplay = theDisplay.getDisplayType();			
			if (mDisplay != null) sDisplay = mDisplay.name();								
		}		
		if (vButtons != null)
		{			
			sButtons = "";
			for (int i = 0; i<vButtons.size(); i++)
			{
				theButton = vButtons.elementAt(i);				
				if (theButton != null)
				{
					theButtonName = theButton.getName();
					if (theButtonName != null) sButtons = theButtonName.name() + " " + sButtons;
				}
			}
		}		
		if (vHMIZone != null)
		{
			sHMIZone = "";
			for (int i = 0; i<vHMIZone.size(); i++)
			{
				theHMIZone = vHMIZone.elementAt(i);				
				if (theHMIZone != null)	sHMIZone = theHMIZone.name() + " " + sHMIZone;					
			}
		}
		if (vSpeech != null)
		{
			sSpeech = "";
			for (int i = 0; i<vSpeech.size(); i++)
			{
				theSpeech = vSpeech.elementAt(i);				
				if (theSpeech != null)	sSpeech = theSpeech.name() + " " + sSpeech;					
			}
		}	
		if (thesmartdevicelinkLanguage != null) ssmartdevicelinkLanguage = thesmartdevicelinkLanguage.name();
		if (theHMIDispLanguage != null) sHMIDispLanguage = theHMIDispLanguage.name();
		if (vVR != null)
		{
			sVR = "";
			for (int i = 0; i<vVR.size(); i++)
			{
				theVR = vVR.elementAt(i);
				if (theVR != null) sVR = theVR.name() + " " + sVR;					
			}
		}
		if (theVehicleType != null)
		{
			if (theVehicleType.getMake() != null) sMake = theVehicleType.getMake();
			if (theVehicleType.getModel() != null) sModel = theVehicleType.getModel();
			if (theVehicleType.getModelYear() != null) sYear = theVehicleType.getModelYear();			
		}
		
		sOutput = "Display Type: " + "\r\n" + sDisplay + "\r\n\r\n" +
				  "Button Capabilities: " + "\r\n" + sButtons + "\r\n\r\n" +
				  "HmiZone Capabilities: " + "\r\n" + sHMIZone + "\r\n\r\n" + 
				  "Speech Capabilities: " + "\r\n" + sSpeech + "\r\n\r\n" +
				  "Language: " + "\r\n" + ssmartdevicelinkLanguage + "\r\n\r\n"+
				  "HMI Display Language: " + "\r\n" + sHMIDispLanguage + "\r\n\r\n"+
				  "VR capabilities: " + "\r\n" + sVR + "\r\n\r\n"+
				  "Make: "  + "\r\n" + sMake  + "\r\n\r\n"  +
				  "Model: " + "\r\n" + sModel + "\r\n\r\n"  + 
				  "Year: "  + "\r\n" + sYear  + "\r\n\r\n";
		
		showDialog("RAI Information", sOutput);		
	}	
	
	
	public void onClick(View v) {
		if (v == findViewById(R.id.btnSendMessage)) {
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
					
					{ public View getView(int position, View convertView, ViewGroup parent) {
		                // User super class to create the View
		                View v = super.getView(position, convertView, parent);
		                TextView tv = (TextView) v.findViewById(android.R.id.text1);		                
		                tv.setTextSize(16);		                
		                return v;
		            }};
			adapter.add(Names.Alert);
			adapter.add(Names.Speak);
			adapter.add(Names.Show);
			adapter.add(ButtonSubscriptions);
			adapter.add(Names.AddCommand);
			adapter.add(Names.DeleteCommand);
			adapter.add(Names.AddSubMenu);
			adapter.add(Names.DeleteSubMenu);
			adapter.add(Names.SetGlobalProperties);
			adapter.add(Names.ResetGlobalProperties);
			adapter.add(Names.SetMediaClockTimer);
			adapter.add(Names.CreateInteractionChoiceSet);
			adapter.add(Names.DeleteInteractionChoiceSet);
			adapter.add(Names.PerformInteraction);
			adapter.add(Names.Slider);
			adapter.add(Names.ScrollableMessage);
			adapter.add(Names.ChangeRegistration);
			adapter.add(Names.PutFile);
			adapter.add(Names.DeleteFile);
			adapter.add(Names.ListFiles);
			adapter.add(Names.SetAppIcon);
			adapter.add(Names.PerformAudioPassThru);
			adapter.add(Names.EndAudioPassThru);
			adapter.add(SubscribeVehicleData);
			adapter.add(UnsubscribeVehicleData);
			adapter.add(Names.GetVehicleData);			
			adapter.add(Names.ReadDID);
			adapter.add(Names.GetDTCs);
			adapter.add(Names.ShowConstantTBT);
			adapter.add(Names.AlertManeuver);
			adapter.add(Names.UpdateTurnList);
			adapter.add(Names.DialNumber);
			
			adapter.sort(new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					// compare based on the number of selections so far
					Integer lCount = messageSelectCount.get(lhs);
					if (lCount == null) {
						lCount = 0;
					}
					Integer rCount = messageSelectCount.get(rhs);
					if (rCount == null) {
						rCount = 0;
					}
					return rCount - lCount;
				}
			});
			

			new AlertDialog.Builder(this)  
		       .setTitle("Pick a Function ")
		       .setAdapter(adapter, new DialogInterface.OnClickListener() {
		    	   
		    	   public void onClick(DialogInterface dialog, int which) {
						if(adapter.getItem(which) == Names.Alert){
							AlertDialog.Builder builder;
							AlertDialog dlg;

							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.alert, null);
							final EditText txtSpeak = (EditText) layout.findViewById(R.id.txtSpeak);
							final EditText txtAlertField1 = (EditText) layout.findViewById(R.id.txtAlertField1);
							final EditText txtAlertField2 = (EditText) layout.findViewById(R.id.txtAlertField2);
							final EditText txtAlertField3 = (EditText) layout.findViewById(R.id.txtAlertField3);
							final EditText txtDuration = (EditText) layout.findViewById(R.id.txtDuration);
							final CheckBox chkPlayTone = (CheckBox) layout.findViewById(R.id.chkPlayTone);
							

							chkIncludeSoftButtons = (CheckBox) layout.findViewById(R.id.chkIncludeSBs);
	
							SoftButton sb1 = new SoftButton();
							sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb1.setText("ReRoute");
							sb1.setType(SoftButtonType.SBT_TEXT);
							sb1.setIsHighlighted(false);
							sb1.setSystemAction(SystemAction.STEAL_FOCUS);
							SoftButton sb2 = new SoftButton();
							sb2.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb2.setText("Close");
							sb2.setType(SoftButtonType.SBT_TEXT);
							sb2.setIsHighlighted(false);
							sb2.setSystemAction(SystemAction.DEFAULT_ACTION);
							currentSoftButtons = new Vector<SoftButton>();
							currentSoftButtons.add(sb1);
							currentSoftButtons.add(sb2);
	
							Button btnSoftButtons = (Button) layout.findViewById(R.id.alert_btnSoftButtons);
							btnSoftButtons.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									IntentHelper.addObjectForKey(currentSoftButtons,
											Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
									Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
									intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
											ALERT_MAXSOFTBUTTONS);
									startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
								}
							});
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String toSpeak = txtSpeak.getText().toString();
									try {
										Alert msg = new Alert();
										msg.setCorrelationID(autoIncCorrId++);
										msg.setAlertText1(txtAlertField1.getText().toString());
										msg.setAlertText2(txtAlertField2.getText().toString());
										msg.setAlertText3(txtAlertField3.getText().toString());
										msg.setDuration(Integer.parseInt(txtDuration.getText().toString()));
										msg.setPlayTone(chkPlayTone.isChecked());
										if (toSpeak.length() > 0) {
											Vector<TTSChunk> ttsChunks = TTSChunkFactory.createSimpleTTSChunks(toSpeak);
											msg.setTtsChunks(ttsChunks);
										}
										if (chkIncludeSoftButtons.isChecked() &&
												(currentSoftButtons != null) &&
												(currentSoftButtons.size() > 0)) {
											msg.setSoftButtons(currentSoftButtons);
										}
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (NumberFormatException e) {
										Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									currentSoftButtons = null;
									chkIncludeSoftButtons = null;
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									currentSoftButtons = null;
									chkIncludeSoftButtons = null;
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();	
						} else if (adapter.getItem(which) == Names.Speak) {
							//something
							AlertDialog.Builder builder;
							AlertDialog dlg;

							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.speak, null);
							final EditText txtSpeakText1 = (EditText) layout.findViewById(R.id.txtSpeakText1);
							final EditText txtSpeakText2 = (EditText) layout.findViewById(R.id.txtSpeakText2);
							final EditText txtSpeakText3 = (EditText) layout.findViewById(R.id.txtSpeakText3);
							final EditText txtSpeakText4 = (EditText) layout.findViewById(R.id.txtSpeakText4);
							
							final Spinner spnSpeakType1 = (Spinner) layout.findViewById(R.id.spnSpeakType1);
							final Spinner spnSpeakType2 = (Spinner) layout.findViewById(R.id.spnSpeakType2);
							final Spinner spnSpeakType3 = (Spinner) layout.findViewById(R.id.spnSpeakType3);
							final Spinner spnSpeakType4 = (Spinner) layout.findViewById(R.id.spnSpeakType4);
							
							ArrayAdapter<SpeechCapabilities> speechSpinnerAdapter = new ArrayAdapter<SpeechCapabilities>(adapter.getContext(), android.R.layout.simple_spinner_item, SpeechCapabilities.values()); 
							spnSpeakType1.setAdapter(speechSpinnerAdapter);
							spnSpeakType2.setAdapter(speechSpinnerAdapter);
							spnSpeakType2.setSelection(3);
							spnSpeakType3.setAdapter(speechSpinnerAdapter);
							spnSpeakType4.setAdapter(speechSpinnerAdapter);
							spnSpeakType4.setSelection(1);
							spnSpeakType4.setEnabled(false);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Speak msg = new Speak();
									msg.setCorrelationID(autoIncCorrId++);
									String speak1 = txtSpeakText1.getText().toString();
									String speak2 = txtSpeakText2.getText().toString();
									String speak3 = txtSpeakText3.getText().toString();
									String speak4 = txtSpeakText4.getText().toString();
									Vector<TTSChunk> chunks = new Vector<TTSChunk>();

									if (speak1.length() > 0) {
										chunks.add(TTSChunkFactory.createChunk((SpeechCapabilities)spnSpeakType1.getSelectedItem(), speak1));
										
									}
									if (speak2.length() > 0) {
										chunks.add(TTSChunkFactory.createChunk((SpeechCapabilities)spnSpeakType2.getSelectedItem(), speak2));
										
									}
									if (speak3.length() > 0) {
										chunks.add(TTSChunkFactory.createChunk((SpeechCapabilities)spnSpeakType3.getSelectedItem(), speak3));
										
									}
									if (speak4.length() > 0) {
										chunks.add(TTSChunkFactory.createChunk(SpeechCapabilities.SAPI_PHONEMES, speak4));
										
									}
									msg.setTtsChunks(chunks);
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.Show) {
							sendShow();
						} else if (adapter.getItem(which) == ButtonSubscriptions) {
							//something
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_buttonAdapter, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
								boolean needToSubscribe = !isButtonSubscribed[which];
									try {
										ButtonName buttonName = ButtonName.values()[which];
										int corrId = autoIncCorrId++;
										if (needToSubscribe) {
											SubscribeButton msg = new SubscribeButton();
											msg.setCorrelationID(corrId);
											msg.setButtonName(buttonName);
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										} else {
											UnsubscribeButton msg = new UnsubscribeButton();
											msg.setCorrelationID(corrId);
											msg.setButtonName(buttonName);
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										}
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									isButtonSubscribed[which] = !isButtonSubscribed[which];
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.AddCommand) {
							//something
							AlertDialog.Builder builder;
							AlertDialog addCommandDialog;

							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.addcommand,
									(ViewGroup) findViewById(R.id.itemRoot));

							final EditText er = (EditText) layout.findViewById(R.id.command);
							final EditText editVrSynonym = (EditText) layout.findViewById(R.id.command2);
							final Spinner s = (Spinner) layout.findViewById(R.id.availableSubmenus);
							s.setAdapter(_submenuAdapter);
							final CheckBox chkUseIcon = (CheckBox) layout.findViewById(R.id.addcommand_useIcon);
							final EditText editIconValue = (EditText) layout.findViewById(R.id.addcommand_iconValue);
							final Spinner spnIconType = (Spinner) layout.findViewById(R.id.addcommand_iconType);
							ArrayAdapter<ImageType> imageTypeAdapter = new ArrayAdapter<ImageType>(
									mContext, android.R.layout.simple_spinner_item, ImageType.values());
							imageTypeAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnIconType.setAdapter(imageTypeAdapter);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									AddCommand msg = new AddCommand();
									msg.setCorrelationID(autoIncCorrId++);
									String itemText = er.getText().toString();
									smartdevicelinkSubMenu sm = new smartdevicelinkSubMenu();
									sm = (smartdevicelinkSubMenu) s.getSelectedItem();
									MenuParams menuParams = new MenuParams();
									menuParams.setMenuName(itemText);
									menuParams.setPosition(0);
									menuParams.setParentID(sm.getSubMenuId());
									msg.setMenuParams(menuParams);
									
									String vrSynonym = editVrSynonym.getText().toString();
									if (vrSynonym.length() > 0) {
										Vector<String> vrCommands = new Vector<String>();
										vrCommands.add(vrSynonym);
										msg.setVrCommands(vrCommands);
									}
									
									if (chkUseIcon.isChecked()) {
										Image icon = new Image();
										icon.setValue(editIconValue.getText().toString());
										icon.setImageType((ImageType) spnIconType.getSelectedItem());
										msg.setCmdIcon(icon);
									}
									
									int cmdID = itemcmdID++;
									msg.setCmdID(cmdID);
									
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									addCommandToList(cmdID, menuParams.getParentID());
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							addCommandDialog = builder.create();
							addCommandDialog.show();
						} else if (adapter.getItem(which) == Names.DeleteCommand) {
							//something
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_commandAdapter, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									DeleteCommand msg = new DeleteCommand();
									msg.setCorrelationID(autoIncCorrId++);
									int cmdID = _commandAdapter.getItem(which);
									msg.setCmdID(cmdID);
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									removeCommandFromList(cmdID);
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.AddSubMenu) {
							//something
							AlertDialog.Builder builder;
							AlertDialog addSubMenuDialog;

							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.addsubmenu,
									(ViewGroup) findViewById(R.id.submenu_Root));

							final EditText subMenu = (EditText) layout.findViewById(R.id.submenu_item);

							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									AddSubMenu msg = new AddSubMenu();
									msg.setCorrelationID(autoIncCorrId++);
									smartdevicelinkSubMenu sm = new smartdevicelinkSubMenu();
									sm.setName(subMenu.getText().toString());
									sm.setSubMenuId(submenucmdID++);
									msg.setMenuID(sm.getSubMenuId());
									msg.setMenuName(sm.getName());
									msg.setPosition(null);
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									
									if (_latestAddSubmenu != null) {
										Log.w(logTag, "Latest addSubmenu should be null, but equals to " + _latestAddSubmenu);
									}
									_latestAddSubmenu = sm;
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							addSubMenuDialog = builder.create();
							addSubMenuDialog.show();
						} else if (adapter.getItem(which) == Names.DeleteSubMenu) {
							//something
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_submenuAdapter, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									smartdevicelinkSubMenu menu = _submenuAdapter.getItem(which);
									if (menu.getSubMenuId() != 0) {
										DeleteSubMenu msg = new DeleteSubMenu();
										msg.setCorrelationID(autoIncCorrId++);
										msg.setMenuID(menu.getSubMenuId());
										try {
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										} catch (SmartDeviceLinkException e) {
											_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
										}

										if (_latestDeleteSubmenu != null) {
											Log.w(logTag, "Latest deleteSubmenu should be null, but equals to " + _latestDeleteSubmenu);
										}
										_latestDeleteSubmenu = menu;
									} else {
										Toast.makeText(getApplicationContext(),
												"Sorry, can't delete top-level menu",
												Toast.LENGTH_LONG).show();
									}
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.SetGlobalProperties) {
							sendSetGlobalProperties();
						} else if (adapter.getItem(which) == Names.ResetGlobalProperties) {
							sendResetGlobalProperties();
						} else if (adapter.getItem(which) == Names.SetMediaClockTimer) {
							//something
							AlertDialog.Builder builder;
							AlertDialog dlg;

							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.setmediaclock, null);
							final EditText txtHours = (EditText) layout.findViewById(R.id.txtHours);
							final EditText txtMinutes = (EditText) layout.findViewById(R.id.txtMinutes);
							final EditText txtSeconds = (EditText) layout.findViewById(R.id.txtSeconds);
							final Spinner spnUpdateMode = (Spinner) layout.findViewById(R.id.spnUpdateMode);
							ArrayAdapter<UpdateMode> spinnerAdapter = new ArrayAdapter<UpdateMode>(adapter.getContext(),
									android.R.layout.simple_spinner_item, UpdateMode.values());
							spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnUpdateMode.setAdapter(spinnerAdapter);
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									SetMediaClockTimer msg = new SetMediaClockTimer();
									msg.setCorrelationID(autoIncCorrId++);
									UpdateMode updateMode =  (UpdateMode)spnUpdateMode.getSelectedItem();
									msg.setUpdateMode(updateMode);
									try {
										Integer hours = Integer.parseInt(txtHours.getText().toString());
										Integer minutes = Integer.parseInt(txtMinutes.getText().toString());
										Integer seconds = Integer.parseInt(txtSeconds.getText().toString());
										StartTime startTime = new StartTime();
										startTime.setHours(hours);
										startTime.setMinutes(minutes);
										startTime.setSeconds(seconds);
										msg.setStartTime(startTime);
									} catch (NumberFormatException e) {
										// skip setting start time if parsing failed
									}
									
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.CreateInteractionChoiceSet) {
							sendCreateInteractionChoiceSet();
						} else if (adapter.getItem(which) == Names.DeleteInteractionChoiceSet) {
							//something
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_choiceSetAdapter, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									DeleteInteractionChoiceSet msg = new DeleteInteractionChoiceSet();
									msg.setCorrelationID(autoIncCorrId++);
									int commandSetID = _choiceSetAdapter.getItem(which);
									msg.setInteractionChoiceSetID(commandSetID);
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										if (_latestDeleteChoiceSetId != CHOICESETID_UNSET) {
											Log.w(logTag, "Latest deleteChoiceSetId should be unset, but equals to " + _latestDeleteChoiceSetId);
										}
										_latestDeleteChoiceSetId = commandSetID;
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.PerformInteraction) {
							sendPerformInteraction();
						} else if (adapter.getItem(which) == Names.Slider) {
							sendSlider();
						} else if (adapter.getItem(which) == Names.ScrollableMessage) {
							//something
							AlertDialog.Builder builder;
							AlertDialog dlg;

							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.scrollablemessage, null);
							final EditText txtScrollableMessageBody = (EditText) layout.findViewById(R.id.txtScrollableMessageBody);
							chkIncludeSoftButtons = (CheckBox) layout.findViewById(R.id.chkIncludeSBs);
							final EditText txtTimeout = (EditText) layout.findViewById(R.id.scrollablemessage_editTimeout);

							SoftButton sb1 = new SoftButton();
							sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb1.setText("Reply");
							sb1.setType(SoftButtonType.SBT_TEXT);
							sb1.setIsHighlighted(false);
							sb1.setSystemAction(SystemAction.STEAL_FOCUS);
							SoftButton sb2 = new SoftButton();
							sb2.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb2.setText("Close");
							sb2.setType(SoftButtonType.SBT_TEXT);
							sb2.setIsHighlighted(false);
							sb2.setSystemAction(SystemAction.DEFAULT_ACTION);
							currentSoftButtons = new Vector<SoftButton>();
							currentSoftButtons.add(sb1);
							currentSoftButtons.add(sb2);

							Button btnSoftButtons = (Button) layout.findViewById(R.id.scrollablemessage_btnSoftButtons);
							btnSoftButtons.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									IntentHelper.addObjectForKey(currentSoftButtons,
											Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
									Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
									intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
											SCROLLABLEMESSAGE_MAXSOFTBUTTONS);
									startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
								}
							});
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									try {
										ScrollableMessage msg = new ScrollableMessage();
										msg.setCorrelationID(autoIncCorrId++);
										try {
											msg.setTimeout(Integer.parseInt(txtTimeout.getText().toString()));
										} catch (NumberFormatException e) {
											// do nothing, leave the default timeout
										}
										msg.setScrollableMessageBody(txtScrollableMessageBody.getEditableText().toString());
										if (chkIncludeSoftButtons.isChecked() &&
												(currentSoftButtons != null) &&
												(currentSoftButtons.size() > 0)) {
											msg.setSoftButtons(currentSoftButtons);
										}
										currentSoftButtons = null;
										chkIncludeSoftButtons = null;
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									currentSoftButtons = null;
									chkIncludeSoftButtons = null;
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.ChangeRegistration) {
							//ChangeRegistration
							AlertDialog.Builder builder;
							AlertDialog dlg;

							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.changeregistration, null);
							
							final Spinner spnLanguage = (Spinner) layout.findViewById(R.id.spnLanguage);
							ArrayAdapter<Language> spinnerAdapterLanguage = new ArrayAdapter<Language>(adapter.getContext(),
									android.R.layout.simple_spinner_item, Language.values());
							spinnerAdapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnLanguage.setAdapter(spinnerAdapterLanguage);
							
							final Spinner spnHmiDisplayLanguage = (Spinner) layout.findViewById(R.id.spnHmiDisplayLanguage);
							ArrayAdapter<Language> spinnerAdapterHmiDisplayLanguage = new ArrayAdapter<Language>(adapter.getContext(),
									android.R.layout.simple_spinner_item, Language.values());
							spinnerAdapterHmiDisplayLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnHmiDisplayLanguage.setAdapter(spinnerAdapterHmiDisplayLanguage);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									try {
										ChangeRegistration msg = new ChangeRegistration();
										msg.setLanguage((Language) spnLanguage.getSelectedItem());
										msg.setHmiDisplayLanguage((Language) spnHmiDisplayLanguage.getSelectedItem());
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.PutFile) {
							//PutFile
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.putfile, null);
							
							final EditText txtsmartdevicelinkFileName = (EditText) layout.findViewById(R.id.sdlFileName);
							
							final Spinner spnFileType = (Spinner) layout.findViewById(R.id.spnFileType);
							ArrayAdapter<FileType> spinnerAdapter = new ArrayAdapter<FileType>(adapter.getContext(),
									android.R.layout.simple_spinner_item, FileType.values());
							spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnFileType.setAdapter(spinnerAdapter);
							
							final CheckBox chkPersistentFile = (CheckBox) layout.findViewById(R.id.chkPersistentFile);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String smartdevicelinkFileName = txtsmartdevicelinkFileName.getText().toString();
									try {
										PutFile msg = new PutFile();
										msg.setSmartDeviceLinkFileName(smartdevicelinkFileName);
										msg.setFileType((FileType) spnFileType.getSelectedItem());
										msg.setPersistentFile(chkPersistentFile.isChecked());
										msg.setCorrelationID(autoIncCorrId++);
										
									    Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.fiesta);
								        ByteArrayOutputStream bas = new ByteArrayOutputStream();
									    photo.compress(CompressFormat.JPEG, 100, bas);
								        byte[] data = new byte[bas.toByteArray().length];
								        data = bas.toByteArray();
								        
								        msg.setBulkData(data);
										
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									_putFileAdapter.add(smartdevicelinkFileName);
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.DeleteFile) {
							//DeleteFile
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_putFileAdapter, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									String smartdevicelinkFileName = _putFileAdapter.getItem(which);
									
									try {
										DeleteFile msg = new DeleteFile();
										msg.setSmartDeviceLinkFileName(smartdevicelinkFileName);
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									_putFileAdapter.remove(smartdevicelinkFileName);
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.ListFiles) {
							//ListFiles
							try {
								ListFiles msg = new ListFiles();
								msg.setCorrelationID(autoIncCorrId++);
								_msgAdapter.logMessage(new RPCLogMessage(msg), true);
								ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
							} catch (SmartDeviceLinkException e) {
								_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
							}
						} else if (adapter.getItem(which) == Names.SetAppIcon) {
							//SetAppIcon
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.setappicon, null);
							
							final EditText txtsmartdevicelinkFileName = (EditText) layout.findViewById(R.id.sdlFileNameIcon);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String smartdevicelinkFileName = txtsmartdevicelinkFileName.getText().toString();
									try {
										SetAppIcon msg = new SetAppIcon();
										msg.setSmartDeviceLinkFileName(smartdevicelinkFileName);
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.PerformAudioPassThru) {
							//PerformAudioPassThru
							AlertDialog.Builder builder;
							AlertDialog dlg;

							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.performaudiopassthru, null);

							final EditText txtInitialPrompt = (EditText) layout.findViewById(R.id.txtInitialPrompt);

							final CheckBox wavfileCheckBox = (CheckBox) layout.findViewById(R.id.savewave_checkMedia);														
							
							final EditText txtAudioPassThruDisplayText1 = (EditText) layout.findViewById(R.id.txtAudioPassThruDisplayText1);

							final EditText txtAudioPassThruDisplayText2 = (EditText) layout.findViewById(R.id.txtAudioPassThruDisplayText2);
							
							final Spinner spnSamplingRate = (Spinner) layout.findViewById(R.id.spnSamplingRate);
							ArrayAdapter<SamplingRate> spinnerAdapterSamplingRate = new ArrayAdapter<SamplingRate>(adapter.getContext(),
									android.R.layout.simple_spinner_item, SamplingRate.values());
							spinnerAdapterSamplingRate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnSamplingRate.setAdapter(spinnerAdapterSamplingRate);

							final EditText txtMaxDuration = (EditText) layout.findViewById(R.id.txtMaxDuration);
							
							final Spinner spnBitsPerSample = (Spinner) layout.findViewById(R.id.spnBitsPerSample);
							ArrayAdapter<BitsPerSample> spinnerAdapterBitsPerSample = new ArrayAdapter<BitsPerSample>(adapter.getContext(),
									android.R.layout.simple_spinner_item, BitsPerSample.values());
							spinnerAdapterBitsPerSample.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnBitsPerSample.setAdapter(spinnerAdapterBitsPerSample);

							final Spinner spnAudioType = (Spinner) layout.findViewById(R.id.spnAudioType);
							ArrayAdapter<AudioType> spinnerAdapterAudioType = new ArrayAdapter<AudioType>(adapter.getContext(),
									android.R.layout.simple_spinner_item, AudioType.values());
							spinnerAdapterAudioType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spnAudioType.setAdapter(spinnerAdapterAudioType);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {								
								public void onClick(DialogInterface dialog, int id) {									
									
									iByteCount = 0;
									bSaveWave = wavfileCheckBox.isChecked();
									Vector<TTSChunk> initChunks = TTSChunkFactory
											.createSimpleTTSChunks(txtInitialPrompt.getText().toString());
									try {
										PerformAudioPassThru msg = new PerformAudioPassThru();
										msg.setInitialPrompt(initChunks);
										//TODO this is currently hard coded.  Should make UI allow to be dynamic.
										msg.setAudioPassThruDisplayText1(txtAudioPassThruDisplayText1.getText().toString());
										msg.setAudioPassThruDisplayText2(txtAudioPassThruDisplayText2.getText().toString());
										msg.setSamplingRate((SamplingRate) spnSamplingRate.getSelectedItem());
										msg.setMaxDuration(Integer.parseInt(txtMaxDuration.getText().toString()));
										msg.setBitsPerSample((BitsPerSample) spnBitsPerSample.getSelectedItem());
										msg.setAudioType((AudioType) spnAudioType.getSelectedItem());
										msg.setCorrelationID(autoIncCorrId++);
										latestPerformAudioPassThruMsg = msg;									
										if (bSaveWave)
										{											
											mySampleRate = getSampleRate(msg.getSamplingRate());
											myBitsPerSample = getBitsPerSample(msg.getBitsPerSample());
										}
										
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (NumberFormatException e) {
										Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.EndAudioPassThru) {
							//EndAudioPassThru
							try {
								EndAudioPassThru msg = new EndAudioPassThru();
								msg.setCorrelationID(autoIncCorrId++);
								_msgAdapter.logMessage(new RPCLogMessage(msg), true);
								ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
							} catch (SmartDeviceLinkException e) {
								_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
							}
						} else if (adapter.getItem(which) == SubscribeVehicleData) {
							//sendVehicleDataSubscriptions();
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_vehicleSubscribeData, new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog, int which) 
								{
									int corrId = autoIncCorrId++;
									boolean [] myVehicleSub = getVehicleDataItem(which, 0);
									
									try {												
											SubscribeVehicleData msg = new SubscribeVehicleData();
											msg.setGps(myVehicleSub[0]);
											msg.setSpeed(myVehicleSub[1]);
											msg.setRpm(myVehicleSub[2]);
											msg.setFuelLevel(myVehicleSub[3]);
											msg.setFuelLevel_State(myVehicleSub[4]);
											msg.setInstantFuelConsumption(myVehicleSub[5]);
											msg.setExternalTemperature(myVehicleSub[6]);
											msg.setPrndl(myVehicleSub[7]);
											msg.setTirePressure(myVehicleSub[8]);
											msg.setOdometer(myVehicleSub[9]);
											msg.setBeltStatus(myVehicleSub[10]);
											msg.setBodyInformation(myVehicleSub[11]);
											msg.setDeviceStatus(myVehicleSub[12]);
											msg.setDriverBraking(myVehicleSub[13]);
											msg.setCorrelationID(corrId);										
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);											
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
							}});
							AlertDialog dlg = builder.create();
							dlg.show();																				
						}
						else if (adapter.getItem(which) == UnsubscribeVehicleData) {
							//sendVehicleDataSubscriptions();
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_vehicleSubscribeData, new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog, int which) 
								{
									int corrId = autoIncCorrId++;
									boolean [] myVehicleSub = getVehicleDataItem(which, 0);
									
									try {												
											UnsubscribeVehicleData msg = new UnsubscribeVehicleData();
											msg.setGps(myVehicleSub[0]);
											msg.setSpeed(myVehicleSub[1]);
											msg.setRpm(myVehicleSub[2]);
											msg.setFuelLevel(myVehicleSub[3]);
											msg.setFuelLevel_State(myVehicleSub[4]);
											msg.setInstantFuelConsumption(myVehicleSub[5]);
											msg.setExternalTemperature(myVehicleSub[6]);
											msg.setPrndl(myVehicleSub[7]);
											msg.setTirePressure(myVehicleSub[8]);
											msg.setOdometer(myVehicleSub[9]);
											msg.setBeltStatus(myVehicleSub[10]);
											msg.setBodyInformation(myVehicleSub[11]);
											msg.setDeviceStatus(myVehicleSub[12]);
											msg.setDriverBraking(myVehicleSub[13]);
											msg.setCorrelationID(corrId);										
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
							}});
							AlertDialog dlg = builder.create();
							dlg.show();																				
						}												
						else if (adapter.getItem(which) == Names.GetVehicleData) {
							//GetVehicleData
							AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
							builder.setAdapter(_getVehicleType, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									try {
										boolean [] myGetVData = getVehicleDataItem(which, 1);
										GetVehicleData msg = new GetVehicleData();									
										msg.setGps(myGetVData[0]);
										msg.setSpeed(myGetVData[1]);
										msg.setRpm(myGetVData[2]);
										msg.setFuelLevel(myGetVData[3]);
										msg.setFuelLevel_State(myGetVData[4]);
										msg.setInstantFuelConsumption(myGetVData[5]);
										msg.setExternalTemperature(myGetVData[6]);
										msg.setVin(myGetVData[7]);
										msg.setPrndl(myGetVData[8]);
										msg.setTirePressure(myGetVData[9]);
										msg.setOdometer(myGetVData[10]);
										msg.setBeltStatus(myGetVData[11]);
										msg.setBodyInformation(myGetVData[12]);
										msg.setDeviceStatus(myGetVData[13]);
										msg.setDriverBraking(myGetVData[14]);
																				
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);																																				
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							AlertDialog dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.ReadDID) {
							//ReadDID
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.readdid, null);
							
							final EditText txtECUNameDID = (EditText) layout.findViewById(R.id.txtECUNameDID);
							final EditText txtDIDLocation = (EditText) layout.findViewById(R.id.txtDIDLocation);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									try {
										Vector<Integer> didlocations = new Vector<Integer>();
										didlocations.add(Integer.parseInt(txtDIDLocation.getText().toString()));
										
										ReadDID msg = new ReadDID();
										msg.setEcuName(Integer.parseInt(txtECUNameDID.getText().toString()));
										msg.setDidLocation(didlocations);
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (NumberFormatException e) {
										Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.GetDTCs) { 
							//GetDTCs
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.getdtcs, null);
							
							final EditText txtECUNameDTC = (EditText) layout.findViewById(R.id.txtECUNameDTC);
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									try {
										GetDTCs msg = new GetDTCs();
										msg.setEcuName(Integer.parseInt(txtECUNameDTC.getText().toString()));
										msg.setCorrelationID(autoIncCorrId++);
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (NumberFormatException e) {
										Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.ShowConstantTBT) {
							//ShowConstantTBT
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.showconstanttbt, null);
							
							final EditText txtNavigationText1 = (EditText) layout.findViewById(R.id.txtNavigationText1);
							final EditText txtNavigationText2 = (EditText) layout.findViewById(R.id.txtNavigationText2);
							final EditText txtEta = (EditText) layout.findViewById(R.id.txtEta);
							final EditText txtTotalDistance = (EditText) layout.findViewById(R.id.txtTotalDistance);
							
							final EditText txtDistanceToManeuver = (EditText) layout.findViewById(R.id.txtDistanceToManeuver);
							final EditText txtDistanceToManeuverScale = (EditText) layout.findViewById(R.id.txtDistanceToManeuverScale);
							
							final CheckBox chkManeuverComplete = (CheckBox) layout.findViewById(R.id.chkManeuverComplete);

							SoftButton sb1 = new SoftButton();
							sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb1.setText("Reply");
							sb1.setType(SoftButtonType.SBT_TEXT);
							sb1.setIsHighlighted(false);
							sb1.setSystemAction(SystemAction.STEAL_FOCUS);
							SoftButton sb2 = new SoftButton();
							sb2.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb2.setText("Close");
							sb2.setType(SoftButtonType.SBT_TEXT);
							sb2.setIsHighlighted(false);
							sb2.setSystemAction(SystemAction.DEFAULT_ACTION);
							currentSoftButtons = new Vector<SoftButton>();
							currentSoftButtons.add(sb1);
							currentSoftButtons.add(sb2);

							Button btnSoftButtons = (Button) layout.findViewById(R.id.showconstanttbt_btnSoftButtons);
							btnSoftButtons.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									IntentHelper.addObjectForKey(currentSoftButtons,
											Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
									Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
									intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
											SHOWCONSTANTTBT_MAXSOFTBUTTONS);
									startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
								}
							});
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Image turnIcon = new Image();
									turnIcon.setValue("Value");
									turnIcon.setImageType(ImageType.STATIC);
									try {
										ShowConstantTBT msg = new ShowConstantTBT();
										msg.setNavigationText1(txtNavigationText1.getText().toString());
										msg.setNavigationText2(txtNavigationText2.getText().toString());
										msg.setEta(txtEta.getText().toString());
										msg.setTotalDistance(txtTotalDistance.getText().toString());
										msg.setTurnIcon(turnIcon);
										msg.setDistanceToManeuver((Double) Double.parseDouble(txtDistanceToManeuver.getText().toString()));
										msg.setDistanceToManeuverScale((Double) Double.parseDouble(txtDistanceToManeuverScale.getText().toString()));
										msg.setManeuverComplete(chkManeuverComplete.isChecked());
										msg.setCorrelationID(autoIncCorrId++);
										if (currentSoftButtons != null) {
											msg.setSoftButtons(currentSoftButtons);
										} else {
											msg.setSoftButtons(new Vector<SoftButton>());
										}
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (NumberFormatException e) {
										Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
									currentSoftButtons = null;
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									currentSoftButtons = null;
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.AlertManeuver) {
							//AlertManeuver
							AlertDialog.Builder builder;
							AlertDialog dlg;
							
							final Context mContext = adapter.getContext();
							LayoutInflater inflater = (LayoutInflater) mContext
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View layout = inflater.inflate(R.layout.alertmaneuver, null);

							final EditText txtTtsChunks = (EditText) layout.findViewById(R.id.txtTtsChunks);
							
							SoftButton sb1 = new SoftButton();
							sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb1.setText("Reply");
							sb1.setType(SoftButtonType.SBT_TEXT);
							sb1.setIsHighlighted(false);
							sb1.setSystemAction(SystemAction.STEAL_FOCUS);
							SoftButton sb2 = new SoftButton();
							sb2.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
							sb2.setText("Close");
							sb2.setType(SoftButtonType.SBT_TEXT);
							sb2.setIsHighlighted(false);
							sb2.setSystemAction(SystemAction.DEFAULT_ACTION);
							currentSoftButtons = new Vector<SoftButton>();
							currentSoftButtons.add(sb1);
							currentSoftButtons.add(sb2);
							
							Button btnSoftButtons = (Button) layout.findViewById(R.id.alertManeuver_btnSoftButtons);
							btnSoftButtons.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									IntentHelper.addObjectForKey(currentSoftButtons,
											Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
									Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
									intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
											ALERTMANEUVER_MAXSOFTBUTTONS);
									startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
								}
							});
							
							builder = new AlertDialog.Builder(mContext);
							builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									Vector<TTSChunk> ttsChunks = new Vector<TTSChunk>();
									// string to join/split ttsChunks string
									final String joinString = ",";
									
									String ttsChunksString = txtTtsChunks.getText().toString();
									for (String ttsChunk : ttsChunksString.split(joinString)) {
										TTSChunk chunk = TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, ttsChunk);
										ttsChunks.add(chunk);
									}

									if (!ttsChunks.isEmpty()) {
										try {
											AlertManeuver msg = new AlertManeuver();
											msg.setTtsChunks(ttsChunks);
											msg.setCorrelationID(autoIncCorrId++);
											if (currentSoftButtons != null) {
												msg.setSoftButtons(currentSoftButtons);
											} else {
												msg.setSoftButtons(new Vector<SoftButton>());
											}
											currentSoftButtons = null;
											_msgAdapter.logMessage(new RPCLogMessage(msg), true);
											ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										} catch (SmartDeviceLinkException e) {
											_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
										}
									} else {
										Toast.makeText(mContext, "No TTS Chunks entered", Toast.LENGTH_SHORT).show();
									}
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									currentSoftButtons = null;
									dialog.cancel();
								}
							});
							builder.setView(layout);
							dlg = builder.create();
							dlg.show();
						} else if (adapter.getItem(which) == Names.UpdateTurnList) {
							sendUpdateTurnList();
						} else if (adapter.getItem(which) == Names.DialNumber) {
							sendDialNumber();
						} else if (adapter.getItem(which) == Names.UnregisterAppInterface) {
							UnregisterAppInterface msg = new UnregisterAppInterface();
							msg.setCorrelationID(autoIncCorrId++);
							
							_msgAdapter.logMessage(new RPCLogMessage(msg), true);
							
							try {
								ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
							} catch (SmartDeviceLinkException e) {
								_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
							}
						}
						
						String function = adapter.getItem(which);
						Integer curCount = messageSelectCount.get(function);
						if (curCount == null) {
							curCount = 0;
						}
						messageSelectCount.put(function, curCount + 1);
					}

					/**
					 * Opens the dialog for CreateInteractionChoiceSet message and sends it.
					 */
					private void sendCreateInteractionChoiceSet() {
						Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.createinteractionchoices,
								(ViewGroup) findViewById(R.id.createcommands_Root));

						final EditText command1 = (EditText) layout.findViewById(R.id.createcommands_command1);
						final EditText command2 = (EditText) layout.findViewById(R.id.createcommands_command2);
						final EditText command3 = (EditText) layout.findViewById(R.id.createcommands_command3);
						final EditText vr1 = (EditText) layout.findViewById(R.id.createcommands_vr1);
						final EditText vr2 = (EditText) layout.findViewById(R.id.createcommands_vr2);
						final EditText vr3 = (EditText) layout.findViewById(R.id.createcommands_vr3);
						final CheckBox choice1 = (CheckBox) layout.findViewById(R.id.createcommands_choice1);
						final CheckBox choice2 = (CheckBox) layout.findViewById(R.id.createcommands_choice2);
						final CheckBox choice3 = (CheckBox) layout.findViewById(R.id.createcommands_choice3);
						final CheckBox image1Check = (CheckBox) layout.findViewById(R.id.createinteractionchoiceset_image1Check);
						final CheckBox image2Check = (CheckBox) layout.findViewById(R.id.createinteractionchoiceset_image2Check);
						final CheckBox image3Check = (CheckBox) layout.findViewById(R.id.createinteractionchoiceset_image3Check);
						final Spinner image1Type = (Spinner) layout.findViewById(R.id.createinteractionchoiceset_image1Type);
						final Spinner image2Type = (Spinner) layout.findViewById(R.id.createinteractionchoiceset_image2Type);
						final Spinner image3Type = (Spinner) layout.findViewById(R.id.createinteractionchoiceset_image3Type);
						final EditText image1Value = (EditText) layout.findViewById(R.id.createinteractionchoiceset_image1Value);
						final EditText image2Value = (EditText) layout.findViewById(R.id.createinteractionchoiceset_image2Value);
						final EditText image3Value = (EditText) layout.findViewById(R.id.createinteractionchoiceset_image3Value);
						
						final ArrayAdapter<ImageType> imageTypeAdapter = new ArrayAdapter<ImageType>(
								mContext, android.R.layout.simple_spinner_item, ImageType.values());
						imageTypeAdapter	.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						
						image1Type.setAdapter(imageTypeAdapter);
						image2Type.setAdapter(imageTypeAdapter);
						image3Type.setAdapter(imageTypeAdapter);
						
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Vector<Choice> commands = new Vector<Choice>();
								
								if (choice1.isChecked()) {
									Choice one = new Choice();
									one.setChoiceID(autoIncChoiceSetIdCmdId++);
									one.setMenuName(command1.getText().toString());
									one.setVrCommands(new Vector<String>(Arrays.asList(new String[] { command1.getText().toString(),
											vr1.getText().toString() })));
									if (image1Check.isChecked()) {
										Image image = new Image();
										image.setImageType(imageTypeAdapter.getItem(image1Type.getSelectedItemPosition()));
										image.setValue(image1Value.getText().toString());
										one.setImage(image);
									}
									commands.add(one);
								}
								
								if (choice2.isChecked()) {
									Choice two = new Choice();
									two.setChoiceID(autoIncChoiceSetIdCmdId++);
									two.setMenuName(command2.getText().toString());
									two.setVrCommands(new Vector<String>(Arrays.asList(new String[] { command2.getText().toString(),
											vr2.getText().toString() })));
									if (image2Check.isChecked()) {
										Image image = new Image();
										image.setImageType(imageTypeAdapter.getItem(image2Type.getSelectedItemPosition()));
										image.setValue(image2Value.getText().toString());
										two.setImage(image);
									}
									commands.add(two);
								}
								
								if (choice3.isChecked()) {
									Choice three = new Choice();
									three.setChoiceID(autoIncChoiceSetIdCmdId++);
									three.setMenuName(command3.getText().toString());
									three.setVrCommands(new Vector<String>(Arrays.asList(new String[] { command3.getText().toString(),
											vr3.getText().toString() })));
									if (image3Check.isChecked()) {
										Image image = new Image();
										image.setImageType(imageTypeAdapter.getItem(image3Type.getSelectedItemPosition()));
										image.setValue(image3Value.getText().toString());
										three.setImage(image);
									}
									commands.add(three);
								}
								
								if (!commands.isEmpty()) {
									CreateInteractionChoiceSet msg = new CreateInteractionChoiceSet();
									msg.setCorrelationID(autoIncCorrId++);
									int choiceSetID = autoIncChoiceSetId++;
									msg.setInteractionChoiceSetID(choiceSetID);
									msg.setChoiceSet(commands);
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
										if (_latestCreateChoiceSetId != CHOICESETID_UNSET) {
											Log.w(logTag, "Latest createChoiceSetId should be unset, but equals to " + _latestCreateChoiceSetId);
										}
										_latestCreateChoiceSetId = choiceSetID;
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								} else {
									Toast.makeText(getApplicationContext(), "No commands to set", Toast.LENGTH_SHORT).show();
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.show();
					}

					/**
					 * Opens the dialog for Show message and sends it.
					 */
					private void sendShow() {
						final Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.show, null);
						
						final CheckBox mainField1Check = (CheckBox) layout.findViewById(R.id.show_mainField1Check);
						final EditText mainField1 = (EditText) layout.findViewById(R.id.show_mainField1);
						final CheckBox mainField2Check = (CheckBox) layout.findViewById(R.id.show_mainField2Check);
						final EditText mainField2 = (EditText) layout.findViewById(R.id.show_mainField2);
						final CheckBox mainField3Check = (CheckBox) layout.findViewById(R.id.show_mainField3Check);
						final EditText mainField3 = (EditText) layout.findViewById(R.id.show_mainField3);
						final CheckBox mainField4Check = (CheckBox) layout.findViewById(R.id.show_mainField4Check);
						final EditText mainField4 = (EditText) layout.findViewById(R.id.show_mainField4);
						final CheckBox textAlignmentCheck = (CheckBox) layout.findViewById(R.id.show_textAlignmentCheck);
						final Spinner textAlignmentSpinner = (Spinner) layout.findViewById(R.id.show_textAlignmentSpinner);
						final CheckBox statusBarCheck = (CheckBox) layout.findViewById(R.id.show_statusBarCheck);
						final EditText statusBar = (EditText) layout.findViewById(R.id.show_statusBar);
						final CheckBox mediaClockCheck = (CheckBox) layout.findViewById(R.id.show_mediaClockCheck);
						final EditText mediaClock = (EditText) layout.findViewById(R.id.show_mediaClock);
						final CheckBox mediaTrackCheck = (CheckBox) layout.findViewById(R.id.show_mediaTrackCheck);
						final EditText mediaTrack = (EditText) layout.findViewById(R.id.show_mediaTrack);
						final CheckBox graphicCheck = (CheckBox) layout.findViewById(R.id.show_graphicCheck);
						final EditText graphic = (EditText) layout.findViewById(R.id.show_graphic);
						chkIncludeSoftButtons = (CheckBox) layout.findViewById(R.id.show_chkIncludeSBs);
						final Button softButtons = (Button) layout.findViewById(R.id.show_btnSoftButtons);
						final CheckBox customPresetsCheck = (CheckBox) layout.findViewById(R.id.show_customPresetsCheck);
						final EditText customPresets = (EditText) layout.findViewById(R.id.show_customPresets);
						
						final ArrayAdapter<TextAlignment> textAlignmentAdapter = new ArrayAdapter<TextAlignment>(
								mContext, android.R.layout.simple_spinner_item, TextAlignment.values());
						textAlignmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						textAlignmentSpinner.setAdapter(textAlignmentAdapter);
						textAlignmentSpinner.setSelection(textAlignmentAdapter.getPosition(TextAlignment.CENTERED));
						
						final boolean isMedia = getIsMedia();
						
						if (!isMedia) {
							int visibility = android.view.View.GONE;
							mediaClock.setVisibility(visibility);
							mediaTrack.setVisibility(visibility);
							mediaTrackCheck.setVisibility(visibility);
							mediaClockCheck.setVisibility(visibility);
						}
						
						SoftButton sb1 = new SoftButton();
						sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
						sb1.setText("KeepContext");
						sb1.setType(SoftButtonType.SBT_TEXT);
						sb1.setIsHighlighted(false);
						sb1.setSystemAction(SystemAction.KEEP_CONTEXT);
						SoftButton sb2 = new SoftButton();
						sb2.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
						sb2.setText("StealFocus");
						sb2.setType(SoftButtonType.SBT_TEXT);
						sb2.setIsHighlighted(false);
						sb2.setSystemAction(SystemAction.STEAL_FOCUS);
						SoftButton sb3 = new SoftButton();
						sb3.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
						sb3.setText("Default");
						sb3.setType(SoftButtonType.SBT_TEXT);
						sb3.setIsHighlighted(false);
						sb3.setSystemAction(SystemAction.DEFAULT_ACTION);
						currentSoftButtons = new Vector<SoftButton>();
						currentSoftButtons.add(sb1);
						currentSoftButtons.add(sb2);
						currentSoftButtons.add(sb3);

						Button btnSoftButtons = (Button) layout.findViewById(R.id.show_btnSoftButtons);
						btnSoftButtons.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								IntentHelper.addObjectForKey(currentSoftButtons,
										Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
								Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
								intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
										SHOW_MAXSOFTBUTTONS);
								startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
							}
							});
						

						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									Show msg = new Show();
									msg.setCorrelationID(autoIncCorrId++);
									
									if (mainField1Check.isChecked()) {
										msg.setMainField1(mainField1.getText().toString());
									}
									if (mainField2Check.isChecked()) {
										msg.setMainField2(mainField2.getText().toString());
									}
									if (mainField3Check.isChecked()) {
										msg.setMainField3(mainField3.getText().toString());
									}
									if (mainField4Check.isChecked()) {
										msg.setMainField4(mainField4.getText().toString());
									}
									if (textAlignmentCheck.isChecked()) {
										msg.setAlignment(textAlignmentAdapter.getItem(textAlignmentSpinner.getSelectedItemPosition()));
									}
									if (statusBarCheck.isChecked()) {
										msg.setStatusBar(statusBar.getText().toString());
									}
									if (isMedia) {
										if (mediaClockCheck.isChecked()) {
											msg.setMediaClock(mediaClock.getText().toString());
										}
										if (mediaTrackCheck.isChecked()) {
											msg.setMediaTrack(mediaTrack.getText().toString());
										}
									}
									if (graphicCheck.isChecked()) {
										Image image = new Image();
										image.setImageType(ImageType.STATIC);
										image.setValue(graphic.getText().toString());
										msg.setGraphic(image);
									}
									if (chkIncludeSoftButtons.isChecked() &&
											(currentSoftButtons != null) &&
											(currentSoftButtons.size() > 0)) {
										msg.setSoftButtons(currentSoftButtons);
									}
									currentSoftButtons = null;
									chkIncludeSoftButtons = null;
									if (customPresetsCheck.isChecked()) {
										String splitter = ",";
										String[] customPresetsList = customPresets.getText().
												toString().split(splitter);
										msg.setCustomPresets(new Vector<String>(Arrays.
												asList(customPresetsList)));
									}
									_msgAdapter.logMessage(new RPCLogMessage(msg), true);
									ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
								} catch (SmartDeviceLinkException e) {
									_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								currentSoftButtons = null;
								chkIncludeSoftButtons = null;
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.show();
					}

					/**
					 * Opens the dialog for PerformInteraction message and sends it.
					 */
					private void sendPerformInteraction() {
						final Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.performinteraction,
								(ViewGroup) findViewById(R.id.performinteraction_Root));
						
						final EditText initialText = (EditText) layout.findViewById(R.id.performinteraction_initialText);
						final EditText initialPrompt = (EditText) layout.findViewById(R.id.performinteraction_initialPrompt);
						final Spinner interactionModeSpinner = (Spinner) layout.findViewById(R.id.performinteraction_interactionModeSpinner);
						final Button choiceSetIDs = (Button) layout.findViewById(R.id.performinteraction_choiceSetIDs);
						final CheckBox helpPromptCheck = (CheckBox) layout.findViewById(R.id.performinteraction_helpPromptCheck);
						final EditText helpPrompt = (EditText) layout.findViewById(R.id.performinteraction_helpPrompt);
						final CheckBox timeoutPromptCheck = (CheckBox) layout.findViewById(R.id.performinteraction_timeoutPromptCheck);
						final EditText timeoutPrompt = (EditText) layout.findViewById(R.id.performinteraction_timeoutPrompt);
						final CheckBox timeoutCheck = (CheckBox) layout.findViewById(R.id.performinteraction_timeoutCheck);
						final EditText timeout = (EditText) layout.findViewById(R.id.performinteraction_timeout);
						final CheckBox vrHelpItemCheck = (CheckBox) layout.findViewById(R.id.performinteraction_vrHelpItemCheck);
						final EditText vrHelpItemPos = (EditText) layout.findViewById(R.id.performinteraction_vrHelpItemPos);
						final EditText vrHelpItemText = (EditText) layout.findViewById(R.id.performinteraction_vrHelpItemText);
						final EditText vrHelpItemImage = (EditText) layout.findViewById(R.id.performinteraction_vrHelpItemImage);
						
						final ArrayAdapter<InteractionMode> interactionModeAdapter = new ArrayAdapter<InteractionMode>(
								mContext, android.R.layout.simple_spinner_item, InteractionMode.values());
						interactionModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						interactionModeSpinner.setAdapter(interactionModeAdapter);
						interactionModeSpinner.setSelection(interactionModeAdapter.getPosition(InteractionMode.BOTH));
						
						final String[] choiceSetIDStrings = new String[_choiceSetAdapter.getCount()];
						final boolean[] choiceSetIDSelections = new boolean[choiceSetIDStrings.length];
						
						for (int i = 0; i < _choiceSetAdapter.getCount(); ++i) {
							choiceSetIDStrings[i] = _choiceSetAdapter.getItem(i).toString();
						}
						
						choiceSetIDs.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								new AlertDialog.Builder(mContext).
									setMultiChoiceItems(choiceSetIDStrings, choiceSetIDSelections, new OnMultiChoiceClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										}
									}).
									setPositiveButton("OK", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									}).
									show();
							}
						});
												
						
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							// string to join/split initial and help prompts, and VR helps
							private String joinString = ",";

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// fail if no interaction choice set selected
								Vector<Integer> choiceSetIDs = new Vector<Integer>();
								for (int i = 0; i < choiceSetIDSelections.length; ++i) {
									if (choiceSetIDSelections[i]) {
										choiceSetIDs.add(_choiceSetAdapter.getItem(i));
									}
								}
								
								if (choiceSetIDs.size() > 0) {
									PerformInteraction msg = new PerformInteraction();
									msg.setCorrelationID(autoIncCorrId++);
									msg.setInitialText(initialText.getText().toString());
									msg.setInitialPrompt(ttsChunksFromString(initialPrompt.getText().toString()));
									msg.setInteractionMode(interactionModeAdapter.getItem(interactionModeSpinner.getSelectedItemPosition()));
									msg.setInteractionChoiceSetIDList(choiceSetIDs);
									
									if (helpPromptCheck.isChecked()) {
										msg.setHelpPrompt(ttsChunksFromString(helpPrompt.getText().toString()));
									}
									
									if (timeoutPromptCheck.isChecked()) {
										msg.setTimeoutPrompt(ttsChunksFromString(timeoutPrompt.getText().toString()));
									}
									
									if (timeoutCheck.isChecked()) {
										try {
											msg.setTimeout(Integer.parseInt(timeout.getText().toString()));
										} catch (NumberFormatException e) {
											// set default timeout
											msg.setTimeout(10000);
										}
									}
									
									if (vrHelpItemCheck.isChecked()) {
										Vector<VrHelpItem> vrHelpItems = new Vector<VrHelpItem>();
										VrHelpItem item = new VrHelpItem();
										item.setText(vrHelpItemText.getText().toString());
										
										try {
											item.setPosition(Integer.parseInt(vrHelpItemPos.getText().toString()));
										} catch (NumberFormatException e) {
											// set default position
											item.setPosition(1);
										}
										
										Image image = new Image();
										image.setValue(vrHelpItemImage.getText().toString());
										image.setImageType(ImageType.STATIC);
										item.setImage(image);
										
										vrHelpItems.add(item);
										msg.setVrHelp(vrHelpItems);
									}
									
									try {
										_msgAdapter.logMessage(new RPCLogMessage(msg), true);
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								} else {
									Toast.makeText(mContext, "No interaction choice set selected", Toast.LENGTH_LONG).show();
								}
							}

							/**
							 * Splits the string with a comma and returns a vector of TTSChunks.
							 */
							private Vector<TTSChunk> ttsChunksFromString(String string) {
								Vector<TTSChunk> chunks = new Vector<TTSChunk>();
								for (String stringChunk : string.split(joinString )) {
									TTSChunk chunk = TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, stringChunk);
									chunks.add(chunk);
								}
								return chunks;
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						
						builder.setView(layout);
						builder.show();
					}

					private void sendUpdateTurnList() {
						AlertDialog.Builder builder;

						final Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.updateturnlist, null);
						final EditText txtTurnList = (EditText) layout.findViewById(R.id.updateturnlist_txtTurnList);
						final EditText txtIconList = (EditText) layout.findViewById(R.id.updateturnlist_txtIconList);
						
						SoftButton sb1 = new SoftButton();
						sb1.setSoftButtonID(SmartDeviceLinkTester.getNewSoftButtonId());
						sb1.setText("Close");
						sb1.setType(SoftButtonType.SBT_TEXT);
						sb1.setIsHighlighted(false);
						sb1.setSystemAction(SystemAction.DEFAULT_ACTION);
						currentSoftButtons = new Vector<SoftButton>();
						currentSoftButtons.add(sb1);

						Button btnSoftButtons = (Button) layout.findViewById(R.id.updateturnlist_btnSoftButtons);
						btnSoftButtons.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								IntentHelper.addObjectForKey(currentSoftButtons,
										Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
								Intent intent = new Intent(mContext, SoftButtonsListActivity.class);
								intent.putExtra(Const.INTENT_KEY_SOFTBUTTONS_MAXNUMBER,
										UPDATETURNLIST_MAXSOFTBUTTONS);
								startActivityForResult(intent, Const.REQUEST_LIST_SOFTBUTTONS);
							}
						});
						
						builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								/*
								 * the number of items to send is determined as max of turn items
								 * and icon items. only when the both fields are empty, we
								 * don't send anything.
								 */
								String turnListString = txtTurnList.getText().toString();
								String iconListString = txtIconList.getText().toString();
								if ((turnListString.length() > 0) || (iconListString.length() > 0)) {
									// string to split turnList/iconList strings
									final String joinString = ",";
									
									Vector<Turn> tarray = new Vector<Turn>();
									
									String[] iconNames = iconListString.split(joinString);
									String[] turnNames = turnListString.split(joinString);
									int turnCount = Math.max(iconNames.length, turnNames.length);
									
									for (int i = 0; i < turnCount; ++i) {
										Turn t = new Turn();
										t.setNavigationText((i < turnNames.length) ? turnNames[i] : "");
										Image ti = new Image();
										ti.setValue((i < iconNames.length) ? iconNames[i] : "");
										ti.setImageType(ImageType.STATIC);
										t.setTurnIcon(ti);
										tarray.add(t);
									}
									UpdateTurnList msg = new UpdateTurnList();
									msg.setCorrelationID(autoIncCorrId++);
									msg.setTurnList(tarray);
									if (currentSoftButtons != null) {
										msg.setSoftButtons(currentSoftButtons);
									} else {
										msg.setSoftButtons(new Vector<SoftButton>());
									}
									currentSoftButtons = null;
									
									_msgAdapter.logMessage(new RPCLogMessage(msg), true);
									
									try {
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								} else {
									Toast.makeText(mContext, "Both fields are empty, nothing to send",
											Toast.LENGTH_LONG).show();
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								currentSoftButtons = null;
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.show();
					}

					private void sendDialNumber() {
						AlertDialog.Builder builder;

						Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.dialnumber, null);
						final EditText txtPhoneNumber = (EditText) layout.findViewById(R.id.dialNumber_editPhoneNumber);
						
						builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DialNumber msg = new DialNumber();
								msg.setNumber(txtPhoneNumber.getText().toString());
								msg.setCorrelationID(autoIncCorrId++);
								
								_msgAdapter.logMessage(new RPCLogMessage(msg), true);
								
								try {
									ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
								} catch (SmartDeviceLinkException e) {
									_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.show();
					}
					
					private void updateDynamicFooter(EditText txtNumTicks,
							EditText txtSliderFooter, String joinString) {
						// set numTicks comma-separated strings
						int numTicks = 0;
						try {
							numTicks = Integer.parseInt(txtNumTicks.getText().toString());
						} catch (NumberFormatException e) {
							// do nothing, leave 0
						}
						if (numTicks > 0) {
							StringBuilder b = new StringBuilder();
							for (int i = 0; i < numTicks; ++i) {
								b.append(joinString).append(i + 1);
							}
							txtSliderFooter.setText(b.toString().substring(joinString.length()));
						} else {
							txtSliderFooter.setText("");
						}
					}

					private void sendSlider() {
						AlertDialog.Builder builder;

						final Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.slider, null);
						final EditText txtNumTicks = (EditText) layout.findViewById(R.id.txtNumTicks);
						final EditText txtPosititon = (EditText) layout.findViewById(R.id.txtPosition);
						final EditText txtSliderHeader = (EditText) layout.findViewById(R.id.txtSliderHeader);
						final EditText txtSliderFooter = (EditText) layout.findViewById(R.id.txtSliderFooter);
						final EditText txtTimeout = (EditText) layout.findViewById(R.id.txtTimeout);

						// string to join/split footer strings
						final String joinString = ",";

						final CheckBox chkDynamicFooter = (CheckBox) layout.findViewById(R.id.slider_chkDynamicFooter);
						chkDynamicFooter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (!isChecked) {
									// set default static text
									txtSliderFooter.setText(R.string.slider_footer);
								} else {
									updateDynamicFooter(txtNumTicks, txtSliderFooter, joinString);
								}
							}
						});
						
						txtNumTicks.setOnFocusChangeListener(new OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if ((!hasFocus) && chkDynamicFooter.isChecked()) {
									updateDynamicFooter(txtNumTicks, txtSliderFooter, joinString);
								}
							}
						});
						
						builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								try {
									if (chkDynamicFooter.isChecked()) {
										updateDynamicFooter(txtNumTicks, txtSliderFooter, joinString);
									}
									
									Slider msg = new Slider();
									msg.setTimeout(Integer.parseInt(txtTimeout.getText().toString()));
									msg.setNumTicks(Integer.parseInt(txtNumTicks.getText().toString()));
									msg.setSliderHeader(txtSliderHeader.getText().toString());
									
									Vector<String> footerelements = null;
									String footer = txtSliderFooter.getText().toString();
									if (chkDynamicFooter.isChecked()) {
										footerelements = new Vector<String>(Arrays.asList(footer.split(joinString)));
									} else {
										footerelements = new Vector<String>();
										footerelements.add(footer);
									}
									msg.setSliderFooter(footerelements);
									
									msg.setPosition(Integer.parseInt(txtPosititon.getText().toString()));
									msg.setCorrelationID(autoIncCorrId++);
									_msgAdapter.logMessage(new RPCLogMessage(msg), true);
									ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
								} catch (NumberFormatException e) {
									Toast.makeText(mContext, "Couldn't parse number", Toast.LENGTH_LONG).show();
								} catch (SmartDeviceLinkException e) {
									_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.show();
					}

					private void sendSetGlobalProperties() {
						AlertDialog.Builder builder;

						Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.setglobalproperties,
								(ViewGroup) findViewById(R.id.setglobalproperties_Root));

						final EditText helpPrompt = (EditText) layout.findViewById(R.id.setglobalproperties_helpPrompt);
						final EditText timeoutPrompt = (EditText) layout.findViewById(R.id.setglobalproperties_timeoutPrompt);
						final EditText vrHelpTitle = (EditText) layout.findViewById(R.id.setglobalproperties_vrHelpTitle);
						final EditText vrHelpItemText = (EditText) layout.findViewById(R.id.setglobalproperties_vrHelpItemText);
						final EditText vrHelpItemImage = (EditText) layout.findViewById(R.id.setglobalproperties_vrHelpItemImage);
						final EditText vrHelpItemPosition = (EditText) layout.findViewById(R.id.setglobalproperties_vrHelpItemPos);
						final CheckBox choiceHelpPrompt = (CheckBox) layout.findViewById(R.id.setglobalproperties_choiceHelpPrompt);
						final CheckBox choiceTimeoutPrompt = (CheckBox) layout.findViewById(R.id.setglobalproperties_choiceTimeoutPrompt);
						final CheckBox choiceVRHelpTitle = (CheckBox) layout.findViewById(R.id.setglobalproperties_choiceVRHelpTitle);
						final CheckBox choiceVRHelpItem = (CheckBox) layout.findViewById(R.id.setglobalproperties_choiceVRHelpItem);
						

						builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								SetGlobalProperties msg = new SetGlobalProperties();
								int numberOfChoices = 0;
								
								// string to join/split help and timeout prompts
								final String joinString = ",";
								
								if (choiceHelpPrompt.isChecked()) {
									Vector<TTSChunk> help = new Vector<TTSChunk>();
									String helpString = helpPrompt.getText().toString();
									for (String ttsChunk : helpString.split(joinString)) {
										TTSChunk chunk = TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, ttsChunk);
										help.add(chunk);
									}
									msg.setHelpPrompt(help);
									++numberOfChoices;
								}

								if (choiceTimeoutPrompt.isChecked()) {
									Vector<TTSChunk> timeout = new Vector<TTSChunk>();
									String timeoutString = timeoutPrompt.getText().toString();
									for (String ttsChunk : timeoutString.split(joinString)) {
										TTSChunk chunk = TTSChunkFactory.createChunk(SpeechCapabilities.TEXT, ttsChunk);
										timeout.add(chunk);
									}
									msg.setTimeoutPrompt(timeout);
									++numberOfChoices;
								}

								if (choiceVRHelpTitle.isChecked()) {
									msg.setVrHelpTitle(vrHelpTitle.getText().toString());
									++numberOfChoices;
								}
								
								if (choiceVRHelpItem.isChecked()) {
									Vector<VrHelpItem> vrHelp = new Vector<VrHelpItem>();
									
									VrHelpItem helpItem = new VrHelpItem();
									helpItem.setText(vrHelpItemText.getText().toString());
									try {
										helpItem.setPosition(Integer.parseInt(vrHelpItemPosition.getText().toString()));
									} catch (NumberFormatException e) {
										// set something default
										helpItem.setPosition(1);
									}
									Image image = new Image();
									image.setValue(vrHelpItemImage.getText().toString());
									image.setImageType(ImageType.STATIC);
									helpItem.setImage(image);
									vrHelp.add(helpItem);
									
									msg.setVrHelp(vrHelp);
									++numberOfChoices;
								}

								if (numberOfChoices > 0) {
									msg.setCorrelationID(autoIncCorrId++);
									_msgAdapter.logMessage(new RPCLogMessage(msg), true);
									try {
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								} else {
									Toast.makeText(getApplicationContext(), "No items selected", Toast.LENGTH_LONG).show();
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.create().show();
					}

					private void sendResetGlobalProperties() {
						AlertDialog.Builder builder;

						Context mContext = adapter.getContext();
						LayoutInflater inflater = (LayoutInflater) mContext
								.getSystemService(LAYOUT_INFLATER_SERVICE);
						View layout = inflater.inflate(R.layout.resetglobalproperties,
								(ViewGroup) findViewById(R.id.resetglobalproperties_Root));

						final CheckBox choiceHelpPrompt = (CheckBox) layout.findViewById(R.id.resetglobalproperties_choiceHelpPrompt);
						final CheckBox choiceTimeoutPrompt = (CheckBox) layout.findViewById(R.id.resetglobalproperties_choiceTimeoutPrompt);
						final CheckBox choiceVRHelpTitle = (CheckBox) layout.findViewById(R.id.resetglobalproperties_choiceVRHelpTitle);
						final CheckBox choiceVRHelpItem = (CheckBox) layout.findViewById(R.id.resetglobalproperties_choiceVRHelpItems);


						builder = new AlertDialog.Builder(mContext);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ResetGlobalProperties msg = new ResetGlobalProperties();
								Vector<GlobalProperty> properties = new Vector<GlobalProperty>();
								
								if (choiceHelpPrompt.isChecked()) {
									properties.add(GlobalProperty.HELPPROMPT);
								}

								if (choiceTimeoutPrompt.isChecked()) {
									properties.add(GlobalProperty.TIMEOUTPROMPT);
								}

								if (choiceVRHelpTitle.isChecked()) {
									properties.add(GlobalProperty.VRHELPTITLE);
								}

								if (choiceVRHelpItem.isChecked()) {
									properties.add(GlobalProperty.VRHELPITEMS);
								}

								if (!properties.isEmpty()) {
									msg.setProperties(properties);
									msg.setCorrelationID(autoIncCorrId++);
									_msgAdapter.logMessage(new RPCLogMessage(msg), true);
									try {
										ProxyService.getInstance().getProxyInstance().sendRPCRequest(msg);
									} catch (SmartDeviceLinkException e) {
										_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
									}
								} else {
									Toast.makeText(getApplicationContext(), "No items selected", Toast.LENGTH_LONG).show();
								}
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
						builder.setView(layout);
						builder.create().show();
					}
				})
		       .setNegativeButton("Close", null)
		       .show();			
		} else if (v == findViewById(R.id.btnPlayPause)) {
			ProxyService.getInstance().playPauseAnnoyingRepetitiveAudio();
        } else if (v == findViewById(R.id.btnSendPMMessage)) {
            showProfileManagerControls();
        } else if (v == findViewById(R.id.btnPMTests)) {
            _profileManagerTests.runProfileManagerTests();
        }
	}

	public void addSubMenuToList(final smartdevicelinkSubMenu sm) {
		runOnUiThread(new Runnable() {
			public void run() {
				_submenuAdapter.add(sm);
			}
		});
	}
	
	/**
	 * Adds command ID to the adapter, and maps it to its parent submenu.
	 * 
	 * @param cmdID
	 *            ID of the new command
	 * @param submenuID
	 *            ID of the command's parent submenu
	 */
	private void addCommandToList(int cmdID, int submenuID) {
		_commandAdapter.add(cmdID);
		_commandIdToParentSubmenuMap.put(cmdID, submenuID);
	}

	/**
	 * Removes command ID from the adapter.
	 * 
	 * @param cmdID
	 *            ID of the command
	 */
	private void removeCommandFromList(int cmdID) {
		_commandAdapter.remove(cmdID);
		_commandIdToParentSubmenuMap.remove(cmdID);
	}

/*	public void startsmartdevicelinkProxyService() {
    	// Get the local Bluetooth adapter
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //BT Adapter exists, is enabled, and there are paired devices with the name smartdevicelink
		//Ideally start service and start proxy if already connected to smartdevicelink
		//but, there is no way to tell if a device is currently connected (pre OS 4.0)
        
        if (mBtAdapter != null)
		{
			if ((mBtAdapter.isEnabled() && mBtAdapter.getBondedDevices().isEmpty() != true)) 
			{
				// Get a set of currently paired devices
				Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
		
				boolean issmartdevicelinkpaired = false;
				// Check if there is a paired device with the name "smartdevicelink"
		        if (pairedDevices.size() > 0) {
		            for (BluetoothDevice device : pairedDevices) {
		               if (device.getName().toString().equals("smartdevicelink")) {
		            	   issmartdevicelinkpaired  = true;
		            	   break;
		               }
		            }
		        } else {
		        	Log.i("TAG", "A No Paired devices with the name smartdevicelink");
		        }
		        
		        if (issmartdevicelinkpaired == true) { 		        	
		        	_applinkService = new ProxyService();
		    		if (ProxyService.getInstance() == null) {
		    			Intent startIntent = new Intent(this, ProxyService.class);
		    			startService(startIntent);
		    			//bindService(startIntent, this, Context.BIND_AUTO_CREATE);
		    		} else {
		    			// need to get the instance and add myself as a listener
		    			ProxyService.getInstance().setCurrentActivity(this);
		    		}
		        }
			}
		}
	}*/

	//upon onDestroy(), dispose current proxy and create a new one to enable auto-start
	//call resetProxy() to do so
	public void endsmartdevicelinkProxyInstance() {	
		ProxyService serviceInstance = ProxyService.getInstance();
		if (serviceInstance != null){
			SmartDeviceLinkProxyALM proxyInstance = serviceInstance.getProxyInstance();
			//if proxy exists, reset it
			if(proxyInstance != null){
				if (proxyInstance.getCurrentTransportType() == TransportType.BLUETOOTH) {
					serviceInstance.reset();
				} else {
					Log.e(logTag, "endsmartdevicelinkProxyInstance. No reset required if transport is TCP");
				}
			//if proxy == null create proxy
			} else {
				serviceInstance.startProxy();
			}
		}
	}
    
    public static SmartDeviceLinkTester getInstance() {
		return _activity;
	}
    
    public static logAdapter getMessageAdapter() {
		return _msgAdapter;
	}
    
	public void setTesterMain(ModuleTest _instance) {
		this._testerMain = _instance;
	}
	
	public static void setESN(byte[] ESN) {
		_ESN = ESN;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		saveMessageSelectCount();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		saveMessageSelectCount();
	}
	
	/**
	 * Called when a CreateChoiceSetResponse comes. If successful, add it to the
	 * adapter. In any case, remove the key from the map.
	 */
	public void onCreateChoiceSetResponse(boolean success) {
		if (_latestCreateChoiceSetId != CHOICESETID_UNSET) {
			if (success) {
				_choiceSetAdapter.add(_latestCreateChoiceSetId);
			}
			_latestCreateChoiceSetId = CHOICESETID_UNSET;
		} else {
			Log.w(logTag, "Latest createChoiceSetId is unset");
		}
	}
	
	/**
	 * Called when a DeleteChoiceSetResponse comes. If successful, remove it
	 * from the adapter.
	 */
	public void onDeleteChoiceSetResponse(boolean success) {
		if (_latestDeleteChoiceSetId != CHOICESETID_UNSET) {
			if (success) {
				_choiceSetAdapter.remove(_latestDeleteChoiceSetId);
			}
			_latestDeleteChoiceSetId = CHOICESETID_UNSET;
		} else {
			Log.w(logTag, "Latest deleteChoiceSetId is unset");
		}
	}
	
	/**
	 * Called when a DeleteSubMenuResponse comes. If successful, remove it from
	 * the adapter. We also need to delete all the commands that were added to
	 * this submenu.
	 */
	public void onDeleteSubMenuResponse(boolean success) {
		if (_latestDeleteSubmenu != null) {
			if (success) {
				_submenuAdapter.remove(_latestDeleteSubmenu);

				for (Iterator<Entry<Integer, Integer>> it = _commandIdToParentSubmenuMap
						.entrySet().iterator(); it.hasNext();) {
					Entry<Integer, Integer> entry = it.next();
					if (entry.getValue() == _latestDeleteSubmenu.getSubMenuId()) {
						_commandAdapter.remove(entry.getKey());
						it.remove();
					}
				}
			}
			_latestDeleteSubmenu = null;
		} else {
			Log.w(logTag, "Latest deleteSubMenu is unset");
		}
	}
	
	/**
	 * Called when a AddSubMenuResponse comes. If successful, add it to the
	 * adapter.
	 */
	public void onAddSubMenuResponse(boolean success) {
		if (_latestAddSubmenu != null) {
			if (success) {
				addSubMenuToList(_latestAddSubmenu);
			}
			_latestAddSubmenu = null;
		} else {
			Log.w(logTag, "Latest addSubMenu is unset");
		}
	}	

    public int getSampleRate(SamplingRate mySample)
    {
    	int iReturn = 0;    	
    	switch (mySample) 
    	{
    		case _8KHZ: iReturn = 8000;
    			break;
    		case _16KHZ:  iReturn = 16000;
                 break;
        	case _22KHZ:  iReturn = 22050;
        		break;
        	case _44KHZ:  iReturn = 44100;        		
            	break;                             
    	}    	
    	return iReturn;
    }
    
    
    public int getBitsPerSample(BitsPerSample myBitsPerSample)
    {
    	int iReturn = 0;    	
    	switch (myBitsPerSample) 
    	{    
    		case _8_BIT: iReturn = 8;
    			break;
    		case _16_BIT:  iReturn = 16;
                 break;
    	}    	
    	return iReturn;    	
    }
    
	private byte[] WriteWaveFileHeader(DataOutputStream out, long totalAudioLen,
            						  long totalDataLen, long longSampleRate, int channels, long byteRate) 
	{     
		byte[] header = new byte[44];      
		header[0] = 'R';  // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f';  // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1;  // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (channels * myBitsPerSample / 8);  // block align
		header[33] = 0;
		header[34] = (byte) myBitsPerSample;  // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);    
		return header;		      
	}
    
    
    private boolean saveAsWav()
    {
		try
        {                                         
			byte[] myData;        
            DataOutputStream outFile  = new DataOutputStream(new FileOutputStream(audioPassThruOutputFile(WAV)));                                     
            long totalAudioLen = iByteCount;
            long totalDataLen = totalAudioLen + 36;
            long longSampleRate = mySampleRate;
            int channels = 1;
            long byteRate =  mySampleRate * channels * myBitsPerSample/8;
            byte[] header = WriteWaveFileHeader(outFile, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);                                    
            outFile.write(header, 0, 44);
            DataInputStream inFile = new DataInputStream(new FileInputStream(audioPassThruOutputFile(PCM)));
            myData = new byte[iByteCount];
            inFile.read(myData);                                        
            outFile.write(myData);                    
            inFile.close();
            outFile.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            return false;
        }
        return true;
    }    
	
		
	/**
	 * Called whenever an OnAudioPassThru notification comes. The aptData is the
	 * audio data sent in it.
	 */
	public void onAudioPassThru(byte[] aptData) {
		if (aptData == null) {
			Log.w(logTag, "onAudioPassThru aptData is null");
			return;
		}
		Log.i(logTag, "data len " + aptData.length);
			
		iByteCount = iByteCount + aptData.length;
		
		File outFile = audioPassThruOutputFile(PCM);
		
		
		try {
			if (audioPassThruOutStream == null) {
				audioPassThruOutStream = new BufferedOutputStream(
						new FileOutputStream(outFile, false));
			}
			audioPassThruOutStream.write(aptData);
			audioPassThruOutStream.flush();
		} catch (FileNotFoundException e) {
			logToConsoleAndUI(
					"Output file "
							+ (outFile != null ? outFile.toString()
									: "'unknown'")
							+ " can't be opened for writing", e);
		} catch (IOException e) {
			logToConsoleAndUI("Can't write to output file", e);
		}
	}

	/**
	 * Called when a PerformAudioPassThru response comes. Save the file only if
	 * the result is success. If the result is retry, send the latest request
	 * again.
	 */
	public void onPerformAudioPassThruResponse(Result result) {	
		closeAudioPassThruStream();
		closeAudioPassThruMediaPlayer();
				
		if (Result.SUCCESS == result && bSaveWave)
		{
			saveAsWav();
		}		
		else if (Result.SUCCESS != result) {
			File outFile = audioPassThruOutputFile(PCM);
			if ((outFile != null) && outFile.exists()) {
				if (!outFile.delete()) {
					logToConsoleAndUI("Failed to delete output file", null);
				}
			}

			if ((Result.RETRY == result)
					&& (latestPerformAudioPassThruMsg != null)) {
				latestPerformAudioPassThruMsg.setCorrelationID(autoIncCorrId++);
				try {
					_msgAdapter.logMessage(new RPCLogMessage(latestPerformAudioPassThruMsg), true);
					ProxyService.getInstance().getProxyInstance()
							.sendRPCRequest(latestPerformAudioPassThruMsg);
				} catch (SmartDeviceLinkException e) {
					_msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e),
							Log.ERROR, e);
				}
			}
		}
	}

	/**
	 * Called when an EndAudioPassThru response comes. The logic is the same as
	 * when a PerformAudioPassThru response comes.
	 */
	public void onEndAudioPassThruResponse(Result result) {
		onPerformAudioPassThruResponse(result);
	}

	private void closeAudioPassThruStream() {
		if (audioPassThruOutStream != null) {
			Log.d(logTag, "closing audioPassThruOutStream");
			try {
				audioPassThruOutStream.flush();
				audioPassThruOutStream.close();
			} catch (IOException e) {
				Log.w(logTag, "Can't close output file", e);
			}
			audioPassThruOutStream = null;
		}
	}
	
	private void closeAudioPassThruMediaPlayer() {
		if (audioPassThruMediaPlayer == null) {
			return;
		}
		
		if (audioPassThruMediaPlayer.isPlaying()) {
			audioPassThruMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					Log.d(logTag, "mediaPlayer completed");
					audioPassThruMediaPlayer.reset();
					audioPassThruMediaPlayer.release();
					audioPassThruMediaPlayer = null;
				}
			});
		} else {
			// the player has stopped
			Log.d(logTag, "mediaPlayer is stopped");
			audioPassThruMediaPlayer.release();
			audioPassThruMediaPlayer = null;
		}
	}

	private File audioPassThruOutputFile(int iTypePar) {
		
		String sFileType = "";
		if (iTypePar == WAV)
		{
			sFileType = AUDIOPASSTHRU_OUTPUT_FILE_WAV;
		}
		else if (iTypePar == PCM)
		{
			sFileType = AUDIOPASSTHRU_OUTPUT_FILE_PCM;
		}
		
		File baseDir = isExtStorageWritable() ? Environment
				.getExternalStorageDirectory() : getFilesDir();
		File outFile = new File(baseDir, sFileType);
		return outFile;
	}

	private void logToConsoleAndUI(String msg, Throwable thr) {
		Log.d(logTag, msg, thr);
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	/** Returns whether the external storage is available for writing. */
	private boolean isExtStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/** Called when a connection to a smartdevicelink device has been closed. */
	public void onProxyClosed() {
		resetAdapters();
		_msgAdapter.logMessage(new StringLogMessage("Disconnected"), true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Const.REQUEST_LIST_SOFTBUTTONS:
			if (resultCode == RESULT_OK) {
				currentSoftButtons = (Vector<SoftButton>) IntentHelper.
						getObjectForKey(Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
				if (chkIncludeSoftButtons != null) {
					chkIncludeSoftButtons.setChecked(true);
				}
			}
			IntentHelper.removeObjectForKey(Const.INTENTHELPER_KEY_SOFTBUTTONSLIST);
			break;
		default:
			Log.i(logTag, "Unknown request code: " + requestCode);
			break;
		}
	}
	
	public boolean[] getVehicleDataItem(int iIndex, int iTypePar)
	{
		boolean [] bReturnArray = null;
		if (iTypePar == 0)
		{
			bReturnArray = new boolean[VehicleSubscribe.values().length];
			bReturnArray[iIndex] = !bReturnArray[iIndex];
		}
		else if (iTypePar == 1)
		{
			bReturnArray = new boolean[GetVehicleType.values().length];
			bReturnArray[iIndex] = !bReturnArray[iIndex];			
		}
		return bReturnArray;
	}
	
	
	public static int getNewSoftButtonId() {
		return autoIncSoftButtonId++;
	}
	
	/**
	 * Called when the app is acivated from HMI for the first time. ProxyService
	 * automatically subscribes to buttons, so we reflect that in the
	 * subscription list.
	 */
	public void buttonsSubscribed(Vector<ButtonName> buttons) {
		List<ButtonName> buttonNames = Arrays.asList(ButtonName.values());
		for (ButtonName buttonName : buttons) {
			isButtonSubscribed[buttonNames.indexOf(buttonName)] = true;
		}
	}

	/**
	 * Returns the file contents from the specified file.
	 * 
	 * @param filename
	 *            Name of the file to open.
	 * @return The file's contents or null in case of an error
	 */
	public static byte[] contentsOfFile(String filename) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(filename));
			ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
			final int buffersize = 4096;
			final byte[] buffer = new byte[buffersize];
			int available = 0;
			while ((available = is.read(buffer)) >= 0) {
				os.write(buffer, 0, available);
			}
			return os.toByteArray();
		} catch (IOException e) {
			Log.w(logTag, "Can't read file " + filename, e);
			return null;
		} catch (OutOfMemoryError e) {
			Log.e(logTag, "File " + filename + " is too big", e);
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
	
    @Override
    public void onUserSelectedItem(String ip, String port) {
        mDiscoverer = null;
        getSharedPreferences(Const.PREFS_NAME, 0) 
        .edit()
        .putString(Const.Transport.PREFS_KEY_TRANSPORT_IP, ip)
        .putInt(Const.Transport.PREFS_KEY_TRANSPORT_PORT, Integer.parseInt(port))
        .commit();
        startsmartdevicelinkProxy();
    }

    @Override
    public void onUserDismissedDialog() {
        mDiscoverer.performSearch();        
    }

    @Override
    public void onDiscovererFoundNothing() {
        mDiscoverer.performSearch();        
    }

    @Override
    public String getDismissDialogButtonName() {
        return "Continue search";
    }
    
    private void showProfileManagerControls() {
        new AlertDialog.Builder(this)
            .setTitle("Pick a Function")
            .setAdapter(getAdapterWithProfManCommands(), mProfileManagerControlsListener)
            .setNegativeButton("Close", null)
            .show();
    }

    private final static String[] mProfileManagerSingleTestCommands = { Names.addProfile, Names.removeProfile,
            Names.loadProfile, Names.unloadProfile, Names.sendAppToProfileMessage, Names.appStateChanged };
    private final static Map<String, Integer> mProfileManaterTestCommandsIndexes = new HashMap<String, Integer>();
    static {
        for (int i = 0; i < mProfileManagerSingleTestCommands.length; i++) {
            mProfileManaterTestCommandsIndexes.put(mProfileManagerSingleTestCommands[i], i);
        }
    }

    private ArrayAdapter<String> getAdapterWithProfManCommands() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,
                mProfileManagerSingleTestCommands);
        return adapter;
    }
    
    private DialogInterface.OnClickListener mProfileManagerControlsListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, final int which) {
            AlertDialog.Builder builder;
            AlertDialog dlg;
            final Context context = SmartDeviceLinkTester.this;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.pm_general, null);
            final EditText txtProfileName = (EditText) layout.findViewById(R.id.txtedProfileName);
            final EditText txtMessage = (EditText) layout.findViewById(R.id.txtPMMessage);
            final Spinner spnState = (Spinner) layout.findViewById(R.id.spnProfileState);
            ArrayAdapter<MobileAppState> spinnerAdapter = new ArrayAdapter<MobileAppState>(context,
                    android.R.layout.simple_spinner_item, MobileAppState.values());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnState.setAdapter(spinnerAdapter);

            if (which == mProfileManaterTestCommandsIndexes.get(Names.appStateChanged)) {
                spnState.setVisibility(View.VISIBLE);
            } else if (which == mProfileManaterTestCommandsIndexes.get(Names.sendAppToProfileMessage)) {
                txtMessage.setVisibility(View.VISIBLE);
            }

            builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        RPCRequest msg = null;
                        String profileName = txtProfileName.getText().toString();
                        if (which == mProfileManaterTestCommandsIndexes.get(Names.addProfile)) {
                            // TODO this should be done in an AsyncTask for real profiles
                            byte[] profileData = loadAssetsFile("lib" + profileName + ".so");
                            if (profileData != null) {
                                autoIncCorrId = ProxyService.getProxyInstance().addProfile(profileName, profileData, autoIncCorrId);
                                autoIncCorrId++;
                            }
                        } else if (which == mProfileManaterTestCommandsIndexes.get(Names.removeProfile)) {
                            msg = RPCRequestFactory.buildRemoveProfile(profileName, autoIncCorrId++);
                        } else if (which == mProfileManaterTestCommandsIndexes.get(Names.loadProfile)) {
                            msg = RPCRequestFactory.buildLoadProfile(profileName, autoIncCorrId++);
                        } else if (which == mProfileManaterTestCommandsIndexes.get(Names.unloadProfile)) {
                            msg = RPCRequestFactory.buildUnloadProfile(profileName, autoIncCorrId++);
                        } else if (which == mProfileManaterTestCommandsIndexes.get(Names.appStateChanged)) {
                            msg = RPCRequestFactory.buildAppStateChanged(profileName,
                                    (MobileAppState) spnState.getSelectedItem(), autoIncCorrId++);
                        } else if (which == mProfileManaterTestCommandsIndexes.get(Names.sendAppToProfileMessage)) {
                            msg = RPCRequestFactory.buildSendAppToProfileMessage(profileName, txtMessage.getText()
                                    .toString().getBytes(), autoIncCorrId++);
                        }

                        if (msg != null) {
                            _msgAdapter.logMessage(new RPCLogMessage(msg), true);
                            ProxyService.getProxyInstance().sendRPCRequest(msg);
                        }

                    } catch (SmartDeviceLinkException e) {
                        _msgAdapter.logMessage(new StringLogMessage("Error sending message: " + e), Log.ERROR, e);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setView(layout);
            dlg = builder.create();
            dlg.show();
        }
    };
    
    public void handlePMMessage(RPCMessage message) {
        _profileManagerTests.handleMessage(message);
    }

    public byte[] loadAssetsFile(String fileName) {
        try {
            InputStream stream = getAssets().open(fileName);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
