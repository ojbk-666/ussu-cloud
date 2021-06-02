package cn.ussu.modules.dczx.thread;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.modules.dczx.entity.DcCourse;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.service.IDcCourseService;
import cn.ussu.modules.dczx.util.DczxUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
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
        if (!DczxUtil.checkInterfaceLogParam(dcInterfaceLog)) return;
        if (StrUtil.isBlank(dcInterfaceLog.getTime())) return;
        // 获取接口结果
        // url: https://classroom.edufe.com.cn/HomeWork?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=2&courseValue=B2001A&courseId=B2001A
        String baseUrl = "https://classroom.edufe.com.cn/api/v1/myclassroom/courseStudyPlan?userId={}&courseId={}";
        Map<String, Object> param = new HashMap<>();
        Map<String, List<String>> decodeParams = HttpUtil.decodeParams(dcInterfaceLog.getUrl(), CharsetUtil.UTF_8);
        param.put("userId", dcInterfaceLog.getUserid());
        param.put("courseId", decodeParams.get("courseId").get(0));
        baseUrl = StrUtil.format(baseUrl, dcInterfaceLog.getUserid(), decodeParams.get("courseId").get(0));
        dcInterfaceLog.setReqUrl(baseUrl);
        String rep = HttpRequest.post(baseUrl)
                .headerMap(DczxUtil.getHeaderMap(dcInterfaceLog), true)
                // .form(param)
                .execute().body();
        dcInterfaceLog.setResponseBody(rep);
        dcInterfaceLog.setResult(true);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(rep);
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
            dcInterfaceLog.setRemarks("采集课程信息线程");
            saveInterfaceLog();
        }
        if (jsonObject != null && dcInterfaceLog.getResult()) {
            // 解析保存
            analyzeResponse(jsonObject, decodeParams);
        }
    }

    // 保存接口日志
    private void saveInterfaceLog() {
        dcInterfaceLog.insert();
    }

    /**
     * 解析结果保存结果
     */
    private void analyzeResponse(JSONObject jsonObject, Map<String, List<String>> decodeParams) {
        DcCourse dcCourse = jsonObject.toJavaObject(DcCourse.class);
        if (dcCourse == null) return;
        if (existsCourse(dcCourse.getCourseId())) return;
        LocalDateTime now = LocalDateTime.now();
        dcCourse.setCreateTime(now).setInterfaceLogId(dcInterfaceLog.getId()).insert();
    }

    /**
     * 判断是否已存在记录
     *
     * @param courseId
     * @return
     */
    public boolean existsCourse(String courseId) {
        IDcCourseService service = SpringContextHolder.getBean(IDcCourseService.class);
        QueryWrapper<DcCourse> qw = new QueryWrapper<>();
        qw.eq("course_id", courseId);
        int count = service.count(qw);
        return count > 0;
    }

}
