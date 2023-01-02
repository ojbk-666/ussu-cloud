package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.service.impl.SimulatedQuestionService;
import cc.ussu.modules.dczx.util.JsonpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模拟试题
 */
@RestController
@RequestMapping(value = "/api/dczx/simulated")
public class CollectSimulatedQuestionApiController extends BaseDczxController {

    @Autowired
    private SimulatedQuestionService simulatedQuestionService;

    /**
     * 采集模拟试题的题目
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void submit() {
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
                .setSign(sign).setUserid(userid).setTime(time).setUrl(url).setCreateBy(userid);
        simulatedQuestionService.collect(dcInterfaceLog);
        // return JsonpUtil.render(JsonResult.ok());
        ServletUtil.write(response, JsonpUtil.render(JsonResult.ok()), TEXT_PLAIN);
    }

}
