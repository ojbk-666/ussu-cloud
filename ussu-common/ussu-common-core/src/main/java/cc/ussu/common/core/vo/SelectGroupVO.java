package cc.ussu.common.core.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SelectGroupVO {

    /**
     * 分组名称
     */
    private String label;
    /**
     * 分组下的选项
     */
    private List<SelectVO> options;

}
