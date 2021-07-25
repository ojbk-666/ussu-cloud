package cn.ussu.modules.ecps.item.entity;

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
@ApiModel(value="EbItemClob对象", description="")
public class EbItemClob extends Model<EbItemClob> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer itemId;

    @ApiModelProperty(value = "商品描述：富文本编辑器；可以输入特殊字符，需转义；无字符限制")
    private String itemDesc;

    @ApiModelProperty(value = "包装清单")
    private String packingList;

}
