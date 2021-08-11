package cn.ussu.modules.ecps.skill.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.skill.entity.EbSeckillSku;
import cn.ussu.modules.ecps.skill.mapper.EbSeckillSkuMapper;
import cn.ussu.modules.ecps.skill.service.IEbSeckillSkuService;
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
 * @since 2021-08-11
 */
@Service
public class EbSeckillSkuServiceImpl extends ServiceImpl<EbSeckillSkuMapper, EbSeckillSku> implements IEbSeckillSkuService {

    @Override
    public EbSeckillSku detail(Integer id) {
        Assert.notNull(id);
        EbSeckillSku obj = new EbSeckillSku().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSeckillSku p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbSeckillSku p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSeckillSku> qw = new LambdaQueryWrapper<>();
        qw.in(EbSeckillSku::getSeckillId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
