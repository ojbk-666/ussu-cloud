package cn.ussu.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.gateway.properties.IgnoreWhiteProperties;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 */
@Component
// @Order(-200)
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
        if (ignoreWhiteProperties.getWhites().contains(path)) {
            return chain.filter(exchange);
        }

        // 没有token
        String token = getRequestToken(serverHttpRequest);
        if (StrUtil.isBlank(token)) {
            return setUnauthorizedResponse(exchange, "缺少登录凭证");
        }
        String str = (String) redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + token);
        // token过期
        if (StrUtil.isBlank(str)) {
            return setUnauthorizedResponse(exchange, "凭证过期,请重新登录");
        }
        // token篡改

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
    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(JsonResult.error(msg)));
        }));
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
