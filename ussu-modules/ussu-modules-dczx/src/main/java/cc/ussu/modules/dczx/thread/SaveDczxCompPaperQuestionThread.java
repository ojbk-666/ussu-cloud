package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.modules.dczx.entity.*;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.QuestionOption;
import cc.ussu.modules.dczx.model.vo.TopicTrunk;
import cc.ussu.modules.dczx.model.vo.composite.CompositeHomeworkVo;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.*;

/**
 * 采集试题线程
 */
public class SaveDczxCompPaperQuestionThread extends Thread {

    public static final ThreadLocal<DcInterfaceLog> threadLocal = new ThreadLocal<>();

    private DcInterfaceLog dcInterfaceLog;

    public SaveDczxCompPaperQuestionThread(DcInterfaceLog dcInterfaceLog) {
        // this.dcInterfaceLog = dcInterfaceLog;
        threadLocal.set(dcInterfaceLog);
        this.dcInterfaceLog = threadLocal.get();
    }

    @Override
    public void run() {
        // 缺少参数跳过
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog)) return;
        // 获取接口结果
        // https://classroom.edufe.com.cn/TKGL/showHistoryPapermcapi.action?tactic_id=19384a85-64f2-48f1-aa8b-f3a43e68f793&courseValue=B0226A&exampapertypeValue=7&customerId=1&examPaper_Id=be6a4141-209c-4fb7-b8c6-a69ddde39cfc&exampaperSeq=6f164433-ea43-40d0-a197-71d6cb97735e&_=1623291769671
        String baseUrl = "https://classroom.edufe.com.cn/TKGL/showHistoryPapermcapi.action";
        dcInterfaceLog.setReqUrl(baseUrl);
        Map<String, Object> param = new HashMap<>();
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        param.put("tactic_id", decodeParams.get("tactic_id").get(0));
        param.put("courseValue", decodeParams.get("courseValue").get(0));
        param.put("exampapertypeValue", decodeParams.get("exampapertypeValue").get(0));
        param.put("customerId", "1");
        param.put("exampaperSeq", decodeParams.get("exampaperSeq").get(0));
        param.put("examPaper_Id", decodeParams.get("examPaper_Id").get(0));
        Date now = new Date();
        param.put("_", now.getTime());
        String rep = MyHttpRequest.createGet(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .form(param)
                .connectionKeepAlive()
                .execute().body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        CompositeHomeworkVo compositeHomeworkVo = null;
        try {
            jsonObject = JSONUtil.parseObj(rep);
            compositeHomeworkVo = JSONUtil.toBean(rep, CompositeHomeworkVo.class);
            String code = jsonObject.getStr(DczxUtil.code);
            if (StrUtil.isNotBlank(code)) {
                dcInterfaceLog.setResult(false);
                dcInterfaceLog.setReason(jsonObject.getStr(DczxUtil.msg));
            }
        } catch (JSONException e) {
            // 出错
            dcInterfaceLog.setResult(false);
            dcInterfaceLog.setReason("josn解析失败");
        } finally {
            dcInterfaceLog.setRemarks("采集综合作业试题");
            DczxUtil.saveInterfaceLog(dcInterfaceLog);
        }
        if (jsonObject != null && dcInterfaceLog.getResult()) {
            // 解析保存
            try {
                // DczxUtil.analyzeQuestionJson(dcInterfaceLog, jsonObject, URLUtil.decode(decodeParams.get("paperName").get(0)));
                analyzeResponse(compositeHomeworkVo, decodeParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析结果保存结果
     */
    private void analyzeResponse(CompositeHomeworkVo compositeHomeworkVo, Map<String, List<String>> decodeParams) {
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        Date now = new Date();
        String paperIdStr = compositeHomeworkVo.getPAPERID();
        // 写入paper
        DcPaper dcPaper = new DcPaper();
        dcPaper.setPaperId(paperIdStr).setPaperName(URLUtil.decode(decodeParams.get("paperName").get(0)))
                .setInterfaceLogId(dcInterfaceLog.getId()).setCreateTime(now);
        dcPaper = DczxUtil.savePaper(dcPaper);
        String courseValue_1 = decodeParams.get("courseValue").get(0);
        String courseIdStr = courseValue_1.split("_")[0];

        List<PaperQuestion> paperQuestions = compositeHomeworkVo.getPAPER_QUESTIONS();
        for (PaperQuestion paperQuestion : paperQuestions) {
            // 保存试题类型
            String topicId = paperQuestion.getTOPIC_ID();
            String topicTypeCd = paperQuestion.getTOPIC_TYPE_CD();
            String fulltopictypecd = paperQuestion.getFulltopictypecd();
            String questionTypeNm = paperQuestion.getQUESTION_TYPE_NM();
            String topicTypeId = paperQuestion.getTOPIC_TYPE_ID();
            DcPaperQuestionTopic dcPaperQuestionTopic = new DcPaperQuestionTopic();
            dcPaperQuestionTopic.setTopicId(topicId)
                    .setTopicTypeCd(topicTypeCd)
                    .setFullTopicTypeCd(fulltopictypecd)
                    .setQuestionTypeNm(questionTypeNm)
                    .setTopicTypeId(topicTypeId)
                    // .setPaperIdLong(dcPaper.getId())
                    .setPaperId(dcPaper.getPaperId())
                    .setInterfaceLogId(dcInterfaceLog.getId());
            dcPaperQuestionTopic = DczxUtil.saveQuestionTopic(dcPaperQuestionTopic);

            List<TopicTrunk> topicTrunk = paperQuestion.getTOPIC_TRUNK();
            for (TopicTrunk object : topicTrunk) {
                String questionId = object.getQUESTION_ID();
                String questionTitle = object.getQUESTION_TITLE().replaceAll(" ", "").trim();
                String topictrunkType = object.getTOPICTRUNK_TYPE();
                String right = object.getRIGHT();
                if ("1".equals(right)) {
                    // 正确的题目才入库
                    List<QuestionOption> questionOptions = object.getQUESTION_OPTIONS();
                    // 跳过已有记录
                    if (existsQuestion(questionId)) {
                        continue;
                    }
                    // 写入题目
                    DcPaperQuestion dpq = new DcPaperQuestion();
                    dpq.setQuestionId(questionId)
                            .setCourseId(courseIdStr)
                            // .setPaperIdLong(dcPaper.getId())
                            .setPaperId(paperIdStr)
                            // .setTopicIdLong(dcPaperQuestionTopic.getId())
                            .setTopicId(topicId)
                            .setQuestionTitle(questionTitle)
                            .setFullTopicTypeCd(paperQuestion.getFulltopictypecd())
                            .setInterfaceLogId(dcInterfaceLog.getId())
                            .setCreateBy(dcInterfaceLog.getCreateBy())
                            .setCreateTime(now);
                    DczxUtil.saveQuestion(dpq);
                    List<DcQuestionOption> questionOptionList = new ArrayList<>();
                    for (QuestionOption o3jo : questionOptions) {
                        String optionId = o3jo.getOPTION_ID().replaceAll(" ", "").trim();
                        String optionContent = o3jo.getOPTION_CONTENT().replaceAll(" ", "").trim();
                        String optionType = o3jo.getOPTION_TYPE();
                        String istrue = o3jo.getISTRUE();
                        // 选项
                        questionOptionList.add(new DcQuestionOption()
                                // .setPaperIdLong(dcPaper.getId())
                                .setPaperId(paperIdStr)
                                // .setQuestionIdLong(dpq.getId())
                                .setQuestionId(questionId)
                                .setInterfaceLogId(dcInterfaceLog.getId())
                                .setOptionId(optionId)
                                .setOptionContent(optionContent)
                                .setOptionType(optionType)
                                .setIstrue("1".equals(istrue))
                                .setCreateBy(dcInterfaceLog.getCreateBy()));
                    }
                    // 写入选项-批量
                    IDcQuestionOptionService questionOptionService = SpringUtil.getBean(IDcQuestionOptionService.class);
                    questionOptionService.saveBatch(questionOptionList);
                }
            }
        }
    }

    /**
     * 判断question是否已存在记录
     *
     * @param questionId
     * @return
     */
    public synchronized boolean existsQuestion(String questionId) {
        return DczxUtil.getRedisUtil().sHasKey(DczxUtil.QUESTION_ID_KEY_IN_REDIS, questionId);
    }

}
