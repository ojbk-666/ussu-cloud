package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统用户表主键
     */
    @TableField("user_id")
    private String userId;

    /**
     * 角色主键
     */
    @TableField("role_id")
    private String roleId;

}
