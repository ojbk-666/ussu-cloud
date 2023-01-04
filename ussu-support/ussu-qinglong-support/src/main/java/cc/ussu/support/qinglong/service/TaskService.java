package cc.ussu.support.qinglong.service;

import cc.ussu.support.qinglong.dto.TaskDTO;
import cc.ussu.support.qinglong.dto.TaskDetailDTO;
import cc.ussu.support.qinglong.dto.TaskListDTO;
import cc.ussu.support.qinglong.dto.TaskLogDTO;

import java.util.List;

/**
 * 任务服务
 */
public interface TaskService extends BaseService {

    /**
     * 获取任务列表
     */
    TaskListDTO getTaskList(String searchValue);

    /**
     * 获取任务详情
     */
    TaskDetailDTO getTaskDetail(Integer id);

    /**
     * 获取任务日志
     */
    TaskLogDTO getTaskLog(Integer taskId);

    /**
     * 添加任务
     */
    void saveTask(TaskDTO task);

    /**
     * 编辑任务
     */
    void updateTask(TaskDTO task);

    /**
     * 删除任务
     */
    void deleteTask(List<Integer> ids);

    /**
     * 禁用任务
     */
    void disableTask(List<Integer> ids);

    /**
     * 启用任务
     */
    void enableTask(List<Integer> ids);

    /**
     * 运行任务
     */
    void runTask(List<Integer> ids);

}
