package cn.ussu.modules.dczx.mapper;

import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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
public interface DcPaperQuestionMapper extends BaseMapper<DcPaperQuestion> {

    IPage<Map> findPage(Page page, @Param("param") Map param);

    List<DcPaperQuestion> findByQuestionIds(@Param("questionIds") List questionIds);

    /**
     * 最近一个月新增的题目数量
     */
    List<Map> countRecentCreates();

    /**
     * 各科目题目数量
     */
    List<Map> countCourseNum();

}
