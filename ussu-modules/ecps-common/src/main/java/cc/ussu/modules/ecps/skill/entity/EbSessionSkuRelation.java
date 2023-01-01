package cc.ussu.modules.ecps.skill.entity;

import cc.ussu.modules.ecps.item.entity.EbSku;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@ApiModel(value="EbSessionSkuRelation对象", description="")
public class EbSessionSkuRelation extends Model<EbSessionSkuRelation> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀与商品关系表主键")
    @TableId(value = "relation_id", type = IdType.AUTO)
    private Integer relationId;

    @ApiModelProperty(value = "秒杀场次主键")
    private Integer sessionId;

    @ApiModelProperty(value = "最小销售单元id")
    private Integer skuId;

    @ApiModelProperty(value = "秒杀价格")
    private BigDecimal seckillPrice;

    @ApiModelProperty(value = "秒杀商品的库存")
    private Integer seckillCount;

    @ApiModelProperty(value = "秒杀商品限购的数量")
    private Integer seckillLimit;

    @TableField(exist = false)
    private EbSku ebSku;

    @TableField(exist = false)
    private Long startTime;

    @TableField(exist = false)
    private Long endTime;

    @TableField(exist = false)
    private String randomCode;

}
