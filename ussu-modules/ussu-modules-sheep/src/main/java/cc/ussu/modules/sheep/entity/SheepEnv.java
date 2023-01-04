package cc.ussu.modules.sheep.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 环境变量
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:40:01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sheep_env")
public class SheepEnv implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 值
     */
    @TableField("value")
    private String value;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 状态 1正常 0禁用
     */
    @TableField("disabled")
    private Boolean disabled;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
