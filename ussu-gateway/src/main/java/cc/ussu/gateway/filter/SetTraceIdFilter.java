package cc.ussu.gateway.filter;

import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.util.SecurityContextHolder;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 设置日志追踪ID
 */
@Slf4j
@Component
public class SetTraceIdFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest.Builder mutate = exchange.getRequest().mutate();
        String traceId = IdUtil.getSnowflakeNextIdStr();
        mutate.header(SecurityConstants.TRACE_ID, traceId);
        MDC.put(SecurityConstants.TRACE_ID, traceId);
        SecurityContextHolder.setTraceId(traceId);
        log.info("请求：{}", exchange.getRequest().getURI());
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return 50;
    }

}
