package cn.ussu.modules.system.entity;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysRoleMenu对象", description="角色菜单表")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色表主键")
    // @TableId
    private String roleId;

    @ApiModelProperty(value = "菜单表主键")
    // @TableId
    private String menuId;

}
