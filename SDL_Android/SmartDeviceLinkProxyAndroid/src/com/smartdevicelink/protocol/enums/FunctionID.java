package com.smartdevicelink.protocol.enums;

import java.util.ArrayList;
import java.util.Arrays;

import com.smartdevicelink.proxy.constants.Names;

public class FunctionID {
	static ArrayList<String> functionID = null;
	
	public FunctionID() {
	}
	
	static public String getFunctionName(int i) {
		if (functionID == null) {
			initFunctionIds();
		}
		return functionID.get(i);
	}
	
	static public void initFunctionIds() {
		String [] functionIds = new String[98306];
		functionIds[1] = Names.RegisterAppInterface;
		functionIds[2] = Names.UnregisterAppInterface;
		functionIds[3] = Names.SetGlobalProperties;
		functionIds[4] = Names.ResetGlobalProperties;
		functionIds[5] = Names.AddCommand;
		functionIds[6] = Names.DeleteCommand;
		functionIds[7] = Names.AddSubMenu;
		functionIds[8] = Names.DeleteSubMenu;
		functionIds[9] = Names.CreateInteractionChoiceSet;
		functionIds[10] = Names.PerformInteraction;
		functionIds[11] = Names.DeleteInteractionChoiceSet;
		functionIds[12] = Names.Alert;
		functionIds[13] = Names.Show;
		functionIds[14] = Names.Speak;
		functionIds[15] = Names.SetMediaClockTimer;
		functionIds[16] = Names.PerformAudioPassThru;
		functionIds[17] = Names.EndAudioPassThru;
		functionIds[18] = Names.SubscribeButton;
		functionIds[19] = Names.UnsubscribeButton;
		functionIds[20] = Names.SubscribeVehicleData;
		functionIds[21] = Names.UnsubscribeVehicleData;
		functionIds[22] = Names.GetVehicleData;
		functionIds[23] = Names.ReadDID;
		functionIds[24] = Names.GetDTCs;
		functionIds[25] = Names.ScrollableMessage;
		functionIds[26] = Names.Slider;
		functionIds[27] = Names.ShowConstantTBT;
		functionIds[28] = Names.AlertManeuver;
		functionIds[29] = Names.UpdateTurnList;
		functionIds[30] = Names.ChangeRegistration;
		functionIds[31] = Names.GenericResponse;
		functionIds[32] = Names.PutFile;
		functionIds[33] = Names.DeleteFile;
		functionIds[34] = Names.ListFiles;
		functionIds[35] = Names.SetAppIcon;
		functionIds[36] = Names.SetDisplayLayout;

        // **************** Profile Management ********************
        functionIds[32762] = Names.addProfile;
        functionIds[32763] = Names.removeProfile;
        functionIds[32764] = Names.loadProfile;
        functionIds[32765] = Names.unloadProfile;
        functionIds[32766] = Names.sendAppToProfileMessage;
        functionIds[32767] = Names.appStateChanged;
        // ********************************************************
		
		functionIds[32768] = Names.OnHMIStatus;
		functionIds[32769] = Names.OnAppInterfaceUnregistered;
		functionIds[32770] = Names.OnButtonEvent;
		functionIds[32771] = Names.OnButtonPress;
		functionIds[32772] = Names.OnVehicleData;
		functionIds[32773] = Names.OnCommand;
		functionIds[32774] = Names.OnTBTClientState;
		functionIds[32775] = Names.OnDriverDistraction;
		functionIds[32776] = Names.OnPermissionsChange;
		functionIds[32777] = Names.OnAudioPassThru;
		functionIds[32778] = Names.OnLanguageChange;

        // **************** Profile Management ********************
        functionIds[65534] = Names.onReceiveMessageFromProfile;
        functionIds[65535] = Names.onProfileClosed;
        // ********************************************************
		
		functionIds[65536] = Names.EncodedSyncPData;
		functionIds[65537] = Names.SyncPData;
		
		functionIds[98304] = Names.OnEncodedSyncPData;
		functionIds[98305] = Names.OnSyncPData;
		
		functionID = new ArrayList<String>(Arrays.asList(functionIds));
	}
	
	static public int getFunctionID(String functionName) {
		if (functionID == null) {
			initFunctionIds();
		}
		return functionID.indexOf(functionName);
	}
}