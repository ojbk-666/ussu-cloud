package cc.ussu.common.log.aspect;

import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.util.JsonUtils;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.service.RecordLogService;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.system.api.model.LoginUser;
import cc.ussu.system.api.vo.SystemLogVO;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 系统操作日志
 */
@Deprecated
@Slf4j
@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private RecordLogService recordLogService;

    /**
     * 切入点
     */
    @Pointcut(value = "@annotation(cc.ussu.common.log.annotation.SystemLog)")
    public void cutService() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "cutService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        Object proceed = null;
        try {
            proceed = point.proceed();
        } finally {
            saveLog(point, proceed);
        }
        return proceed;
    }

    private void saveLog(ProceedingJoinPoint point, Object result) {
        MethodSignature msig = (MethodSignature) point.getSignature();
        if (msig == null) {
            return;
        }
        // 注解信息
        SystemLog annotation = msig.getMethod().getAnnotation(SystemLog.class);
        SystemLog classAnno = point.getTarget().getClass().getAnnotation(SystemLog.class);
        String group = annotation.group();
        String name = annotation.name();
        String serviceName = annotation.serviceName();
        if (classAnno != null) {
            group = StrUtil.blankToDefault(group, classAnno.group());
            name = StrUtil.blankToDefault(name, classAnno.name());
            serviceName = StrUtil.blankToDefault(serviceName, classAnno.serviceName());
        }
        SystemLogVO systemLog = new SystemLogVO()
            .setGroup(group).setName(name)
            .setServiceName(serviceName)
            .setTraceId(MDC.get(SecurityConstants.TRACE_ID))
            .setCreateTime(new Date());
        // 用户信息
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null) {
            systemLog.setUserId(loginUser.getUserId()).setAccount(loginUser.getAccount());
        }
        // 请求ip
        HttpServletRequest request = HttpContextHolder.getRequest();
        if (request != null) {
            systemLog.setIp(ServletUtil.getClientIP(request)).setReferer(ServletUtil.getHeaderIgnoreCase(request, Header.REFERER.getValue()));
        }
        // 结果
        if (result != null && annotation.saveResult()) {
            try {
                String s = JsonUtils.toJsonStr(result);
                systemLog.setResult(s);
            } catch (Exception e) {
                systemLog.setResult(result.toString());
            }
        }
        // 参数
        Object[] args = point.getArgs();
        if (ArrayUtil.isNotEmpty(args) && annotation.saveParam()) {
            try {
                String s = JsonUtils.toJsonStr(args);
                systemLog.setParams(s);
            } catch (Exception e) {
                systemLog.setParams(args.toString());
            }
        }
        recordLogService.saveLog(systemLog);
    }

}
