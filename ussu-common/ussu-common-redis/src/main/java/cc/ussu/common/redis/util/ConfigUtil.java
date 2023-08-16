package cc.ussu.common.redis.util;

import cc.ussu.common.core.vo.SelectVO;
import cc.ussu.common.redis.service.RedisService;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ConfigUtil {

    public static final String KEY_PREFIX = "ussu:system:sys-config:";

    public ConfigUtil() {
    }

    @Deprecated
    private static final String getKey(String key) {
        return KEY_PREFIX + key;
    }

    private static final String getKey(String module, String key) {
        return KEY_PREFIX + module + ":" + key;
    }

    private static RedisService getRedisService() {
        return SpringUtil.getBean(RedisService.class);
    }

    /**
     * 获取字典值
     *
     * @param group        类型
     * @param item         编码
     * @param defaultValue 默认值
     * @return
     */
    public static String getValue(String module, String group, String item, String defaultValue) {
        Map<String, Object> cacheMap = getRedisService().getCacheMap(getKey(module, group));
        String v = MapUtil.getStr(cacheMap, item);
        if (StrUtil.isBlank(v)) {
            return defaultValue;
        }
        return v;
    }

    /**
     * 获取字典值
     *
     * @param group 类型
     * @param item  编码
     * @return
     */
    public static String getValue(String module, String group, String item) {
        return getValue(module, group, item, null);
    }

    /**
     * 获取字典项的多个值
     *
     * @param group 类型
     * @return
     */
    public static Map<String, String> getValue(String module, String group) {
        return getRedisService().getCacheMap(getKey(module, group));
    }

    /**
     * 获取boolean类型值
     *
     * @param group
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getValueBoolean(String group, String key, boolean defaultValue) {
        Map<String, String> cacheMap = getRedisService().getCacheMap(getKey(group));
        String v = MapUtil.getStr(cacheMap, key);
        if (StrUtil.isBlank(v)) {
            return defaultValue;
        }
        return BooleanUtil.toBoolean(v);
    }

    /**
     * 获取boolean类型值
     *
     * @param group
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getValueBoolean(String module, String group, String key, boolean defaultValue) {
        Map<String, String> cacheMap = getRedisService().getCacheMap(getKey(module, group));
        String v = MapUtil.getStr(cacheMap, key);
        if (StrUtil.isBlank(v)) {
            return defaultValue;
        }
        return BooleanUtil.toBoolean(v);
    }

    /**
     * 获取boolean类型值，默认否
     *
     * @param group
     * @param key
     * @return
     */
    public static boolean getValueBoolean(String module, String group, String key) {
        return getValueBoolean(module, group, key, false);
    }

    public static Integer getValueInteger(String module, String group, String key, Integer defaultValue) {
        Map<String, String> cacheMap = getRedisService().getCacheMap(getKey(module, group));
        String v = MapUtil.getStr(cacheMap, key);
        if (StrUtil.isBlank(v)) {
            return defaultValue;
        }
        return Integer.valueOf(v);
    }

    public static Long getValueLong(String module, String group, String key, Long defaultValue) {
        Map<String, String> cacheMap = getRedisService().getCacheMap(getKey(module, group));
        String v = MapUtil.getStr(cacheMap, key);
        if (StrUtil.isBlank(v)) {
            return defaultValue;
        }
        return Long.valueOf(v);
    }

    /**
     * 获取下拉选择值 label为key value为value
     *
     * @param group
     * @return
     */
    public static List<SelectVO> getSelectVOList(String module, String group) {
        List<SelectVO> list = new ArrayList<>();
        Map<String, String> map = getValue(module, group);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(new SelectVO().setLabel(entry.getKey()).setValue(entry.getValue()));
            }
        }
        return list;
    }

}
