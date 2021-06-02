package cn.ussu.modules.dczx.mapper;

import cn.ussu.modules.dczx.entity.DcQuestionOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 题目的选项 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
public interface DcQuestionOptionMapper extends BaseMapper<DcQuestionOption> {

    // IPage<Map> findPage(Page page, @Param("param") Map param);

    /**
     * 根据问题id查询
     * @param questionId
     * @return
     */
    DcQuestionOption findByQuestionIdLong(Long questionId);

}
