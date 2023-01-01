package cc.ussu.common.log.model.vo;

import cc.ussu.common.core.constants.StrConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class LogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String requestMethod;
    private String requestUri;
    private String requestParams;
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private LocalDateTime requestTime;
    private Integer httpStatus;
    private Integer resultCode;
    private String resultMsg;
    private String deviceName;
    private String browserType;
    private String userAgent;
    private String logName;
    private String createBy;
    private String remoteIp;
    private Long executeTime;
    private String exceptionInfo;
    private String locationStr;
    private String locationAdcode;
}
