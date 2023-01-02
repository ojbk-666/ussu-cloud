package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.studyplan.AllStudyPlanResponseItem;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import cc.ussu.modules.dczx.service.IDcTaskVideoService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Date;
import java.util.List;

/**
 * 通过全部学习计划采集课程信息线程
 */
public class SaveDczxCourseByAllStudyPlanThread extends Thread {

    private DczxLoginResultVo loginResultVo;
    private DcInterfaceLog dcInterfaceLog;

    public SaveDczxCourseByAllStudyPlanThread(DczxLoginResultVo loginResultVo) {
        this.loginResultVo = loginResultVo;
        this.dcInterfaceLog = new DcInterfaceLog().setUserid(loginResultVo.getUserId())
                .setTime(loginResultVo.getTime()).setSign(loginResultVo.getSign())
                .setAccessToken(loginResultVo.getAccesstoken());
    }

    @Override
    public void run() {
        // 缺少参数跳过
        if (loginResultVo == null) {
            return;
        }
        // 获取接口结果
        // url: https://classroom.edufe.com.cn/api/v1/myclassroom/allStudyPlan
        String baseUrl = "https://classroom.edufe.com.cn/api/v1/myclassroom/allStudyPlan?userId=" + loginResultVo.getUserId();
        // 判断是否已经采集过
        synchronized (this) {
            IDcInterfaceLogService interfaceLogService = SpringUtil.getBean(IDcInterfaceLogService.class);
            if (interfaceLogService.count(Wrappers.lambdaQuery(DcInterfaceLog.class).eq(DcInterfaceLog::getReqUrl, baseUrl)) > 0) {
                return;
            }
            dcInterfaceLog.setReqUrl(baseUrl);
            String rep = MyHttpRequest.createPost(baseUrl)
                    .headerMap(loginResultVo.getHeaderMap(), true)
                    .disableCookie()
                    .cookie(loginResultVo.getRequestCookie())
                    .connectionKeepAlive()
                    .execute()
                    .body();
            dcInterfaceLog.setResponseBody(rep).setResult(true);
            JSONObject jsonObject = null;
            List<AllStudyPlanResponseItem> list = null;
            try {
                if (JSONUtil.isJsonObj(rep)) {
                    // 有异常
                    jsonObject = JSONUtil.parseObj(rep);
                    String code = jsonObject.getStr(DczxUtil.code);
                    if (StrUtil.isNotBlank(code)) {
                        dcInterfaceLog.setResult(false);
                        dcInterfaceLog.setReason(jsonObject.getStr(DczxUtil.msg));
                    }
                } else if (JSONUtil.isJsonArray(rep)) {
                    // 正常返回
                    list = JSONUtil.toList(rep, AllStudyPlanResponseItem.class);
                }
            } catch (JSONException e) {
                // 出错
                dcInterfaceLog.setResult(false);
                dcInterfaceLog.setReason("josn解析失败");
            } finally {
                dcInterfaceLog.setRemarks("通过全部学习计划采集课程信息线程");
                DczxUtil.saveInterfaceLog(dcInterfaceLog);
            }
            if (jsonObject == null && CollUtil.isNotEmpty(list)) {
                // 解析保存
                analyzeResponse(list);
            }
        }
    }

    /**
     * 解析结果保存结果
     */
    private synchronized void analyzeResponse(List<AllStudyPlanResponseItem> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        IDcCourseService courseService = getICourseService();
        Date now = new Date();
        for (AllStudyPlanResponseItem studyPlanResponseItem : list) {
            List<DcCourse> courseList = studyPlanResponseItem.getCourseList();
            if (CollUtil.isEmpty(courseList)) {
                return;
            }
            for (DcCourse dcCourse : courseList) {
                if (!existsCourse(dcCourse.getCourseId())) {
                    // course
                    dcCourse.setCreateTime(now).setInterfaceLogId(dcInterfaceLog.getId());
                    courseService.save(dcCourse);
                    DczxUtil.getRedisUtil().setCacheMapValue(DczxUtil.COURSE_KEY_IN_REDIS, dcCourse.getCourseId(), dcCourse);
                }
                try {
                    SpringUtil.getBean(IDcTaskVideoService.class).saveVideoByStudyPlan(loginResultVo.getJsessionid(), loginResultVo.getUserId(), dcCourse.getCourseId());
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 判断是否已存在记录
     *
     * @param courseId
     * @return
     */
    public boolean existsCourse(String courseId) {
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        // 判断缓存
        return redisService.hHasKey(DczxUtil.COURSE_KEY_IN_REDIS, courseId);
    }

    private IDcCourseService getICourseService() {
        return SpringUtil.getBean(IDcCourseService.class);
    }

}
