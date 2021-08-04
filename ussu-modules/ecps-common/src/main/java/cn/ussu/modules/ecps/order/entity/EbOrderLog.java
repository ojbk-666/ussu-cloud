package cn.ussu.modules.ecps.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 订单日志	Name	Code	Comment	Default Value	Data Type	Length	
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbOrderLog对象", description="订单日志	Name	Code	Comment	Default Value	Data Type	Length	")
public class EbOrderLog extends Model<EbOrderLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单日志主键")
    @TableId(value = "order_log_id", type = IdType.AUTO)
    private Integer orderLogId;

    @ApiModelProperty(value = "订单主键")
    private Integer orderId;

    @ApiModelProperty(value = "操作前订单状态")
    private Integer stateBeforeOp;

    @ApiModelProperty(value = "操作类型")
    private String opType;

    @ApiModelProperty(value = "操作时间")
    private Date opTime;

    @ApiModelProperty(value = "操作人用户名")
    private String opUserName;

    @ApiModelProperty(value = "操作备注")
    private String opNotes;

}
