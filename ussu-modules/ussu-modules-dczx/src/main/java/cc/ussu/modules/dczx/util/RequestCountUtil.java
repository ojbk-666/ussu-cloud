package cc.ussu.modules.dczx.util;

import cc.ussu.common.redis.service.RedisService;
import cn.hutool.extra.spring.SpringUtil;

public class RequestCountUtil {

    public static final String KEY = "ussu:dczx:request_count";

    public static void count() {
        RedisService redisService = SpringUtil.getBean(RedisService.class);
        redisService.redisTemplate.opsForValue().increment(KEY, 1);
    }

    public static long get() {
        Long o = SpringUtil.getBean(RedisService.class).getCacheObject(KEY);
        if (o == null) {
            o = 0L;
        }
        return o;
    }

}
