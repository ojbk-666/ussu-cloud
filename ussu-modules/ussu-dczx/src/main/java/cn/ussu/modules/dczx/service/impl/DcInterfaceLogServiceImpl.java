package cn.ussu.modules.dczx.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.mapper.DcInterfaceLogMapper;
import cn.ussu.modules.dczx.model.param.DcInterfaceLogParam;
import cn.ussu.modules.dczx.service.IDcInterfaceLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口日志 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Service
public class DcInterfaceLogServiceImpl extends ServiceImpl<DcInterfaceLogMapper, DcInterfaceLog> implements IDcInterfaceLogService {

    @Autowired
    private DcInterfaceLogMapper mapper;

    @Override
    public ReturnPageInfo<DcInterfaceLog> findPage(DcInterfaceLogParam param) {
        LambdaQueryWrapper<DcInterfaceLog> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(DcInterfaceLog::getCreateTime)
                .eq(StrUtil.isNotBlank(param.getUserid()), DcInterfaceLog::getUserid, param.getUserid())
                .eq(param.getResult() != null, DcInterfaceLog::getResult, param.getResult())
                .select(DcInterfaceLog::getId, DcInterfaceLog::getUrl,
                        DcInterfaceLog::getRemarks, DcInterfaceLog::getCreateTime,
                        DcInterfaceLog::getUserid);
        IPage iPage = this.mapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    /*@Override
    @Transactional
    public void addOne(DcInterfaceLog obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(DcInterfaceLog obj) {
        this.mapper.updateById(obj);
    }*/

}
