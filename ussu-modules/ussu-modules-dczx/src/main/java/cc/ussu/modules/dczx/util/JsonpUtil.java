package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.vo.JsonResult;
import cn.hutool.json.JSONUtil;

public class JsonpUtil {

    /**
     * 返回jsonp字符串
     *
     * @param jsonResult
     * @return
     */
    public static String render(JsonResult jsonResult) {
        String callbackName = HttpContextHolder.getRequest().getParameter("callback");
        return callbackName + "(" + JSONUtil.toJsonStr(jsonResult) + ")";
    }
}
