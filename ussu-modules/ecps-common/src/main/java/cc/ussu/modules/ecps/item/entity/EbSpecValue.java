package cc.ussu.modules.ecps.item.entity;

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

/**
 * <p>
 * 规格值（与价格有关）
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbSpecValue对象", description="规格值（与价格有关）")
public class EbSpecValue extends Model<EbSpecValue> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规格值主键")
    @TableId(value = "spec_id", type = IdType.AUTO)
    private Integer specId;

    private Integer skuId;

    private Integer featureId;

    @ApiModelProperty(value = "规格值")
    private String specValue;

    // 属性名称
    @TableField(exist = false)
    private String featureName;

}
