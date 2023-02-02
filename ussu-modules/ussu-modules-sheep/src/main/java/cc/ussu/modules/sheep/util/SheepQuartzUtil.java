package cc.ussu.modules.sheep.util;

import cc.ussu.common.quartz.util.QuartzUtil2;
import cc.ussu.modules.sheep.common.SheepLogger;
import cc.ussu.modules.sheep.entity.SheepTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
public class SheepQuartzUtil extends QuartzUtil2 {

    public static final String GROUP_NAME = "sheep_task";

    public static JobKey getJobKey(SheepTask sheepTask) {
        return JobKey.jobKey(sheepTask.getId(), GROUP_NAME);
    }

    public static TriggerKey getTriggerKey(SheepTask sheepTask) {
        return TriggerKey.triggerKey(sheepTask.getId(), GROUP_NAME);
    }

    public static void addJob(SheepTask sheepTask) throws SchedulerException {
        Scheduler scheduler = getScheduler(); // 通过SchedulerFactory构建Scheduler对象
        JobKey jobKey = getJobKey(sheepTask);
        JobDetail jobDetail = JobBuilder.newJob(findJob(sheepTask.getTargetClass()))
                .withIdentity(jobKey)
                .withDescription(sheepTask.getTaskName())
                .build();
        // 参数
        // if (CollUtil.isNotEmpty(parameter)) {
        //     jobDetail.getJobDataMap().putAll(parameter);
        // }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sheepTask.getCron());
        // 错过策略 https://blog.csdn.net/yangshangwei/article/details/78539433
        // 不触发立即执行
        // 等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
        cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(sheepTask.getId(), GROUP_NAME))
                // cron表达式
                .withSchedule(cronScheduleBuilder)
                .build();
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
            // throw new SchedulerException("该任务已存在");
        }
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("添加任务：{} -> {}", sheepTask.getId(), sheepTask.getTaskName());
    }

    public static void pause(SheepTask sheepTask) throws SchedulerException {
        JobKey jobKey = getJobKey(sheepTask);
        getScheduler().pauseJob(jobKey);
        getScheduler().pauseTrigger(getTriggerKey(sheepTask));
        // Trigger.TriggerState triggerState = getScheduler().getTriggerState(getTriggerKey(sheepTask));
        // if (!Trigger.TriggerState.PAUSED.equals(triggerState)) {
            getScheduler().interrupt(jobKey);
        // }
    }

    public static void resume(SheepTask sheepTask) throws SchedulerException {
        getScheduler().resumeJob(getJobKey(sheepTask));
    }

    public static void delete(SheepTask sheepTask) throws SchedulerException {
        Scheduler sched = getScheduler();
        TriggerKey triggerKey = getTriggerKey(sheepTask);
        sched.pauseTrigger(triggerKey); // 停止触发器
        sched.unscheduleJob(triggerKey);// 移除触发器
        sched.deleteJob(getJobKey(sheepTask)); // 删除任务
    }

    /**
     * 立即触发任务执行
     *
     * @param sheepTask
     * @throws SchedulerException
     */
    public static void triggerJob(SheepTask sheepTask) throws SchedulerException {
        getScheduler().triggerJob(getJobKey(sheepTask));
    }

    public static Date getNextFireTimeByCron(String cron) {
        try {
            CronExpression cronExpression = new CronExpression(cron);
            return cronExpression.getTimeAfter(new Date());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取下一次执行时间
     *
     * @param sheepTask
     * @return
     */
    public static Date getNextFireTime(SheepTask sheepTask) {
        return getNextFireTimeByCron(sheepTask.getCron());
    }

    public static void runOnce(SheepTask sheepTask) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(new Date().getTime() + "", GROUP_NAME);
        // JobKey jobKey = getJobKey(sheepTask);
        JobDetail jobDetail = JobBuilder.newJob(findJob(sheepTask.getTargetClass()))
                .withIdentity(jobKey)
                .build();
        Scheduler scheduler = getScheduler();
        // scheduler.addJob(jobDetail, false, true);
        Trigger trigger = TriggerBuilder.newTrigger().startNow().build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 获取正在执行的任务日志
     */
    public static SheepLogger getRunningTaskSheepLogger(SheepTask sheepTask) throws SchedulerException {
        JobKey jobKey = getJobKey(sheepTask);
        List<JobExecutionContext> currentlyExecutingJobs = getScheduler().getCurrentlyExecutingJobs();
        for (JobExecutionContext job : currentlyExecutingJobs) {
            if(job.getJobDetail().getKey().getName().equals(jobKey.getName())) {
                // 找到
                SheepLogger sheepLogger = (SheepLogger) job.getJobDetail().getJobDataMap().get("sheepLogger");
                return sheepLogger;
            }
        }
        return null;
    }
}
