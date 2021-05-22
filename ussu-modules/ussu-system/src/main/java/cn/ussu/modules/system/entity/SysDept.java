package cn.ussu.modules.system.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
/**
 * <p>
 * 部门
 * </p>
 *
 * @author liming
 * @since 2020-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysDept对象", description="部门")
public class SysDept extends Model<SysDept> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "上级部门")
    private String parentId;

    @ApiModelProperty(value = "部门简称")
    private String name;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "部门电话")
    private String phone;

    @ApiModelProperty(value = "部门排序")
    private Integer sort;

    @ApiModelProperty(value = "部门路径(/总部/山东/济南/综合部)")
    private String path;

    @ApiModelProperty(value = "部门级别")
    private Integer level;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除：1，是；0，否")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

    @ApiModelProperty(value = "乐观锁")
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

}
