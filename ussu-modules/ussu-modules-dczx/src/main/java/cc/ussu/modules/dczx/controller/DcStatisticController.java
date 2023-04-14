package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.DcUserStudyPlan;
import cc.ussu.modules.dczx.entity.vo.DcTaskVO;
import cc.ussu.modules.dczx.entity.vo.DcUserStudyPlanVO;
import cc.ussu.modules.dczx.mapper.DcStatisticMapper;
import cc.ussu.modules.dczx.service.*;
import cc.ussu.modules.dczx.util.RequestCountUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/statistic")
public class DcStatisticController extends BaseController {

    @Autowired
    private DcStatisticMapper dcStatisticMapper;
    @Autowired
    private IDcPaperQuestionService dcPaperQuestionService;
    @Autowired
    private IDcCourseService dcCourseService;
    @Autowired
    private IDcPaperQuestionTopicService dcPaperQuestionTopicService;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;
    @Autowired
    private IDcTaskService dcTaskService;
    @Autowired
    private IDcTrusteeshipService dcTrusteeshipService;
    @Autowired
    private IDcUserStudyPlanService dcUserStudyPlanService;

    /**
     * 统计数量相关
     */
    @GetMapping("/countNum")
    public Object countNum() {
        Map<String, Object> map = new HashMap<>();
        // 总题目
        map.put("questionNum", dcPaperQuestionService.count());
        Date now = new Date();
        DateTime end = DateUtil.endOfDay(now);
        Date startWeek = DateUtil.beginOfDay(DateUtil.offsetDay(now, -6));
        // 总课程数
        map.put("courseNum", dcCourseService.count());
        // 总题型数
        map.put("topicNum", dcPaperQuestionTopicService.count());
        // 总选项数
        map.put("optionNum", dcQuestionOptionService.count());
        // 最近7天新增题目
        Long recent7DaysNum = dcStatisticMapper.countQuestionNumByRangeDate(startWeek, end);
        map.put("questionRecent7DaysNum", recent7DaysNum);
        List<Map> questionRecent7Days = dcStatisticMapper.getQuestionNumByRangeDate(startWeek, end);
        map.put("questionRecent7Days", questionRecent7Days);
        // 各题型数量
        map.put("questionTopic", dcStatisticMapper.countTopicQuestionNum());
        // 当月新增
        DateTime startMonth = DateUtil.beginOfMonth(now);
        Long thisMonthNum = dcStatisticMapper.countQuestionNumByRangeDate(startMonth, end);
        map.put("questionThisMonthNum", thisMonthNum);
        List<Map> questionThisMonth = dcStatisticMapper.getQuestionNumByRangeDate(startMonth, end);
        map.put("questionThisMonth", questionThisMonth);
        // 用户数
        map.put("userNum", dcStatisticMapper.countUserNum());
        // 最近新增用户
        map.put("recentNewUser", dcStatisticMapper.recentNewUser(10));
        // 接口请求次数
        map.put("requestCount", RequestCountUtil.get());
        // 反馈次数
        map.put("feedbackCount", dcStatisticMapper.countFeedback());
        return JsonResult.ok(map);
    }

    /**
     * 各科目题目数
     */
    @GetMapping("/courseQuestionNum")
    public Object courseQuestionNum() {
        List<Map> mapList = dcStatisticMapper.courseQuestionNum();
        return JsonResult.ok(mapList);
    }

    /**
     * 用户贡献题目数
     */
    @GetMapping("/userQuestionNum")
    public Object userQuestionNum(String dateType) {
        Date start = null;
        Date end = null;
        Date now = new Date();
        if (StrUtil.equalsIgnoreCase("week", dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetDay(now, -6));
        } else if (StrUtil.equalsIgnoreCase("month", dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetMonth(now, -1));
        } else if (StrUtil.equalsIgnoreCase("year", dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetMonth(now, -12 * 1));
        }
        if (start != null) {
            end = now;
        }
        List<Map> mapList = dcStatisticMapper.userQuestionNum(start, end);
        return JsonResult.ok(mapList);
    }

    /**
     * 最近新增题目数
     */
    @GetMapping("/recentNewQuestion")
    public Object recentNewQuestion(String dateType) {
        Date start = null;
        Date end = null;
        Date now = new Date();
        // mysql日期格式化格式
        String mysqlDateFormatPattern = "%Y-%m-%d";
        String sysDateLineColumn = "date_str";
        if (StrUtil.equalsIgnoreCase("week", dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetDay(now, -6));
        } else if (StrUtil.equalsIgnoreCase("month", dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetMonth(now, -1));
        } else if (StrUtil.equalsIgnoreCase("year", dateType) || StrUtil.isBlank(dateType)) {
            start = DateUtil.beginOfDay(DateUtil.offsetMonth(now, -12 * 1));
            mysqlDateFormatPattern = "%Y-%m";
            sysDateLineColumn = "month_str";
        }
        if (start != null) {
            end = now;
        }
        List<Map> mapList = dcStatisticMapper.recentNewQuestion(mysqlDateFormatPattern, sysDateLineColumn, start, end);
        return JsonResult.ok(mapList);
    }

