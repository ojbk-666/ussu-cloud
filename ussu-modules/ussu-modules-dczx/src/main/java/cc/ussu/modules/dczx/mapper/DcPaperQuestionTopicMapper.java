package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import com.baomidou.dynamic.datasource.annotation.Slave;
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
@Slave
public interface DcPaperQuestionTopicMapper extends BaseMapper<DcPaperQuestionTopic> {

    // DcPaperQuestionTopic findById(@Param("id") Long id);

    List<DcPaperQuestionTopic> findByTopicTypeIds(@Param("ids") List topicTypeIdList);

    DcPaperQuestionTopic selectByFullTopicTypeCd(@Param("fullTopicTypeCd") String fullTopicTypeCd);

}
