package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.entity.*;
import cc.ussu.modules.dczx.model.vo.*;
import cc.ussu.modules.dczx.properties.DczxProperties;
import cc.ussu.modules.dczx.service.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String SLAVE = "slave";
    private static final String DCZX_CACHE_KEY_PREFIX = "dczx:";
    public static final String QUESTION_ID_KEY_IN_REDIS = DCZX_CACHE_KEY_PREFIX + "questionId";
    public static final String COURSE_KEY_IN_REDIS = DCZX_CACHE_KEY_PREFIX + "course";
    public static final String PAPER_KEY_IN_REDIS = DCZX_CACHE_KEY_PREFIX + "paper";
    public static final String TOPIC_KEY_IN_REDIS = DCZX_CACHE_KEY_PREFIX + "topic";

    //开关
    public static final String THREAD_SWITCH_PREFIX = DCZX_CACHE_KEY_PREFIX + "enable:";
    public static final String THREAD_SWITCH_USERINFO = THREAD_SWITCH_PREFIX + "get_user_info";
    public static final String THREAD_SWITCH_COURSE = THREAD_SWITCH_PREFIX + "get_course_info";
    public static final String THREAD_SWITCH_QUESTION = THREAD_SWITCH_PREFIX + "get_question";

    public static boolean checkInterfaceLogParam(DcInterfaceLog dcInterfaceLog) {
        if (dcInterfaceLog == null) {
            return false;
        }
        if (StrUtil.isBlank(dcInterfaceLog.getUrl())) {
            return false;
        }
        if (StrUtil.isBlank(dcInterfaceLog.getAccessToken())) {
            return false;
        }
        if (StrUtil.isBlank(dcInterfaceLog.getSign())) {
            return false;
        }
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
    public synchronized static void analyzeQuestionJson(DcInterfaceLog dcInterfaceLog, JSONObject jsonObject, String paperName) throws Exception {
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        // DcPaperQuestionSearch questionSearch = SpringUtil.getBean(DcPaperQuestionSearch.class);
        Date now = new Date();
        SimulatedQuestionResponse questionResponse = jsonObject.toBean(SimulatedQuestionResponse.class);
        String paperIdStr = questionResponse.getPAPERID();
        // String paperIdStr = jsonObject.getStr(PAPERID);
        // courseIdStr
        String dcInterfaceLogUrl = dcInterfaceLog.getUrl();
        String tempurl1 = dcInterfaceLogUrl.split("courseCd=")[1];
        String tempurl2 = tempurl1.split("&batchValue")[0];
        String courseIdStr = null;
        if (tempurl2.indexOf("_") != -1) {
            QueryWrapper<DcCourse> tempqw = new QueryWrapper<>();
            tempqw.eq("service_course_vers_id", tempurl2);
            IDcCourseService tempservice1 = SpringUtil.getBean(IDcCourseService.class);
            List<DcCourse> templist = tempservice1.list(tempqw);
            if (CollUtil.isNotEmpty(templist)) {
                courseIdStr = templist.get(0).getCourseId();
            }
        } else {
            courseIdStr = tempurl2;
        }
        // 写入paper
        DcPaper dcPaper = new DcPaper();
        dcPaper.setPaperId(paperIdStr)
                .setPaperName(URLUtil.decode(paperName, CharsetUtil.CHARSET_UTF_8))
                .setInterfaceLogId(dcInterfaceLog.getId()).setCreateTime(now);
        dcPaper = savePaper(dcPaper);

        List<PaperQuestion> paperQuestions = questionResponse.getPAPER_QUESTIONS();
        // JSONArray paperQuestions = jsonObject.getJSONArray(PAPER_QUESTIONS);
        // for (Object obj : paperQuestions) {
        for (PaperQuestion pq : paperQuestions) {
            // JSONObject jsonObj = (JSONObject) obj;
            // 保存试题类型
            /*String topicId = jsonObj.getStr(TOPIC_ID);
            String topicTypeCd = jsonObj.getStr(TOPIC_TYPE_CD);
            String fulltopictypecd = jsonObj.getStr(Fulltopictypecd);
            String questionTypeNm = jsonObj.getStr(QUESTION_TYPE_NM);
            String topicTypeId = jsonObj.getStr(TOPIC_TYPE_ID);*/
            String topicId = pq.getTOPIC_ID();
            String topicTypeCd = pq.getTOPIC_TYPE_CD();
            String fulltopictypecd = pq.getFulltopictypecd();
            String questionTypeNm = pq.getQUESTION_TYPE_NM();
            String topicTypeId = pq.getTOPIC_TYPE_ID();
            DcPaperQuestionTopic dcPaperQuestionTopic = new DcPaperQuestionTopic();
            dcPaperQuestionTopic.setTopicId(topicId)
                    .setTopicTypeCd(topicTypeCd)
                    .setFullTopicTypeCd(fulltopictypecd)
                    .setQuestionTypeNm(questionTypeNm)
                    .setTopicTypeId(topicTypeId)
                    // .setPaperIdLong(dcPaper.getId())
                    .setPaperId(dcPaper.getPaperId())
                    .setInterfaceLogId(dcInterfaceLog.getId());
            dcPaperQuestionTopic = saveQuestionTopic(dcPaperQuestionTopic);
            Thread.sleep(100);

            // JSONArray topicTrunk = jsonObj.getJSONArray(TOPIC_TRUNK);
            List<TopicTrunk> topicTrunk = pq.getTOPIC_TRUNK();
            // for (Object o : topicTrunk) {
            for (TopicTrunk tt : topicTrunk) {
                // JSONObject object = (JSONObject) o;
                // String questionId = object.getStr(QUESTION_ID);
                // String questionTitle = object.getStr(QUESTION_TITLE).replaceAll(" ", "").trim();
                // String topictrunkType = object.getStr(TOPICTRUNK_TYPE);
                // JSONArray questionOptions = object.getJSONArray(QUESTION_OPTIONS);
                String questionId = tt.getQUESTION_ID();
                String questionTitle = tt.getQUESTION_TITLE().replaceAll(" ", "").trim();
                String topictrunkType = tt.getTOPICTRUNK_TYPE();
                // JSONArray questionOptions = object.getJSONArray(QUESTION_OPTIONS);
                List<QuestionOption> questionOptions = tt.getQUESTION_OPTIONS();
                // 跳过已有记录
                if (existQuestion(questionId)) {
                    continue;
                }
                // 写入题目
                DcPaperQuestion dpq = new DcPaperQuestion();
                dpq.setQuestionId(questionId)
                        // .setPaperIdLong(dcPaper.getId())
                        .setPaperId(paperIdStr)
                        // .setTopicIdLong(dcPaperQuestionTopic.getId())
                        .setTopicId(topicId)
                        .setQuestionTitle(questionTitle)
                        .setFullTopicTypeCd(pq.getFulltopictypecd())
                        .setInterfaceLogId(dcInterfaceLog.getId())
                        .setCreateBy(dcInterfaceLog.getCreateBy())
                        .setCreateTime(now)
                        .setCourseId(courseIdStr);
                saveQuestion(dpq);
                // Thread.sleep(100);
                List<DcQuestionOption> questionOptionList = new ArrayList<>();
                // for (Object o3 : questionOptions) {
                for (QuestionOption qo : questionOptions) {
                    // JSONObject o3jo = (JSONObject) o3;
                    // String optionId = o3jo.getStr(OPTION_ID).replaceAll(" ", "").trim();
                    // String optionContent = o3jo.getStr(OPTION_CONTENT).replaceAll(" ", "").trim();
                    // String optionType = o3jo.getStr(OPTION_TYPE);
                    // String istrue = o3jo.getStr(ISTRUE);
                    // String selected = o3jo.getStr("selected");
                    String optionId = qo.getOPTION_ID().replaceAll(" ", "").trim();
                    String optionContent = qo.getOPTION_CONTENT().replaceAll(" ", "").trim();
                    String optionType = qo.getOPTION_TYPE();
                    String istrue = qo.getISTRUE();
                    // String selected = ((String) qo.getSelected());
                    // select 为正确的情况
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
                // 写入es
                try {
                    // questionSearch.insertByQuestionId(questionId);
                } catch (Exception e) {
                    System.out.println(StrUtil.format("试题[{}]写入 Elastic Search 失败", questionId));
                }
            }
        }
    }
    /**
     * 保存paper
     *
     * @param dcPaper dcPaper
     * @return 回写过id的bean
     */
    public synchronized static DcPaper savePaper(DcPaper dcPaper) {
        RedisService redisService = DczxUtil.getRedisUtil();
        String key = DczxUtil.PAPER_KEY_IN_REDIS;
        DcPaper cache = (DcPaper) redisService.getCacheMapValue(key, dcPaper.getPaperId());
        if (cache != null) {
            return cache;
        } else {
            SpringUtil.getBean(IDcPaperService.class).save(dcPaper);
            redisService.setCacheMapValue(key, dcPaper.getPaperId(), dcPaper);
            return dcPaper;
        }
    }

    /**
     * 保存试题类型，缓存
     *
     * @param dcPaperQuestionTopic dcPaperQuestionTopic
     * @return 返回回写过id的bean
     */
    public synchronized static DcPaperQuestionTopic saveQuestionTopic(DcPaperQuestionTopic dcPaperQuestionTopic) {
        if (dcPaperQuestionTopic == null) {
            return null;
        }
        RedisService redisService = DczxUtil.getRedisUtil();
        String key = DczxUtil.TOPIC_KEY_IN_REDIS;
        DcPaperQuestionTopic cache = (DcPaperQuestionTopic) redisService.getCacheMapValue(key, dcPaperQuestionTopic.getFullTopicTypeCd());
        if (cache != null) {
            return cache;
        } else {
            SpringUtil.getBean(IDcPaperQuestionTopicService.class).save(dcPaperQuestionTopic);
            redisService.setCacheMapValue(key, dcPaperQuestionTopic.getFullTopicTypeCd(), dcPaperQuestionTopic);
            return dcPaperQuestionTopic;
        }
    }

    /**
     * 判断试题是否存在
     */
    public synchronized static boolean existQuestion(String questionId) {
        RedisService redisService = getRedisUtil();
        return redisService.sHasKey(QUESTION_ID_KEY_IN_REDIS, questionId);
    }

    /**
     * 保存试题
     *
     * @param dcPaperQuestion dcPaperQuestion
     * @return 返回回写过id的bean
     */
    public synchronized static DcPaperQuestion saveQuestion(DcPaperQuestion dcPaperQuestion) {
        // 是否存在
        if (!existQuestion(dcPaperQuestion.getQuestionId())) {
            // 写入
            SpringUtil.getBean(IDcPaperQuestionService.class).save(dcPaperQuestion);
            getRedisUtil().setCacheSet(QUESTION_ID_KEY_IN_REDIS, CollUtil.newHashSet(dcPaperQuestion.getQuestionId()));
        }
        return dcPaperQuestion;
    }

    /**
     * 是否已采集过
     */
    public synchronized static boolean existsInterfaceLog(String url) {
        if (StrUtil.isBlank(url)) {
            return true;
        }
        IDcInterfaceLogService interfaceLogService = SpringUtil.getBean(IDcInterfaceLogService.class);
        return interfaceLogService.count(Wrappers.lambdaQuery(DcInterfaceLog.class).eq(DcInterfaceLog::getUrl, url)) > 0;
    }

    public static RedisService getRedisUtil() {
        return SpringUtil.getBean(RedisService.class);
    }

    private static IDcPaperQuestionService getIDcPaperQuestionService() {
        return SpringUtil.getBean(IDcPaperQuestionService.class);
    }

    public synchronized static void saveInterfaceLog(DcInterfaceLog dcInterfaceLog) {
        if (dcInterfaceLog == null || StrUtil.isBlank(dcInterfaceLog.getUrl())) {
            return;
        }
        dcInterfaceLog.setId(null).setDelFlag(false);
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        SpringUtil.getBean(IDcInterfaceLogService.class).save(dcInterfaceLog);
        // DynamicDataSourceContextHolder.clear();
    }

    /**
     * 得到html字符串中的图片的地址
     */
    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern pattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);;
        Matcher matcher = pattern.matcher(htmlStr);
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

    /**
     * 将html字符串中的图片下载到本地并替换为可访问地址{@link DczxProperties#getQuestionImgDomain()}
     *
     * @param htmlStr html字符串
     * @return 替换为本项目域名的图片地址字符串
     */
    public static String downloadImgToLocalFromHtmlStr(String htmlStr) {
        Set<String> imgSet = getImgStr(htmlStr);
        String doubleSlash = StrPool.SLASH + StrPool.SLASH;
        if (CollUtil.isNotEmpty(imgSet)) {
            DczxProperties dczxProperties = SpringUtil.getBean(DczxProperties.class);
            for (String img : imgSet) {
                if (img.startsWith(doubleSlash)) {
                    String s = img.replaceAll(doubleSlash, StrUtil.EMPTY).split(StrPool.SLASH)[0];
                    String fileName = img.replaceFirst(doubleSlash + s, StrUtil.EMPTY);
                    // HttpUtil.downloadFileFromUrl("https:" + img, FileUtil.file(dczxProperties.getQuestionImgLocal() + fileName), timeout);
                    MyHttpRequest.downloadFileFromUrl("https:" + img, FileUtil.file(dczxProperties.getQuestionImgLocal() + fileName));
                    htmlStr = StrUtil.replace(htmlStr, img, dczxProperties.getQuestionImgDomain() + fileName);
                } else {
                    // HttpUtil.downloadFileFromUrl(dczxProperties.getQuestionImgDownloadDomain() + img, FileUtil.file(dczxProperties.getQuestionImgLocal() + img), timeout);
                    MyHttpRequest.downloadFileFromUrl(dczxProperties.getQuestionImgDownloadDomain() + img, FileUtil.file(dczxProperties.getQuestionImgLocal() + img));
                    htmlStr = StrUtil.replace(htmlStr, img, dczxProperties.getQuestionImgDomain() + img);
                }
            }
        }
        return htmlStr;
    }

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @return
     */
    public static DczxLoginResultVo login(String loginName, String password) {
        return SpringUtil.getBean(DczxService.class).login(loginName, password);
    }

}
