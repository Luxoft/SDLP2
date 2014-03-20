/**
* \file SynchronisationPrimitives.h
* \brief SynchronisationPrimitives class header.
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

#ifndef SRC_COMPONENTS_UTILS_INCLUDE_UTILS_SYNCHRONISATIONPRIMITIVESWRAPPER_H_
#define SRC_COMPONENTS_UTILS_INCLUDE_UTILS_SYNCHRONISATIONPRIMITIVESWRAPPER_H_

#if defined(OS_WIN)
#include <windows.h>
typedef HANDLE PlatformMutex;
typedef CONDITION_VARIABLE PlatformConditionalVar;
#elif defined(OS_POSIX)
#include <pthread.h>
typedef pthread_mutex_t PlatformMutex;
typedef pthread_cond_t PlatformConditionalVar;
#if defined(OS_LINUX) || defined(OS_OPENBSD)
#include <unistd.h>
#elif defined(OS_BSD)
#include <sys/types.h>
#elif defined(OS_MACOSX)
#include <mach/mach.h>
#endif
#endif

#include "utils/macro.h"
#include "utils/logger.h"

namespace sync_primitives {

class SynchronisationPrimitives {
  public:
    enum WaitStatus {SIGNALED = 0, TIMED_OUT, FAILED};
  public:
    SynchronisationPrimitives();
    virtual ~SynchronisationPrimitives();

    virtual void init();

    virtual void lock();

    virtual void unlock();

    virtual void wait();

    virtual WaitStatus timedwait(time_t sec, uint32_t nsec);

    virtual void signal();

    PlatformMutex& mutex();

    PlatformConditionalVar& conditional_var();

  private:
    PlatformMutex mutex_;
    PlatformConditionalVar cond_variable_;

    DISALLOW_COPY_AND_ASSIGN(SynchronisationPrimitives);
};

}  // namespace sync_primitives

#endif  // SRC_COMPONENTS_UTILS_INCLUDE_UTILS_SYNCHRONISATIONPRIMITIVESWRAPPER_H_
