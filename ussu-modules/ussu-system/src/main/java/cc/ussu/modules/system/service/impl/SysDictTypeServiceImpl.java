package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysDictType;
import cc.ussu.modules.system.mapper.SysDictTypeMapper;
import cc.ussu.modules.system.service.ISysDictTypeService;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@Master
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Override
    public boolean checkTypeExist(SysDictType p) {
        LambdaQueryWrapper<SysDictType> qw = Wrappers.lambdaQuery(SysDictType.class).eq(SysDictType::getDictType, p.getDictType())
                .ne(SysDictType::getId, p.getId());
        return super.count(qw) > 0;
    }

    @Override
    public SysDictType getOneByType(String type) {
        return super.getOne(Wrappers.lambdaQuery(SysDictType.class).eq(SysDictType::getDictType, type));
    }
}
