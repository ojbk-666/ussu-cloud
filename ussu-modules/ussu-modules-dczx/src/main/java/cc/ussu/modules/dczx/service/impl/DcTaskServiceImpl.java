package cc.ussu.modules.dczx.service.impl;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.*;
import cc.ussu.modules.dczx.entity.vo.RunningTaskDTO;
import cc.ussu.modules.dczx.mapper.DcTaskMapper;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.StudyPlanVO;
import cc.ussu.modules.dczx.model.vo.videos.*;
import cc.ussu.modules.dczx.service.*;
import cc.ussu.modules.dczx.thread.AutoFinishCompHomeworkThread;
import cc.ussu.modules.dczx.thread.AutoFinishHomeworkThread;
import cc.ussu.modules.dczx.thread.AutoWatchVideoTaskThread;
import cc.ussu.modules.dczx.util.DczxUtil;
import cc.ussu.modules.dczx.util.VideoUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-01 15:52:07
 */
@Slave
@Service
public class DcTaskServiceImpl extends ServiceImpl<DcTaskMapper, DcTask> implements IDcTaskService {

    @Autowired
    private IDcCourseService courseService;
    @Autowired
    private IDcTaskVideoService taskVideoService;
    @Autowired
    private IDcTrusteeshipService trusteeshipService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DczxService dczxService;
    @Autowired
    private IDcUserStudyPlanService userStudyPlanService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 提交任务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitTask(DcTask task, boolean saveTask) {
        String trusteeshipId = task.getTrusteeshipId();
        DcTrusteeship trusteeship = trusteeshipService.getById(trusteeshipId);
        Assert.notNull(trusteeship, "未找到该账号");
        String dcUsername = trusteeship.getUsername();
        task.setDcUsername(dcUsername);
        String taskType = task.getTaskType();
        Date now = new Date();
        if (saveTask) {
            // 重复检查
            try {
                lock.lock();
                long c = super.count(Wrappers.lambdaQuery(DcTask.class)
                        .eq(DcTask::getCourseId, task.getCourseId())
                        .eq(DcTask::getDcUsername, dcUsername)
                        .eq(DcTask::getTaskType, taskType));
                Assert.isFalse(c > 0, "已存在该任务");
            } finally {
                lock.unlock();
            }
            task.setCreateTime(now).setCreateBy(SecurityUtil.getLoginUser().getUserId())
                    .setStatus(DczxConstants.TASK_STATUS_NOT_START).setProgress(0);
            // 保存任务
            task.setDelFlag(false);
            super.save(task);
        }
        // 是否已有进行中的任务
        try {
            lock.lock();
            long c = super.count(Wrappers.lambdaQuery(DcTask.class).eq(DcTask::getDcUsername, dcUsername)
                    .eq(DcTask::getStatus, DczxConstants.TASK_STATUS_DOING)
                    .eq(DcTask::getTaskType, taskType));
            // 如果已有进行中的任务则只添加但不运行任务
            if (c == 0) {
                // runTask(task);
                threadPoolTaskExecutor.submit(() -> runTask(task));
            }
        } finally {
            lock.unlock();
        }
    }

    private DczxLoginResultVo getLoginCookie(String username, String password) {
        DczxLoginResultVo loginResultVo = redisService.getCacheObject(DczxConstants.CACHE_JSESSIONID_PREFIX + username);
        if (loginResultVo == null) {
            synchronized (this) {
                if (loginResultVo == null) {
                    loginResultVo = DczxUtil.login(username, password);
                }
            }
        }
        return loginResultVo;
    }

    private DcTaskVideo getVideoByParam(List<DcTaskVideo> videoList, GoStudyParam param) {
        for (DcTaskVideo item : videoList) {
            if (item.getVersionCode().equals(param.getVersionCode())
                    && item.getChapterId().equals(param.getChapterId().toString())
                    && item.getSubChapterId().equals(param.getSubChapterId().toString())
                    && item.getServiceId().equals(param.getServiceId().toString())
                    && item.getServiceType().equals(param.getServiceType().toString())
            ) {
                return item;
            }
        }
        return null;
    }

