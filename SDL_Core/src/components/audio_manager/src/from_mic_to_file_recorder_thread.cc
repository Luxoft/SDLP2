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

#include "audio_manager/from_mic_to_file_recorder_thread.h"

namespace audio_manager {

log4cxx::LoggerPtr FromMicToFileRecorderThread::logger_ = log4cxx::LoggerPtr(
      log4cxx::Logger::getLogger("FromMicToFileRecorderThread"));

GMainLoop* FromMicToFileRecorderThread::loop = NULL;

FromMicToFileRecorderThread::FromMicToFileRecorderThread()
  : threads::ThreadDelegate(),
    argc_(5),
    oKey_("-o"),
    tKey_("-t"),
    sleepThread_(NULL) {
  LOG4CXX_TRACE_ENTER(logger_);
  stopFlagMutex_.init();
}

void FromMicToFileRecorderThread::setOutputFileName(
  const std::string& outputFileName) {
  LOG4CXX_TRACE_ENTER(logger_);

  outputFileName_ = outputFileName;
}

void FromMicToFileRecorderThread::setRecordDuration(int duration) {
  LOG4CXX_TRACE_ENTER(logger_);

  std::stringstream stringStream;
  stringStream << duration / 1000;
  durationString_ = stringStream.str();
}

void FromMicToFileRecorderThread::initArgs() {
  LOG4CXX_TRACE_ENTER(logger_);

  argv_ = new gchar*[argc_];

  argv_[0] = new gchar[14];
  argv_[1] = new gchar[3];
  argv_[2] = new gchar[outputFileName_.length() + 1];
  argv_[3] = new gchar[3];
  argv_[4] = new gchar[durationString_.length() + 1];

  argv_[0] = const_cast<gchar*>(std::string("AudioManager").c_str());
  argv_[1] = const_cast<gchar*>(oKey_.c_str());
  argv_[2] = const_cast<gchar*>(outputFileName_.c_str());
  argv_[3] = const_cast<gchar*>(tKey_.c_str());
  argv_[4] = const_cast<gchar*>(durationString_.c_str());
}

void FromMicToFileRecorderThread::threadMain() {
  LOG4CXX_TRACE_ENTER(logger_);

  stopFlagMutex_.lock();
  shouldBeStoped_ = false;
  stopFlagMutex_.unlock();

  initArgs();

  GstElement* pipeline;
  GstElement* alsasrc, *panorama, *wavenc, *filesink;
  GstBus* bus;

  gchar* device = "hw:0,0";
  gchar* outfile = NULL;
  gint duration = -1;
  GOptionContext* context = NULL;
  GError* err = NULL;
  GOptionEntry entries[] = {
    {
      "device", 'd', 0, G_OPTION_ARG_FILENAME, &device,
      "device file (Default: hw:0,0)", "SRC"
    },
    {
      "output", 'o', 0, G_OPTION_ARG_FILENAME, &outfile,
      "save output of the stream to DEST", "DEST"
    },
    {
      "duration", 't', 0, G_OPTION_ARG_INT, &duration,
      "length of time in seconds to capture", "INT"
    },
    {NULL}
  };

  pthread_t wait;
  int retcode;

  if (!g_thread_supported()) {
    g_thread_init(NULL);
  }

  // Parse the arguments
  context = g_option_context_new("-- M-AUDIO RAW");
  g_option_context_add_main_entries(context, entries, NULL);
  g_option_context_add_group(context, gst_init_get_option_group());
  if (!g_option_context_parse(context, &argc_, &argv_, &err)) {
    g_error("%s\n", err->message);
  }

  // Check for proper arguments
  if (outfile == NULL) {
    g_error("Must supply destination (-d FILE)\n");
  }

  LOG4CXX_TRACE(logger_, "Reading from device: " << device);
  LOG4CXX_TRACE(logger_, "Saving pipeline output to: " << outfile);
  LOG4CXX_TRACE(logger_, "Duration set to: " << duration);

  // Initialize gstreamer and setup the main loop information
  gst_init(&argc_, &argv_);

  pipeline = gst_pipeline_new("vga2usb-h264");

  // Set up error handling
  bus = gst_pipeline_get_bus(GST_PIPELINE(pipeline));
  gst_bus_add_watch(bus,
                    reinterpret_cast<int (*)(_GstBus*, _GstMessage*, void*)>(recvmsg),
                    NULL);
  gst_object_unref(bus);

  // Create all of the elements to be added to the pipeline
  alsasrc = gst_element_factory_make("alsasrc", "alsasrc0");
  panorama = gst_element_factory_make("audiopanorama", "panorama0");
  wavenc = gst_element_factory_make("wavenc", "wavenc0");
  filesink = gst_element_factory_make("filesink", "filesink0");

  // Assert that all the elements were created
  if (!alsasrc || !panorama || !wavenc || !filesink) {
    g_error("Failed creating one or more of the pipeline elements.\n");
  }

  // Set input and output destinations
  g_object_set(G_OBJECT(alsasrc), "device", device, NULL);
  g_object_set(G_OBJECT(panorama), "panorama", 0.5, NULL);
  g_object_set(G_OBJECT(filesink), "location", outfile, NULL);

  // Add the elements to the pipeline
  gst_bin_add_many(GST_BIN(pipeline), alsasrc,
                   panorama, wavenc, filesink, NULL);

  // Link the elements
  gst_element_link_many(alsasrc, panorama, wavenc, filesink, NULL);

  gst_element_set_state(pipeline, GST_STATE_PLAYING);

  LOG4CXX_TRACE(logger_, "Initializing pipeline ...");
  while (GST_STATE(pipeline) != GST_STATE_PLAYING) {
    LOG4CXX_TRACE(logger_, "GST_STATE(pipeline) != GST_STATE_PLAYING");

    stopFlagMutex_.lock();
    bool shouldBeStoped = shouldBeStoped_;
    stopFlagMutex_.unlock();

    if (shouldBeStoped) {
      return;
    }
  }

  LOG4CXX_TRACE(logger_, "Pipeline started ...\n");

  // Start up a timer for the pipeline
  if (duration > 0) {
    GstTimeout timeout;
    timeout.pipeline = pipeline;
    timeout.duration = duration;

    sleepThread_ = new threads::Thread("SleepThread"
                                       , new SleepThreadDelegate(timeout));

    if (NULL != sleepThread_) {
      sleepThread_->start();
    }
  }

  loop = g_main_loop_new(NULL, FALSE);

  g_main_loop_run(loop);

  gst_element_set_state(pipeline, GST_STATE_NULL);

  LOG4CXX_TRACE(logger_, "Deleting pipeline\n");
  gst_object_unref(GST_OBJECT(pipeline));
  g_main_loop_unref(loop);

  loop = NULL;
}

FromMicToFileRecorderThread::SleepThreadDelegate::SleepThreadDelegate(GstTimeout timeout)
  : threads::ThreadDelegate(),
    timeout_(timeout) {
}

void FromMicToFileRecorderThread::SleepThreadDelegate::threadMain() {
  LOG4CXX_TRACE(logger_, "Sleep for " << timeout_.duration << " seconds");

  sleep(timeout_.duration);

  if (NULL != loop) {
    if (g_main_loop_is_running(loop)) {
      gst_element_send_event(timeout_.pipeline, gst_event_new_eos());
    }
  }
}

bool FromMicToFileRecorderThread::exitThreadMain() {
  LOG4CXX_TRACE_ENTER(logger_);

  if (NULL != loop) {
    if (g_main_loop_is_running(loop)) {
      LOG4CXX_TRACE(logger_, "Quit loop\n");
      g_main_loop_quit(loop);
    }
  }

  if (NULL != sleepThread_) {
    LOG4CXX_TRACE(logger_, "Stop sleep thread\n");
    sleepThread_->stop();
    delete sleepThread_;
    sleepThread_ = NULL;
  }

  LOG4CXX_TRACE(logger_, "Set should be stopped flag\n");
  stopFlagMutex_.lock();
  shouldBeStoped_ = true;
  stopFlagMutex_.unlock();

  return true;
}

}  // namespace audio_manager
