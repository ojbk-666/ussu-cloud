package cc.ussu.modules.dczx.api;

import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.modules.dczx.base.BaseDczxController;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.entity.DcQuestionOption;
import cc.ussu.modules.dczx.entity.DcRequestLog;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import cc.ussu.modules.dczx.es.mapper.ESDcPaperQuestionMapper;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import cc.ussu.modules.dczx.service.IDcRequestLogService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cc.ussu.modules.dczx.util.RequestCountUtil;
import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import cn.easyes.core.toolkit.EsWrappers;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
// @Tag(name = "RequestRightOptionApiController", description = RequestRightOptionApiController.SYSTEM_LOG_GROUP)
@RestController
@RequestMapping(value = "/api/dczx/option")
public class RequestRightOptionApiController extends BaseDczxController {

    public static final String SYSTEM_LOG_GROUP = "东财在线插件请求";

    @Autowired
    private IDcQuestionOptionService optionService;
    @Autowired
    private IDcPaperQuestionService questionService;
    @Autowired
    private IDcInterfaceLogService dcInterfaceLogService;
    @Autowired
    private ESDcPaperQuestionMapper esDcPaperQuestionMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 通过题目id获取答案，仅返回正确答案的id 返回json
     */
    // @Operation(summary = "通过题目id获取答案")
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "通过题目id获取答案")
    @GetMapping("/json")
    public JsonResult getRightOptionByQuestionIdsJson(@RequestParam("q") String idsStr) {
        RequestCountUtil.count();
        saveRequestLog(idsStr);
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
        return JsonResult.ok(rightOptionIdList);
    }

    /**
     * 获取单元练习答案，返回json格式
     */
    // @Operation(summary = "获取单元作业答案")
    @SystemLog(saveResult = false, group = SYSTEM_LOG_GROUP, name = "获取单元作业答案")
    @GetMapping("/HomeWorkPaper/json")
    public JsonResult getRightOptionHomeworkPaperJson(@RequestParam("q") String idsStr) {
        RequestCountUtil.count();
        saveRequestLog(idsStr);
        idsStr = idsStr.trim().replaceAll(" ", "")
                .replaceAll("，", ",")
                .replaceAll(",,", ",");
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        List<DcPaperQuestionVO> list = questionService.findByQuestionIds(idsStr.split(","));
        // 按照请求顺序进行排序
        List<String> idList = StrUtil.split(idsStr, StrPool.COMMA);
        List<DcPaperQuestionVO> result = new LinkedList<>();
        a: for (String id : idList) {
            b: for (DcPaperQuestionVO item : list) {
                if (id.equals(item.getQuestionId())) {
                    result.add(item);
                    break b;
                }
            }
        }
        return JsonResult.ok(result);
    }

    /**
     * 获取综合作业答案，返回json，获取的json中的答案不再准确，获取题目id，之后之后获取答案
     */
    // @Operation(summary = "获取综合作业答案")
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "获取综合作业答案")
    @GetMapping("/CompHomeworkPaper/json2")
    public JsonResult getRightOptionCompHomeworkPaperJson2(String dil, String p2) throws Exception {
        RequestCountUtil.count();
        DcInterfaceLog dilObj = JSONUtil.toBean(dil, DcInterfaceLog.class);
        JSONObject entries = JSONUtil.parseObj(dil);
        String accesstoken = entries.getStr("accesstoken");
        if (StrUtil.isBlank(dilObj.getAccessToken()) && StrUtil.isNotBlank(accesstoken)) {
            dilObj.setAccessToken(accesstoken);
        }
        // 获取试题内容-模拟刷新浏览器了
        // https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action?tactic_id=f9443f25-1805-43eb-8120-b8fbb981ba58&batchValue=202003&gradationValue=a23&subjectValue=37&courseValue=B1081B_1&chapterssections_id=&exampapertypeValue=7&customerId=1&sign=6384c1693a77b7700835d09755499488110072e8&_=1594176231043
        String ajaxUrl = "https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action";
        dilObj.setReqUrl(ajaxUrl);
        if (!(DczxUtil.checkInterfaceLogParam(dilObj))) {
            return JsonResult.error();
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
                .header("User-Agent", request.getHeader(HttpHeaders.USER_AGENT))
                .form(reqParam)
                .execute().body();
        dilObj.setResponseBody(repBody);
        // 解析并丰富题库，返回结果直接就有答案
        JSONObject jsonObject = null;
        dilObj.setResult(true);
        try {
            jsonObject = JSONUtil.parseObj(repBody);
            String code = jsonObject.getStr("code");
            if (StrUtil.isNotBlank(code)) {
                dilObj.setResult(false);
                dilObj.setReason(jsonObject.getStr("msg"));
            }
        } catch (Exception e) {
            dilObj.setResult(false);
            dilObj.setReason("json解析失败");
        } finally {
            dilObj.setRemarks("获取综合作业答案");
            dcInterfaceLogService.save(dilObj);
            if (jsonObject != null && dilObj.getResult()) {
                // 解析试题列表
                // DczxUtil.analyzeQuestionJson(dilObj, jsonObject, urlParam.get("paperName"));
                List<String> ids = new LinkedList<>();
                // JSONObject tempd1 = jsonObject.getJSONObject("data");
                JSONArray tempd2 = jsonObject.getJSONArray("PAPER_QUESTIONS");
                for (Object o : tempd2) {
                    JSONObject tempd3 = (JSONObject) o;
                    JSONObject tempd4 = (JSONObject) tempd3.getJSONArray("TOPIC_TRUNK").get(0);
                    String tempQuestionId = tempd4.getStr("QUESTION_ID");
                    ids.add(tempQuestionId);
                }
                // 返回答案
                return getRightOptionHomeworkPaperJson(CollectionUtil.join(ids, StrPool.COMMA));
            }
        }
        return JsonResult.ok(jsonObject);
    }

    /**
     * 精确搜索，从数据库搜索记录
     */
    // @Operation(summary = "精确搜索")
    @SystemLog(saveResult = false, group = SYSTEM_LOG_GROUP, name = "精确搜索")
    @GetMapping("/searchDb")
    public Object findQuestion(String keyword) {
        RequestCountUtil.count();
        saveRequestLog(keyword);
        Map<String, Object> map = new HashMap<>();
        map.put("questionTitle", keyword);
        if (DictUtil.getValueBoolean("dczx", "search_db_from_es", false)) {
            LambdaEsQueryWrapper<DcPaperQuestionVO> qw = EsWrappers.lambdaQuery(DcPaperQuestionVO.class)
                    .sortByScore().match(DcPaperQuestionVO::getQuestionTitle, keyword);
            EsPageInfo<DcPaperQuestionVO> pageInfo = esDcPaperQuestionMapper.pageQuery(qw, 1, 20);
            List<DcPaperQuestionVO> list = pageInfo.getList();
            return JsonResult.ok(list);
        } else {
            IPage page = questionService.findPage(map);
            return JsonResult.ok(page.getRecords());
        }
    }

    private void saveRequestLog(String param) {
        String userid = ServletUtil.getHeader(HttpContextHolder.getRequest(), "userid", CharsetUtil.CHARSET_UTF_8);
        DcRequestLog dcRequestLog = DcRequestLog.builder().id(null).createTime(new Date()).userid(userid).param(param).build();
        threadPoolTaskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SpringUtil.getBean(IDcRequestLogService.class).save(dcRequestLog);
            }
        });
    }

    /**
     * 模糊搜索，从es搜索记录
     */
    // @Operation(summary = "模糊搜索")
    @SystemLog(saveResult = false, group = SYSTEM_LOG_GROUP, name = "模糊搜索")
    @GetMapping("/searchEs")
    public Object searchQuestion(@RequestParam String keyword) {
        if (!DictUtil.getValueBoolean("dczx", DczxConstants.PARAM_KEY_REQUEST_RIGHT_OPTION_FROM_ES)) {
            return findQuestion(keyword);
        }
        RequestCountUtil.count();
        saveRequestLog(keyword);
        LambdaEsQueryWrapper<DcPaperQuestionVO> qw = EsWrappers.lambdaQuery(DcPaperQuestionVO.class)
                .sortByScore()
                .match(DcPaperQuestionVO::getQuestionTitle, keyword);
        EsPageInfo<DcPaperQuestionVO> pageInfo = esDcPaperQuestionMapper.pageQuery(qw, 1, 20);
        List<DcPaperQuestionVO> list = pageInfo.getList();
        return JsonResult.ok(list);
    }

}
