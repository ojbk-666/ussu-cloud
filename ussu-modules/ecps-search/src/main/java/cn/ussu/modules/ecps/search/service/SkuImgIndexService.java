package cn.ussu.modules.ecps.search.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.ussu.modules.ecps.search.model.SkuImgIndex;
import cn.ussu.modules.ecps.search.model.SkuIndex;
import cn.ussu.modules.ecps.search.repository.SkuImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuImgIndexService {

    @Autowired
    private SkuImgRepository skuImgRepository;

    public boolean check(SkuImgIndex skuImgIndex) {
        Assert.notNull(skuImgIndex.getImgId());
        Assert.notNull(skuImgIndex.getSkuId());
        return true;
    }

    public boolean add(SkuImgIndex skuImgIndex) {
        if (check(skuImgIndex) && skuImgRepository.existsById(skuImgIndex.getImgId())) {
            skuImgRepository.save(skuImgIndex);
        }
        return true;
    }

    public boolean addBySku(SkuIndex skuIndex) {
        List<SkuImgIndex> skuImgList = skuIndex.getSkuImgList();
        if (CollUtil.isNotEmpty(skuImgList)) {
            for (SkuImgIndex skuImgIndex : skuImgList) {
                check(skuImgIndex);
            }
            skuImgRepository.saveAll(skuImgList);
        }
        return true;
    }

}
