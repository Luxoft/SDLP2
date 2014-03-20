package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCNotification;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.TriggerSource;
import com.smartdevicelink.util.DebugTool;

public class OnSmartDeviceLinkChoiceChosen extends RPCNotification {
	
	
	public class SmartDeviceLinkSubMenu {
		private Integer _menuID = null;
		private Integer _position = null;
		private String _menuName = null;
		
		// Constructor
		SmartDeviceLinkSubMenu(Integer menuID, Integer position, String menuName) {
			_menuID = menuID;
			_position = position;
			_menuName = menuName;
		}
		
		// Restrict no-arg constructor
		private SmartDeviceLinkSubMenu() {}
		
		// Public Getters
		public Integer getMenuID() {
			return _menuID;
		}
		
		public String getMenuName() {
			return _menuName;
		}
		
		public String toString() {
			return _menuName;
		}
	}
	
	public class SmartDeviceLinkCommand {
		private Integer _commandID = null;
		private SmartDeviceLinkSubMenu _parentSubMenu = null;
		private Integer _position = null;
		private String _menuName = null;
		private Vector<String> _vrCommands = null;
		
		// Constructor
		SmartDeviceLinkCommand(Integer commandID, SmartDeviceLinkSubMenu parentSubMenu, Integer position, String menuName, Vector<String> vrCommands) {
			_commandID = commandID;
			_parentSubMenu = parentSubMenu;
			_position = position;
			_menuName = menuName;
			_vrCommands = vrCommands;
		}
		
		// Restrict no-arg constructor
		private SmartDeviceLinkCommand() {}
		
		// Public Getters
		public Integer getCommandID() {
			return _commandID;
		}
		
		public SmartDeviceLinkSubMenu getParentSubMenu() {
			return _parentSubMenu;
		}
		
		public String getMenuName() {
			return _menuName;
		}
		
		public Vector<String> getVrCommands() {
			return _vrCommands;
		}
		
		public String toString() {
			return _menuName;
		}
	}
	
	public class SmartDeviceLinkChoice {
		
		private Choice _choice = null;
		
		// Constructor
		SmartDeviceLinkChoice(Choice choice) {
			_choice = choice;
		}
		
		public Choice getChoice() {
			return _choice;
		}
		
		public Integer getChoiceID() {
			return _choice.getChoiceID();
		}
		
		public String getMenuName() {
			return _choice.getMenuName();
		}
		
		public Vector<String> getVrCommands() {
			return _choice.getVrCommands();
		}
		
		public String toString() {
			return _choice.getMenuName();
		}
	}
	
	public class SmartDeviceLinkChoiceSet {
		private Integer _choiceSetID = null;
		private Vector<SmartDeviceLinkChoice> _choiceSet = null;
		
		// Constructor
		SmartDeviceLinkChoiceSet(Integer choiceSetID, Vector<SmartDeviceLinkChoice> choiceSet) {
			_choiceSetID = choiceSetID;
			_choiceSet = choiceSet;
		}
		
		public Integer getChoiceSetID() {
			return _choiceSetID;
		}
		
		public Vector<SmartDeviceLinkChoice> getChoiceSet() {
			return _choiceSet;
		}
	}
	
	
	

	public OnSmartDeviceLinkChoiceChosen() {
		super(Names.OnSmartDeviceLinkChoiceChosen);
	}
	public OnSmartDeviceLinkChoiceChosen(Hashtable hash){
		super(hash);
	}
    public SmartDeviceLinkChoice getSmartDeviceLinkChoice() {
    	return (SmartDeviceLinkChoice) parameters.get(Names.smartDeviceLinkChoice);
    }
    public void setSmartDeviceLinkChoice(SmartDeviceLinkChoice smartDeviceLinkChoice) {
    	if (smartDeviceLinkChoice != null) {
    		parameters.put(Names.smartDeviceLinkChoice, smartDeviceLinkChoice);
    	}
    }
    public TriggerSource getTriggerSource() {
        Object obj = parameters.get(Names.triggerSource);
        if (obj instanceof TriggerSource) {
            return (TriggerSource) obj;
        } else if (obj instanceof String) {
            TriggerSource theCode = null;
            try {
                theCode = TriggerSource.valueForString((String) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.triggerSource, e);
            }
            return theCode;
        }
        return null;
    }
    public void setTriggerSource( TriggerSource triggerSource ) {
        if (triggerSource != null) {
            parameters.put(Names.triggerSource, triggerSource );
        }
    }
}
