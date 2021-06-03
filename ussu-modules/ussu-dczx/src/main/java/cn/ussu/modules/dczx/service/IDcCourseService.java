package cn.ussu.modules.dczx.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.dczx.entity.DcCourse;
import cn.ussu.modules.dczx.model.param.DcCourseParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
public interface IDcCourseService extends IService<DcCourse> {

    /**
     * 分页查询
     */
    ReturnPageInfo<DcCourse> findPage(DcCourseParam param);

    /**
     * 新增
     */
    // void addOne(DcCourse obj);

    /**
     * 修改
     */
    // void updateOne(DcCourse obj);

}