    public DcUserStudyPlan getCurrentUserStudyPlan(DcTask task) {
        List<DcUserStudyPlan> userStudyPlanList = userStudyPlanService.listByUserid(task.getDcUsername());
        if (CollUtil.isNotEmpty(userStudyPlanList)) {
            List<DcUserStudyPlan> collect = userStudyPlanList.stream().filter(r -> r.getCourseId().equals(task.getCourseId())).collect(Collectors.toList());
            Assert.notEmpty(collect, "你没有购买该课程");
            return collect.get(0);
        }
        throw new IllegalArgumentException("没有获取到该账号的学习计划信息，请尝试删除该账号后重新添加");
    }

    /**
     * 通过学习计划添加任务
     */
    @Override
    public void addByStudyPlan(String studyPlanId, String type) {
        Assert.notBlank(studyPlanId, "参数不能为空");
        Assert.notBlank(type, "类型不能为空");
        Assert.isTrue(StrUtil.equalsAny(type, "1", "2"), "类型非法");
        DcUserStudyPlan studyPlan = userStudyPlanService.getById(studyPlanId);
        Assert.notNull(studyPlan, "没有找到该学习计划");
        Assert.isTrue("1".equals(studyPlan.getCurrentFlag()) && !"1".equals(studyPlan.getCourseStatusId()), "该课程不在当前学习计划或已通过考试");
        String courseId = studyPlan.getCourseId();
        // 查找用户名
        DcTrusteeship trusteeship = trusteeshipService.getByUsername(studyPlan.getUserid());
        Assert.notNull(trusteeship, "未找到对应的账号");
        submitTask(new DcTask().setTaskType(type).setCourseId(courseId).setTrusteeshipId(trusteeship.getId()),true);
    }

