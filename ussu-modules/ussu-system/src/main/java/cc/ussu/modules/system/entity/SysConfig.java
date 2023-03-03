package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_config")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 分组名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 分组编码
     */
    @TableField("group_code")
    private String groupCode;

    /**
     * 是否是分组 1是 0否
     */
    @TableField("group_flag")
    private String groupFlag;

    /**
     * 配置编码
     */
    @TableField("code")
    private String code;

    /**
     * 标签
     */
    @TableField("label")
    private String label;

    /**
     * 值
     */
    @TableField("value")
    private String value;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 禁用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 是否删除：0未删除 1已删除
     */
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;


}
