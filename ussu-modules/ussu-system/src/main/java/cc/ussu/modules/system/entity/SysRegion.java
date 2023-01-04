package cc.ussu.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 省市区区域表
 * </p>
 *
 * @author liming
 * @since 2022-01-23 17:45:37
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_region")
public class SysRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域主键
     */
    @TableId("id")
    private String id;

    /**
     * 区域编码，身份证号前6位
     */
    @NotBlank(message = "区域编码不能为空")
    @TableField("code")
    private Integer code;

    /**
     * 区域名称
     */
    @NotBlank(message = "名称不能为空")
    @TableField("name")
    private String name;

    /**
     * 区域上级标识
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * id路径
     */
    @TableField("path")
    private String path;

    /**
     * 地名简称
     */
    @TableField("simple_name")
    private String simpleName;

    /**
     * 区域等级
     */
    @TableField("level")
    private Integer level;

    /**
     * 区号，即固定电话前拼接的
     */
    @TableField("citycode")
    private String citycode;

    /**
     * 邮政编码
     */
    @TableField("yzcode")
    private String yzcode;

    /**
     * 组合名称
     */
    @TableField("mername")
    private String mername;

    /**
     * 经度
     */
    @TableField("lng")
    private Float lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private Float lat;

    /**
     * 拼音
     */
    @TableField("pinyin")
    private String pinyin;

    /**
     * 是否删除 1删除 0正常
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    @TableField(exist = false)
    private boolean hasChildren;

    @TableField(exist = false)
    private List<SysRegion> children;

    /**
     * 选项标签
     */
    @TableField(exist = false)
    private String label;

    /**
     * 选项的值
     */
    @TableField(exist = false)
    private String value;

    /**
     * 选项是否禁用
     */
    @TableField(exist = false)
    private boolean disabled;

    /**
     * 是否是叶子节点 即没有子节点
     */
    @TableField(exist = false)
    private boolean leaf;

}
