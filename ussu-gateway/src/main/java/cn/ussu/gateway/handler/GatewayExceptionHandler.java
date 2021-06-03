package cn.ussu.gateway.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关异常处理
 */
@Component
@Order(-1)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        String errorMsg;
        JsonResult error = JsonResult.error();
        if (ex instanceof NotFoundException) {
            errorMsg = "服务未找到";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorMsg = responseStatusException.getMessage();
        } else {
            error.put("exception", ExceptionUtil.stacktraceToString(ex));
            errorMsg = "服务器内部错误";
        }
        error.setMsg(errorMsg);
        String requestId = exchange.getRequest().getId();
        error.put("requestId", requestId);
        String requestPath = exchange.getRequest().getURI().getPath();
        error.put("path", requestPath);
        log.error("[网关异常处理]请求路径:{},异常信息:{}", requestPath, ex.getMessage());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(error));
        }));
    }
}
