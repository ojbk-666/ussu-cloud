package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.modules.sheep.common.ISheepTask;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cn.hutool.extra.spring.SpringUtil;

import java.util.List;

/**
 * 京东任务
 */
public interface JdSheepTask extends ISheepTask<String> {

    /**
     * 从环境变量获取京东cookie
     */
    @Override
    default List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    /**
     * 农场服务
     */
    default JdFruitService getFruitService() {
        return SpringUtil.getBean(JdFruitService.class);
    }

}
