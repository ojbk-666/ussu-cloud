package cc.ussu.common.quartz.util;

import cc.ussu.common.quartz.vo.QuartzTaskVO;
import cc.ussu.common.quartz.vo.QuartzTriggerVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;

@Slf4j
public class QuartzUtil2 {

    public static final String TASK_NAME = "taskName";
    public static final String JOB_ID = "jobId";

    /**
     * 获取一个调度器
     */
    public static Scheduler getScheduler() {
        return SpringUtil.getBean(Scheduler.class);
    }

    public static JobKey getJobKey(String name) {
        return getJobKey(name, null);
    }
    public static JobKey getJobKey(String name, String group) {
        return JobKey.jobKey(name, StrUtil.blankToDefault(group, "default_group"));
    }

    public static TriggerKey getTriggerKey(String name, String group) {
        return TriggerKey.triggerKey(name, StrUtil.blankToDefault(group, "default_trigger"));
    }

    /**
     * 查找指定的调度类，将字符串转为调度class
     *
     * @param classPath
     * @return
     */
    public static Class<? extends Job> findJob(String classPath) {
        Class<?> jobClass = ClassLoaderUtil.loadClass(classPath);
        return (Class<? extends Job>) jobClass;
    }

    /**
     * 创建一个任务
     *
     * @throws SchedulerException
     */
    public static void addJobCron(String name, String group, String targetClass, String cron, String description,Map<String, Object> parameter) throws SchedulerException {
        Scheduler sched = getScheduler(); // 通过SchedulerFactory构建Scheduler对象
        JobKey jobKey = getJobKey(name, group);
        JobDetail jobDetail = JobBuilder.newJob(findJob(targetClass))
                .withIdentity(jobKey)
                .withDescription(null)
                .build();
        // 参数
        if (CollUtil.isNotEmpty(parameter)) {
            jobDetail.getJobDataMap().putAll(parameter);
        }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        // 错过策略 https://blog.csdn.net/yangshangwei/article/details/78539433
        /*if (sysJob.misfireRunAll()) {
            // 以错过的第一个频率时间立刻开始执行
            // 重做错过的所有频率周期后
            // 当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (sysJob.misfireRunOnceRightNow()) {
            // 以当前时间为触发频率立刻触发一次执行
            // 然后按照Cron频率依次执行
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else if (sysJob.misfireNothing()) {*/
            // 不触发立即执行
            // 等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
        cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        // }
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(name, group))
                // cron表达式
                .withSchedule(cronScheduleBuilder)
                .build();
        // 如果已存在则移除
        if (sched.checkExists(jobKey)) {
            sched.deleteJob(jobKey);
        }
        log.info("添加job:[" + name + "]");
        sched.scheduleJob(jobDetail, trigger);
        // if (!sched.isShutdown()) {
        //     sched.start(); // 启动
        // }
    }

    /**
     * 暂停任务
     *
     * @throws SchedulerException
     */
    public static void pauseJob(String name, String group) throws SchedulerException {
        log.info("暂停job:[" + name + "]");
        getScheduler().pauseJob(getJobKey(name, group));
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(String name, String group) throws SchedulerException {
        log.info("恢复job:[" + name + "]");
        getScheduler().resumeJob(getJobKey(name, group));
    }

    /**
     * 删除任务
     */
    public static void deleteJob(String name, String group) throws SchedulerException {
        log.info("删除job:[" + name + "]");
        Scheduler sched = getScheduler();
        TriggerKey triggerKey = getTriggerKey(name, group);
        sched.pauseTrigger(triggerKey); // 停止触发器
        sched.unscheduleJob(triggerKey);// 移除触发器
        sched.deleteJob(getJobKey(name, group)); // 删除任务
    }

    public static void runOnceRightNow(String name, String group, String targetClass, String description) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        JobKey jobKey = getJobKey(name, group);
        if (scheduler.checkExists(jobKey)) {
            scheduler.triggerJob(jobKey);
        } else {
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withDescription(StrUtil.format("立即执行一次:[]", name))
                    .startNow()
                    .build();
            JobDetail jobDetail = JobBuilder.newJob(findJob(targetClass))
                    .withIdentity(name + "_once")
                    .withDescription(description)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    /**
     * 获取所有定时任务
     */
    public static List<QuartzTaskVO> getAllTask() {
        List<QuartzTaskVO> taskVOS = new ArrayList<>();
        try {
            Scheduler scheduler = getScheduler();
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            for (JobKey jobKey : jobKeys) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                String name = jobKey.getName();
                String group = jobKey.getGroup();
                String description = jobDetail.getDescription();
                String jobClassName = jobDetail.getJobClass().getName();
                QuartzTaskVO taskVO = new QuartzTaskVO().setName(name).setGroup(group)
                    .setDescription(description).setJobClassName(jobClassName);
                ArrayList<QuartzTriggerVO> triggerVOS = new ArrayList<>();
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    TriggerKey key = trigger.getKey();
                    Date previousFireTime = trigger.getPreviousFireTime();
                    Date fireTimeAfter = trigger.getFireTimeAfter(new Date());
                    Date startTime = trigger.getStartTime();
                    Date endTime = trigger.getEndTime();
                    taskVO.setPreviousFireTime(previousFireTime).setFireTimeAfter(fireTimeAfter).setStartTime(startTime).setEndTime(endTime);
                    triggerVOS.add(new QuartzTriggerVO().setName(key.getName()).setGroup(key.getGroup()).setStartTime(startTime).setEndTime(endTime));
                }
                taskVOS.add(taskVO.setTriggers(triggerVOS));
            }
        } catch (SchedulerException e) {
        }
        return taskVOS;
    }

}
