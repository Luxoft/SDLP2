//  SDLProxy.m
//  SmartDeviceLink
//  Copyright (c) 2013 Ford Motor Company

#import <Foundation/Foundation.h>
#import <ExternalAccessory/ExternalAccessory.h>
#import <objc/runtime.h>

#import <SmartDeviceLink/SDLDebugTool.h>
#import <SmartDeviceLink/SDLEncodedSyncPData.h>
#import <SmartDeviceLink/SDLFunctionID.h>
#import <SmartDeviceLink/SDLJsonDecoder.h>
#import <SmartDeviceLink/SDLJsonEncoder.h>
#import <SmartDeviceLink/SDLLanguage.h>
#import <SmartDeviceLink/SDLNames.h>
#import <SmartDeviceLink/SDLSiphonServer.h>
#import <SmartDeviceLink/SDLProxy.h>

#define VERSION_STRING @"SDL-2.0.0"

@interface SDLCallback : NSObject {
	NSObject* target;
	SEL selector;
	NSObject* parameter;
}

@property (nonatomic, retain) NSObject* target;
@property (nonatomic, assign) SEL selector;
@property (nonatomic, retain) NSObject* parameter;

@end

@implementation SDLCallback
@synthesize target;
@synthesize selector;
@synthesize parameter;

-(void) dealloc {
	[target release];
	[parameter release];
	
	[super dealloc];
}

@end

@interface SDLProxy ()

-(void) notifyProxyClosed;
-(void) handleProtocolMessage:(SDLProtocolMessage*) msgData;
-(void) performCallback:(SDLCallback*) callback;

@end

@implementation SDLProxy
{
    SDLFunctionID * _functionID;
}

const float handshakeTime = 30.0;
const float notifyProxyClosedDelay = 0.1;
bool httpPolicyUpdateInProgress = NO;

const int POLICIES_CORRELATION_ID = 65535;

- (void)handshakeTimerFired {
    [self destroyHandshakeTimer];
    [self performSelector:@selector(notifyProxyClosed) withObject:nil afterDelay:notifyProxyClosedDelay];
}

-(id) initWithTransport:(NSObject<SDLITransport>*) theTransport protocol:(NSObject<SDLIProtocol>*) theProtocol delegate:(NSObject<SDLProxyListener>*) theDelegate {
	if (self = [super init]) {
        _functionID = [[SDLFunctionID alloc] init];
        [[EAAccessoryManager sharedAccessoryManager] registerForLocalNotifications];
        
        proxyListeners = [[NSMutableArray alloc] initWithObjects:theDelegate, nil];
        protocol = [theProtocol retain];
        transport = [theTransport retain];
        rpcSessionID = 0;
        
        alreadyDestructed = NO;
        
        [transport addTransportListener:protocol];
        [protocol addProtocolListener:self];
        protocol.transport = transport;
        
        [transport connect];
        
        
        return self;
    }
    return self;
}

-(void)destroyHandshakeTimer {
    if (handshakeTimer != nil) {
        [handshakeTimer invalidate];
        [handshakeTimer release];
        handshakeTimer = nil;
    }
}

-(void) destructObjects {
    if(!alreadyDestructed) {
        [self destroyHandshakeTimer];
        
        [transport release];
        transport = nil;
        
        [protocol release];
        protocol = nil;
        
        [proxyListeners release];
        proxyListeners = nil;
        
        if (externalLibraries) {
            [externalLibraries release];
            externalLibraries = nil;
        }
        
        alreadyDestructed = YES;
    }
}

-(void) dispose {
    [self destructObjects];
}

-(void) dealloc {
    [self destructObjects];
	[super dealloc];
}

-(NSObject<SDLITransport>*)getTransport {
    return transport;
}

-(NSObject<SDLIProtocol>*)getProtocol {
    return protocol;
}

-(void) addDelegate:(NSObject<SDLProxyListener>*) delegate {
	@synchronized(proxyListeners) {
		[proxyListeners addObject:delegate];
	}
}

