package cn.ussu.common.security.config;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.util.HttpContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign 拦截器
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest httpServletRequest = HttpContext.getRequest();
                if (httpServletRequest != null) {
                    String tokenValueInHeader = httpServletRequest.getHeader(CacheConstants.TOKEN_IN_REQUEST_KEY);
                    if (StrUtil.isNotBlank(tokenValueInHeader)) {
                        template.header(CacheConstants.TOKEN_IN_REQUEST_KEY, tokenValueInHeader);
                    }
                    String cookieStr = httpServletRequest.getHeader("Cookie");
                    template.header("Cookie", cookieStr);
                }
            }
        };
    }

}
