package cn.ussu.modules.dczx.mapper;

import cn.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface DcPaperQuestionTopicMapper extends BaseMapper<DcPaperQuestionTopic> {

    // IPage<Map> findPage(Page page, @Param("param") Map param);

    DcPaperQuestionTopic findById(@Param("id") Long id);

    List<DcPaperQuestionTopic> findByTopicTypeIds(@Param("ids") List topicTypeIdList);
}
