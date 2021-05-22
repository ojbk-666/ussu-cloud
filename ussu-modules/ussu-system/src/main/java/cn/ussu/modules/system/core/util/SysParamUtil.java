package cn.ussu.modules.system.core.util;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.RedisConstants;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.system.entity.SysParam;
import cn.ussu.modules.system.service.ISysParamService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SysParamUtil {
    private static RedisService redisUtil = SpringContextHolder.getBean(RedisService.class);

    private SysParamUtil() {
    }

    /**
     * 写入系统参数到缓存
     */
    public static int init() {
        ISysParamService sysParamService = SpringContextHolder.getBean(ISysParamService.class);
        List<SysParam> paramList = sysParamService.list();
        Map<String, Object> allParamMap = new HashMap<>();
        for (SysParam sysParam : paramList) {
            allParamMap.put(sysParam.getParamKey(), sysParam.getParamValue());
        }
        redisUtil.setCacheMap(RedisConstants.SYS_PARAM, allParamMap);
        return paramList.size();
    }

    /**
     * 获取指定的参数值
     *
     * @param key key
     * @param defaultValue 默认值
     * @return
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        if (StrUtil.isBlank(value)) return defaultValue;
        return value;
    }

    /**
     * 获取指定的参数值
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return ((String) redisUtil.getCacheMapValue(RedisConstants.SYS_PARAM, key));
    }
}
