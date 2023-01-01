package cc.ussu.modules.ecps.search.service;

import cc.ussu.modules.ecps.search.model.SkuIndex;
import cc.ussu.modules.ecps.search.model.SpecValueIndex;
import cc.ussu.modules.ecps.search.repository.SpecValueRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecValueIndexService {

    @Autowired
    private SpecValueRepository specValueRepository;

    public boolean check(SpecValueIndex specValueIndex) {
        Assert.notNull(specValueIndex.getSpecId());
        Assert.notNull(specValueIndex.getSkuId());
        Assert.notNull(specValueIndex.getFeatureId());
        return true;
    }

    public boolean add(SpecValueIndex specValueIndex) {
        if (check(specValueIndex)) {
            if (!specValueRepository.existsById(specValueIndex.getSpecId())) {
                specValueRepository.save(specValueIndex);
            }
        }
        return true;
    }

    public boolean addBySku(SkuIndex skuIndex) {
        List<SpecValueIndex> specList = skuIndex.getSpecList();
        if (CollUtil.isNotEmpty(specList)) {
            for (SpecValueIndex specValueIndex : specList) {
                check(specValueIndex);
            }
            specValueRepository.saveAll(specList);
        }
        return true;
    }

}
