package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.core.exception.RequestEmptyException;
import cn.ussu.modules.system.core.util.DefaultPageFactory;
import cn.ussu.modules.system.core.util.DictUtil;
import cn.ussu.modules.system.entity.SysDict;
import cn.ussu.modules.system.entity.SysDictType;
import cn.ussu.modules.system.mapper.SysDictMapper;
import cn.ussu.modules.system.mapper.SysDictTypeMapper;
import cn.ussu.modules.system.model.param.SysDictTypeParam;
import cn.ussu.modules.system.model.result.SysDictTypeResult;
import cn.ussu.modules.system.service.ISysDictTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-07
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;
    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public ReturnPageInfo<SysDictType> findPage(SysDictTypeParam param) {
        LambdaQueryWrapper<SysDictType> qw = new LambdaQueryWrapper<>();
        if (param != null) {
            qw.like(StrUtil.isNotBlank(param.getTypeName()), SysDictType::getTypeName, param.getTypeName())
                    .like(StrUtil.isNotBlank(param.getTypeCode()), SysDictType::getTypeCode, param.getTypeCode())
                    .eq(param.getStatus() != null, SysDictType::getStatus, param.getStatus());
        }
        IPage iPage = this.sysDictTypeMapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

    @Override
    @Transactional
    public void deleteDictTypeAndData(String id) {
        if (StrUtil.isBlank(id)) throw new RequestEmptyException();
        SysDictType old = new SysDictType().setId(id).selectById();
        this.sysDictTypeMapper.deleteById(id);
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.eq("type_id", id);
        sysDictMapper.delete(qw);
        DictUtil.deleteDictType(old.getTypeCode());
    }

    // result

    @Override
    public List<SysDictTypeResult> listAllSysDictTypeResultList() {
        return this.sysDictTypeMapper.listAllSysDictTypeResult();
    }
}
