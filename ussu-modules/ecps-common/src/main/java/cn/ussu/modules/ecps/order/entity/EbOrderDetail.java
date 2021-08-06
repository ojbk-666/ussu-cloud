package cn.ussu.modules.ecps.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbOrderDetail对象", description="3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK")
public class EbOrderDetail extends Model<EbOrderDetail> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_detail_id", type = IdType.AUTO)
    private Integer orderDetailId;

    @ApiModelProperty(value = "订单主键")
    private Integer orderId;

    @ApiModelProperty(value = "商品主键")
    private Integer itemId;

    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "商品编号：自动生成，不可重复，添加完成后不可修改。编号规则：一级目录数字“1”+7位数递增，裸机起始编号   10000001。7位数递增满时自动升为8位数。	            编号根据EB_ITEM.CAT_TYPE获得，如实体商品为1（EB_ITEM.CAT_TYPE=1）	            （删除	            --实体起始编号：   10000001 虚拟起始编号：   20000001。	            --注意：商品是否为手机或号卡，用其所属的类目来区分，CAT_ID为1的为手机，CAT_ID为2的为号卡。）")
    private String itemNo;

    @ApiModelProperty(value = "最小销售单元主键")
    private Integer skuId;

    @ApiModelProperty(value = "货号")
    private String sku;

    @ApiModelProperty(value = "图片存储位置，1~5")
    private String skuImg;

    @ApiModelProperty(value = "SKU名称")
    private String skuName;

    @ApiModelProperty(value = "冗余EB_CAT里边的CAT_TYPE字段值，用以标示：0、不分类；1、实体；2、号卡；3、虚拟商品")
    private BigDecimal skuCatType;

    @ApiModelProperty(value = "规格1：规格2：规格3。。。规格N，格式参见订单需求文档")
    private String skuSpec;

    @ApiModelProperty(value = "(以分币为单位)市场价：所有价格相关的字段限定为9位有效数字以内（即1234567.89，相当于千万元以内），给前端用户显示时，小数永远保持2位，即整数价格后面显示为.00；若小数价格多于2位小数，则直接截掉2位小数右边的小数（不用四舍五入）；若小数为1位，如1元9角，则显示1.90")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "优惠额")
    private BigDecimal skuDiscount;

    @ApiModelProperty(value = "sku销售价")
    private BigDecimal skuPrice;

    private Integer offerGroupId;

    @ApiModelProperty(value = "活动名称")
    private String offerGroupName;

    @ApiModelProperty(value = "活动类型：1：购物送礼；2：优惠购机；3：购机返话费")
    private BigDecimal offerType;

    @ApiModelProperty(value = "活动简称。用字母代表，如A1中的A")
    private String shortName;

    @ApiModelProperty(value = "促销活动主键")
    private Integer offerId;

    @ApiModelProperty(value = "活动档次简介")
    private String offerName;

    @ApiModelProperty(value = "活动档次编号：起始编号： 50000001")
    private String offerNo;

    @ApiModelProperty(value = "活动档次数字编号，如A1中的1")
    private String shortName2;

    @ApiModelProperty(value = "0、混搭；1、裸机；2、号卡；3、营销案；4、团购裸机")
    private BigDecimal orderDetailType;

    private Integer merchantId;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "(以分币为单位)成交价格（即实际购买价格）。若是营销活动，则按公式sku_price+prepaid-sku_discount计算")
    private BigDecimal price;

    private String productId;

    private BigDecimal paymentPrice;

    private BigDecimal purchasePrice;

}
