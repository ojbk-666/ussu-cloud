package cc.ussu.modules.sheep.common;

import cc.ussu.common.core.condition.DevEnvCondition;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.modules.sheep.properties.SheepProperties;
import cc.ussu.modules.sheep.service.ISheepEnvService;
import cc.ussu.modules.sheep.service.ISheepTaskService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.List;

/**
 * 任务
 */
// public interface ISheepTask<P extends SheepParam> {
public interface ISheepTask<P> {

    default ISheepEnvService getEnvService() {
        return SpringUtil.getBean(ISheepEnvService.class);
    }
    default ISheepTaskService getTaskService() {
        return SpringUtil.getBean(ISheepTaskService.class);
    }
    default RedisService getRedisService() {
        return SpringUtil.getBean(RedisService.class);
    }
    default String getRedisKey(String key) {
        return "sheep:" + key;
    }

    default boolean debugEnable() {
        return ArrayUtil.contains(SpringUtil.getBean(Environment.class).getActiveProfiles(), DevEnvCondition.ENV_NAME)
                || ConfigUtil.getValueBoolean("sheep","task","debugLog", false);
    }

    /**
     * 获取任务名称
     */
    String getTaskName();

    /**
     * 任务分组
     */
    default String getTaskGroupName() {
        return "默认分组";
    }

    /**
     * 获取参数
     */
    List<P> getParamList();

    /**
     * 获取日志存放的相对路径
     * @param fileName 文件名
     *
     * @return 相对路径地址 例 /a/b/c/2022-10-16.log
     */
    String getLogRelativePath(String fileName);

    /**
     * 记录日志
     */
    default void logToFile(String logFileName, String logContent) {
        String basePath = SpringUtil.getBean(SheepProperties.class).getLogBasePath();
        String relativePath = StrUtil.addPrefixIfNot(StrUtil.addSuffixIfNot(getLogRelativePath(logFileName), StrPool.SLASH), StrPool.SLASH);
        String absolutePath = StrUtil.removeSuffix(basePath, StrUtil.SLASH) + relativePath;
        FileUtil.writeUtf8String(logContent, new File(absolutePath));
    }

    /**
     * 任务执行之前
     */
    default void beforeDoTask() {}

    /**
     * 执行任务
     */
    void doTask(List<P> params);

    /**
     * 执行任务
     */
    // void doTask(P param);

    /**
     * 任务执行之后
     */
    default void afterDoTask() {}

}
