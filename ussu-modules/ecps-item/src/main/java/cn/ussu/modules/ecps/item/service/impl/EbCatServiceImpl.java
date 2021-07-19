package cn.ussu.modules.ecps.item.service.impl;

import cn.ussu.modules.ecps.item.entity.EbCat;
import cn.ussu.modules.ecps.item.mapper.EbCatMapper;
import cn.ussu.modules.ecps.item.service.IEbCatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
