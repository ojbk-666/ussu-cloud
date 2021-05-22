package cn.ussu.modules.system.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 字典表
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysDict对象", description="字典表")
public class SysDict extends Model<SysDict> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "数据值")
    private String dictValue;

    @ApiModelProperty(value = "标签名")
    private String dictLabel;

    @ApiModelProperty(value = "类型id")
    private String typeId;

    @ApiModelProperty(value = "类型编码")
    private String typeCode;

    @ApiModelProperty(value = "描述")
    private String dictDesc;

    @ApiModelProperty(value = "排序")
    private Integer dictSort;

    @ApiModelProperty(value = "状态 1正常 0停用")
    private Integer status;

    @ApiModelProperty(value = "删除标记：0正常  1删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

}
