package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;

import com.smartdevicelink.proxy.RPCStruct;
import com.smartdevicelink.proxy.constants.Names;

public class FuelEconomyInformation extends RPCStruct {

    public FuelEconomyInformation() { }
    public FuelEconomyInformation(Hashtable hash) {
        super(hash);
    }
    public void setFuelEconomySinceLastReset(Double fuelEconomySinceLastReset) {
        if (fuelEconomySinceLastReset != null) {
        	store.put(Names.fuelEconomySinceLastReset, fuelEconomySinceLastReset);
        } else {
        	store.remove(Names.fuelEconomySinceLastReset);
        }
    }
    public Double getFuelEconomySinceLastReset() {
        return (Double) store.get(Names.fuelEconomySinceLastReset);
    }
    public void setCurrentTripFuelEconomy(Double currentTripFuelEconomy) {
        if (currentTripFuelEconomy != null) {
        	store.put(Names.currentTripFuelEconomy, currentTripFuelEconomy);
        } else {
        	store.remove(Names.currentTripFuelEconomy);
        }
    }
    public Double getCurrentTripFuelEconomy() {
        return (Double) store.get(Names.currentTripFuelEconomy);
    }
    public void setAverageTripFuelEconomy(Double averageTripFuelEconomy) {
        if (averageTripFuelEconomy != null) {
        	store.put(Names.averageTripFuelEconomy, averageTripFuelEconomy);
        } else {
        	store.remove(Names.averageTripFuelEconomy);
        }
    }
    public Double getAverageTripFuelEconomy() {
        return (Double) store.get(Names.averageTripFuelEconomy);
    }
    public void setCurrentCycleFuelEconomy(Double currentCycleFuelEconomy) {
        if (currentCycleFuelEconomy != null) {
        	store.put(Names.currentCycleFuelEconomy, currentCycleFuelEconomy);
        } else {
        	store.remove(Names.currentCycleFuelEconomy);
        }
    }
    public Double getCurrentCycleFuelEconomy() {
        return (Double) store.get(Names.currentCycleFuelEconomy);
    }
}