-(void) registerLibrary:(id <SDLExternalLibrary>) externalLibrary {
    if (externalLibrary) {
        if (!externalLibraries) {
            externalLibraries = [[NSMutableArray alloc] init];
        }
        [externalLibraries addObject:externalLibrary];
    }
}

-(NSString*) getProxyVersion {
    return VERSION_STRING;
}

-(void) sendRPCRequest:(SDLRPCMessage*) msg {
    if ([msg isKindOfClass:SDLRPCRequest.class]) {
        SDLRPCRequest *request = (SDLRPCRequest*)msg;
        [self sendRPCRequestPrivate:request];
    }
}

-(void) sendRPCRequestPrivate:(SDLRPCRequest*) msg {
	@try {
        SDLProtocolMessage* pm = [[SDLProtocolMessage alloc] init];
        pm._version = _version;
        pm._data = [[SDLJsonEncoder instance] encodeDictionary:[msg serializeAsDictionary:_version]];
        pm._jsonSize = pm._data.length;
        pm._sessionID = rpcSessionID;
        pm._messageType = SDLMessageType_RPC;
        pm._sessionType = SDLSessionType_RPC;
        pm._functionID = [[_functionID getFunctionID:[msg getFunctionName]] intValue];
        NSLog(@"Sending: %@", [msg getFunctionName]);
        pm._correlationID = [msg.correlationID intValue];
        
        if ([msg getBulkData] != nil) {
            pm._bulkData = [msg getBulkData];
        }
        
		[protocol sendData:pm];
        [pm release];
	} @catch (NSException * e) {
		[SDLDebugTool logException:e withMessage:@"Proxy: Failed to send RPC request: %@", msg.name];
	} @finally {}
}

-(void) handleProtocolSessionStarted:(SDLSessionType) sessionType sessionID:(Byte) sessionID version:(Byte) version {

    if (_version <= 1) {
        if (version == 2) {
            _version = version;
        }
    }
    
    if (sessionType == SDLSessionType_RPC || _version == 2) {
        rpcSessionID = sessionID;
        
        NSArray *localListeners = nil;
        @synchronized (proxyListeners) {
            localListeners = [proxyListeners copy];
        }
        
        for (NSObject<SDLProxyListener> *listener in localListeners) {
            [listener onProxyOpened];
        }
        
        [localListeners release];
    } else {
        // Handle other protocol session types here
    }
}
	 
- (void) onProtocolMessageReceived:(SDLProtocolMessage*) msgData {
	@try {
		[self handleProtocolMessage:msgData];
	}
	@catch (NSException * e) {
		[SDLDebugTool logException:e withMessage:@"Proxy: Failed to handle protocol message"];
	}
	@finally {
		
	}
}

-(void) handleProtocolMessage:(SDLProtocolMessage*) msgData {
    if (msgData._sessionType == SDLSessionType_RPC) {
        if (_version == 1) {
            if (msgData._version == 2) _version = msgData._version;
        }
        
        NSMutableDictionary* msg;
        if (_version == 2) {
            msg = [[[NSMutableDictionary alloc] init] autorelease];
            NSMutableDictionary* msgTemp = [[NSMutableDictionary alloc] init];
            if (msgData._correlationID != 0) {
                [msgTemp setObject:[NSNumber numberWithInt:msgData._correlationID] forKey:NAMES_correlationID];
            }
            if (msgData._jsonSize > 0) {
                NSDictionary* mMsg = [[SDLJsonDecoder instance] decode:msgData._data];
                [msgTemp setObject:mMsg forKey:NAMES_parameters];
            }
            [msgTemp setObject:[_functionID getFunctionName:msgData._functionID] forKey:NAMES_operation_name];
            if (msgData._rpcType == 0x00) {
                [msg setObject:msgTemp forKey:NAMES_request];
            } else if (msgData._rpcType == 0x01) {
                [msg setObject:msgTemp forKey:NAMES_response];
            } else if (msgData._rpcType == 0x02) {
                [msg setObject:msgTemp forKey:NAMES_notification];
            }
            if (msgData._bulkData != nil) {
                [msg setObject:msgData._bulkData forKey:NAMES_bulkData];
            }
            [msgTemp release];
            
        } else {
            msg = (NSMutableDictionary*) [[SDLJsonDecoder instance] decode:msgData._data];
        }
		[self handleRpcMessage:msg];        
    } else if (msgData._sessionType == SDLSessionType_BulkData) {
        bulkSessionID = msgData._sessionID;
    }
}

