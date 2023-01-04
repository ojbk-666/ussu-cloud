package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.*;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.JdSpeedCoinThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.ApDoTaskResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.ApTaskListResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.WheelsHomeResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.WheelsLotteryResponse;
import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice.*;
import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 京东极速版赚金币
 */
@Component
@DisallowConcurrentExecution
public class JdSpeedCoinTask extends SheepQuartzJobBean<String> {

    public static final String JD_API_HOST = "https://api.m.jd.com/";
    private static final String CLIENT_HANDLE_SERVICE_EXECUTE = "ClientHandleService.execute";

    private static final ThreadLocal<JdSpeedCoinThreadLocalDTO> threadLocal = new ThreadLocal<>();

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "京东极速版赚金币";
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
        return "/jd/speed/coin/" + fileName + ".log";
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
            threadLocal.set(new JdSpeedCoinThreadLocalDTO().setJdCookieVO(jdCookieVO).setLlAPIError(false));
            try {
                jdGlobal();
                checkStop();
                Thread.sleep(10 * 1000);
                checkStop();
                if (BooleanUtil.isTrue(threadLocal.get().getLlAPIError())) {
                    loggerThreadLocal.get().log("黑IP了，赶紧重新拨号换个IP吧");
                }
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

    private void jdGlobal() throws InterruptedException {
        wheelsHome();
        apTaskList();
        wheelsHome();
        if (true) {
            // invite();
            // invite2();
        }
        taskList();
        queryJoy();
        cash();
        Date now = new Date();
        if (DateUtil.isSameDay(now, DateUtil.endOfMonth(now))) {
            loggerThreadLocal.get().info("月底了,自动领下单红包奖励");
            orderReward(null);
        } else {
            loggerThreadLocal.get().debug("非月底,不自动领下单红包奖励");
        }
    }

    private String getAuthorInviterId() {
        return "";
    }

    private void invite() {
        long t = new Date().getTime();
        String authorInviterId = getAuthorInviterId();
        JdBodyParam jdBodyParam = new JdBodyParam().method("attendInviteActivity").put("data", new JdBodyParam().inviterPin(URLEncodeUtil.encodeAll(authorInviterId)).channel(1).token("").frontendInitStatus("").getMap());
        String body = "functionId=InviteFriendChangeAssertsService&body=" + jdBodyParam.getBody() + "&referer=-1&eid=eidI9b2981202fsec83iRW1nTsOVzCocWda3YHPN471AY78%2FQBhYbXeWtdg%2F3TCtVTMrE1JjM8Sqt8f2TqF1Z5P%2FRPGlzA1dERP0Z5bLWdq5N5B2VbBO&aid=&client=ios&clientVersion=14.4.2&networkType=wifi&fp=-1&uuid=ab048084b47df24880613326feffdf7eee471488&osVersion=14.4.2&d_brand=iPhone&d_model=iPhone10,2&agent=-1&pageClickKey=-1&platform=3&lang=zh_CN&appid=market-task-h5&_t=" + t;
        HttpResponse execute = HttpUtil.createPost(JD_API_HOST + "?t=" + t)
                .body(body)
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.ACCEPT, "application/json, text/plain, */*")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString())
                .header(Header.ORIGIN, "https://invite-reward.jd.com")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.REFERER, "https://invite-reward.jd.com/")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.USER_AGENT, getUserAgent())
                .disableCookie()
                .cookie(threadLocal.get().getJdCookieVO().toString())
                .timeout(5000)
                .execute();
    }

    private void orderReward(Integer type) throws InterruptedException {
        long t = new Date().getTime();
        JdBodyParam jdBodyParam = new JdBodyParam().method("queryRewards");
        jdBodyParam.put("data", new HashMap<>());
        String body = "functionId=OrderRewardService&body={}&_t=" + t + "&appid=market-task-h5&eid=";
        if (type != null) {
            jdBodyParam.method("receiveReward").put("data", new JdBodyParam().orderQty(type).getMap());
        }
        MyHttpResponse execute = MyHttpRequest.createPost(JD_API_HOST + "?" + StrUtil.format(body, URLEncodeUtil.encodeAll(jdBodyParam.getBody())))
                .host("api.m.jd.com").accept("*/*").contentTypeFormUrlencoded()
                .origin("https://palace.m.jd.com").acceptLanguage("zh-cn").referer("").userAgent(getUserAgent())
                .connectionKeepAlive().disableCookie().cookie(threadLocal.get().getJdCookieVO().toString())
                // .body(body)
                .execute();
        // HttpResponse execute = HttpUtil.createPost(JD_API_HOST).header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .contentType(ContentType.FORM_URLENCODED.toString())
        //         .header(Header.ORIGIN, "https://palace.m.jd.com")
        //         .header(Header.ACCEPT_LANGUAGE, "zh-cn")
        //         .header(Header.REFERER, "")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .disableCookie()
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(5000)
        //         .body(body)
        //         .execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            OrderRewardResponse rep = JSONUtil.toBean(responseBody, OrderRewardResponse.class);
            if (rep.isSuccess()) {
                List<OrderRewardResponse.Detail> details = rep.getData().getDetails();
                if (CollUtil.isNotEmpty(details)) {
                    for (OrderRewardResponse.Detail detail : details) {
                        if (2 == detail.getStatus()) {
                            loggerThreadLocal.get().info("检测到【下单领红包】有奖励可领取，开始领取奖励");
                            orderReward(detail.getOrderQty());
                            Thread.sleep(2000);
                            checkStop();
                        } else if (1 == detail.getStatus()) {
                            loggerThreadLocal.get().info("【下单领红包】暂无奖励可领取，再下 {} 单可领取 {} 元", detail.getNeedOrderQty(), detail.getValue());
                            break;
                        }
                    }
                } else {
                    loggerThreadLocal.get().info("奖励领取结果，获得 {} 元", rep.getData().getRewardAmount());
                }
            }

        } else {
            throw new RequestErrorException(execute);
        }
    }

    private CashResponse cash() throws TaskStopException {
        CashResponse rep = requestSignGet(CashResponse.class, "MyAssetsService.execute",
                new JdBodyParam().method("userCashRecord").put("data",
                        new JSONObject().put("channel", 1).put("pageNum", 1).put("pageSize", 20).getRaw()));
        if (rep.isSuccess()) {
            loggerThreadLocal.get().info("本次运行获得 {} 金币，账号共 {} 金币，可兑换 {} 元京东红包。",
                    threadLocal.get().getScore().intValue(), rep.getData().getGoldBalance(), NumberUtil.div(rep.getData().getGoldBalance(), new BigDecimal(10000), 2));
        }
        return rep;
    }

    private void queryJoy() throws InterruptedException {
        QueryJoyResponse rep = requestSignGet(QueryJoyResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE, new JdBodyParam().method("queryJoyPage").put("data", new JSONObject().put("channel", 1).getRaw()));
        if (rep.isSuccess()) {
            List<QueryJoyResponse.TaskBubble> taskBubbles = rep.getData().getTaskBubbles();
            if (CollUtil.isNotEmpty(taskBubbles)) {
                for (QueryJoyResponse.TaskBubble taskBubble : taskBubbles) {
                    rewardTask(taskBubble);
                    Thread.sleep(500);
                    checkStop();
                }
            }
        }
    }

    private void rewardTask(QueryJoyResponse.TaskBubble taskBubble) throws TaskStopException {
        RewardTaskResponse rep = requestSignGet(RewardTaskResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE, new JdBodyParam().method("joyTaskReward")
                .put("data", new JdBodyParam().id(taskBubble.getId()).channel(1)
                        .clientTime(new Date().getTime() + 0.588F).activeType(taskBubble.getActiveType()).getMap()));
        if (rep.isSuccess()) {
            Integer reward = rep.getData().getReward();
            threadLocal.get().getScore().addAndGet(reward == null ? 0 : reward);
            loggerThreadLocal.get().info("气泡收取成功，获得 {} 金币", reward);
        } else {
            loggerThreadLocal.get().info("气泡收取失败 {}", rep.getMessage());
        }
    }

    /**
     * 获取任务列表
     */
    private void taskList() throws InterruptedException {
        // 计算加密参数
        TaskListResponse rep = requestSignGet(TaskListResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE,
                new JdBodyParam().version("3.1.0").method("newTaskCenterPage").put("data", new JSONObject().put("channel", 1).getRaw()));
        if (BooleanUtil.isTrue(rep.getIsSuccess())) {
            List<TaskListResponse.TaskData> taskList = rep.getData();
            for (TaskListResponse.TaskData task : taskList) {
                String taskName = task.getTaskInfo().getMainTitle();
                if (0 == task.getTaskInfo().getStatus()) {
                    if (task.getTaskType() >= 1000) {
                        doTask2(task.getTaskType());
                        Thread.sleep(1000);
                        checkStop();
                    } else {
                        threadLocal.get().setCanStartNewItem(true);
                        while (BooleanUtil.isTrue(threadLocal.get().getCanStartNewItem())) {
                            if (3 != task.getTaskType()) {
                                queryItem(task.getTaskType());
                            } else {
                                startItem("", task.getTaskType());
                            }
                        }
                    }
                } else {
                    loggerThreadLocal.get().debug("任务 {} 已完成", taskName);
                }
                if (BooleanUtil.isTrue(threadLocal.get().getLlAPIError())) {
                    loggerThreadLocal.get().info("API请求失败，停止执行");
                    break;
                }
            }
        }
    }

    /**
     * 开始任务
     */
    private void startItem(String activeId, Integer activeType) throws InterruptedException {
        StartItemResponse rep = requestSignGet(StartItemResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE,
                new JdBodyParam().method("enterAndLeave").put("data", new JdBodyParam().activeId(activeId).clientTime(new Date().getTime()).channel("1").messageType("1").activeType(activeType).getMap()));
        if (rep.isSuccess()) {
            loggerThreadLocal.get().debug("任务开始");
            StartItemResponse.TaskInfo taskInfo = rep.getData().getTaskInfo();
            Integer isTaskLimit = taskInfo.getIsTaskLimit();
            Integer taskCompletionLimit = taskInfo.getTaskCompletionLimit();
            Integer taskCompletionProgress = taskInfo.getTaskCompletionProgress();
            Integer videoBrowsing = taskInfo.getVideoBrowsing();
            if (0 == isTaskLimit) {
                if (3 != activeType) {
                    videoBrowsing = (1 == activeType ? 5 : 10);
                }
                loggerThreadLocal.get().info("【{} / {}】浏览商品任务记录成功，等待 {} 秒", taskCompletionProgress + 1, taskCompletionLimit, videoBrowsing);
                checkStop();
                Thread.sleep(videoBrowsing * 1000);
                checkStop();
                Thread.sleep(3000);
                checkStop();
                endItem(rep.getData().getUuid(), activeType, activeId, 3 == activeType ? videoBrowsing : null);
                Thread.sleep(4000);
                checkStop();
            } else {
                loggerThreadLocal.get().info("任务已达上限");
                threadLocal.get().setCanStartNewItem(false);
            }
        } else {
            threadLocal.get().setCanStartNewItem(false);
        }
    }

    /**
     * 结束任务
     */
    private void endItem(String uuid, Integer activeType, String activeId, Integer videoTimeLength) throws TaskStopException {
        if (activeId == null) {
            activeId = "";
        }
        JdBodyParam p = new JdBodyParam().channel("1").clientTime(new Date().getTime())
                .uuid(uuid)
                .messageType("2").activeType(activeType).activeId(activeId);
        if (videoTimeLength == null) {
            p.videoTimeLength("");
        } else {
            p.videoTimeLength(videoTimeLength);
        }
        EndItemResponse rep = requestSignGet(EndItemResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE,
                new JdBodyParam().method("enterAndLeave").put("data", p.getMap()));
        if (rep.isSuccess()) {
            loggerThreadLocal.get().debug("任务结束，准备领取奖励");
            rewardItem(uuid, activeType, activeId, videoTimeLength);
        } else {
            loggerThreadLocal.get().info("任务失败 {}", rep.getMessage());
        }
    }

    /**
     * 领取奖励
     */
    private void rewardItem(String uuid, Integer activeType, String activeId, Integer videoTimeLength) throws TaskStopException {
        if (activeId == null) {
            activeId = "";
        }
        JdBodyParam data = new JdBodyParam().channel("1").clientTime(new Date().getTime()).uuid(uuid)
                .messageType("2").activeType(activeType).activeId(activeId);
        if (videoTimeLength == null) {
            data.videoTimeLength("");
        } else {
            data.videoTimeLength(videoTimeLength);
        }
        RewardItemResponse rep = requestSignGet(RewardItemResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE, new JdBodyParam().method("rewardPayment")
                .put("data", data.getMap()));
        if (rep.isSuccess()) {
            Integer reward = rep.getData().getReward();
            threadLocal.get().getScore().addAndGet(reward == null ? 0 : reward);
            loggerThreadLocal.get().info("领取奖励完成，获得 {} 金币", reward);
        } else {
            loggerThreadLocal.get().info("领取奖励失败 {}", rep.getMessage());
        }
    }

    private void queryItem(Integer activeType) throws InterruptedException {
        if (activeType == null) {
            activeType = 1;
        }
        QueryNextTaskResponse rep = requestSignGet(QueryNextTaskResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE,
                new JdBodyParam().method("queryNextTask").put("data", new JSONObject().put("channel", 1).put("activeType", activeType).getRaw()));
        if (BooleanUtil.isTrue(rep.getIsSuccess())) {
            startItem(rep.getData().getNextResource(), activeType);
        } else {
            loggerThreadLocal.get().info("商品任务开启失败 {}", rep.getMessage());
            threadLocal.get().setCanStartNewItem(false);
            threadLocal.get().setLlAPIError(true);
        }
    }

    private void doTask2(Integer taskId) throws TaskStopException {
        MarketTaskRewardPaymentResponse rep = requestSignGet(MarketTaskRewardPaymentResponse.class, CLIENT_HANDLE_SERVICE_EXECUTE, new JdBodyParam().method("marketTaskRewardPayment")
                .put("data", new JdBodyParam().channel(1).clientTime(new Date().getTime() + 0.588F).activeType(taskId).getMap()));
        if (rep.isSuccess()) {
            loggerThreadLocal.get().info("{} 任务完成成功，预计获得 {} 金币", rep.getData().getTaskInfo().getMainTitle(), rep.getData().getReward());
        }
    }


    /**
     * 大转盘任务
     */
    private void apTaskList() throws TaskStopException {
        ApTaskListResponse rep = requestGet(ApTaskListResponse.class, "apTaskList", new JdBodyParam().linkId("toxw9c5sy9xllGBr3QFdYg"));
        if (rep.isSuccess()) {
            List<ApTaskListResponse.ApTask> apTaskList = rep.getData();
            for (ApTaskListResponse.ApTask apTask : apTaskList) {
                if (BooleanUtil.isFalse(apTask.getTaskFinished())) {
                    if (StrUtil.containsAny(apTask.getTaskType(), "SIGN", "BROWSE_CHANNEL")) {
                        loggerThreadLocal.get().debug("去做任务 {}", apTask.getTaskTitle());
                        apDoTask(apTask);
                    }
                }
            }
        } else {
            throw new ResponseResultException("获取大转盘任务失败 -> " + rep.getErrMsg());
        }
    }

    /**
     * 做任务
     */
    private void apDoTask(ApTaskListResponse.ApTask apTask) throws TaskStopException {
        ApDoTaskResponse rep = requestGet(ApDoTaskResponse.class, "apDoTask",
                new JdBodyParam().linkId("toxw9c5sy9xllGBr3QFdYg").taskType(apTask.getTaskType())
                        .taskId(apTask.getId()).channel(4).itemId(apTask.getTaskSourceUrl()));
        if (rep.isSuccess()) {
            if (BooleanUtil.isTrue(rep.getData().getFinished())) {
                loggerThreadLocal.get().info("任务 {} 完成", apTask.getTaskTitle());
            }
        }
    }

    /**
     * 大转盘
     */
    private void wheelsHome() throws InterruptedException {
        WheelsHomeResponse rep = requestGet(WheelsHomeResponse.class, "wheelsHome", new JdBodyParam().linkId("toxw9c5sy9xllGBr3QFdYg"));
        if (rep.isSuccess()) {
            loggerThreadLocal.get().info("【幸运大转盘】剩余抽奖机会 {} 次", rep.getData().getLotteryChances());
            for (int i = 0; i < rep.getData().getLotteryChances(); i++) {
                wheelsLottery();
                Thread.sleep(500);
                checkStop();
            }
        }
    }

    /**
     * 大转盘抽奖
     */
    private void wheelsLottery() throws TaskStopException {
        WheelsLotteryResponse rep = requestGet(WheelsLotteryResponse.class, "wheelsLottery", new JdBodyParam().linkId("toxw9c5sy9xllGBr3QFdYg"));
        if (rep.isSuccess()) {
            WheelsLotteryResponse.WheelsLotteryData data = rep.getData();
            if (data.getRewardType() != null && !data.getRewardType().equals(0)) {
                if (data.getCouponUsedValue() != null && data.getRewardValue() != null) {
                    loggerThreadLocal.get().info("幸运大转盘抽奖获得 {} {}", data.getCouponUsedValue() - data.getRewardValue(), data.getCouponDesc());
                }
            } else {
                // 获得空气
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JdCookieVO jdCookieVO = new JdCookieVO("pt_pin=jd_TdqgKDzCtuMo;pt_key=app_openAAJjYammADAS7asMxD3bOBYm0JHM1cmBLzVrbitGLpjOwWCky6Sb7NuofJRBZdgocIS-9vlktlA;");
        JdSpeedCoinTask task = new JdSpeedCoinTask();
        task.loggerThreadLocal.set(new SheepLogger());
        task.threadLocal.set(new JdSpeedCoinThreadLocalDTO().setJdCookieVO(jdCookieVO));
        // task.wheelsHome();
        // task.apTaskList();
        task.taskList();
    }

    private MyHttpRequest.HttpRequestBuilder taskGetUrl(String functionId, String body) {
        String url = StrUtil.format(JD_API_HOST + "?appid=activities_platform&functionId={}&body={}&t=" + new Date().getTime(), functionId, URLEncodeUtil.encodeAll(body));
        // return HttpUtil.createGet(url)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .header(Header.CONNECTION, "keep-alive")
        //         .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
        //         .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
        //         .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
        //         .header(Header.REFERER, "https://an.jd.com/babelDiy/Zeus/q1eB6WUB8oC4eH1BsCLWvQakVsX/index.html")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .timeout(5000)
        //         .disableCookie()
        //         .cookie(threadLocal.get().getJdCookieVO().toString());
        return MyHttpRequest.createGet(url).host("api.m.jd.com").accept("*/*").connectionKeepAlive()
                .acceptLanguage("zh-CN,zh-Hans;q=0.9").contentEncoding("gzip, deflate, br").contentTypeFormUrlencoded()
                .referer("https://an.jd.com/babelDiy/Zeus/q1eB6WUB8oC4eH1BsCLWvQakVsX/index.html").userAgent(getUserAgent())
                .connectionKeepAlive().disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

    private MyHttpRequest.HttpRequestBuilder taskGetUrl(String functionId, JdBodyParam jdBodyParam) {
        if (jdBodyParam == null) {
            jdBodyParam = new JdBodyParam();
        }
        return taskGetUrl(functionId, jdBodyParam.getBody());
    }

    private MyHttpRequest.HttpRequestBuilder taskGetUrl(String functionId) {
        return taskGetUrl(functionId, "{}");
    }

    private <T> T requestGet(Class<T> t, String functionId, JdBodyParam jdBodyParam) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskGetUrl(functionId, jdBodyParam).execute();
        if (execute.isOk()) {
            String body = execute.body();
            JdSpeedBaseResponse rep = JSONUtil.toBean(body, JdSpeedBaseResponse.class);
            if (1000 == rep.getCode()) {
                throw new JdCookieExpiredException();
            }
            return JSONUtil.toBean(body, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 需要sign的url
     */
    private MyHttpRequest.HttpRequestBuilder taskUrl(String functionId, JdBodyParam jdBodyParam) {
        Object clientTime = jdBodyParam.getMap().get("clientTime");
        String now = clientTime != null ? clientTime.toString() : new Date().getTime() + "";
        String jdBodyParamBody = jdBodyParam.getBody();
        String p = "lite-android&" + jdBodyParamBody + "&android&3.1.0&" + functionId + "&" + now + "&846c4c32dae910ef";
        String sign = SecureUtil.hmacSha256("12aea658f76e453faf803d15c40a72e0").digestHex(p);
        String url = JD_API_HOST + "api?functionId=" + functionId + "&body=" + URLEncodeUtil.encodeAll(jdBodyParamBody) + "&appid=lite-android&client=android&uuid=846c4c32dae910ef&clientVersion=3.1.0&t=" + now + "&sign=" + sign;
        // return HttpUtil.createGet(url)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .header("kernelplatform", "RN")
        //         .header(Header.USER_AGENT, "JDMobileLite/3.1.0 (iPad; iOS 14.4; Scale/2.00)")
        //         .header(Header.ACCEPT_LANGUAGE, "zh-Hans-CN;q=1, ja-CN;q=0.9")
        //         .disableCookie()
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(5000);
        return MyHttpRequest.createGet(url)
                .host("api.m.jd.com").accept("*/*")
                .header("kernelplatform", "RN")
                .userAgent("JDMobileLite/3.1.0 (iPad; iOS 14.4; Scale/2.00)")
                .acceptLanguage("zh-Hans-CN;q=1, ja-CN;q=0.9")
                .connectionKeepAlive()
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

    private <T> T requestSignGet(Class<T> t, String functionId, JdBodyParam jdBodyParam) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskUrl(functionId, jdBodyParam).execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            JdSpeedBaseResponse rep = JSONUtil.toBean(responseBody, JdSpeedBaseResponse.class);
            if (1000 == rep.getCode()) {
                throw new JdCookieExpiredException();
            }
            return JSONUtil.toBean(responseBody, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private String getUserAgent() {
        return "jdltapp;iPad;3.1.0;14.4;network/wifi;Mozilla/5.0 (iPad; CPU OS 14_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1";
    }
}
