package cc.ussu.modules.dczx.service;

import cc.ussu.modules.dczx.entity.DcTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 任务 服务类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-01 15:52:07
 */
public interface IDcTaskService extends IService<DcTask> {

    /**
     * 提交任务
     *
     * @param task
     * @param saveTask 是否入库
     */
    void submitTask(DcTask task, boolean saveTask);

    /**
     * 通过学习计划添加任务
     */
    void addByStudyPlan(String studyPlanId, String type);

    /**
     * 运行视频任务
     */
    void runVideoTask(DcTask task);

    /**
     * 运行单元作业任务
     */
    void runHomeworkTask(DcTask task);

    void runCompHomeworkTask(DcTask task);

    /**
     * 运行任务
     */
    void runTask(DcTask task);

    /**
     * 删除
     */
    void del(List<String> idList);

    /**
     * 暂停标记初始化
     */
    void taskPauseLockInit(String taskId);

    /**
     * 标记为暂停
     */
    void markTaskPause(String taskId);

}
