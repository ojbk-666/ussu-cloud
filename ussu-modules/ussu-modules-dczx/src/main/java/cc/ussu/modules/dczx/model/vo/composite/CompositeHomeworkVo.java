package cc.ussu.modules.dczx.model.vo.composite;

import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 综合作业回调bean
 *
 * @author liming
 * @date 2021-06-03 20:29
 */
@Data
@Accessors(chain = true)
public class CompositeHomeworkVo implements Serializable {

    private static final long serialVersionUID = -1941514697456946699L;

    private String PAPERID;
    private List<PaperQuestion> PAPER_QUESTIONS;

}
