package cc.ussu.modules.sheep.task.jdbeans.service;

import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.service.IJdDayBeansService;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jdbeans.vo.JdDetailResponseVo;
import cc.ussu.modules.sheep.task.jdbeans.vo.TodayBeanVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 更新数据库的今日京豆收入
 */
@Component
@DisallowConcurrentExecution
public class UpdateDBTodayBeansIncomeTask extends SheepQuartzJobBean<String> {

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "更新数据库的今日京豆收入";
    }

    /**
     * 获取参数
     */
    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    /**
     * 获取日志存放的相对路径
     *
     * @param fileName 文件名
     * @return 相对路径地址 例 /a/b/c/2022-10-16.log
     */
    @Override
    public String getLogRelativePath(String fileName) {
        return "/db/jd_beans_today_income/" + fileName + ".log";
    }

    /**
     * 执行任务
     *
     * @param params
     */
    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        for (String ck : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(ck);
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            try {
                tryUpdate(jdCookieVO);
                checkStop();
                Thread.sleep(2000);
                checkStop();
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (ResponseResultException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 完成", jdCookieVO.getPt_pin()).newline();
                }
            }
        }
    }

    private void tryUpdate(JdCookieVO jdCookieVO) throws InterruptedException {
        JdBeansService beansService = SpringUtil.getBean(JdBeansService.class);
        IJdDayBeansService jdDayBeansService = SpringUtil.getBean(IJdDayBeansService.class);
        loggerThreadLocal.get().info("请求 todayBean 接口");
        checkStop();
        TodayBeanVO todayBeanVo = beansService.todayBean2(jdCookieVO.toString());
        loggerThreadLocal.get().info("今日收入 {} 豆,支出 {} 豆", todayBeanVo.getIncome().toString(), todayBeanVo.getOut().toString());
        List<JdDetailResponseVo.DetailList> incomeList = todayBeanVo.getIncomeList();
        jdDayBeansService.updateRecentBeans(todayBeanVo, jdCookieVO.getPt_pin());
    }

    /**
     * 获取任务数据库id
     */
    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }
}