-(void) neverCalled {
	[[SDLAddCommandResponse alloc] release];
	[[SDLAddSubMenuResponse alloc] release];
	[[SDLAlertResponse alloc] release];
	[[SDLCreateInteractionChoiceSetResponse alloc] release];
	[[SDLDeleteCommandResponse alloc] release];
	[[SDLDeleteInteractionChoiceSetResponse alloc] release];
	[[SDLDeleteSubMenuResponse alloc] release];
	[[SDLOnHMIStatus alloc] release];
	[[SDLOnButtonEvent alloc] release];
	[[SDLOnButtonPress alloc] release];
	[[SDLOnCommand alloc] release];
	[[SDLOnAppInterfaceUnregistered alloc] release];
	[[SDLPerformInteractionResponse alloc] release];
	[[SDLRegisterAppInterfaceResponse alloc] release];
	[[SDLSetGlobalPropertiesResponse alloc] release];
	[[SDLResetGlobalPropertiesResponse alloc] release];
	[[SDLSetMediaClockTimerResponse alloc] release];
	[[SDLShowResponse alloc] release];
	[[SDLSpeakResponse alloc] release];
	[[SDLSubscribeButtonResponse alloc] release];
	[[SDLUnregisterAppInterfaceResponse alloc] release];
	[[SDLUnsubscribeButtonResponse alloc] release];
    [[SDLGenericResponse alloc] release];
    [[SDLOnDriverDistraction alloc] release];
    [[SDLOnTBTClientState alloc] release];
    [[SDLSubscribeVehicleDataResponse alloc] release];
    [[SDLUnsubscribeVehicleDataResponse alloc] release];
    [[SDLGetVehicleDataResponse alloc] release];
    [[SDLGetDTCsResponse alloc] release];
    [[SDLReadDIDResponse alloc] release];
    [[SDLOnVehicleData alloc] release];
    [[SDLOnPermissionsChange alloc] release];
    [[SDLSliderResponse alloc] release];
    [[SDLPutFileResponse alloc] release];
    [[SDLDeleteFileResponse alloc] release];
    [[SDLListFilesResponse alloc] release];
    [[SDLSetAppIconResponse alloc] release];
    [[SDLPerformAudioPassThruResponse alloc] release];
    [[SDLEndAudioPassThruResponse alloc] release];
    [[SDLOnAudioPassThru alloc] release];
    [[SDLScrollableMessageResponse alloc] release];
    [[SDLShowConstantTBTResponse alloc] release];
    [[SDLAlertManeuverResponse alloc] release];
    [[SDLChangeRegistrationResponse alloc] release];
    [[SDLOnLanguageChange alloc] release];
    [[SDLSetDisplayLayout alloc] release];
}

