package cc.ussu.modules.sheep.service.impl;

import cc.ussu.modules.sheep.entity.SheepEnv;
import cc.ussu.modules.sheep.mapper.SheepEnvMapper;
import cc.ussu.modules.sheep.service.ISheepEnvService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 环境变量 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:40:01
 */
@Master
@Service
public class SheepEnvServiceImpl extends ServiceImpl<SheepEnvMapper, SheepEnv> implements ISheepEnvService {

    @Override
    public List<SheepEnv> getList(String name) {
        return list(Wrappers.lambdaQuery(SheepEnv.class).eq(SheepEnv::getDisabled, false).eq(SheepEnv::getName, name));
    }

    @Override
    public List<String> getValueList(String name) {
        return getList(name).stream().map(SheepEnv::getValue).collect(Collectors.toList());
    }

    @Override
    public SheepEnv get(String name) {
        List<SheepEnv> list = getList(name);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<SheepEnv> queryList(String query) {
        if (StrUtil.isBlank(query)) {
            return new ArrayList<>();
        }
        return list(Wrappers.lambdaQuery(SheepEnv.class)
                .like(SheepEnv::getName, query)
                .or().like(SheepEnv::getValue, query)
                .or().like(SheepEnv::getRemarks, query));
    }

    @Override
    public String getValue(String name) {
        SheepEnv sheepEnv = get(name);
        if (sheepEnv != null) {
            return sheepEnv.getValue();
        }
        return null;
    }

    @Transactional
    @Override
    public void disable(String id) {
        Assert.notBlank(id, "缺少参数");
        SheepEnv sheepEnv = getById(id);
        Assert.notNull(sheepEnv, "环境变量不存在");
        Assert.isFalse(sheepEnv.getDisabled(), "已经是禁用状态");
        updateById(new SheepEnv().setId(id).setDisabled(true));
    }

    @Transactional
    @Override
    public void enable(String id) {
        Assert.notBlank(id, "缺少参数");
        SheepEnv sheepEnv = getById(id);
        Assert.notNull(sheepEnv, "环境变量不存在");
        Assert.isTrue(sheepEnv.getDisabled(), "已经是启用状态");
        updateById(new SheepEnv().setId(id).setDisabled(false));
    }

}
