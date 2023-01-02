package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 东财接口访问日志
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-03 14:16:34
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("dc_request_log")
public class DcRequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("userid")
    private String userid;

    /**
     * 请求参数
     */
    @TableField("param")
    private String param;

    /**
     * 请求时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
