package cn.ussu.modules.dczx.service;

import cn.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface IDcPaperQuestionTopicService extends IService<DcPaperQuestionTopic> {

    public static final String DC_PAPER_QUESTION_TOPIC_ = "dczx:dc_paper_question_topic:";

    /**
     * 分页查询
     */
    // LayuiPageInfo findPage(Map param);

    /**
     * 通过topic_type_id查询，会缓存
     */
    List<DcPaperQuestionTopic> findByTopicTypeIds(String... topicTypeId);

    /**
     * 新增
     */
    // void addOne(DcPaperQuestionTopic obj);

    /**
     * 修改
     */
    // void updateOne(DcPaperQuestionTopic obj);

}
