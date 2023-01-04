package cc.ussu.modules.sheep.service;

import cc.ussu.modules.sheep.entity.SheepTask;
import com.baomidou.mybatisplus.extension.service.IService;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 任务 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:38:58
 */
public interface ISheepTaskService extends IService<SheepTask> {

    List<SheepTask> listNotDisabledTask();

    void add(SheepTask p) throws SchedulerException;

    void edit(SheepTask p) throws SchedulerException;
}
