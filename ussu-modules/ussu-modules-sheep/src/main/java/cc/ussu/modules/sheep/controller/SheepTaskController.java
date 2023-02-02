package cc.ussu.modules.sheep.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.SelectGroupVO;
import cc.ussu.common.core.vo.SelectVO;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.sheep.common.ISheepTask;
import cc.ussu.modules.sheep.entity.SheepTask;
import cc.ussu.modules.sheep.properties.SheepProperties;
import cc.ussu.modules.sheep.service.ISheepTaskService;
import cc.ussu.modules.sheep.util.SheepQuartzUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:38:58
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.sheep}/sheep-task")
public class SheepTaskController extends BaseController {

    @Autowired
    private ISheepTaskService service;
    @Autowired
    private SheepProperties sheepProperties;

    /**
     * 分页
     */
    @PermCheck("sheep:sheep-task:select")
    @GetMapping("/page")
    public Object page(SheepTask query) {
        LambdaQueryWrapper<SheepTask> qw = Wrappers.lambdaQuery(SheepTask.class)
            .orderByAsc(SheepTask::getDisabled)
            .like(StrUtil.isNotBlank(query.getTaskName()), SheepTask::getTaskName, query.getTaskName());
        Page<SheepTask> page = service.page(MybatisPlusUtil.getPage(), qw);
        page.getRecords().forEach(r -> r.setNextFireTime(SheepQuartzUtil.getNextFireTime(r)));
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck("sheep:sheep-task:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SheepTask> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 获取可选任务
     */
    @GetMapping("/select")
    public JsonResult<List<SelectGroupVO>> getSelectTask(Boolean edit) {
        List<String> existIds = service.list(Wrappers.lambdaQuery(SheepTask.class).select(SheepTask::getId))
                .stream().map(SheepTask::getId).collect(Collectors.toList());
        Map<String, ISheepTask> beansOfType = SpringUtil.getBeansOfType(ISheepTask.class);
        Collection<ISheepTask> values = beansOfType.values();
        List<SelectGroupVO> result = values.stream().map(ISheepTask::getTaskGroupName).collect(Collectors.toSet())
                .stream().map(r -> new SelectGroupVO().setLabel(r)).collect(Collectors.toList());
        for (SelectGroupVO groupVO : result) {
            List<SelectVO> options = new ArrayList<>();
            for (ISheepTask iSheepTask : values) {
                String taskGroupName = iSheepTask.getTaskGroupName();
                if (taskGroupName.equals(groupVO.getLabel())) {
                    options.add(new SelectVO().setLabel(iSheepTask.getTaskName())
                            .setValue(iSheepTask.getClass().getName())
                            .setDisabled(existIds.contains(iSheepTask.getClass().getName())));
                }
            }
            groupVO.setOptions(options);
        }
        return JsonResult.ok(result);
    }

    /**
     * 立即执行
     */
    @PostMapping("/trigger/{id}")
    public JsonResult triggerTask(@PathVariable String id) {
        SheepTask task = service.getById(id);
        Assert.notNull(task, "该任务不存在");
        // 判断任务是否被禁用
        if (BooleanUtil.isTrue(task.getDisabled())) {
            // 任务被禁用（删除）则创建一个立即运行的一次性任务
            // SheepQuartzUtil.runOnce(task);
        } else {
            // 任务没有被禁用，直接触发
            // SheepQuartzUtil.triggerJob(task);
        }
        return JsonResult.ok();
    }

    /**
     * 终止任务
     */
    @PostMapping("/pause/{id}")
    public JsonResult pause(@PathVariable String id) {
        SheepTask task = service.getById(id);
        Assert.notNull(task, "该任务不存在");
        Assert.isTrue(task.getRunning(), "该任务未在运行");
        service.updateById(new SheepTask().setId(task.getId()).setRunning(false));
        // SheepQuartzUtil.pause(task);
        return JsonResult.ok();
    }

    /**
     * 禁用任务
     */
    @PostMapping("/disable/{id}")
    public JsonResult disable(@PathVariable String id) {
        SheepTask task = service.getById(id);
        Assert.notNull(task, "该任务不存在");
        Assert.isFalse(task.getDisabled(), "该任务已是禁用状态");
        service.updateById(new SheepTask().setId(task.getId()).setDisabled(true));
        // SheepQuartzUtil.delete(task);
        return JsonResult.ok();
    }

    /**
     * 启用任务
     */
    @PostMapping("/enable/{id}")
    public JsonResult enable(@PathVariable String id) {
        SheepTask task = service.getById(id);
        Assert.notNull(task, "该任务不存在");
        Assert.isTrue(task.getDisabled(), "该任务已是启用状态");
        service.updateById(new SheepTask().setId(task.getId()).setDisabled(false));
        // SheepQuartzUtil.addJob(task);
        return JsonResult.ok();
    }

    /**
     * 查看日志
     */
    @GetMapping("/log/{id}")
    public JsonResult log(@PathVariable String id) {
        SheepTask sheepTask = service.getById(id);
        Assert.notNull(sheepTask, "任务不存在");
        /*SheepLogger sheepLogger = SheepQuartzUtil.getRunningTaskSheepLogger(sheepTask);
        String content = null;
        if (sheepLogger != null) {
            content = sheepLogger.toString();
        } else {
            // 获取文件
            String logPath = sheepTask.getLogPath();
            if (StrUtil.isBlank(logPath)) {
                // 没有日志
                return JsonResult.ok();
            }
            content = FileUtil.readUtf8String(sheepProperties.getLogBasePath() + logPath);
        }*/
        HashMap<String, Object> data = new HashMap<>();
        data.put("running", sheepTask.getRunning());
        // data.put("log", StrUtil.replace(content, StrPool.LF, "<br/>"));
        return JsonResult.ok(data);
    }

    /**
     * 添加
     */
    @PermCheck("sheep:sheep-task:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SheepTask p) {
        // service.add(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PermCheck("sheep:sheep-task:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SheepTask p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // service.edit(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @PermCheck("sheep:sheep-task:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

