//  SDLVehicleDataType.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h> 
#import <SmartDeviceLink/SDLEnum.h>   

@interface SDLVehicleDataType : SDLEnum {}

+(SDLVehicleDataType*) valueOf:(NSString*) value;
+(NSMutableArray*) values;                                 

+(SDLVehicleDataType*) VEHICLEDATA_GPS;
+(SDLVehicleDataType*) VEHICLEDATA_SPEED;
+(SDLVehicleDataType*) VEHICLEDATA_RPM;
+(SDLVehicleDataType*) VEHICLEDATA_FUELLEVEL;
+(SDLVehicleDataType*) VEHICLEDATA_FUELLEVEL_STATE;
+(SDLVehicleDataType*) VEHICLEDATA_FUELCONSUMPTION;
+(SDLVehicleDataType*) VEHICLEDATA_EXTERNTEMP;
+(SDLVehicleDataType*) VEHICLEDATA_VIN;
+(SDLVehicleDataType*) VEHICLEDATA_PRNDL;
+(SDLVehicleDataType*) VEHICLEDATA_TIREPRESSURE;
+(SDLVehicleDataType*) VEHICLEDATA_ODOMETER;
+(SDLVehicleDataType*) VEHICLEDATA_BELTSTATUS;
+(SDLVehicleDataType*) VEHICLEDATA_BODYINFO;
+(SDLVehicleDataType*) VEHICLEDATA_DEVICESTATUS;
+(SDLVehicleDataType*) VEHICLEDATA_BRAKING;
+(SDLVehicleDataType*) VEHICLEDATA_WIPERSTATUS;
+(SDLVehicleDataType*) VEHICLEDATA_HEADLAMPSTATUS;
+(SDLVehicleDataType*) VEHICLEDATA_BATTVOLTAGE;
+(SDLVehicleDataType*) VEHICLEDATA_ENGINETORQUE;
+(SDLVehicleDataType*) VEHICLEDATA_ACCPEDAL;
+(SDLVehicleDataType*) VEHICLEDATA_STEERINGWHEEL;

@end


