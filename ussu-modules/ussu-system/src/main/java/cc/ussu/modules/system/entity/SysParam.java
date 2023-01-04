package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统参数表
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_param")
public class SysParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名不能为空")
    @TableField("param_name")
    private String paramName;

    /**
     * 参数名称key
     */
    @NotBlank(message = "参数键不能为空")
    @TableField("param_key")
    private String paramKey;

    /**
     * 参数值value
     */
    @NotBlank(message = "参数值不能为空")
    @TableField("param_value")
    private String paramValue;

    /**
     * 参数描述
     */
    @TableField("remark")
    private String remark;

    private String disableFlag;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 是否删除：0未删除 1已删除
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

    /**
     * 乐观锁
     */
    @TableField("version")
    @Version
    private Integer version;

}
