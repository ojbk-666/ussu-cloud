package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.thread.SaveDczxCourseThread;
import cc.ussu.modules.dczx.thread.SaveHomeWorkListThread;
import cc.ussu.modules.dczx.util.JsonpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 丰富题库
 */
@RestController
@RequestMapping(value = "/api/dczx/homework")
public class CollectHomeworkApiController extends BaseDczxController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void submitHomeworkHistoryList() {
        response.setContentType("application/javascript");
        String accesstoken = getReqParam("accesstoken");
        String sign = getReqParam("sign");
        String userid = getReqParam("userid");
        String time = getReqParam("time");
        String url = getReqParam("url");
        // 跳过少参
        if (StrUtil.isBlank(accesstoken) || StrUtil.isBlank(sign) || StrUtil.isBlank(userid) || StrUtil.isBlank(url)) {
            return;
        }
        DcInterfaceLog dcInterfaceLog = new DcInterfaceLog().setAccessToken(accesstoken)
                .setSign(sign).setUserid(userid).setTime(time).setUrl(url).setUserid(userid);
        // 采集课程信息
        // new SaveDczxCourseThread(dcInterfaceLog);
        threadPoolTaskExecutor.submit(new SaveDczxCourseThread(dcInterfaceLog));
        // new SaveDczxCourseByAllStudyPlanThread(dcInterfaceLog).start();  // 需要cookie
        // 采集答题记录列表
        // new SaveHomeWorkListThread(dcInterfaceLog).start();
        SaveHomeWorkListThread saveHomeWorkListThread = new SaveHomeWorkListThread(dcInterfaceLog);
        threadPoolTaskExecutor.submit(saveHomeWorkListThread);
        // 采集用户信息
        // new SaveDcxzUserInfoThread(dcInterfaceLog).start();
        ServletUtil.write(response, JsonpUtil.render(JsonResult.ok()), TEXT_PLAIN);
    }

}
