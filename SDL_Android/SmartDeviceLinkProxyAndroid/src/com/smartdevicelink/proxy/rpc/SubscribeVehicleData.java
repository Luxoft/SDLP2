package com.smartdevicelink.proxy.rpc;

import java.util.Hashtable;
import java.util.Vector;

import com.smartdevicelink.proxy.RPCRequest;
import com.smartdevicelink.proxy.constants.Names;
import com.smartdevicelink.proxy.rpc.enums.VehicleDataType;
import com.smartdevicelink.util.DebugTool;

/**
 * Subscribes for specific published vehicle data items. The data will be only
 * sent, if it has changed. The application will be notified by the
 * onVehicleData notification whenever new data is available. The update rate is
 * very much dependent on sensors, vehicle architecture and vehicle type. Be
 * also prepared for the situation that a signal is not available on a vehicle
 * <p>
 * Function Group: Location, VehicleInfo and DrivingChara
 * <p>
 * <b>HMILevel needs to be FULL, LIMITED or BACKGROUND</b>
 * </p>
 * 
 * @since SmartDeviceLink 2.0
 * @see UnsubscribeVehicleData
 * @see GetVehicleData
 */
public class SubscribeVehicleData extends RPCRequest {

	/**
	 * Constructs a new SubscribeVehicleData object
	 */
    public SubscribeVehicleData() {
        super("SubscribeVehicleData");
    }

	/**
	 * Constructs a new SubscribeVehicleData object indicated by the Hashtable
	 * parameter
	 * <p>
	 * 
	 * @param hash
	 *            The Hashtable to use
	 */
    public SubscribeVehicleData(Hashtable hash) {
        super(hash);
    }

