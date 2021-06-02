package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.mapper.SysDictMapper;
import cn.ussu.modules.system.model.param.SysDictParam;
import cn.ussu.modules.system.service.ISysDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public ReturnPageInfo<SysDict> findPage(SysDictParam param) {
        LambdaQueryWrapper<SysDict> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(SysDict::getTypeId)
                .orderByAsc(SysDict::getDictSort);
        if (param != null) {
            String typeId = param.getTypeId();
            String typeCode = param.getTypeCode();
            Integer status = param.getStatus();
            String dictLabel = param.getDictLabel();
            qw.eq(StrUtil.isNotBlank(typeCode), SysDict::getTypeCode, typeCode)
                    .eq(StrUtil.isNotBlank(typeId), SysDict::getTypeId, typeId)
                    .eq(status != null, SysDict::getStatus, status)
                    .like(StrUtil.isNotBlank(dictLabel), SysDict::getDictLabel, dictLabel);
        }
        IPage<SysDict> iPage = this.sysDictMapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }
}
