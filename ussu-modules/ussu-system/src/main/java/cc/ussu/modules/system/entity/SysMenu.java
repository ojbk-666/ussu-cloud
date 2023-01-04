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
 * 系统菜单
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer TYPE_DIR = 10;
    public static final Integer TYPE_MENU = 20;
    public static final Integer TYPE_BUTTON = 30;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 父级编号，一级菜单0
     */
    @TableField("parent_id")
    private String parentId;

    public boolean parentIsZero() {
        return "0".equals(parentId);
    }

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @TableField("name")
    private String name;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 菜单类型（10菜单 20权限）
     */
    @TableField("type")
    private Integer type;

    public boolean isDir() {
        return TYPE_DIR.equals(type);
    }

    public boolean isMenu() {
        return TYPE_MENU.equals(type);
    }

    public boolean isButton() {
        return TYPE_BUTTON.equals(type);
    }

    /**
     * 路由
     */
    @TableField("path")
    private String path;

    /**
     * 组件
     */
    @TableField("component")
    private String component;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 是否在菜单中显示：1，显示；0，隐藏；
     */
    @TableField("show_flag")
    private Boolean showFlag;

    @TableField("frame_flag")
    private Boolean frameFlag;

    @TableField("cache_flag")
    private Boolean cacheFlag;

    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 权限标识
     */
    @TableField("action")
    private String action;

    @TableField("sys_type")
    private String sysType;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 是否删除：1，是；0，否
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
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children;

}
