package cn.ussu.modules.system.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 岗位
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPost对象", description="岗位")
public class SysPost extends Model<SysPost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    @ApiModelProperty(value = "上级岗位id")
    private String parentId;

    @ApiModelProperty(value = "上级岗位code")
    private String parentCode;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "排序号")
    private Integer postSort;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态：0正常  1停用")
    private Boolean stopFlag;

    @ApiModelProperty(value = "删除标记：0正常 1删除")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
