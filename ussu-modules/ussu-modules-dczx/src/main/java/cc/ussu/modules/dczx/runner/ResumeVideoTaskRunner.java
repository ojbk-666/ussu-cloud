package cc.ussu.modules.dczx.runner;

import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 恢复被中断的刷视频任务
 */
@Slf4j
@Component
public class ResumeVideoTaskRunner implements ApplicationRunner {

    @Autowired
    private IDcTaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DcTask> list = taskService.list(Wrappers.lambdaQuery(DcTask.class).eq(DcTask::getStatus, DczxConstants.TASK_STATUS_DOING));
        if (CollUtil.isNotEmpty(list)) {
            List<List<DcTask>> group = CollUtil.groupByField(list, "dcUsername");
            for (List<DcTask> dcTasks : group) {
                DcTask item = dcTasks.get(0);
                if (item != null) {
                    log.info(item.getDcUsername() + " 的任务已恢复,进度:" + item.getProgress() + " %");
                    taskService.runTask(item);
                    Thread.sleep(1000);
                }
            }
        }
    }

}
