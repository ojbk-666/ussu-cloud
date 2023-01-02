package cc.ussu.modules.dczx.mapper;

import cc.ussu.modules.dczx.entity.DcCourse;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@Slave
public interface DcCourseMapper extends BaseMapper<DcCourse> {

}
