package cn.ussu.modules.dczx.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.dczx.entity.DcCourse;
import cn.ussu.modules.dczx.mapper.DcCourseMapper;
import cn.ussu.modules.dczx.model.param.DcCourseParam;
import cn.ussu.modules.dczx.service.IDcCourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @Override
    public ReturnPageInfo<DcCourse> findPage(DcCourseParam param) {
        LambdaQueryWrapper<DcCourse> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(DcCourse::getCreateTime)
                .like(StrUtil.isNotBlank(param.getCourseId()), DcCourse::getCourseId, param.getCourseId())
                .like(StrUtil.isNotBlank(param.getCourseName()), DcCourse::getCourseName, param.getCourseName());
        IPage iPage = this.mapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

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
