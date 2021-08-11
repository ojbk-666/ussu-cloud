package cn.ussu.modules.ecps.skill.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
@ApiModel(value="EbSeckill对象", description="")
public class EbSeckill extends Model<EbSeckill> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "seckill_id", type = IdType.AUTO)
    private Integer seckillId;

    @ApiModelProperty(value = "1秒杀活动（根据产品的说明，该类型和团购里的不同，该类型指以后秒杀可能会存在多种秒杀类型）")
    private Integer activityType;

    private String seckillTitle;

    private String seckillImg;

    private String giftInfo;

    private BigDecimal seckillOrder;

    private BigDecimal noticeOrder;

    private BigDecimal sales;

    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date startTime;

    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date endTime;

    @ApiModelProperty(value = "0-不推荐；1-首页推荐；")
    private Integer isRecommend;

    @ApiModelProperty(value = "0为不限制，大于0的整数，即秒到该商品的次数")
    private BigDecimal limitSeckillTimes;

    @ApiModelProperty(value = "0为不限制，大于0的整数，即秒杀时候填的购买数量")
    private BigDecimal limitSeckillCount;

    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

}
