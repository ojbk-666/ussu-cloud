package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.mapper.DcCourseMapper;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@Slave
@Service
public class DcCourseServiceImpl extends ServiceImpl<DcCourseMapper, DcCourse> implements IDcCourseService {

    @Override
    public DcCourse getByServiceCourseVersId(String c) {
        if (StrUtil.isBlank(c)) {
            return null;
        }
        return super.getOne(Wrappers.lambdaQuery(DcCourse.class).eq(DcCourse::getServiceCourseVersId, c));
    }
}
