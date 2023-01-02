package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.entity.*;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.homework.ShowHistoryPapermcapiResponse;
import cc.ussu.modules.dczx.service.*;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 采集试题线程
 */
public class SaveDczxPaperQuestionThread extends Thread {

    public static final ThreadLocal<DcInterfaceLog> threadLocal = new ThreadLocal<>();

    private DcInterfaceLog dcInterfaceLog;

    public SaveDczxPaperQuestionThread(DcInterfaceLog dcInterfaceLog) {
        // this.dcInterfaceLog = dcInterfaceLog;
        threadLocal.set(dcInterfaceLog);
        this.dcInterfaceLog = threadLocal.get();
    }

    @Override
    public void run() {
        // 缺少参数跳过
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog))  {
            return;
        }
        // 获取接口结果
        String baseUrl = "https://classroom.edufe.com.cn/TKGL/showHistoryPapermcapi.action";
        dcInterfaceLog.setReqUrl(baseUrl);
        Map<String, Object> param = new HashMap<>();
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        param.put("tactic_id", decodeParams.get("tactic_id").get(0));
        param.put("courseValue", decodeParams.get("courseCd").get(0));
        param.put("exampapertypeValue", decodeParams.get("exampapertypeValue").get(0));
        param.put("customerId", "1");
        param.put("exampaperSeq", decodeParams.get("exampaperSeq").get(0));
        param.put("examPaper_Id", decodeParams.get("exampaperId").get(0));
        Date now = new Date();
        param.put("_", now.getTime());
        MyHttpResponse execute = MyHttpRequest.createGet(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .form(param)
                .connectionKeepAlive()
                .execute();
        String rep = execute.body();
                dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        ShowHistoryPapermcapiResponse showHistoryPapermcapiResponse = null;
        try {
            if (execute.isOk()) {
                showHistoryPapermcapiResponse = JSONUtil.toBean(rep, ShowHistoryPapermcapiResponse.class);
                if (showHistoryPapermcapiResponse != null) {
                    if (StrUtil.isNotBlank(showHistoryPapermcapiResponse.getCode()) || StrUtil.isNotBlank(showHistoryPapermcapiResponse.getErrorMessage())) {
                        dcInterfaceLog.setResult(false);
                        dcInterfaceLog.setReason(jsonObject.getStr(DczxUtil.msg));
                    }
                }
            } else {
                throw new IllegalArgumentException("网络错误：" + execute.getStatus());
            }
        } catch (JSONException e) {
            // 出错
            dcInterfaceLog.setResult(false);
            dcInterfaceLog.setReason("josn解析失败");
        } finally {
            dcInterfaceLog.setRemarks("采集试题线程");
            dcInterfaceLog.setId(null);
            SpringUtil.getBean(IDcInterfaceLogService.class).save(dcInterfaceLog);
        }
        if (showHistoryPapermcapiResponse != null && dcInterfaceLog.getResult()) {
            // 解析保存
            // try {
            //     DczxUtil.analyzeQuestionJson(dcInterfaceLog, jsonObject, URLUtil.decode(decodeParams.get("paperName").get(0)));
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
            List<DcPaperQuestion> questionList = analyzeQuestionJson(showHistoryPapermcapiResponse);
            if (CollUtil.isEmpty(questionList)) {
                return;
            }
            String courseCd = decodeParams.get("courseCd").get(0);
            DcCourse dcCourse = SpringUtil.getBean(IDcCourseService.class).getByServiceCourseVersId(courseCd);
            questionList.forEach(r -> {
                if (dcCourse != null) {
                    r.setCourseId(dcCourse.getCourseId());
                }
                r.setCreateTime(now).setCreateBy(dcInterfaceLog.getUserid()).setInterfaceLogId(dcInterfaceLog.getId());
                List<DcQuestionOption> options = r.getOptions();
                if (CollUtil.isNotEmpty(options)) {
                    options.forEach(r2 -> {
                        r2.setCreateBy(r.getCreateBy()).setCreateTime(r.getCreateTime()).setInterfaceLogId(r.getInterfaceLogId());
                    });
                }
            });
            // 保存
            saveQuestion(questionList);
            saveTopic(questionList);
            saveOption(questionList);
            savePaper(showHistoryPapermcapiResponse.getPAPERID(), decodeParams.get("exampaperId").get(0));
            // DynamicDataSourceContextHolder.clear();
        }
    }

    public List<DcPaperQuestion> analyzeQuestionJson(ShowHistoryPapermcapiResponse showHistoryPapermcapiResponse) {
        String paperIdStr = showHistoryPapermcapiResponse.getPAPERID();
        // 题目列表
        List<PaperQuestion> paperQuestions = showHistoryPapermcapiResponse.getPAPER_QUESTIONS();
        // 要保存的所有题目
        List<DcPaperQuestion> questionList = new ArrayList<>();
        paperQuestions.forEach(pq -> {
            pq.getTOPIC_TRUNK().forEach(r -> {
                DcPaperQuestion dcPaperQuestion = new DcPaperQuestion();
                // 解析题目中的图片信息
                String questionTitle = DczxUtil.downloadImgToLocalFromHtmlStr(r.getQUESTION_TITLE());
                dcPaperQuestion.setQuestionId(r.getQUESTION_ID())
                        .setPaperId(paperIdStr)
                        .setTopicId(pq.getTOPIC_ID())
                        .setQuestionTitle(questionTitle)
                        .setFullTopicTypeCd(pq.getFulltopictypecd());
                // 题型
                DcPaperQuestionTopic dcTopic = new DcPaperQuestionTopic();
                dcTopic.setTopicId(pq.getTOPIC_ID())
                        .setTopicTypeId(pq.getTOPIC_ID())
                        .setTopicTypeCd(pq.getTOPIC_TYPE_CD())
                        .setFullTopicTypeCd(pq.getFulltopictypecd())
                        .setPaperId(paperIdStr)
                        .setQuestionTypeNm(pq.getQUESTION_TYPE_NM());
                dcPaperQuestion.setTopic(dcTopic);
                // 选项
                List<DcQuestionOption> tempQuestionOptionList = r.getQUESTION_OPTIONS().stream().map(qo -> new DcQuestionOption()
                        .setPaperId(paperIdStr)
                        .setQuestionId(r.getQUESTION_ID())
                        .setOptionId(qo.getOPTION_ID())
                        .setOptionContent(DczxUtil.downloadImgToLocalFromHtmlStr(qo.getOPTION_CONTENT()))
                        .setOptionType(qo.getOPTION_TYPE())
                        .setIstrue("1".equals(qo.getISTRUE()))
                ).collect(Collectors.toList());
                dcPaperQuestion.setOptions(tempQuestionOptionList);
                questionList.add(dcPaperQuestion);
            });
        });
        return questionList;
    }

    public synchronized void saveQuestion(List<DcPaperQuestion> questionList) {
        // 已存在的id
        List<String> questionIdList = questionList.stream().map(DcPaperQuestion::getQuestionId).collect(Collectors.toList());
        IDcPaperQuestionService questionService = SpringUtil.getBean(IDcPaperQuestionService.class);
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        List<String> existsIds = questionService.listByIds(questionIdList).stream().map(DcPaperQuestion::getQuestionId).collect(Collectors.toList());
        questionIdList.removeAll(existsIds);
        if (CollUtil.isNotEmpty(questionIdList)) {
            List<DcPaperQuestion> newQuestions = questionList.stream().filter(r -> CollUtil.contains(questionIdList, r.getQuestionId())).collect(Collectors.toList());
            questionService.saveBatch(newQuestions);
        }
    }

    public synchronized void saveTopic(List<DcPaperQuestion> questionList) {
        List<DcPaperQuestionTopic> topicList = questionList.stream().map(DcPaperQuestion::getTopic).collect(Collectors.toList());
        // 已存在的id
        List<String> idList = topicList.stream().map(DcPaperQuestionTopic::getFullTopicTypeCd).collect(Collectors.toList());
        IDcPaperQuestionTopicService service = SpringUtil.getBean(IDcPaperQuestionTopicService.class);
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        List<String> existsIds = service.listByIds(idList).stream().map(DcPaperQuestionTopic::getFullTopicTypeCd).collect(Collectors.toList());
        idList.removeAll(existsIds);
        if (CollUtil.isNotEmpty(idList)) {
            List<DcPaperQuestionTopic> newList = topicList.stream().filter(r -> CollUtil.contains(idList, r.getFullTopicTypeCd())).collect(Collectors.toList());
            service.saveBatch(newList);
        }
    }

    public synchronized void saveOption(List<DcPaperQuestion> questionList) {
        List<DcQuestionOption> optionList = new ArrayList<>();
        questionList.forEach(r -> {
            optionList.addAll(r.getOptions());
        });
        // 已存在的id
        List<String> idList = optionList.stream().map(DcQuestionOption::getOptionId).collect(Collectors.toList());
        IDcQuestionOptionService service = SpringUtil.getBean(IDcQuestionOptionService.class);
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        List<String> existsIds = service.listByIds(idList).stream().map(DcQuestionOption::getOptionId).collect(Collectors.toList());
        idList.removeAll(existsIds);
        if (CollUtil.isNotEmpty(idList)) {
            List<DcQuestionOption> newList = optionList.stream().filter(r -> CollUtil.contains(idList, r.getOptionId())).collect(Collectors.toList());
            service.saveBatch(newList);
        }
    }

    private synchronized void savePaper(String paperId, String paperName) {
        DcPaper dcPaper = new DcPaper();
        dcPaper.setPaperId(paperId)
                .setPaperName(paperName)
                .setInterfaceLogId(dcInterfaceLog.getId()).setCreateTime(dcInterfaceLog.getCreateTime());
        DczxUtil.savePaper(dcPaper);
    }

}
