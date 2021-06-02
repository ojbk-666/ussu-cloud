package cn.ussu.modules.dczx.service.impl;

import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.mapper.DcInterfaceLogMapper;
import cn.ussu.modules.dczx.service.IDcInterfaceLogService;
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

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcInterfaceLog> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        String query7 = (String) param.get("query_result");
        qw.like(StrUtil.isNotBlank(query7), "result", query7);
        String query9 = (String) param.get("query_create_time");
        qw.like(StrUtil.isNotBlank(query9), "create_time", query9);
        String query10 = (String) param.get("query_create_by");
        qw.like(StrUtil.isNotBlank(query10), "create_by", query10);
        IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

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
