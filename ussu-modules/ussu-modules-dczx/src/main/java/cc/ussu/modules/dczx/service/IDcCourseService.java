package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcCourse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
public interface IDcCourseService extends IService<DcCourse> {

    DcCourse getByServiceCourseVersId(String c);

}
