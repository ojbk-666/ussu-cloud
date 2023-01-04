package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
public interface ISysDictDataService extends IService<SysDictData> {

    /**
     * 是否存在类型，应该存在，类型必须存在
     */
    boolean checkTypeExist(SysDictData p);

    /**
     * 是否存在值，应该否 值不允许重复
     */
    boolean checkValueExist(SysDictData p);

    /**
     * 检查是否存在编码
     */
    boolean checkCodeExist(SysDictData p);

    /**
     * 根据类型获取状态为正常的数据列表
     */
    List<SysDictData> getListByDictTypeNotDisabled(String type);

    /**
     * 刷新缓存
     */
    void refreshCache();
}
