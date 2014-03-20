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
 * @name SDL.InteractionChoicesView
 * @desc Interaction Choices visual representation
 * @category View
 * @filesource app/view/sdl/shared/interactionChoicesView.js
 * @version 1.0
 */

SDL.InteractionChoicesView = SDL.SDLAbstractView
    .create( {

        elementId: 'perform_interaction_view',

        childViews: [
            'backButton', 'captionText', 'listOfChoices'
        ],

        backButton: SDL.Button.extend( {
            classNames:
                [
                    'back-button'
                ],
            target: 'SDL.SDLController',
            action: 'InteractionChoicesDeactivate',
            icon: 'images/media/ico_back.png',
            onDown: false
        } ),

        listOfChoices: SDL.List.extend( {
            elementId: 'perform_interaction_view_list',
            itemsOnPage: 5,
            items: []
        }),

        timer: null,

        /**
         * Identifier of current request
         */
        performInteractionRequestID: null,

        /**
         * Deactivate window
         */
        deactivate: function(ABORTED) {

            clearTimeout(this.timer);
            this.set('active', false);
            SDL.SDLController.VRMove();

            switch (ABORTED) {
            case "ABORTED": {
                SDL.SDLController
                    .interactionChoiseCloseResponse(SDL.SDLModel.resultCode["ABORTED"],
                        this.performInteractionRequestID);
                break;
            }
            case "TIMED_OUT": {
                SDL.SDLController
                    .interactionChoiseCloseResponse(SDL.SDLModel.resultCode["TIMED_OUT"],
                        this.performInteractionRequestID);
                break;
            }
            default: {
                // default action
            }
            }
        },

        /**
         * Clean choices caption and list before new proform
         */
        clean: function() {

            this.set('captionText.content', 'Interaction Choices');
            this.listOfChoices.items = [];
            this.listOfChoices.list.refresh();
        },

        /**
         * Update choises list with actual set id
         * 
         * @param data:
         *            Array
         */
        preformChoices: function(data, performInteractionRequestID, timeout) {

            if (!data) {
                Em.Logger.error('No choices to preform');
                return;
            }

            this
                .set('performInteractionRequestID', performInteractionRequestID);

            var i = 0, length = data.length, self = this;

            // temp for testing
            for (i = 0; i < length; i++) {
                this.listOfChoices.items
                    .push( {
                        type: SDL.Button,
                        params: {
                            text: data[i].menuName,
                            choiceID: data[i].choiceID,
                            action: 'onChoiceInteraction',
                            onDown: false,
                            target: 'SDL.SDLAppController',
                            performInteractionRequestID: performInteractionRequestID,
                            templateName: data[i].image ? 'rightIcon' : 'text',
                            icon: data[i].image ? data[i].image.value : null
                        }
                    });
            }

            this.listOfChoices.list.refresh();

            clearTimeout(this.timer);
            this.timer = setTimeout(function() {

                self.deactivate("TIMED_OUT");
            }, timeout);
        }
    });
