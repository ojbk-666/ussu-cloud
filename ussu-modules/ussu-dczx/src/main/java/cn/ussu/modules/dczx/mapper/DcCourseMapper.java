package cn.ussu.modules.dczx.mapper;

import cn.ussu.modules.dczx.entity.DcCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
public interface DcCourseMapper extends BaseMapper<DcCourse> {

    // IPage<Map> findPage(Page page, @Param("param") Map param);

    DcCourse findByCourseId(@Param("courseId") String courseId);

}