-(void) handleRpcMessage:(NSDictionary*) msg {
	
    SDLRPCMessage* rpcMsg = [[SDLRPCMessage alloc] initWithDictionary:(NSMutableDictionary*) msg];
    NSString* functionName = [rpcMsg getFunctionName];
    NSString* messageType = [rpcMsg messageType];

	if ([functionName isEqualToString:NAMES_OnAppInterfaceUnregistered]
        || [functionName isEqualToString:NAMES_UnregisterAppInterface]) {
		[self notifyProxyClosed];
        [rpcMsg release];
		return;
	}
    
	if ([messageType isEqualToString:NAMES_response]) {
		bool notGenericResponseMessage = ![functionName isEqualToString:@"GenericResponse"];
		if(notGenericResponseMessage) functionName = [NSString stringWithFormat:@"%@Response", functionName];
	}
    
    
    if ([functionName isEqualToString:@"RegisterAppInterfaceResponse"]) {
        // It's possible to run into a scenario in which SYNC fails to send a StartSessionACK. This issue will be caught by the timer that's waiting for a RegisterAppInterfaceResponse. If no RAIResponse is received, a call to onProxyClosed will occur.
        
        // Turn off the timer, the handshake has succeeded
        [self destroyHandshakeTimer];
        
        //Print Proxy Version To Console
        [SDLDebugTool logInfo:@"Framework Version: %@", [self getProxyVersion]];
        
        //Print external library versions to Console
        if (externalLibraries) {
            for (id <SDLExternalLibrary> library in externalLibraries) {
                [SDLDebugTool logInfo:@"%@ Version: %@", [library getLibraryName], [library getVersion]];
            }
        }
    }
   
    
    // Intercept OnEncodedSyncPData. If URL != nil, perform HTTP Post and don't pass the notification to the ProxyListeners
    if ([functionName isEqualToString:@"OnEncodedSyncPData"]) {
        NSString *urlString = (NSString *)[rpcMsg getParameters:@"URL"];
        if (urlString != nil) {
            httpPolicyUpdateInProgress = YES;
            NSDictionary *encodedSyncPData = (NSDictionary *)[rpcMsg getParameters:@"data"];
            NSNumber *encodedSyncPTimeout = (NSNumber *) [rpcMsg getParameters:@"Timeout"];
            [self sendEncodedSyncPData:encodedSyncPData toURL:urlString withTimeout:encodedSyncPTimeout];
            [rpcMsg release];
            return;
        }
    }
    
	NSString* functionClassName = [NSString stringWithFormat:@"SDL%@", functionName];
	Class functionClass = objc_getClass([functionClassName cStringUsingEncoding:NSUTF8StringEncoding]);
    
    // functionObject doesn't leak because performSelector returns a pointer to the same instance that class_createInstance() creates
	NSObject* functionObject = (id)class_createInstance(functionClass, 0);
    NSObject* rpcCallbackObject = [functionObject performSelector:@selector(initWithDictionary:) withObject:msg];
	
	NSString* handlerName = [NSString stringWithFormat:@"on%@:", functionName];

	SEL handlerSelector = NSSelectorFromString(handlerName);
	
	NSArray *localListeners = nil;
	@synchronized (proxyListeners) {
		localListeners = [proxyListeners copy];
	}
	
	for (NSObject<SDLProxyListener> *listener in localListeners) {
		if ([listener respondsToSelector:handlerSelector]) {
			SDLCallback* callback = [[SDLCallback alloc] init];
			callback.target = listener;
			callback.selector = handlerSelector;
			callback.parameter = rpcCallbackObject;
            [rpcCallbackObject retain];
			[self performSelectorOnMainThread:@selector(performCallback:) withObject:callback waitUntilDone:NO];
			// [callback release]; Moved to performCallback to avoid thread race condition
		} else {
			[SDLDebugTool logInfo:@"Proxy listener doesn't respond to selector: %@", handlerName];
		}
	}
    [rpcCallbackObject release];
	[localListeners release];
    [rpcMsg release];
}

-(void) performCallback:(SDLCallback*) callback {
	@try {
		[callback.target performSelector:callback.selector withObject:callback.parameter];
        [callback.parameter release];
	} @catch (NSException * e) {
		[SDLDebugTool logException:e withMessage:@"Exception thrown during call to %@ with object %@", callback.target, callback.parameter];
	} @finally {
		[callback release];
	}
}

-(void) onProtocolClosed {
	[self notifyProxyClosed];
}

