package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.item.entity.EbSkuImg;
import cn.ussu.modules.ecps.item.mapper.EbSkuImgMapper;
import cn.ussu.modules.ecps.item.service.IEbSkuImgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Service
public class EbSkuImgServiceImpl extends ServiceImpl<EbSkuImgMapper, EbSkuImg> implements IEbSkuImgService {

    @Override
    public EbSkuImg detail(Integer id) {
        Assert.notNull(id);
        EbSkuImg obj = new EbSkuImg().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSkuImg p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbSkuImg p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSkuImg> qw = new LambdaQueryWrapper<>();
        qw.in(EbSkuImg::getImgId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    @Override
    public List<EbSkuImg> getBySkuId(Integer skuId) {
        Assert.notNull(skuId);
        LambdaQueryWrapper<EbSkuImg> qw = Wrappers.lambdaQuery(EbSkuImg.class)
                .orderByAsc(EbSkuImg::getImgId)
                .eq(EbSkuImg::getSkuId, skuId);
        return super.list(qw);
    }

}
