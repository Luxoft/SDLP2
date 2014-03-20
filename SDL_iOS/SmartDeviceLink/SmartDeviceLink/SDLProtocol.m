//  SDLProtocol.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <SmartDeviceLink/SDLProtocol.h>

#import <UIKit/UIKit.h>
#import <SmartDeviceLink/SDLBitConverter.h>
#import <SmartDeviceLink/SDLDebugTool.h>
#import <SmartDeviceLink/SDLProtocolFrameHeaderFactory.h>
#import <SmartDeviceLink/SDLProtocolFrameHeader.h>
#import <SmartDeviceLink/SDLBinaryFrameHeader.h>

#define HEADER_BUF_LENGTH 8
#define PROT2_HEADER_BUF_LENGTH 12
#define MTU_SIZE 512

@implementation SDLProtocol

-(id) init {
	if (self = [super init]) {
		msgLock = [[NSObject alloc] init];
		headerBuf = nil;
		dataBuf = nil;
		currentHeader = nil;
		[self resetHeaderAndData];
		frameAssemblerForSessionID = [[NSMutableDictionary alloc] initWithCapacity:2];
        
        _version = 1;
        _messageID = 0;
	}
	return self;
}

-(void) resetHeaderAndData {
    [headerBuf release];

	haveHeader = NO;
    if (_version == 1) {
        headerSize = HEADER_BUF_LENGTH * sizeof(Byte);
    } else {
        headerSize = PROT2_HEADER_BUF_LENGTH * sizeof(Byte);
    }
	headerBuf = [[NSMutableData alloc] initWithCapacity:headerSize];

    [dataBuf release];
    dataBuf = nil;

    [currentHeader release];
    currentHeader = nil;
}

- (void) setVersion:(Byte) version {
    _version = version;
    
    [headerBuf release];
    headerBuf = nil;
    
    if (version == 2) {
        headerSize = PROT2_HEADER_BUF_LENGTH * sizeof(Byte);
        headerBuf = [[NSMutableData alloc] initWithCapacity:headerSize];
    }
}

- (void) doAlertMessage:(NSString*) message withTitle:(NSString*) title{
	UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:title message:message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
	[alertView show];
	[alertView release];
	
}

-(FrameAssembler*) getFrameAssemblerForFrameHeader:(SDLProtocolFrameHeader*) header {
	id sessionIDKey = [NSNumber numberWithInt:header._sessionID];
    
	FrameAssembler *ret = [frameAssemblerForSessionID objectForKey:sessionIDKey];

	if (ret == nil) {
		if (header._sessionType == SDLSessionType_RPC) {
			ret = [[FrameAssembler alloc] initWithListeners:protocolListeners];
		} else if (header._sessionType == SDLSessionType_BulkData) {
			ret = [[BulkAssembler alloc] initWithListeners:protocolListeners];
		} else {
            return nil;
        }
		[frameAssemblerForSessionID setObject:ret forKey:sessionIDKey];
        return [ret autorelease];
	}
	return ret;
}

-(void) handleBytesFromTransport:(Byte *)receivedBytes length:(long)receivedBytesLength {
	long receivedBytesReadPos = 0;
    
    //Check for a version difference
    if (_version == 1) {
        //Nothing has been read into the buffer and version is 2
        if (headerBuf.length == 0 && (receivedBytes[0] >> 4) == 2) {
            [self setVersion:(Byte) receivedBytes[0] >> 4];
			//Buffer has something in it and version is 2
        } else if ((((Byte *)headerBuf.bytes)[0] >> 4) == 2) {
            //safe current state of the buffer and also set the new version
            NSMutableData* tempHeader = nil;
            tempHeader = [[NSMutableData alloc] initWithCapacity:headerBuf.length];
            tempHeader = headerBuf;
            [self setVersion:(Byte) ((Byte *)headerBuf.bytes)[0] >> 4];
            headerBuf = tempHeader;
        }
    }
    
	// If I don't yet know the message size, grab those bytes.
	if (!haveHeader) {
		// If I can't get the size, just get the bytes that are there.
        int sizeBytesNeeded;
        if (_version == 1) {
            sizeBytesNeeded = HEADER_BUF_LENGTH - headerBuf.length;
        } else {
            sizeBytesNeeded = PROT2_HEADER_BUF_LENGTH - headerBuf.length;
        }
        
		if (receivedBytesLength < sizeBytesNeeded) {
			[headerBuf appendBytes:receivedBytes + receivedBytesReadPos length:receivedBytesLength];
			return;
		} else {
            // If I got the size, allocate the buffer
			[headerBuf appendBytes:receivedBytes + receivedBytesReadPos length:sizeBytesNeeded];
			receivedBytesReadPos += sizeBytesNeeded;
			haveHeader = true;
			dataBufFinalLength = [SDLBitConverter intFromByteArray:(Byte*)headerBuf.bytes offset:4];
            
            [dataBuf release];
            dataBuf = nil;

			dataBuf = [[NSMutableData alloc] initWithCapacity:dataBufFinalLength];
			currentHeader = [[SDLProtocolFrameHeaderFactory parseHeader:headerBuf] retain];
		}
		
	}
	
	int bytesLeft = receivedBytesLength - receivedBytesReadPos;
	int bytesNeeded = dataBufFinalLength - dataBuf.length;
	// If I don't have enough bytes for the message, just grab what's there.
	if (bytesLeft < bytesNeeded) {
		[dataBuf appendBytes:receivedBytes + receivedBytesReadPos length:bytesLeft];
		return;
	} else {
        // Fill the buffer and call the handler!
		[dataBuf appendBytes:receivedBytes + receivedBytesReadPos length:bytesNeeded];
		receivedBytesReadPos += bytesNeeded;
		FrameAssembler *assembler = [self getFrameAssemblerForFrameHeader:currentHeader];
		[assembler handleFrame:currentHeader data:dataBuf];
		[self resetHeaderAndData];
		
		//If there are any bytes left, recurse.
		int moreBytesLeft = receivedBytesLength - receivedBytesReadPos;
		if (moreBytesLeft > 0) {
			[self handleBytesFromTransport:receivedBytes + receivedBytesReadPos  length:moreBytesLeft];
		}
	}
}

