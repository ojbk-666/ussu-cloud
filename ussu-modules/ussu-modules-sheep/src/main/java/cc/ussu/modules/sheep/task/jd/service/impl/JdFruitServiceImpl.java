package cc.ussu.modules.sheep.task.jd.service.impl;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.InitForFarmException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.TaskInitForFarmException;
import cc.ussu.modules.sheep.task.jd.service.JdFruitService;
import cc.ussu.modules.sheep.task.jd.util.JdCkWskUtil;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitInviteCodeVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.FriendListInitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForTurntableFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.TaskInitForFarmResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JdFruitServiceImpl implements JdFruitService {

    @Autowired
    private RedisService redisService;

    public InitForFarmResponse initForFarm(String ckval) throws JdCookieExpiredException, RequestErrorException {
        return request(ckval, InitForFarmResponse.class, "initForFarm",
                new JdBodyParam().babelChannel("121").sid("a3c52b5f17ab2a42398939a2787eaf8w")
                        .un_area("17_1381_0_0").version(18).channel(1).getBody());
    }

    @Override
    public InitForFarmResponse tryInitForFarm(String ckval) throws InitForFarmException, JdCookieExpiredException, RequestErrorException {
        final int INIT_FARM_MAX_COUNT = 3;  // 重试几次
        InitForFarmResponse initForFarmResponse = null;
        int count = 1;
        do {
            try {
                count++;
                initForFarmResponse = initForFarm(ckval);
            } catch (JdCookieExpiredException e) {
                throw e;
            } catch (Exception e) {
                // 初始化农场数据异常
                logger.info("初始化农场数据异常 -> {}", e.getMessage());
            }
        } while ((initForFarmResponse == null || initForFarmResponse.getFarmUserPro() == null) && count <= INIT_FARM_MAX_COUNT);
        logger.info("农场数据初始化完成 -> {}", JSONUtil.toJsonStr(initForFarmResponse));
        if (initForFarmResponse == null) {
            throw new InitForFarmException();
        }
        if (initForFarmResponse.getFarmUserPro() != null) {
            String pin = JdCkWskUtil.getPinByCk(ckval);
            logger.info("缓存助力码：{} -> {}", pin, initForFarmResponse.getFarmUserPro().getShareCode());
            redisService.redisTemplate.opsForSet().add(getJdFruitShareCodeCacheKey(), new JdFruitInviteCodeVO(pin, initForFarmResponse.getFarmUserPro().getShareCode()));
            redisService.expire(getJdFruitShareCodeCacheKey(), 60L * 60 * 24 * 2);
        }
        return initForFarmResponse;
    }

    @Override
    public InitForFarmResponse initForFarm(JdCookieVO ck) throws JdCookieExpiredException, RequestErrorException {
        return initForFarm(ck.toString());
    }

    /**
     * 收集助力码
     */
    @Override
    public void collectInviteCode(List<String> cks) {
        long l = redisService.redisTemplate.opsForSet().size(getJdFruitShareCodeCacheKey());
        if (l == 0) {
            for (String ck : cks) {
                try {
                    tryInitForFarm(ck);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 获取助力码
     */
    @Override
    public Set<JdFruitInviteCodeVO> getInviteCodes() {
        Set<JdFruitInviteCodeVO> vos = redisService.getCacheSet(getJdFruitShareCodeCacheKey());
        return vos;
    }

    @Override
    public TaskInitForFarmResponse taskInitForFarm(String ckval) throws JdCookieExpiredException, RequestErrorException {
        TaskInitForFarmResponse taskInitForFarmResponse = request(ckval, TaskInitForFarmResponse.class,
                "taskInitForFarm", new JdBodyParam().version(14).channel(1).babelChannel("45").getBody());
        if (taskInitForFarmResponse == null) {
            throw new TaskInitForFarmException();
        }
        return taskInitForFarmResponse;
    }

    @Override
    public InitForTurntableFarmResponse initForTurntableFarm(String ckval) {
        return request(ckval, InitForTurntableFarmResponse.class, "initForTurntableFarm",
                new JdBodyParam().version(4).channel(1).getBody());
    }

    @Override
    public FriendListInitForFarmResponse friendListInitForFarm(String ckval) {
        return request(ckval, FriendListInitForFarmResponse.class, "friendListInitForFarm",
                new JdBodyParam().version(4).channel(1).getBody());
    }
}
