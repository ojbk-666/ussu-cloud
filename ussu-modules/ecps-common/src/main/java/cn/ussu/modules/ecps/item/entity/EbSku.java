package cn.ussu.modules.ecps.item.entity;

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
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbSku对象", description="最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN")
public class EbSku extends Model<EbSku> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "最小销售单元主键")
    @TableId(value = "sku_id", type = IdType.AUTO)
    private Integer skuId;

    private Integer itemId;

    @ApiModelProperty(value = "货号")
    private String sku;

    @ApiModelProperty(value = "(以分币为单位)售价、号卡预存话费：所有价格相关的字段限定为9位有效数字以内（即1234567.89，相当于千万元以内），给前端用户显示时，小数永远保持2位，即整数价格后面显示为.00；	            若小数价格多于2位小数，则直接截掉2位小数右边的小数（不用四舍五入）；若小数为1位，如1元9角，则显示1.90")
    private BigDecimal skuPrice;

    @ApiModelProperty(value = "上下架状态：1.为上架；0.为下架")
    private Integer showStatus;

    @ApiModelProperty(value = "库存")
    private Integer stockInventory;

    @ApiModelProperty(value = "购买上限")
    private Integer skuUpperLimit;

    @ApiModelProperty(value = "货位：可以输入特殊字符，需转义")
    private String location;

    @ApiModelProperty(value = "图片存储位置，1~5")
    private String skuImg;

    @ApiModelProperty(value = "前台显示排序")
    private Integer skuSort;

    @ApiModelProperty(value = "SKU名称")
    private String skuName;

    @ApiModelProperty(value = "(以分币为单位)市场价：所有价格相关的字段限定为9位有效数字以内（即1234567.89，相当于千万元以内），给前端用户显示时，小数永远保持2位，即整数价格后面显示为.00；若小数价格多于2位小数，则直接截掉2位小数右边的小数（不用四舍五入）；若小数为1位，如1元9角，则显示1.90")
    private BigDecimal marketPrice;

    private Date createTime;

    private Date updateTime;

    private String createUserId;

    private String updateUserId;

    private Integer originalSkuId;

    @ApiModelProperty(value = "0是历史记录;1最新")
    private Integer lastStatus;

    private Integer merchantId;

    @ApiModelProperty(value = "0为赠品；1为普通SKU")
    private Integer skuType;

    private Integer sales;

    /**
     * 规格列表
     */
    @TableField(exist = false)
    private List<EbSpecValue> specList;

    /**
     * 图片列表
     */
    @TableField(exist = false)
    private List<EbSkuImg> skuImgList;

    @TableField(exist = false)
    private EbItem item;

}
