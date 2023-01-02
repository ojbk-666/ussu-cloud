package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.util.SecurityContextHolder;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.system.api.RemoteSystemNoticeService;
import cc.ussu.system.api.vo.SendNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/dczx/test")
public class TestTraceIdNoticeApiController extends BaseController {

    @Autowired
    private RemoteSystemNoticeService remoteSystemNoticeService;

    @GetMapping
    public JsonResult test() {
        log.info("通知管理员 start");
        JsonResult jr = remoteSystemNoticeService.noticeSuperAdmin(new SendNoticeVO().setTitle("测试traciId通知").setContent(SecurityContextHolder.getTraceId()));
        log.info("通知管理员 end");
        return JsonResult.ok(jr);
    }

}
