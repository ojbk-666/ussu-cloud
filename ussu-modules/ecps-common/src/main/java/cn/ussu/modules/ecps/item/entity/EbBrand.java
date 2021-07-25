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
import java.util.List;

/**
 * <p>
 * 品牌
 * </p>
 *
 * @author liming
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbBrand对象", description="品牌")
public class EbBrand extends Model<EbBrand> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "品牌主键")
    @TableId(value = "brand_id", type = IdType.AUTO)
    private Integer brandId;

    @ApiModelProperty(value = "品牌名称：一般为该字段在前台显示的中文名称，也可含有英文或数字")
    @TableField("brand_name")
    private String brandName;

    @ApiModelProperty(value = "品牌描述")
    @TableField("brand_desc")
    private String brandDesc;

    @ApiModelProperty(value = "品牌图标：存储图标地址")
    @TableField("imgs")
    private String imgs;

    @ApiModelProperty(value = "品牌网址")
    @TableField("website")
    private String website;

    @ApiModelProperty(value = "前台显示排序")
    @TableField("brand_sort")
    private BigDecimal brandSort;

    @TableField(exist = false)
    private String label;

    public EbBrand setBrandName(String brandName) {
        this.brandName = brandName;
        this.label = brandName;
        return this;
    }

    @TableField(exist = false)
    private Integer value;

    public EbBrand setBrandId(Integer brandId) {
        this.brandId = brandId;
        this.value = brandId;
        return this;
    }

    @TableField(exist = false)
    private List<Integer> catIds;

    @TableField(exist = false)
    private List<EbCat> catList;

}
