package cn.ussu.modules.controller;

import cn.ussu.common.entity.JsonResult;
import cn.ussu.modules.feign.RemoteTest2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test1接口
 */
@RestController
@RequestMapping("/test1")
public class Test1Controller {

    @Autowired
    private RemoteTest2Service remoteTest2Service;

    /**
     * 调用ussu-test2远程服务上的test2
     * @return
     */
    @GetMapping("/test")
    public JsonResult test() {
        System.out.println("test1接口调用远程服务2  start");
        JsonResult jsonResult = remoteTest2Service.test2("123");
        System.out.println("test1接口调用远程服务2  end");
        return JsonResult.ok("test1 ok").data(jsonResult);
    }

}
