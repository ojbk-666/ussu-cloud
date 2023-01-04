package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.lotterybeans.LotteryBeanResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 抽京豆
 */
@Component
@DisallowConcurrentExecution
public class JdLotteryBeanTask extends SheepQuartzJobBean<String> {

    private static final ThreadLocal<JdCookieVO> threadLocal = new ThreadLocal<>();

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "抽京豆";
    }

    /**
     * 任务分组
     */
    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
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
        return "/jd/lottery_beans/" + fileName + ".log";
    }

    /**
     * 获取任务数据库id
     */
    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    /**
     * 执行任务
     *
     * @param params
     */
    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        for (String ckval : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(ckval);
            threadLocal.set(jdCookieVO);
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            try {
                checkStop();
                lotteryBeans();
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
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void lotteryBeans() throws InterruptedException {
        checkStop();
        String bodyStr = "{\"enAwardK\":\"ltvTJ/WYFPZcuWIWHCAjR7T24629O5Bj4Hc8NBIy1TEaqi8FPY5ILFQIt4+L+Dk2AVaczoUoQHqy\\ny4p8d6/bK/bwdZK6Aw80mPSE7ShF/0r28HWSugMPNPm5JQ8b9nflgkMfDwDJiaqThDW7a9IYpL8z\\n7mu4l56kMNsaMgLecghsgTYjv+RZ8bosQ6kKx+PNAP61OWarrOeJ2rhtFmhQncw6DQFeBryeMUM1\\nw9SpK5iag4uLvHGIZstZMKOALjB/r9TIJDYxHs/sFMU4vtb2jX9DEwleHSLTLeRpLM1w+RakAk8s\\nfC4gHoKM/1zPHJXq1xfwXKFh5wKt4jr5hEqddxiI8N28vWT05HuOdPqtP+1RkVF72YQobfIdkP1B\\nAwJ/R5Kt0js9JUM=_babel\",\"awardSource\":\"1\",\"srv\":\"{\\\"bord\\\":\\\"0\\\",\\\"fno\\\":\\\"0-0-2\\\",\\\"mid\\\":\\\"77512871\\\",\\\"bi2\\\":\\\"2\\\",\\\"bid\\\":\\\"0\\\",\\\"aid\\\":\\\"01155413\\\"}\",\"encryptProjectId\":\"3u4fVy1c75fAdDN6XRYDzAbkXz1E\",\"encryptAssignmentId\":\"2x5WEhFsDhmf8JohWQJFYfURTh9w\",\"authType\":\"2\",\"riskParam\":{\"platform\":\"3\",\"orgType\":\"2\",\"openId\":\"-1\",\"pageClickKey\":\"Babel_WheelSurf\",\"eid\":\"eidIbada81221bs1zNtS1S46RiqFtEJLHk9T/czbVFI9b83IlgGwX6enoffkP1qB9QrN7TU13kzR++PQb/v0JEpxCfco1wl0dgsxxGqdB9AiOQGarfxZ\",\"fp\":\"-1\",\"shshshfp\":\"c66014b1ca05a0272dd41894e0709149\",\"shshshfpa\":\"d7f637ca-7cf6-ac16-adec-6ac855da1f7a-1639191684\",\"shshshfpb\":\"t9rJJvMIReEDpquJnAmh6CA\",\"childActivityUrl\":\"https%3A%2F%2Fpro.m.jd.com%2Fmall%2Factive%2F2xoBJwC5D1Q3okksMUFHcJQhFq8j%2Findex.html%3Ftttparams%3DjzUEzDjjeyJncHNfYXJlYSI6IjEzXzEwMDBfNDI3N182MzA5NiIsInByc3RhdGUiOiIwIiwidW5fYXJlYSI6IjEzXzEwMDBfNDA0OTFfNTk2NjkiLCJkTGF0IjoiIiwiZ0xhdCI6IjM2LjcwMzQiLCJnTG5nIjoiMTE3LjExNiIsImxuZyI6IjExNy4xMjExODYiLCJsYXQiOiIzNi42ODMwNDQiLCJkTG5nIjoiIiwibW9kZWwiOiJpUGhvbmUxNCw1In80%253D\",\"userArea\":\"-1\",\"client\":\"\",\"clientVersion\":\"\",\"uuid\":\"\",\"osVersion\":\"\",\"brand\":\"\",\"model\":\"\",\"networkType\":\"\",\"jda\":\"-1\"},\"siteClient\":\"apple\",\"mitemAddrId\":\"\",\"geo\":{\"lng\":\"117.121186\",\"lat\":\"36.683044\"},\"addressId\":\"\",\"posLng\":\"\",\"posLat\":\"\",\"un_area\":\"13_1000_40491_59669\",\"gps_area\":\"13_1000_4277_63096\",\"homeLng\":\"117.116\",\"homeLat\":\"36.7034\",\"homeCityLng\":\"\",\"homeCityLat\":\"\",\"focus\":\"\",\"innerAnchor\":\"\",\"cv\":\"2.0\"}";
        StringBuffer body = new StringBuffer().append("body=")
                .append(URLEncodeUtil.encodeAll(bodyStr))
                .append("&client=wh5")
                .append("&clientVersion=1.0.0");
        MyHttpResponse execute = MyHttpRequest.createPost("https://api.m.jd.com/client.action?functionId=babelGetLottery&" + body.toString())
                .contentTypeFormUrlencoded().userAgent(getUserAgent()).accept("zh-CN,zh-Hans;q=0.9")
                .origin("https://pro.m.jd.com").referer("https://pro.m.jd.com/")
                .disableCookie().cookie(threadLocal.get().toString())
                .execute();
        // HttpResponse execute = HttpUtil.createPost("https://api.m.jd.com/client.action?functionId=babelGetLottery")
        //         .body(body.toString())
        //         .contentType(ContentType.FORM_URLENCODED.toString())
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT, "zh-CN,zh-Hans;q=0.9")
        //         .header(Header.ORIGIN, "https://pro.m.jd.com")
        //         .header(Header.REFERER, "https://pro.m.jd.com/")
        //         .disableCookie()
        //         .cookie(threadLocal.get().toString())
        //         .timeout(5000)
        //         .execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            LotteryBeanResponse rep = JSONUtil.toBean(responseBody, LotteryBeanResponse.class);
            if (rep.isSuccess()) {
                // 抽中了
                loggerThreadLocal.get().info("{}", rep.getPrizeName());
            } else if ("1".equals(rep.getCode())){
                throw new RequestErrorException("请求不正确：" + rep.getMsg());
            } else {
                loggerThreadLocal.get().info("{} -> {}", rep.getPromptMsg(), rep.getResponseMessage());
            }
        } else {
            throw new RequestErrorException(execute);
        }
        // 休眠3s
        loggerThreadLocal.get().debug("休息3秒");
        Thread.sleep(3000);
    }

    private String getUserAgent() {
        return "jdapp;iPhone;11.2.8;;;M/5.0;appBuild/168328;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1;";
    }

}
