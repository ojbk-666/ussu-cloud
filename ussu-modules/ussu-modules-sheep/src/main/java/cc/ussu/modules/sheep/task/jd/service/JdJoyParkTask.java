package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.SheepLogger;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.exception.joy.GameShopListException;
import cc.ussu.modules.sheep.task.jd.exception.joy.GetJoyBaseInfoException;
import cc.ussu.modules.sheep.task.jd.exception.joy.GetJoyListException;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyParkTaskThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.joypark.response.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 汪汪乐园每日任务
 */
@Component
@DisallowConcurrentExecution
public class JdJoyParkTask extends SheepQuartzJobBean<String> {

    private static final ThreadLocal<JdJoyParkTaskThreadLocalDTO> threadLocal = new ThreadLocal<>();

    public static final String AUTHOR_INVITER_PIN = "BIuLwDXt6Y5OfZ5DHXJHO8apYKBykBOEB0mPrngm668";

    private String getUserAgent() {
        return "jdltapp;iPhone;4.2.0;;;M/5.0;hasUPPay/0;pushNoticeIsOpen/0;lang/zh_CN;hasOCPay/0;appBuild/1217;supportBestPay/0;jdSupportDarkMode/0;ef/1;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1;";
    }

    /**
     * 最大化硬币收益模式
     */
    private boolean enableJoyCoinMaximizeMode() {
        return true;
    }

    private int randomSleep() {
        return RandomUtil.randomInt(1000, 3000);
    }

