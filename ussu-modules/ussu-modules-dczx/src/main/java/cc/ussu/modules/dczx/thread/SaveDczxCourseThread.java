package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采集课程信息线程
 */
public class SaveDczxCourseThread extends Thread {

    private DcInterfaceLog dcInterfaceLog;

    public SaveDczxCourseThread(DcInterfaceLog dcInterfaceLog) {
        this.dcInterfaceLog = dcInterfaceLog;
    }

    @Override
    public void run() {
        // 缺少参数跳过
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog)) {
            return;
        }
        if (StrUtil.isBlank(dcInterfaceLog.getTime())) {
            return;
        }
        // 获取接口结果
        // url: https://classroom.edufe.com.cn/HomeWork?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=2&courseValue=B2001A&courseId=B2001A
        String baseUrl = "https://classroom.edufe.com.cn/api/v1/myclassroom/courseStudyPlan?userId={}&courseId={}";
        Map<String, Object> param = new HashMap<>();
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        param.put("userId", dcInterfaceLog.getUserid());
        param.put("courseId", decodeParams.get("courseId").get(0));
        baseUrl = StrUtil.format(baseUrl, dcInterfaceLog.getUserid(), decodeParams.get("courseId").get(0));
        dcInterfaceLog.setReqUrl(baseUrl);
        String rep = MyHttpRequest.createPost(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                // .form(param)
                .connectionKeepAlive()
                .execute()
                .body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtil.parseObj(rep);
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
            dcInterfaceLog.setRemarks("采集课程信息线程");
            DczxUtil.saveInterfaceLog(dcInterfaceLog);
        }
        if (jsonObject != null && dcInterfaceLog.getResult()) {
            // 解析保存
            analyzeResponse(jsonObject, decodeParams);
        }
    }

    /**
     * 解析结果保存结果
     */
    private synchronized void analyzeResponse(JSONObject jsonObject, Map<String, List<String>> decodeParams) {
        DcCourse dcCourse = jsonObject.toBean(DcCourse.class);
        if (dcCourse == null) {
            return;
        }
        if (DczxUtil.getRedisUtil().hHasKey(DczxUtil.COURSE_KEY_IN_REDIS, dcCourse.getCourseId())) {
            return;
        }
        Date now = new Date();
        dcCourse.setCreateTime(now).setInterfaceLogId(dcInterfaceLog.getId());
        SpringUtil.getBean(IDcCourseService.class).save(dcCourse);
        DczxUtil.getRedisUtil().setCacheMapValue(DczxUtil.COURSE_KEY_IN_REDIS, dcCourse.getCourseId(), dcCourse);
    }

}
