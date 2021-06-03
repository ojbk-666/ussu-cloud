package cn.ussu.modules.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysLog;
import cn.ussu.modules.system.mapper.SysLogMapper;
import cn.ussu.modules.system.model.param.SysLogParam;
import cn.ussu.modules.system.model.result.SysLogResult;
import cn.ussu.modules.system.service.ISysLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Autowired
    private SysLogMapper mapper;

    @Override
    public ReturnPageInfo<SysLogResult> findPage(SysLogParam param) {
        LambdaQueryWrapper<SysLog> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(param.getLogName()), SysLog::getLogName, param.getLogName())
                .eq(StrUtil.isNotBlank(param.getRemoteIp()), SysLog::getRemoteIp, param.getRemoteIp())
                .le(param.getCreateTimeEnd() != null, SysLog::getRequestTime, param.getCreateTimeEnd())
                .ge(param.getCreateTimeStart() != null, SysLog::getRequestTime, param.getCreateTimeStart())
                .orderByDesc(SysLog::getRequestTime);
        Page page = mapper.selectPage(DefaultPageFactory.getPage(), qw);
        List<SysLogResult> records = page.getRecords();
        List<SysLogResult> list = DefaultPageFactory.convertToResult(records, SysLogResult.class);
        for (SysLogResult r : list) {
            Long executeTime = r.getExecuteTime();
            r.setExecuteTimeStr(DateUtil.formatBetween(executeTime));
        }
        return DefaultPageFactory.createReturnPageInfo(page, list);
    }

    @Override
    public List<Map<String, Object>> findCountGroupBy(String column) {
        // 统计当前用户操作日志
        // SELECT log_name, ifnull(count(id), 0) AS countid FROM sys_log GROUP BY log_name,create_dept_id HAVING create_dept_id in ('1001')
        return this.mapper.queryCountGroupBy(column);
    }

}
