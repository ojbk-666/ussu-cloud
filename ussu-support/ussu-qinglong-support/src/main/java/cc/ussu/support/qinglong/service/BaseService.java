package cc.ussu.support.qinglong.service;

import cc.ussu.support.qinglong.model.QingLongConfig;

public interface BaseService {

    /**
     * 获取配置
     */
    QingLongConfig getQingLongConfig();


    /**
     * 获取缓存key
     */
    String getCacheKey();

    /**
     * 登录青龙 返回值 token
     */
    String getToken();

}
