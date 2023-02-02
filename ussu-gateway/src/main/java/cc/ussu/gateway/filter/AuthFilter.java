package cc.ussu.gateway.filter;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cc.ussu.common.core.constants.CacheConstants;
import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.constants.TokenConstants;
import cc.ussu.common.core.util.JwtUtils;
import cc.ussu.common.core.util.PathMatcherUtil;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.gateway.properties.IgnoreWhiteProperties;
import cc.ussu.gateway.util.ServletUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IgnoreWhiteProperties ignoreWhiteProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String path = request.getURI().getPath();
        // 不需要验证token的访问路径
        if (PathMatcherUtil.match(ignoreWhiteProperties.getWhites(), path)) {
            return chain.filter(exchange);
        }
        String token = getRequestToken(request);
        // String token = getToken(request);
        if (StrUtil.isBlank(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        if (!redisService.hasKey(getTokenKey(userkey))) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        if (StrUtil.hasBlank(userid, username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }
        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED.value());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLEncodeUtil.encode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }


    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY_ + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotBlank(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }

    /**
     * 获取请求携带的token
     *
     * @param serverHttpRequest
     * @return
     */
    private String getRequestToken(ServerHttpRequest serverHttpRequest) {
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String requestToken = headers.getFirst(TokenConstants.AUTHENTICATION);
        if (StrUtil.isBlank(requestToken)) {
            MultiValueMap<String, HttpCookie> cookies = serverHttpRequest.getCookies();
            if (cookies != null) {
                HttpCookie cookie = cookies.getFirst(TokenConstants.AUTHENTICATION);
                if (cookie != null) {
                    String cookieValue = cookie.getValue();
                    if (StrUtil.isNotBlank(cookieValue)) {
                        requestToken = cookieValue;
                        // 重新放入头部
                        headers.add(TokenConstants.AUTHENTICATION, requestToken);
                    }
                }
            }
            if (StrUtil.isBlank(requestToken)) {
                MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
                if (queryParams != null) {
                    requestToken = queryParams.getFirst(TokenConstants.AUTHENTICATION);
                }
            }
        }
        if (StrUtil.isNotBlank(requestToken) && requestToken.startsWith(TokenConstants.PREFIX)) {
            requestToken = requestToken.replaceFirst(TokenConstants.PREFIX, StrUtil.EMPTY);
        }
        return requestToken;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
