package cc.ussu.modules.dczx.util;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.dczx.exception.InitExamListmcapiException;
import cc.ussu.modules.dczx.exception.homework.GetExamScoremcapiException;
import cc.ussu.modules.dczx.exception.homework.ShowExamPapermcapiException;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.StudyPlanVO;
import cc.ussu.modules.dczx.model.vo.composite.InitExamListmcapiVo;
import cc.ussu.modules.dczx.model.vo.composite.SaveForMCRExammcapiParam;
import cc.ussu.modules.dczx.model.vo.homework.ShowExamPapermcapiResponse;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 综合作业工具
 */
public final class CompHomeworkUtil {

    /**
     * 获取首页的学习计划跳转到综合作业的url
     *
     * @param studyPlanVO
     * @return
     * @deprecated 未修改 未验证 不要使用
     */
    @Deprecated
    public static String getHomeworkPageUrl(StudyPlanVO studyPlanVO) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        return new StringBuffer()
                .append("https://classroom.edufe.com.cn/HomeWork")
                .append("?batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=").append("7")
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&courseId=").append(studyPlanVO.getCourseId())
                .toString();
    }

    /**
     * 获取已有综合作业列表
     */
    public static InitExamListmcapiVo getCompHomeworkList(DczxLoginResultVo loginResultVo, StudyPlanVO studyPlanVO) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        String url = new StringBuffer("https://classroom.edufe.com.cn/TKGL/initExamListmcapi.action")
                .append("?batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=").append("7")
                .append("&courseValue=").append(buttonList.getCourseValue())
                .append("&customerId=").append("1")
                .append("&_=").append(new Date().getTime())
                .toString();
        MyHttpResponse execute = MyHttpRequest.createGet(url)
                .disableCookie().cookie(loginResultVo.getRequestCookie())
                .headerMap(loginResultVo.getHeaderMap(), true)
                .connectionKeepAlive().execute();
        if (execute.isOk()) {
            return JSONUtil.toBean(execute.body(),  InitExamListmcapiVo.class);
        } else {
            throw new InitExamListmcapiException();
        }
    }

    /**
     * 获取作业综合作业列表url
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
                .append("&exampapertypeValue=").append("7")
                .append("&customerId=1")
                // sign 解密未完成 不提交该参数也可以
                // .append("&sign=487ad8c5bce5d33cffe2710621ecd5277b6e5e29")
                .append("&_=").append(new Date().getTime())
                .toString();
        return url;
    }

    /**
     * 开始新的综合作业
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
    public static List<SaveForMCRExammcapiParam> convert(PaperQuestion paperQuestion, String paperId, String rightOptionId) {
        // 试卷id
        SaveForMCRExammcapiParam savemcapiParam = new SaveForMCRExammcapiParam().setPaperId(paperId)
                .setPaperType("7")
                .setTypeId(paperQuestion.getTOPIC_TYPE_ID())
                // 题目id
                .setQuestionId(paperQuestion.getTOPIC_ID())
                // 答案id
                .setTrunkId(paperQuestion.getTOPIC_TRUNK().get(0).getQUESTION_ID())
                .setType("").setAnswer(rightOptionId)
                // 选中
                .setChecked("on");
        return savemcapiParam.getParam();
    }

    /**
     * 保存综合作业进度
     *
     * @param param
     * @return
     */
    public static boolean saveForMCRExammcapi(DczxLoginResultVo loginResultVo, List<SaveForMCRExammcapiParam> param) {
        Map<String, Object> form = new HashMap<>();
        form.put("topicTypeAnswer", JSONUtil.toJsonStr(param));
        MyHttpResponse execute = MyHttpRequest.createPost("https://classroom.edufe.com.cn/TKGL/saveForMCRExammcapi.action")
            .contentType(ContentType.FORM_URLENCODED.getValue())
            .headerMap(loginResultVo.getHeaderMap(), true)
            .disableCookie()
            .cookie(loginResultVo.getRequestCookie())
            .form(form)
            .connectionKeepAlive()
            .execute();
        if (execute != null) {
            return execute.isOk();
        }
        throw new RuntimeException("没有响应信息");
    }

    /**
     * 提交综合作业
     *
     * @param loginResultVo
     */
    public static Float getExamScoremcapi(DczxLoginResultVo loginResultVo, StudyPlanVO studyPlanVO,String paperid, String workerId, String userexampaper_id) {
        StudyPlanVO.ButtonList buttonList = studyPlanVO.getButtonList();
        String url = new StringBuffer("https://classroom.edufe.com.cn/TKGL/getExamScoremcapi.action")
                .append("?examPaper_Id=").append(paperid)
                .append("&tactic_id=").append(workerId)
                .append("&batchValue=").append(buttonList.getBatchValue())
                .append("&gradationValue=").append(buttonList.getGradationValue())
                .append("&subjectValue=").append(buttonList.getSubjectValue())
                .append("&exampapertypeValue=").append("7")
                .append("&examResultId=").append("")
                .append("&courseId=").append(buttonList.getCourseValue())
                .append("&customerId=1").append("")
                .append("&userexampaper_id=").append("userexampaper_id")
                .append("&courseValue=").append(buttonList.getCourseValue())
                // .append("&sign=").append("487ad8c5bce5d33cffe2710621ecd5277b6e5e29")
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
