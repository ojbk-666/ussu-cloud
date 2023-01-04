package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 字典类型表
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict_type")
public class SysDictType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 字典名称
     */
    @NotBlank(message = "名称不能为空")
    @TableField("dict_name")
    private String dictName;

    /**
     * 字典编码
     */
    @NotBlank(message = "编码不能为空")
    @TableField("dict_type")
    private String dictType;

    /**
     * 状态 0 正常 1 停用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 备注信息
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标记：0正常  1删除
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

}
