package cn.ussu.modules.ecps.skill.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.skill.entity.EbSeckillQua;
import cn.ussu.modules.ecps.skill.mapper.EbSeckillQuaMapper;
import cn.ussu.modules.ecps.skill.service.IEbSeckillQuaService;
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
public class EbSeckillQuaServiceImpl extends ServiceImpl<EbSeckillQuaMapper, EbSeckillQua> implements IEbSeckillQuaService {

    @Override
    public EbSeckillQua detail(Integer id) {
        Assert.notNull(id);
        EbSeckillQua obj = new EbSeckillQua().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSeckillQua p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbSeckillQua p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSeckillQua> qw = new LambdaQueryWrapper<>();
        qw.in(EbSeckillQua::getSeckillQuaId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
