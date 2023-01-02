package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.exception.homework.GetExamScoremcapiException;
import cc.ussu.modules.dczx.exception.homework.InitWorkListmcapiException;
import cc.ussu.modules.dczx.exception.homework.QueryHomeworkScoreException;
import cc.ussu.modules.dczx.exception.homework.ShowExamPapermcapiException;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.StudyPlanVO;
import cc.ussu.modules.dczx.model.vo.homework.InitWorkListmcapiResponse;
import cc.ussu.modules.dczx.model.vo.homework.SavemcapiParam;
import cc.ussu.modules.dczx.model.vo.homework.ShowExamPapermcapiResponse;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;

/**
 * 单元作业工具
 */
public final class HomeworkUtil {

    /**
     * 获取首页的学习计划跳转到单元作业的url
     *
     * @param studyPlanVO
     * @return
     */
    public static String getHomeworkPageUrl(StudyPlanVO studyPlanVO) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        return new StringBuffer()
                .append("https://classroom.edufe.com.cn/HomeWork")
                .append("?batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=").append("2")
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&courseId=").append(studyPlanVO.getCourseId())
                .toString();
    }

    /**
     * 查询单元作业分数
     *
     * @param loginResultVo
     * @param courseId
     * @return
     */
    public static Float queryHomeworkScore(DczxLoginResultVo loginResultVo, String courseId) {
        MyHttpResponse execute = MyHttpRequest.createPost(StrUtil.format("https://classroom.edufe.com.cn/api/v1/myclassroom/queryHomeworkScore?userId={}&courseId={}", loginResultVo.getUserId(), courseId))
            .disableCookie().cookie(loginResultVo.getRequestCookie())
            .headerMap(loginResultVo.getHeaderMap(), true)
            .connectionKeepAlive().execute();
        if (execute.isOk()) {
            return Float.parseFloat(execute.body());
        } else {
            throw new QueryHomeworkScoreException();
        }
    }

    /**
     * 获取已有单元作业列表
     */
    public static InitWorkListmcapiResponse getHomeworkList(DczxLoginResultVo loginResultVo, StudyPlanVO studyPlanVO) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        String url = new StringBuffer("https://classroom.edufe.com.cn/TKGL/initWorkListmcapi.action")
                .append("?batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=2").append("")
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&customerId=").append("1")
                .append("&_=").append(new Date().getTime())
                .toString();
        MyHttpResponse execute = MyHttpRequest.createGet(url)
                .disableCookie().cookie(loginResultVo.getRequestCookie())
                .headerMap(loginResultVo.getHeaderMap(), true)
                .connectionKeepAlive().execute();
        if (execute.isOk()) {
            return JSONUtil.toBean(execute.body(),  InitWorkListmcapiResponse.class);
        } else {
            throw new InitWorkListmcapiException();
        }
    }

    /**
     * 获取作业单元作业列表url
     */
    public static String getShowExamPapermcapiUrl(StudyPlanVO studyPlanVO, String workerId) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        String url = new StringBuffer("https://classroom.edufe.com.cn/TKGL/showExamPapermcapi.action")
                .append("?tactic_id=").append(workerId)
                .append("&batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&chapterssections_id=")
                .append("&exampapertypeValue=").append("2")
                .append("&customerId=1")
                // sign 解密未完成 不提交该参数也可以
                // .append("&sign=31cc8265beeb801e64ce4ce7e13b92b5283ff7ea")
                .append("&_=").append(new Date().getTime())
                .toString();
        return url;
    }

    /**
     * 开始新的单元作业
     *
     * @param loginResultVo
     * @param studyPlanVO
     * @return
     */
    public static ShowExamPapermcapiResponse showExamPapermcapi(DczxLoginResultVo loginResultVo, StudyPlanVO studyPlanVO, String workerId) {
        MyHttpResponse execute = MyHttpRequest.createGet(getShowExamPapermcapiUrl(studyPlanVO, workerId))
                .headerMap(loginResultVo.getHeaderMap(), true)
                .disableCookie().cookie(loginResultVo.getRequestCookie())
                .connectionKeepAlive().execute();
        if (execute.isOk()) {
            return JSONUtil.toBean(execute.body(), ShowExamPapermcapiResponse.class);
        } else {
            throw new ShowExamPapermcapiException();
        }
    }

    /**
     * 题目转换为保存题目的进度参数
     *
     * @param paperQuestion
     * @return
     */
    public static SavemcapiParam convert(PaperQuestion paperQuestion, String paperId, String rightOptionId) {
        // 试卷id
        SavemcapiParam savemcapiParam = new SavemcapiParam().setExamPaper_Id(paperId)
                .setTopictype_id(paperQuestion.getTOPIC_TYPE_ID())
                .setTopic_id(paperQuestion.getTOPIC_ID())
                // 题目id
                .setTopictrunk_id(paperQuestion.getTOPIC_TRUNK().get(0).getQUESTION_ID())
                // 答案id
                .setGeneralanswer(rightOptionId)
                // 选中
                .setChecked(SavemcapiParam.CHECKED_ON)
                .setExampapertypeValue("2");
        if (DcPaperQuestionTopic.DUO_XUAN.equals(paperQuestion.getFulltopictypecd())) {
            // 多选题
            savemcapiParam.setType("chk");
        }
        return savemcapiParam;
    }

    /**
     * 保存单元作业进度
     *
     * @param param
     * @return
     */
    public static boolean savemcapi(DczxLoginResultVo loginResultVo, SavemcapiParam param) {
        MyHttpResponse execute = MyHttpRequest.createPost("https://classroom.edufe.com.cn/TKGL/savemcapi.action")
            .contentType(ContentType.FORM_URLENCODED.getValue())
            .headerMap(loginResultVo.getHeaderMap(), true)
            .disableCookie()
            .cookie(loginResultVo.getRequestCookie())
            .form(BeanUtil.beanToMap(param))
            .connectionKeepAlive()
            .execute();
        if (execute != null) {
            return execute.isOk();
        }
        throw new RuntimeException("没有响应信息");
    }

    /**
     * 提交单元作业
     *
     * @param loginResultVo
     */
    public static Float getExamScoremcapi(DczxLoginResultVo loginResultVo, StudyPlanVO studyPlanVO,String paperid, String workerId) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        // 检查提交信息
        /*HttpResponse checkSubmitResponse = HttpUtil.createGet("https://classroom.edufe.com.cn/TKGL/checkSubmitmcapi.action" +
                        "?userexampaper_id=undefined&examPaper_Id=" + paperid +
                        "&_=" + new Date().getTime())
                .headerMap(loginResultVo.getHeaderMap(), true)
                .cookie(loginResultVo.getRequestCookie())
                .timeout(TIMEOUT)
                .execute();
        if (checkSubmitResponse.isOk()) {
            // 检查答案
            HttpResponse checkAnswerResponse = HttpUtil.createGet("https://classroom.edufe.com.cn/TKGL/checkAnswerCountmcapi.action" +
                            "?examPaper_Id=" + paperid +
                            "&exampapertypeValue=2&_=" + new Date().getTime())
                    .headerMap(loginResultVo.getHeaderMap(), true)
                    .cookie(loginResultVo.getRequestCookie())
                    .timeout(TIMEOUT)
                    .execute();
            if (checkAnswerResponse.isOk()) {

            }
        }*/
        String url = new StringBuffer("https://classroom.edufe.com.cn/TKGL/getExamScoremcapi.action")
                .append("?examPaper_Id=").append(paperid)
                .append("&tactic_id=").append(workerId)
                .append("&batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=2").append("")
                .append("&courseId=").append(buttonList.getCourseValue())
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&customerId=1").append("")
                .append("&userexampaper_id=undefined").append("")
                // .append("&sign=31cc8265beeb801e64ce4ce7e13b92b5283ff7ea").append("")
                .append("&_=").append(new Date().getTime())
                .toString();
        MyHttpResponse execute = MyHttpRequest.createGet(url)
                .headerMap(loginResultVo.getHeaderMap(), true)
                .disableCookie().cookie(loginResultVo.getRequestCookie())
                .connectionKeepAlive()
                .execute();
        if (execute.isOk()) {
            JSONObject jsonObject = JSONUtil.parseObj(execute.body());
            Float score = jsonObject.getFloat("score");
            return score;
        } else {
            throw new GetExamScoremcapiException();
        }
    }

}
