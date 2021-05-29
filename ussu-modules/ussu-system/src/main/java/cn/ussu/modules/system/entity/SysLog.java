package cn.ussu.modules.system.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysLog对象", description="系统日志")
public class SysLog extends Model<SysLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "请求方式:get,post,delete,put")
    private String requestMethod;

    @ApiModelProperty(value = "请求地址")
    private String requestUri;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求发起时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private LocalDateTime requestTime;

    @ApiModelProperty(value = "http状态码:200,404,500等")
    private Integer httpStatus;

    @ApiModelProperty(value = "业务响应状态码，code")
    private Integer resultCode;

    @ApiModelProperty(value = "业务响应状态消息，msg")
    private String resultMsg;

    @ApiModelProperty(value = "设备类型")
    private String deviceName;

    @ApiModelProperty(value = "浏览器类型")
    private String browserType;

    @ApiModelProperty(value = "原始的浏览器ua信息")
    private String userAgent;

    @ApiModelProperty(value = "日志名称")
    private String logName;

    @ApiModelProperty(value = "操作用户的userid")
    private String createBy;

    @ApiModelProperty(value = "客户端ip")
    private String remoteIp;

    @ApiModelProperty(value = "请求耗时")
    private Long executeTime;

    @ApiModelProperty(value = "异常")
    private String exceptionInfo;

    private String locationStr;

    private String locationAdcode;

}
