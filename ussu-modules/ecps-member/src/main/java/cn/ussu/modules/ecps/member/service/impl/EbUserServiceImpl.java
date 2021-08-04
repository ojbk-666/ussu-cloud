package cn.ussu.modules.ecps.member.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.member.entity.EbUser;
import cn.ussu.modules.ecps.member.mapper.EbUserMapper;
import cn.ussu.modules.ecps.member.service.IEbUserService;
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
 * @since 2021-07-29
 */
@Service
public class EbUserServiceImpl extends ServiceImpl<EbUserMapper, EbUser> implements IEbUserService {

    @Override
    public EbUser detail(Integer id) {
        Assert.notNull(id);
        EbUser obj = new EbUser().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbUser p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbUser p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbUser> qw = new LambdaQueryWrapper<>();
        qw.in(EbUser::getEbUserId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
