package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbCat;
import cc.ussu.modules.ecps.item.mapper.EbCatMapper;
import cc.ussu.modules.ecps.item.service.IEbCatService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类目。	1. 电商一期只支持两种商品，即手机和号卡。促销活动作为一种规则配置到上述两种商品上。二期会增加 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-13
 */
@Service
public class EbCatServiceImpl extends ServiceImpl<EbCatMapper, EbCat> implements IEbCatService {

    private static final String NAME_NOT_BLANK = "名称不能为空";

    @Override
    public List<EbCat> listTree() {
        List<EbCat> list = baseMapper.selectList(null);
        List<EbCat> result = list.stream()
                .filter(item -> item.getParentId().equals(0))
                .map(item -> {
                    item.setChildren(recursion(item, list));
                    return item;
                }).collect(Collectors.toList());
        return result;
    }

    /**
     * 递归子节点
     */
    public List<EbCat> recursion(EbCat pcat, List<EbCat> list) {
        List<EbCat> child = new LinkedList<>();
        for (EbCat item : list) {
            if (item.getParentId().equals(pcat.getCatId())) {
                child.add(item);
            }
        }
        if (child.size() != 0) {
            for (EbCat item : child) {
                item.setChildren(recursion(item, list));
            }
        }
        return child;
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_CAT, key = "'catTree'", allEntries = true)
    @Transactional
    @Override
    public void add(EbCat p) {
        Assert.notBlank(p.getCatName(), NAME_NOT_BLANK);
        String parentIdPath = null;
        if (p.getParentId() == null) {
            p.setParentId(0).setCatLevel(1);
        } else {
            Integer parentId = p.getParentId();
            EbCat ebCat = new EbCat().setCatId(parentId).selectById();
            String idPath = ebCat.getIdPath();
            parentIdPath = idPath;
            p.setCatLevel(ebCat.getCatLevel() + 1);
        }
        p.insert();
        if (parentIdPath == null) {
            p.setIdPath(p.getCatId() + "");
        } else {
            p.setIdPath(parentIdPath + StrPool.COMMA + p.getCatId());
        }
        p.updateById();
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_CAT, key = "'catTree'", allEntries = true)
    @Transactional
    @Override
    public void edit(EbCat p) {
        Assert.notNull(p.getCatId());
        Assert.notBlank(p.getCatName(), NAME_NOT_BLANK);
        if (p.getParentId() == null) {
            p.setParentId(0);
            p.setIdPath(p.getCatId() + "");
        } else {
            EbCat parentCat = new EbCat().setCatId(p.getParentId()).selectById();
            p.setIdPath(parentCat.getIdPath() + StrPool.COMMA + p.getCatId())
                    .setCatLevel(parentCat.getCatLevel() + 1);
        }
        p.updateById();
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_CAT, key = "'catTree'", allEntries = true)
    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        List<Integer> idList = Arrays.stream(ints).boxed().collect(Collectors.toList());
        super.removeByIds(idList);
    }
}
