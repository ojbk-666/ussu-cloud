package cc.ussu.gateway.handler;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.gateway.util.ServletUtils;
import cn.hutool.http.HttpStatus;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关异常处理
 */
@Slf4j
@Component
@Order(-1)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        int code = JsonResult.ERROR_CODE;
        String msg;
        if (ex instanceof NotFoundException) {
            code = HttpStatus.HTTP_NOT_FOUND;
            msg = "服务未找到";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            msg = responseStatusException.getMessage();
        } else if (ex instanceof SignatureException){
            code = HttpStatus.HTTP_UNAUTHORIZED;
            msg = "登录信息已失效，请重新登录";
        } else {
            msg = "内部服务器错误";
        }
        log.error("[网关异常处理]请求id:{},请求路径:{},异常信息:{}", exchange.getRequest().getId(), exchange.getRequest().getPath(), ex.getMessage());
        return ServletUtils.webFluxResponseWriter(response, msg, code);
    }
}
