package cc.ussu.modules.ecps.skill.job;

import cc.ussu.modules.ecps.skill.service.IEbSeckillSessionService;
import lombok.extern.java.Log;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Log
@EnableAsync
public class SkillSkuUploadJob {

    @Autowired
    private IEbSeckillSessionService seckillSessionService;
    @Autowired
    private RedissonClient redissonClient;

    private final String SKILL_LOCK = "skill:lock";

    @Scheduled(cron = "0 * * * * ?")
    public void uploadSkillSku() {
        log.info("开始上架秒杀商品");
        RLock lock = redissonClient.getLock(SKILL_LOCK);
        try {
            lock.lock();
            // 上架当天的秒杀商品
            seckillSessionService.uploadSkillSku();
        } finally {
            lock.unlock();
        }
    }

}
