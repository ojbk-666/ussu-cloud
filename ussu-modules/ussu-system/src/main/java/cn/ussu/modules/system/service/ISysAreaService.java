package cn.ussu.modules.system.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysArea;
import cn.ussu.modules.system.model.param.SysAreaParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 省市区区域表 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
public interface ISysAreaService extends IService<SysArea> {

    /**
     * 分页查询
     */
    ReturnPageInfo<SysArea> findPage(SysAreaParam param);

    /**
     * 获取子级
     * @return
     */
    // List<Map<String, Object>> findCascaderByParendId(Integer pid);
    List<? extends Object> findCascaderByParendId(Integer pid);

    /**
     * 新增
     */
    void addOne(SysArea obj);

    /**
     * 修改
     */
    void updateOne(SysArea obj);

    /**
     * 删除
     */
    void deleteOne(Integer id);

    /**
     * 删除缓存中的值指定父节点的所有子节点
     */
    boolean deleteAreaListInRedisByParentId(Integer parentId);

}
