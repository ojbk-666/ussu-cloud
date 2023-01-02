package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class DcInterfaceLog implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String url;

    private String reqUrl;

    private String accessToken;

    private String userid;

    private String sign;

    private String time;

    private String responseBody;

    private Boolean result;

    private String reason;

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
