package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.modules.dczx.entity.DcFeedback;
import cc.ussu.modules.dczx.service.IDcFeedbackService;
import cc.ussu.system.api.RemoteSystemNoticeService;
import cc.ussu.system.api.vo.SendNoticeVO;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

// @Tag(name = SendFeedbackApiController.SYSTEM_LOG_GROUP)
@RestController
@RequestMapping("/api/dczx/feedback")
public class SendFeedbackApiController {

    public static final String SYSTEM_LOG_GROUP = "插件反馈";

    @Autowired
    private IDcFeedbackService dcFeedbackService;
    @Autowired
    private RemoteSystemNoticeService sysNoticeService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // @Operation(summary = "插件反馈", description = "从插件添加反馈信息")
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PutMapping
    public JsonResult add(@RequestBody DcFeedback dcFeedback) {
        if (StrUtil.isNotBlank(dcFeedback.getContent())) {
            dcFeedbackService.save(dcFeedback.setCreateTime(new Date()));
        }
        threadPoolTaskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                sysNoticeService.noticeSuperAdmin(new SendNoticeVO().setTitle("有新的东财在线脚本反馈").setContent(dcFeedback.getEmail() + ":" + dcFeedback.getContent()));
            }
        });
        return JsonResult.ok();
    }

}
