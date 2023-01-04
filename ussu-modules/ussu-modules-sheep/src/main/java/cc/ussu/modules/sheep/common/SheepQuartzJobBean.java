package cc.ussu.modules.sheep.common;

import cc.ussu.modules.sheep.entity.SheepTask;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public abstract class SheepQuartzJobBean<P> extends QuartzJobBean implements ISheepTask<P>, InterruptableJob {

    protected static final Logger logger = LoggerFactory.getLogger(SheepQuartzJobBean.class);

    protected ThreadLocal<SheepLogger> loggerThreadLocal = new ThreadLocal<>();
    protected ThreadLocal<JobExecutionContext> jobExecutionContextThreadLocal = new ThreadLocal<>();

    protected volatile boolean isInterrupted = false;

    /**
     * 检查中断标志
     */
    protected void checkStop() throws TaskStopException {
        if (isInterrupted) {
            throw new TaskStopException();
        }
    }

    /**
     * 获取任务数据库id
     */
    protected abstract String getSheepTaskId();

    protected String getLogContent() {
        return loggerThreadLocal.get() == null ? "" : loggerThreadLocal.get().toString();
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("{} 开始执行", getTaskName());
        jobExecutionContextThreadLocal.set(context);
        SheepLogger log = new SheepLogger(debugEnable());
        log.setTraceId(getSheepTaskId());
        loggerThreadLocal.set(log);
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        jobDataMap.put("sheepLogger", log);
        Date now = new Date();
        String fileName = DateUtil.format(now, "yyyy-MM-dd-HH-mm-ss");
        beforeDoTask();
        try {
            log.info("任务：{} 开始执行，时间：{}", getTaskName(), DateUtil.formatChineseDate(now, false, true));
            // 更新状态
            getTaskService().updateById(new SheepTask().setId(getSheepTaskId()).setRunning(true).setLastRunTime(now));
            List<P> paramList = getParamList();
            doTask(paramList);
            afterDoTask();
        } finally {
            try {
                String duration = DateUtil.formatBetween(DateUtil.spendMs(now.getTime()), BetweenFormatter.Level.SECOND);
                log.info("任务：{} 执行结束，耗时\uD83D\uDD5B：{}", getTaskName(), duration);
                String logRelativePath = getLogRelativePath(fileName);
                // 更新状态
                getTaskService().updateById(new SheepTask().setId(getSheepTaskId()).setRunning(false)
                        .setLogPath(logRelativePath).setLastRunDuration(duration));
                // 将日志写入文件
                logToFile(fileName, getLogContent());
            } finally {
                loggerThreadLocal.remove();
                jobExecutionContextThreadLocal.remove();
            }
        }
        logger.info("{} 执行完毕", getTaskName());
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        isInterrupted = true;
    }

}
