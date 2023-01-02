package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.SelectGroupVO;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.PageHelperUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.vo.DcTaskVO;
import cc.ussu.modules.dczx.mapper.DcTaskMapper;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-01 15:52:07
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-task")
public class DcTaskController extends BaseController {

    private static final String SYSTEM_LOG_GROUP = "刷课任务";

    @Autowired
    private IDcTaskService service;
    @Autowired
    private DcTaskMapper dcTaskMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-task:select")
    @GetMapping("/page")
    public Object page(DcTaskVO query) {
        if (SecurityUtil.isNotSuperAdmin()) {
            query.setCreateBy(SecurityUtil.getLoginUser().getUserId());
        }
        PageHelperUtil.startPage();
        List<DcTaskVO> list = dcTaskMapper.findPage(query);
        list.forEach(r -> {
            if (r.getStartTime() != null && r.getEndTime() != null) {
                r.setDuration(DateUtil.betweenMs(r.getStartTime(), r.getEndTime()));
            }
        });
        return PageHelperUtil.getResult(list);
    }

    /**
     * 获取可选课程
     */
    @PermCheck({"dczx:dc-task:add", "dczx:dc-task:delete"})
    @GetMapping("/course/select")
    public JsonResult<List<SelectGroupVO>> getSelectGroupList() {
        return null;
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-task:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcTask> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 获取日志
     */
    @GetMapping("/log/{taskId}")
    public JsonResult<String> getTaskLog(@PathVariable String taskId) {
        DcTask task = service.getById(taskId);
        Assert.notNull(task, "未找到该任务");
        if (SecurityUtil.isNotSuperAdmin()) {
            Assert.isTrue(task.getCreateBy().equals(SecurityUtil.getLoginUser().getUserId()), "未找到该任务");
        }
        String taskLog = task.getTaskLog();
        /*List<TimelineItem> list = JSONUtil.toList(taskLog, TimelineItem.class);
        for (TimelineItem timelineItem : list) {
            timelineItem.setContent(StrUtil.replace(timelineItem.getContent(), "\n", "<br/>"));
        }*/
        return JsonResult.ok(StrUtil.replace(taskLog, "\n", "<br/>"), null);
    }

    /**
     * 添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck("dczx:dc-task:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody DcTask p) {
        service.submitTask(p, true);
        return JsonResult.ok();
    }

    /**
     * 通过学习计划添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "通过学习计划添加")
    @PutMapping("/add-by-study-plan/{studyPlanId}/{type}")
    public JsonResult addByStudyPlan(@PathVariable String studyPlanId, @PathVariable String type) {
        service.addByStudyPlan(studyPlanId, type);
        return JsonResult.ok();
    }

    /**
     * 恢复任务
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "恢复任务")
    @PostMapping("/resume/{taskId}")
    public JsonResult resumeTask(@PathVariable String taskId) {
        DcTask task = service.getById(taskId);
        if (task != null && (DczxConstants.TASK_STATUS_ERROR.equals(task.getStatus())) || DczxConstants.TASK_STATUS_NOT_START.equals(task.getStatus())) {
            long c = service.count(Wrappers.lambdaQuery(DcTask.class).eq(DcTask::getDcUsername, task.getDcUsername())
                    .eq(DcTask::getStatus, DczxConstants.TASK_STATUS_DOING));
            Assert.isTrue(c == 0, "已存在进行中的任务");
            service.submitTask(task, false);
        }
        return JsonResult.ok();
    }

    /**
     * 暂停任务
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "暂停任务")
    @PostMapping("/pause/{taskId}")
    public JsonResult pauseTask(@PathVariable String taskId) {
        DcTask one = service.getOne(Wrappers.lambdaQuery(DcTask.class).eq(DcTask::getId, taskId)
            .eq(SecurityUtil.isNotSuperAdmin(), DcTask::getCreateBy, SecurityUtil.getLoginUser().getUserId()));
        if (DczxConstants.TASK_STATUS_DOING.equals(one.getStatus())) {
            service.markTaskPause(taskId);
        }
        return JsonResult.ok();
    }

    /**
     * 重做任务
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = "重做任务")
    @PostMapping("/redo/{taskId}")
    public JsonResult redoTask(@PathVariable String taskId) {
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.UPDATE)
    @PermCheck("dczx:dc-task:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody DcTask p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.DELETE)
    @PermCheck("dczx:dc-task:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.del(idList);
        return JsonResult.ok();
    }

}

