package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目 服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface IDcPaperQuestionService extends IService<DcPaperQuestion> {

    public static final String DC_PAPER_QUESTION_ = "dczx:dc_paper_question:";

    /**
     * 分页查询
     */
    IPage findPage(Map param);

    /**
     * 通过question_id查询
     * 缓存的接口
     *
     * @param questionIds questionIds
     * @return
     */
    List<DcPaperQuestionVO> findByQuestionIds(String... questionIds);

}
