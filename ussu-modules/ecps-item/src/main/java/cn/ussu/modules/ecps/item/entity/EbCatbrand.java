package cn.ussu.modules.ecps.item.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
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
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbCatbrand对象", description="")
public class EbCatbrand extends Model<EbCatbrand> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("cat_id")
    private Integer catId;

    @TableField("brand_id")
    private Integer brandId;


}
