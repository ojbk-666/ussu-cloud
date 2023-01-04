package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysRegion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 省市区区域表 服务类
 * </p>
 *
 * @author liming
 * @since 2022-01-23 17:45:37
 */
public interface ISysRegionService extends IService<SysRegion> {

    /**
     * 根据id获取子集
     *
     * @param id
     * @return
     */
    List<SysRegion> getChildren(String id);

    /**
     * 新增
     */
    boolean add(SysRegion p);

    /**
     * 修改
     */
    boolean edit(SysRegion p);
}
