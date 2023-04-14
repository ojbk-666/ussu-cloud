package cc.ussu.modules.dczx.thread;

import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.vo.RunningTaskDTO;
import cc.ussu.modules.dczx.exception.TaskPausedException;
import cc.ussu.modules.dczx.model.vo.videos.DivisionCourse;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cc.ussu.modules.dczx.service.IDcUserStudyPlanService;
import cc.ussu.modules.dczx.util.VideoUtil;
import cc.ussu.modules.dczx.vo.DczxLogger;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 获取课程作业列表的视频和作业
 * 首页 - 开始学习按钮跳过去的页面
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AutoWatchVideoTaskThread extends Thread {

    private List<RunningTaskDTO> detailList;
    private DcTrusteeship trusteeship;

    @Override
    public void run() {
        if (CollUtil.isNotEmpty(detailList)) {
            final String dcUsername = trusteeship.getUsername();
            List<List<RunningTaskDTO>> groupList = CollUtil.groupByField(detailList, "chapterTitle");
            IDcTaskService taskService = getTaskService();
            final int total = detailList.size();
            RunningTaskDTO first = CollUtil.getFirst(detailList);
            String taskId = first.getTaskId();
            // 更新开始时间
            taskService.update(new DcTask().setStartTime(new Date()), Wrappers.lambdaQuery(DcTask.class).eq(DcTask::getId, taskId).isNull(DcTask::getStartTime));
            long pauseTime = getPauseTime();
            boolean parseTaskFlag = false;
            // 运行日志记录
            DczxLogger sb = new DczxLogger(taskId);
            Integer updateStatus = null;
            String reason = null;
            try {
                boolean updateTaskProgress = false;
                // 更新开始时间
                taskService.updateById(new DcTask().setId(taskId).setStatus(DczxConstants.TASK_STATUS_DOING));
                int finishedRunningTaskCount = 0;
                for (List<RunningTaskDTO> group : groupList) {
                    RunningTaskDTO groupFirst = CollUtil.getFirst(group);
                    // 日志
                    sb.info("---------- {} ----------", groupFirst.getChapterTitle());
                    if (groupFirst.getDivisionCourse() != null) {
                        sb.info(groupFirst.getDivisionCourse().getDivisionTitle());
                    }
                    a:
                    for (int j = 0; j < group.size(); j++) {
                        finishedRunningTaskCount++;
                        RunningTaskDTO item = group.get(j);
                        DivisionCourse divisionCourse = item.getDivisionCourse();
                        DcTaskVideo dcTaskVideo = item.getDcTaskVideo();
                        String logContent = divisionCourse.getChapterTitle() + ": " + divisionCourse.getDivisionTitle() + " -> " + divisionCourse.getTitle();
                        String divisionCourseTitle = divisionCourse.getTitle();
                        if (divisionCourse.isVideo()) {
                            // 视频任务
                            if (divisionCourse.isVideoDone()) {
                                // 完成
                                sb.log("视频任务 {} 已完成,跳过 ", divisionCourseTitle);
                            } else {
                                // if (parseTaskFlag = pauseTask(taskId)) {
                                //     break a;
                                // }
                                if (dcTaskVideo == null) {
                                    sb.log("未找到该视频 {}", divisionCourseTitle);
                                    continue a;
                                }
                                updateTaskProgress = true;
                                // 计算要发起的次数
                                Integer duration = dcTaskVideo.getDuration();
                                Assert.notNull(duration, "未获取到视频时长");
                                if (BooleanUtil.isFalse(trusteeship.getVideoJump())) {
                                    int count = duration / 5;
                                    int start = -1;
                                    b:
                                    for (int i = 1; i <= count; i++) {
                                        if (i != count - 1) {
                                            Thread.sleep(pauseTime);
                                        }
                                        // if (i % 11 == 0 && (parseTaskFlag = pauseTask(taskId))) {
                                        //     break a;
                                        // }
                                        int progress = start + (i * 5);
                                        if (progress <= duration) {
                                            VideoUtil.updateVideoProgress(dcTaskVideo, progress, item.getCookieStr());
                                            sb.debug("任务进行中：{}: {} / {} ({}%)", logContent, progress, duration, (progress * 100 / duration));
                                            // sb.append(StrUtil.format("任务进行中：{}: {} / {} ({}%)", logContent, progress, duration, (progress * 100 / duration))).append(NEWLINE);
                                        }
                                    }
                                    updateTaskLog(taskId, sb.toString());
                                }
                                VideoUtil.updateVideoProgress(dcTaskVideo, VideoUtil.VIDEO_FINISH_PROGRESS, item.getCookieStr());
                                sb.info("视频任务 任务完成：{}", logContent);
                                Thread.sleep(2000);
                                // 单元小结任务完成
                            }
                        } else if (divisionCourse.isText()) {
                            // 文本
                            if (divisionCourse.isTextDone()) {
                                sb.info("浏览课件 {} 任务已完成,跳过 ", divisionCourseTitle);
                            } else {
                                // if (parseTaskFlag = pauseTask(taskId)) {
                                //     break;
                                // }
                                updateTaskProgress = true;
                                Thread.sleep(pauseTime);
                                VideoUtil.showViewPage(divisionCourse.getGoStudyParam(), item.getCookieStr());
                                sb.info("浏览课件 {} 任务完成", divisionCourseTitle);
                            }
                        } else if (divisionCourse.isHomework()) {
                            if (divisionCourse.isHomeworkDone()) {
                                sb.info("视频中的作业 {} 任务已完成,跳过", divisionCourseTitle);
                            } else {
                                // 暂不支持
                                // updateTaskProgress = true;
                                sb.info("视频中的作业任务 {} 暂不支持,跳过", divisionCourseTitle);
                            }
                        }
                        // 更新总进度
                        if (updateTaskProgress && StrUtil.isNotBlank(taskId)) {
                            DcTask updateTaskEntity = new DcTask().setId(taskId).setProgress((finishedRunningTaskCount * 100) / total);
                            taskService.updateById(updateTaskEntity);
                        }
                        // log.info(">>>>>> 任务完成 ");
                    }
                    sb.newline();
                }
                // 全部任务完成
                if (StrUtil.isNotBlank(taskId)) {
                    if (parseTaskFlag) {
                        // 暂停任务
                        throw new TaskPausedException();
                    } else {
                        // 任务完成
                        DcTask updateTaskEntity = new DcTask().setId(taskId)
                                .setEndTime(new Date()).setProgress(100)
                                .setStatus(DczxConstants.TASK_STATUS_FINISHED);
                        taskService.updateById(updateTaskEntity);
                    }
                }
            } catch (InterruptedException e) {
                sb.info("任务被暂停");
                updateStatus = DczxConstants.TASK_STATUS_ERROR;
                reason = "任务被暂停";
            } catch (Exception e) {
                e.printStackTrace();
                sb.info(e.getMessage());
                updateStatus = DczxConstants.TASK_STATUS_ERROR;
                reason = "发生错误 " + e.getMessage();
            } finally {
                updateTaskLog(taskId, sb.toString());
                taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus).setReason(reason).setTaskLog(sb.toString()));
            }
            // 下一个循环
            // if (!pauseTask(taskId)) {
            DcTask notStartOne = taskService.getOne(Wrappers.lambdaQuery(DcTask.class).orderByAsc(DcTask::getCreateTime)
                    .eq(DcTask::getDcUsername, dcUsername)
                    .eq(DcTask::getTaskType, DcTask.TYPE_VIDEO)
                    .eq(DcTask::getStatus, DczxConstants.TASK_STATUS_NOT_START)
                    .last(" limit 1 "));
            if (notStartOne != null) {
                taskService.runTask(notStartOne);
            } else {
                // 没有任务了
            }
            // }
            // 更新日志
            updateTaskLog(taskId, sb.toString());
            // 更新课程计划
            SpringUtil.getBean(IDcUserStudyPlanService.class)
                    .updateUserStudyPlan(trusteeship.getUsername(), trusteeship.getPassword(), trusteeship.getCreateBy());
        }
    }

    private void updateTaskLog(String taskId, String logContent) {
        if (StrUtil.isNotEmpty(logContent)) {
            getTaskService().updateById(new DcTask().setId(taskId).setTaskLog(logContent));
        }
    }

    private IDcTaskService getTaskService() {
        return SpringUtil.getBean(IDcTaskService.class);
    }

    /**
     * 观看视频的暂停时间，根据视频倍速计算
     * 1x   5s      5000ms
     * 5x   1s      1000ms
     * 10x  0.5s    500ms
     */
    private long getPauseTime() {
        Integer videoSpeed = trusteeship.getVideoSpeed();
        if (videoSpeed == null || videoSpeed < 1) {
            videoSpeed = 5;
        }
        return new Integer(5000 / videoSpeed).longValue();
    }

}
