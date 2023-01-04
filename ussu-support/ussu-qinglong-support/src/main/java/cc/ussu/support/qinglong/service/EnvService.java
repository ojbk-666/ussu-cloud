package cc.ussu.support.qinglong.service;

import cc.ussu.support.qinglong.dto.EnvListDTO;

import java.util.List;

/**
 * 环境变量服务类
 */
public interface EnvService extends BaseService {

    /**
     * 获取环境变量列表
     */
    EnvListDTO getEnvList(String searchValue);

    /**
     * 获取环境变量详情
     */
    // EnvListDTO.EnvDTO getEnvDetail(String id);

    /**
     * 添加环境变量
     */
    void saveEnv(EnvListDTO.EnvDTO env);

    /**
     * 编辑环境变量
     */
    void updateEnv(EnvListDTO.EnvDTO env);

    /**
     * 删除环境变量
     */
    void deleteEnv(List<Integer> ids);

    /**
     * 禁用环境变量
     */
    void disableEnv(List<Integer> ids);

    /**
     * 启用环境变量
     */
    void enableEnv(List<Integer> ids);

}
