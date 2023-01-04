package cc.ussu.modules.sheep.task.jdbeans.service;

import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import org.quartz.DisallowConcurrentExecution;

import java.util.ArrayList;
import java.util.List;

// @Component
@DisallowConcurrentExecution
public class TestPauseRunTask extends SheepQuartzJobBean<String> {
    @Override
    public String getTaskName() {
        return "测试";
    }

    @Override
    public List getParamList() {
        return new ArrayList();
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/test/" + fileName + ".log";
    }

    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().setTraceId(getSheepTaskId());
        int i = 0;
        while (true) {
            try {
                // System.out.println("打印信息 " + i++);
                loggerThreadLocal.get().info("打印信息" + i++);
                Thread.sleep(1000);
                if (isInterrupted) {
                    loggerThreadLocal.get().info("任务被强制打断");
                    break;
                }
            } catch (InterruptedException e) {
            }
        }
    }

    // @Override
    // public void interrupt() throws UnableToInterruptJobException {
    //     isInterrupted = true;
    //     super.interrupt();
    // }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

}
