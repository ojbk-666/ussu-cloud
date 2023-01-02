package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.service.impl.PracticeListService;
import cc.ussu.modules.dczx.util.JsonpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 随堂随练 收集题目
 */
@RestController
@RequestMapping("/api/dczx/practice")
public class CollectPracticeQuestionApiController extends BaseDczxController {

    @Autowired
    private PracticeListService practiceQuestionService;

    /**
     * 采集随堂随练列表
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void submit() {
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
                .setSign(sign).setUserid(userid).setTime(time).setUrl(url).setCreateBy(userid);
        practiceQuestionService.getPracticeList(dcInterfaceLog);
        ServletUtil.write(response, JsonpUtil.render(JsonResult.ok()), TEXT_PLAIN);
    }

}
