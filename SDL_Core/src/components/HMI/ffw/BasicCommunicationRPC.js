/*
 * Copyright (c) 2013, Ford Motor Company All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: ·
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. · Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. · Neither the name of the Ford Motor Company nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Reference implementation of BasicCommunication component. Interface to get or
 * set some essential information from OS. BasicCommunication responsible for
 * handling the basic commands of non-graphical part such as the registration of
 * mobile apps, geting the list of devices and applications and data transfer.
 */

FFW.BasicCommunication = FFW.RPCObserver
    .create( {

        /**
         * access to basic RPC functionality
         */
        client: FFW.RPCClient.create( {
            componentName: "BasicCommunication"
        }),

        onAppRegisteredSubscribeRequestID: -1,
        onAppUnregisteredSubscribeRequestID: -1,
        onPlayToneSubscribeRequestID: -1,

        onAppRegisteredUnsubscribeRequestID: -1,
        onAppUnregisteredUnsubscribeRequestID: -1,
        onPlayToneUnsubscribeRequestID: -1,

        // const
        onAppRegisteredNotification: "BasicCommunication.OnAppRegistered",
        onAppUnregisteredNotification: "BasicCommunication.OnAppUnregistered",
        onPlayToneNotification: "BasicCommunication.PlayTone",

        /**
         * init object
         */
        init: function() {

        },

        /**
         * connect to RPC bus
         */
        connect: function() {

            this.client.connect(this, 600); // Magic number is unique identifier
            // for component
        },

        /**
         * disconnect from RPC bus
         */
        disconnect: function() {

            this.client.disconnect();
        },

        /**
         * Client is registered - we can send request starting from this point
         * of time
         */
        onRPCRegistered: function() {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCRegistered");
            this._super();

            // subscribe to notifications
            this.onAppRegisteredSubscribeRequestID = this.client
                .subscribeToNotification(this.onAppRegisteredNotification);
            this.onAppUnregisteredSubscribeRequestID = this.client
                .subscribeToNotification(this.onAppUnregisteredNotification);
            this.onPlayToneNotificationID = this.client
                .subscribeToNotification(this.onPlayToneNotification);

        },

        /**
         * Client is unregistered - no more requests
         */
        onRPCUnregistered: function() {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCUnregistered");
            this._super();

            // unsubscribe from notifications
            this.onAppRegusteredUnsubscribeRequestID = this.client
                .unsubscribeFromNotification(this.onAppRegisteredNotification);
            this.onAppUnregusteredUnsubscribeRequestID = this.client
                .unsubscribeFromNotification(this.onAppUnregisteredNotification);
            this.onPlayToneUpdatedNotificationID = this.client
                .unsubscribeFromNotification(this.onPlayToneUpdatedNotification);
        },

        /**
         * Client disconnected.
         */
        onRPCDisconnected: function() {

            if (SDL.SDLAppController) {
                SDL.SDLAppController.onSDLDisconected();
            }
        },

        /**
         * when result is received from RPC component this function is called It
         * is the propriate place to check results of reuqest execution Please
         * use previously store reuqestID to determine to which request repsonse
         * belongs to
         */
        onRPCResult: function(response) {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCResult");
            this._super();
        },

        /**
         * handle RPC erros here
         */
        onRPCError: function(error) {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCError");
            this._super();
        },

        /**
         * handle RPC notifications here
         */
        onRPCNotification: function(notification) {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCNotification");
            this._super();

            if (notification.method == this.onAppRegisteredNotification) {
                SDL.SDLModel.onAppRegistered(notification.params.application);
                this.OnFindApplications();
            }

            if (notification.method == this.onAppUnregisteredNotification) {
                // remove app from list
                SDL.SDLModel.onAppUnregistered(notification.params);
            }

            if (notification.method == this.onPlayToneNotification) {
                SDL.SDLModel.onPlayTone();
            }
        },

        /**
         * handle RPC requests here
         */
        onRPCRequest: function(request) {

            Em.Logger.log("FFW.BasicCommunicationRPC.onRPCRequest");
            this._super();

            if (this.validationCheck(request)) {

                if (request.method == "BasicCommunication.MixingAudioSupported") {
                    this.MixingAudioSupported(true);
                }
                if (request.method == "BasicCommunication.AllowAllApps") {
                    this.AllowAllApps(true);
                }
                if (request.method == "BasicCommunication.AllowApp") {
                    this.AllowApp(true);
                }
                if (request.method == "BasicCommunication.AllowDeviceToConnect") {
                    this
                        .AllowDeviceToConnect(request.id, request.method, allow);
                }
                if (request.method == "BasicCommunication.UpdateAppList") {
                    if (SDL.States.info.active) {
                        SDL.SDLController
                            .onGetAppList(request.params.applications);
                    }
                    this.sendBCResult(SDL.SDLModel.resultCode["SUCCESS"],
                        request.id,
                        request.method);
                }
                if (request.method == "BasicCommunication.UpdateDeviceList") {
                    SDL.SDLModel.onGetDeviceList(request.params);
                    this.sendBCResult(SDL.SDLModel.resultCode["SUCCESS"],
                        request.id,
                        request.method);
                }
            }
        },

        /**
         * send response from onRPCRequest
         * 
         * @param {Number}
         *            resultCode
         * @param {Number}
         *            id
         * @param {String}
         *            method
         */
        sendBCResult: function(resultCode, id, method) {

            Em.Logger.log("FFW." + method + "Response");

            if (resultCode === SDL.SDLModel.resultCode["SUCCESS"]) {

                // send repsonse
                var JSONMessage = {
                    "jsonrpc": "2.0",
                    "id": id,
                    "result": {
                        "code": resultCode, // type (enum) from SDL protocol
                        "method": method
                    }
                };
                this.client.send(JSONMessage);
            }
        },

        /**
         * send response from onRPCRequest
         * 
         * @param {Number}
         *            id
         * @param {String}
         *            method
         * @param {Boolean}
         *            allow
         */
        AllowDeviceToConnect: function(id, method, allow) {

            Em.Logger.log("FFW." + method + "Response");

            // send repsonse
            var JSONMessage = {
                "jsonrpc": "2.0",
                "id": id,
                "result": {
                    "code": SDL.SDLModel.resultCode["SUCCESS"], // type (enum)
                    // from SDL
                    // protocol
                    "method": method,
                    "allow": true
                }
            };
            this.client.send(JSONMessage);

        },

        /**
         * notification that UI is ready BasicCommunication should be sunscribed
         * to this notification
         */
        onReady: function() {

            Em.Logger.log("FFW.BasicCommunication.onReady");

            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnReady"
            };
            this.client.send(JSONMessage);
        },

        /**
         * Send request if application was activated
         * 
         * @param {String}
         *            appName
         */
        OnAppActivated: function(appID) {

            Em.Logger.log("FFW.BasicCommunication.OnAppActivated");

            // send notification
            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnAppActivated",
                "params": {
                    "appID": appID
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * This methos is request to get list of registered apps.
         */
        OnFindApplications: function() {

            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnFindApplications"
            };

            if (SDL.SDLModel.CurrDeviceInfo.name
                || SDL.SDLModel.CurrDeviceInfo.id) {

                JSONMessage.params = {
                    "deviceInfo": SDL.SDLModel.CurrDeviceInfo
                };
            }

            this.client.send(JSONMessage);
        },

        /**
         * Request for list of avaliable devices
         */
        getDeviceList: function() {

            this.getDeviceListRequestID = this.client.generateID();

            var JSONMessage = {
                "id": this.getDeviceListRequestID,
                "jsonrpc": "2.0",
                "method": "BasicCommunication.GetDeviceList"
            };
            this.client.send(JSONMessage);
        },

        /**
         * Invoked by UI component when user switches to any functionality which
         * is not other mobile application.
         * 
         * @params {String}
         * @params {Number}
         */
        OnAppDeactivated: function(reason, appID) {

            Em.Logger.log("FFW.BasicCommunication.OnAppDeactivated");

            // send request

            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnAppDeactivated",
                "params": {
                    "appID": appID,
                    "reason": reason
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Initiated by HMI user. In response optional list of found devices -
         * if not provided, not were found.
         */
        OnStartDeviceDiscovery: function() {

            Em.Logger.log("FFW.BasicCommunication.OnStartDeviceDiscovery");

            // send request

            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnStartDeviceDiscovery"
            };
            this.client.send(JSONMessage);
        },

        /**
         * Used by HMI when User chooses to exit application.
         * 
         * @params {Number}
         */
        ExitApplication: function(appID) {

            Em.Logger.log("FFW.BasicCommunication.ExitApplication");

            // send request

            var JSONMessage = {
                "id": this.client.idStart,
                "jsonrpc": "2.0",
                "method": "BasicCommunication.ExitApplication",
                "params": {
                    "appID": appID
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Sent by HMI to SDL to close all registered applications.
         * 
         * @params {String}
         */
        ExitAllApplications: function(reason) {

            Em.Logger.log("FFW.BasicCommunication.ExitAllApplicationsResponse");

            // send request

            var JSONMessage = {
                "id": this.getAppListRequestID,
                "jsonrpc": "2.0",
                "method": "BasicCommunication.ExitAllApplications",
                "params": {
                    "reason": reason
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Response with params of the last one supports mixing audio (ie
         * recording TTS command and playing audio).
         * 
         * @params {Number}
         */
        MixingAudioSupported: function(attenuatedSupported) {

            Em.Logger
                .log("FFW.BasicCommunication.MixingAudioSupportedResponse");

            // send request

            var JSONMessage = {
                "id": this.client.idStart,
                "jsonrpc": "2.0",
                "result": {
                    "code": 0,
                    "attenuatedSupported": attenuatedSupported,
                    "method": "BasicCommunication.MixingAudioSupported"
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Response with Results by user/HMI allowing SDL functionality or
         * disallowing access to all mobile apps.
         * 
         * @params {Number}
         */
        AllowAllApps: function(allowed) {

            Em.Logger.log("FFW.BasicCommunication.AllowAllAppsResponse");

            // send request

            var JSONMessage = {
                "id": this.client.idStart,
                "jsonrpc": "2.0",
                "result": {
                    "code": 0,
                    "method": "BasicCommunication.AllowAllApps",
                    "allowed": allowed
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Response with result of allowed application
         * 
         * @params {Number}
         */
        AllowApp: function(allowed) {

            Em.Logger.log("FFW.BasicCommunication.AllowAppResponse");

            // send request

            var JSONMessage = {
                "id": this.client.idStart,
                "jsonrpc": "2.0",
                "result": {
                    "code": 0,
                    "method": "BasicCommunication.AllowApp",
                    "allowed": allowed
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Notifies if device was choosed
         * 
         * @param {String}
         *            deviceName
         * @param {Number}
         *            appID
         */
        OnDeviceChosen: function(deviceName, appID) {

            Em.Logger.log("FFW.UI.OnDeviceChosen");

            // send repsonse
            var JSONMessage = {
                "jsonrpc": "2.0",
                "method": "BasicCommunication.OnDeviceChosen",
                "params": {
                    "deviceInfo": {
                        "name": deviceName,
                        "id": appID
                    }
                }
            };
            this.client.send(JSONMessage);
        },

        /**
         * Send error response from onRPCRequest
         * 
         * @param {Number}
         *            resultCode
         * @param {Number}
         *            id
         * @param {String}
         *            method
         */
        sendError: function(resultCode, id, method, message) {

            Em.Logger.log("FFW." + method + "Response");

            if (resultCode != SDL.SDLModel.resultCode["SUCCESS"]) {

                // send repsonse
                var JSONMessage = {
                    "jsonrpc": "2.0",
                    "id": id,
                    "error": {
                        "code": resultCode, // type (enum) from SDL protocol
                        "message": message,
                        "data": {
                            "method": method
                        }
                    }
                };
                this.client.send(JSONMessage);
            }
        }

    })