    /**
     * 运行视频任务
     */
    @Override
    public void runVideoTask(DcTask task) {
        String trusteeshipId = task.getTrusteeshipId();
        DcTrusteeship trusteeship = trusteeshipService.getById(trusteeshipId);
        Assert.notNull(trusteeship, "未找到该账号");
        String serviceCourseVersId = null;
        DcCourse course = courseService.getById(task.getCourseId());
        if (course != null) {
            serviceCourseVersId = course.getServiceCourseVersId();
        }
        // 登录
        DczxLoginResultVo loginResultVo = getLoginCookie(trusteeship.getUsername(), trusteeship.getPassword());
        // 获取视频ck
        VideoUtil.VideoCookieDTO videoCookieDTO = VideoUtil.getVideoCookie(loginResultVo.getJsessionid(), loginResultVo.getUserId(), serviceCourseVersId);
        String html = VideoUtil.getVideoListPage(videoCookieDTO);
        LmsStudyVO lmsStudyVO = VideoUtil.parse(html);
        List<RunningTaskDTO> runningTaskDTOList = new ArrayList<>();
        if (lmsStudyVO != null) {
            List<DcTaskVideo> dcTaskVideoList = taskVideoService.listByCourseVersion(lmsStudyVO.getVersionCode());
            List<Chapter> chapterList = lmsStudyVO.getChapterList();
            for (Chapter chapter : chapterList) {
                for (Division division : chapter.getDivisions()) {
                    List<DivisionCourse> courseList = division.getCourseList();
                    for (DivisionCourse divisionCourse : courseList) {
                        // 单元
                        divisionCourse.setChapterTitle(chapter.getTitle());
                        // 单元下的任务名称
                        divisionCourse.setDivisionTitle(division.getTitle());
                        GoStudyParam goStudyParam = divisionCourse.getGoStudyParam();
                        RunningTaskDTO detail = new RunningTaskDTO().setCookieStr(videoCookieDTO.getCookieStr())
                                .setGoStudyParam(goStudyParam).setDivisionCourse(divisionCourse)
                                .setChapterTitle(chapter.getTitle()).setTaskId(task.getId());
                        if (divisionCourse.isVideoDone() || divisionCourse.isTextDone() || divisionCourse.isHomeworkDone()) {
                        }
                        if (divisionCourse.isVideo()) {
                            DcTaskVideo taskVideo = getVideoByParam(dcTaskVideoList, goStudyParam);
                            if (taskVideo != null) {
                                detail.setDcTaskVideo(taskVideo);
                            }
                        } else if (divisionCourse.isText()) {
                        } else if (divisionCourse.isHomework()) {
                            // todo
                        }
                        runningTaskDTOList.add(detail);
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(runningTaskDTOList)) {
            AutoWatchVideoTaskThread thread = new AutoWatchVideoTaskThread(runningTaskDTOList, trusteeship);
            threadPoolTaskExecutor.submit(thread);
        }
        taskPauseLockInit(task.getId());
    }

    @Override
    public void runHomeworkTask(DcTask task) {
        // 获取配置信息
        String trusteeshipId = task.getTrusteeshipId();
        DcTrusteeship trusteeship = trusteeshipService.getById(trusteeshipId);
        Assert.notNull(trusteeship, "未找到该账号");
        // 获取学习计划
        DcUserStudyPlan currentUserStudyPlan = getCurrentUserStudyPlan(task);
        StudyPlanVO studyPlanVO = BeanUtil.toBean(currentUserStudyPlan, StudyPlanVO.class, CopyOptions.create().ignoreError());
        StudyPlanVO.ButtonList buttonList = JSONUtil.toBean(currentUserStudyPlan.getButtonList(), StudyPlanVO.ButtonList.class);
        studyPlanVO.setButtonList(buttonList);
        // 登录
        DczxLoginResultVo loginResultVo = getLoginCookie(trusteeship.getUsername(), trusteeship.getPassword());
        // 提交任务
        AutoFinishHomeworkThread autoFinishHomeworkThread = new AutoFinishHomeworkThread(loginResultVo, studyPlanVO, task.getId(), trusteeship);
        threadPoolTaskExecutor.submit(autoFinishHomeworkThread);
        taskPauseLockInit(task.getId());
    }

    @Override
    public void runCompHomeworkTask(DcTask task) {
        // 获取配置信息
        String trusteeshipId = task.getTrusteeshipId();
        DcTrusteeship trusteeship = trusteeshipService.getById(trusteeshipId);
        Assert.notNull(trusteeship, "未找到该账号");
        // 获取学习计划
        DcUserStudyPlan currentUserStudyPlan = getCurrentUserStudyPlan(task);
        StudyPlanVO studyPlanVO = BeanUtil.toBean(currentUserStudyPlan, StudyPlanVO.class, CopyOptions.create().ignoreError());
        StudyPlanVO.ButtonList buttonList = JSONUtil.toBean(currentUserStudyPlan.getButtonList(), StudyPlanVO.ButtonList.class);
        studyPlanVO.setButtonList(buttonList);
        // 登录
        DczxLoginResultVo loginResultVo = getLoginCookie(trusteeship.getUsername(), trusteeship.getPassword());
        // 提交任务
        AutoFinishCompHomeworkThread autoFinishHomeworkThread = new AutoFinishCompHomeworkThread(loginResultVo, studyPlanVO, task.getId(), trusteeship);
        threadPoolTaskExecutor.submit(autoFinishHomeworkThread);
        taskPauseLockInit(task.getId());
    }

    /**
     * 运行任务
     *
     * @param task
     */
    @Override
    public void runTask(DcTask task) {
        if (DcTask.TYPE_VIDEO.equals(task.getTaskType())) {
            // 视频任务
            runVideoTask(task);
        } else if (DcTask.TYPE_HOMEWORK.equals(task.getTaskType())) {
            // 单元作业任务
            runHomeworkTask(task);
        } else if (DcTask.TYPE_COMP_HOMEWORK.equals(task.getTaskType())) {
            // 综合作业
            runCompHomeworkTask(task);
        }
    }

    /**
     * 删除
     */
    @Transactional
    @Override
    public void del(List<String> idList) {
        CollUtil.removeBlank(idList);
        super.removeByIds(idList);
    }

    @Override
    public void taskPauseLockInit(String taskId) {
        redisService.setCacheObject(DczxConstants.THREAD_TASK_PARSE_KEY_PREFIX + taskId, false, 60L * 5);
    }

    @Override
    public void markTaskPause(String taskId) {
        redisService.setCacheObject(DczxConstants.THREAD_TASK_PARSE_KEY_PREFIX + taskId, true, 60L * 5);
    }

}
