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
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbFeatureGroup对象", description="")
public class EbFeatureGroup extends Model<EbFeatureGroup> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品属性组主键")
    @TableId(value = "group_id", type = IdType.AUTO)
    private Integer groupId;

    private Integer catId;

    private String groupName;

    private String groupDesc;

    private Integer groupSort;

    @TableField(exist = false)
    private String label;

    public EbFeatureGroup setGroupName(String groupName) {
        this.groupName = groupName;
        this.label = groupName;
        return this;
    }

    @TableField(exist = false)
    private Integer value;

    public EbFeatureGroup setGroupId(Integer groupId) {
        this.groupId = groupId;
        this.value = groupId;
        return this;
    }

    @TableField(exist = false)
    private List<Integer> featureIdList;

    @TableField(exist = false)
    private List<EbFeature> featureList;

}
