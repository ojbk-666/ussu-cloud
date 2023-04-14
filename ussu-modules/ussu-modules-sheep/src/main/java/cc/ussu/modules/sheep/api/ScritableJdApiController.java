package cc.ussu.modules.sheep.api;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.modules.sheep.entity.JdDayBeans;
import cc.ussu.modules.sheep.service.IJdDayBeansService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/jd/scriptable")
public class ScritableJdApiController extends BaseController {

    @Autowired
    private IJdDayBeansService jdDayBeansService;

    /**
     * 获取最近7天的京豆收入
     */
    @GetMapping("/recent-jd-bean")
    public JsonResult getRecentJdBean(String pin, String sign) {
        Assert.isTrue(StrUtil.equals(ConfigUtil.getValue("dczx", "scriptable:secret"), sign), "error");
        Assert.notBlank(pin, "error");
        pin = pin.replaceAll("pin=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
        // 获取几天的记录
        String limit = ConfigUtil.getValue("sheep", "jd:scriptable:limit", "7");
        List<JdDayBeans> list = jdDayBeansService.list(Wrappers.lambdaQuery(JdDayBeans.class)
                .orderByDesc(JdDayBeans::getCreateDate).eq(JdDayBeans::getJdUserId, pin).last(" limit " + limit));
        List<String> dateList = new ArrayList<>();
        List<Integer> incomeList = new ArrayList<>();
        List<Integer> outList = new ArrayList<>();
        List<JSONObject> voList = new ArrayList<>();
        // x轴日期格式化格式
        String pattern = ConfigUtil.getValue("sheep","scriptable:xAxis-date-format","MM/dd");
        for (JdDayBeans item : CollUtil.reverse(list)) {
            String dateStr = DateUtil.format(item.getCreateDate(), pattern);
            dateList.add(dateStr);
            incomeList.add(item.getIncomeBean());
            outList.add(item.getOutBean());
            voList.add(new JSONObject().set("label", dateStr).set("income", item.getIncomeBean()).set("out", item.getOutBean()));
        }
        JSONObject jr = new JSONObject().set("dateList", dateList).set("incomeList", incomeList).set("outList", outList).set("voList", voList);
        return JsonResult.ok(jr);
    }

}
