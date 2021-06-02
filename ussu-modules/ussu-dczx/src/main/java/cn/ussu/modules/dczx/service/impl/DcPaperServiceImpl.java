package cn.ussu.modules.dczx.service.impl;

import cn.ussu.modules.dczx.entity.DcPaper;
import cn.ussu.modules.dczx.mapper.DcPaperMapper;
import cn.ussu.modules.dczx.service.IDcPaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * paper 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Service
public class DcPaperServiceImpl extends ServiceImpl<DcPaperMapper, DcPaper> implements IDcPaperService {

    @Autowired
    private DcPaperMapper mapper;

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcPaper> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

    /*@Override
    @Transactional
    public void addOne(DcPaper obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(DcPaper obj) {
        this.mapper.updateById(obj);
    }*/

}
