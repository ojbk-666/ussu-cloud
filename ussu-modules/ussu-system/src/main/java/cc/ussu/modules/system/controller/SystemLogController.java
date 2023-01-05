package cc.ussu.modules.system.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.system.es.domain.SystemLog;
import cc.ussu.modules.system.es.mapper.ESSystemLogMapper;
import cc.ussu.system.api.vo.SystemLogVO;
import cn.easyes.core.biz.PageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.toolkit.EsWrappers;
import cn.easyes.starter.config.EasyEsConfigProperties;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/system-log")
public class SystemLogController extends BaseController {

    @Autowired(required = false)
    private EasyEsConfigProperties easyEsConfigProperties;
    @Autowired
    private ESSystemLogMapper systemLogMapper;

    @GetMapping("/page")
    public Object page(SystemLog q) {
        LambdaEsQueryWrapper<SystemLog> qw = EsWrappers.lambdaQuery(SystemLog.class)
            .orderByDesc(SystemLog::getCreateTime)
            .eq(StrUtil.isNotBlank(q.getUserId()), SystemLog::getUserId, q.getUserId())
            .eq(StrUtil.isNotBlank(q.getAccount()), SystemLog::getAccount, q.getAccount())
            .eq(StrUtil.isNotBlank(q.getTraceId()), SystemLog::getTraceId, q.getTraceId())
            .eq(StrUtil.isNotBlank(q.getServiceName()), SystemLog::getServiceName, q.getServiceName())
            .eq(StrUtil.isNotBlank(q.getIp()), SystemLog::getIp, q.getIp())
            .like(StrUtil.isNotBlank(q.getGroup()), SystemLog::getGroup, q.getGroup())
            .like(StrUtil.isNotBlank(q.getName()), SystemLog::getName, q.getName());
        Long pageNo = MybatisPlusUtil.getPageNo();
        PageInfo<SystemLog> pageInfo = systemLogMapper.pageQuery(qw, pageNo.intValue(), MybatisPlusUtil.getPageSize().intValue());
        return MybatisPlusUtil.getResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @PutMapping
    public JsonResult save(@RequestBody SystemLogVO vo) {
        if (easyEsConfigProperties != null && easyEsConfigProperties.isEnable()) {
            SystemLog systemLog = BeanUtil.toBean(vo, SystemLog.class);
            systemLog.setCreateTime(DateUtil.formatDateTime(vo.getCreateTime()));
            systemLogMapper.updateById(systemLog);
        }
        return JsonResult.ok();
    }

}
