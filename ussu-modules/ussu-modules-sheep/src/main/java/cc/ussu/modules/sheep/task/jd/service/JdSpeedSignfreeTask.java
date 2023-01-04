package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedSignfreeTaskLocalThreadDTO;
import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.SignFreeHomeResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 京东极速版签到免单
 */
@Component
@DisallowConcurrentExecution
public class JdSpeedSignfreeTask extends AJdSheepTask {

    private static final String JD_API_HOST = "https://api.m.jd.com/";
    private static final String activityId = "PiuLvM8vamONsWzC0wqBGQ";

    int TIMEOUT = 10000;

    private ThreadLocal<JdSpeedSignfreeTaskLocalThreadDTO> threadLocal = new ThreadLocal<>();

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD_SPEED;
    }

    @Override
    public String getTaskName() {
        return "京东极速版签到免单";
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/speed/signfree/" + fileName + ".log";
    }

    @Override
    public void doTask(List<JdCookieVO> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量。", CollUtil.size(params));
        for (JdCookieVO param : params) {
            threadLocal.set(new JdSpeedSignfreeTaskLocalThreadDTO().setJdCookieVO(param));
            try {
                signAll();
                checkStop();
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", param.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    @Override
    protected String getUserAgent() {
        return "jdltapp;iPad;3.1.0;14.4;network/wifi;Mozilla/5.0 (iPad; CPU OS 14_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1";
    }

    /*public static void main(String[] args) throws InterruptedException {
        JdSpeedSignfreeTask jdSpeedSignfreeTask = new JdSpeedSignfreeTask();
        JdCookieVO jdCookieVO = new JdCookieVO("pt_pin=jd_7b37a844c22b9;pt_key=app_openAAJjTjLSADAmGNd1Bf3UR_w53hRbIJ_uC95yyl4jMCRMFSAGHVeP3fDa8AGEHR4jxSnJon6Ki-M;");
        jdSpeedSignfreeTask.threadLocal.set(new JdSpeedSignfreeTaskLocalThreadDTO().setJdCookieVO(jdCookieVO));
        jdSpeedSignfreeTask.loggerThreadLocal.set(new SheepLogger());
        jdSpeedSignfreeTask.signAll();
    }*/

    private void signAll() throws InterruptedException {
        query();
        List<SignFreeHomeResponse.Data1.SignFreeOrderInfo> list = threadLocal.get().getSignFreeOrderInfoList();
        if (list == null) {
            return;
        }
        Thread.sleep(3000);
        checkStop();
        for (SignFreeHomeResponse.Data1.SignFreeOrderInfo order : list) {
            sign(order);
            Thread.sleep(3000);
            checkStop();
        }
        query();
        List<SignFreeHomeResponse.Data1.SignFreeOrderInfo> signFreeOrderInfoList = threadLocal.get().getSignFreeOrderInfoList();
        if (CollUtil.isNotEmpty(signFreeOrderInfoList)) {
            for (SignFreeHomeResponse.Data1.SignFreeOrderInfo info : signFreeOrderInfoList) {
                loggerThreadLocal.get().info("{}：签到天数：{}/{}", info.getProductName(), info.getHasSignDays(), info.getNeedSignDays());
            }
        }
        Thread.sleep(3000);
        checkStop();
        for (SignFreeHomeResponse.Data1.SignFreeOrderInfo order : list) {
            if (order.getNeedSignDays() == order.getHasSignDays()) {
                loggerThreadLocal.get().info("{} 可提现，执行提现", order.getProductName());
                cash(order);
                Thread.sleep(3000);
                checkStop();
            }
        }
    }

    /**
     * 提现
     */
    private void cash(SignFreeHomeResponse.Data1.SignFreeOrderInfo order) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskPostUrl("signFreePrize", new JdBodyParam().linkId(activityId).orderId(order.getOrderId()).prizeType(2)).execute();
        if (execute.isOk()) {
            JdSpeedBaseResponse jdSpeedBaseResponse = JSONUtil.toBean(execute.body(), JdSpeedBaseResponse.class);
            if (jdSpeedBaseResponse.isSuccess()) {
                loggerThreadLocal.get().info("{} 提现成功", order.getProductName());
            } else {
                loggerThreadLocal.get().log("{} 提现结果：{}", order.getProductName(), jdSpeedBaseResponse.getErrMsg());
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 签到
     */
    private void sign(SignFreeHomeResponse.Data1.SignFreeOrderInfo order) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskPostUrl("signFreeSignIn", new JdBodyParam().linkId(activityId).orderId(order.getOrderId())).execute();
        if (execute.isOk()) {
            JdSpeedBaseResponse jdSpeedBaseResponse = JSONUtil.toBean(execute.body(), JdSpeedBaseResponse.class);
            if (jdSpeedBaseResponse.isSuccess()) {
                loggerThreadLocal.get().info("{} 签到成功", order.getProductName());
            } else {
                loggerThreadLocal.get().info("{} 签到结果：{}", order.getProductName(), jdSpeedBaseResponse.getErrMsg());
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 获取信息
     */
    private List<SignFreeHomeResponse.Data1.SignFreeOrderInfo> query() throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskGetUrl("signFreeHome", new JdBodyParam().linkId(activityId)).execute();
        String body = execute.body();
        if (execute.isOk()) {
            SignFreeHomeResponse signFreeHomeResponse = JSONUtil.toBean(body, SignFreeHomeResponse.class);
            if (signFreeHomeResponse.isSuccess()) {
                List<SignFreeHomeResponse.Data1.SignFreeOrderInfo> signFreeOrderInfoList = signFreeHomeResponse.getData().getSignFreeOrderInfoList();
                if (CollUtil.isEmpty(signFreeOrderInfoList)) {
                    loggerThreadLocal.get().info("没有需要签到的商品,请到京东极速版[签到免单]购买商品");
                } else {
                    threadLocal.get().setSignFreeOrderInfoList(signFreeOrderInfoList);
                    // firstFlag = signFreeHomeResponse.getData()
                    if (BooleanUtil.isFalse(signFreeHomeResponse.getData().getRisk())) {
                        loggerThreadLocal.get().info("风控用户，可能有异常");
                    }
                }
                return signFreeOrderInfoList;
            } else if (1000 == signFreeHomeResponse.getCode()) {
                throw new JdCookieExpiredException();
            } else {
                loggerThreadLocal.get().info("请求失败 -> {}", body);
                return null;
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private MyHttpRequest.HttpRequestBuilder taskGetUrl(String functionId, JdBodyParam param) {
        String url = StrUtil.format("{}?functionId={}&body={}&_t={}&appid=activities_platform",
                JD_API_HOST, functionId, URLEncodeUtil.encodeAll(param.getBody()), new Date().getTime());
        // HttpRequest request = HttpUtil.createRequest(Method.GET, url)
        //         .timeout(TIMEOUT)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "application/json, text/plain, */*")
        //         .header(Header.ORIGIN, "https://signfree.jd.com")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT_LANGUAGE, "en-US,zh-CN;q=0.9")
        //         .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
        //         .header(Header.REFERER, "https://signfree.jd.com/?activityId=" + activityId)
        //         .cookie(threadLocal.get().getJdCookieVO().toString());
        // return request;
        return MyHttpRequest.createGet(url).host("api.m.jd.com").accept("application/json, text/plain, */*")
                .origin("https://signfree.jd.com").userAgent(getUserAgent())
                .acceptLanguage("en-US,zh-CN;q=0.9").acceptEncoding("gzip, deflate, br")
                .referer("https://signfree.jd.com/?activityId=" + activityId)
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

    private MyHttpRequest.HttpRequestBuilder taskPostUrl(String functionId, JdBodyParam param) {
        String body = StrUtil.format("functionId={}&body={}&_t={}&appid=activities_platform",
                functionId, URLEncodeUtil.encodeAll(param.getBody()), new Date().getTime());
        // HttpRequest request = HttpUtil.createRequest(Method.POST, JD_API_HOST)
        //         .body(body)
        //         .timeout(TIMEOUT)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
        //         // .header(Header.ACCEPT, "application/json, text/plain, */*")
        //         // .header(Header.ORIGIN, "https://signfree.jd.com")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT_LANGUAGE, "en-US,zh-CN;q=0.9")
        //         .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
        //         .header(Header.REFERER, "https://signfree.jd.com/?activityId=" + activityId)
        //         .cookie(threadLocal.get().getJdCookieVO().toString());
        // return request;
        return MyHttpRequest.createPost(JD_API_HOST + "?" + body).host("api.m.jd.com")
                .userAgent(getUserAgent()).acceptLanguage("en-US,zh-CN;q=0.9").acceptEncoding("gzip, deflate, br")
                .referer("https://signfree.jd.com/?activityId=" + activityId)
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

}
