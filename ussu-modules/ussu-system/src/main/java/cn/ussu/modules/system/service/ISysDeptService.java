package cn.ussu.modules.system.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysDept;
import cn.ussu.modules.system.model.param.SysDeptParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-16
 */
public interface ISysDeptService extends IService<SysDept> {

    /**
     * 分页查询
     */
    ReturnPageInfo<SysDept> findPage(SysDeptParam param);

    /**
     * 获取指定部门的直接子部门
     *
     * @author liming
     * @date 2020-07-28 14:37
    */
    List<Map> findSubDeptByParentId(String pid);

    /**
     * 获取所有子部门
     *
     * @param deptId 上级id
     * @param containsSelf 是否包含自己
     */
    List<SysDept> findAllSubDeptListByParentId(String deptId, boolean containsSelf);

    /**
     * 新增
     */
    SysDept addOne(SysDept obj);

    /**
     * 修改
     */
    void updateOne(SysDept obj);

    /**
     * 删除
     */
    int deleteMany(List<String> ids);

    /**
     * 获取权限范围得所有部门
     */
    List<SysDept> findAll2DeptList();

}
