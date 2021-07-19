package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.item.entity.EbParaValue;
import cn.ussu.modules.ecps.item.mapper.EbParaValueMapper;
import cn.ussu.modules.ecps.item.service.IEbParaValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        qw.in(EbParaValue::getParaId, ids);
        super.remove(qw);
    }

}
