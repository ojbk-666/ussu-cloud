package cc.ussu.modules.dczx.mapper;

import com.baomidou.dynamic.datasource.annotation.Slave;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slave
@Repository
public interface DcStatisticMapper {

    /**
     * 指定时间范围新增题目
     */
    Long countQuestionNumByRangeDate(@Param("start") Date start, @Param("end") Date end);

    /**
     * 指定范围每天的新增数量
     */
    List<Map> getQuestionNumByRangeDate(@Param("start") Date start, @Param("end") Date end);

    /**
     * 用户数
     */
    Long countUserNum();

    /**
     * 反馈数量
     */
    @Select("select count(*) from dc_feedback")
    Long countFeedback();

    /**
     * 各科目题目数
     */
    List<Map> courseQuestionNum();

    /**
     * 用户贡献题目数
     */
    List<Map> userQuestionNum(@Param("start") Date start, @Param("end") Date end);

    /**
     * 各题型题目数量
     */
    List<Map> countTopicQuestionNum();

    /**
     * 最近新增用户
     */
    List<Map> recentNewUser(@Param("size") Integer size);

    /**
     * 最近新增题目数量
     */
    List<Map> recentNewQuestion(@Param("dateFormatPattern") String groupByType, @Param("sysDateLineColumn") String sysDateLineColumn, @Param("start") Date start, @Param("end") Date end);

    /**
     * 最新访问数
     */
    List<Map> countRecentRequest(@Param("start") Date start, @Param("end") Date end);
}