-(NSData*) assembleHeaderBytes:(SDLProtocolFrameHeader*) msg {
	UInt32 header = 0;
	header |= msg._version;
	header <<= 1;
	header |= (msg._compressed ? 1 : 0);
	header <<= 3;
	header |= msg._frameType;
	header <<= 8;
	header |= msg._sessionType;
	header <<= 8;
	header |= msg._frameData;
	header <<= 8;
	header |= msg._sessionID;

    NSMutableData * ret = [[NSMutableData alloc] init];
    [ret appendData:[SDLBitConverter intToByteArray:header]];
    [ret appendData:[SDLBitConverter intToByteArray:msg._dataSize]];
    if (_version == 2)
    {
        [ret appendData:[SDLBitConverter intToByteArray:msg._messageID]];
    }
	
	return [ret autorelease];
}

-(void) sendFrameToTransport:(SDLProtocolFrameHeader *)header withData:(NSData*) data {
	if (data == nil || data.length == 0){
		[transport sendBytes:[self assembleHeaderBytes:header]];
	} else {
        NSMutableData* toSend;
        if (_version == 1) {
            toSend = [NSMutableData dataWithCapacity:data.length + HEADER_BUF_LENGTH];
        } else {
            toSend = [NSMutableData dataWithCapacity:data.length + PROT2_HEADER_BUF_LENGTH];
        }
		
		[toSend appendData:[self assembleHeaderBytes:header]];
		
		[toSend appendData:data];
		
		[transport sendBytes:toSend];
	}
	
}

-(void) sendFrameToTransport:(SDLProtocolFrameHeader *)header {
	[self sendFrameToTransport:header withData:nil];
}

-(void) sendFrameToTransport:(SDLProtocolFrameHeader *)header withData:(NSData*) data offset:(NSInteger) offset length:(NSInteger) length{
	
    NSMutableData* toSend;
    if (_version == 1) {
        toSend = [NSMutableData dataWithCapacity:length + HEADER_BUF_LENGTH];
    } else {
        toSend = [NSMutableData dataWithCapacity:length + PROT2_HEADER_BUF_LENGTH];
    }
	
	[toSend appendData:[self assembleHeaderBytes:header]];
	
	[toSend appendBytes:data.bytes + offset length:length];
	[transport sendBytes:toSend];
	
}

