//  SDLExternalLibrary.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

@protocol SDLExternalLibrary <NSObject>

@required
- (NSString*)getLibraryName;
- (NSString*)getVersion;

@end
