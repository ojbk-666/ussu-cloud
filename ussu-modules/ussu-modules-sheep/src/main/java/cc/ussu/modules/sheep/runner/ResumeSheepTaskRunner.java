package cc.ussu.modules.sheep.runner;

import cc.ussu.modules.sheep.entity.SheepTask;
import cc.ussu.modules.sheep.service.ISheepTaskService;
import cc.ussu.modules.sheep.util.SheepQuartzUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 恢复任务
 */
@Slf4j
@Component
public class ResumeSheepTaskRunner implements ApplicationRunner {

    @Autowired
    private ISheepTaskService sheepTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<SheepTask> tasks = sheepTaskService.listNotDisabledTask();
        for (SheepTask task : tasks) {
            try {
                SheepQuartzUtil.addJob(task);
                if (BooleanUtil.isTrue(task.getRunning())) {
                    SheepQuartzUtil.resume(task);
                }
            } catch (SchedulerException e) {
                log.error("任务 {} 添加失败:{}", task.getTaskName(), e.getMessage(), e);
            }
        }
    }
}
