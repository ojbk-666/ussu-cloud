package cc.ussu.common.security.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.util.SecurityContextHolder;
import cc.ussu.common.security.auth.AuthUtil;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.system.api.model.LoginUser;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        SecurityContextHolder.setUserId(ServletUtil.getHeader(request, SecurityConstants.DETAILS_USER_ID, StandardCharsets.UTF_8));
        SecurityContextHolder.setUserName(ServletUtil.getHeader(request, SecurityConstants.DETAILS_USERNAME, StandardCharsets.UTF_8));
        SecurityContextHolder.setUserKey(ServletUtil.getHeader(request, SecurityConstants.USER_KEY, StandardCharsets.UTF_8));

        // 请求id设置
        String traceId = ServletUtil.getHeader(request, SecurityConstants.TRACE_ID, StandardCharsets.UTF_8);
        if (StrUtil.isNotBlank(traceId)) {
            SecurityContextHolder.setTraceId(traceId);
            MDC.put(SecurityConstants.TRACE_ID, traceId);
        }

        // String token = SecurityUtil.getRequestToken();
        String token = SecurityUtil.getRequestToken(request);
        if (StrUtil.isNotBlank(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (ObjectUtil.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.remove();
    }
}
