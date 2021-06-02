package cn.ussu.modules.dczx.service.impl;

import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.mapper.DcQuestionOptionMapper;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目的选项 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Service
public class DcQuestionOptionServiceImpl extends ServiceImpl<DcQuestionOptionMapper, DcQuestionOption> implements IDcQuestionOptionService {

    @Autowired
    private DcQuestionOptionMapper mapper;

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcQuestionOption> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

    /*@Override
    @Transactional
    public void addOne(DcQuestionOption obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(DcQuestionOption obj) {
        this.mapper.updateById(obj);
    }*/

}
