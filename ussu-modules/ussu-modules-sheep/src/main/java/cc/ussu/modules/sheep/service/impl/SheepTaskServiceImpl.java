package cc.ussu.modules.sheep.service.impl;

import cc.ussu.modules.sheep.entity.SheepTask;
import cc.ussu.modules.sheep.mapper.SheepTaskMapper;
import cc.ussu.modules.sheep.service.ISheepTaskService;
import cc.ussu.modules.sheep.util.SheepQuartzUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 任务 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:38:58
 */
@Master
@Service
public class SheepTaskServiceImpl extends ServiceImpl<SheepTaskMapper, SheepTask> implements ISheepTaskService {

    @Override
    public List<SheepTask> listNotDisabledTask() {
        return super.list(Wrappers.lambdaQuery(SheepTask.class).eq(SheepTask::getDisabled, false));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SheepTask p) throws SchedulerException {
        p.setTargetClass(p.getId());
        Assert.isNull(getById(p.getId()), "该任务已存在");
        super.save(p);
        SheepQuartzUtil.addJob(p);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SheepTask p) throws SchedulerException {
        super.updateById(p);
        SheepQuartzUtil.addJob(p);
    }
}
