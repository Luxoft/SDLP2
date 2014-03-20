package com.smartdevicelink.proxy.rpc.enums;

/**
 * Defines the vehicle data types that can be published and subscribed to
 * 
 */
public enum VehicleDataType {
    VEHICLEDATA_SPEED,
    VEHICLEDATA_RPM,
    VEHICLEDATA_FUELLEVEL,
    VEHICLEDATA_FUELLEVEL_STATE,
    VEHICLEDATA_FUELCONSUMPTION,
    VEHICLEDATA_EXTERNTEMP,
    VEHICLEDATA_VIN,
    VEHICLEDATA_PRNDL,
    VEHICLEDATA_TIREPRESSURE,
    VEHICLEDATA_ODOMETER,    
    VEHICLEDATA_BELTSTATUS,
    VEHICLEDATA_BODYINFO,
    VEHICLEDATA_DEVICESTATUS,
    VEHICLEDATA_BRAKING;

    public static VehicleDataType valueForString(String value) {
        return valueOf(value);
    }
}
