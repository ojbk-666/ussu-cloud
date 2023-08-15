package cc.ussu.common.log.logback.appender;


import cc.ussu.common.log.util.LogUtil;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class Log2ESAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent eventObject) {
        // LoggingEventVO loggingEventVO = LoggingEventVO.build(eventObject);
        LogUtil.saveLog(eventObject);
    }
}
