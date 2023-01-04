package cc.ussu.modules.system.service.impl;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.system.entity.SysRegion;
import cc.ussu.modules.system.mapper.SysRegionMapper;
import cc.ussu.modules.system.service.ISysRegionService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 省市区区域表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2022-01-23 17:45:37
 */
@Master
@Service
public class SysRegionServiceImpl extends ServiceImpl<SysRegionMapper, SysRegion> implements ISysRegionService {

    public static final String REGION_CACHE_KEY = "ussu:region";

    @Autowired
    private RedisService redisService;

    /**
     * 初始化redis
     */
    @PostConstruct
    private void initCache() {
        redisService.deleteObject(REGION_CACHE_KEY);
        List<SysRegion> list = super.list();
        list.stream().forEach(r -> {
            r.setLabel(r.getName());
            r.setValue(r.getId());
            r.setLeaf(!list.stream().anyMatch(a -> r.getId().equals(a.getParentId())));
            r.setHasChildren(!r.isLeaf());
        });
        List<List<SysRegion>> group = CollUtil.groupByField(list, "parentId");
        for (List<SysRegion> item : group) {
            SysRegion first = CollUtil.getFirst(item);
            redisService.setCacheMapValue(REGION_CACHE_KEY, first.getParentId(), item);
        }
    }

    @Override
    public List<SysRegion> getChildren(String id) {
        Assert.notBlank(id, "参数id不能为空");
        List<SysRegion> list = redisService.getCacheMapValue(REGION_CACHE_KEY, id);
        if (list == null) {
            return new ArrayList<SysRegion>();
        }
        return list;
    }

    /**
     * 新增
     *
     * @param p
     */
    @Override
    public boolean add(SysRegion p) {
        return false;
    }

    /**
     * 修改
     *
     * @param p
     */
    @Override
    public boolean edit(SysRegion p) {
        return false;
    }
}