    /**
     * 接口调用曲线
     */
    @GetMapping("/recent-request-count")
    public Object recentRequestCount () {
        String v = ConfigUtil.getValue("dczx", "request-count:statistic-num", "12");
        int offset = -Integer.parseInt(v);
        Date now = new Date();
        Date start = DateUtil.offsetDay(now, offset);
        Date end = now;
        List<Map> mapList = dcStatisticMapper.countRecentRequest(start, end);
        return JsonResult.ok(mapList);
    }

    /**
     * 统计任务相关数量
     */
    @GetMapping("/task-count")
    public Object getTaskCount() {
        Map<String, Object> result = new HashMap<>();
        boolean isSuperAdmin = SecurityUtil.isSuperAdmin();
        String userId = SecurityUtil.getLoginUser().getUserId();
        // 托管账号数
        long countTrusteeship = dcTrusteeshipService.count(Wrappers.lambdaQuery(DcTrusteeship.class)
                .eq(!isSuperAdmin, DcTrusteeship::getCreateBy, userId));
        // 全部任务数
        long countTask = dcTaskService.count(Wrappers.lambdaQuery(DcTask.class).eq(!isSuperAdmin, DcTask::getCreateBy, userId));
        // 未完成的任务数
        long countUnFinishTask = dcTaskService.count(Wrappers.lambdaQuery(DcTask.class)
                .ne(DcTask::getStatus, DczxConstants.TASK_STATUS_FINISHED)
                .eq(!isSuperAdmin, DcTask::getCreateBy, userId));
        result.put("trusteeship", countTrusteeship);
        result.put("countTask", countTask);
        result.put("unFinishTask", countUnFinishTask);
        return JsonResult.ok(result);
    }

    /**
     * 我的任务 取6个
     */
    @GetMapping("/my-task")
    public JsonResult<List<DcTaskVO>> myTaskList () {
        LambdaQueryWrapper<DcTask> qw = Wrappers.lambdaQuery(DcTask.class)
            .select(DcTask::getId, DcTask::getCourseId, DcTask::getStatus, DcTask::getTaskType, DcTask::getCreateTime)
            .orderByDesc(DcTask::getCreateTime)
            .eq(SecurityUtil.isNotSuperAdmin(), DcTask::getCreateBy, SecurityUtil.getLoginUser().getUserId())
            .last(" limit 6 ");
        List<DcTask> list = dcTaskService.list(qw);
        List<String> courseIdList = list.stream().map(DcTask::getCourseId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(courseIdList)) {
            List<DcCourse> courseList = dcCourseService.listByIds(courseIdList);
            List<DcTaskVO> collect = list.stream().map(r -> {
                DcTaskVO vo = BeanUtil.toBean(r, DcTaskVO.class);
                for (DcCourse dcCourse : courseList) {
                    if (dcCourse.getCourseId().equals(vo.getCourseId())) {
                        vo.setCourseName(dcCourse.getCourseName());
                    }
                }
                return vo;
            }).collect(Collectors.toList());
            return JsonResult.ok(collect);
        }
        return JsonResult.ok();
    }

    /**
     * 获取最近任务
     */
    @GetMapping("/recent-task")
    public JsonResult<List<DcTaskVO>> recentTaskList() {
        LambdaQueryWrapper<DcTask> qw = Wrappers.lambdaQuery(DcTask.class)
            .select(DcTask::getId, DcTask::getCourseId,DcTask::getDcUsername, DcTask::getStatus, DcTask::getTaskType,
                DcTask::getCreateTime, DcTask::getStartTime, DcTask::getEndTime)
            .orderByDesc(DcTask::getCreateTime)
            .last(" limit 20 ");
        List<DcTask> list = dcTaskService.list(qw);
        List<String> courseIdList = list.stream().map(DcTask::getCourseId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(courseIdList)) {
            List<DcCourse> courseList = dcCourseService.listByIds(courseIdList);
            List<DcTaskVO> collect = list.stream().map(r -> {
                DcTaskVO vo = BeanUtil.toBean(r, DcTaskVO.class);
                vo.setDcUsername(DesensitizedUtil.chineseName(r.getDcUsername()));
                for (DcCourse dcCourse : courseList) {
                    if (dcCourse.getCourseId().equals(vo.getCourseId())) {
                        vo.setCourseName(dcCourse.getCourseName());
                    }
                }
                return vo;
            }).collect(Collectors.toList());
            return JsonResult.ok(collect);
        }
        return JsonResult.ok();
    }

    /**
     * 学习计划
     */
    @GetMapping("/study-plan")
    public JsonResult<List<DcUserStudyPlanVO>> studyPlanList() {
        List<DcUserStudyPlan> list = dcUserStudyPlanService.list(Wrappers.lambdaQuery(DcUserStudyPlan.class)
            .orderByDesc(DcUserStudyPlan::getCurrentFlag).eq(DcUserStudyPlan::getUserid, "19862111126")
            .eq(SecurityUtil.isNotSuperAdmin(), DcUserStudyPlan::getCreateBy, SecurityUtil.getLoginUser().getUserId())
            .last(SecurityUtil.isSuperAdmin(), " limit 20 "));
        List<DcUserStudyPlanVO> collect = list.stream().map(r -> BeanUtil.toBean(r, DcUserStudyPlanVO.class).setButtonList(null)).collect(Collectors.toList());
        return JsonResult.ok(collect);
    }

}
