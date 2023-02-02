package cc.ussu.system.api.factory;

import cc.ussu.system.api.RemoteSystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteSystemUserService> {

    @Override
    public RemoteSystemUserService create(Throwable cause) {
        log.error("用户服务调用失败:{}", cause.getMessage());
        return null;
    }

}
