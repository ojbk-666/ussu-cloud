package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.item.entity.EbItemClob;
import cc.ussu.modules.ecps.item.mapper.EbItemClobMapper;
import cc.ussu.modules.ecps.item.service.IEbItemClobService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * @since 2021-07-17
 */
@Service
public class EbItemClobServiceImpl extends ServiceImpl<EbItemClobMapper, EbItemClob> implements IEbItemClobService {

    @Override
    public EbItemClob detail(Integer id) {
        Assert.notNull(id);
        EbItemClob obj = new EbItemClob().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbItemClob p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbItemClob p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbItemClob> qw = new LambdaQueryWrapper<>();
        qw.in(EbItemClob::getItemId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    @Override
    public EbItemClob getByItemId(Integer itemId) {
        Assert.notNull(itemId);
        LambdaQueryWrapper<EbItemClob> qw = Wrappers.lambdaQuery(EbItemClob.class)
                .eq(EbItemClob::getItemId, itemId);
        return super.getOne(qw, true);
    }
}
