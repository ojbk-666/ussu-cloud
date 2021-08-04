package cn.ussu.modules.ecps.portal.service;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.item.entity.EbBrand;
import cn.ussu.modules.ecps.item.entity.EbCat;
import cn.ussu.modules.ecps.item.entity.EbFeature;
import cn.ussu.modules.ecps.portal.feign.item.RemoteBrandService;
import cn.ussu.modules.ecps.portal.feign.item.RemoteFeatureService;
import cn.ussu.modules.ecps.portal.feign.item.RemoteItemService;
import cn.ussu.modules.ecps.portal.feign.item.RemoveFeatureGroupService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RemoteItemService remoteItemService;
    @Autowired
    private RemoveFeatureGroupService removeFeatureGroupService;
    @Autowired
    private RemoteFeatureService remoteFeatureService;
    @Autowired
    private RemoteBrandService remoteBrandService;

    @Autowired
    private RedissonClient redisson;

    private RLock lock;

    @Autowired
    public void setLock() {
        lock = redisson.getLock("reentrant_lock");
    }

    @Cacheable(value = ConstantsEcps.CACHE_VALUE_CAT, key = "'catTree'")
    public List<EbCat> getCatTree() {
        List<EbCat> cats = null;
        if (lock.tryLock()) {
            try {
                System.out.println("我拿到锁了,从DB获取数据库后写入缓存");
                // 从数据库查询数据
                JsonResult jsonResult = remoteItemService.listCatTree();
                cats = jsonResult.getData();
            } finally {
                lock.unlock();// 释放锁
            }
        } else {
            //如果没有获得到分布式锁，递归
            cats = getCatTree();
        }
        return cats;
    }

    /**
     * 获取品牌
     */
    @Cacheable(value = ConstantsEcps.CACHE_VALUE_BRAND, key = "'getBrandListByCatId_' + args[0]")
    public List<EbBrand> getBrandListByCatId(Integer catId) {
        List<EbBrand> list = null;
        if (lock.tryLock()) {
            try {
                // 从数据库查询数据
                JsonResult jsonResult = remoteBrandService.getBrandlistByCatId(catId);
                list = jsonResult.getData();
            } finally {
                lock.unlock();
            }
        } else {
            //如果没有获得到分布式锁，递归
            list = getBrandListByCatId(catId);
        }
        return list;
    }

    /**
     * 获取属性组
     */
    // @Cacheable(value = ConstantsEcps.CACHE_VALUE_FEATURE, key = "'getFeatureListByCatId_' + #root.args[0]")
    @Cacheable(value = ConstantsEcps.CACHE_VALUE_FEATURE, key = "'getFeatureListByCatId_' + #root.args[0]")
    public List<EbFeature> getFeatureListByCatId(Integer catId) {
        List<EbFeature> list = null;
        if (lock.tryLock()) {
            try {
                System.out.println("我拿到锁了,从DB获取数据库后写入缓存");
                // 从数据库查询数据
                JsonResult jsonResult = remoteFeatureService.getFeaturelistByCatId(catId, 0, 1);
                list = jsonResult.getData();
            } finally {
                lock.unlock();// 释放锁
            }
        } else {
            //如果没有获得到分布式锁，递归
            list = getFeatureListByCatId(catId);
        }
        return list;
    }
}
