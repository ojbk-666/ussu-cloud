package cn.ussu.modules.feign;

import cn.ussu.common.entity.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * test2远程调用
 */
@FeignClient(name = "ussu-test2")
public interface RemoteTest2Service {

    /**
     * 调用test2接口
     */
    @GetMapping("/test2/{id}")
    public JsonResult test2(@PathVariable("id") String id);

}