-(void) notifyProxyClosed {
	if (isConnected) {
		isConnected = NO;
		NSArray *localListeners = nil;
		@synchronized (proxyListeners) {
			localListeners = [proxyListeners copy];
		}
		
		for (NSObject<SDLProxyListener> *listener in localListeners) {
			[listener performSelectorOnMainThread:@selector(onProxyClosed) withObject:nil waitUntilDone:NO];
		}
		[localListeners release];
	}
}

-(void) onError:(NSString*) info exception:(NSException*) e {
	
	NSArray *localListeners = nil;
	@synchronized (proxyListeners) {
		localListeners = [proxyListeners copy];
	}
	
	for (NSObject<SDLProxyListener> *listener in localListeners) {
		[listener performSelectorOnMainThread:@selector(onError:) withObject:e waitUntilDone:NO];
	}
	[localListeners release];
}


- (void) onProtocolOpened {    
    isConnected = YES;
    [protocol sendStartSessionWithType:SDLSessionType_RPC];
    
    [self destroyHandshakeTimer];
    handshakeTimer = [NSTimer scheduledTimerWithTimeInterval:handshakeTime target:self selector:@selector(handshakeTimerFired) userInfo:nil repeats:NO];
    [handshakeTimer retain];
}

+(void)enableSiphonDebug {
    [SDLSiphonServer enableSiphonDebug];
}

+(void)disableSiphonDebug {
    [SDLSiphonServer disableSiphonDebug];
}

-(void)sendEncodedSyncPData:(NSDictionary*)encodedSyncPData toURL:(NSString*)urlString withTimeout:(NSNumber*) timeout{
    
    NSString *encodedSyncPDataString = [[NSString stringWithFormat:@"%@", encodedSyncPData] componentsSeparatedByString:@"\""][1];
    NSString* jsonString = [NSString stringWithFormat:@"{\"data\":[\"%@\"]}", encodedSyncPDataString];
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    NSURL *url = [NSURL URLWithString:urlString];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    
    request.HTTPMethod = @"POST";
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    request.HTTPBody = jsonData;
    request.timeoutInterval = [timeout doubleValue];
    
    NSURLConnection *conn = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    
    if (conn == nil) {
        [SDLDebugTool logInfo:@"%s: Error creating NSURLConnection", __PRETTY_FUNCTION__];
    }
}

#pragma mark - NSURL Methods
- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    // Can be called multiple times, such as a server redirect, so reset the data each time
    httpResponseData = [[NSMutableData alloc] init];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [httpResponseData appendData:data];
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection
                  willCacheResponse:(NSCachedURLResponse*)cachedResponse {
    // Return nil to indicate not necessary to store a cached response for this connection
    return nil;
}

-(void)connectionDidFinishLoading:(NSURLConnection *)connection {
    
    [connection release]; // Release the connection we allocated in sendEncodedSyncPData
    
    if ([httpResponseData length] == 0){
        
        httpPolicyUpdateInProgress = NO;
        return;
    }

    NSString *responseString = [[[NSString alloc] initWithData:httpResponseData encoding:NSUTF8StringEncoding] autorelease];
    NSData *responseData = [responseString dataUsingEncoding:NSASCIIStringEncoding];
    
    NSError *error = nil;
    NSDictionary *responseDictionary = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
    NSMutableArray *encodedSyncPDataArray = [responseDictionary objectForKey:@"data"];
    
    SDLEncodedSyncPData *request = [[[SDLEncodedSyncPData alloc] init] autorelease];
    request.data = encodedSyncPDataArray;
    request.correlationID = [NSNumber numberWithInt:POLICIES_CORRELATION_ID];

    httpPolicyUpdateInProgress = NO;
    [self sendRPCRequestPrivate:request];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
        
    [SDLDebugTool logInfo:@"%s, Connection failed with error: %@", __PRETTY_FUNCTION__, [error localizedDescription]];
    [connection release];
    
    httpPolicyUpdateInProgress = NO;
}

@end
