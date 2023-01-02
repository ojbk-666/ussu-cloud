package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
@Repository
public interface DcPaperQuestionMapper extends BaseMapper<DcPaperQuestion> {

    /**
     * 分页查询
     */
    IPage<DcPaperQuestionVO> findPage(Page page, @Param("param") Map param);

    /**
     * 根据问题id查询多个，如果问题id不存在则返回全部记录，不支持分页
     *
     * @param questionIds 问题id
     */
    List<DcPaperQuestionVO> findWithOptions(@Param("questionIds") List<String> questionIds);

    /**
     * 更新创建人为userid
     */
    void updateQuestionCreateBy();

}
