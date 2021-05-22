package cn.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysMenu对象", description="系统菜单")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "父级编号，一级菜单0")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "菜单类型（10目录 20菜单 30按钮）")
    private Integer type;

    private String path;

    @ApiModelProperty(value = "链接")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否在菜单中显示：1，显示；0，隐藏；")
    private Integer showFlag;

    private Integer cacheFlag;

    private Integer iframeFlag;

    @ApiModelProperty(value = "权限标识")
    private String perm;

    private String sysType;

    @ApiModelProperty(value = "备注")
    private String remarks;

    private Integer status;

    @ApiModelProperty(value = "是否删除：1，是；0，否")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    // 是否被选中
    @TableField(exist = false)
    private Boolean checked;

    // @TableField(exist = false)
    // private Boolean disabled;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

}
