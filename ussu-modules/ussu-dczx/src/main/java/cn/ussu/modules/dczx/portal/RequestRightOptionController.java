package cn.ussu.modules.dczx.portal;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import cn.ussu.modules.dczx.util.DczxUtil;
import cn.ussu.modules.dczx.util.JsonpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 获取答案
 *
 * @author liming
 * @date 2020-07-07 22:18
 */
@RestController
@RequestMapping(value = "/api/dczx/option")
public class RequestRightOptionController extends BaseController {

    @Autowired
    private IDcQuestionOptionService optionService;
    @Autowired
    private IDcPaperQuestionService questionService;

    /**
     * 通过题目id获取答案
     */
    @GetMapping
    public Object getRightOptionByQuestionIds(@RequestParam("q") String idsStr) {
        idsStr = idsStr.trim().replaceAll(" ", "")
                .replaceAll("，", ",")
                .replaceAll(",,", ",");
        List<String> idsList = Arrays.asList(idsStr.split(","));
        QueryWrapper<DcQuestionOption> qw = new QueryWrapper<>();
        qw.select("option_id");
        qw.eq("istrue", 1).in("question_id_str", idsList);
        List<DcQuestionOption> list = optionService.list(qw);
        Set<String> rightOptionIdList = list.stream().map(item -> {
            return item.getOptionId();
        }).collect(Collectors.toSet());
        return JsonpUtil.render(JsonResult.ok().data(rightOptionIdList));
    }

    /**
     * 获取单元练习答案
     */
    @GetMapping("/HomeWorkPaper")
    public Object getRightOptionHomeworkPaper(@RequestParam("q") String idsStr) {
        idsStr = idsStr.trim().replaceAll(" ", "")
                .replaceAll("，", ",")
                .replaceAll(",,", ",");
        List<DcPaperQuestion> list = questionService.findByQuestionIds(idsStr.split(","));
        return JsonpUtil.render(JsonResult.ok(0).data(list));
    }

    /**
     * 获取综合作业答案
     */
    @GetMapping("/CompHomeworkPaper")
    public Object getRightOptionCompHomeworkPaper(String dil, String p2) throws Exception {
        checkReqParamThrowException(dil);
        checkReqParamThrowException(p2);
        DcInterfaceLog dilObj = JSON.parseObject(dil, DcInterfaceLog.class);
        // 获取试题内容-模拟刷新浏览器了
        // https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action?tactic_id=f9443f25-1805-43eb-8120-b8fbb981ba58&batchValue=202003&gradationValue=a23&subjectValue=37&courseValue=B1081B_1&chapterssections_id=&exampapertypeValue=7&customerId=1&sign=6384c1693a77b7700835d09755499488110072e8&_=1594176231043
        String ajaxUrl = "https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action";
        dilObj.setReqUrl(ajaxUrl);
        if (!(DczxUtil.checkInterfaceLogParam(dilObj))) {
            return JsonpUtil.render(JsonResult.error());
        }
        // 页面url参数
        // https://classroom.edufe.com.cn/CompHomeworkPaper?tactic_id=f9443f25-1805-43eb-8120-b8fbb981ba58&courseCd=B1081B_1&batchValue=202003&gradationValue=a23&subjectValue=37&courseName=undefined&exampapertypeValue=7&paperName=%25E9%25A9%25AC%25E5%2585%258B%25E6%2580%259D%25E4%25B8%25BB%25E4%25B9%2589%25E5%259F%25BA%25E6%259C%25AC%25E5%258E%259F%25E7%2590%2586%25E6%25A6%2582%25E8%25AE%25BA%25E7%25BB%25BC%25E5%2590%2588%25E4%25BD%259C%25E4%25B8%259A&courseId=B1081B&sign=6384c1693a77b7700835d09755499488110072e8&examResultId=69ab7894-918e-45b4-9796-503c74f9ea57
        Map<String, String> urlParam = HttpUtil.decodeParamMap(p2, CharsetUtil.CHARSET_UTF_8);
        // 必传参数
        Map<String, Object> reqParam = new HashMap<>();
        reqParam.put("tactic_id", urlParam.get("tactic_id"));
        reqParam.put("batchValue", urlParam.get("batchValue"));
        reqParam.put("gradationValue", urlParam.get("gradationValue"));
        reqParam.put("subjectValue", urlParam.get("subjectValue"));
        reqParam.put("courseValue", urlParam.get("courseCd"));
        reqParam.put("chapterssections_id", urlParam.get(""));
        reqParam.put("exampapertypeValue", urlParam.get("exampapertypeValue"));
        reqParam.put("customerId", urlParam.get("1"));  // 未明确
        reqParam.put("sign", urlParam.get("sign"));
        reqParam.put("_", DczxUtil.timestamp());
        String repBody = HttpRequest.get(ajaxUrl)
                .headerMap(DczxUtil.getHeaderMap(dilObj), true)
                .header("User-Agent",request.getHeader(Header.USER_AGENT.getValue()))
                .form(reqParam)
                .execute().body();
        dilObj.setResponseBody(repBody);
        // 解析并丰富题库，返回结果直接就有答案
        JSONObject jsonObject = null;
        dilObj.setResult(true);
        try {
            jsonObject = JSON.parseObject(repBody);
            String code = jsonObject.getString("code");
            if (StrUtil.isNotBlank(code)) {
                dilObj.setResult(false);
                dilObj.setReason(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            dilObj.setResult(false);
            dilObj.setReason("json解析失败");
        } finally {
            dilObj.setRemarks("获取综合作业答案");
            dilObj.insert();
            if (jsonObject != null && dilObj.getResult()) {
                DczxUtil.analyzeQuestionJson(dilObj, jsonObject, urlParam.get("paperName"));
            }
        }
        return JsonpUtil.render(JsonResult.ok(0).data(jsonObject));
    }

}
