//  SDLBinaryFrameHeader.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLBinaryFrameHeader.h>

#import <SmartDeviceLink/SDLBitConverter.h>
#import <SmartDeviceLink/SDLDebugTool.h>

@implementation SDLBinaryFrameHeader

@synthesize _rpcType;
@synthesize _functionID;
@synthesize _correlationID;
@synthesize _jsonSize;
@synthesize _jsonData;
@synthesize _bulkData;

-(id) init {
	if (self = [super init]) {
        _rpcType = 0;
        _functionID = 0;
        _correlationID = 0;
        _jsonSize = 0;
        _jsonData = nil;
        _bulkData = nil;
	}
	return self;
}

+(SDLBinaryFrameHeader*) parseBinaryHeader:(NSData*) binHeader {
	SDLBinaryFrameHeader* msg = [[[SDLBinaryFrameHeader alloc] init] autorelease];

	Byte rpcType = (Byte)(((Byte*)binHeader.bytes)[0] >> 4);
    msg._rpcType = rpcType;
    
	UInt32 functionID = [SDLBitConverter intFromByteArray:((Byte*)binHeader.bytes) offset:0] & 0x0FFFFFFF;
    msg._functionID = functionID;
    
	UInt32 correlationID = [SDLBitConverter intFromByteArray:((Byte*)binHeader.bytes) offset:4];
    msg._correlationID = correlationID;
    
	UInt32 jsonSize = [SDLBitConverter intFromByteArray:((Byte*)binHeader.bytes) offset:8];
    msg._jsonSize = jsonSize;
    
    if (msg._jsonSize > 0) {
        msg._jsonData = [binHeader subdataWithRange:NSMakeRange(12, msg._jsonSize)];
    }
    
    if (binHeader.length - msg._jsonSize - 12 > 0) {
        msg._bulkData = [binHeader subdataWithRange: NSMakeRange(12 + msg._jsonSize, binHeader.length - 12 - msg._jsonSize)];
    }
	
	return msg;
}

-(NSData*) assembleHeaderBytes {
	UInt32 binHeader = 0;
    binHeader |= _rpcType;
	binHeader <<= 28;
	binHeader |= _functionID;
	
	Byte* mallocPtr = malloc(12);
	if (mallocPtr == nil) {
		@throw [NSException exceptionWithName:@"OutOfMemoryException" reason:@"malloc failed" userInfo:nil];
	}
	NSData* ret = [[NSData alloc] initWithBytesNoCopy:mallocPtr length:12];
    
	memcpy((void*)ret.bytes, [SDLBitConverter intToByteArray:binHeader].bytes, 4);
	memcpy((void*)ret.bytes + 4, [SDLBitConverter intToByteArray:_correlationID].bytes, 4);
	memcpy((void*)ret.bytes + 8, [SDLBitConverter intToByteArray:_jsonSize].bytes, 4);
    
	return [ret autorelease];
}

-(void) dealloc {
    [_jsonData release];
    [_bulkData release];
    [super dealloc];
}

@end
