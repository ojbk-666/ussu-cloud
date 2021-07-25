package cn.ussu.modules.ecps.item.entity;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
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
import java.util.List;

/**
 * <p>
 * 商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号            
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbFeature对象", description="商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号            ")
public class EbFeature extends Model<EbFeature> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品属性主键")
    @TableId(value = "feature_id", type = IdType.AUTO)
    private Integer featureId;

    private Integer groupId;

    private Integer catId;

    @ApiModelProperty(value = "属性名称")
    private String featureName;

    @ApiModelProperty(value = "是否为规格：0.为否 1.为是")
    private Integer isSpec;

    @ApiModelProperty(value = "是否为筛选：0为否 1为是")
    private Integer isSelect;

    @ApiModelProperty(value = "是否前台显示：0.为否 1.为是")
    private Integer isShow;

    @ApiModelProperty(value = "属性可选值：用英文逗号分割的可选值，可选值里不许有逗号")
    private String selectValues;

    public EbFeature setSelectValues(String selectValues) {
        this.selectValues = selectValues;
        this.selectValueList = (StrUtil.split(selectValues, StrPool.COMMA));
        return this;
    }

    @TableField(exist = false)
    private List<String> selectValueList;

    @ApiModelProperty(value = "录入方式：1.树状菜单，2.单选，3.复选，4.文本框")
    private Integer inputType;

    @ApiModelProperty(value = "前台显示排序")
    private Integer featureSort;

    @TableField(exist = false)
    private String label;

    public EbFeature setFeatureName(String featureName) {
        this.featureName = featureName;
        this.label = featureName;
        return this;
    }

    @TableField(exist = false)
    private Integer value;

    public EbFeature setFeatureId(Integer featureId) {
        this.featureId = featureId;
        this.value = featureId;
        return this;
    }

    @TableField(exist = false)
    private String groupName;

    @TableField(exist = false)
    private String catName;

}