-(void) sendData:(SDLProtocolMessage*) protocolMsg {
    protocolMsg._rpcType = (Byte) 0x00;
    SDLSessionType sessionType = protocolMsg._sessionType;
    Byte sessionID = protocolMsg._sessionID;
    
    NSMutableData* data;
    if (_version == 2) {
        data = [[NSMutableData alloc] init];
        if (protocolMsg._bulkData != nil) {
            sessionType = SDLSessionType_BulkData;
        }
        SDLBinaryFrameHeader* binFrameHeader = [[SDLBinaryFrameHeader alloc] init];
        binFrameHeader._rpcType = protocolMsg._rpcType;
        binFrameHeader._functionID = protocolMsg._functionID;
        binFrameHeader._correlationID = protocolMsg._correlationID;
        binFrameHeader._jsonSize = protocolMsg._jsonSize;
        [data appendData:[binFrameHeader assembleHeaderBytes]];
        [data appendData: protocolMsg._data];
        
        if (protocolMsg._bulkData != nil) {
            [data appendData:protocolMsg._bulkData];
        }
        [protocolMsg set_data:data];
        [data release];
        [binFrameHeader release];
    }
    
    int maxDataSize;
    if (_version == 1) {
        maxDataSize = MTU_SIZE - HEADER_BUF_LENGTH;
    } else {
        maxDataSize = MTU_SIZE - PROT2_HEADER_BUF_LENGTH;
    }
	
	@synchronized (msgLock) {
        if (protocolMsg._data.length < maxDataSize) {
            
            _messageID++;
            SDLProtocolFrameHeader *singleHeader = [SDLProtocolFrameHeaderFactory singleFrameWithSessionType:sessionType sessionID:sessionID dataSize:protocolMsg._data.length messageID:_messageID version:_version];
            
            [self sendFrameToTransport:singleHeader withData:protocolMsg._data];
        } else {
            _messageID++;
            SDLProtocolFrameHeader *firstHeader = [SDLProtocolFrameHeaderFactory firstFrameWithSessionType:sessionType sessionID:sessionID messageID:_messageID version:_version];
            
            // Assemble first frame.
            int frameCount = protocolMsg._data.length / maxDataSize;
            if (protocolMsg._data.length % maxDataSize > 0) {
                frameCount++;
            }
            NSMutableData *firstFrameData = [NSMutableData dataWithCapacity:8];
            // First four bytes are data size.
            [firstFrameData appendData:[SDLBitConverter intToByteArray:protocolMsg._data.length]];
            // Second four bytes are frame count.
            [firstFrameData appendData:[SDLBitConverter intToByteArray:frameCount]];
            
            [self sendFrameToTransport:firstHeader withData:firstFrameData];
            
            int currentOffset = 0;
            
            for (int i = 0; i < frameCount; i++) {
                int bytesToWrite = protocolMsg._data.length - currentOffset;
                if (bytesToWrite > maxDataSize) { bytesToWrite = maxDataSize
                    ; }
                SDLProtocolFrameHeader *consecHeader;
                if (i == frameCount - 1)
                {
                    consecHeader = [SDLProtocolFrameHeaderFactory lastFrameWithSessionType:sessionType
                                                                                 sessionID:sessionID
                                                                                  dataSize:bytesToWrite
                                                                                 messageID:_messageID
                                                                                   version:_version];
                }
                else{
                    consecHeader = [SDLProtocolFrameHeaderFactory consecutiveFrameWithSessionType:sessionType
                                                                                        sessionID:sessionID
                                                                                         dataSize:bytesToWrite
                                                                                        messageID:_messageID
                                                                                          version:_version];
                }
                [self sendFrameToTransport:consecHeader withData:protocolMsg._data offset:currentOffset length:bytesToWrite];
                currentOffset += bytesToWrite;
            }
        }
	}
}

-(void) sendStartSessionWithType:(SDLSessionType) sessionType {
    SDLProtocolFrameHeader *header = [SDLProtocolFrameHeaderFactory startSessionWithSessionType:sessionType messageID:0x00 version:_version];
	
	@synchronized (msgLock) {
		[self sendFrameToTransport:header];
	}
}

-(void) sendEndSessionWithType:(SDLSessionType)sessionType sessionID:(Byte)sessionID {
	SDLProtocolFrameHeader *header = [SDLProtocolFrameHeaderFactory endSessionWithSessionType:sessionType sessionID:sessionID messageID:0x00 version:_version];
	@synchronized (msgLock) {
		[self sendFrameToTransport:header];
	}
}


-(void) dealloc {

    [headerBuf release];
    headerBuf = nil;

    [dataBuf release];
    dataBuf = nil;

    [currentHeader release];
    currentHeader = nil;

    [frameAssemblerForSessionID release];
    frameAssemblerForSessionID = nil;

    [msgLock release];
    msgLock = nil;
	
	[super dealloc];
}

@end

@implementation FrameAssembler

-(id) initWithListeners:(NSArray*)theListeners{
	if (self = [super init]) {
		listeners = [theListeners retain];
	}
	return self;
}

-(void) handleFirstFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
	//The message is new, so let's figure out how big it is.
	hasFirstFrame = true;
	totalSize = [SDLBitConverter intFromByteArray:(Byte*)data.bytes offset:0] - 8;
	framesRemaining = [SDLBitConverter intFromByteArray:(Byte*)data.bytes offset:4];
    
    [accumulator release];
    accumulator = nil;
    
	accumulator = [[NSMutableData dataWithCapacity:totalSize] retain];
	
}

-(void) handleSecondFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
	[self handleRemainingFrame:header data:data];
}

-(void) handleRemainingFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
	[accumulator appendData:data];
	[self notifyIfFinished:header];
	
}

