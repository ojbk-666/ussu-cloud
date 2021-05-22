package cn.ussu.modules.system.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 省市区区域表
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysArea对象", description="省市区区域表")
public class SysArea extends Model<SysArea> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域主键")
    @TableId
    private Integer id;

    @ApiModelProperty(value = "区域编码，身份证号前6位")
    private Integer code;

    @ApiModelProperty(value = "区域名称")
    private String name;

    @ApiModelProperty(value = "区域上级标识")
    private Integer parentId;

    private String path;

    @ApiModelProperty(value = "地名简称")
    private String simpleName;

    @ApiModelProperty(value = "区域等级")
    private Integer level;

    @ApiModelProperty(value = "区号，即固定电话前拼接的")
    private String citycode;

    @ApiModelProperty(value = "邮政编码")
    private String yzcode;

    @ApiModelProperty(value = "组合名称")
    private String mername;

    @ApiModelProperty(value = "经度")
    private Float lng;

    @ApiModelProperty(value = "纬度")
    private Float lat;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "是否删除 1删除 0正常")
    @TableLogic
    private Boolean delFlag;

}
