package cc.ussu.modules.ecps.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收货地址
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbShipAddr对象", description="收货地址")
public class EbShipAddr extends Model<EbShipAddr> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "收货人地址主键")
    @TableId(value = "ship_addr_id", type = IdType.AUTO)
    private Integer shipAddrId;

    @ApiModelProperty(value = "前台用户ID")
    private String ebUserId;

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

    @ApiModelProperty(value = "默认收货地址：若为默认收货地址则为1。")
    private Integer defaultAddr;

    @ApiModelProperty(value = "固定电话")
    private String fixedPhone;

}
