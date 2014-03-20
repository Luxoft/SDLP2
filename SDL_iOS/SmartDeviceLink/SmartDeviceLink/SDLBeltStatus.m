//  SDLBeltStatus.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLBeltStatus.h>

#import <SmartDeviceLink/SDLNames.h>

@implementation SDLBeltStatus

-(id) init {
    if (self = [super init]) {}
    return self;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict {
    if (self = [super initWithDictionary:dict]) {}
    return self;
}

-(void) setDriverBeltDeployed:(SDLBeltStatus*) driverBeltDeployed {
    if (driverBeltDeployed != nil) {
        [store setObject:driverBeltDeployed forKey:NAMES_driverBeltDeployed];
    } else {
        [store removeObjectForKey:NAMES_driverBeltDeployed];
    }
}

-(NSNumber*) driverBeltDeployed {
    return [store objectForKey:NAMES_driverBeltDeployed];
}

-(void) setPassengerBeltDeployed:(SDLBeltStatus*) passengerBeltDeployed {
    if (passengerBeltDeployed != nil) {
        [store setObject:passengerBeltDeployed forKey:NAMES_passengerBeltDeployed];
    } else {
        [store removeObjectForKey:NAMES_passengerBeltDeployed];
    }
}

-(NSNumber*) passengerBeltDeployed {
    return [store objectForKey:NAMES_passengerBeltDeployed];
}

-(void) setPassengerBuckleBelted:(SDLBeltStatus*) passengerBuckleBelted {
    if (passengerBuckleBelted != nil) {
        [store setObject:passengerBuckleBelted forKey:NAMES_passengerBuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_passengerBuckleBelted];
    }
}

-(NSNumber*) passengerBuckleBelted {
    return [store objectForKey:NAMES_passengerBuckleBelted];
}

-(void) setDriverBuckleBelted:(SDLBeltStatus*) driverBuckleBelted {
    if (driverBuckleBelted != nil) {
        [store setObject:driverBuckleBelted forKey:NAMES_driverBuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_driverBuckleBelted];
    }
}

-(NSNumber*) driverBuckleBelted {
    return [store objectForKey:NAMES_driverBuckleBelted];
}

-(void) setLeftRow2BuckleBelted:(SDLBeltStatus*) leftRow2BuckleBelted {
    if (leftRow2BuckleBelted != nil) {
        [store setObject:leftRow2BuckleBelted forKey:NAMES_leftRow2BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_leftRow2BuckleBelted];
    }
}

-(NSNumber*) leftRow2BuckleBelted {
    return [store objectForKey:NAMES_leftRow2BuckleBelted];
}

-(void) setPassengerChildDetected:(SDLBeltStatus*) passengerChildDetected {
    if (passengerChildDetected != nil) {
        [store setObject:passengerChildDetected forKey:NAMES_passengerChildDetected];
    } else {
        [store removeObjectForKey:NAMES_passengerChildDetected];
    }
}

-(NSNumber*) passengerChildDetected {
    return [store objectForKey:NAMES_passengerChildDetected];
}

-(void) setRightRow2BuckleBelted:(SDLBeltStatus*) rightRow2BuckleBelted {
    if (rightRow2BuckleBelted != nil) {
        [store setObject:rightRow2BuckleBelted forKey:NAMES_rightRow2BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_rightRow2BuckleBelted];
    }
}

-(NSNumber*) rightRow2BuckleBelted {
    return [store objectForKey:NAMES_rightRow2BuckleBelted];
}

-(void) setMiddleRow2BuckleBelted:(SDLBeltStatus*) middleRow2BuckleBelted {
    if (middleRow2BuckleBelted != nil) {
        [store setObject:middleRow2BuckleBelted forKey:NAMES_middleRow2BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_middleRow2BuckleBelted];
    }
}

-(NSNumber*) middleRow2BuckleBelted {
    return [store objectForKey:NAMES_middleRow2BuckleBelted];
}

-(void) setMiddleRow3BuckleBelted:(SDLBeltStatus*) middleRow3BuckleBelted {
    if (middleRow3BuckleBelted != nil) {
        [store setObject:middleRow3BuckleBelted forKey:NAMES_middleRow3BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_middleRow3BuckleBelted];
    }
}

-(NSNumber*) middleRow3BuckleBelted {
    return [store objectForKey:NAMES_middleRow3BuckleBelted];
}

-(void) setLeftRow3BuckledBelted:(SDLBeltStatus*) leftRow3BuckledBelted {
    if (leftRow3BuckledBelted != nil) {
        [store setObject:leftRow3BuckledBelted forKey:NAMES_leftRow3BuckledBelted];
    } else {
        [store removeObjectForKey:NAMES_leftRow3BuckledBelted];
    }
}

-(NSNumber*) leftRow3BuckledBelted {
    return [store objectForKey:NAMES_leftRow3BuckledBelted];
}

-(void) setRightRow3BuckleBelted:(SDLBeltStatus*) rightRow3BuckleBelted {
    if (rightRow3BuckleBelted != nil) {
        [store setObject:rightRow3BuckleBelted forKey:NAMES_rightRow3BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_rightRow3BuckleBelted];
    }
}

-(NSNumber*) rightRow3BuckleBelted {
    return [store objectForKey:NAMES_rightRow3BuckleBelted];
}

-(void) setLeftRearInflatableBelted:(SDLBeltStatus*) leftRearInflatableBelted {
    if (leftRearInflatableBelted != nil) {
        [store setObject:leftRearInflatableBelted forKey:NAMES_leftRearInflatableBelted];
    } else {
        [store removeObjectForKey:NAMES_leftRearInflatableBelted];
    }
}

-(NSNumber*) leftRearInflatableBelted {
    return [store objectForKey:NAMES_leftRearInflatableBelted];
}

-(void) setRightRearInflatableBelted:(SDLBeltStatus*) rightRearInflatableBelted {
    if (rightRearInflatableBelted != nil) {
        [store setObject:rightRearInflatableBelted forKey:NAMES_rightRearInflatableBelted];
    } else {
        [store removeObjectForKey:NAMES_rightRearInflatableBelted];
    }
}

-(NSNumber*) rightRearInflatableBelted {
    return [store objectForKey:NAMES_rightRearInflatableBelted];
}

-(void) setMiddleRow1BeltDeployed:(SDLBeltStatus*) middleRow1BeltDeployed {
    if (middleRow1BeltDeployed != nil) {
        [store setObject:middleRow1BeltDeployed forKey:NAMES_middleRow1BeltDeployed];
    } else {
        [store removeObjectForKey:NAMES_middleRow1BeltDeployed];
    }
}

-(NSNumber*) middleRow1BeltDeployed {
    return [store objectForKey:NAMES_middleRow1BeltDeployed];
}

-(void) setMiddleRow1BuckleBelted:(SDLBeltStatus*) middleRow1BuckleBelted {
    if (middleRow1BuckleBelted != nil) {
        [store setObject:middleRow1BuckleBelted forKey:NAMES_middleRow1BuckleBelted];
    } else {
        [store removeObjectForKey:NAMES_middleRow1BuckleBelted];
    }
}

-(NSNumber*) middleRow1BuckleBelted {
    return [store objectForKey:NAMES_middleRow1BuckleBelted];
}

@end
