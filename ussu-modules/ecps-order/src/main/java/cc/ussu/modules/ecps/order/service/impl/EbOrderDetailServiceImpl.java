package cc.ussu.modules.ecps.order.service.impl;

import cc.ussu.modules.ecps.order.entity.EbOrderDetail;
import cc.ussu.modules.ecps.order.mapper.EbOrderDetailMapper;
import cc.ussu.modules.ecps.order.service.IEbOrderDetailService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 3-10是商品的冗余数据	11-26是营销案的冗余数据	营销案的时候，根据SKU存多条，每条SK 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Service
public class EbOrderDetailServiceImpl extends ServiceImpl<EbOrderDetailMapper, EbOrderDetail> implements IEbOrderDetailService {

    @Override
    public EbOrderDetail detail(Integer id) {
        Assert.notNull(id);
        EbOrderDetail obj = new EbOrderDetail().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbOrderDetail p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbOrderDetail p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbOrderDetail> qw = new LambdaQueryWrapper<>();
        qw.in(EbOrderDetail::getOrderDetailId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
