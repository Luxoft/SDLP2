/**
 * Copyright (c) 2013, Ford Motor Company
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of the Ford Motor Company nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
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

#include "./to_hmh_thread_impl.h"

namespace application_manager {

log4cxx::LoggerPtr ToHMHThreadImpl::logger_ = log4cxx::LoggerPtr(
    log4cxx::Logger::getLogger("ApplicationManager"));

ToHMHThreadImpl::ToHMHThreadImpl(ApplicationManagerImpl* handler)
    : handler_(handler) {
  DCHECK(handler);
}

ToHMHThreadImpl::~ToHMHThreadImpl() {
  handler_ = NULL;
}

void ToHMHThreadImpl::threadMain() {
  while (1) {
    while (!handler_->messages_to_hmh_.empty()) {
      LOG4CXX_INFO(logger_, "Received message to hmi");
      utils::SharedPtr<Message> message = handler_->messages_to_hmh_.pop();

      if (!handler_->hmi_handler_) {
        LOG4CXX_ERROR(logger_, "Observer is not set for HMIMessageHandler");
        continue;
      }

      handler_->hmi_handler_->SendMessageToHMI(message);
      LOG4CXX_INFO(logger_, "Message from hmi given away.");
    }
    handler_->messages_to_hmh_.wait();
  }
}

}  // namespace application_manager
