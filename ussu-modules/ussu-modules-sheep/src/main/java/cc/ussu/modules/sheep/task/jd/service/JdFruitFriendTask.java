package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.modules.sheep.common.SheepLogger;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitFriendThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitInviteCodeVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.FriendListInitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.HelpFriendResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForFarmResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 东东农场好友删减奖励
 */
@Component
@DisallowConcurrentExecution
public class JdFruitFriendTask extends SheepQuartzJobBean<String> implements JdSheepTask {

    private static final ThreadLocal<JdFruitFriendThreadLocalDTO> threadLocal= new ThreadLocal<>();

    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    @Override
    public String getTaskName() {
        return "东东农场好友删减奖励";
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/fruit/friend/" + fileName + ".log";
    }

    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        loggerThreadLocal.get().info("正在收集助力码...");
        getFruitService().collectInviteCode(params);
        for (String param : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(param);
            threadLocal.set(new JdFruitFriendThreadLocalDTO().setJdCookieVO(jdCookieVO));
            try {
                jdFruit();
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info("账号 {} ck失效", jdCookieVO.getPt_pin());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                loggerThreadLocal.get().info("未知错误：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 执行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void jdFruit() throws InterruptedException {
        checkStop();
        InitForFarmResponse farmInfo = getFruitService().tryInitForFarm(threadLocal.get().getJdCookieVO().toString());
        threadLocal.get().setFarmInfo(farmInfo);
        // 删除好友与接受邀请成为别人的好友
        getAwardInviteFriend();
    }

    /**
     * 删除好友与接受邀请成为别人的好友
     */
    private void getAwardInviteFriend() throws InterruptedException {
        checkStop();
        SheepLogger log = loggerThreadLocal.get();
        FriendListInitForFarmResponse friendList = getFruitService().friendListInitForFarm(threadLocal.get().getJdCookieVO().toString());
        if (friendList.isSuccess()) {
            log.info("今日已邀请好友 {} 个/每日邀请上限 {} 个", friendList.getInviteFriendCount(), friendList.getInviteFriendMax());
            log.info("开始删除 {} 个好友，可拿每天的奖励", CollUtil.size(friendList.getFriends()));
            if (CollUtil.isNotEmpty(friendList.getFriends())) {
                for (FriendListInitForFarmResponse.Friend friend : friendList.getFriends()) {
                    checkStop();
                    Thread.sleep(1000);
                    log.info("开始删除好友 {} -> {}", friend.getNickName(), friend.getFriendState());
                    JdBaseResponse delRes = getFruitService().request(threadLocal.get().getJdCookieVO().toString(), JdBaseResponse.class, "deleteFriendForFarm",
                            new JdBodyParam().shareCode(friend.getShareCode()).version(8).channel(1));
                    if (delRes.isSuccess()) {
                        log.info("删除好友成功");
                    }
                }
            }
            // 为他人助力,接受邀请成为别人的好友
            receiveFriendInvite();
            if (friendList.getInviteFriendCount() > 0) {
                if (friendList.getInviteFriendCount() > friendList.getInviteFriendGotAwardCount()) {
                    log.debug("开始领取邀请好友的奖励");
                    checkStop();
                    JdBaseResponse awardInviteFriendForFarmRep = getFruitService().request(threadLocal.get().getJdCookieVO().toString(), JdBaseResponse.class, "awardInviteFriendForFarm");
                    if (awardInviteFriendForFarmRep.isSuccess()) {
                        log.debug("邀请好友奖励领取成功");
                    } else {
                        log.debug("邀请好友奖励可能领取失败");
                    }
                }
            } else {
                log.info("您今天没有邀请过好友");
            }
        } else {
            log.info("查询好友列表失败");
        }
    }

    /**
     * 为他人助力,接受邀请成为别人的好友
     */
    private void receiveFriendInvite() throws TaskStopException {
        checkStop();
        Set<JdFruitInviteCodeVO> shareCodes = getCollect();
        for (JdFruitInviteCodeVO shareCode : shareCodes) {
            if (shareCode.getInviteCode().equals(threadLocal.get().getFarmInfo().getFarmUserPro().getShareCode())) {
                loggerThreadLocal.get().debug("自己不能邀请自己成为好友噢");
                continue;
            }
            HelpFriendResponse inviteFriendRes = inviteFriend(shareCode.getInviteCode());
            if (inviteFriendRes.isSuccess()) {
                HelpFriendResponse.HelpResult helpResult = inviteFriendRes.getHelpResult();
                if (helpResult != null) {
                    if ("0".equals(helpResult.getCode())) {
                        loggerThreadLocal.get().info("接收邀请成为好友结果成功,您已成为 {} 的好友", helpResult.getMasterUserInfo().getNickName());
                    } else if ("17".equals(helpResult.getCode())) {
                        loggerThreadLocal.get().info("接收邀请成为好友结果失败,对方已是您的好友");
                    }
                }
            }
        }
    }

    /**
     * 接受对方邀请,成为对方好友的API
     */
    private HelpFriendResponse inviteFriend(String shareCode) throws TaskStopException {
        checkStop();
        return getFruitService().request(threadLocal.get().getJdCookieVO().toString(), HelpFriendResponse.class, "initForFarm",
                new JdBodyParam().imageUrl("").nickName("").shareCode(shareCode + "-inviteFriend").version(4).channel(2));
    }

    /**
     * 读取互助码
     */
    private Set<JdFruitInviteCodeVO> getCollect() {
        return SpringUtil.getBean(JdFruitService.class).getInviteCodes();
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }
}
