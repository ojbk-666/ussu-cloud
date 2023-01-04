package cc.ussu.modules.system.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class RegionSelectVO implements Serializable {

    private static final long serialVersionUID = 67624080416820361L;

    /**
     * 选项标签
     */
    private String label;

    /**
     * 选项的值
     */
    private String value;

    /**
     * 选项是否禁用
     */
    private boolean disabled;

    /**
     * 是否是叶子节点 即没有子节点
     */
    private boolean leaf;

    /**
     * 选项的子选项
     */
    private List<RegionSelectVO> children;

}
