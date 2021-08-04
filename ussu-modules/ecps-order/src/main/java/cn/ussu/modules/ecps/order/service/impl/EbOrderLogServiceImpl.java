package cn.ussu.modules.ecps.order.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.order.entity.EbOrderLog;
import cn.ussu.modules.ecps.order.mapper.EbOrderLogMapper;
import cn.ussu.modules.ecps.order.service.IEbOrderLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单日志	Name	Code	Comment	Default Value	Data Type	Length	 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Service
public class EbOrderLogServiceImpl extends ServiceImpl<EbOrderLogMapper, EbOrderLog> implements IEbOrderLogService {

    @Override
    public EbOrderLog detail(Integer id) {
        Assert.notNull(id);
        EbOrderLog obj = new EbOrderLog().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbOrderLog p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbOrderLog p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbOrderLog> qw = new LambdaQueryWrapper<>();
        qw.in(EbOrderLog::getOrderLogId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
