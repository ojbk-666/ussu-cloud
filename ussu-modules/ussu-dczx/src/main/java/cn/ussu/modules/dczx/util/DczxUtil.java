package cn.ussu.modules.dczx.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.modules.dczx.entity.*;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import cn.ussu.modules.dczx.service.IDcPaperService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
import java.util.*;

public class DczxUtil {

    public static final String PAPERID = "PAPERID";
    public static final String PAPER_QUESTIONS = "PAPER_QUESTIONS";

    public static final String TOPIC_ID = "TOPIC_ID";
    public static final String TOPIC_TYPE_CD = "TOPIC_TYPE_CD";
    public static final String Fulltopictypecd = "Fulltopictypecd";
    public static final String QUESTION_TYPE_NM = "QUESTION_TYPE_NM";
    public static final String TOPIC_TYPE_ID = "TOPIC_TYPE_ID";
    public static final String TOPIC_TRUNK = "TOPIC_TRUNK";

    public static final String QUESTION_ID = "QUESTION_ID";
    public static final String QUESTION_TITLE = "QUESTION_TITLE";
    public static final String TOPICTRUNK_TYPE = "TOPICTRUNK_TYPE";
    public static final String QUESTION_OPTIONS = "QUESTION_OPTIONS";

    public static final String OPTION_ID = "OPTION_ID";
    public static final String OPTION_CONTENT = "OPTION_CONTENT";
    public static final String OPTION_TYPE = "OPTION_TYPE";
    public static final String ISTRUE = "ISTRUE";

    public static final String code = "code";
    public static final String msg = "msg";

    public static boolean checkInterfaceLogParam(DcInterfaceLog dcInterfaceLog) {
        if (dcInterfaceLog == null) return false;
        if (StrUtil.isBlank(dcInterfaceLog.getUrl())) return false;
        if (StrUtil.isBlank(dcInterfaceLog.getAccessToken())) return false;
        if (StrUtil.isBlank(dcInterfaceLog.getSign())) return false;
        return true;
    }

    public static String timestamp() {
        return String.valueOf(new Date().getTime());
    }

    public static Map<String, String> getHeaderMap(DcInterfaceLog dil) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", dil.getUserid());
        // 校验签名
        if (StrUtil.isNotBlank(dil.getTime())) {
            map.put("time", dil.getTime());
        } else {
            map.put("time", String.valueOf(new Date().getTime()));
        }
        map.put("accesstoken", dil.getAccessToken());
        map.put("sign", dil.getSign());
        map.put("Host", "classroom.edufe.com.cn");
        map.put("Referer", dil.getUrl());
        map.put("Sec-Fetch-Dest", "Sec-Fetch-Dest");
        map.put("Sec-Fetch-Mode", "cors");
        map.put("Sec-Fetch-Site", "same-origin");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4");
        return map;
    }

    /**
     * 解析试题返回的结果并写入数据库
     *
     * @param dcInterfaceLog 接口必要参数
     * @param jsonObject 接口返回的json对象
     * @param paperName 名称
     * @throws Exception
     */
    public static void analyzeQuestionJson(DcInterfaceLog dcInterfaceLog, JSONObject jsonObject, String paperName) throws Exception {
        // DcPaperQuestionSearch questionSearch = SpringContextHolder.getBean(DcPaperQuestionSearch.class);
        LocalDateTime now = LocalDateTime.now();
        String paperIdStr = jsonObject.getString(PAPERID);
        // 写入paper
        DcPaper dcPaper = new DcPaper();
        dcPaper.setPaperId(paperIdStr)
                .setPaperName(URLUtil.decode(paperName, CharsetUtil.CHARSET_UTF_8))
                .setInterfaceLogId(dcInterfaceLog.getId());
        dcPaper = savePaper(dcPaper);

        JSONArray paperQuestions = jsonObject.getJSONArray(PAPER_QUESTIONS);
        for (Object obj : paperQuestions) {
            JSONObject jsonObj = (JSONObject) obj;
            // 保存试题类型
            String topicId = jsonObj.getString(TOPIC_ID);
            String topicTypeCd = jsonObj.getString(TOPIC_TYPE_CD);
            String fulltopictypecd = jsonObj.getString(Fulltopictypecd);
            String questionTypeNm = jsonObj.getString(QUESTION_TYPE_NM);
            String topicTypeId = jsonObj.getString(TOPIC_TYPE_ID);
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
            Thread.sleep(100);

            JSONArray topicTrunk = jsonObj.getJSONArray(TOPIC_TRUNK);
            for (Object o : topicTrunk) {
                JSONObject object = (JSONObject) o;
                String questionId = object.getString(QUESTION_ID);
                String questionTitle = object.getString(QUESTION_TITLE).replaceAll(" ", "").trim();
                String topictrunkType = object.getString(TOPICTRUNK_TYPE);
                JSONArray questionOptions = object.getJSONArray(QUESTION_OPTIONS);
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
                Thread.sleep(100);
                List<DcQuestionOption> questionOptionList = new ArrayList<>();
                for (Object o3 : questionOptions) {
                    JSONObject o3jo = (JSONObject) o3;
                    String optionId = o3jo.getString(OPTION_ID).replaceAll(" ", "").trim();
                    String optionContent = o3jo.getString(OPTION_CONTENT).replaceAll(" ", "").trim();
                    String optionType = o3jo.getString(OPTION_TYPE);
                    String istrue = o3jo.getString(ISTRUE);
                    String selected = o3jo.getString("selected");
                    // select 为正确的情况
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
                            .setSelected(selected)
                            .setCreateBy(dcInterfaceLog.getCreateBy()));
                }
                // 写入选项-批量
                IDcQuestionOptionService questionOptionService = SpringContextHolder.getBean(IDcQuestionOptionService.class);
                questionOptionService.saveBatch(questionOptionList);
                // 写入es
                // questionSearch.insertByQuestionId(questionId);
            }
        }
    }
    /**
     * 保存paper
     *
     * @param dcPaper dcPaper
     * @return 回写过id的bean
     */
    private static DcPaper savePaper(DcPaper dcPaper) {
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
     * 保存试题类型，缓存
     *
     * @param dcPaperQuestionTopic dcPaperQuestionTopic
     * @return 返回回写过id的bean
     */
    private static DcPaperQuestionTopic saveQuestionTopic(DcPaperQuestionTopic dcPaperQuestionTopic) {
        // 是否存在
        IDcPaperQuestionTopicService service = SpringContextHolder.getBean(IDcPaperQuestionTopicService.class);
        List<DcPaperQuestionTopic> list = service.findByTopicTypeIds(dcPaperQuestionTopic.getTopicTypeId());
        if (list == null || list.size() == 0) {
            // 不存在
            dcPaperQuestionTopic.insert();
            return dcPaperQuestionTopic;
        } else {
            return list.get(0);
        }
    }

    /**
     * 判断question是否已存在记录,缓存
     *
     * @param questionId questionId
     * @return 已存在
     */
    public synchronized static boolean existsQuestion(String questionId) {
        IDcPaperQuestionService service = SpringContextHolder.getBean(IDcPaperQuestionService.class);
        List<DcPaperQuestion> byQuestionIds = service.findByQuestionIds(questionId);
        return byQuestionIds != null && byQuestionIds.size() > 0;
    }

}
