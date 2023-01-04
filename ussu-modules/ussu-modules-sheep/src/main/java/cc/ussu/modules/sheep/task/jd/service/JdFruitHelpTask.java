package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.modules.sheep.common.SheepLogger;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.entity.SheepEnv;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.InitForFarmException;
import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitHelpThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitInviteCodeVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 东东农场内部水滴互助
 */
@Component
@DisallowConcurrentExecution
public class JdFruitHelpTask extends SheepQuartzJobBean<String> implements JdSheepTask {

    private static final ThreadLocal<JdFruitHelpThreadLocalDTO> threadLocal = new ThreadLocal<>();

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    @Override
    public String getTaskName() {
        return "东东农场内部水滴互助";
    }

    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/fruit/help/" + fileName + ".log";
    }

    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        loggerThreadLocal.get().info("正在收集助力码...");
        getFruitService().collectInviteCode(params);
        for (String param : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(param);
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            threadLocal.set(new JdFruitHelpThreadLocalDTO().setJdCookieVO(jdCookieVO));
            try {
                // getCollect();
                jdFruit();
            } catch (InitForFarmException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info("账号 {} ck过期");
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void jdFruit() throws InterruptedException, InitForFarmException {
        checkStop();
        InitForFarmResponse farmInfo = getFruitService().tryInitForFarm(threadLocal.get().getJdCookieVO().toString());
        if (farmInfo.isSuccess()) {
            threadLocal.get().setFarmInfo(farmInfo);
            checkStop();
            masterHelpShare();
            checkStop();
            turntableFarm();
        }
    }

    /**
     * 天天抽奖活动
     */
    private void turntableFarm() throws TaskStopException {
        checkStop();
        SheepLogger log = loggerThreadLocal.get();
        InitForTurntableFarmResponse initForTurntableFarmRes = getFruitService().initForTurntableFarm(threadLocal.get().getJdCookieVO().toString());
        if (initForTurntableFarmRes.isSuccess()) {
            Integer remainLotteryTimes = initForTurntableFarmRes.getRemainLotteryTimes();
            List<InitForTurntableFarmResponse.TurntableInfo> turntableInfos = initForTurntableFarmRes.getTurntableInfos();
            log.debug("开始天天抽奖--好友助力--每人每天只有三次助力机会.");
            InitForFarmResponse farmInfo = threadLocal.get().getFarmInfo();
            for (JdFruitInviteCodeVO code : getCollect()) {
                if (code.getInviteCode().equals(farmInfo.getFarmUserPro().getShareCode())) {
                    log.debug("天天抽奖-不能自己给自己助力");
                    continue;
                }
                LotteryMasterHelpResponse lotteryMasterHelpRes = lotteryMasterHelp(code.getInviteCode());
                HelpFriendResponse.HelpResult helpResult = lotteryMasterHelpRes.getHelpResult();
                if (lotteryMasterHelpRes.isSuccess() && helpResult != null) {
                    String helpResultCode = helpResult.getCode();
                    HelpFriendResponse.MasterUserInfo masterUserInfo = helpResult.getMasterUserInfo();
                    if ("0".equals(helpResultCode)) {
                        log.debug("天天抽奖-助力 {} 成功", masterUserInfo.getNickName());
                    } else if ("11".equals(helpResultCode)) {
                        log.debug("天天抽奖-不要重复助力 {}", masterUserInfo.getNickName());
                    } else if ("13".equals(helpResultCode)) {
                        log.debug("天天抽奖-助力 {} 失败，助力次数已耗尽", masterUserInfo.getNickName());
                        break;
                    }
                }
            }
            log.debug("天天抽奖次数共 {} 次", remainLotteryTimes);
            // 抽奖
            if (remainLotteryTimes > 0) {
                log.debug("开始抽奖");
                List<String> lotteryResult = new ArrayList<>();
                for (int i = 0; i < remainLotteryTimes; i++) {
                    LotteryForTurntableFarmResponse lotteryRes = lotteryForTurntableFarm();
                    if (lotteryRes.isSuccess()) {
                        turntableInfos.forEach(item -> {
                            if (item.getType().equals(lotteryRes.getType())) {
                                lotteryResult.add(item.getName());
                            }
                        });
                        if (lotteryRes.getRemainLotteryTimes() == 0) {
                            // 没有抽奖次数了
                            break;
                        }
                    }
                }
                if (CollUtil.isNotEmpty(lotteryResult)) {
                    log.info("天天抽奖获得：{}", CollUtil.join(lotteryResult, ","));
                }
            } else {
                log.debug("抽奖完成，没有抽奖次数了");
            }
        } else {
            log.debug("初始化天天抽奖得好礼失败");
        }
    }

    private LotteryForTurntableFarmResponse lotteryForTurntableFarm() throws TaskStopException {
        try {
            Thread.sleep(2000);
            loggerThreadLocal.get().debug("等待了2秒");
        } catch (InterruptedException e) {
        }
        checkStop();
        return getFruitService().request(threadLocal.get().getJdCookieVO().toString(), LotteryForTurntableFarmResponse.class,
                "lotteryForTurntableFarm", new JdBodyParam().type(1).version(4).channel(1));
    }

    private LotteryMasterHelpResponse lotteryMasterHelp(String code) throws TaskStopException {
        checkStop();
        return getFruitService().request(threadLocal.get().getJdCookieVO().toString(), LotteryMasterHelpResponse.class, "initForFarm",
                new JdBodyParam().imageUrl("").nickName("").shareCode(code + "-3").babelChannel("3").version(4).channel(1));
    }

    /**
     * 读取互助码
     */
    private Set<JdFruitInviteCodeVO> getCollect() throws TaskStopException {
        checkStop();
        Set<JdFruitInviteCodeVO> inviteCodes = SpringUtil.getBean(JdFruitService.class).getInviteCodes();
        List<SheepEnv> envs = getEnvService().getList(JdConstants.JD_COOKIE);
        if (CollUtil.isNotEmpty(inviteCodes) && CollUtil.isNotEmpty(envs)) {
            Set<JdFruitInviteCodeVO> list = new LinkedHashSet<>();
            for (SheepEnv env : envs) {
                for (JdFruitInviteCodeVO inviteCode : inviteCodes) {
                    if (inviteCode.getPin().equals(JdCkWskUtil.getPinByCk(env.getValue()))) {
                        list.add(inviteCode);
                    }
                }
            }
            return list;
        }
        return inviteCodes;
    }

    /**
     * 助力好友
     */
    private void masterHelpShare() throws InterruptedException {
        SheepLogger log = loggerThreadLocal.get();
        Thread.sleep(2000);
        checkStop();
        InitForFarmResponse farmInfo = getFruitService().tryInitForFarm(threadLocal.get().getJdCookieVO().toString());
        AtomicInteger salveHelpAddWater = new AtomicInteger(0);
        boolean llhelp = true;
        if (llhelp) {
            Set<JdFruitInviteCodeVO> newShareCodes = getCollect();
            for (JdFruitInviteCodeVO shareCode : newShareCodes) {
                if (shareCode != null && StrUtil.isNotBlank(shareCode.getInviteCode())) {
                    loggerThreadLocal.get().info("正在助力 {} ...", shareCode.getPin());
                    if (farmInfo.getFarmUserPro() == null) {
                        // 未种植
                        log.debug("账号 {} 水果未种植");
                        continue;
                    }
                    if (shareCode.getInviteCode().equals(farmInfo.getFarmUserPro().getShareCode())) {
                        // 自己不能给自己助力
                        log.debug("不能给自己助力");
                        continue;
                    }
                    HelpFriendResponse helpResultResponse = masterHelp(shareCode.getInviteCode());
                    if (helpResultResponse.isSuccess()) {
                        HelpFriendResponse.HelpResult helpResult = helpResultResponse.getHelpResult();
                        HelpFriendResponse.MasterUserInfo masterUserInfo = helpResult.getMasterUserInfo();
                        String masterNickName = masterUserInfo.getNickName();
                        String helpResultCode = helpResult.getCode();
                        if ("0".equals(helpResultCode)) {
                            salveHelpAddWater.addAndGet(helpResult.getSalveHelpAddWater());
                            log.info("助力成功，已成功给 {} 助力，获得 {} g💧", masterNickName, helpResult.getSalveHelpAddWater());
                        } else if ("8".equals(helpResultCode)) {
                            log.info("助力失败，今天的助力次数已耗尽");
                        } else if ("9".equals(helpResultCode)) {
                            log.info("之前给 {} 助力过了", masterNickName);
                        } else if ("10".equals(helpResultCode)) {
                            log.info("好友 {} 已满5人助力", masterNickName);
                        } else {
                            log.info("助力结果：{}", JSONUtil.toJsonStr(helpResult));
                        }
                        log.info("【今日助力次数还剩】{} 次", helpResult.getRemainTimes());
                        if (0 == helpResult.getRemainTimes()) {
                            log.info("助力次数已耗尽，跳出");
                            break;
                        }
                    } else {
                        log.info("助力失败");
                    }
                }
            }
        }
        if (salveHelpAddWater.intValue() > 0) {
            log.info("助力好友👬获得 {} g💧", salveHelpAddWater);
        }
        log.info("助力好友结束，即将开始领取额外水滴奖励");
    }

    private HelpFriendResponse masterHelp(String shareCode) throws TaskStopException {
        checkStop();
        return getFruitService().request(threadLocal.get().getJdCookieVO().toString(), HelpFriendResponse.class, "initForFarm",
                new JdBodyParam().imageUrl("").nickName("").shareCode(shareCode).babelChannel("3").version(2).channel(1));
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

}
