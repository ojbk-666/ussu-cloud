package cc.ussu.modules.ecps.member.service.impl;

import cc.ussu.modules.ecps.member.entity.EbShipAddr;
import cc.ussu.modules.ecps.member.mapper.EbShipAddrMapper;
import cc.ussu.modules.ecps.member.service.IEbShipAddrService;
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
 * 收货地址 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-29
 */
@Service
public class EbShipAddrServiceImpl extends ServiceImpl<EbShipAddrMapper, EbShipAddr> implements IEbShipAddrService {

    @Override
    public EbShipAddr detail(Integer id) {
        Assert.notNull(id);
        EbShipAddr obj = new EbShipAddr().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbShipAddr p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbShipAddr p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbShipAddr> qw = new LambdaQueryWrapper<>();
        qw.in(EbShipAddr::getShipAddrId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
