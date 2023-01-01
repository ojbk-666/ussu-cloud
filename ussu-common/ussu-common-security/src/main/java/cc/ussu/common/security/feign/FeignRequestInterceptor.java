package cc.ussu.common.security.feign;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.util.HttpContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * feign请求拦截器 传递登录用户信息
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest httpServletRequest = HttpContextHolder.getRequest();
        if (ObjectUtil.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtil.getHeaderMap(httpServletRequest);
            // 传递用户信息请求头，防止丢失
            String userId = headers.get(SecurityConstants.DETAILS_USER_ID);
            if (StrUtil.isNotBlank(userId)) {
                template.header(SecurityConstants.DETAILS_USER_ID, userId);
            }
            String userKey = headers.get(SecurityConstants.USER_KEY);
            if (StrUtil.isNotBlank(userKey)) {
                template.header(SecurityConstants.USER_KEY, userKey);
            }
            String userName = headers.get(SecurityConstants.DETAILS_USERNAME);
            if (StrUtil.isNotBlank(userName)) {
                template.header(SecurityConstants.DETAILS_USERNAME, userName);
            }
            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
            if (StrUtil.isNotBlank(authentication)) {
                template.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
            }
            // traceId
            String traceId = headers.get(SecurityConstants.TRACE_ID);
            if (StrUtil.isNotBlank(traceId)) {
                template.header(SecurityConstants.TRACE_ID, traceId);
            }

            // 配置客户端IP
            template.header("X-Forwarded-For", ServletUtil.getClientIP(httpServletRequest));
        }
    }
}
