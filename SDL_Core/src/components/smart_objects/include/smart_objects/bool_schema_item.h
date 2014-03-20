/**
 * @file CBoolSchemaItem.hpp
 * @brief CBoolSchemaItem header file.
 */
// Copyright (c) 2013, Ford Motor Company
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following
// disclaimer in the documentation and/or other materials provided with the
// distribution.
//
// Neither the name of the Ford Motor Company nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 'A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

#ifndef __SMARTOBJECT_CBOOLSCHEMAITEM_HPP__
#define __SMARTOBJECT_CBOOLSCHEMAITEM_HPP__

#include "utils/shared_ptr.h"

#include "smart_objects/schema_item_parameter.h"
#include "smart_objects/schema_item.h"

namespace NsSmartDeviceLink
{
    namespace NsSmartObjects
    {
        /**
         * @brief Bool schema item.
         **/
        class CBoolSchemaItem: public ISchemaItem
        {
        public:
            /**
             * @brief Create a new schema item.
             *
             * @param DefaultValue Default value of a parameter.
             *
             * @return Shared pointer to a new schema item.
             **/
            static utils::SharedPtr<CBoolSchemaItem> create(const TSchemaItemParameter<bool> & DefaultValue = TSchemaItemParameter<bool>());

            /**
             * @brief Validate smart object.
             *
             * @param Object Object to validate.
             *
             * @return NsSmartObjects::Errors::eType
             **/
            virtual Errors::eType validate(const NsSmartDeviceLink::NsSmartObjects::SmartObject & Object);

            /**
             * @brief Set default value to an object.
             *
             * @param Object Object to set default value.
             *
             * @return true if default value was successfully set, false otherwise.
             **/
            virtual bool setDefaultValue(SmartObject & Object);

           /**
             * @brief Build smart object by smart schema having copied matched
             *        parameters from pattern smart object
             *
             * @param pattern_object pattern object
             * @param result_object object to build
             */
            virtual void BuildObjectBySchema(
              const NsSmartDeviceLink::NsSmartObjects::SmartObject& pattern_object,
              NsSmartDeviceLink::NsSmartObjects::SmartObject& result_object);

        private:
            /**
             * @brief Constructor.
             *
             * @param DefaultValue Default value of a parameter.
             **/
            CBoolSchemaItem(const TSchemaItemParameter<bool> & DefaultValue);

            /**
             * @brief Copy constructor.
             *
             * Not implemented to prevent misuse.
             *
             * @param Other Other schema item.
             **/
            CBoolSchemaItem(const CBoolSchemaItem & Other);

            /**
             * @brief Assignment operator.
             *
             * Not implemented to prevent misuse.
             *
             * @param Other Other schema item.
             *
             * @return Not implemented.
             **/
            CBoolSchemaItem & operator =(const CBoolSchemaItem & Other);

            /**
             * @param DefaultValue Default value of a parameter.
             */
            const TSchemaItemParameter<bool> mDefaultValue;
        };
    }
}

#endif
