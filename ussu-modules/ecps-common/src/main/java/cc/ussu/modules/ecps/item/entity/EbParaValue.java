package cc.ussu.modules.ecps.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbParaValue对象", description="")
public class EbParaValue extends Model<EbParaValue> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "参数值主键")
    @TableId(value = "para_id", type = IdType.AUTO)
    private Integer paraId;

    private Integer itemId;

    private Integer featureId;

    @ApiModelProperty(value = "参数值")
    private String paraValue;

}
