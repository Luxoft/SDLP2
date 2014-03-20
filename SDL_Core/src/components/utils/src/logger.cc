#include "utils/logger.h"


using namespace log4cxx;

Tracer::Tracer(LoggerPtr logger, std::string const& event)
{
	mLogger = logger;
	mEvent = event;
	LOG4CXX_TRACE(mLogger, "ENTER: " + mEvent);
}

Tracer::~Tracer()
{
	LOG4CXX_TRACE(mLogger, "EXIT: " + mEvent);
}