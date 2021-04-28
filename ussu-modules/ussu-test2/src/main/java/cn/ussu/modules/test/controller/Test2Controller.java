package cn.ussu.modules.test.controller;

import cn.ussu.common.entity.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供测试服务
 */
@RestController
@RequestMapping
public class Test2Controller {

    /**
     * test2接口
     *
     * @param id
     * @return
     */
    @GetMapping("/test2/{id}")
    public JsonResult test(@PathVariable("id") String id) {
        return JsonResult.ok().data("test2接口接受的数据路径参数id：" + id);
    }

}
