package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataType;
import com.smartdevicelink.util.DebugTool;

/**
 * Unsubscribe Vehicle Data Response is sent, when UnsubscribeVehicleData has been called
 * 
 * @since SmartDeviceLink 2.0
 */
public class UnsubscribeVehicleDataResponse extends RPCResponse {

	/**
	 * Constructs a new UnsubscribeVehicleDataResponse object
	 */
    public UnsubscribeVehicleDataResponse() {
        super("UnsubscribeVehicleData");
    }

	/**
	 * Constructs a new UnsubscribeVehicleDataResponse object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public UnsubscribeVehicleDataResponse(Hashtable hash) {
        super(hash);
    }
    /**
     * Sets Gps
     * @param gps
     */
    public void setGps(VehicleDataResult gps) {
        if (gps != null) {
            parameters.put(Names.gps, gps);
        } else {
        	parameters.remove(Names.gps);
        }
    }
    /**
     * Gets Gps
     * @return VehicleDataResult
     */
    public VehicleDataResult getGps() {
    	Object obj = parameters.get(Names.gps);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.gps, e);
            }
        }
        return null;
    }
    /**
     * Sets Speed
     * @param speed
     */
    public void setSpeed(VehicleDataResult speed) {
        if (speed != null) {
            parameters.put(Names.speed, speed);
        } else {
        	parameters.remove(Names.speed);
        }
    }
    /**
     * Gets Speed
     * @return VehicleDataResult
     */
    public VehicleDataResult getSpeed() {
    	Object obj = parameters.get(Names.speed);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.speed, e);
            }
        }
        return null;
    }
    /**
     * Sets rpm
     * @param rpm
     */
    public void setRpm(VehicleDataResult rpm) {
        if (rpm != null) {
            parameters.put(Names.rpm, rpm);
        } else {
        	parameters.remove(Names.rpm);
        }
    }
    /**
     * Gets rpm
     * @return VehicleDataResult
     */
    public VehicleDataResult getRpm() {
    	Object obj = parameters.get(Names.rpm);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.rpm, e);
            }
        }
        return null;
    }
    /**
     * Sets Fuel Level
     * @param fuelLevel
     */
    public void setFuelLevel(VehicleDataResult fuelLevel) {
        if (fuelLevel != null) {
            parameters.put(Names.fuelLevel, fuelLevel);
        } else {
        	parameters.remove(Names.fuelLevel);
        }
    }
    /**
     * Gets Fuel Level
     * @return VehicleDataResult
     */
    public VehicleDataResult getFuelLevel() {
    	Object obj = parameters.get(Names.fuelLevel);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.fuelLevel, e);
            }
        }
        return null;
    }
    /**
     * Sets Fuel Level State
     * @param fuelLevel_State
     */
    public void setFuelLevel_State(VehicleDataResult fuelLevel_State) {
        if (fuelLevel_State != null) {
            parameters.put(Names.fuelLevel_State, fuelLevel_State);
        } else {
        	parameters.remove(Names.fuelLevel_State);
        }
    }
    /**
     * Gets Fuel Level State
     * @return VehicleDataResult
     */
    public VehicleDataResult getFuelLevel_State() {
    	Object obj = parameters.get(Names.fuelLevel_State);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.fuelLevel_State, e);
            }
        }
        return null;
    }
    /**
     * Sets Instant Fuel Comsumption
     * @param instantFuelConsumption
     */
    public void setInstantFuelConsumption(VehicleDataResult instantFuelConsumption) {
        if (instantFuelConsumption != null) {
            parameters.put(Names.instantFuelConsumption, instantFuelConsumption);
        } else {
        	parameters.remove(Names.instantFuelConsumption);
        }
    }
    /**
     * Gets Instant Fuel Comsumption
     * @return VehicleDataResult
     */
    public VehicleDataResult getInstantFuelConsumption() {
    	Object obj = parameters.get(Names.instantFuelConsumption);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.instantFuelConsumption, e);
            }
        }
        return null;
    }
    /**
     * Sets External Temperature
     * @param externalTemperature
     */
    public void setExternalTemperature(VehicleDataResult externalTemperature) {
        if (externalTemperature != null) {
            parameters.put(Names.externalTemperature, externalTemperature);
        } else {
        	parameters.remove(Names.externalTemperature);
        }
    }
    /**
     * Gets External Temperature
     * @return VehicleDataResult
     */
    public VehicleDataResult getExternalTemperature() {
    	Object obj = parameters.get(Names.externalTemperature);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.externalTemperature, e);
            }
        }
        return null;
    }
    /**
     * Gets currently selected gear data
     * @param prndl
     */
    public void setPrndl(VehicleDataResult prndl) {
        if (prndl != null) {
            parameters.put(Names.prndl, prndl);
        } else {
        	parameters.remove(Names.prndl);
        }
    }
    /**
     * Gets currently selected gear data
     * @return VehicleDataResult
     */
    public VehicleDataResult getPrndl() {
    	Object obj = parameters.get(Names.prndl);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.prndl, e);
            }
        }
        return null;
    }
    /**
     * Sets Tire Pressure
     * @param tirePressure
     */
    public void setTirePressure(VehicleDataResult tirePressure) {
        if (tirePressure != null) {
            parameters.put(Names.tirePressure, tirePressure);
        } else {
        	parameters.remove(Names.tirePressure);
        }
    }
    /**
     * Gets Tire Pressure
     * @return VehicleDataResult
     */
    public VehicleDataResult getTirePressure() {
    	Object obj = parameters.get(Names.tirePressure);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.tirePressure, e);
            }
        }
        return null;
    }
    /**
     * Sets Odometer
     * @param odometer
     */
    public void setOdometer(VehicleDataResult odometer) {
        if (odometer != null) {
            parameters.put(Names.odometer, odometer);
        } else {
        	parameters.remove(Names.odometer);
        }
    }
    /**
     * Gets Odometer
     * @return VehicleDataResult
     */
    public VehicleDataResult getOdometer() {
    	Object obj = parameters.get(Names.odometer);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.odometer, e);
            }
        }
        return null;
    }
    /**
     * Sets Belt Status
     * @param beltStatus
     */
    public void setBeltStatus(VehicleDataResult beltStatus) {
        if (beltStatus != null) {
            parameters.put(Names.beltStatus, beltStatus);
        } else {
        	parameters.remove(Names.beltStatus);
        }
    }
    /**
     * Gets Belt Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getBeltStatus() {
    	Object obj = parameters.get(Names.beltStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.beltStatus, e);
            }
        }
        return null;
    }
    /**
     * Sets Body Information
     * @param bodyInformation
     */
    public void setBodyInformation(VehicleDataResult bodyInformation) {
        if (bodyInformation != null) {
            parameters.put(Names.bodyInformation, bodyInformation);
        } else {
        	parameters.remove(Names.bodyInformation);
        }
    }
    /**
     * Gets Body Information
     * @return VehicleDataResult
     */
    public VehicleDataResult getBodyInformation() {
    	Object obj = parameters.get(Names.bodyInformation);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.bodyInformation, e);
            }
        }
        return null;
    }
    /**
     * Sets Device Status
     * @param deviceStatus
     */
    public void setDeviceStatus(VehicleDataResult deviceStatus) {
        if (deviceStatus != null) {
            parameters.put(Names.deviceStatus, deviceStatus);
        } else {
        	parameters.remove(Names.deviceStatus);
        }
    }
    /**
     * Gets Device Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getDeviceStatus() {
    	Object obj = parameters.get(Names.deviceStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.deviceStatus, e);
            }
        }
        return null;
    }
    /**
     * Sets Driver Braking
     * @param driverBraking
     */
    public void setDriverBraking(VehicleDataResult driverBraking) {
        if (driverBraking != null) {
            parameters.put(Names.driverBraking, driverBraking);
        } else {
        	parameters.remove(Names.driverBraking);
        }
    }
    /**
     * Gets Driver Braking
     * @return VehicleDataResult
     */
    public VehicleDataResult getDriverBraking() {
    	Object obj = parameters.get(Names.driverBraking);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.driverBraking, e);
            }
        }
        return null;
    }
    /**
     * Sets Wiper Status
     * @param wiperStatus
     */
    public void setWiperStatus(VehicleDataResult wiperStatus) {
        if (wiperStatus != null) {
            parameters.put(Names.wiperStatus, wiperStatus);
        } else {
        	parameters.remove(Names.wiperStatus);
        }
    }
    /**
     * Gets Wiper Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getWiperStatus() {
    	Object obj = parameters.get(Names.wiperStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.wiperStatus, e);
            }
        }
        return null;
    }
    /**
     * Sets Fuel Economy
     * @param fuelEconomy
     */
    public void setFuelEconomy(VehicleDataResult fuelEconomy) {
        if (fuelEconomy != null) {
            parameters.put(Names.fuelEconomy, fuelEconomy);
        } else {
        	parameters.remove(Names.fuelEconomy);
        }
    }
    /**
     * Gets Fuel Economy
     * @return VehicleDataResult
     */
    public VehicleDataResult getFuelEconomy() {
    	Object obj = parameters.get(Names.fuelEconomy);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.fuelEconomy, e);
            }
        }
        return null;
    }
    /**
     * Sets Engine Oil Life
     * @param engineOilLife
     */
    public void setEngineOilLife(VehicleDataResult engineOilLife) {
        if (engineOilLife != null) {
            parameters.put(Names.engineOilLife, engineOilLife);
        } else {
        	parameters.remove(Names.engineOilLife);
        }
    }
    /**
     * Gets Engine Oil Life
     * @return VehicleDataResult
     */
    public VehicleDataResult getEngineOilLife() {
    	Object obj = parameters.get(Names.engineOilLife);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.engineOilLife, e);
            }
        }
        return null;
    }
    /**
     * Sets Head Lamp Status
     * @param headLampStatus
     */
    public void setHeadLampStatus(VehicleDataResult headLampStatus) {
        if (headLampStatus != null) {
            parameters.put(Names.headLampStatus, headLampStatus);
        } else {
        	parameters.remove(Names.headLampStatus);
        }
    }
    /**
     * Gets Head Lamp Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getHeadLampStatus() {
    	Object obj = parameters.get(Names.headLampStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.headLampStatus, e);
            }
        }
        return null;
    }
    /**
     * Sets Battery Voltage
     * @param batteryVoltage
     */
    public void setBatteryVoltage(VehicleDataResult batteryVoltage) {
        if (batteryVoltage != null) {
            parameters.put(Names.batteryVoltage, batteryVoltage);
        } else {
        	parameters.remove(Names.batteryVoltage);
        }
    }
    /**
     * Gets Battery Voltage
     * @return VehicleDataResult
     */
    public VehicleDataResult getBatteryVoltage() {
    	Object obj = parameters.get(Names.batteryVoltage);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.batteryVoltage, e);
            }
        }
        return null;
    }
    /**
     * Sets Brake Torque
     * @param brakeTorque
     */
    public void setBrakeTorque(VehicleDataResult brakeTorque) {
        if (brakeTorque != null) {
            parameters.put(Names.brakeTorque, brakeTorque);
        } else {
        	parameters.remove(Names.brakeTorque);
        }
    }
    /**
     * Gets Brake Torque
     * @return VehicleDataResult
     */
    public VehicleDataResult getBrakeTorque() {
    	Object obj = parameters.get(Names.brakeTorque);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.brakeTorque, e);
            }
        }
        return null;
    }
    /**
     * Sets Engine Torque
     * @param engineTorque
     */
    public void setEngineTorque(VehicleDataResult engineTorque) {
        if (engineTorque != null) {
            parameters.put(Names.engineTorque, engineTorque);
        } else {
        	parameters.remove(Names.engineTorque);
        }
    }
    /**
     * Gets Engine Torque
     * @return VehicleDataResult
     */
    public VehicleDataResult getEngineTorque() {
    	Object obj = parameters.get(Names.engineTorque);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.engineTorque, e);
            }
        }
        return null;
    }
    /**
     * Sets Turbo Boost
     * @param turboBoost
     */
    public void setTurboBoost(VehicleDataResult turboBoost) {
        if (turboBoost != null) {
            parameters.put(Names.turboBoost, turboBoost);
        } else {
        	parameters.remove(Names.turboBoost);
        }
    }
    /**
     * Gets Turbo Boost
     * @return VehicleDataResult
     */
    public VehicleDataResult getTurboBoost() {
    	Object obj = parameters.get(Names.turboBoost);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.turboBoost, e);
            }
        }
        return null;
    }
    /**
     * Sets Coolant Temp
     * @param coolantTemp
     */
    public void setCoolantTemp(VehicleDataResult coolantTemp) {
        if (coolantTemp != null) {
            parameters.put(Names.coolantTemp, coolantTemp);
        } else {
        	parameters.remove(Names.coolantTemp);
        }
    }
    /**
     * Gets Coolant Temp
     * @return VehicleDataResult
     */
    public VehicleDataResult getCoolantTemp() {
    	Object obj = parameters.get(Names.coolantTemp);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.coolantTemp, e);
            }
        }
        return null;
    }
    /**
     * Sets Air Fuel Ratio
     * @param airFuelRatio
     */
    public void setAirFuelRatio(VehicleDataResult airFuelRatio) {
        if (airFuelRatio != null) {
            parameters.put(Names.airFuelRatio, airFuelRatio);
        } else {
        	parameters.remove(Names.airFuelRatio);
        }
    }
    /**
     * Gets Air Fuel Ratio
     * @return VehicleDataResult
     */
    public VehicleDataResult getAirFuelRatio() {
    	Object obj = parameters.get(Names.airFuelRatio);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.airFuelRatio, e);
            }
        }
        return null;
    }
    /**
     * Sets Cooling Head Temp
     * @param coolingHeadTemp
     */
    public void setCoolingHeadTemp(VehicleDataResult coolingHeadTemp) {
        if (coolingHeadTemp != null) {
            parameters.put(Names.coolingHeadTemp, coolingHeadTemp);
        } else {
        	parameters.remove(Names.coolingHeadTemp);
        }
    }
    /**
     * Gets Cooling Head Temp
     * @return VehicleDataResult
     */
    public VehicleDataResult getCoolingHeadTemp() {
    	Object obj = parameters.get(Names.coolingHeadTemp);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.coolingHeadTemp, e);
            }
        }
        return null;
    }
    /**
     * Sets Oil Temp
     * @param oilTemp
     */
    public void setOilTemp(VehicleDataResult oilTemp) {
        if (oilTemp != null) {
            parameters.put(Names.oilTemp, oilTemp);
        } else {
        	parameters.remove(Names.oilTemp);
        }
    }
    /**
     * Gets Oil Temp
     * @return VehicleDataResult
     */
    public VehicleDataResult getOilTemp() {
    	Object obj = parameters.get(Names.oilTemp);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.oilTemp, e);
            }
        }
        return null;
    }
    /**
     * Sets Intake Air Temp
     * @param intakeAirTemp
     */
    public void setIntakeAirTemp(VehicleDataResult intakeAirTemp) {
        if (intakeAirTemp != null) {
            parameters.put(Names.intakeAirTemp, intakeAirTemp);
        } else {
        	parameters.remove(Names.intakeAirTemp);
        }
    }
    /**
     * Gets Intake Air Temp
     * @return VehicleDataResult
     */
    public VehicleDataResult getIntakeAirTemp() {
    	Object obj = parameters.get(Names.intakeAirTemp);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.intakeAirTemp, e);
            }
        }
        return null;
    }
    /**
     * Sets Gear Shift Advice
     * @param gearShiftAdvice
     */
    public void setGearShiftAdvice(VehicleDataResult gearShiftAdvice) {
        if (gearShiftAdvice != null) {
            parameters.put(Names.gearShiftAdvice, gearShiftAdvice);
        } else {
        	parameters.remove(Names.gearShiftAdvice);
        }
    }
    /**
     * Gets Gear Shift Advice
     * @return VehicleDataResult
     */
    public VehicleDataResult getGearShiftAdvice() {
    	Object obj = parameters.get(Names.gearShiftAdvice);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.gearShiftAdvice, e);
            }
        }
        return null;
    }
    /**
     * Sets Acceleration
     * @param acceleration
     */
    public void setAcceleration(VehicleDataResult acceleration) {
        if (acceleration != null) {
            parameters.put(Names.acceleration, acceleration);
        } else {
        	parameters.remove(Names.acceleration);
        }
    }
    /**
     * Gets Acceleration
     * @return VehicleDataResult
     */
    public VehicleDataResult getAcceleration() {
    	Object obj = parameters.get(Names.acceleration);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.acceleration, e);
            }
        }
        return null;
    }
    /**
     * Sets AccPedal Position
     * @param accPedalPosition
     */
    public void setAccPedalPosition(VehicleDataResult accPedalPosition) {
        if (accPedalPosition != null) {
            parameters.put(Names.accPedalPosition, accPedalPosition);
        } else {
        	parameters.remove(Names.accPedalPosition);
        }
    }
    /**
     * Gets AccPedal Position
     * @return VehicleDataResult
     */
    public VehicleDataResult getAccPedalPosition() {
    	Object obj = parameters.get(Names.accPedalPosition);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.accPedalPosition, e);
            }
        }
        return null;
    }
    /**
     * Sets ClutchPedal Position
     * @param clutchPedalPosition
     */
    public void setClutchPedalPosition(VehicleDataResult clutchPedalPosition) {
        if (clutchPedalPosition != null) {
            parameters.put(Names.clutchPedalPosition, clutchPedalPosition);
        } else {
        	parameters.remove(Names.clutchPedalPosition);
        }
    }
    /**
     * Gets ClutchPedal Position
     * @return VehicleDataResult
     */
    public VehicleDataResult getClutchPedalPosition() {
    	Object obj = parameters.get(Names.clutchPedalPosition);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.clutchPedalPosition, e);
            }
        }
        return null;
    }
    /**
     * Sets Reverse Gear Status
     * @param reverseGearStatus
     */
    public void setReverseGearStatus(VehicleDataResult reverseGearStatus) {
        if (reverseGearStatus != null) {
            parameters.put(Names.reverseGearStatus, reverseGearStatus);
        } else {
        	parameters.remove(Names.reverseGearStatus);
        }
    }
    /**
     * Gets Reverse Gear Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getReverseGearStatus() {
    	Object obj = parameters.get(Names.reverseGearStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.reverseGearStatus, e);
            }
        }
        return null;
    }
    /**
     * Sets AccTorque
     * @param accTorque
     */
    public void setAccTorque(VehicleDataResult accTorque) {
        if (accTorque != null) {
            parameters.put(Names.accTorque, accTorque);
        } else {
        	parameters.remove(Names.accTorque);
        }
    }
    /**
     * Gets AccTorque
     * @return VehicleDataResult
     */
    public VehicleDataResult getAccTorque() {
    	Object obj = parameters.get(Names.accTorque);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.accTorque, e);
            }
        }
        return null;
    }
    /**
     * Sets Event Info
     * @param evInfo
     */
    public void setEvInfo(VehicleDataResult evInfo) {
        if (evInfo != null) {
            parameters.put(Names.evInfo, evInfo);
        } else {
        	parameters.remove(Names.evInfo);
        }
    }
    /**
     * Gets Event Info
     * @return VehicleDataResult
     */
    public VehicleDataResult getEvInfo() {
    	Object obj = parameters.get(Names.evInfo);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.evInfo, e);
            }
        }
        return null;
    }
    /**
     * Sets Ambient Light Status
     * @param ambientLightStatus
     */
    public void setAmbientLightStatus(VehicleDataResult ambientLightStatus) {
        if (ambientLightStatus != null) {
            parameters.put(Names.ambientLightStatus, ambientLightStatus);
        } else {
        	parameters.remove(Names.ambientLightStatus);
        }
    }
    /**
     * Get Ambient Light Status
     * @return VehicleDataResult
     */
    public VehicleDataResult getAmbientLightStatus() {
    	Object obj = parameters.get(Names.ambientLightStatus);
        if (obj instanceof VehicleDataResult) {
            return (VehicleDataResult) obj;
        } else if (obj instanceof Hashtable) {
        	try {
        		return new VehicleDataResult((Hashtable) obj);
            } catch (Exception e) {
            	DebugTool.logError("Failed to parse " + getClass().getSimpleName() + "." + Names.ambientLightStatus, e);
            }
        }
        return null;
    }   
}
