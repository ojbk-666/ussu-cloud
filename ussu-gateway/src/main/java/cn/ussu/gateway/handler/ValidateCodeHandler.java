package cn.ussu.gateway.handler;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.exception.CaptchaException;
import cn.ussu.gateway.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 获取验证码
 */
@Component
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        JsonResult r = null;
        try {
            Optional<String> uuidOptional = request.queryParam("uuid");
            r = validateCodeService.create(uuidOptional.orElse(null));
        } catch (CaptchaException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(r));
    }

}
