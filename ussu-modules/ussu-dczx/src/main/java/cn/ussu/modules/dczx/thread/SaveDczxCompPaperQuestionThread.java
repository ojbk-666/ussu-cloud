package cn.ussu.modules.dczx.thread;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.modules.dczx.entity.*;
import cn.ussu.modules.dczx.model.vo.composite.CompositeHomeworkVo;
import cn.ussu.modules.dczx.model.vo.composite.PaperQuestion;
import cn.ussu.modules.dczx.model.vo.composite.QuestionOption;
import cn.ussu.modules.dczx.model.vo.composite.TopicTrunk;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import cn.ussu.modules.dczx.service.IDcPaperService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import cn.ussu.modules.dczx.util.DczxUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
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
        String rep = HttpRequest.get(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .form(param).execute().body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        CompositeHomeworkVo compositeHomeworkVo = null;
        try {
            jsonObject = JSON.parseObject(rep);
            compositeHomeworkVo = JSON.parseObject(rep, CompositeHomeworkVo.class);
            String code = jsonObject.getString(DczxUtil.code);
            if (StrUtil.isNotBlank(code)) {
                dcInterfaceLog.setResult(false);
                dcInterfaceLog.setReason(jsonObject.getString(DczxUtil.msg));
            }
        } catch (JSONException e) {
            // 出错
            dcInterfaceLog.setResult(false);
            dcInterfaceLog.setReason("josn解析失败");
        } finally {
            dcInterfaceLog.setRemarks("采集综合作业试题");
            saveInterfaceLog();
        }
        if (jsonObject != null && dcInterfaceLog.getResult()) {
            // 解析保存
            try {
                // DczxUtil.analyzeQuestionJson(dcInterfaceLog, jsonObject, URLUtil.decode(decodeParams.get("paperName").get(0)));
                analyzeResponse(compositeHomeworkVo, decodeParams);
            } catch (Exception e) {
            }
        }
    }

    // 保存接口日志
    private void saveInterfaceLog() {
        dcInterfaceLog.insert();
    }

    /**
     * 解析结果保存结果
     */
    private void analyzeResponse(CompositeHomeworkVo compositeHomeworkVo, Map<String, List<String>> decodeParams) {
        LocalDateTime now = LocalDateTime.now();
        String paperIdStr = compositeHomeworkVo.getPAPERID();
        // 写入paper
        DcPaper dcPaper = new DcPaper();
        dcPaper.setPaperId(paperIdStr).setPaperName(URLUtil.decode(decodeParams.get("paperName").get(0))).setInterfaceLogId(dcInterfaceLog.getId());
        dcPaper = savePaper(dcPaper);

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
                    .setPaperIdLong(dcPaper.getId())
                    .setPaperIdStr(dcPaper.getPaperId())
                    .setInterfaceLogId(dcInterfaceLog.getId());
            dcPaperQuestionTopic = saveQuestionTopic(dcPaperQuestionTopic);

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
                    if (existsQuestion(questionId)) continue;
                    // 写入题目
                    DcPaperQuestion dpq = new DcPaperQuestion();
                    dpq.setQuestionId(questionId)
                            .setPaperIdLong(dcPaper.getId())
                            .setPaperIdStr(paperIdStr)
                            .setTopicIdLong(dcPaperQuestionTopic.getId())
                            .setTopicIdStr(topicId)
                            .setQuestionTitle(questionTitle)
                            .setTopictrunkType(topictrunkType)
                            .setInterfaceLogId(dcInterfaceLog.getId())
                            .setCreateBy(dcInterfaceLog.getCreateBy())
                            .setCreateTime(now)
                            .insert();
                    List<DcQuestionOption> questionOptionList = new ArrayList<>();
                    for (QuestionOption o3jo : questionOptions) {
                        String optionId = o3jo.getOPTION_ID().replaceAll(" ", "").trim();
                        String optionContent = o3jo.getOPTION_CONTENT().replaceAll(" ", "").trim();
                        String optionType = o3jo.getOPTION_TYPE();
                        // String istrue = o3jo.getISTRUE();
                        String istrue = "0";
                        Object selected = o3jo.getSelected();
                        if (selected.toString().contains("on")) {
                            // select 为正确的情况
                            istrue = "1";
                        }
                        // 选项
                        questionOptionList.add(new DcQuestionOption()
                                .setPaperIdLong(dcPaper.getId())
                                .setPaperIdStr(paperIdStr)
                                .setQuestionIdLong(dpq.getId())
                                .setQuestionIdStr(questionId)
                                .setInterfaceLogId(dcInterfaceLog.getId())
                                .setOptionId(optionId)
                                .setOptionContent(optionContent)
                                .setOptionType(optionType)
                                .setIstrue("1".equals(istrue))
                                .setCreateBy(dcInterfaceLog.getCreateBy()));
                    }
                    // 写入选项-批量
                    IDcQuestionOptionService questionOptionService = SpringContextHolder.getBean(IDcQuestionOptionService.class);
                    questionOptionService.saveBatch(questionOptionList);
                }
            }
        }
    }

    /**
     * 保存paper
     *
     * @param dcPaper
     * @return
     */
    private DcPaper savePaper(DcPaper dcPaper) {
        // 是否存在
        IDcPaperService service = SpringContextHolder.getBean(IDcPaperService.class);
        QueryWrapper<DcPaper> qw = new QueryWrapper<>();
        qw.eq("paper_id", dcPaper.getPaperId());
        List<DcPaper> list = service.list(qw);
        if (list == null || list.size() == 0) {
            // 不存在
            dcPaper.insert();
            return dcPaper;
        } else {
            return list.get(0);
        }
    }

    /**
     * 保存试题类型
     *
     * @param dcPaperQuestionTopic
     * @return
     */
    private DcPaperQuestionTopic saveQuestionTopic(DcPaperQuestionTopic dcPaperQuestionTopic) {
        // 是否存在
        IDcPaperQuestionTopicService service = SpringContextHolder.getBean(IDcPaperQuestionTopicService.class);
        QueryWrapper<DcPaperQuestionTopic> qw = new QueryWrapper<>();
        qw.eq("topic_type_id", dcPaperQuestionTopic.getTopicTypeId());
        List<DcPaperQuestionTopic> list = service.list(qw);
        // DcPaperQuestionTopic one = service.getOne(qw);
        if (list == null || list.size() == 0) {
            // 不存在
            dcPaperQuestionTopic.insert();
            return dcPaperQuestionTopic;
        } else {
            return list.get(0);
        }
    }

    /**
     * 判断question是否已存在记录
     *
     * @param questionId
     * @return
     */
    public synchronized boolean existsQuestion(String questionId) {
        IDcPaperQuestionService service = SpringContextHolder.getBean(IDcPaperQuestionService.class);
        QueryWrapper<DcPaperQuestion> qw = new QueryWrapper<>();
        qw.eq("question_id", questionId);
        int count = service.count(qw);
        return count > 0;
    }

}
