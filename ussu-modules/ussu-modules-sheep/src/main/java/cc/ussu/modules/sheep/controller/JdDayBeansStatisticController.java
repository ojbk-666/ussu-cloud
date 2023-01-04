package cc.ussu.modules.sheep.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.modules.sheep.entity.JdDayBeans;
import cc.ussu.modules.sheep.entity.JdUserInfo;
import cc.ussu.modules.sheep.service.IJdDayBeansService;
import cc.ussu.modules.sheep.service.IJdUserInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${ussu.mapping-prefix.sheep}/jd-beans/statistic")
public class JdDayBeansStatisticController extends BaseController {

    @Autowired
    private IJdDayBeansService jdDayBeansService;
    @Autowired
    private IJdUserInfoService jdUserInfoService;

    /**
     * 获取所有pin
     */
    @GetMapping("/pins")
    public JsonResult<List<String>> getPins() {
        List<JdDayBeans> list = jdDayBeansService.list(Wrappers.lambdaQuery(JdDayBeans.class)
                // .orderByDesc(JdDayBeans::getCreateDate)
                .select(JdDayBeans::getJdUserId).groupBy(JdDayBeans::getJdUserId));
        List<String> collect = list.stream().map(JdDayBeans::getJdUserId).collect(Collectors.toList());
        return JsonResult.ok(collect);
    }

    @GetMapping("/recent-by-pin/{pin}")
    public JsonResult<JSONObject> recentByJdPin(@PathVariable("pin") String pin) {
        List<JdDayBeans> recent = jdDayBeansService.list(Wrappers.lambdaQuery(JdDayBeans.class)
                .orderByDesc(JdDayBeans::getCreateDate).eq(JdDayBeans::getJdUserId, pin).last("limit 7"));
        List<JdDayBeans> list = CollUtil.reverse(recent);
        JdUserInfo jdUserInfo = jdUserInfoService.getById(pin);
        return JsonResult.ok(new JSONObject().set("pin", pin)
                .set("nickname", jdUserInfo == null ? pin : jdUserInfo.getNickname())
                // .set("list", list)
                .set("xarr", list.stream().map(r -> DateUtil.format(r.getCreateDate(), "MM/dd")).collect(Collectors.toList()))
                .set("y1arr", list.stream().map(JdDayBeans::getIncomeBean).collect(Collectors.toList()))
                .set("y2arr", list.stream().map(JdDayBeans::getOutBean).collect(Collectors.toList()))
                .set("z1arr", list.stream().map(r -> r.getIncomeBean() - r.getOutBean()).collect(Collectors.toList())))
        ;
    }

    @GetMapping("/recent")
    public JsonResult recent() {
        List<JSONObject> result = new LinkedList<>();
        Date end = DateUtil.endOfDay(new Date());
        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -14));
        List<JdDayBeans> list = jdDayBeansService.list(Wrappers.lambdaQuery(JdDayBeans.class)
                .orderByAsc(JdDayBeans::getCreateDate)
                .ge(JdDayBeans::getCreateDate, start)
                .le(JdDayBeans::getCreateDate, end));
        // List<String> pins = getPins();
        Set<String> pins = list.stream().map(JdDayBeans::getJdUserId).collect(Collectors.toSet());
        for (String pin : pins) {
            result.add(recentByJdPin(pin).getData());
        }
        return JsonResult.ok(result);
    }

    private String filterNickname(String uid, List<JdUserInfo> list) {
        for (JdUserInfo item : list) {
            if (item.getJdUserId().equalsIgnoreCase(uid)) {
                return item.getNickname();
            }
        }
        return uid;
    }

    /**
     * 所有账号的京豆记录折线图
     */
    @GetMapping("/recent10")
    public JsonResult recentTop10() {
        // 统计最近多少条记录
        final int recentNum = 15;
        Date end = DateUtil.endOfDay(new Date());
        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), 1 - recentNum));
        List<JdDayBeans> list = jdDayBeansService.list(Wrappers.lambdaQuery(JdDayBeans.class)
                .orderByAsc(JdDayBeans::getCreateDate)
                .ge(JdDayBeans::getCreateDate, start)
                .le(JdDayBeans::getCreateDate, end));
        List<JdUserInfo> jdUserInfoList = jdUserInfoService.list();
        Set<String> jdUserIdSet = list.stream().map(JdDayBeans::getJdUserId).collect(Collectors.toSet());
        List<JSONObject> result = new ArrayList<>();
        for (String uid : jdUserIdSet) {
            List<Integer> incomeList = new ArrayList<>();
            List<Integer> outList = new ArrayList<>();
            for (JdDayBeans item : list) {
                if (item.getJdUserId().equalsIgnoreCase(uid)) {
                    incomeList.add(item.getIncomeBean());
                    outList.add(item.getOutBean());
                }
            }
            CollUtil.padLeft(incomeList, recentNum, 0);
            CollUtil.padLeft(outList, recentNum, 0);
            result.add(new JSONObject().set("uid", uid).set("nickname", filterNickname(uid, jdUserInfoList)).set("y", incomeList).set("y1", outList));
        }
        // 获取日期
        List<String> x = DateUtil.rangeToList(start, end, DateField.HOUR_OF_DAY, 24).stream().map(r -> DateUtil.format(r, "MM-dd")).collect(Collectors.toList());
        return JsonResult.ok(new JSONObject().set("x", x).set("list", result));
    }

}
