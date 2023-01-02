package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.modules.dczx.controller.DcStatisticController;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小组件接口
 */
@RestController
@RequestMapping("/api/dczx/scriptable")
public class ScriptableApiController extends BaseController {

    @Autowired
    private DcStatisticController dcStatisticController;

    private String getAppSecret() {
        return DictUtil.getValue("dczx", "scriptable:secret");
    }

    /**
     * 小组件统计接口
     */
    @GetMapping("/statistic")
    public Object statistic(String s) {
        if (!StrUtil.equals(getAppSecret(), s)) {
            return JsonResult.error("appSecret invalid");
        }
        return dcStatisticController.countNum();
    }

}
