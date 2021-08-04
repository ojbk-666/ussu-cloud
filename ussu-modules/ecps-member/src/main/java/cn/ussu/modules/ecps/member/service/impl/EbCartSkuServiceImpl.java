package cn.ussu.modules.ecps.member.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.member.mapper.EbCartSkuMapper;
import cn.ussu.modules.ecps.member.service.IEbCartSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-30
 */
@Service
public class EbCartSkuServiceImpl extends ServiceImpl<EbCartSkuMapper, EbCartSku> implements IEbCartSkuService {

    @Override
    public EbCartSku detail(Integer id) {
        Assert.notNull(id);
        EbCartSku obj = new EbCartSku().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbCartSku p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbCartSku p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbCartSku> qw = new LambdaQueryWrapper<>();
        qw.in(EbCartSku::getCartSkuId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
