package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.item.entity.EbParaValue;
import cc.ussu.modules.ecps.item.mapper.EbParaValueMapper;
import cc.ussu.modules.ecps.item.service.IEbParaValueService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
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
public class EbParaValueServiceImpl extends ServiceImpl<EbParaValueMapper, EbParaValue> implements IEbParaValueService {

    @Override
    public EbParaValue detail(Integer id) {
        Assert.notNull(id);
        EbParaValue obj = new EbParaValue().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbParaValue p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbParaValue p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbParaValue> qw = new LambdaQueryWrapper<>();
        qw.in(EbParaValue::getParaId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    @Override
    public List<EbParaValue> getByItemId(Integer itemId) {
        Assert.notNull(itemId);
        LambdaQueryWrapper<EbParaValue> qw = Wrappers.lambdaQuery(EbParaValue.class)
                .orderByAsc(EbParaValue::getParaId)
                .eq(EbParaValue::getItemId, itemId);
        return super.list(qw);
    }
}