	/**
	 * Sets a boolean value. If true, subscribes Gps data
	 * 
	 * @param gps
	 *            a boolean value
	 */
    public void setGps(Boolean gps) {
        if (gps != null) {
            parameters.put(Names.gps, gps);
        } else {
        	parameters.remove(Names.gps);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Gps data has been subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Gps data has been
	 *         subscribed.
	 */
    public Boolean getGps() {
        return (Boolean) parameters.get(Names.gps);
    }

	/**
	 * Sets a boolean value. If true, subscribes speed data
	 * 
	 * @param speed
	 *            a boolean value
	 */
    public void setSpeed(Boolean speed) {
        if (speed != null) {
            parameters.put(Names.speed, speed);
        } else {
        	parameters.remove(Names.speed);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Speed data has been subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Speed data has been
	 *         subscribed.
	 */
    public Boolean getSpeed() {
        return (Boolean) parameters.get(Names.speed);
    }

	/**
	 * Sets a boolean value. If true, subscribes rpm data
	 * 
	 * @param rpm
	 *            a boolean value
	 */
    public void setRpm(Boolean rpm) {
        if (rpm != null) {
            parameters.put(Names.rpm, rpm);
        } else {
        	parameters.remove(Names.rpm);
        }
    }

	/**
	 * Gets a boolean value. If true, means the rpm data has been subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the rpm data has been
	 *         subscribed.
	 */
    public Boolean getRpm() {
        return (Boolean) parameters.get(Names.rpm);
    }

	/**
	 * Sets a boolean value. If true, subscribes FuelLevel data
	 * 
	 * @param fuelLevel
	 *            a boolean value
	 */
    public void setFuelLevel(Boolean fuelLevel) {
        if (fuelLevel != null) {
            parameters.put(Names.fuelLevel, fuelLevel);
        } else {
        	parameters.remove(Names.fuelLevel);
        }
    }

	/**
	 * Gets a boolean value. If true, means the FuelLevel data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the FuelLevel data has
	 *         been subscribed.
	 */
    public Boolean getFuelLevel() {
        return (Boolean) parameters.get(Names.fuelLevel);
    }

	/**
	 * Sets a boolean value. If true, subscribes fuelLevel_State data
	 * 
	 * @param fuelLevel_State
	 *            a boolean value
	 */
    public void setFuelLevel_State(Boolean fuelLevel_State) {
        if (fuelLevel_State != null) {
            parameters.put(Names.fuelLevel_State, fuelLevel_State);
        } else {
        	parameters.remove(Names.fuelLevel_State);
        }
    }

	/**
	 * Gets a boolean value. If true, means the fuelLevel_State data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the fuelLevel_State data
	 *         has been subscribed.
	 */
    public Boolean getFuelLevel_State() {
        return (Boolean) parameters.get(Names.fuelLevel_State);
    }

	/**
	 * Sets a boolean value. If true, subscribes instantFuelConsumption data
	 * 
	 * @param instantFuelConsumption
	 *            a boolean value
	 */
    public void setInstantFuelConsumption(Boolean instantFuelConsumption) {
        if (instantFuelConsumption != null) {
            parameters.put(Names.instantFuelConsumption, instantFuelConsumption);
        } else {
        	parameters.remove(Names.instantFuelConsumption);
        }
    }

	/**
	 * Gets a boolean value. If true, means the getInstantFuelConsumption data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the getInstantFuelConsumption data
	 *         has been subscribed.
	 */
    public Boolean getInstantFuelConsumption() {
        return (Boolean) parameters.get(Names.instantFuelConsumption);
    }

	/**
	 * Sets a boolean value. If true, subscribes externalTemperature data
	 * 
	 * @param externalTemperature
	 *            a boolean value
	 */
    public void setExternalTemperature(Boolean externalTemperature) {
        if (externalTemperature != null) {
            parameters.put(Names.externalTemperature, externalTemperature);
        } else {
        	parameters.remove(Names.externalTemperature);
        }
    }

	/**
	 * Gets a boolean value. If true, means the externalTemperature data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the externalTemperature data
	 *         has been subscribed.
	 */
    public Boolean getExternalTemperature() {
        return (Boolean) parameters.get(Names.externalTemperature);
    }

	/**
	 * Sets a boolean value. If true, subscribes Currently selected gear data
	 * 
	 * @param prndl
	 *            a boolean value
	 */
    public void setPrndl(Boolean prndl) {
        if (prndl != null) {
            parameters.put(Names.prndl, prndl);
        } else {
        	parameters.remove(Names.prndl);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Currently selected gear data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Currently selected gear data
	 *         has been subscribed.
	 */
    public Boolean getPrndl() {
        return (Boolean) parameters.get(Names.prndl);
    }

	/**
	 * Sets a boolean value. If true, subscribes tire pressure status data
	 * 
	 * @param tirePressure
	 *            a boolean value
	 */
    public void setTirePressure(Boolean tirePressure) {
        if (tirePressure != null) {
            parameters.put(Names.tirePressure, tirePressure);
        } else {
        	parameters.remove(Names.tirePressure);
        }
    }

	/**
	 * Gets a boolean value. If true, means the tire pressure status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the tire pressure status data
	 *         has been subscribed.
	 */
    public Boolean getTirePressure() {
        return (Boolean) parameters.get(Names.tirePressure);
    }

	/**
	 * Sets a boolean value. If true, subscribes odometer data
	 * 
	 * @param odometer
	 *            a boolean value
	 */
    public void setOdometer(Boolean odometer) {
        if (odometer != null) {
            parameters.put(Names.odometer, odometer);
        } else {
        	parameters.remove(Names.odometer);
        }
    }

	/**
	 * Gets a boolean value. If true, means the odometer data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the odometer data
	 *         has been subscribed.
	 */
    public Boolean getOdometer() {
        return (Boolean) parameters.get(Names.odometer);
    }

	/**
	 * Sets a boolean value. If true, subscribes belt Status data
	 * 
	 * @param beltStatus
	 *            a boolean value
	 */
    public void setBeltStatus(Boolean beltStatus) {
        if (beltStatus != null) {
            parameters.put(Names.beltStatus, beltStatus);
        } else {
        	parameters.remove(Names.beltStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the belt Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the belt Status data
	 *         has been subscribed.
	 */
    public Boolean getBeltStatus() {
        return (Boolean) parameters.get(Names.beltStatus);
    }

	/**
	 * Sets a boolean value. If true, subscribes body Information data
	 * 
	 * @param bodyInformation
	 *            a boolean value
	 */
    public void setBodyInformation(Boolean bodyInformation) {
        if (bodyInformation != null) {
            parameters.put(Names.bodyInformation, bodyInformation);
        } else {
        	parameters.remove(Names.bodyInformation);
        }
    }

	/**
	 * Gets a boolean value. If true, means the body Information data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the body Information data
	 *         has been subscribed.
	 */
    public Boolean getBodyInformation() {
        return (Boolean) parameters.get(Names.bodyInformation);
    }

	/**
	 * Sets a boolean value. If true, subscribes device Status data
	 * 
	 * @param deviceStatus
	 *            a boolean value
	 */
    public void setDeviceStatus(Boolean deviceStatus) {
        if (deviceStatus != null) {
            parameters.put(Names.deviceStatus, deviceStatus);
        } else {
        	parameters.remove(Names.deviceStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the device Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the device Status data
	 *         has been subscribed.
	 */
    public Boolean getDeviceStatus() {
        return (Boolean) parameters.get(Names.deviceStatus);
    }

	/**
	 * Sets a boolean value. If true, subscribes driver Braking data
	 * 
	 * @param driverBraking
	 *            a boolean value
	 */
    public void setDriverBraking(Boolean driverBraking) {
        if (driverBraking != null) {
            parameters.put(Names.driverBraking, driverBraking);
        } else {
        	parameters.remove(Names.driverBraking);
        }
    }

	/**
	 * Gets a boolean value. If true, means the driver Braking data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the driver Braking data
	 *         has been subscribed.
	 */
    public Boolean getDriverBraking() {
        return (Boolean) parameters.get(Names.driverBraking);
    }

	/**
	 * Sets a boolean value. If true, subscribes wiper Status data
	 * 
	 * @param wiperStatus
	 *            a boolean value
	 */
    public void setWiperStatus(Boolean wiperStatus) {
        if (wiperStatus != null) {
            parameters.put(Names.wiperStatus, wiperStatus);
        } else {
        	parameters.remove(Names.wiperStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the wiper Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the wiper Status data
	 *         has been subscribed.
	 */
    public Boolean getWiperStatus() {
        return (Boolean) parameters.get(Names.wiperStatus);
    }

	/**
	 * Sets a boolean value. If true, subscribes fuel Economy data
	 * 
	 * @param fuelEconomy
	 *            a boolean value
	 */
    public void setFuelEconomy(Boolean fuelEconomy) {
        if (fuelEconomy != null) {
            parameters.put(Names.fuelEconomy, fuelEconomy);
        } else {
        	parameters.remove(Names.fuelEconomy);
        }
    }

	/**
	 * Gets a boolean value. If true, means the fuel Economy data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the fuel Economy data
	 *         has been subscribed.
	 */
    public Boolean getFuelEconomy() {
        return (Boolean) parameters.get(Names.fuelEconomy);
    }

	/**
	 * Sets a boolean value. If true, subscribes Engine Oil Life data
	 * 
	 * @param engineOilLife
	 *            a boolean value
	 */
    public void setEngineOilLife(Boolean engineOilLife) {
        if (engineOilLife != null) {
            parameters.put(Names.engineOilLife, engineOilLife);
        } else {
        	parameters.remove(Names.engineOilLife);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Engine Oil Life data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Engine Oil Life data
	 *         has been subscribed.
	 */
    public Boolean getEngineOilLife() {
        return (Boolean) parameters.get(Names.engineOilLife);
    }

	/**
	 * Sets a boolean value. If true, subscribes Head Lamp Status data
	 * 
	 * @param headLampStatus
	 *            a boolean value
	 */
    public void setHeadLampStatus(Boolean headLampStatus) {
        if (headLampStatus != null) {
            parameters.put(Names.headLampStatus, headLampStatus);
        } else {
        	parameters.remove(Names.headLampStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Head Lamp Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Head Lamp Status data
	 *         has been subscribed.
	 */
    public Boolean getHeadLampStatus() {
        return (Boolean) parameters.get(Names.headLampStatus);
    }

	/**
	 * Sets a boolean value. If true, subscribes Battery Voltage data
	 * 
	 * @param batteryVoltage
	 *            a boolean value
	 */
    public void setBatteryVoltage(Boolean batteryVoltage) {
        if (batteryVoltage != null) {
            parameters.put(Names.batteryVoltage, batteryVoltage);
        } else {
        	parameters.remove(Names.batteryVoltage);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Battery Voltage data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Battery Voltage data
	 *         has been subscribed.
	 */
    public Boolean getBatteryVoltage() {
        return (Boolean) parameters.get(Names.batteryVoltage);
    }

	/**
	 * Sets a boolean value. If true, subscribes Brake Torque data
	 * 
	 * @param brakeTorque
	 *            a boolean value
	 */
    public void setBrakeTorque(Boolean brakeTorque) {
        if (brakeTorque != null) {
            parameters.put(Names.brakeTorque, brakeTorque);
        } else {
        	parameters.remove(Names.brakeTorque);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Brake Torque data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Brake Torque data
	 *         has been subscribed.
	 */
    public Boolean getBrakeTorque() {
        return (Boolean) parameters.get(Names.brakeTorque);
    }

	/**
	 * Sets a boolean value. If true, subscribes Engine Torque data
	 * 
	 * @param engineTorque
	 *            a boolean value
	 */
    public void setEngineTorque(Boolean engineTorque) {
        if (engineTorque != null) {
            parameters.put(Names.engineTorque, engineTorque);
        } else {
        	parameters.remove(Names.engineTorque);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Engine Torque data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Engine Torque data
	 *         has been subscribed.
	 */
    public Boolean getEngineTorque() {
        return (Boolean) parameters.get(Names.engineTorque);
    }

	/**
	 * Sets a boolean value. If true, subscribes Turbo Boost data
	 * 
	 * @param turboBoost
	 *            a boolean value
	 */
    public void setTurboBoost(Boolean turboBoost) {
        if (turboBoost != null) {
            parameters.put(Names.turboBoost, turboBoost);
        } else {
        	parameters.remove(Names.turboBoost);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Turbo Boost data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Turbo Boost data
	 *         has been subscribed.
	 */
    public Boolean getTurboBoost() {
        return (Boolean) parameters.get(Names.turboBoost);
    }

	/**
	 * Sets a boolean value. If true, subscribes Coolant Temp data
	 * 
	 * @param coolantTemp
	 *            a boolean value
	 */
    public void setCoolantTemp(Boolean coolantTemp) {
        if (coolantTemp != null) {
            parameters.put(Names.coolantTemp, coolantTemp);
        } else {
        	parameters.remove(Names.coolantTemp);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Coolant Temp data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Coolant Temp data
	 *         has been subscribed.
	 */
    public Boolean getCoolantTemp() {
        return (Boolean) parameters.get(Names.coolantTemp);
    }

	/**
	 * Sets a boolean value. If true, subscribes Air Fuel Ratio data
	 * 
	 * @param airFuelRatio
	 *            a boolean value
	 */
    public void setAirFuelRatio(Boolean airFuelRatio) {
        if (airFuelRatio != null) {
            parameters.put(Names.airFuelRatio, airFuelRatio);
        } else {
        	parameters.remove(Names.airFuelRatio);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Air Fuel Ratio data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Air Fuel Ratio data
	 *         has been subscribed.
	 */
    public Boolean getAirFuelRatio() {
        return (Boolean) parameters.get(Names.airFuelRatio);
    }

	/**
	 * Sets a boolean value. If true, subscribes Cooling Head Temp data
	 * 
	 * @param coolingHeadTemp
	 *            a boolean value
	 */
    public void setCoolingHeadTemp(Boolean coolingHeadTemp) {
        if (coolingHeadTemp != null) {
            parameters.put(Names.coolingHeadTemp, coolingHeadTemp);
        } else {
        	parameters.remove(Names.coolingHeadTemp);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Cooling Head Temp data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Cooling Head Temp data
	 *         has been subscribed.
	 */
    public Boolean getCoolingHeadTemp() {
        return (Boolean) parameters.get(Names.coolingHeadTemp);
    }

	/**
	 * Sets a boolean value. If true, subscribes Cooling Oil Temp data
	 * 
	 * @param oilTemp
	 *            a boolean value
	 */
    public void setOilTemp(Boolean oilTemp) {
        if (oilTemp != null) {
            parameters.put(Names.oilTemp, oilTemp);
        } else {
        	parameters.remove(Names.oilTemp);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Oil Temp data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Oil Temp data
	 *         has been subscribed.
	 */
    public Boolean getOilTemp() {
        return (Boolean) parameters.get(Names.oilTemp);
    }

	/**
	 * Sets a boolean value. If true, subscribes Intake Air Temp data
	 * 
	 * @param intakeAirTemp
	 *            a boolean value
	 */
    public void setIntakeAirTemp(Boolean intakeAirTemp) {
        if (intakeAirTemp != null) {
            parameters.put(Names.intakeAirTemp, intakeAirTemp);
        } else {
        	parameters.remove(Names.intakeAirTemp);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Intake Air Temp data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Intake Air Temp data
	 *         has been subscribed.
	 */
    public Boolean getIntakeAirTemp() {
        return (Boolean) parameters.get(Names.intakeAirTemp);
    }

	/**
	 * Sets a boolean value. If true, subscribes Gear Shift Advice data
	 * 
	 * @param gearShiftAdvice
	 *            a boolean value
	 */
    public void setGearShiftAdvice(Boolean gearShiftAdvice) {
        if (gearShiftAdvice != null) {
            parameters.put(Names.gearShiftAdvice, gearShiftAdvice);
        } else {
        	parameters.remove(Names.gearShiftAdvice);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Gear Shift Advice data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Gear Shift Advice data
	 *         has been subscribed.
	 */
    public Boolean getGearShiftAdvice() {
        return (Boolean) parameters.get(Names.gearShiftAdvice);
    }

	/**
	 * Sets a boolean value. If true, subscribes acceleration data
	 * 
	 * @param acceleration
	 *            a boolean value
	 */
    public void setAcceleration(Boolean acceleration) {
        if (acceleration != null) {
            parameters.put(Names.acceleration, acceleration);
        } else {
        	parameters.remove(Names.acceleration);
        }
    }

	/**
	 * Gets a boolean value. If true, means the acceleration data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the acceleration data
	 *         has been subscribed.
	 */
    public Boolean getAcceleration() {
        return (Boolean) parameters.get(Names.acceleration);
    }

	/**
	 * Sets a boolean value. If true, subscribes accPedalPosition data
	 * 
	 * @param accPedalPosition
	 *            a boolean value
	 */
    public void setAccPedalPosition(Boolean accPedalPosition) {
        if (accPedalPosition != null) {
            parameters.put(Names.accPedalPosition, accPedalPosition);
        } else {
        	parameters.remove(Names.accPedalPosition);
        }
    }

	/**
	 * Gets a boolean value. If true, means the accPedalPosition data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the accPedalPosition data
	 *         has been subscribed.
	 */
    public Boolean getAccPedalPosition() {
        return (Boolean) parameters.get(Names.accPedalPosition);
    }

	/**
	 * Sets a boolean value. If true, subscribes ClutchPedal Position data
	 * 
	 * @param clutchPedalPosition
	 *            a boolean value
	 */
    public void setClutchPedalPosition(Boolean clutchPedalPosition) {
        if (clutchPedalPosition != null) {
            parameters.put(Names.clutchPedalPosition, clutchPedalPosition);
        } else {
        	parameters.remove(Names.clutchPedalPosition);
        }
    }

	/**
	 * Gets a boolean value. If true, means the ClutchPedal Position data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the ClutchPedal Position data
	 *         has been subscribed.
	 */
    public Boolean getClutchPedalPosition() {
        return (Boolean) parameters.get(Names.clutchPedalPosition);
    }

	/**
	 * Sets a boolean value. If true, subscribes Reverse Gear Status data
	 * 
	 * @param reverseGearStatus
	 *            a boolean value
	 */
    public void setReverseGearStatus(Boolean reverseGearStatus) {
        if (reverseGearStatus != null) {
            parameters.put(Names.reverseGearStatus, reverseGearStatus);
        } else {
        	parameters.remove(Names.reverseGearStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Reverse Gear Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Reverse Gear Status data
	 *         has been subscribed.
	 */
    public Boolean getReverseGearStatus() {
        return (Boolean) parameters.get(Names.reverseGearStatus);
    }

	/**
	 * Sets a boolean value. If true, subscribes accTorque data
	 * 
	 * @param accTorque
	 *            a boolean value
	 */
    public void setAccTorque(Boolean accTorque) {
        if (accTorque != null) {
            parameters.put(Names.accTorque, accTorque);
        } else {
        	parameters.remove(Names.accTorque);
        }
    }

	/**
	 * Gets a boolean value. If true, means the accTorque data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the accTorque data
	 *         has been subscribed.
	 */
    public Boolean getAccTorque() {
        return (Boolean) parameters.get(Names.accTorque);
    }

	/**
	 * Sets a boolean value. If true, subscribes evInfo data
	 * 
	 * @param evInfo
	 *            a boolean value
	 */
    public void setEvInfo(Boolean evInfo) {
        if (evInfo != null) {
            parameters.put(Names.evInfo, evInfo);
        } else {
        	parameters.remove(Names.evInfo);
        }
    }

	/**
	 * Gets a boolean value. If true, means the evInfo data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the evInfo data
	 *         has been subscribed.
	 */
    public Boolean getEvInfo() {
        return (Boolean) parameters.get(Names.evInfo);
    }

	/**
	 * Sets a boolean value. If true, subscribes Ambient Light Status data
	 * 
	 * @param ambientLightStatus
	 *            a boolean value
	 */
    public void setAmbientLightStatus(Boolean ambientLightStatus) {
        if (ambientLightStatus != null) {
            parameters.put(Names.ambientLightStatus, ambientLightStatus);
        } else {
        	parameters.remove(Names.ambientLightStatus);
        }
    }

	/**
	 * Gets a boolean value. If true, means the Ambient Light Status data has been
	 * subscribed.
	 * 
	 * @return Boolean -a Boolean value. If true, means the Ambient Light Status data
	 *         has been subscribed.
	 */
    public Boolean getAmbientLightStatus() {
        return (Boolean) parameters.get(Names.ambientLightStatus);
    }
}
