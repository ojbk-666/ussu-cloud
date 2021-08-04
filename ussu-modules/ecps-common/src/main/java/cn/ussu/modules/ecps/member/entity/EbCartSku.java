package cn.ussu.modules.ecps.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbCartSku对象", description="")
public class EbCartSku extends Model<EbCartSku> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车内SKU主键")
    @TableId(value = "cart_sku_id", type = IdType.AUTO)
    private Integer cartSkuId;

    @ApiModelProperty(value = "前台用户主键")
    private String ebUserId;

    @ApiModelProperty(value = "商品主键")
    private Integer itemId;

    @ApiModelProperty(value = "商品编号")
    private String itemNo;

    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "skuId")
    private Integer skuId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "SKU名称")
    private String skuName;

    @ApiModelProperty(value = "规格1：规格2：规格3。。。规格N，格式参见订单需求文档")
    private String skuSpec;

    @ApiModelProperty(value = "sku图片")
    private String skuImg;

    @ApiModelProperty(value = "(以分币为单位)市场价：所有价格相关的字段限定为9位有效数字以内（即1234567.89，相当于千万元以内），给前端用户显示时，小数永远保持2位，即整数价格后面显示为.00；若小数价格多于2位小数，则直接截掉2位小数右边的小数（不用四舍五入）；若小数为1位，如1元9角，则显示1.90")
    private BigDecimal marketPrice;

    private Integer offerGroupId;

    @ApiModelProperty(value = "活动名称")
    private String offerGroupName;

    @ApiModelProperty(value = "活动类型：1：购物送礼；2：优惠购机；3：购机返话费")
    private Integer offerType;

    @ApiModelProperty(value = "活动简称。用字母代表，如A1中的A")
    private String shortName;

    @ApiModelProperty(value = "活动档次简介")
    private String offerName;

    @ApiModelProperty(value = "活动档次数字编号，如A1中的1")
    private String shortName2;

    @ApiModelProperty(value = "(以分币为单位)购买时价格、预存话费。")
    private BigDecimal price;

    private BigDecimal totalPrice;

    private Integer quantity;

}
