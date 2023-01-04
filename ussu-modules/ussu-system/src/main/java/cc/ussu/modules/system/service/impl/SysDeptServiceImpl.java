package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysDept;
import cc.ussu.modules.system.mapper.SysDeptMapper;
import cc.ussu.modules.system.service.ISysDeptService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 部门 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    /**
     * 新增
     */
    @Override
    public synchronized void add(SysDept p) {
        String path = "";
        if (StrUtil.isBlank(p.getParentId())) {
            p.setParentId(ROOT_PARENT_ID).setLevel(1);
        } else {
            SysDept parent = super.getById(p.getParentId());
            Assert.notNull(parent, "上级部门不存在");
            path = parent.getPath();
            p.setLevel(parent.getLevel() + 1);
        }
        p.setId(baseMapper.getNextId()).setPath(path + "/" + p.getId());
        super.save(p);
    }

    /**
     * 编辑
     *
     * @param p
     */
    @Override
    public void edit(SysDept p) {
        Assert.notBlank(p.getId(), "部门id不能为空");
        Assert.notNull(super.getById(p.getId()), "数据不存在");
        String path = "";
        if (StrUtil.isBlank(p.getParentId())) {
            p.setParentId(ROOT_PARENT_ID).setLevel(1);
        } else {
            SysDept parent = super.getById(p.getParentId());
            Assert.notNull(parent, "上级部门不存在");
            path = parent.getPath();
            p.setLevel(parent.getLevel() + 1);
        }
        p.setPath(path + "/" + p.getId());
        super.updateById(p);
    }

    @Override
    public List<SysDept> renderToTree(List<SysDept> list) {
        List<SysDept> returnList = new ArrayList<>();
        Iterator<SysDept> iterator = list.iterator();
        while (iterator.hasNext()) {
            SysDept t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(ZERO)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return CollUtil.isNotEmpty(getChildList(list, t));
    }
}
