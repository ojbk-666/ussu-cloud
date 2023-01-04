package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.invite.JdSpeedCoinInviteThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.invite.response.HelpResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.invite.response.InfoResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice.BaseClientHandleServiceExecuteResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东极速版赚金币 邀请好友
 */
@Component
@DisallowConcurrentExecution
public class JdSpeedCoinInviteTask extends SheepQuartzJobBean<String> {

    private static final ThreadLocal<JdSpeedCoinInviteThreadLocalDTO> threadLocal = new ThreadLocal<>();

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "京东极速版赚金币 邀请好友";
    }

    /**
     * 任务分组
     */
    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD_SPEED;
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
        return "/jd/speed/coin/invite/" + fileName + ".log";
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
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params)).newline();
        List<String> expireJdCkList = new ArrayList<>();
        for (String ckval : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(ckval);
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            threadLocal.set(new JdSpeedCoinInviteThreadLocalDTO().setJdCookieVO(jdCookieVO));
            try {
                info();
                String envInviterCode = getEnvInviterCode();
                if (StrUtil.isNotBlank(envInviterCode)) {
                    help(envInviterCode);
                } else {
                    help("scdHR0/UTIYLqMcaaYN8D7pqvfpjWXfhKdQQYvkE5Eg=");
                }
                Thread.sleep(10 * 1000);
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info(e.getMessage());
                expireJdCkList.add(ckval);
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知错误：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void help(String inviterCode) throws TaskStopException {
        String body = "functionId=TaskInviteService&body={\"method\":\"participateInviteTask\",\"data\":{\"channel\":\"1\",\"encryptionInviterPin\":\""+ URLEncodeUtil.encodeAll(inviterCode) +"\",\"type\":1}}&appid=market-task-h5&uuid=7303439343432346-7356431353233311&eu=7303439343432341&fv=7356431353233321&_t=1623475839367";
        HelpResponse rep = request(HelpResponse.class, body);
        if (rep.isSuccess()) {
            loggerThreadLocal.get().info("邀请 {} 获得 {} 金币", inviterCode, rep.getData().getCoinReward() * 0.1);
        } else if (605 == rep.getCode()){
            loggerThreadLocal.get().info(rep.getMessage());
        } else {
            loggerThreadLocal.get().info("{} -> {}", rep.getCode(), rep.getMessage());
        }
    }

    private void info() throws TaskStopException {
        InfoResponse rep = request(InfoResponse.class, "functionId=TaskInviteService&body={\"method\":\"inviteTaskHomePage\",\"data\":{\"channel\":\"1\"}}&appid=market-task-h5&uuid=7303439343432346-7356431353233311&eu=7303439343432341&fv=7356431353233321&_t=1623475839367");
        if (rep.isSuccess()) {
            InfoResponse.InvitationTaskVO data = rep.getData();
            loggerThreadLocal.get().info("账号 {} 的邀请码为：{} 显示的邀请码：{}", threadLocal.get().getJdCookieVO().getPt_pin(), data.getEncryptionInviterPin(), data.getInviterCode());
        }
    }

    private <T> T request(Class<T> t, String body) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = MyHttpRequest.createPost("https://api.m.jd.com?" + URLEncodeUtil.encodeQuery(body)).host("api.m.jd.com").origin("https://assignment.jd.com")
                .connectionKeepAlive()
                .userAgent("jdltapp;android;3.5.0;10;7303439343432346-7356431353233323;network/wifi;model/PCAM00;addressid/4228801336;aid/7049442d7e415232;oaid/;osVer/29;appBuild/1587;psn/jkWXTyfQA2PDVmg3OkxOiWnHy7pHXWA |155;psq/12;adk/;ads/;pap/JA2020_3112531|3.5.0|ANDROID 10;osv/10;pv/36.36;jdv/;ref/com.jd.jdlite.lib.mission.allowance.AllowanceFragment;partner/oppo;apprpd/Allowance_Registered;eufv/1;Mozilla/5.0 (Linux; Android 10; PCAM00 Build/QKQ1.190918.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/045140 Mobile Safari/537.36")
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString()).execute();
        // HttpResponse execute = HttpUtil.createPost("https://api.m.jd.com")
        //         .body(body)
        //         .contentType(ContentType.FORM_URLENCODED.toString())
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ORIGIN, "https://assignment.jd.com")
        //         .header(Header.USER_AGENT, "jdltapp;android;3.5.0;10;7303439343432346-7356431353233323;network/wifi;model/PCAM00;addressid/4228801336;aid/7049442d7e415232;oaid/;osVer/29;appBuild/1587;psn/jkWXTyfQA2PDVmg3OkxOiWnHy7pHXWA |155;psq/12;adk/;ads/;pap/JA2020_3112531|3.5.0|ANDROID 10;osv/10;pv/36.36;jdv/;ref/com.jd.jdlite.lib.mission.allowance.AllowanceFragment;partner/oppo;apprpd/Allowance_Registered;eufv/1;Mozilla/5.0 (Linux; Android 10; PCAM00 Build/QKQ1.190918.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/045140 Mobile Safari/537.36")
        //         .disableCookie()
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(5000)
        //         .execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            BaseClientHandleServiceExecuteResponse rep = JSONUtil.toBean(responseBody, BaseClientHandleServiceExecuteResponse.class);
            if (801 == rep.getCode()) {
                throw new JdCookieExpiredException();
            }
            return JSONUtil.toBean(responseBody, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private String getEnvInviterCode() {
        return getEnvService().getValue("jd_speed_coin_inviter_code");
    }

}