-(void) notifyIfFinished:(SDLProtocolFrameHeader*) header {
	if (framesRemaining == 0) {
		SDLProtocolMessage * message = [[SDLProtocolMessage alloc] init];
        if (header._sessionType == SDLSessionType_RPC) {
            message._messageType = SDLMessageType_RPC;
        } else if (header._sessionType == SDLSessionType_BulkData) {
            message._messageType = SDLMessageType_BULK;
        }
		message._sessionType = header._sessionType;
		message._sessionID = header._sessionID;
        
        if (_version == 2) {
            SDLBinaryFrameHeader* binFrameHeader = [SDLBinaryFrameHeader parseBinaryHeader:accumulator];
            message._version = _version;
            message._rpcType = binFrameHeader._rpcType;
            message._functionID = binFrameHeader._functionID;
            message._jsonSize = binFrameHeader._jsonSize;
            message._correlationID = binFrameHeader._correlationID;
            if (binFrameHeader._jsonSize > 0) {
                message._data = binFrameHeader._jsonData;
            }
            if (binFrameHeader._bulkData != nil) {
                message._bulkData = binFrameHeader._bulkData;
            }
        } else {
            message._data = accumulator;
        }
		
		NSArray* localListeners = nil;
		@synchronized (listeners) {
			localListeners = [listeners copy];
		}
			
		for (NSObject<SDLProtocolListener> *listener in localListeners) {
			[listener onProtocolMessageReceived:message];
		}
		[localListeners release];
        
        [message release];
		
		hasFirstFrame = false;
		hasSecondFrame = false;

        [accumulator release];
        accumulator = nil;

	}
}

-(void) handleMultiFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
	if (!hasFirstFrame) {
		hasFirstFrame = true;
		[self handleFirstFrame:header data:data];
	} else if (!hasSecondFrame) {
		hasSecondFrame = true;
		framesRemaining--;
		[self handleSecondFrame:header data:data ];
	} else {
		framesRemaining--;
		[self handleRemainingFrame:header data:data];
	}
	
}

-(void) handleFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
    if (header._version == 2) {
        _version = header._version;
    }
    
    if (header._frameType == SDLFrameType_Control) {
        if (header._frameData == SDLFrameData_StartSessionACK) {
            if (_version == 2) {
                _hashID = header._messageID;
            }
            NSArray* localListeners = nil;
            @synchronized (listeners) {
                localListeners = [listeners copy];
            }
            
            for (NSObject<SDLProtocolListener> *listener in localListeners) {
                [listener handleProtocolSessionStarted:header._sessionType sessionID:header._sessionID version:_version];
            }
            [localListeners release];
        }
    } else {
        if (header._frameType == SDLFrameType_First || header._frameType == SDLFrameType_Consecutive) {
            [self handleMultiFrame:header data:data];
        } else {
            SDLProtocolMessage * message = [[SDLProtocolMessage alloc] init];
            if (header._sessionType == SDLSessionType_RPC) {
                message._messageType = SDLMessageType_RPC;
            } else if (header._sessionType == SDLSessionType_BulkData) {
                message._messageType = SDLMessageType_BULK;
            } // end-if
            message._sessionType = header._sessionType;
            message._sessionID = header._sessionID;
            if (_version == 2) {
                SDLBinaryFrameHeader* binFrameHeader = [SDLBinaryFrameHeader parseBinaryHeader:data];
                message._version = _version;
                message._rpcType = binFrameHeader._rpcType;
                message._functionID = binFrameHeader._functionID;
                message._jsonSize = binFrameHeader._jsonSize;
                message._correlationID = binFrameHeader._correlationID;
                if (binFrameHeader._jsonSize > 0) {
                    message._data = binFrameHeader._jsonData;
                }
                if (binFrameHeader._bulkData != nil) {
                    message._bulkData = binFrameHeader._bulkData;
                }
            } else {
                message._data = data;
            }
            
            NSArray* localListeners = nil;
            @synchronized (listeners) {
                localListeners = [listeners copy];
            }
            
            for (NSObject<SDLProtocolListener> *listener in localListeners) {
                [listener onProtocolMessageReceived:message];
            }
            [localListeners release];
            
            [message release];
        }
    }
}

-(void) dealloc {

    [accumulator release];
    accumulator = nil;
    
    [listeners release];
    listeners = nil;
    
    [super dealloc];
}

@end

@implementation BulkAssembler

-(void) handleSecondFrame:(SDLProtocolFrameHeader*) header data:(NSData*) data {
	bulkCorrId = [SDLBitConverter intFromByteArray:(Byte*)data.bytes offset:4];
	[accumulator appendBytes:data.bytes + 8 length:header._dataSize - 8];
	[self notifyIfFinished:header];
}

@end


