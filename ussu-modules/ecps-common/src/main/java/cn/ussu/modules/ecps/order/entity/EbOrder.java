package cn.ussu.modules.ecps.order.entity;

import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.modules.ecps.common.constants.OrderStatus;
import com.baomidou.mybatisplus.annotation.IdType;
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单。包括实体商品和虚拟商品的订单
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbOrder对象", description="订单。包括实体商品和虚拟商品的订单")
public class EbOrder extends Model<EbOrder> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单主键")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty(value = "前台用户主键")
    private String ebUserId;

    private String username;

    @ApiModelProperty(value = "订单号：订单号规则：订单生成日期+6位数，递增. 订单起始编号：111024000001. 后六位数一旦满，则后六位数递增为七位数")
    private String orderNum;

    @ApiModelProperty(value = "支付方式：1: 在线支付；2: 货到付款；3: 营业厅付款；4:邮局汇款")
    private Integer payment;

    @ApiModelProperty(value = "支付平台：1: 工商银行; 2: 建设银行; 3: 招商银行")
    private Integer payPlatform;

    @ApiModelProperty(value = "送货时间：1: 只工作日送货(双休日，假日不用送); 2: 工作日、双休日、假日均可送货; 3: 只双休日、假日送货(工作日送货)")
    private Integer delivery;

    @ApiModelProperty(value = "送货前电话确认：0: 否; 1: 是")
    private Integer isConfirm;

    @ApiModelProperty(value = "订单总金额：各订单项的售价之和，包括运费等。")
    private BigDecimal orderSum;

    @ApiModelProperty(value = "运费")
    private BigDecimal shipFee;

    @ApiModelProperty(value = "未付款(待付款)=0;已付款(付款成功)=1;待退款=2;退款成功=3;退款失败=4;撤销成功=5;撤销失败=6;关闭=7;")
    private Integer isPaid;

    @ApiModelProperty(value = "订单状态。参见类EbOrderStateConstants.java")
    private Integer orderState;

    public EbOrder setOrderState(Integer orderState) {
        this.orderState = orderState;
        // 转换名称
        this.orderStateStr = OrderStatus.getStatusByCode(orderState);
        return this;
    }

    @TableField(exist = false)
    private String orderStateStr;

    @ApiModelProperty(value = "货到付款方式：1: 现金；2: POS刷卡; 3: 支票")
    private Integer paymentCash;

    @ApiModelProperty(value = "配送商ID")
    private Integer distriId;

    @ApiModelProperty(value = "送货方式：1：快递；2：营业厅自提；3：平邮")
    private Integer deliveryMethod;

    @ApiModelProperty(value = "支付号")
    private String paymentNo;

    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date orderTime;

    @ApiModelProperty(value = "付款时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date payTime;

    @ApiModelProperty(value = "到帐时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date depositTime;

    @ApiModelProperty(value = "成功时间")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date successTime;

    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    private Date updateTime;

    @ApiModelProperty(value = "前台用户删除订单标记。1：是；0：否")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

    @ApiModelProperty(value = "前台用户删除订单标记。1：是；0：否")
    private Integer isDisplay;

    @ApiModelProperty(value = "备注")
    private String notes;

    @ApiModelProperty(value = "收货人姓名")
    private String shipName;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "地区")
    private String district;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "街道地址")
    private String addr;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "发票抬头：0: 不需要发票；1: 个人；2: 单位")
    private Integer payable;

    @ApiModelProperty(value = "单位名称")
    private String company;

    @ApiModelProperty(value = "发票内容：1: 明细；2: 办公用品")
    private Integer contents;

    @ApiModelProperty(value = "物流编号。")
    private String deliveryNo;

    @ApiModelProperty(value = "市区代码（冗余自EB_CITY_AREA表）")
    private String areaCode;

    @ApiModelProperty(value = "市区名称（冗余自EB_CITY_AREA表）")
    private String areaName;

    @ApiModelProperty(value = "是否已打印发票。0-未打印；1-已打印")
    private Integer isPrint;

    @ApiModelProperty(value = "0、混搭；1、裸机；2、号卡；3、营销案；4、团购裸机")
    private Integer orderType;

    @ApiModelProperty(value = "固定电话")
    private String fixedPhone;

    private String attachFile;

    private Integer returnType;

    @ApiModelProperty(value = "如果订单是秒杀时填此字段，为了在前台‘我的订单‘中能够链接到下单的秒杀页面")
    private Integer seckillId;

    private String orderExt1;

    private String orderExt2;

    private String orderExt3;

    private String orderExt4;

    private String orderExt5;

    @TableField(exist = false)
    private String cartIdStr;

    @TableField(exist = false)
    private List<EbOrderDetail> orderDetailList;


}
