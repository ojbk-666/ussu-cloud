package cn.ussu.modules.dczx.util;

import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.util.HttpContext;
import com.alibaba.fastjson.JSON;

public class JsonpUtil {

    /**
     * 返回jsonp字符串
     *
     * @param jsonResult
     * @return
     */
    public static String render(JsonResult jsonResult) {
        String callbackName = HttpContext.getRequest().getParameter("callback");
        return callbackName + "(" + JSON.toJSONString(jsonResult) + ")";
    }
}
