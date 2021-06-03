package cn.ussu.modules.dczx.portal;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.thread.SaveDczxCourseThread;
import cn.ussu.modules.dczx.thread.SaveHomeWorkListThread;
import cn.ussu.modules.dczx.util.JsonpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 丰富题库
 */
@RestController
@RequestMapping(value = "/api/dczx/homework")
public class CollectHomeworkController extends BaseController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public Object submitHomeworkHistoryList() {
        String accesstoken = getReqParam("accesstoken");
        String sign = getReqParam("sign");
        String userid = getReqParam("userid");
        String time = getReqParam("time");
        String url = getReqParam("url");
        // 跳过少参
        if (StrUtil.isBlank(accesstoken) || StrUtil.isBlank(sign) || StrUtil.isBlank(userid) || StrUtil.isBlank(url)) {
            return "";
        }
        DcInterfaceLog dcInterfaceLog = new DcInterfaceLog().setAccessToken(accesstoken)
                .setSign(sign).setUserid(userid).setTime(time).setUrl(url);
        // 采集课程信息
        new SaveDczxCourseThread(dcInterfaceLog).start();
        // 采集答题记录列表
        new SaveHomeWorkListThread(dcInterfaceLog).start();
        // 采集用户信息
        // new SaveDcxzUserInfoThread(dcInterfaceLog).start();
        return JsonpUtil.render(JsonResult.ok());
    }

}
