package cc.ussu.log.api.vo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LogVO {

    private String id;
    private String threadName;
    private String loggerName;
    private String level;
    private String formattedMessage;
    private String exception;
    private long timeStamp;

    private String traceId;

    private Float score;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;

    public LogVO() {
    }

    public LogVO(ILoggingEvent event) {
        threadName = event.getThreadName();
        loggerName = event.getLoggerName();
        level = event.getLevel().levelStr;
        timeStamp = event.getTimeStamp();
        formattedMessage = event.getFormattedMessage();
        exception = ThrowableProxyUtil.asString(event.getThrowableProxy());
        // traceId = MDC.get(LogConstants.TRACE_ID);
    }

}
