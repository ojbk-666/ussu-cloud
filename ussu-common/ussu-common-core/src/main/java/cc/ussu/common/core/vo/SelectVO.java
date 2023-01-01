package cc.ussu.common.core.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SelectVO<T> implements Serializable {

    private static final long serialVersionUID = -9020911156334755107L;

    /**
     * 选项的标签
     */
    private String label;

    /**
     * 选项的值
     */
    private T value;

    /**
     * 是否禁用该选项
     */
    private Boolean disabled = false;

}
