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
 * 字典类型表
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysDictType对象", description="字典类型表")
public class SysDictType extends Model<SysDictType> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "字典名称")
    private String typeName;

    @ApiModelProperty(value = "字典编码")
    private String typeCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;

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
