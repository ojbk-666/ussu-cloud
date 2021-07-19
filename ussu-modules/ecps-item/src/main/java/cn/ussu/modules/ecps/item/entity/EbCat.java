package cn.ussu.modules.ecps.item.entity;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加
 * </p>
 *
 * @author liming
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbCat对象", description="商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加")
public class EbCat extends Model<EbCat> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类目主键。商品本身并不记录自己所属的商品种类（即：是属于手机还是号卡），而是通过其所属的类目来确定。就是说，系统的类目会初始化两种顶级类目：一种是手机（CAT_ID号是1），另一种是号卡（CAT_ID号是2）。系统需要根据商品所属的类目的ID号，来判断该商品是手机还是号卡。注意，商品类目表的CAT_ID号3至99是预留给系统将来的顶级类目的，顶级类目下面的二级类目CAT_ID从100开始编号（需要修改ORACLE的SEQUENCE）。编辑商品时，在手机类目中的所添加的商品，不可以更改到号卡类目中，反之亦然。")
    @TableId(value = "cat_id", type = IdType.AUTO)
    private Integer catId;

    @ApiModelProperty(value = "类目名称")
    @TableField("cat_name")
    private String catName;

    @ApiModelProperty(value = "类目描述")
    @TableField("cat_desc")
    private String catDesc;

    @ApiModelProperty(value = "父类目CAT_ID。0表示顶级类目，没有父类目。")
    @TableField("parent_id")
    private Integer parentId;

    private String idPath;

    @TableField("cat_level")
    private Integer catLevel;

    @ApiModelProperty(value = "前台显示排序")
    @TableField("cat_sort")
    private BigDecimal catSort;

    @ApiModelProperty(value = "关键词")
    @TableField("keywords")
    private String keywords;

    @ApiModelProperty(value = "静态化访问路径")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "分类标识：该分类的唯一标识，用于分类路径和模板标识，如用于生成前台静态页面，例如手机分类的标识为\"SHOUJI\",则置成HTTP://127.0.0.1/SHOUJI")
    @TableField("mark")
    private String mark;

    @ApiModelProperty(value = "是否前台显示")
    @TableField("isdisplay")
    private BigDecimal isdisplay;

    @ApiModelProperty(value = "静态化面包屑导航全路径。如手机顶级分类的FULL_PATH是\"/1/\"")
    @TableField("full_path")
    private String fullPath;

    @ApiModelProperty(value = "0、不分类；1、实体；2、号卡；3、虚拟商品")
    @TableField("cat_type")
    private BigDecimal catType;

    @TableField(exist = false)
    private String label;

    public EbCat setCatName(String catName) {
        this.catName = catName;
        this.label = catName;
        return this;
    }

    @TableField(exist = false)
    private Integer value;

    public EbCat setCatId(Integer catId) {
        this.catId = catId;
        this.value = catId;
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<EbCat> children;

    @TableField(exist = false)
    private List<Integer> idPathList;

    public EbCat setIdPath(String idPath) {
        this.idPath = idPath;
        int[] ints = StrUtil.splitToInt(idPath, StrPool.COMMA);
        this.idPathList = Arrays.stream(ints).boxed().collect(Collectors.toList());
        return this;
    }
}
