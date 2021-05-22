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

import java.time.LocalDateTime;
/**
 * <p>
 * 附件表
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysAttach对象", description="附件表")
public class SysAttach extends Model<SysAttach> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId
    private String id;

    @ApiModelProperty(value = "附件组唯一ID")
    private String groupId;

    @ApiModelProperty(value = "原始文件名")
    private String fileName;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件（字节）")
    private String fileSize;

    @ApiModelProperty(value = "文件扩展名")
    private String fileExt;

    @ApiModelProperty(value = "排序")
    private Integer attachSort;

    @ApiModelProperty(value = "模块代码")
    private String attachCode;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否删除：1，是；0，否")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
