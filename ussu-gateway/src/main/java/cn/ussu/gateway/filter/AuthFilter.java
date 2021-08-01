package cn.ussu.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.gateway.properties.IgnoreWhiteProperties;
import cn.ussu.gateway.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 网关鉴权
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private IgnoreWhiteProperties ignoreWhiteProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        // 不需要验证token的访问路径
        if (matchUrl(ignoreWhiteProperties.getWhites(), path)) {
            return chain.filter(exchange);
        }

        // 没有token
        // String token = getRequestToken(serverHttpRequest);
        // if (StrUtil.isBlank(token)) {
        //     return setUnauthorizedResponse(exchange, ErrorCodeConstants.TIME_OUT,"缺少登录凭证");
        // }
        // Object object = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + token);
        // token过期
        // if (object == null) {
        //     return setUnauthorizedResponse(exchange, ErrorCodeConstants.TIME_OUT,"凭证过期,请重新登录");
        // }
        JsonResult checkTokenResult = TokenUtil.checkTokenResult(getRequestToken(serverHttpRequest));
        if (!checkTokenResult.isSuccess()) {
            return setUnauthorizedResponse(exchange, checkTokenResult);
        }
        // 设置用户信息到请求
        ServerHttpRequest mutableReq = exchange.getRequest().mutate()
                .header("userId", "")
                .header("userType", "")
                .header("name", "")
                .header("nickName", "")
                .build();
        ServerWebExchange serverWebExchange = exchange.mutate().request(mutableReq).build();
        // return chain.filter(serverWebExchange);
        return chain.filter(exchange);
    }

    /**
     * 获取请求携带的token
     *
     * @param serverHttpRequest
     * @return
     */
    private String getRequestToken(ServerHttpRequest serverHttpRequest) {
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String requestToken = headers.getFirst(CacheConstants.TOKEN_IN_REQUEST_KEY);
        if (StrUtil.isBlank(requestToken)) {
            MultiValueMap<String, HttpCookie> cookies = serverHttpRequest.getCookies();
            HttpCookie cookie = cookies.getFirst(CacheConstants.TOKEN_IN_REQUEST_KEY);
            String cookieValue = cookie.getValue();
            if (StrUtil.isNotBlank(cookieValue)) {
                requestToken = cookieValue;
                // 重新放入头部
                headers.add(CacheConstants.TOKEN_IN_REQUEST_KEY, requestToken);
            }
        }
        if (StrUtil.isNotBlank(requestToken) && requestToken.startsWith(CacheConstants.TOKEN_PREFIX)) {
            requestToken = requestToken.replaceFirst(CacheConstants.TOKEN_PREFIX, StrUtil.EMPTY);
        }
        return requestToken;
    }

    /**
     * 返回鉴权失败
     *
     * @param exchange
     * @param msg
     * @return
     */
    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, Integer errorCode, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(JsonResult.error(errorCode, msg)));
        }));
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, JsonResult jsonResult) {
        return setUnauthorizedResponse(exchange, jsonResult.getCode(), jsonResult.getMsg());
    }

    /**
     * 判断路径是否匹配
     */
    public boolean matchUrl(List<String> patterns, String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return patterns.stream().anyMatch(item -> antPathMatcher.match(item, path));
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
