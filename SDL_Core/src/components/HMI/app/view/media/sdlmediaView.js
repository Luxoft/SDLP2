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
 * @name SDL.sdlView
 * @desc SDL Media application module visual representation
 * @category View
 * @filesource app/view/media/sdlView.js
 * @version 1.0
 */
SDL.sdlView = Em.ContainerView
    .create( {

        /**
         * View Id
         */
        elementId: 'sdl_view_container',

        classNameBindings: [
            'SDL.States.media.sdlmedia.active:active_state:inactive_state'
        ],

        /**
         * View Components
         */
        childViews: [
            'innerMenu', 'controlls'
        ],

        controlls: SDL.SDLMediaControlls,

        /**
         * Deactivate View
         */
        deactivate: function() {

            SDL.States.goToStates('info.apps');
        },

        innerMenu: SDL.MenuList
            .extend( {

                refreshItems: function() {

                    if (SDL.SDLAppController.model.appID == SDL.SDLMediaController.currentAppId) {
                        this.addItems(SDL.SDLAppController.model.softButtons,
                            SDL.SDLAppController.model.appID);
                    }
                }.observes('SDL.SDLAppController.model.softButtons.@each'),

                groupName: "MediaView",

                content: Em.ContainerView.extend( {

                    classNames: [
                        'content'
                    ],

                    attributeBindings: [
                        'parentView.contentPositon:style'
                    ],

                    childViews: [
                        'optionsButton'
                    ],

                    optionsButton: SDL.Button.extend( {
                        text: 'Options',

                        templateName: 'arrow',

                        action: 'openCommandsList',
                        target: 'SDL.SDLAppController'
                    })
                })
            })
    });