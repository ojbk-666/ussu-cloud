package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 角色排序
     */
    @TableField("role_sort")
    private Integer roleSort;

    /**
     * 状态：0正常  1停用
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 删除状态 0正常  1删除 
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 乐观锁
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 数据范围类型：10全部数据权限 20本部门及以下数据权限 30本部门数据权限 40自定数据权限 
     */
    @TableField("data_scope_type")
    private Integer dataScopeType;

    /**
     * 关联的菜单id集合
     */
    @TableField(exist = false)
    private List<String> menuIds;

}
