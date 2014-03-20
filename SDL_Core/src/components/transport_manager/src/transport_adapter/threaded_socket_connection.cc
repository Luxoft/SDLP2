/**
 * \file threaded_socket_connection.cc
 * \brief ThreadedSocketConnection class source file.
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

#include <errno.h>
#include <fcntl.h>
#include <memory.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>

#include "transport_manager/transport_adapter/threaded_socket_connection.h"
#include "transport_manager/transport_adapter/transport_adapter_controller.h"

namespace transport_manager {
namespace transport_adapter {

ThreadedSocketConnection::ThreadedSocketConnection(
    const DeviceUID& device_id, const ApplicationHandle& app_handle,
    TransportAdapterController* controller)
    : read_fd_(-1), write_fd_(-1), controller_(controller),
      frames_to_send_(),
      frames_to_send_mutex_(),
      thread_(),
      socket_(-1),
      terminate_flag_(false),
      unexpected_disconnect_(false),
      device_uid_(device_id),
      app_handle_(app_handle) {
  pthread_mutex_init(&frames_to_send_mutex_, 0);
}

ThreadedSocketConnection::~ThreadedSocketConnection() {
  LOG4CXX_TRACE_ENTER(logger_);
  terminate_flag_ = true;
  Notify();
  pthread_join(thread_, 0);
  pthread_mutex_destroy(&frames_to_send_mutex_);

  if (-1 != read_fd_)
    close(read_fd_);
  if (-1 != write_fd_)
    close(write_fd_);

  LOG4CXX_TRACE_EXIT(logger_);
}

void ThreadedSocketConnection::Abort() {
  LOG4CXX_TRACE_ENTER(logger_);
  unexpected_disconnect_ = true;
  terminate_flag_ = true;
  LOG4CXX_TRACE_EXIT(logger_);
}

void* StartThreadedSocketConnection(void* v) {
  LOG4CXX_TRACE_ENTER(logger_);
  ThreadedSocketConnection* connection =
      static_cast<ThreadedSocketConnection*>(v);
  connection->Thread();
  LOG4CXX_TRACE_EXIT(logger_);
  return 0;
}

TransportAdapter::Error ThreadedSocketConnection::Start() {
  LOG4CXX_TRACE_ENTER(logger_);
  int fds[2];
  const int pipe_ret = pipe(fds);
  if (0 == pipe_ret) {
    LOG4CXX_INFO(logger_, "pipe created(#" << pthread_self() << ")");
    read_fd_ = fds[0];
    write_fd_ = fds[1];
  } else {
    LOG4CXX_INFO(logger_, "pipe creation failed (#" << pthread_self() << ")");
    LOG4CXX_TRACE_EXIT(logger_);
    return TransportAdapter::FAIL;
  }
  const int fcntl_ret = fcntl(read_fd_, F_SETFL,
                              fcntl(read_fd_, F_GETFL) | O_NONBLOCK);
  if (0 != fcntl_ret) {
    LOG4CXX_INFO(logger_, "fcntl failed (#" << pthread_self() << ")");
    LOG4CXX_TRACE_EXIT(logger_);
    return TransportAdapter::FAIL;
  }

  if (0 == pthread_create(&thread_, 0, &StartThreadedSocketConnection, this)) {
    LOG4CXX_INFO(logger_, "thread created (#" << pthread_self() << ")");
    LOG4CXX_TRACE_EXIT(logger_);
    return TransportAdapter::OK;
  } else {
    LOG4CXX_INFO(logger_, "thread creation failed (#" << pthread_self() << ")");
    LOG4CXX_TRACE_EXIT(logger_);
    return TransportAdapter::FAIL;
  }
}

void ThreadedSocketConnection::Finalize() {
  LOG4CXX_TRACE_ENTER(logger_);
  if (unexpected_disconnect_) {
    LOG4CXX_INFO(logger_, "unexpected_disconnect (#" << pthread_self() << ")");
    controller_->ConnectionAborted(device_handle(), application_handle(),
                                   CommunicationError());
  } else {
    LOG4CXX_INFO(logger_, "not unexpected_disconnect (#" << pthread_self() << ")");
    controller_->ConnectionFinished(device_handle(), application_handle());
  }
  close(socket_);
  LOG4CXX_INFO(logger_, "Connection finalized");
  LOG4CXX_TRACE_EXIT(logger_);
}

TransportAdapter::Error ThreadedSocketConnection::Notify() const {
  LOG4CXX_TRACE_ENTER(logger_);
  if (-1 == write_fd_) {
    LOG4CXX_ERROR_WITH_ERRNO(
            logger_, "Failed to wake up connection thread for connection " << this);
    LOG4CXX_INFO(logger_, "exit");
    return TransportAdapter::BAD_STATE;
  }
  uint8_t c = 0;
  if (1 == write(write_fd_, &c, 1)) {
    LOG4CXX_INFO(logger_, "exit");
    return TransportAdapter::OK;
  } else {
    LOG4CXX_ERROR_WITH_ERRNO(
            logger_, "Failed to wake up connection thread for connection " << this);
    LOG4CXX_TRACE_EXIT(logger_);
    return TransportAdapter::FAIL;
  }
}

TransportAdapter::Error ThreadedSocketConnection::SendData(
    RawMessageSptr message) {
  LOG4CXX_TRACE_ENTER(logger_);
  pthread_mutex_lock(&frames_to_send_mutex_);
  frames_to_send_.push(message);
  pthread_mutex_unlock(&frames_to_send_mutex_);
  LOG4CXX_TRACE_EXIT(logger_);
  return Notify();
}

TransportAdapter::Error ThreadedSocketConnection::Disconnect() {
  LOG4CXX_TRACE_ENTER(logger_);
  terminate_flag_ = true;
  LOG4CXX_TRACE_EXIT(logger_);
  return Notify();
}

void ThreadedSocketConnection::Thread() {
  LOG4CXX_TRACE_ENTER(logger_);
  controller_->ConnectionCreated(this, device_uid_, app_handle_);
  ConnectError* connect_error = nullptr;
  if (Establish(&connect_error)) {
    LOG4CXX_INFO(logger_, "Connection established (#" << pthread_self() << ")");
    controller_->ConnectDone(device_handle(), application_handle());
    while (!terminate_flag_) {
      Transmit();
    }
    LOG4CXX_INFO(logger_, "Connection is to finalize (#" << pthread_self() << ")");
    Finalize();
    while (!frames_to_send_.empty()) {
      LOG4CXX_INFO(logger_, "removing message (#" << pthread_self() << ")");
      RawMessageSptr message = frames_to_send_.front();
      frames_to_send_.pop();
      controller_->DataSendFailed(device_handle(), application_handle(),
                                  message, DataSendError());
    }
    controller_->DisconnectDone(device_handle(), application_handle());
  } else {
    LOG4CXX_INFO(logger_, "Connection Establish failed (#" << pthread_self() << ")");
    controller_->ConnectFailed(device_handle(), application_handle(),
                               *connect_error);
    delete connect_error;
  }
  LOG4CXX_TRACE_EXIT(logger_);
}

void ThreadedSocketConnection::Transmit() {
  LOG4CXX_TRACE_ENTER(logger_);
  bool pipe_notified = false;
  bool pipe_terminated = false;

  const nfds_t poll_fds_size = 2;
  pollfd poll_fds[poll_fds_size];
  poll_fds[0].fd = socket_;
  poll_fds[0].events = POLLIN | POLLPRI | (frames_to_send_.empty() ? 0 : POLLOUT);
  poll_fds[1].fd = read_fd_;
  poll_fds[1].events = POLLIN | POLLPRI;

  LOG4CXX_INFO(logger_, "poll (#" << pthread_self() << ") " << this);
  if (-1 == poll(poll_fds, poll_fds_size, -1)) {
    LOG4CXX_ERROR_WITH_ERRNO(logger_, "poll failed for connection " << this);
    Abort();
    LOG4CXX_INFO(logger_, "exit");
    return;
  }
  LOG4CXX_INFO(logger_, "poll is ok (#" << pthread_self() << ") " << this);
  // error check
  if (0 != (poll_fds[1].revents & (POLLERR | POLLHUP | POLLNVAL))) {
    LOG4CXX_ERROR(logger_,
                  "Notification pipe for connection " << this << " terminated");
    Abort();
    LOG4CXX_INFO(logger_, "exit");
    return;
  }

  if (0 != (poll_fds[0].revents & (POLLERR | POLLHUP | POLLNVAL))) {
    LOG4CXX_INFO(logger_, "Connection " << this << " terminated");
    Abort();
    LOG4CXX_INFO(logger_, "exit");
    return;
  }

  // send data if possible
  if (!frames_to_send_.empty() && (poll_fds[0].revents | POLLOUT)) {
    LOG4CXX_INFO(logger_, "frames_to_send_ not empty()  (#" << pthread_self() << ")");
    // clear notifications
    char buffer[256];
    ssize_t bytes_read = -1;
    do {
      bytes_read = read(read_fd_, buffer, sizeof(buffer));
    } while (bytes_read > 0);

    if ((bytes_read < 0) && (EAGAIN != errno)) {
      LOG4CXX_ERROR_WITH_ERRNO(logger_, "Failed to clear notification pipe");
      LOG4CXX_ERROR_WITH_ERRNO(logger_, "poll failed for connection " << this);
      Abort();
      LOG4CXX_INFO(logger_, "exit");
      return;
    }

    // send data
    const bool send_ok = Send();
    if (!send_ok) {
      LOG4CXX_INFO(logger_, "Send() failed  (#" << pthread_self() << ")");
      Abort();
      LOG4CXX_INFO(logger_, "exit");
      return;
    }
  }

  // receive data
  if (0 != poll_fds[0].revents & POLLIN) {
    const bool receive_ok = Receive();
    if (!receive_ok) {
      LOG4CXX_INFO(logger_, "Receive() failed  (#" << pthread_self() << ")");
      Abort();
      LOG4CXX_INFO(logger_, "exit");
      return;
    }
  }
  LOG4CXX_TRACE_EXIT(logger_);
}

bool ThreadedSocketConnection::Receive() {
  LOG4CXX_TRACE_ENTER(logger_);
  uint8_t buffer[4096];
  ssize_t bytes_read = -1;

  do {
    bytes_read = recv(socket_, buffer, sizeof(buffer), MSG_DONTWAIT);

    if (bytes_read > 0) {
      LOG4CXX_INFO(
          logger_,
          "Received " << bytes_read << " bytes for connection " << this);

      RawMessageSptr frame(
          new protocol_handler::RawMessage(0, 0, buffer, bytes_read));
      controller_->DataReceiveDone(device_handle(), application_handle(),
                                     frame);
    } else if (bytes_read < 0) {
      if (EAGAIN != errno && EWOULDBLOCK != errno) {
        LOG4CXX_ERROR_WITH_ERRNO(logger_,
                                 "recv() failed for connection " << this);
        LOG4CXX_TRACE_EXIT(logger_);
        return false;
      }
    } else {
      LOG4CXX_INFO(logger_, "Connection " << this << " closed by remote peer");
      LOG4CXX_TRACE_EXIT(logger_);
      return false;
    }
  } while (bytes_read > 0);
  LOG4CXX_TRACE_EXIT(logger_);
  return true;
}

bool ThreadedSocketConnection::Send() {
  LOG4CXX_TRACE_ENTER(logger_);
  FrameQueue frames_to_send;
  pthread_mutex_lock(&frames_to_send_mutex_);
  std::swap(frames_to_send, frames_to_send_);
  pthread_mutex_unlock(&frames_to_send_mutex_);

  bool frame_sent = false;
  size_t offset = 0;
  while (!frames_to_send.empty()) {
    LOG4CXX_INFO(logger_, "frames_to_send is not empty" << pthread_self() << ")");
    RawMessageSptr frame = frames_to_send.front();

    const ssize_t bytes_sent = ::send(socket_, frame->data() + offset,
                                      frame->data_size() - offset, 0);

    if (bytes_sent >= 0) {
      LOG4CXX_INFO(logger_, "bytes_sent >= 0" << pthread_self() << ")");
      offset += bytes_sent;
      if (offset == frame->data_size()) {
        frames_to_send.pop();
        offset = 0;
        controller_->DataSendDone(device_handle(), application_handle(), frame);
      }
    } else {
      LOG4CXX_INFO(logger_, "bytes_sent < 0" << pthread_self() << ")");
      LOG4CXX_ERROR_WITH_ERRNO(logger_, "Send failed for connection " << this);
      frames_to_send.pop();
      offset = 0;
      controller_->DataSendFailed(device_handle(), application_handle(), frame,
                                  DataSendError());
    }
  }
  LOG4CXX_TRACE_EXIT(logger_);
  return true;
}

}  // namespace
}  // namespace

