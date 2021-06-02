package cn.ussu.modules.dczx.service.impl;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.dczx.entity.DcPaper;
import cn.ussu.modules.dczx.mapper.DcPaperMapper;
import cn.ussu.modules.dczx.model.param.DcPaperParam;
import cn.ussu.modules.dczx.service.IDcPaperService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @Override
    public ReturnPageInfo<DcPaper> findPage(DcPaperParam param) {
        LambdaQueryWrapper<DcPaper> qw = new LambdaQueryWrapper<>();
        IPage iPage = this.mapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    /*@Override
    @Transactional
    public void updateOne(DcPaper obj) {
        this.mapper.updateById(obj);
    }*/

}
