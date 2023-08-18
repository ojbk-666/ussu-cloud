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
 * 部门
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dept")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 上级部门
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 部门简称
     */
    @NotBlank(message = "名称不能为空")
    @TableField("name")
    private String name;

    /**
     * 部门全称
     */
    @TableField("full_name")
    private String fullName;

    /**
     * 部门排序
     */
    @TableField("dept_sort")
    private Integer deptSort;

    /**
     * 部门路径(/总部/山东/济南/综合部)
     */
    @TableField("path")
    private String path;

    /**
     * 部门级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否删除：1，是；0，否
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    @TableField(exist = false)
    private List<SysDept> children;

    @TableField(exist = false)
    private boolean hasChildren;

}
