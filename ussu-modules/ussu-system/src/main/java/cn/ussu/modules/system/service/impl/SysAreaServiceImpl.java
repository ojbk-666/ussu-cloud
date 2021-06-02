package cn.ussu.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.ussu.common.core.constants.RedisConstants;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysArea;
import cn.ussu.modules.system.mapper.SysAreaMapper;
import cn.ussu.modules.system.model.param.SysAreaParam;
import cn.ussu.modules.system.service.ISysAreaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 省市区区域表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-17
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Autowired
    private SysAreaMapper mapper;
    @Autowired
    private RedisService redisUtil;

    /**
     * 分页查询
     */
    @Override
    public ReturnPageInfo<SysArea> findPage(SysAreaParam param) {
        LambdaQueryWrapper<SysArea> qw = new LambdaQueryWrapper<>();
        if (param != null) {
            qw.like(param.getParentId() != null, SysArea::getPath, param.getParentId());
        }
        IPage iPage = this.mapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    @Override
    public List<? extends Object> findCascaderByParendId(Integer pid) {
        if (pid == null) throw new RequestEmptyException();
        String keyInRedis = getAreaRedisKey(pid);
        // 从缓存获取数据
        List<Object> objects = (List<Object>) redisUtil.getCacheObject(keyInRedis);
        if (CollectionUtil.isNotEmpty(objects)) {
            return objects;
        }
        List<Map<String, Object>> list = this.mapper.findCascaderByParendId(pid);
        redisUtil.setCacheList(keyInRedis, list);
        return list;
    }

    @Override
    @Transactional
    public void addOne(SysArea obj) {
        this.mapper.insert(obj);
        deleteAreaListInRedisByParentId(obj.getParentId());
    }

    @Override
    @Transactional
    public void updateOne(SysArea obj) {
        this.mapper.updateById(obj);
        deleteAreaListInRedisByParentId(obj.getParentId());
    }

    @Override
    @Transactional
    public void deleteOne(Integer id) {
        this.mapper.deleteById(id);
        deleteAreaListInRedisByParentId(id);
    }

    /**
     * 获取缓存的key
     */
    private String getAreaRedisKey(Integer pid) {
        return RedisConstants.SYS_AREA_ + pid;
    }

    @Override
    public boolean deleteAreaListInRedisByParentId(Integer parentId) {
        if (parentId == null) return true;
        redisUtil.deleteObject(getAreaRedisKey(parentId));
        return true;
    }
}
