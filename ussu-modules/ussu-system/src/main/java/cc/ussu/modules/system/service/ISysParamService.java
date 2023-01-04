package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
public interface ISysParamService extends IService<SysParam> {

    /**
     * 检查是否已存在name
     * @param p
     * @return
     */
    boolean checkNameExist(SysParam p);

    /**
     * 检查是否已存在key
     * @param p
     * @return
     */
    boolean checkKeyExist(SysParam p);

    /**
     * 通过key获取
     */
    SysParam getByKey(String key);

    boolean getCaptchaEnable();

    /**
     * 通过key获取值
     *
     * @param key
     * @return
     */
    String getValueByKey(String key);

    /**
     * 通过key获取值
     *
     * @param key 要获取的key
     * @param defaultValue 默认值
     * @return
     */
    String getValueByKey(String key, String defaultValue);

    boolean getValueByKeyBoolean(String key);

}
