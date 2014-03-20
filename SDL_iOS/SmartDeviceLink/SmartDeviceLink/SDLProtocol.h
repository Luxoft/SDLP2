//  SDLProtocol.h
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <SmartDeviceLink/SDLAbstractProtocol.h>

@interface SDLProtocol : SDLAbstractProtocol {
    int headerSize;
    Byte _version;
	BOOL haveHeader;
	NSMutableData* headerBuf;
	NSMutableData* dataBuf;
	SDLProtocolFrameHeader* currentHeader;
	NSMutableDictionary *frameAssemblerForSessionID;
	NSInteger dataBufFinalLength;
	NSObject *msgLock;
    UInt32 _messageID;
}

-(void) setVersion:(Byte) version;
-(void) resetHeaderAndData;

@end

@interface FrameAssembler : NSObject {
    Byte _version;
	BOOL hasFirstFrame;
	BOOL hasSecondFrame;
	NSMutableData *accumulator;
	NSInteger totalSize;
	NSInteger framesRemaining;
	NSArray* listeners;
    UInt32 _hashID;
}
	
-(id) initWithListeners:(NSArray*)listeners;
-(void) handleFirstFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data;
-(void) handleSecondFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data;
-(void) handleRemainingFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data;
-(void) notifyIfFinished:(SDLProtocolFrameHeader*) header;
-(void) handleMultiFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data;
-(void) handleFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data;	
	
@end

@interface BulkAssembler: FrameAssembler {
	
	NSInteger bulkCorrId;
}

@end