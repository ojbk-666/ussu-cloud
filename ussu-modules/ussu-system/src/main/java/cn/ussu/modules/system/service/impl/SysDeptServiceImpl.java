package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysDept;
import cn.ussu.modules.system.mapper.SysDeptMapper;
import cn.ussu.modules.system.model.param.SysDeptParam;
import cn.ussu.modules.system.service.ISysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-16
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper mapper;

    /**
     * 分页查询
     */
    @Override
    public ReturnPageInfo<SysDept> findPage(SysDeptParam param) {
        LambdaQueryWrapper<SysDept> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime);
        if (param != null) {
            qw.like(StrUtil.isNotBlank(param.getName()), SysDept::getName, param.getName())
                    .like(StrUtil.isNotBlank(param.getParentId()), SysDept::getPath, StrUtil.SLASH + param.getParentId());
        }
        IPage iPage = this.mapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    @Override
    public List<Map> findSubDeptByParentId(String pid) {
        List<Map> list = this.mapper.findSubDeptByParentId(pid);
        return list;
    }

    @Override
    public List<SysDept> findAllSubDeptListByParentId(String deptId, boolean containsSelf) {
        QueryWrapper<SysDept> qw = new QueryWrapper<>();
        qw.like("path", "/" + deptId);
        List<SysDept> deptList = this.mapper.selectList(qw);
        // 不包含则移除
        if (!containsSelf) {
            for (SysDept sysDept : deptList) {
                if (sysDept.getId().equals(deptId)) deptList.remove(sysDept);
                break;
            }
        }
        return deptList;
    }

    @Override
    @Transactional
    public SysDept addOne(SysDept obj) {
        obj.setId(IdWorker.getIdStr());
        // 获取父级路径
        String parentId = obj.getParentId();
        if (StrUtil.isBlank(parentId)) {
            obj.setParentId("0");
        }
        if ("0".equals(parentId)) {
            // 顶级
            obj.setPath("/" + obj.getId());
            obj.setLevel(1);
        } else {
            SysDept parentDept = new SysDept().setId(parentId).selectById();
            obj.setPath(parentDept.getPath() + "/" + obj.getId()).setLevel(parentDept.getPath().split("/").length);
        }
        boolean b = obj.insert();
        return obj;
    }

    @Override
    @Transactional
    public void updateOne(SysDept obj) {
        // 获取旧数据
        obj.setParentId(null);  //不允许修改上级
        this.mapper.updateById(obj);
    }

    @Override
    @Transactional
    public int deleteMany(List<String> ids) {
        int deleted = 0;
        for (String id : ids) {
            if (StrUtil.isBlank(id)) continue;
            // 删除自己及子部门
            QueryWrapper<SysDept> qw = new QueryWrapper<>();
            qw.eq(StrConstants.id, id).or().like("path", "/" + id);
            deleted += this.mapper.delete(qw);
            // todo 用户等其他信息
        }
        // SecurityUtils.refreshUserDeptDataScope();
        return deleted;
    }

    /**
     * 获取权限下得所有部门
     */
    @Override
    public List<SysDept> findAll2DeptList() {
        return this.mapper.listAll2();
    }

}
