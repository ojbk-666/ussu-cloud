package cc.ussu.modules.system.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysConfigGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 备注
     */
    private String remark;

}
