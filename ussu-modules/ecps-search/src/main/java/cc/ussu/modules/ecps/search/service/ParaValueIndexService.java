package cc.ussu.modules.ecps.search.service;

import cc.ussu.modules.ecps.search.model.ItemIndex;
import cc.ussu.modules.ecps.search.model.ParaValueIndex;
import cc.ussu.modules.ecps.search.repository.ParaValueRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParaValueIndexService {

    @Autowired
    private ParaValueRepository paraValueRepository;

    public void add(ParaValueIndex paraValueIndex) {
        Assert.notNull(paraValueIndex.getParaId());
        Assert.notNull(paraValueIndex.getItemId());
        if (!paraValueRepository.existsById(paraValueIndex.getParaId())) {
            paraValueRepository.save(paraValueIndex);
        }
    }

    public void addByItemIndex(ItemIndex itemIndex) {
        List<ParaValueIndex> paraList = itemIndex.getParaList();
        if (CollUtil.isNotEmpty(paraList)) {
            paraValueRepository.saveAll(paraList);
        }
    }

}
