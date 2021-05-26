package cn.ussu.modules.system.core.util;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.RedisConstants;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.service.ISysDictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * 缓存中的格式：sys_sex: [{key:123,value:"名称"},...]
 *
 * @author liming
 * @date 2019-12-19 16:27
 */
public class DictUtil {

    private static RedisService redisUtil = SpringContextHolder.getBean(RedisService.class);

    /**
     * 初始化，缓存值
     */
    public synchronized static void init() {
        // 清理旧数据，缓存新数据
        ISysDictService service = SpringContextHolder.getBean(ISysDictService.class);
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        List<SysDict> list = service.list(qw);
        list.forEach(item -> redisUtil.deleteObject(RedisConstants.SYS_DICT_ + item.getTypeCode()));
        for (SysDict sysDict : list) {
            // redisUtil.seth(RedisConstants.SYS_DICT_ + sysDict.getTypeCode(), sysDict.getDictValue(), sysDict.getDictName());
        }
    }

    /**
     * 获取类型下的所有值
     *
     * @return key：字典值 value：字典名称
     */
    public static List<Map<String, String>> getDataByType(String dictTypeCode) {
        return getDataByType(dictTypeCode, StrConstants.value, StrConstants.name);
    }

    /**
     * 获取类型下的所有值，自定义 key value
     *
     * @return dictName：字典值 dictValue：字典名称
     */
    public static List<Map<String, String>> getDataByType(String dictTypeCode, String dictValueAlias, String dictNameAlias) {
        Map<String, Object> map = redisUtil.getCacheMap(RedisConstants.SYS_DICT_ + dictTypeCode);
        String mapKey = StrConstants.value;
        String mapValue = StrConstants.name;
        if (StrUtil.isNotBlank(dictValueAlias)) {
            mapKey = dictValueAlias;
        }
        if (StrUtil.isNotBlank(dictNameAlias)) {
            mapValue = dictNameAlias;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            Map<String, String> result = new HashMap<>();
            String key = ((String) e.getKey());
            String value = ((String) e.getValue());
            result.put(mapKey, key);
            result.put(mapValue, value);
            list.add(result);
        }
        return list;
    }

    /**
     * 根据类型和值获取名称
     *
     * @param dictTypeCode 类型
     * @param value    值
     * @return 名称
     */
    public static String getNameByValue(String dictTypeCode, String value) {
        if (value == null) {
            return null;
        }
        return (String) redisUtil.getCacheMapValue(RedisConstants.SYS_DICT_ + dictTypeCode, value);
    }

    /*----------字典类型----------*/

    /**
     * 将指定字典类型的数据加入缓存
     *
     * @param dictTypeCode 类型名称
     * @author liming
     * @date 2019-12-27 10:33
     */
    public static void addDictType(String dictTypeCode) {
        ISysDictService dictService = SpringContextHolder.getBean(ISysDictService.class);
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.eq("type_code", dictTypeCode).eq("status", 1);
        List<SysDict> sysDictList = dictService.list(qw);
        if (sysDictList == null || sysDictList.size() == 0) {
            return;
        }
        for (SysDict sysDict : sysDictList) {
            redisUtil.setCacheMapValue(RedisConstants.SYS_DICT_ + dictTypeCode, sysDict.getDictValue(), sysDict.getDictLabel());
        }
    }

    /**
     * 刷新指定字典类型，更新字典数据
     *
     * @param oldDictTypeCode 旧的缓存key
     * @param newDictTypeCode 新缓存key
     * @author liming
     * @date 2019-12-27 10:33
     */
    public synchronized static void refreshDictType(String oldDictTypeCode, String newDictTypeCode) {
        // 删除
        deleteDictType(oldDictTypeCode);
        // 添加
        addDictType(newDictTypeCode);
    }

    /**
     * 删除字典类型，同时删除字段数据
     *
     * @param dictTypeCode 字典类型
     * @author liming
     * @date 2019-12-27 10:35
     */
    public synchronized static void deleteDictType(String dictTypeCode) {
        redisUtil.deleteObject(RedisConstants.SYS_DICT_ + dictTypeCode);
    }
    /*----------字典类型----------*/

    /*----------字典数据----------*/

    /**
     * 添加字典数据
     */
    public synchronized static void addDict(String dictTypeCode, String dictValue, String dictName) {
        redisUtil.setCacheMapValue(RedisConstants.SYS_DICT_ + dictTypeCode, dictValue, dictName);
    }

    /**
     * 添加字典数据
     * @see #addDict(String, String, String)
     */
    public static void addDict(SysDict sysDict) {
        addDict(sysDict.getTypeCode(), sysDict.getDictValue(), sysDict.getDictLabel());
    }

    /**
     * 更新字典数据
     * @see #updateDict(String, String, String, String)
     */
    public synchronized static void updateDict(SysDict sysDict, String oldDictValue) {
        updateDict(sysDict.getTypeCode(), oldDictValue, sysDict.getDictValue(), sysDict.getDictLabel());
    }

    /**
     * 更新字典数据
     */
    public synchronized static void updateDict(String dictTypeCode, String oldDictValue, String dictValue, String dictName) {
        if (!oldDictValue.equals(dictValue)) {
            // delete
            deleteDict(dictTypeCode, oldDictValue);
        }
        // add
        addDict(dictTypeCode, dictValue, dictName);
    }

    /**
     * 删除字典数据
     */
    public synchronized static void deleteDict(String dictTypeCode, String dictValue) {
        // redisUtil.hdel(RedisConstants.SYS_DICT_ + dictTypeCode, dictValue);
    }

    /**
     * 删除字典数据
     * @see #deleteDict(String, String)
     */
    public synchronized static void deleteDict(SysDict sysDict) {
        deleteDict(sysDict.getTypeCode(), sysDict.getDictValue());
    }

    /*----------字典数据----------*/


}
