package cn.ussu.modules.ecps.skill.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2021-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbSeckillSession对象", description="")
public class EbSeckillSession extends Model<EbSeckillSession> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀场次主键")
    @TableId(value = "session_id", type = IdType.AUTO)
    private Integer sessionId;

    @ApiModelProperty(value = "秒杀场次名称")
    private String sessionName;

    @ApiModelProperty(value = "秒杀场次开始时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date startTime;

    @ApiModelProperty(value = "秒杀场次结束时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date endTime;

    @TableField(exist = false)
    private List<EbSessionSkuRelation> relationList;

}
