package cn.ussu.modules.ecps.skill.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
@ApiModel(value="EbSeckillSku对象", description="")
public class EbSeckillSku extends Model<EbSeckillSku> {

    private static final long serialVersionUID = 1L;

    private Integer seckillId;

    @ApiModelProperty(value = "最小销售单元主键")
    private Integer skuId;

    @ApiModelProperty(value = "可以秒杀的商品数量，必须小于商品库存量")
    private Integer seckillCount;

    private BigDecimal seckillPrice;

    private BigDecimal orderNo;

}
