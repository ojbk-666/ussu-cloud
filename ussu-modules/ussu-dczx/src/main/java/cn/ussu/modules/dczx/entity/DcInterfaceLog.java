package cn.ussu.modules.dczx.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 接口日志
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DcInterfaceLog对象", description = "接口日志")
public class DcInterfaceLog extends Model<DcInterfaceLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "页面访问的url")
    private String url;

    private String reqUrl;

    @ApiModelProperty(value = "accesstoken")
    private String accessToken;

    @ApiModelProperty(value = "userid")
    private String userid;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "参与签名计算")
    private String time;

    @ApiModelProperty(value = "响应体")
    private String responseBody;

    @ApiModelProperty(value = "是否成功")
    private Boolean result;

    @ApiModelProperty(value = "失败原因")
    private String reason;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    // @TableField(fill = FieldFill.INSERT)
    private String createBy;

    private String remarks;

    @ApiModelProperty(value = "删除标记")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
