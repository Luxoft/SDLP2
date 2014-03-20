//  SDLRPCMessage.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLEnum.h>

@interface SDLRPCStruct : NSObject {
	NSMutableDictionary* store;
}

-(id) initWithDictionary:(NSMutableDictionary*) dict;
-(id) init;

-(NSMutableDictionary*) serializeAsDictionary:(Byte) version;

@end

@interface SDLRPCMessage : SDLRPCStruct {
	NSMutableDictionary* function;
	NSMutableDictionary* parameters;
	NSString* messageType;
	NSData* _bulkData;
}

-(id) initWithName:(NSString*) name;
-(id) initWithDictionary:(NSMutableDictionary*) dict;
-(NSString*) getFunctionName;
-(void) setFunctionName:(NSString*) functionName;
-(NSObject*) getParameters:(NSString*) functionName;
-(void) setParameters:(NSString*) functionName value:(NSObject*) value;
-(NSData*) getBulkData;
-(void) setBulkData:(NSData*) bulkData;

@property(readonly) NSString* name;
@property(readonly) NSString* messageType;

@end