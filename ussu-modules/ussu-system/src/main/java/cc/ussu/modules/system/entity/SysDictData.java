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
 * 字典表
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_data")
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 类型编码
     */
    @NotBlank(message = "编码不能为空")
    @TableField("dict_type")
    private String dictType;

    /**
     * 类型编码
     */
    @NotBlank(message = "字典编码不能为空")
    @TableField("dict_code")
    private String dictCode;

    /**
     * 标签名
     */
    // @NotBlank(message = "名称不能为空")
    @TableField("dict_label")
    private String dictLabel;

    /**
     * 数据值
     */
    @NotBlank(message = "值不能为空")
    @TableField("dict_value")
    private String dictValue;

    /**
     * 状态 0 正常 1停用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;

    /**
     * 删除标记：0正常  1删除
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

}
