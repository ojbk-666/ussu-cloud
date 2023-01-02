package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.*;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.QuestionOption;
import cc.ussu.modules.dczx.model.vo.SimulatedQuestionResponse;
import cc.ussu.modules.dczx.model.vo.TopicTrunk;
import cc.ussu.modules.dczx.properties.DczxProperties;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模拟试题写入数据库
 */
@Service
public class SimulatedQuestionService {

    @Autowired
    private DczxProperties dczxProperties;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;
    @Autowired
    private IDcInterfaceLogService dcInterfaceLogService;
    @Autowired
    private IDcPaperQuestionService dcPaperQuestionService;

    /**
     * 保存模拟试题的所有题目
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(SimulatedQuestionResponse simulatedQuestionResponse, DcPaper dcPaper, DcInterfaceLog dcInterfaceLog, String courseId) {
        String paperid = simulatedQuestionResponse.getPAPERID();
        List<PaperQuestion> paperQuestionList = simulatedQuestionResponse.getPAPER_QUESTIONS();
        // 没有则跳过
        if (CollUtil.isEmpty(paperQuestionList)) {
            return;
        }
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        for (PaperQuestion paperQuestion : paperQuestionList) {
            // 写入题型
            DcPaperQuestionTopic dcPaperQuestionTopic = new DcPaperQuestionTopic()
                    .setPaperId(paperid)
                    .setTopicId(paperQuestion.getTOPIC_ID())
                    .setTopicTypeCd(paperQuestion.getTOPIC_TYPE_CD())
                    .setFullTopicTypeCd(paperQuestion.getFulltopictypecd())
                    .setQuestionTypeNm(paperQuestion.getQUESTION_TYPE_NM())
                    .setTopicTypeId(paperQuestion.getTOPIC_TYPE_ID())
                    .setInterfaceLogId(dcInterfaceLog.getId());
            DcPaperQuestionTopic dcTopic = DczxUtil.saveQuestionTopic(dcPaperQuestionTopic);
            // 保存题目
            List<TopicTrunk> topicTrunkList = paperQuestion.getTOPIC_TRUNK();
            for (TopicTrunk topicTrunk : topicTrunkList) {
                DcPaperQuestion dcPaperQuestion = new DcPaperQuestion()
                        .setQuestionId(topicTrunk.getQUESTION_ID())
                        .setQuestionTitle(topicTrunk.getQUESTION_TITLE())
                        .setCourseId(courseId)
                        .setPaperId(paperid)
                        .setTopicId(dcTopic.getTopicId())
                        .setFullTopicTypeCd(paperQuestion.getFulltopictypecd())
                        .setInterfaceLogId(dcInterfaceLog.getId());
                if (!DczxUtil.existQuestion(dcPaperQuestion.getQuestionId())) {
                    dcPaperQuestionService.save(dcPaperQuestion);
                    // 保存选项
                    List<DcQuestionOption> questionOptionList = new ArrayList<>();
                    for (QuestionOption questionOption : topicTrunk.getQUESTION_OPTIONS()) {
                        // html图片则下载图片到本地指定目录
                        String optionContent = DczxUtil.downloadImgToLocalFromHtmlStr(questionOption.getOPTION_CONTENT());
                        /*String optionContent = questionOption.getOPTION_CONTENT();
                        Set<String> imgSet = DczxUtil.getImgStr(optionContent);
                        if (CollUtil.isNotEmpty(imgSet)) {
                            // 下载图片到本地
                            for (String img : imgSet) {
                                int timeout = 10 * 1000;
                                if (img.startsWith(StrUtil.SLASH + StrUtil.SLASH)) {
                                    // 根地址开始下载
                                    // //exercises.edufe.com.cn/TKGL/uedit/jsp/upload/image/20191209/1575876291604009885.jpg
                                    String img1 = img.replaceAll("//", StrUtil.EMPTY);
                                    String filePathName = img.replaceFirst(img1.split(StrUtil.SLASH)[0], StrUtil.EMPTY);
                                    File imgInLocalPath = new File(dczxProperties.getQuestionImgLocal() + filePathName);
                                    HttpUtil.downloadFileFromUrl(dczxProperties.getQuestionImgDownloadDomain().split(StrUtil.COLON)[0] + img, imgInLocalPath, timeout);
                                    optionContent = optionContent.replaceAll(img, dczxProperties.getQuestionImgDomain() + filePathName);
                                } else {
                                    // 当前url相对路路径
                                    HttpUtil.downloadFileFromUrl(dczxProperties.getQuestionImgDownloadDomain() + img, new File(dczxProperties.getQuestionImgLocal() + img), timeout);
                                    optionContent = optionContent.replaceAll(img,dczxProperties.getQuestionImgDomain() + img);
                                }
                            }
                        }*/
                        DcQuestionOption dcQuestionOption = new DcQuestionOption()
                                .setPaperId(paperid)
                                .setQuestionId(dcPaperQuestion.getQuestionId())
                                .setOptionId(questionOption.getOPTION_ID())
                                .setOptionContent(optionContent)
                                .setOptionType(questionOption.getOPTION_TYPE())
                                .setInterfaceLogId(dcInterfaceLog.getId());
                        // 是否为正确答案 根据题型判断
                        boolean istrue = false;
                        String tixing = dcPaperQuestionTopic.getFullTopicTypeCd();
                        if ("001".equals(tixing)) {
                            // 单选
                            istrue = "1".equals(questionOption.getISTRUE());
                        } else if ("002".equals(tixing)) {
                            // 多选
                            istrue = "1".equals(questionOption.getISTRUE());
                        } else if ("004".equals(tixing)) {
                            // 判断题
                            istrue = "1".equals(questionOption.getISTRUE());
                        } else if ("005".equals(tixing)) {
                            // 简答题
                            istrue = true;
                        } else if ("37".equals(tixing)) {
                            // 交际用语
                        } else if ("41".equals(tixing)) {
                            // 词汇与结构
                        } else if ("901".equals(tixing)) {
                            // 古文阅读
                        } else if ("903".equals(tixing)) {
                            // 理解与辨析
                        } else if ("".equals(tixing)) {
                            // 未知
                        } else {
                            // 其他
                        }
                        dcQuestionOption.setIstrue(istrue);
                        questionOptionList.add(dcQuestionOption);
                    }
                    // 批量保存
                    dcQuestionOptionService.saveBatch(questionOptionList);
                }
            }

        }
        // DynamicDataSourceContextHolder.clear();
    }

    @Async
    public void collect(DcInterfaceLog dcInterfaceLog) {
        if (DczxUtil.existsInterfaceLog(dcInterfaceLog.getUrl())) {
            return;
        }
        String decode = URLUtil.decode(URLUtil.decode(dcInterfaceLog.getUrl()));
        Map<String, String> param = HttpUtil.decodeParamMap(decode, CharsetUtil.CHARSET_UTF_8);
        String tactic_id = MapUtil.getStr(param, "tactic_id");
        String courseCd = MapUtil.getStr(param, "courseCd");
        String batchValue = MapUtil.getStr(param, "batchValue");
        String gradationValue = MapUtil.getStr(param, "gradationValue");
        String paperName = MapUtil.getStr(param, "paperName");
        String subjectValue = MapUtil.getStr(param, "subjectValue");
        String exampapertypeValue = MapUtil.getStr(param, "exampapertypeValue");
        String courseId = MapUtil.getStr(param, "courseId");
        param.put("courseValue", courseCd);
        param.put("customerId", "1");
        dcInterfaceLog.setReqUrl(StrUtil.format("https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action?tactic_id={tactic_id}&batchValue={batchValue}&gradationValue={gradationValue}&subjectValue={subjectValue}&courseValue={courseValue}&exampapertypeValue={exampapertypeValue}&customerId={customerId}&_="+new Date().getTime(), param));
        String rep = HttpRequest.get(dcInterfaceLog.getReqUrl())
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                .execute().body();
        dcInterfaceLog.setResponseBody(rep).setResult(true).setRemarks("模拟试题题目采集").setDelFlag(false).setId(null);
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        dcInterfaceLogService.save(dcInterfaceLog);

        SimulatedQuestionResponse simulatedQuestionResponse = JSONUtil.toBean(rep, SimulatedQuestionResponse.class);
        if (simulatedQuestionResponse == null || simulatedQuestionResponse.getFinish() == null || StrUtil.isBlank(simulatedQuestionResponse.getPAPERID())) {
            // 请求失败了
            dcInterfaceLog.setResult(false).setReason(rep);
        } else {
            DcPaper dcPaper = new DcPaper().setPaperId(simulatedQuestionResponse.getPAPERID()).setPaperName(paperName)
                    .setInterfaceLogId(dcInterfaceLog.getId()).setCreateTime(new Date());
            dcPaper = DczxUtil.savePaper(dcPaper);
            this.add(simulatedQuestionResponse, dcPaper, dcInterfaceLog, courseId);
        }
        // DynamicDataSourceContextHolder.clear();
    }

    /**
     * 得到网页中图片的地址
     */
    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern pattern;
        Matcher matcher;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        pattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(htmlStr);
        while (matcher.find()) {
            // 得到<img />数据
            img = matcher.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

}
