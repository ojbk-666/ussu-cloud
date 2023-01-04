package cc.ussu.modules.sheep.service;

import cc.ussu.modules.sheep.entity.SheepEnv;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 环境变量 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:40:01
 */
public interface ISheepEnvService extends IService<SheepEnv> {

    /**
     * 通过名称获取 仅返回正常状态的
     */
    List<SheepEnv> getList(String name);
    /**
     * 通过名称获取值 仅返回正常状态的
     */
    List<String> getValueList(String name);

    /**
     * 通过名称获取 仅返回正常状态的
     */
    SheepEnv get(String name);

    /**
     * 通过名称获取 仅返回正常状态的
     */
    List<SheepEnv> queryList(String query);

    /**
     * 通过名称获取值 仅返回正常状态的
     */
    String getValue(String name);

    /**
     * 禁用环境变量
     */
    void disable(String id);

    /**
     * 启用环境变量
     */
    void enable(String id);
}