    @Override
    public String getTaskName() {
        return "汪汪乐园养joy";
    }

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD_SPEED;
    }

    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/joy/park/" + fileName + ".log";
    }

    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        for (String param : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(param);
            threadLocal.set(new JdJoyParkTaskThreadLocalDTO().setJdCookieVO(jdCookieVO));
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            try {
                // helpAuthor();
                TaskResponse taskResponse = getTaskList();
                doTaskList(taskResponse);
                printJoyInfo();
                Thread.sleep(randomSleep());
                getJoyList(true);
                Thread.sleep(randomSleep());
                getGameShopList();
                Thread.sleep(randomSleep());
                //清理工位
                doJoyMoveDownAll();
                Thread.sleep(randomSleep());
                doJoyMergeAll();
                Thread.sleep(randomSleep());
                getGameMyPrize();
            } catch (JdCookieExpiredException e) {
                logger.info("ck过期");
                loggerThreadLocal.get().info("账号 {} ck失效", jdCookieVO.getPt_pin());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                logger.info("未知异常：{}", e.getMessage());
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void getGameMyPrize() throws InterruptedException {
        GameMyPrizeResponse gameMyPrizeResponse = requestPost(GameMyPrizeResponse.class, "gameMyPrize", "body={\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform");
        if (gameMyPrizeResponse.isSuccess() && gameMyPrizeResponse.getData() != null) {
            List<GameMyPrizeResponse.GameMyPrizeData.GamePrizeItemVo> Vos = gameMyPrizeResponse.getData().getGamePrizeItemVos();
            for (GameMyPrizeResponse.GameMyPrizeData.GamePrizeItemVo vo : Vos) {
                if (vo.getPrizeType() == 4 && vo.getStatus() == 1 && vo.getPrizeTypeVO().getPrizeUsed() == 0) {
                    loggerThreadLocal.get().info("当前账号有 {} 可提现", vo.getPrizeName());
                    GameMyPrizeResponse.GameMyPrizeData.GamePrizeItemVo.PrizeTypeVO prizeTypeVO = vo.getPrizeTypeVO();
                    // 提现
                    apCashWithDraw(prizeTypeVO);
                    Thread.sleep(1500);
                }
            }
        }
    }

    /**
     * 提现
     */
    private void apCashWithDraw(GameMyPrizeResponse.GameMyPrizeData.GamePrizeItemVo.PrizeTypeVO vo) throws TaskStopException {
        String body = "body={\"businessSource\":\"JOY_PARK\",\"base\":{\"id\":"+vo.getId()+",\"business\":\"joyPark\",\"poolBaseId\":"+vo.getPoolBaseId()+",\"prizeGroupId\":"+vo.getPrizeGroupId()+",\"prizeBaseId\":"+vo.getPrizeBaseId()+",\"prizeType\":4},\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&_t="+new Date().getTime()+"&appid=activities_platform";
        JdJoyBaseResponse jdJoyBaseResponse = requestPost(JdJoyBaseResponse.class, "apCashWithDraw", body);
        if (jdJoyBaseResponse.isSuccess()) {
            loggerThreadLocal.get().debug("提现成功");
        }
    }

    private void doJoyMoveDownAll() throws InterruptedException {
        List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> workJoyInfoList = threadLocal.get().getJoyListData().getWorkJoyInfoList();
        List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> collect = workJoyInfoList.stream().filter(r -> r.getJoyDTO() != null).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            loggerThreadLocal.get().debug("工位清理完成！");
            return;
        }
        for (GetJoyListResponse.GetJoyListData.WorkJoyInfo workJoyInfo : workJoyInfoList) {
            if (BooleanUtil.isTrue(workJoyInfo.getUnlock()) && workJoyInfo.getJoyDTO() != null) {
                GetJoyListResponse.GetJoyListData.WorkJoyInfo.JoyDTO joyDTO = workJoyInfo.getJoyDTO();
                loggerThreadLocal.get().debug("从工位移除joy -> id：{}，name：{}，level：{}", joyDTO.getId(), joyDTO.getName(), joyDTO.getLevel());
                doJoyMove(joyDTO.getId(), 0);
                Thread.sleep(1500);
            }
        }
        getJoyList(false);
        Thread.sleep(1500);
        doJoyMoveDownAll();
        Thread.sleep(1500);
    }

    private JoyMoveResponse doJoyMove(Integer id, Integer location) throws TaskStopException {
        String body = "body={\"joyId\":"+id+",\"location\":"+location+",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        JoyMoveResponse joyMoveResponse = requestPost(JoyMoveResponse.class, "joyMove", body);
        if (location != 0) {
            loggerThreadLocal.get().debug("下地完成了");
        }
        return joyMoveResponse;
    }

    private void doJoyMergeAll() throws InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        List<GetJoyListResponse.GetJoyListData.ActivityJoy> activityJoyList = threadLocal.get().getJoyListData().getActivityJoyList();
        List<Integer> levels = activityJoyList.stream().map(GetJoyListResponse.GetJoyListData.ActivityJoy::getLevel).sorted().collect(Collectors.toList());
        Integer minLevel = CollUtil.getFirst(levels);
        List<GetJoyListResponse.GetJoyListData.ActivityJoy> joyMinLevelArr = activityJoyList.stream().filter(row -> row.getLevel() == minLevel).collect(Collectors.toList());
        JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo = getJoyBaseInfo("", "", "");
        Integer fastBuyLevel = joyBaseInfo.getFastBuyLevel();
        if (CollUtil.size(joyMinLevelArr) >= 2) {
            log.info("开始合成 {}：{}<---->{}",minLevel, joyMinLevelArr.get(0).getId(), joyMinLevelArr.get(1).getId());
            doJoyMerge(joyMinLevelArr.get(0).getId(), joyMinLevelArr.get(1).getId());
            getJoyList(false);
            doJoyMergeAll();
        } else if (CollUtil.size(joyMinLevelArr) == 1 && joyMinLevelArr.get(0).getLevel() < fastBuyLevel) {
            // 有低于快速购买的joy
            JdJoyBaseResponse rep = doJoyBuy(joyMinLevelArr.get(0).getLevel());
            if (rep.isSuccess()) {
                getJoyList(false);
                doJoyMergeAll();
            } else {
                log.debug("完成");
                doJoyMoveUpAll();
            }
        } else {
            log.info("没有需要合成的joy 开始买买买🛒🛒🛒🛒🛒🛒🛒🛒");
            log.info("现在最高可以购买:{} 购买 {} 的joy，你还有 {} 金币",fastBuyLevel, fastBuyLevel, joyBaseInfo.getJoyCoin());
            JdJoyBaseResponse rep = doJoyBuy(fastBuyLevel);
            if (rep.isSuccess()) {
                getJoyList(false);
                doJoyMergeAll();
            } else {
                log.debug("完成");
                doJoyMoveUpAll();
            }
        }
    }

    private void doJoyMoveUpAll() throws InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        GetJoyListResponse.GetJoyListData joyListData = threadLocal.get().getJoyListData();
        List<GetJoyListResponse.GetJoyListData.ActivityJoy> activityJoyList = joyListData.getActivityJoyList();
        List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> workJoyInfoList = joyListData.getWorkJoyInfoList();
        List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> workJoyInfoUnlockList = workJoyInfoList.stream().filter(r -> BooleanUtil.isTrue(r.getUnlock()) && r.getJoyDTO() == null).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(activityJoyList) && CollUtil.isNotEmpty(workJoyInfoUnlockList)) {
            Integer maxLevelJoy = CollUtil.max(activityJoyList.stream().map(GetJoyListResponse.GetJoyListData.ActivityJoy::getLevel).collect(Collectors.toList()));
            List<GetJoyListResponse.GetJoyListData.ActivityJoy> maxLevelJoyList = activityJoyList.stream().filter(r -> r.getLevel().equals(maxLevelJoy)).collect(Collectors.toList());
            log.info("下地干活！ joyId={} location={}", maxLevelJoyList.get(0).getId(), workJoyInfoUnlockList.get(0).getLocation());
            doJoyMove(maxLevelJoyList.get(0).getId(), workJoyInfoUnlockList.get(0).getLocation());
            getJoyList(false);
            doJoyMoveDownAll();
        } else if (enableJoyCoinMaximizeMode()){
            joyCoinMaximize(workJoyInfoUnlockList);
        }
    }

    private void joyCoinMaximize(List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> workJoyInfoUnlockList) throws InterruptedException {
        boolean hasJoyCoin = true;
        if (CollUtil.isNotEmpty(workJoyInfoUnlockList) && hasJoyCoin) {
            SheepLogger log = loggerThreadLocal.get();
            log.debug("竟然还有工位挖土？开启瞎买瞎下地模式！");
            JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo = getJoyBaseInfo("", "", "");
            Integer joyCoin = joyBaseInfo.getJoyCoin();
            log.debug("还有 {} 金币，看看还能买啥下地");
            List<GameShopListResponse.GameShop> shopList = getGameShopList();
            boolean newBuyCount = false;
            for (int i = shopList.size() -1 ; i >0 && i -3 >=0; i--) {
                if (joyCoin>shopList.get(i).getConsume()) {
                    log.debug("买一只 {} 级的");
                    joyCoin = joyCoin - shopList.get(i).getConsume();
                    JdJoyBaseResponse rep = doJoyBuy(shopList.get(i).getUserLevel());
                    if (!rep.isSuccess()) {
                        break;
                    } else {
                        newBuyCount = true;
                        hasJoyCoin = false;
                        i++;
                    }
                }
                Thread.sleep(1500);
            }
            hasJoyCoin = false;
            if (newBuyCount) {
                getJoyList(false);
                Thread.sleep(1500);
                doJoyMoveUpAll();
                Thread.sleep(1500);
                getJoyBaseInfo("", "", "");
            }
        }
    }

    private JdJoyBaseResponse doJoyBuy(Integer level) throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        String body = "body={\"level\":"+level+",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        JdJoyBaseResponse rep = requestPost(JdJoyBaseResponse.class, "joyBuy", body);
        Integer code = rep.getCode();
        if (code.equals(519)) {
            log.debug("没钱了");
        } else if (code.equals(518)) {
            log.debug("没空位");
            List<GetJoyListResponse.GetJoyListData.ActivityJoy> activityJoyList = threadLocal.get().getJoyListData().getActivityJoyList();
            if (CollUtil.isNotEmpty(activityJoyList)) {
                // 正常买模式
                log.debug("因为购买 {} 级🐶 没空位 所以我要删掉比低级的狗了", level);
                List<Integer> collect = activityJoyList.stream().map(GetJoyListResponse.GetJoyListData.ActivityJoy::getLevel).sorted().collect(Collectors.toList());
                Integer minLevel = CollUtil.getFirst(collect);
                if (minLevel != null) {
                    List<GetJoyListResponse.GetJoyListData.ActivityJoy> collect1 = activityJoyList.stream().filter(r -> r.getLevel().equals(minLevel)).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect1)) {
                        doJoyRecovery(collect1.get(0).getId());
                    }
                }
            }
        } else if (code.equals(0)) {
            log.debug("OK");
        }
        return rep;
    }

    private void doJoyRecovery(Integer joyId) throws TaskStopException {
        String body = "body={\"joyId\":"+joyId +",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        JdJoyBaseResponse rep = requestPost(JdJoyBaseResponse.class, "joyRecovery", body);
        loggerThreadLocal.get().debug("回收🐶 " + (rep.isSuccess() ? "成功":"失败"));
    }

    /**
     * 合并joy
     */
    private void doJoyMerge(Integer joyId1, Integer joyId2) throws TaskStopException {
        String h5st = "20220526204915679%3B3946208156885550%3Bb08cf%3Btk02we1ee1dbb18nffWdQwiLx71mV9VoyryciNclmi3XcKWh%2B%2B1rcvkrZ7S7J8O1uQoPtbNM0ohylcwdqjuciHhlrFMB%3Bca4678a12faa4690140d2d5446233a79ab86be2e5590d468d98b4c3d532f75e9%3B3.0%3B1653569355679";
        String body = "body={\"joyOneId\":"+joyId1+",\"joyTwoId\":"+joyId2+",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform&cthr=1&h5st=" + h5st;
        JdJoyBaseResponse jdJoyBaseResponse = requestGet(JdJoyBaseResponse.class, "joyMergeGet", body);
        loggerThreadLocal.get().debug("合成 {} <--> {} {}", joyId1, joyId2, (jdJoyBaseResponse.isSuccess()?"成功":"失败"));
        if ("1006".equals(jdJoyBaseResponse.getCode()) || "1006".equals(jdJoyBaseResponse.getCode() + "")) {
            loggerThreadLocal.get().debug("请求限流了");
        }
    }

    private List<GameShopListResponse.GameShop> getGameShopList() throws TaskStopException {
        GameShopListResponse gameShopListResponse = requestGet(GameShopListResponse.class, "gameShopList", "appid=activities_platform&body={\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}");
        if (gameShopListResponse.isSuccess()) {
            List<GameShopListResponse.GameShop> collect = gameShopListResponse.getData().stream().filter(r -> r.getShopStatus().equals(1)).collect(Collectors.toList());
            return collect;
        } else {
            throw new GameShopListException();
        }
    }

    private GetJoyListResponse.GetJoyListData getJoyList(boolean printLog) throws InterruptedException {
        String h5st = "20220526203414393%3B5316297164080644%3Be18ed%3Btk02w90831ba818n0FXZRP9aZKuPjhU4esrQ7a25F0acoFJWvcMmzsFM%2Fzi8yWb%2BJJBpLC5Lz4%2BMY%2Fv%2BwIwGzqf3Bzy%2F%3B326131f68af8d7cb7403e7f0fcfe6eab17bc95192f357cd8175ec8d295fa8012%3B3.0%3B1653568454393";
        String body = "appid=activities_platform&body={\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&t=" + new Date().getTime() + "&cthr=1&h5st=" + h5st;
        GetJoyListResponse getJoyListResponse = requestGet(GetJoyListResponse.class, "joyList", body);
        if (getJoyListResponse.isSuccess()) {
            GetJoyListResponse.GetJoyListData data = getJoyListResponse.getData();
            if (printLog) {
                SheepLogger log = loggerThreadLocal.get();
                List<GetJoyListResponse.GetJoyListData.ActivityJoy> activityJoyList = data.getActivityJoyList();
                loggerThreadLocal.get().debug("在逛街的joy⬇️⬇️⬇️⬇️⬇️⬇️⬇️⬇️");
                for (GetJoyListResponse.GetJoyListData.ActivityJoy activityJoy : activityJoyList) {
                    // Thread.sleep(1500);
                    if (activityJoy.getLevel() >= 30) {
                        loggerThreadLocal.get().info("请去极速版app手动解锁新场景...");
                    }
                }
                List<GetJoyListResponse.GetJoyListData.WorkJoyInfo> workJoyInfoList = data.getWorkJoyInfoList();
                loggerThreadLocal.get().debug("在铲土的joy⬇️⬇️⬇️⬇️⬇️⬇️⬇️⬇️");
                for (GetJoyListResponse.GetJoyListData.WorkJoyInfo workJoyInfo : workJoyInfoList) {
                }
            }
            threadLocal.get().setJoyListData(data);
            return data;
        } else {
            throw new GetJoyListException();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JdJoyParkTask jdJoyParkTask = new JdJoyParkTask();
        String ckval = "pt_pin=jd_7b37a844c22b9;pt_key=app_openAAJjWrkQADD_W7A1x08ndwOWoOAPGFwTR8KECmr4bj5mVeamwKyU6oE5NRO5kV2Q7sO4L6dTtEs;";
        jdJoyParkTask.threadLocal.set(new JdJoyParkTaskThreadLocalDTO().setJdCookieVO(new JdCookieVO(ckval)));
        // jdJoyParkTask.getTaskList();
        // jdJoyParkTask.apTaskDetail(810, "BROWSE_PRODUCT");
        // jdJoyParkTask.getJoyBaseInfo("","","");
        jdJoyParkTask.getJoyList(true);
        // jdJoyParkTask.getGameShopList();
        // jdJoyParkTask.getGameMyPrize();
    }

    private void printJoyInfo() throws TaskStopException {
        JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo = getJoyBaseInfo("", "", "");
        SheepLogger log = loggerThreadLocal.get();
        log.info("joy等级：{}，金币：{}", joyBaseInfo.getLevel(), joyBaseInfo.getJoyCoin());
        if (joyBaseInfo.getLevel() >= 30) {
            log.info("请去极速版app手动解锁新场景...");
        }
    }

    private void helpAuthor() throws TaskStopException {
        SheepLogger log = loggerThreadLocal.get();
        log.debug("开始帮作者助力开工位");
        JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo = getJoyBaseInfo("", "2", AUTHOR_INVITER_PIN);
        if (1 == joyBaseInfo.getHelpState()) {
            log.debug("帮作者开工位成功，感谢！");
        } else if (3 == joyBaseInfo.getHelpState()) {
            log.debug("你不是新用户！跳过开工位助力");
        } else if (2 == joyBaseInfo.getHelpState()) {
            log.debug("他的工位已全部开完啦！");
        } else {
            log.debug("开工位失败！");
        }
    }

    /**
     * 获取joy信息
     */
    private JoyBaseInfoResponse.JoyBaseInfo getJoyBaseInfo(String taskId, String inviteType,String inviterPin) throws TaskStopException {
        JdBodyParam jdBodyParam = new JdBodyParam().taskId(taskId).inviteType(inviteType).inviterPin(inviterPin).linkId("LsQNxL7iWDlXUs6cFl-AAg");
        JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo = null;
        for (int i = 0; i < 3 && joyBaseInfo != null; i++) {
            JoyBaseInfoResponse rep = requestPost(JoyBaseInfoResponse.class, "joyBaseInfo", jdBodyParam, "activities_platform");
            if (rep.isSuccess() && rep.getData() != null) {
                joyBaseInfo = rep.getData();
            }
        }
        if (joyBaseInfo == null) {
            throw new GetJoyBaseInfoException();
        }
        threadLocal.get().setJoyBaseInfo(joyBaseInfo);
        return joyBaseInfo;
    }

    /**
     * 获取任务列表
     */
    private TaskResponse getTaskList() throws TaskStopException {
        return requestPost(TaskResponse.class, "apTaskList", "body={\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform");
    }

    private void doTaskList(TaskResponse taskResponse) throws InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        if (taskResponse.isSuccess()) {
            List<TaskResponse.Task> taskList = taskResponse.getData();
            for (TaskResponse.Task task : taskList) {
                if (task.isSignTask()) {
                    if (BooleanUtil.isFalse(task.getTaskFinished())) {
                        log.debug("准备签到");
                        JdJoyBaseResponse rep = apDoTask(task.getId(), task.getTaskType(), null, null);
                        if (rep.isSuccess()) {
                            log.debug("签到完成");
                        }
                    } else {
                    }
                    JdJoyBaseResponse rep = apTaskDrawAward(task.getId(), task.getTaskType());
                    if (rep.isSuccess()) {
                        log.debug("签到奖励领取成功");
                    } else {
                        log.debug("签到奖励领取可能失败");
                    }
                }
                if (task.isBrowseProductTask() || task.isBrowseChannelTask() && task.getTaskLimitTimes() != 1) {
                    ApTaskDetailResponse apTaskDetailResponse = apTaskDetail(task.getId(), task.getTaskType());
                    if (apTaskDetailResponse.isSuccess()) {
                        List<ApTaskDetailResponse.ApTaskDetailData.TaskItem> productList = apTaskDetailResponse.getData().getTaskItemList();
                        int productListNow = 0;
                        if (CollUtil.size(productList) == 0) {
                            JdJoyBaseResponse rep = apTaskDrawAward(task.getId(), task.getTaskType());
                            if (rep.isSuccess()) {
                                log.info("");
                                ApTaskDetailResponse rep2 = apTaskDetail(task.getId(), task.getTaskType());
                                productList = rep2.getData().getTaskItemList();
                            }
                        }
                        while (task.getTaskLimitTimes() - task.getTaskDoTimes() >= 0) {
                            if (CollUtil.size(productList) == 0) {
                                log.debug("{} 活动火爆，素材库没有素材，我也不知道啥回事 = = ", task.getTaskTitle());
                                break;
                            }
                            Thread.sleep(1000);
                            checkStop();
                            JdJoyBaseResponse rep = apDoTask(task.getId(), task.getTaskType(), productList.get(productListNow).getItemId(), productList.get(productListNow).getAppid());
                            if (rep.getCode() == 2005 || rep.getCode() == 0) {
                                log.debug("任务完成");
                            } else {
                                log.debug("任务失败");
                            }
                            productListNow++;
                            task.taskDoTimes++;
                            if (productListNow > productList.size()) {
                                return;
                            }
                        }
                        // 领
                        for (int i = 0; i < task.getTaskLimitTimes(); i++) {
                            JdJoyBaseResponse rep = apTaskDrawAward(task.getId(), task.getTaskType());
                            if (!rep.isSuccess()) {
                                log.debug("{} 领取完成", task.getTaskTitle());
                                break;
                            }
                        }
                    }
                } else if (task.isShareInviteTask()) {
                    Integer yqTaskId = task.getId();
                    // 邀请好友任务
                    for (int i = 0; i < 5; i++) {
                        JdJoyBaseResponse rep = apTaskDrawAward(yqTaskId, task.getTaskType());
                        if (!rep.isSuccess()) {
                            break;
                        }
                        log.info("领取助力奖励成功！");
                    }
                }
                if (task.isBrowseChannelTask() && task.getTaskLimitTimes() == 1) {
                    String body2 = "body={\"taskType\":\""+task.getTaskType()+"\",\"taskId\":"+task.getId()+",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\",\"itemId\":\""+task.getTaskSourceUrl()+"\"}&appid=activities_platform";
                    JdJoyBaseResponse rep = requestPost(JdJoyBaseResponse.class, "apDoTask", body2);
                    if (rep.isSuccess()) {
                        log.debug("任务完成");
                    }
                    JdJoyBaseResponse rep1 = apTaskDrawAward(task.getId(), task.getTaskType());
                    if (rep1.isSuccess()) {
                        log.debug("领取奖励成功");
                    }
                }
                Thread.sleep(1500);
                checkStop();
            }
        }
        // ======汪汪乐园开始内部互助======
        doHelpFriend();
    }

    private void doHelpFriend() {
        List<String> shareCodes = getShareCodes();
        if (CollUtil.isNotEmpty(shareCodes)) {
            for (String shareCode : shareCodes) {
            }
        }
    }

    private List<String> getShareCodes() {
        return null;
    }

    private ApTaskDetailResponse apTaskDetail(Integer taskId, String taskType) throws TaskStopException {
        String body = "functionId=apTaskDetail&body={\"taskType\":\""+taskType+"\",\"taskId\":"+taskId+",\"channel\":4,\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        return requestPost(ApTaskDetailResponse.class, "apTaskDetail", body);
    }

    /**
     * 领取奖励
     */
    private JdJoyBaseResponse apTaskDrawAward(Integer taskId, String taskType) throws TaskStopException {
        String body = "body={\"taskType\":\""+taskType+"\",\"taskId\":"+taskId+",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        return requestPost(JdJoyBaseResponse.class, "apTaskDrawAward", body);
    }

    /**
     * 完成任务
     */
    private JdJoyBaseResponse apDoTask(Integer taskId, String taskType, String itemId, String appid) throws TaskStopException {
        if (itemId == null) {
            itemId = "";
        }
        if (StrUtil.isBlank(appid)) {
            appid = "activities_platform";
        }
        String body = "body={\"taskType\":\""+taskType+"\",\"taskId\":"+taskId+",\"channel\":4,\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\",\"itemId\":\""+itemId+"\"}&appid="+appid+"";
        return requestPost(JdJoyBaseResponse.class, "apDoTask", body);
    }

    private <T> T requestPost(Class<T> t,String functionId, String body) throws RequestErrorException, TaskStopException {
        checkStop();
        MyHttpResponse execute = taskPostClientActionUrl(functionId, body).execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            return JSONUtil.toBean(responseBody, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private <T> T requestGet(Class<T> t,String functionId, String body) throws RequestErrorException, TaskStopException {
        checkStop();
        MyHttpResponse execute = taskGetClientActionUrl(functionId, body).execute();
        if (execute.isOk()) {
            String responseBody = execute.body();
            return JSONUtil.toBean(responseBody, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private <T> T requestPost(Class<T> t, String functionId, JdBodyParam jdBodyParam, String appid) throws RequestErrorException, TaskStopException {
        return requestPost(t, functionId, new StringBuffer("body=").append(jdBodyParam.getBody()).append(StrUtil.isNotBlank(appid) ? "&appid=" + appid : "").toString());
    }

    private MyHttpRequest.HttpRequestBuilder taskGetClientActionUrl(String functionId, String body) {
        String url = "https://api.m.jd.com/?"+ (StrUtil.isNotBlank(functionId) ? "functionId=" + functionId : "");
        url += (StrUtil.isNotBlank(body) ? "&" + body : "");
        // return HttpUtil.createGet(url)
        //         .body(body)
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .contentType(ContentType.FORM_URLENCODED.toString())
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ORIGIN, "https://joypark.jd.com")
        //         .header(Header.REFERER, "https://joypark.jd.com/?activityId=LsQNxL7iWDlXUs6cFl-AAg&lng=113.387899&lat=22.512678&sid=4d76080a9da10fbb31f5cd43396ed6cw&un_area=19_1657_52093_0")
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(10000);
        return MyHttpRequest.createGet("https://api.m.jd.com/?" + URLEncodeUtil.encodeQuery(body) + (StrUtil.isNotBlank(functionId) ? "&functionId=" + functionId : ""))
                .userAgent(getUserAgent()).host("api.m.jd.com").origin("https://joypark.jd.com")
                .connectionKeepAlive()
                .referer("https://joypark.jd.com/?activityId=LsQNxL7iWDlXUs6cFl-AAg&lng=113.387899&lat=22.512678&sid=4d76080a9da10fbb31f5cd43396ed6cw&un_area=19_1657_52093_0")
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

    private MyHttpRequest.HttpRequestBuilder taskPostClientActionUrl(String functionId, String body) {
        // return HttpUtil.createPost("https://api.m.jd.com/?" + (StrUtil.isNotBlank(functionId) ? "functionId=" + functionId : ""))
        //         .body(body)
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .contentType(ContentType.FORM_URLENCODED.toString())
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ORIGIN, "https://joypark.jd.com")
        //         .header(Header.REFERER, "https://joypark.jd.com/?activityId=LsQNxL7iWDlXUs6cFl-AAg&lng=113.387899&lat=22.512678&sid=4d76080a9da10fbb31f5cd43396ed6cw&un_area=19_1657_52093_0")
        //         .cookie(threadLocal.get().getJdCookieVO().toString())
        //         .timeout(10000);
        return MyHttpRequest.createGet("https://api.m.jd.com/?" + URLEncodeUtil.encodeQuery(body) + (StrUtil.isNotBlank(functionId) ? "&functionId=" + functionId : ""))
                .userAgent(getUserAgent()).host("api.m.jd.com").origin("https://joypark.jd.com")
                .connectionKeepAlive()
                .referer("https://joypark.jd.com/?activityId=LsQNxL7iWDlXUs6cFl-AAg&lng=113.387899&lat=22.512678&sid=4d76080a9da10fbb31f5cd43396ed6cw&un_area=19_1657_52093_0")
                .disableCookie().cookie(threadLocal.get().getJdCookieVO().toString());
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }
}
