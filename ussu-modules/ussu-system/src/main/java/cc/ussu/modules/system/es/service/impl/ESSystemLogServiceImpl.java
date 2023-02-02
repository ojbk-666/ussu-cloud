package cc.ussu.modules.system.es.service.impl;

import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.service.RecordLogService;
import cc.ussu.modules.system.es.domain.SystemLog;
import cc.ussu.modules.system.es.mapper.ESSystemLogMapper;
import cc.ussu.modules.system.es.service.ESSystemLogService;
import cc.ussu.system.api.vo.SystemLogVO;
import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.toolkit.EsWrappers;
import cn.easyes.starter.config.EasyEsConfigProperties;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * system模块使用本地日志保存 不用调远程兜一圈
 */
@Service
public class ESSystemLogServiceImpl implements ESSystemLogService, RecordLogService {

    @Autowired(required = false)
    private EasyEsConfigProperties easyEsConfigProperties;
    @Autowired(required = false)
    private ESSystemLogMapper systemLogMapper;

    @Override
    public EsPageInfo<SystemLog> findPage(SystemLog query) {
        LambdaEsQueryWrapper<SystemLog> qw = EsWrappers.lambdaQuery(SystemLog.class)
            .orderByDesc(SystemLog::getCreateTime)
            .eq(StrUtil.isNotBlank(query.getUserId()), SystemLog::getUserId, query.getUserId())
            .eq(StrUtil.isNotBlank(query.getAccount()), SystemLog::getAccount, query.getAccount())
            .eq(StrUtil.isNotBlank(query.getTraceId()), SystemLog::getTraceId, query.getTraceId())
            .eq(StrUtil.isNotBlank(query.getServiceName()), SystemLog::getServiceName, query.getServiceName())
            .eq(StrUtil.isNotBlank(query.getIp()), SystemLog::getIp, query.getIp())
            .like(StrUtil.isNotBlank(query.getGroup()), SystemLog::getGroup, query.getGroup())
            .like(StrUtil.isNotBlank(query.getName()), SystemLog::getName, query.getName());
        EsPageInfo<SystemLog> pageInfo = systemLogMapper.pageQuery(qw, MybatisPlusUtil.getPageNo().intValue(), MybatisPlusUtil.getPageSize().intValue());
        return pageInfo;
    }

    @Override
    public void saveLog(SystemLogVO systemLogVO) {
        if (easyEsConfigProperties != null && easyEsConfigProperties.isEnable()) {
            SystemLog systemLog = BeanUtil.toBean(systemLogVO, SystemLog.class);
            systemLog.setCreateTime(DateUtil.formatDateTime(systemLogVO.getCreateTime()));
            systemLogMapper.insert(systemLog);
        }
    }

}
