package cn.ussu.modules.dczx.service.impl;

import cn.ussu.modules.dczx.entity.DcCourse;
import cn.ussu.modules.dczx.mapper.DcCourseMapper;
import cn.ussu.modules.dczx.service.IDcCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@Service
public class DcCourseServiceImpl extends ServiceImpl<DcCourseMapper, DcCourse> implements IDcCourseService {

    @Autowired
    private DcCourseMapper mapper;

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcCourse> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        String query2 = (String) param.get("query_course_id");
        qw.like(StrUtil.isNotBlank(query2), "course_id", query2);
        String query4 = (String) param.get("query_course_name");
        qw.like(StrUtil.isNotBlank(query4), "course_name", query4);
        IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

    /*@Override
    @Transactional
    public void addOne(DcCourse obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(DcCourse obj) {
        this.mapper.updateById(obj);
    }*/

}
