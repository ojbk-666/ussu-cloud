package cn.ussu.modules.dczx.service;

import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
    // LayuiPageInfo findPage(Map param);

    /**
     * 通过question_id查询
     * 缓存的接口
     *
     * @param questionIds questionIds
     * @return
     */
    List<DcPaperQuestion> findByQuestionIds(String... questionIds);

    /**
     * 新增
     */
    boolean addOne(DcPaperQuestion obj);

    /**
     * 修改
     */
    // void updateOne(DcPaperQuertion obj);

}
