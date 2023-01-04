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
 * 岗位
 * </p>
 *
 * @author liming
 * @since 2021-12-31 19:24:31
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_post")
public class SysPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 岗位编码
     */
    @NotBlank(message = "岗位编码不能为空")
    @TableField("post_code")
    private String postCode;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    @TableField("post_name")
    private String postName;

    /**
     * 排序号
     */
    @TableField("post_sort")
    private Integer postSort;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态：0正常  1停用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 删除标记：0正常 1删除
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

}
