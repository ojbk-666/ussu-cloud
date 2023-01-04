package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.SheepLogger;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.FruitFinishedException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.InitForFarmException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.TaskInitForFarmException;
import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdBaseAmountResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitTaskThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 东东农场日常任务
 */
@Component
@DisallowConcurrentExecution
public class JdFruitTask extends SheepQuartzJobBean<String> implements JdSheepTask {

    private static final Logger logger = LoggerFactory.getLogger(JdFruitTask.class);

    private ThreadLocal<JdFruitTaskThreadLocalDTO> threadLocal = new ThreadLocal();

    public static final String JD_API_HOST = "https://api.m.jd.com/client.action";
    // 超时时间
    public static final int TIMEOUT = 10000;
    // 初始化农场数据的最大重试次数
    public static final int INIT_FARM_MAX_COUNT = 3;
    /**
     * 使用水滴换豆卡 100g水滴换京豆
     */
    private boolean useBeanCard() {
        return true;
    }

    /**
     * 保留水滴
     */
    private int retainWater() {
        String value = getEnvService().getValue("jd_fruit_retain_water");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 100;
        }
    }

    public static void main(String[] args) throws FruitFinishedException, InitForFarmException {
        JdFruitTask jdFruitTask = new JdFruitTask();
        String ckval = "";
        JdCookieVO ck = new JdCookieVO(ckval);
        jdFruitTask.threadLocal.set(new JdFruitTaskThreadLocalDTO().setJdCookieVO(ck));
        // jdFruitTask.jdFruit();
        jdFruitTask.doTask(CollUtil.newArrayList(ckval));
        // jdFruitTask.signForFarm();
        // jdFruitTask.doDailyTask();
        // jdFruitTask.initForFarm();
        // jdFruitTask.taskInitForFarm();
        // jdFruitTask.doBrowseAdTask();
        // 单次测试广告
        // jdFruitTask.browseAdTaskForFarm("6401672940");
        // jdFruitTask.browseAdTaskForFarmRward("6401672940");
        // 给好友浇水
        // jdFruitTask.doDailyTaskWaterFriends();
        // FriendListInitForFarmResponse friendListInitForFarmResponse = jdFruitTask.friendListInitForFarm();
        // jdFruitTask.clockInIn();
        // jdFruitTask.getExtraAward();
        // jdFruitTask.turntableFarm();
        // 打卡领水
        // jdFruitTask.clockInIn();
        // jdFruitTask.doTenWater();
        // jdFruitTask.waterGoodForFarm();
        // jdFruitTask.getFirstWaterAward();
        // jdFruitTask.duck();
    }

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    @Override
    public String getTaskName() {
        return "东东农场日常任务";
    }

    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/fruit/" + fileName + ".log";
    }

    /**
     * 执行任务
     */
    @Override
    public void doTask(List<String> cks) {
        SheepLogger log = loggerThreadLocal.get();
        for (String ckval : cks) {
            JdCookieVO ck = new JdCookieVO(ckval);
            log.info("账号 {} 开始执行", ck.getPt_pin());
            threadLocal.set(new JdFruitTaskThreadLocalDTO().setJdCookieVO(ck));
            try {
                jdFruit();
                Thread.sleep(3000);
                checkStop();
            } catch (FruitFinishedException e) {
                log.info("♥♥ 水果已成熟 ♥♥");
            } catch (JdCookieExpiredException e) {
                log.info(e.getMessage());
            } catch (InitForFarmException e) {
                log.info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("异常！！ -> {}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    log.info("账号 {} 执行完毕", ck.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    /**
     * 获取User-Agent
     */
    private String getUserAgent() {
        return "jdapp;iPhone;11.2.8;;;M/5.0;appBuild/168328;jdSupportDarkMode/0;ef/1;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/0;";
    }

    private void jdFruit() throws FruitFinishedException, InitForFarmException, JdCookieExpiredException, InterruptedException {
        InitForFarmResponse initForFarmResponse = initForFarm();
        printInitFarmInfo(initForFarmResponse);
        if (initForFarmResponse.treeStatePlanting()) {
            // 水果种植中
            // 做日常任务
            doDailyTask();
            // 浇水十次
            doTenWater();
            // 领取首次浇水奖励
            getFirstWaterAward();
            // 领取10浇水奖励
            getTenWaterAward();
            // 领取为2好友浇水奖励
            getWaterFriendGotAward();
            // 小鸭子任务
            duck();
            // 再次执行浇水任务
            doTenWaterAgain();
            // 预测水果成熟时间
            predictionFruit();
        }
    }

    /**
     * 再次执行浇水任务
     */
    private void doTenWaterAgain() throws FruitFinishedException, InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        log.debug("开始检查剩余水滴能否再次浇水再次浇水");
        InitForFarmResponse farmInfo = initForFarm();
        Integer totalEnergy = farmInfo.getFarmUserPro().getTotalEnergy();
        log.debug("剩余水滴 {} g", totalEnergy);
        MyCardInfoForFarmResponse myCardInfoRes = myCardInfoForFarm();
        Integer fastCard = myCardInfoRes.getFastCard();
        Integer beanCard = myCardInfoRes.getBeanCard();
        Integer doubleCard = myCardInfoRes.getDoubleCard();
        Integer signCard = myCardInfoRes.getSignCard();
        log.debug("背包已有道具：快速浇水卡 {} 张，水滴翻倍卡 {} 张，水滴换豆卡 {} 张，加签卡 {} 张", fastCard, doubleCard, beanCard, signCard);
        if (totalEnergy.intValue() >= 100 && doubleCard.intValue() > 0) {
            for (int i = 0; i < doubleCard; i++) {
                UserMyCardForFarmResponse rep = userMyCardForFarm("doubleCard");
                log.debug("使用水滴翻倍卡结果：{}", JSONUtil.toJsonStr(rep));
            }
            totalEnergy = initForFarm().getFarmUserPro().getTotalEnergy();
        }
        if (useBeanCard() && JSONUtil.toJsonStr(myCardInfoRes).contains("限时翻倍")) {
            log.debug("您设置的是水滴换豆功能,现在为您换豆");
            if (totalEnergy.intValue() >= 100 && beanCard > 0) {
                UserMyCardForFarmResponse rep1 = userMyCardForFarm("beanCard");
                log.debug("使用水滴换豆卡结果：{}", JSONUtil.toJsonStr(rep1));
                if (rep1.isSuccess()) {
                    log.info("【水滴换豆卡】获得 {} 个京豆", rep1.getBeanCount());
                    return;
                }
            } else {
                log.debug("您目前水滴: {} g,水滴换豆卡 {} 张,暂不满足水滴换豆的条件,为您继续浇水", totalEnergy, beanCard);
            }
        }
        if (retainWater() > totalEnergy) {
            log.debug("保留水滴不足,停止继续浇水");
            return;
        }
        Integer overageEnergy = totalEnergy - retainWater();
        InitForFarmResponse.FarmUserPro farmUserPro = threadLocal.get().getInitForFarmResponse().getFarmUserPro();
        if (overageEnergy >= farmUserPro.getTreeTotalEnergy() - farmUserPro.getTreeEnergy()) {
            //如果现有的水滴，大于水果可兑换所需的对滴(也就是把水滴浇完，水果就能兑换了)
            for (int i = 0; i < (farmUserPro.getTreeTotalEnergy()-farmUserPro.getTreeEnergy()) / 10; i++) {
                WaterGoodForFarmResponse waterResult = waterGoodForFarm();
                log.debug("本次浇水结果(水果马上就可兑换了): {}", JSONUtil.toJsonStr(waterResult));
                if (waterResult.isSuccess()) {
                    if (BooleanUtil.isTrue(waterResult.getFinished())) {
                        throw new FruitFinishedException();
                    } else {
                        log.debug("目前水滴 {} g,继续浇水，水果马上就可以兑换了", waterResult.getTotalEnergy());
                    }
                } else {
                    log.debug("浇水出现失败异常,跳出不在继续浇水");
                }
            }
        } else if (overageEnergy >= 10) {
            log.debug("目前剩余水滴 {} g，可继续浇水", overageEnergy);
            for (int i = 0; i < overageEnergy / 10; i++) {
                WaterGoodForFarmResponse waterResult = waterGoodForFarm();
                if (waterResult.isSuccess()) {
                    log.debug("浇水10g，剩余 {} g", waterResult.getTotalEnergy());
                    if (BooleanUtil.isTrue(waterResult.getFinished())) {
                        throw new FruitFinishedException();
                    } else {
                        gotStageAward(waterResult);
                    }
                } else {
                    log.debug("浇水出现失败异常,跳出不在继续浇水");
                }
            }
        } else {
            log.debug("目前剩余水滴 {} g,不再继续浇水,保留部分水滴用于完成第二天【十次浇水得水滴】任务", totalEnergy);
        }
    }

    /**
     * 使用道具卡
     */
    private UserMyCardForFarmResponse userMyCardForFarm(String card) throws TaskStopException {
        return request(UserMyCardForFarmResponse.class, "userMyCardForFarm", new JdBodyParam().cardType(card));
    }

    /**
     * 预测水果成熟时间
     */
    private void predictionFruit() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        log.info("开始预测水果成熟时间");
        InitForFarmResponse farmInfo = initForFarm();
        TaskInitForFarmResponse farmTask = taskInitForFarm();
        Integer waterEveryDayT = farmTask.getTotalWaterTaskInit().getTotalWaterTaskTimes();
        log.debug("今日共浇水 {} 次", waterEveryDayT);
        InitForFarmResponse.FarmUserPro farmUserPro = farmInfo.getFarmUserPro();
        log.debug("剩余 水滴 {} g💧", farmUserPro.getTotalEnergy());
        // 水果进度百分比
        BigDecimal mul = NumberUtil.mul(100, NumberUtil.div(farmUserPro.getTreeEnergy(), farmUserPro.getTreeTotalEnergy(), 2));
        int needTimes = new Double(NumberUtil.div(farmUserPro.getTreeTotalEnergy() - farmUserPro.getTreeEnergy(), 10, 0)).intValue();
        log.info("水果进度 {} 还需要浇水 {} 次", mul + " %", needTimes);
        int waterTotalT = (farmUserPro.getTreeTotalEnergy() - farmUserPro.getTreeEnergy() - farmUserPro.getTotalEnergy()) / 10;
        int waterD = waterTotalT / waterEveryDayT + (waterTotalT%waterEveryDayT == 0 ? 0 : 1);
        String chineseDate = DateUtil.formatChineseDate(DateUtil.offsetDay(new Date(), waterD), false, false);
        log.info("【预测】{} ({})可兑换水果", waterD == 1 ? "明天" : (waterD == 2 ? "后天" : waterD + "天之后"), chineseDate);
    }

    /**
     * 领取首次浇水奖励
     */
    private void getFirstWaterAward() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        TaskInitForFarmResponse farmTask = taskInitForFarm();
        if (BooleanUtil.isFalse(farmTask.getFirstWaterInit().getF()) && farmTask.getFirstWaterInit().getTotalWaterTimes() > 0) {
            JdBaseAmountResponse firstWaterReward = firstWaterTaskForFarm();
            if (firstWaterReward.isSuccess()) {
                log.info("【首次浇水奖励】获得 {} g💧", firstWaterReward.getAmount());
            } else {
                log.info("领取首次浇水奖励结果 -> {}", JSONUtil.toJsonStr(firstWaterReward));
            }
        } else {
            log.info("首次浇水奖励已经领取过了");
        }
    }

    /**
     * 领取首次浇水奖励API
     */
    private JdBaseAmountResponse firstWaterTaskForFarm() throws TaskStopException {
        return request(JdBaseAmountResponse.class, "firstWaterTaskForFarm");
    }

    /**
     * 领取十次浇水奖励
     */
    private void getTenWaterAward() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        TaskInitForFarmResponse farmTask = threadLocal.get().getTaskInitForFarmResponse();
        TaskInitForFarmResponse.TotalWaterTaskInit totalWaterTaskInit = farmTask.getTotalWaterTaskInit();
        if (BooleanUtil.isFalse(totalWaterTaskInit.getF()) && totalWaterTaskInit.getTotalWaterTaskTimes() >= totalWaterTaskInit.getTotalWaterTaskLimit()) {
            TotalWaterTaskForFarmResponse totalWaterReward = totalWaterTaskForFarm();
            if (totalWaterReward.isSuccess()) {
                log.info("【十次浇水奖励】获得 {} g💧", totalWaterReward.getTotalWaterTaskEnergy());
            } else {
                log.debug("领取10次浇水奖励结果 -> {}", JSONUtil.toJsonStr(totalWaterReward));
            }
        } else if (totalWaterTaskInit.getTotalWaterTaskTimes() <= totalWaterTaskInit.getTotalWaterTaskLimit()) {
            log.info("【十次浇水奖励】任务未完成，今日浇水 {} 次", totalWaterTaskInit.getTotalWaterTaskTimes());
        }
    }

    /**
     * 领取10次浇水奖励API
     */
    private TotalWaterTaskForFarmResponse totalWaterTaskForFarm() throws TaskStopException {
        return request(TotalWaterTaskForFarmResponse.class, "totalWaterTaskForFarm");
    }

    /**
     * 领取为2好友浇水奖励
     */
    private void getWaterFriendGotAward() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        TaskInitForFarmResponse farmTask = taskInitForFarm();
        TaskInitForFarmResponse.WaterFriendTaskInit waterFriendTaskInit = farmTask.getWaterFriendTaskInit();
        Boolean waterFriendGotAward = waterFriendTaskInit.getWaterFriendGotAward();
        Integer waterFriendSendWater = waterFriendTaskInit.getWaterFriendSendWater();
        Integer waterFriendMax = waterFriendTaskInit.getWaterFriendMax();
        Integer waterFriendCountKey = waterFriendTaskInit.getWaterFriendCountKey();
        if (waterFriendCountKey >= waterFriendMax) {
            if (BooleanUtil.isFalse(waterFriendGotAward)) {
                WaterFriendGotAwardForFarmResponse rep = waterFriendGotAwardForFarm();
                if (rep.isSuccess()) {
                    log.info("【给 {} 个好友浇水】奖励 {} g水滴", waterFriendMax, rep.getAddWater());
                }
            } else {
                log.debug("给好友浇水的 {} g水滴奖励已领取", waterFriendSendWater);
            }
        } else {
            log.debug("暂未给 {} 个好友浇水", waterFriendMax);
        }
    }

    /**
     * 领取给2个好友浇水后的奖励水滴API
     */
    private WaterFriendGotAwardForFarmResponse waterFriendGotAwardForFarm() throws TaskStopException {
        return request(WaterFriendGotAwardForFarmResponse.class, "waterFriendGotAwardForFarm",
                new JdBodyParam().version(4).channel(1));
    }

    /**
     * 小鸭子游戏
     */
    private void duck() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        for (int i = 0; i < 10; i++) {
            GetFullCollectionRewardResponse duckRes = getFullCollectionReward();
            if (duckRes.isSuccess()) {
                if (BooleanUtil.isFalse(duckRes.getHasLimit())) {
                    log.info("小鸭子游戏：{}", duckRes.getTitle());
                }
            } else if ("10".equals(duckRes.getCode())) {
                log.debug("小鸭子游戏达到上限");
                break;
            }
        }
    }

    /**
     * 鸭子，点我有惊喜
     */
    private GetFullCollectionRewardResponse getFullCollectionReward() throws TaskStopException {
        return requestPost(GetFullCollectionRewardResponse.class, "getFullCollectionReward",
                new JdBodyParam().type(2).version(6).channel(2));
    }

    /**
     * 打印农场信息
     */
    private void printInitFarmInfo(InitForFarmResponse initForFarmResponse) throws TaskStopException {
        InitForFarmResponse.FarmUserPro farmUserPro = initForFarmResponse.getFarmUserPro();
        Assert.notNull(farmUserPro, "没有获取到水果信息");
        SheepLogger log = loggerThreadLocal.get();
        // 打印水果信息
        log.info("【水果名称】" + farmUserPro.getName());
        log.info("【已兑换水果】" + farmUserPro.getWinTimes() + "次");
        if (initForFarmResponse.treeStateFinished()) {
            // 成熟
            log.info("【提醒⏰】水果已成熟");
        } else if (initForFarmResponse.treeStateNotPlant()) {
            // 未开始种植
            log.info("【提醒⏰】您忘了种植新的水果");
        } else if (initForFarmResponse.treeStatePlanting()) {
            // 种植中
            log.info("水果种植中...");
        }
    }

    /**
     * 初始化农场信息
     */
    private InitForFarmResponse initForFarm() throws InitForFarmException, JdCookieExpiredException {
        InitForFarmResponse initForFarmResponse = getJdFruitService().tryInitForFarm(threadLocal.get().getJdCookieVO().toString());
        // 当前方法名
        /*String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        InitForFarmResponse initForFarmResponse = null;
        int count = 1;
        do {
            try {
                count++;
                initForFarmResponse = request(InitForFarmResponse.class, methodName,
                        new JdBodyParam().babelChannel("121").sid("a3c52b5f17ab2a42398939a2787eaf8w")
                                .un_area("17_1381_0_0").version(18).channel(1).getBody());
            } catch (JdCookieExpiredException e) {
                throw e;
            } catch (Exception e) {
                // 初始化农场数据异常
                logger.info("初始化农场数据异常 -> {}", e.getMessage());
            }
        } while ((initForFarmResponse == null || initForFarmResponse.getFarmUserPro() == null) && count <= INIT_FARM_MAX_COUNT);
        logger.info("农场数据初始化完成 -> {}", JSONUtil.toJsonStr(initForFarmResponse));

        // 缓存互助码
        getRedisService().sSet(getShareCodeRedisKey(JdConstants.SHARE_CODE_KEY_JD_FRUIT), initForFarmResponse.getFarmUserPro().getShareCode());
        return initForFarmResponse;*/
        if (initForFarmResponse == null || initForFarmResponse.getFarmUserPro() == null) {
            throw new InitForFarmException();
        }
        threadLocal.get().setInitForFarmResponse(initForFarmResponse);
        return initForFarmResponse;
    }

    private <T> T request(Class<T> t, String functionId, String body) throws TaskStopException {
        if (isInterrupted) {
            throw new TaskStopException();
        }
        // HttpResponse execute = httpRequest.execute();
        MyHttpResponse execute = taskUrl(Method.GET, functionId, body).execute();
        String responseBody = execute.body();
        if (debugEnable()) {
            // System.out.println("----------------------------");
            // System.out.println("请求地址：" + httpRequest.getUrl());
            // System.out.println("响应体：" + responseBody);
            // System.out.println("----------------------------");
        }
        if (execute.isOk()) {
            JdBaseResponse jdBaseResponse = JSONUtil.toBean(responseBody, JdBaseResponse.class);
            if (jdBaseResponse.isSuccess()) {
                return JSONUtil.toBean(responseBody, t);
            } else if ("3".equals(jdBaseResponse.getCode())) {
                // 没有登录
                throw new JdCookieExpiredException();
            } else {
                return JSONUtil.toBean(responseBody, t);
            }
        } else {
            throw new RuntimeException("东东农场发起请求失败 HTTP状态码：" + execute.getStatus());
        }
    }

    private <T> T request(Class<T> t, String functionId, JdBodyParam p) throws TaskStopException {
        return request(t, functionId, p.getBody());
    }

    private <T> T request(Class<T> t, String functionId) throws TaskStopException {
        return request(t, functionId, "{}");
    }

    private <T> T requestPost(Class<T> t, String functionId, String body) throws TaskStopException {
        checkStop();
        // HttpRequest httpRequest = taskUrl(Method.POST, functionId, body);
        // HttpResponse execute = httpRequest.execute();
        MyHttpResponse execute = taskUrl(Method.POST, functionId, body).execute();
        String responseBody = execute.body();
        if (execute.isOk()) {
            JdBaseResponse jdBaseResponse = JSONUtil.toBean(responseBody, JdBaseResponse.class);
            if (jdBaseResponse.isSuccess()) {
                return JSONUtil.toBean(responseBody, t);
            } else {
                return JSONUtil.toBean(responseBody, t);
            }
        } else {
            throw new RuntimeException("东东农场发起请求失败 HTTP状态码：" + execute.getStatus());
        }
    }

    private <T> T requestPost(Class<T> t, String functionId, JdBodyParam p) throws TaskStopException {
        return requestPost(t, functionId, p.getBody());
    }

    private <T> T requestPost(Class<T> t, String functionId) throws TaskStopException {
        return requestPost(t, functionId, "{}");
    }

    private MyHttpRequest.HttpRequestBuilder taskUrl(Method method, String functionId, String body) {
        body = StrUtil.blankToDefault(body, "{}");
        String url = StrUtil.format("{}?functionId={}&body={}&appid=wh5", JD_API_HOST, functionId, URLEncodeUtil.encodeAll(body));
        // HttpRequest httpRequest = HttpUtil.createRequest(method, url)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .header(Header.ORIGIN, "https://carry.m.jd.com")
        //         .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
        //         .header(Header.REFERER, "https://carry.m.jd.com/")
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(TIMEOUT);
        // return httpRequest;
        MyHttpRequest.HttpRequestBuilder builder = MyHttpRequest.create(method).uri(url).host("api.m.jd.com").accept("*/*")
                .origin("https://carry.m.jd.com").referer("https://carry.m.jd.com/")
                .acceptEncoding("gzip, deflate, br").acceptLanguage("zh-CN,zh-Hans;q=0.9")
                .connectionKeepAlive()
                .userAgent(getUserAgent()).disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
        return builder;
    }

    /**
     * 日常任务
     */
    public void doDailyTask() throws InterruptedException {
        // 初始化任务
        taskInitForFarm();
        // 签到
        /*TaskInitForFarmResponse.SignInit signInit = taskInitForFarmResponse.getSignInit();
        if (BooleanUtil.isTrue(signInit.getTodaySigned())) {
            log.log2("今天已签到,连续签到{},下次签到可得{}g", signInit.getTotalSigned(), signInit.getSignEnergyEachAmount());
        } else {
            SignForFarmResponse signForFarmResponse = signForFarm();
            if (signForFarmResponse.isSuccess()) {
                log.log2("【签到成功】获得{]g", signForFarmResponse.getAmount());
            } else {
                log.log2("签到结果：{}", signForFarmResponse == null ? null : JSONUtil.toJsonStr(signForFarmResponse));
            }
        }*/
        // 被水滴砸中
        doDailyTaskHitByWater();
        // 广告浏览任务
        doDailyTaskDoBrowseAdTask();
        // 定时领水
        doDailyTaskTimeToCollectWater();
        // 给好友浇水
        doDailyTaskWaterFriends();
        // 打卡领水
        clockInIn();
        // 水滴雨
        executeWaterRains();
        // 领取额外水滴奖励
        getExtraAward();
        // 天天抽奖得好礼
        turntableFarm();
    }

    /**
     * 浇水10次
     */
    private void doTenWater() throws FruitFinishedException, InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        MyCardInfoForFarmResponse myCardInfoRes = myCardInfoForFarm();
        if (useBeanCard() && myCardInfoRes.getBeanCard() > 0 && JSONUtil.toJsonStr(myCardInfoRes).contains("限时翻倍")) {
            log.info("您设置的是使用水滴换豆卡，且背包有水滴换豆卡{}张, 跳过10次浇水任务", myCardInfoRes.getBeanCard());
            return;
        }
        TaskInitForFarmResponse farmTask = threadLocal.get().getTaskInitForFarmResponse();
        TaskInitForFarmResponse.TotalWaterTaskInit totalWaterTaskInit = farmTask.getTotalWaterTaskInit();
        if (totalWaterTaskInit.getTotalWaterTaskTimes() < totalWaterTaskInit.getTotalWaterTaskLimit()) {
            log.info("准备浇水10次");
            int waterCount = 0;
            boolean isFruitFinished = false;
            for (; waterCount < totalWaterTaskInit.getTotalWaterTaskLimit() - totalWaterTaskInit.getTotalWaterTaskTimes(); waterCount++) {
                log.debug("第 {} 次浇水", waterCount + 1);
                WaterGoodForFarmResponse waterResult = waterGoodForFarm();
                if (waterResult.isSuccess()) {
                    log.debug("浇水成功，剩余 {} g💧", waterResult.getTotalEnergy());
                    if (BooleanUtil.isTrue(isFruitFinished = waterResult.getFinished())) {
                        break;
                    } else {
                        if (waterResult.getTotalEnergy() < 10) {
                            log.info("水滴不够");
                            break;
                        }
                        // 领取阶段性水滴奖励
                        gotStageAward(waterResult);
                    }
                } else {
                    log.debug("浇水失败 -> {}", JSONUtil.toJsonStr(waterResult));
                }
            }
            if (isFruitFinished) {
                log.info("【提醒⏰】账号 {} 水果已成熟",
                        threadLocal.get().getInitForFarmResponse().getFarmUserPro().getNickName());
                throw new FruitFinishedException();
            }
        }
    }

    /**
     * 领取阶段性水滴奖励
     */
    private void gotStageAward(WaterGoodForFarmResponse waterResult) throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        if (waterResult.getWaterStatus() == 0 && waterResult.getTreeEnergy() == 10) {
            // 果树发芽 领取30g
            GotStageAwardForFarmResponse gotStageAwardForFarmRes = gotStageAwardForFarm("1");
            if (gotStageAwardForFarmRes.isSuccess()) {
                log.debug("果树发芽，奖励 {} g💧", gotStageAwardForFarmRes.getAddEnergy());
            }
        } else if (waterResult.getWaterStatus() == 1) {
            // 果树开花了,奖励40g水滴
            GotStageAwardForFarmResponse gotStageAwardForFarmRes = gotStageAwardForFarm("2");
            if (gotStageAwardForFarmRes.isSuccess()) {
                log.debug("果树开花，奖励 {} g💧", gotStageAwardForFarmRes.getAddEnergy());
            }
        } else if (waterResult.getWaterStatus() == 2) {
            GotStageAwardForFarmResponse gotStageAwardForFarmRes = gotStageAwardForFarm("3");
            if (gotStageAwardForFarmRes.isSuccess()) {
                log.debug("果树结果了，奖励 {} g💧", gotStageAwardForFarmRes.getAddEnergy());
            }
        }
    }

    /**
     * 领取浇水过程中的阶段性奖励
     */
    private GotStageAwardForFarmResponse gotStageAwardForFarm(String type) throws TaskStopException {
        return request(GotStageAwardForFarmResponse.class, "gotStageAwardForFarm", new JdBodyParam().type(type));
    }

    /**
     * 浇水
     */
    private WaterGoodForFarmResponse waterGoodForFarm() throws InterruptedException {
        Thread.sleep(3000);
        checkStop();
        return request(WaterGoodForFarmResponse.class, "waterGoodForFarm");
    }

    /**
     * 获取道具信息
     */
    private MyCardInfoForFarmResponse myCardInfoForFarm() throws TaskStopException {
        return request(MyCardInfoForFarmResponse.class, "myCardInfoForFarm", new JdBodyParam().version(5).channel(1));
    }

    /**
     * 初始化日常任务列表
     */
    public TaskInitForFarmResponse taskInitForFarm() throws TaskStopException {
        loggerThreadLocal.get().debug("初始化任务列表");
        TaskInitForFarmResponse taskInitForFarmResponse = request(TaskInitForFarmResponse.class, "taskInitForFarm",
                new JdBodyParam().version(14).channel(1).babelChannel("45").getBody());
        if (taskInitForFarmResponse == null) {
            throw new TaskInitForFarmException();
        }
        threadLocal.get().setTaskInitForFarmResponse(taskInitForFarmResponse);
        return taskInitForFarmResponse;
    }

    /**
     * 签到任务
     *
     * @deprecated API 已废弃
     */
    @Deprecated
    private SignForFarmResponse signForFarm() throws TaskStopException {
        return request(SignForFarmResponse.class, "signForFarm");
    }

    /**
     * 被水滴砸中
     */
    private void doDailyTaskHitByWater() throws TaskStopException {
        InitForFarmResponse farmInfo = threadLocal.get().getInitForFarmResponse();
        if (farmInfo.getTodayGotWaterGoalTask() != null && BooleanUtil.isTrue(farmInfo.getTodayGotWaterGoalTask().getCanPop())) {
            GotWaterGoalTaskForFarmResponse goalResult = gotWaterGoalTaskForFarm();
            if (goalResult.isSuccess()) {
                loggerThreadLocal.get().info("【被水滴砸中获得 {} g💧】", goalResult.getAddEnergy());
            }
        }
    }

    /**
     * 广告浏览
     */
    private void doDailyTaskDoBrowseAdTask() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        log.info("开始广告浏览任务");
        TaskInitForFarmResponse.GotBrowseTaskAdInit gotBrowseTaskAdInit = threadLocal.get().getTaskInitForFarmResponse().getGotBrowseTaskAdInit();
        if (BooleanUtil.isFalse(gotBrowseTaskAdInit.getF())) {
            List<TaskInitForFarmResponse.UserBrowseTaskAd> adverts = gotBrowseTaskAdInit.getUserBrowseTaskAds();
            // 广告浏览任务获取多少水滴
            AtomicInteger browseReward = new AtomicInteger(0);
            for (TaskInitForFarmResponse.UserBrowseTaskAd advert : adverts) {
                if (advert.getLimit() <= advert.getHadFinishedTimes()) {
                    log.debug("跳过已完成任务：{}", advert.getMainTitle());
                    continue;
                }
                log.debug("正在进行广告浏览任务 {} -> {}", advert.getShowTitle(), advert.getSubTitle());
                BrowseAdTaskForFarmResponse browseAdTaskForFarmResponse = browseAdTaskForFarm(advert.getAdvertId());
                if (browseAdTaskForFarmResponse.isSuccess()) {
                    log.debug("{} 浏览任务完成", advert.getMainTitle());
                    // 开始领取奖励
                    BrowseAdTaskForFarmRwardResponse browseAdTaskForFarmRwardResponse = browseAdTaskForFarmRward(advert.getAdvertId());
                    if (browseAdTaskForFarmRwardResponse.isSuccess()) {
                        browseReward.addAndGet(browseAdTaskForFarmRwardResponse.getAmount());
                        log.debug("领取奖励 {} g💧", browseAdTaskForFarmRwardResponse.getAmount());
                    } else {
                        log.debug("领取结果：{}", JSONUtil.toJsonStr(browseAdTaskForFarmRwardResponse));
                    }
                } else {
                    // 浏览任务失败
                    log.debug("广告浏览任务结果：{}", JSONUtil.toJsonStr(browseAdTaskForFarmResponse));
                }
            }
            log.debug("广告浏览任务完成，获取 {} g💧", browseReward.get());
        } else {
            log.info("广告浏览任务已完成");
        }
    }

    /**
     * 定时领水任务
     */
    private void doDailyTaskTimeToCollectWater() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        TaskInitForFarmResponse.GotThreeMealInit gotThreeMealInit = threadLocal.get().getTaskInitForFarmResponse().getGotThreeMealInit();
        if (BooleanUtil.isTrue(gotThreeMealInit.getF())) {
            log.info("当前不在定时领水时间断或者已经领过");
        } else {
            GotThreeMealForFarmResponse gotThreeMealForFarmResponse = gotThreeMealForFarm();
            if (gotThreeMealForFarmResponse.isSuccess()) {
                log.info("定时领水获得 {} g💧", gotThreeMealForFarmResponse.getAmount());
            } else {
                log.debug("定时领水结果: {}", JSONUtil.toJsonStr(gotThreeMealForFarmResponse));
            }
        }
    }

    /**
     * 给好友浇水任务
     */
    private void doDailyTaskWaterFriends() throws TaskStopException {
        TaskInitForFarmResponse.WaterFriendTaskInit waterFriendTaskInit = threadLocal.get().getTaskInitForFarmResponse().getWaterFriendTaskInit();
        if (BooleanUtil.isTrue(waterFriendTaskInit.getF())) {
            loggerThreadLocal.get().info("给 {} 个好友浇水任务已完成", waterFriendTaskInit.getWaterFriendMax());
        } else {
            if (waterFriendTaskInit.getWaterFriendCountKey() < waterFriendTaskInit.getWaterFriendMax()) {
                doFriendsWater();
            }
        }
    }

    /**
     * 浏览广告任务
     * type为0时, 完成浏览任务
     * type为1时, 领取浏览任务奖励
     */
    private BrowseAdTaskForFarmResponse browseAdTaskForFarm(String advertId) throws TaskStopException {
        return request(BrowseAdTaskForFarmResponse.class, "browseAdTaskForFarm",
                new JdBodyParam().advertId(advertId).type(0).version(14).channel(1).babelChannel("45").getBody());
    }

    /**
     * 领取浏览广告奖励
     *
     * @param advertId
     * @return
     */
    private BrowseAdTaskForFarmRwardResponse browseAdTaskForFarmRward(String advertId) throws TaskStopException {
        return request(BrowseAdTaskForFarmRwardResponse.class, "browseAdTaskForFarm",
                new JdBodyParam().advertId(advertId).type(1).version(14).channel(1).babelChannel("45").getBody());
    }

    /**
     * 定时领水
     */
    private GotThreeMealForFarmResponse gotThreeMealForFarm() throws TaskStopException {
        return request(GotThreeMealForFarmResponse.class, "gotThreeMealForFarm");
    }

    /**
     * 给好友浇水
     */
    private void doFriendsWater() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        FriendListInitForFarmResponse friendListInitForFarmResponse = friendListInitForFarm();
        List<FriendListInitForFarmResponse.Friend> friends = friendListInitForFarmResponse.getFriends();
        // 刷新水滴数量
        TaskInitForFarmResponse taskInitForFarmResponse = taskInitForFarm();
        TaskInitForFarmResponse.WaterFriendTaskInit waterFriendTaskInit = taskInitForFarmResponse.getWaterFriendTaskInit();
        Integer waterFriendCountKey = waterFriendTaskInit.getWaterFriendCountKey();
        Integer waterFriendMax = waterFriendTaskInit.getWaterFriendMax();
        log.info("今日已给 {} 个好友浇水", waterFriendCountKey);
        if (waterFriendCountKey < waterFriendMax) {
            if (CollUtil.isNotEmpty(friends)) {
                AtomicInteger waterFriendSuccess = new AtomicInteger();
                List<FriendListInitForFarmResponse.Friend> collect = friends.stream().filter(r -> 1 == r.getFriendState()).collect(Collectors.toList());
                for (int i = 0; i < collect.size(); i++) {
                    FriendListInitForFarmResponse.Friend friend = collect.get(i);
                    WaterFriendForFarmResponse waterFriendForFarmResponse = waterFriendForFarm(friend.getShareCode());
                    log.debug("正在给第 {} 个好友浇水,结果：{}", i + 1, JSONUtil.toJsonStr(waterFriendForFarmResponse));
                    if (waterFriendForFarmResponse.isSuccess()) {
                        waterFriendSuccess.incrementAndGet();
                        WaterFriendForFarmResponse.CardInfo cardInfo = waterFriendForFarmResponse.getCardInfo();
                        if (cardInfo != null) {
                            log.debug("获得道具：{}", cardInfo.getCardName());
                        }
                    } else if ("11".equals(waterFriendForFarmResponse.getCode())) {
                        log.debug("水滴不够，跳出浇水");
                    }
                }
                log.debug("给 {} 个好友浇水成功，消耗 {} g💧", waterFriendSuccess, waterFriendSuccess.intValue() * 10);
            } else {
                log.info("好友列表为空");
            }
        } else {
            log.debug("今日为好友浇水量已达 {} 个", waterFriendMax);
        }
        log.info("给好友浇水任务完成");
    }

    /**
     * 被水滴砸中API
     */
    private GotWaterGoalTaskForFarmResponse gotWaterGoalTaskForFarm() throws TaskStopException {
        return request(GotWaterGoalTaskForFarmResponse.class, "gotWaterGoalTaskForFarm", new JdBodyParam().type(3));
    }

    /**
     * 获取好友列表
     */
    private FriendListInitForFarmResponse friendListInitForFarm() throws TaskStopException {
        return request(FriendListInitForFarmResponse.class, "friendListInitForFarm",
                new JdBodyParam().version(4).channel(1).getBody());
    }

    /**
     * 给好友浇水
     */
    private WaterFriendForFarmResponse waterFriendForFarm(String shareCode) throws TaskStopException {
        return request(WaterFriendForFarmResponse.class, "waterFriendForFarm",
                new JdBodyParam().shareCode(shareCode).version(6).channel(1));
    }

    /**
     * 打卡领水
     */
    private void clockInIn() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        log.debug("开始打卡领水活动（签到，关注，领券）");
        ClockInInitForFarmResponse clockInInitForFarmResponse = clockInInitForFarm();
        if (clockInInitForFarmResponse.isSuccess()) {
            if (BooleanUtil.isFalse(clockInInitForFarmResponse.getTodaySigned())) {
                log.debug("开始今日签到");
                ClockInForFarmResponse clockInForFarmResponse = clockInForFarm();
                if (clockInForFarmResponse.isSuccess()) {
                    log.debug("第 {} 天签到成功，获得 {} g💧", clockInForFarmResponse.getSignDay(), clockInForFarmResponse.getAmount());
                    if (7 == clockInForFarmResponse.getSignDay()) {
                        log.debug("开始领取--惊喜礼包 xx g💧");
                        GotClockInGiftResponse gotClockInGiftResponse = gotClockInGift();
                        if (gotClockInGiftResponse.isSuccess()) {
                            log.debug("【惊喜礼包】获得 {} g💧", gotClockInGiftResponse.getAmount());
                        }
                    }
                }
            }
            if (BooleanUtil.isTrue(clockInInitForFarmResponse.getTodaySigned()) && clockInInitForFarmResponse.getTotalSigned() == 7) {
                log.debug("开始领取--惊喜礼包38g💧");
                GotClockInGiftResponse gotClockInGiftResponse = gotClockInGift();
                if (gotClockInGiftResponse.isSuccess()) {
                    log.debug("【惊喜礼包】获得 {} g💧", gotClockInGiftResponse.getAmount());
                }
            }
            // 限时关注得水滴
            List<ClockInInitForFarmResponse.Theme> themes = clockInInitForFarmResponse.getThemes();
            if (CollUtil.isNotEmpty(themes)) {
                for (ClockInInitForFarmResponse.Theme theme : themes) {
                    if (BooleanUtil.isFalse(theme.getHadGot())) {
                        log.debug("开始关注 {} ID {}", theme.getName(), theme.getId());
                        ClockInFollowForFarmResponse rep1 = clockInFollowForFarm(theme.getId(), "theme", "1");
                        if (rep1.isSuccess() || "11".equals(rep1.getCode())) {
                            ClockInFollowForFarmResponse rep2 = clockInFollowForFarm(theme.getId(), "theme", "2");
                            if (rep2.isSuccess()) {
                                log.debug("领取 关注 {} 获得 {} g💧", theme.getName(), rep2.getAmount());
                            } else {
                                log.debug("领取关注任务奖励失败 -> {}", JSONUtil.toJsonStr(rep2));
                            }
                        } else {
                            log.debug("关注任务 失败 -> {}", JSONUtil.toJsonStr(rep1));
                        }
                    }
                }
            }
            // 限时领券得水滴
            List<ClockInInitForFarmResponse.VenderCoupon> venderCoupons = clockInInitForFarmResponse.getVenderCoupons();
            if (CollUtil.isNotEmpty(venderCoupons)) {
                for (ClockInInitForFarmResponse.VenderCoupon venderCoupon : venderCoupons) {
                    if (BooleanUtil.isFalse(venderCoupon.getHadGot())) {
                        log.debug("开始领券 {} ID {}", venderCoupon.getName(), venderCoupon.getId());
                        ClockInFollowForFarmResponse rep1 = clockInFollowForFarm(venderCoupon.getId(), "venderCoupon", "1");
                        if (rep1.isSuccess() || "11".equals(rep1.getCode())) {
                            ClockInFollowForFarmResponse rep2 = clockInFollowForFarm(venderCoupon.getId(), "venderCoupon", "2");
                            if (rep2.isSuccess()) {
                                log.debug("领券 {} 获得 {} g💧", venderCoupon.getName(), rep2.getAmount());
                            } else {
                                log.debug("领取领券奖励失败 -> {}", JSONUtil.toJsonStr(rep2));
                            }
                        } else {
                            log.debug("领券任务 失败 -> {}", JSONUtil.toJsonStr(rep1));
                        }
                    }
                }
            }
        }
        log.debug("打卡领水活动（签到，关注，领券）结束");
    }

    private ClockInInitForFarmResponse clockInInitForFarm() throws TaskStopException {
        return request(ClockInInitForFarmResponse.class, "clockInInitForFarm");
    }

    private ClockInForFarmResponse clockInForFarm() throws TaskStopException {
        return request(ClockInForFarmResponse.class, "clockInForFarm", new JdBodyParam().type(1).getBody());
    }

    /**
     * 领水滴38g
     */
    private GotClockInGiftResponse gotClockInGift() throws TaskStopException {
        return request(GotClockInGiftResponse.class, "clockInForFarm", new JdBodyParam().type(2).getBody());
    }

    private ClockInFollowForFarmResponse clockInFollowForFarm(String id, String type, String step) throws TaskStopException {
        return request(ClockInFollowForFarmResponse.class, "clockInFollowForFarm",
                new JdBodyParam().id(id).type(type).step(step));
    }

    /**
     * 水滴雨
     */
    private void executeWaterRains() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        JdFruitTaskThreadLocalDTO tmp = threadLocal.get();
        TaskInitForFarmResponse taskInitForFarmResponse = tmp.getTaskInitForFarmResponse();
        TaskInitForFarmResponse.WaterRainInit waterRainInit = taskInitForFarmResponse.getWaterRainInit();
        if (BooleanUtil.isFalse(waterRainInit.getF())) {
            log.debug("水滴雨任务，每天两次，最多可得10g💧");
            // System.out.println("两次水滴雨任务是否全部完成");
            if (waterRainInit.getLastTime() != null) {
                int tmpi = waterRainInit.getLastTime() + 3 * 60 * 60 * 1000;
                if (new Date().getTime() < tmpi) {
                    log.debug("【第 {} 次水滴雨】未到时间，请 {} 再试", waterRainInit.getWinTimes(), DateUtil.format(new Date(new Long(tmpi)), "HH:MM:ss"));
                    return;
                }
            }
            log.debug("开始水滴雨任务,这是第 {} 次,剩余 {} 次", waterRainInit.getWinTimes() + 1, 2 - (waterRainInit.getWinTimes() + 1));
            WaterRainForFarmResponse waterRainForFarmResponse = waterRainForFarm();
            if (waterRainForFarmResponse.isSuccess()) {
                log.debug("水滴雨任务执行成功，获得水滴：{}", waterRainForFarmResponse.getAddEnergy());
                log.debug("【第 {} 次水滴雨】获得 {} g💧", waterRainInit.getWinTimes() + 1, waterRainForFarmResponse.getAddEnergy());
            }
        }
    }

    private WaterRainForFarmResponse waterRainForFarm() throws TaskStopException {
        return request(WaterRainForFarmResponse.class, "waterRainForFarm", "{\"type\": 1,\"hongBaoTimes\": 100,\"version\": 3}");
    }

    /**
     * 领取额外水滴奖励
     */
    private void getExtraAward() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        log.debug("领取额外水滴奖励");
        FarmAssistInitResponse farmAssistInitResponse = farmAssistInit();
        if (farmAssistInitResponse.isSuccess()) {
            List<FarmAssistInitResponse.AssistFriend> assistFriendList = farmAssistInitResponse.getAssistFriendList();
            /*if (CollUtil.isNotEmpty(assistFriendList) && assistFriendList.size() >= 2) {
                Integer status = farmAssistInitResponse.getStatus();
                if (status == 2) {
                    AtomicInteger num = new AtomicInteger();
                    int k = 0;
                    for (FarmAssistInitResponse.AssistStage assistStage : farmAssistInitResponse.getAssistStageList()) {
                        k++;
                        if (assistStage.getStageStaus() == 2) {
                            ReceiveStageEnergyResponse receiveStageEnergyResponse = receiveStageEnergy();
                            if (receiveStageEnergyResponse.isSuccess()) {
                                log.debug("已成功领取第 {} 阶段好友助力奖励：{} g💧", k, receiveStageEnergyResponse.getAmount());
                                num.addAndGet(receiveStageEnergyResponse.getAmount());
                            }
                        }
                    }
                }
                if (status == 3) {
                    log.debug("【额外奖励】已被领取过");
                }
            } else {
                log.debug("【额外奖励】领取失败,原因：给您助力的人未达2个");
            }*/
            // 打印助力我的好友信息
            if (CollUtil.isNotEmpty(assistFriendList)) {
                for (FarmAssistInitResponse.AssistFriend assistFriend : assistFriendList) {
                    log.debug("好友：{} 在 {} 给您助力过", assistFriend.getNickName(), DateUtil.formatDateTime(new Date(assistFriend.getTime())));
                }
            }
            List<FarmAssistInitResponse.AssistStage> assistStageList = farmAssistInitResponse.getAssistStageList();
            if (CollUtil.isNotEmpty(assistStageList)) {
                for (FarmAssistInitResponse.AssistStage assistStage : assistStageList) {
                    Integer stageStaus = assistStage.getStageStaus();
                    Integer assistNum = assistStage.getAssistNum();
                    Integer waterEnergy = assistStage.getWaterEnergy();
                    Integer stage = assistStage.getStage();
                    if (2 == stageStaus) {
                        // 领取水滴
                        ReceiveStageEnergyResponse receiveStageEnergyResponse = receiveStageEnergy();
                        if (receiveStageEnergyResponse.isSuccess()) {
                            log.debug("已成功领取第 {} 阶段 {} 个好友助力奖励 {} g💧", stage, assistNum, receiveStageEnergyResponse.getAmount());
                        }
                    } else if (3 == stageStaus) {
                        // 【额外奖励】已被领取过
                        log.debug("第 {} 阶段 {} 个好友助力奖励 {} g💧,已领取过了", stage, assistNum, waterEnergy);
                    } else if (1 == stageStaus) {
                        // 为满足领取条件
                        continue;
                    }
                }
            }
            log.info("领取额外奖励水滴结束");
        } else {
            // 旧版本助力好友信息...
        }
    }

    /**
     * 获取被助力结果
     */
    private FarmAssistInitResponse farmAssistInit() throws TaskStopException {
        return request(FarmAssistInitResponse.class, "farmAssistInit",
                new JdBodyParam().version(14).channel(1).babelChannel("120").getBody());
    }

    private ReceiveStageEnergyResponse receiveStageEnergy() throws TaskStopException {
        return request(ReceiveStageEnergyResponse.class, "receiveStageEnergy",
                new JdBodyParam().version(14).channel(1).babelChannel("120").getBody());
    }

    /**
     * 天天抽奖得好礼
     */
    private void turntableFarm() throws InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        InitForTurntableFarmResponse initForTurntableFarmResponse = initForTurntableFarm();
        if (initForTurntableFarmResponse.isSuccess()) {
            // 领取定时奖励 //4小时一次
            Integer timingIntervalHours = initForTurntableFarmResponse.getTimingIntervalHours();
            Long timingLastSysTime = initForTurntableFarmResponse.getTimingLastSysTime();
            Long sysTime = initForTurntableFarmResponse.getSysTime();
            Boolean timingGotStatus = initForTurntableFarmResponse.getTimingGotStatus();
            int remainLotteryTimes = initForTurntableFarmResponse.getRemainLotteryTimes();
            List<InitForTurntableFarmResponse.TurntableInfo> turntableInfos = initForTurntableFarmResponse.getTurntableInfos();
            if (BooleanUtil.isFalse(timingGotStatus)) {
                log.debug("是否到了领取免费赠送的抽奖机会----");
                if (sysTime > (timingLastSysTime + 60 * 60 * timingIntervalHours * 1000)) {
                    TimingAwardForTurntableFarmResponse timingAwardForTurntableFarmResponse = timingAwardForTurntableFarm();
                    log.debug("领取定时奖励结果：{}", JSONUtil.toJsonStr(timingAwardForTurntableFarmResponse));
                    remainLotteryTimes = initForTurntableFarm().getRemainLotteryTimes();
                } else {
                    log.debug("免费赠送的抽奖机会未到时间");
                }
            } else {
                log.debug("4小时候免费赠送的抽奖机会已领取");
            }
            List<InitForTurntableFarmResponse.TurntableBrowserAd> turntableBrowserAds = initForTurntableFarmResponse.getTurntableBrowserAds();
            if (CollUtil.isNotEmpty(turntableBrowserAds)) {
                for (int i = 0; i < turntableBrowserAds.size(); i++) {
                    InitForTurntableFarmResponse.TurntableBrowserAd turntableBrowserAd = turntableBrowserAds.get(i);
                    if (BooleanUtil.isFalse(turntableBrowserAd.getStatus())) {
                        log.debug("开始浏览天天抽奖的第 {} 个逛会场任务", i + 1);
                        BrowserForTurntableFarmResponse browserForTurntableFarmResponse = browserForTurntableFarm(1, turntableBrowserAd.getAdId());
                        if (browserForTurntableFarmResponse.isSuccess() && BooleanUtil.isTrue(browserForTurntableFarmResponse.getStatus())) {
                            log.debug("第 {} 个逛会场任务完成，开始领取水滴奖励", i + 1);
                            BrowserForTurntableFarmResponse browserForTurntableFarmResponse1 = browserForTurntableFarm(2, turntableBrowserAd.getAdId());
                            if (browserForTurntableFarmResponse1.isSuccess()) {
                                log.debug("第 {} 个逛会场任务领取水滴奖励完成", i + 1);
                                remainLotteryTimes = initForTurntableFarm().getRemainLotteryTimes();
                            }
                        }
                    }
                }
            }
            log.debug("---天天抽奖次数--- -> {} 次", remainLotteryTimes);
            if (remainLotteryTimes > 0) {
                log.debug("开始抽奖");
                for (int i = 0; i < remainLotteryTimes; i++) {
                    LotteryForTurntableFarmResponse lotteryForTurntableFarmResponse = lotteryForTurntableFarm();
                    if (lotteryForTurntableFarmResponse.isSuccess()) {
                        turntableInfos.forEach(turntableInfo -> {
                            // String itemType = turntableInfo.getType();
                            if (turntableInfo.getType().equals(lotteryForTurntableFarmResponse.getType())) {
                                // final boolean isBean = itemType.contains("bean");
                                // final boolean isCoupon = itemType.contains("coupon");
                                // final boolean isThanks = itemType.contains("thanks");
                                log.debug("抽奖结果: {}", turntableInfo.getName());
                            }
                        });
                        // 没有次数了
                        if (lotteryForTurntableFarmResponse.getRemainLotteryTimes() == 0) {
                            log.debug("没有抽奖次数了");
                            break;
                        }
                    }
                }
            } else {
                log.debug("天天抽奖--抽奖机会为 0 次");
            }
        } else {
            log.debug("初始化天天抽奖得好礼失败");
        }
    }

    /**
     * 初始化集卡抽奖活动数据API
     */
    private InitForTurntableFarmResponse initForTurntableFarm() throws TaskStopException {
        return request(InitForTurntableFarmResponse.class, "initForTurntableFarm",
                new JdBodyParam().version(4).channel(1).getBody());
    }

    private TimingAwardForTurntableFarmResponse timingAwardForTurntableFarm() throws TaskStopException {
        return request(TimingAwardForTurntableFarmResponse.class, "timingAwardForTurntableFarm",
                new JdBodyParam().version(4).channel(1).getBody());
    }

    private BrowserForTurntableFarmResponse browserForTurntableFarm(Integer type, String adId) throws TaskStopException {
        if (type == 1) {
            loggerThreadLocal.get().debug("浏览爆品会场");
        } else if (type == 2) {
            loggerThreadLocal.get().debug("天天抽奖浏览任务领取水滴");
        }
        String body = new JSONObject().set("type", type).set("adId", adId).set("version", 4).set("channel", 1).toString();
        return request(BrowserForTurntableFarmResponse.class, "browserForTurntableFarm", body);
    }

    private LotteryForTurntableFarmResponse lotteryForTurntableFarm() throws InterruptedException {
        Thread.sleep(2000);
        checkStop();
        return request(LotteryForTurntableFarmResponse.class, "lotteryForTurntableFarm",
                new JdBodyParam().type(1).version(4).channel(1).getBody());
    }

    private JdFruitService getJdFruitService() {
        return SpringUtil.getBean(JdFruitService.class);
    }
}
