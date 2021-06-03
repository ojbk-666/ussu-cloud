package cn.ussu.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.util.HttpContext;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.log.model.vo.LogVo;
import cn.ussu.common.log.service.RecordLogService;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.util.SecurityUtils;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 操作日志,仅记录异步请求切包含注解{@link InsertLog}的方法
 */
@Aspect
@Component
public class RecordLogAspect {

    @Autowired
    private RecordLogService recordLogService;

    // RequestMapping 无法拦截GetMapping PostMapping...的问题：http://www.imooc.com/article/details/id/297283
    @Pointcut(value = "execution(@(org.springframework.web.bind.annotation.*Mapping) * *(..))")
    public void cutService() {
    }

    @Around(value = "cutService()")
    public Object recordLog(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = HttpContext.getRequest();
        HttpServletResponse response = HttpContext.getResponse();
        LocalDateTime now = LocalDateTime.now();
        LogVo logVo = new LogVo();
        boolean needRecordLog = validateAnnotation(point);
        if (needRecordLog) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String userAgentStr = request.getHeader(Header.USER_AGENT.getValue());
            UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
            LoginUser loginUser = SecurityUtils.getLoginUser();
            logVo.setRequestTime(LocalDateTime.now())
                    .setRequestUri(request.getServletPath())
                    .setRequestParams(StrUtil.maxLength(JSON.toJSONString(parameterMap), 2000))
                    .setRemoteIp(ServletUtil.getClientIP(request))
                    .setRequestMethod(request.getMethod())
                    .setUserAgent(userAgentStr)
                    .setBrowserType(userAgent.getBrowser().getName())
                    .setDeviceName(userAgent.getOs().getName())
                    .setLogName(hanldeAnnotation(point));
            if (loginUser != null) {
                logVo.setCreateBy(loginUser.getSysUser().getId());
            }
        }
        long startTimeLong = System.currentTimeMillis();
        Object proceed = point.proceed();
        if (needRecordLog) {
            logVo.setExecuteTime(System.currentTimeMillis() - startTimeLong)
                    .setHttpStatus(response.getStatus());
            if (proceed instanceof JsonResult) {   // json返回
                JsonResult jr = (JsonResult) proceed;
                logVo.setResultCode(jr.getCode()).setResultMsg(jr.getMsg());
                if (jr.getCode() != JsonResult.SUCCESS_CODE) {
                    // 有异常
                    logVo.setExceptionInfo(((String) jr.get("exception")));
                }
            }
            recordLogService.recordLog(logVo);
        }
        return proceed;
    }

    /**
     * 验证注解
     */
    private boolean validateAnnotation(ProceedingJoinPoint point) {
        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();
        // 获取目标方法或类是否包含InsertLog注解
        InsertLog annotationOnMethod = msig.getMethod().getAnnotation(InsertLog.class);
        InsertLog annotationOnClass = target.getClass().getAnnotation(InsertLog.class);
        if (annotationOnMethod == null && annotationOnClass == null) {
            return false;
        }
        return true;
    }

    /**
     * 处理注解上的参数
     *
     * @param point
     * @return
     * @throws Exception
     */
    private String hanldeAnnotation(ProceedingJoinPoint point) throws Exception {
        //获取拦截的方法名
        Signature sig = point.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = point.getTarget();

        //如果当前用户未登录，不做日志
        // Authentication authentication = SecurityUtils.getAuthentication();
        // if (null == authentication) {
        // 	return;
        // }

        // 获取目标方法或类是否包含InsertLog注解
        InsertLog annotationOnMethod = msig.getMethod().getAnnotation(InsertLog.class);
        InsertLog annotationOnClass = target.getClass().getAnnotation(InsertLog.class);
        // sysLog = new SysLog();
        // 优先方法上的注解值,方法注解值为空时使用类注解的值
        String name = null;
        if (annotationOnMethod != null) {
            name = annotationOnMethod.value();
        } else if (annotationOnClass != null) {
            if (StrUtil.isBlank(name)) {
                name = annotationOnClass.value();
            }
        }
        return name;
    }

}
