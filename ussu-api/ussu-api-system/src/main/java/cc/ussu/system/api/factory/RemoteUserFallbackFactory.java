package cc.ussu.system.api.factory;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.RemoteSystemUserService;
import cc.ussu.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用户服务降级处理
 */
@Slf4j
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteSystemUserService> {

    @Override
    public RemoteSystemUserService create(Throwable cause) {
        log.error("用户服务调用失败:{}", cause.getMessage());
        return new RemoteSystemUserService() {
            @Override
            public JsonResult<LoginUser> getUserByUsername(String username) {
                return JsonResult.error("获取用户失败:" + cause.getMessage());
            }

            @Override
            public JsonResult<LoginUser> insertOrUpdateByThird(Map param) {
                return JsonResult.error("失败:" + cause.getMessage());
            }
        };
    }

}
