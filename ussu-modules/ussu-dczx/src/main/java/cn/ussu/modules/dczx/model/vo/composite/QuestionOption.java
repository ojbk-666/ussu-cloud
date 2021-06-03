package cn.ussu.modules.dczx.model.vo.composite;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 选项
 *
 * @author liming
 * @date 2021-06-03 20:34
 */
@Data
@Accessors(chain = true)
public class QuestionOption implements Serializable {
    private static final long serialVersionUID = -1827964742029791959L;

    private String OPTION_ID;
    private String OPTION_CONTENT;
    private String OPTION_TYPE;
    private String ISTRUE;
    private Object selected;

}
