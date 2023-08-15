package cc.ussu.common.log.util;

import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.log.service.LogService;
import cc.ussu.log.api.vo.LogVO;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.extra.spring.SpringUtil;
import org.slf4j.MDC;

public class LogUtil {

    public static String getTraceId() {
        return MDC.get(SecurityConstants.TRACE_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put(SecurityConstants.TRACE_ID, traceId);
    }

    public static void saveLog(ILoggingEvent loggingEvent) {
        LogService logService = SpringUtil.getBean(LogService.class);
        if (logService != null) {
            LogVO logVO = new LogVO(loggingEvent);
            logService.saveLog(logVO);
        }
    }

}
