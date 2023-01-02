package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcQuestionOption;
import cc.ussu.modules.dczx.entity.vo.DcQuestionOptionVO;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 题目的选项 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
public interface DcQuestionOptionMapper extends BaseMapper<DcQuestionOption> {

    /**
     * 根据问题id查询
     * @param questionId
     * @return
     */
    List<DcQuestionOption> selectListByQuestionId(@Param("questionId") String questionId);

    List<DcQuestionOptionVO> selectVOListByQuestionId(@Param("questionId") String questionId);

}
