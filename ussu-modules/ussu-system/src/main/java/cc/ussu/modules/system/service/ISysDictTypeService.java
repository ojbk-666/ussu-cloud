package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典类型表 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    boolean checkTypeExist(SysDictType p);

    /**
     * 根据类型代码获取
     */
    SysDictType getOneByType(String type);
}
