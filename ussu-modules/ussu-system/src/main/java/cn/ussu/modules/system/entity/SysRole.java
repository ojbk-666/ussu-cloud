package cn.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRole对象", description="角色")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色描述")
    private String roleDesc;

    @ApiModelProperty(value = "角色排序")
    private Integer roleSort;

    @ApiModelProperty(value = "状态：1正常  1停用")
    private Integer status;

    @ApiModelProperty(value = "删除状态 0正常  1删除 ")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    private Integer dataScopeType;

    @TableField(exist = false)
    private List<String> perms;

    @TableField(exist = false)
    private List<String> menuIds;

}
