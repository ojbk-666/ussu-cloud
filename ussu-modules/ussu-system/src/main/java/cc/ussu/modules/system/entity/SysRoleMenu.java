package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色表主键
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 菜单表主键
     */
    @TableField("menu_id")
    private String menuId;

}
