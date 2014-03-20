package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.MaintenanceModeStatus;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataActiveStatus;
import com.smartdevicelink.util.DebugTool;

public class EVInfo extends RPCStruct {

    public EVInfo() {}
    public EVInfo(Hashtable hash) {
        super(hash);
    }
    public void setElectricFuelConsumption(Double electricFuelConsumption) {
        if (electricFuelConsumption != null) {
        	store.put(Names.electricFuelConsumption, electricFuelConsumption);
        } else {
        	store.remove(Names.electricFuelConsumption);
        }
    }
    public Double getElectricFuelConsumption() {
        return (Double) store.get(Names.electricFuelConsumption);
    }
    public void setStateOfCharge(Double stateOfCharge) {
        if (stateOfCharge != null) {
        	store.put(Names.stateOfCharge, stateOfCharge);
        } else {
        	store.remove(Names.stateOfCharge);
        }
    }
    public Double getStateOfCharge() {
        return (Double) store.get(Names.stateOfCharge);
    }
    public void setFuelMaintenanceMode(MaintenanceModeStatus fuelMaintenanceMode) {
        if (fuelMaintenanceMode != null) {
        	store.put(Names.fuelMaintenanceMode, fuelMaintenanceMode);
        } else {
        	store.remove(Names.fuelMaintenanceMode);
        }
    }
    public MaintenanceModeStatus getFuelMaintenanceMode() {
        Object obj = store.get(Names.fuelMaintenanceMode);
        if (obj instanceof MaintenanceModeStatus) {
            return (MaintenanceModeStatus) obj;
        } else if (obj instanceof String) {
        	MaintenanceModeStatus theCode = null;
            try {
                theCode = MaintenanceModeStatus.valueForString((String) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.fuelMaintenanceMode, e);
            }
            return theCode;
        }
        return null;
    }
    public void setDistanceToEmpty(Double distanceToEmpty) {
        if (distanceToEmpty != null) {
        	store.put(Names.distanceToEmpty, distanceToEmpty);
        } else {
        	store.remove(Names.distanceToEmpty);
        }
    }
    public Double getDistanceToEmpty() {
        return (Double) store.get(Names.distanceToEmpty);
    }
}