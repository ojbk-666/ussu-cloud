package cn.ussu.modules.ecps.skill.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="EbSeckillQua对象", description="")
public class EbSeckillQua extends Model<EbSeckillQua> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "seckill_qua_id", type = IdType.AUTO)
    private Integer seckillQuaId;

    @ApiModelProperty(value = "前台用户主键")
    private String ebUserId;

    private Integer seckillId;

    @ApiModelProperty(value = "最小销售单元主键")
    private Integer skuId;

    @ApiModelProperty(value = "秒杀到商品时的时间，用于和定时器做时间比较")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date seckillTime;

    @ApiModelProperty(value = "本次购买的数量，用于当资格作废时偿还秒杀库存和商品库存")
    private BigDecimal quantity;

    @ApiModelProperty(value = "当获取秒杀资格时为空，当成功下单后回填该字段，用于秒杀和订单的绑定")
    private Integer orderId;

    @ApiModelProperty(value = "是否失效：1为失效")
    private Integer isExpired;

}
