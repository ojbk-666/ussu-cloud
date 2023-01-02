package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.vo.StringLogger;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import cc.ussu.modules.dczx.entity.vo.DcQuestionOptionVO;
import cc.ussu.modules.dczx.exception.TaskPausedException;
import cc.ussu.modules.dczx.exception.homework.InitWorkListmcapiException;
import cc.ussu.modules.dczx.exception.homework.ShowExamPapermcapiException;
import cc.ussu.modules.dczx.model.vo.DczxLoginResultVo;
import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import cc.ussu.modules.dczx.model.vo.StudyPlanVO;
import cc.ussu.modules.dczx.model.vo.TopicTrunk;
import cc.ussu.modules.dczx.model.vo.homework.HomeWork;
import cc.ussu.modules.dczx.model.vo.homework.InitWorkListmcapiResponse;
import cc.ussu.modules.dczx.model.vo.homework.SavemcapiParam;
import cc.ussu.modules.dczx.model.vo.homework.ShowExamPapermcapiResponse;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cc.ussu.modules.dczx.service.SendNoticeAfterTaskFinished;
import cc.ussu.modules.dczx.service.impl.SendNoticeAfterTaskFinishedEmailImpl;
import cc.ussu.modules.dczx.service.impl.SendNoticeAfterTaskFinishedNoticeImpl;
import cc.ussu.modules.dczx.util.HomeworkUtil;
import cc.ussu.modules.dczx.vo.DczxLogger;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.*;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自动完成单元作业任务的线程
 */
@Slf4j
@AllArgsConstructor
public class AutoFinishHomeworkThread extends Thread {

    /**
     * 登录信息
     */
    private DczxLoginResultVo loginResultVo;
    /**
     * 学习计划
     */
    private StudyPlanVO studyPlanVO;
    private String taskId;

    private DcTrusteeship trusteeship;

    // private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();
    @Override
    public void run() {
        DczxLogger sb = new DczxLogger(taskId);
        // 运行日志记录
        Integer updateStatus = DczxConstants.TASK_STATUS_DOING;
        Integer progress = 0;
        String reason = null;
        Date startTime = new Date();
        IDcTaskService taskService = getTaskService();
        DcTask taskInfo = taskService.getById(taskId);
        // DcCourse courseInfo = SpringUtil.getBean(IDcCourseService.class).getById(taskInfo.getCourseId());
        taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus).setStartTime(startTime));
        try {
            // 获取已完成的单元作业列表及分数
            InitWorkListmcapiResponse initWorkListmcapiResponse = HomeworkUtil.getHomeworkList(loginResultVo, studyPlanVO);
            // float currentScore = initWorkListmcapiResponse.getExampaperScore();
            float currentScore = HomeworkUtil.queryHomeworkScore(loginResultVo, studyPlanVO.getCourseId());
            String courseName = initWorkListmcapiResponse.getCourseName();
            // 分数合格
            final float targetScore = trusteeship.getHomeworkTargetScore().floatValue();
            // 最大做题次数
            int homeworkMaxCount = trusteeship.getHomeworkMaxCount().intValue();
            if (currentScore >= targetScore) {
                sb.info("课程 {} 已达到目标分数 {} -> {}", courseName, currentScore, targetScore);
                updateStatus = DczxConstants.TASK_STATUS_FINISHED;
                progress = 100;
                taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus)
                        .setProgress(progress).setTaskLog(sb.toString()));
                return;
            }
            List<HomeWork> homeWorkList = initWorkListmcapiResponse.getHomeWorkList();
            // 反转 从前往后做
            homeWorkList = CollUtil.reverse(homeWorkList);
            // 判断每一科目的成绩是否合格
            a:
            for (int i = 0; i < homeWorkList.size(); i++) {
                checkPauseTask();
                HomeWork item = homeWorkList.get(i);
                // 已完成几次
                int doCount = CollUtil.size(item.getExamResultInfos());
                if (item.getWORKER_SCORE() >= targetScore) {
                    sb.info("课程 {}：{} 已达到目标分数 {} -> {}", courseName, item.getWORKER_NAME(), item.getWORKER_SCORE(), targetScore);
                    continue a;
                }
                if (doCount >= homeworkMaxCount) {
                    sb.info("已达到最大完成次数，建议手动做题");
                }
                // StringBuffer sb = new StringBuffer();
                // 准备做题
                sb.info("现在开始做题");
                sb.info("一.获取试题...");
                String workerId = item.getWORKER_ID();  // WORKER_ID
                ShowExamPapermcapiResponse newHomework = HomeworkUtil.showExamPapermcapi(loginResultVo, studyPlanVO, workerId);
                if (newHomework == null) {
                    sb.info("获取新的单元作业题目异常");
                    throw new ShowExamPapermcapiException();
                }
                String paperid = newHomework.getPAPERID();  // PAPERID
                // 本地获取的试题有多少道题目
                int newHomeworkQuestionCount = CollUtil.size(newHomework.getPAPER_QUESTIONS());
                sb.info("获取试题成功，共 {} 道题目", newHomeworkQuestionCount);
                // 获取题目的答案
                List<PaperQuestion> newHomeworkPAPERQuestions = newHomework.getPAPER_QUESTIONS();
                sb.info("二.从题库搜索正确答案...");
                List<DcPaperQuestionVO> dcPaperQuestionList = convertToDcQuestions(newHomeworkPAPERQuestions);
                if (CollUtil.isNotEmpty(dcPaperQuestionList)) {
                    // 每题大概多少分
                    int everyQuestionScore = 100 / newHomeworkQuestionCount;
                    // 预计成绩
                    int expectedScore = everyQuestionScore * dcPaperQuestionList.size();
                    if (expectedScore < targetScore) {
                        // 如果获取的答案不足则放弃提交
                        sb.info("预估本地提交成绩 {} 小于目标分数 {}，由于答题时间限制，可能造成答题分数为0 本次仍然提交", expectedScore, targetScore);
                        continue a;
                    }
                    sb.info("三.开始做题...");
                    // 提交答案
                    b:
                    for (PaperQuestion paperQuestion : newHomeworkPAPERQuestions) {
                        checkPauseTask();
                        List<TopicTrunk> topicTrunks = paperQuestion.getTOPIC_TRUNK();
                        if (CollUtil.isEmpty(topicTrunks)) {
                            continue b;
                        }
                        TopicTrunk topicTrunk = topicTrunks.get(0);
                        String question_id = topicTrunk.getQUESTION_ID();
                        DcPaperQuestionVO dcPaperQuestion = getMatchedDcPaperQuestion(dcPaperQuestionList, question_id);
                        if (dcPaperQuestion == null) {
                            sb.info("❗❗❗❗❗没有找到正确答案❗❗❗❗❗");
                            continue b;
                        }
                        String topicName = paperQuestion.getQUESTION_TYPE_NM(); // 题型
                        String questionTitle = topicTrunk.getQUESTION_TITLE();
                        List<DcQuestionOptionVO> options = dcPaperQuestion.getOptions();
                        // 做题
                        sb.info("正在做第 {} 题：{} -> {}",newHomeworkPAPERQuestions.indexOf(paperQuestion), topicName, questionTitle);
                        extracted(sb, paperid, paperQuestion, options);
                        checkPauseTask();
                    }
                    sb.info("四.开始交卷...");
                    // 提交单元作业
                    Float newScore = HomeworkUtil.getExamScoremcapi(loginResultVo, studyPlanVO, paperid, workerId);
                    if (newScore != null) {
                        sb.info("单元作业答案提交成功 -> 分数为：{}", newScore);
                    } else {
                        sb.info("单元作业答案提交成功");
                    }
                }
                sb.newline();
                // 进度
                int count = 100 / homeWorkList.size();
                progress = count * i;
                checkPauseTask();
            }
            updateStatus = DczxConstants.TASK_STATUS_FINISHED;
            progress = 100;
            // 发送通知
            if (BooleanUtil.isTrue(trusteeship.getSendNoticeFlag())) {
                sendOneTaskFinishedNotice(taskInfo, courseName);
            }
        } catch (InitWorkListmcapiException e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = e.getMessage();
        } catch (ShowExamPapermcapiException e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = e.getMessage();
        } catch (TaskPausedException e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = e.getMessage();
        } catch (Exception e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = e.getMessage();
        } finally {
            taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus)
                    .setProgress(progress).setEndTime(new Date())
                    .setReason(reason).setTaskLog(sb.toString()));
        }
        // 下一个
        if (!pauseTask()) {
            DcTask notStartOne = taskService.getOne(Wrappers.lambdaQuery(DcTask.class)
                .orderByAsc(DcTask::getCreateTime)
                .eq(DcTask::getDcUsername, trusteeship.getUsername())
                .eq(DcTask::getTaskType, DcTask.TYPE_HOMEWORK)
                .eq(DcTask::getStatus, DczxConstants.TASK_STATUS_NOT_START)
                .last(" limit 1 "));
            if (notStartOne != null) {
                taskService.runTask(notStartOne);
            } else {
                // 没有任务了
                sendAllTaskFinishedNotice(taskInfo);
            }
        }
    }

    /**
     * 发送单个任务完成通知
     */
    private void sendOneTaskFinishedNotice(DcTask taskInfo, String courseName) {
        if (BooleanUtil.isTrue(trusteeship.getSendNoticeFlag()) && DcTrusteeship.NOTICE_TYPE_ONE_TASK.equals(trusteeship.getSendNoticeType())) {
            String reciever = trusteeship.getSendNoticeId();
            String noticeTitle = StrUtil.format("账号 {} {} 任务完成", taskInfo.getDcUsername(), courseName);
            String noticeContent = StrUtil.format("您于 {} 提交的账号 {} 的 {} 刷单元作业任务 已完成",
                DateUtil.formatDateTime(trusteeship.getCreateTime()), taskInfo.getDcUsername(), courseName);
            SendNoticeAfterTaskFinished send = null;
            if (DcTrusteeship.NOTICE_METHOD_NOTICE.equals(trusteeship.getSendNoticeMethod())) {
                // 站内信
                send = SpringUtil.getBean(SendNoticeAfterTaskFinishedNoticeImpl.class);
            } else if (DcTrusteeship.NOTICE_METHOD_EMAIL.equals(trusteeship.getSendNoticeMethod())) {
                // 邮件
                send = SpringUtil.getBean(SendNoticeAfterTaskFinishedEmailImpl.class);
            }
            send.notice(reciever, noticeTitle, noticeContent);
        }
    }

    /**
     * 发送所有任务完成通知
     */
    private void sendAllTaskFinishedNotice(DcTask taskInfo) {
        if (BooleanUtil.isTrue(trusteeship.getSendNoticeFlag()) && DcTrusteeship.NOTICE_TYPE_ALL_TASK.equals(trusteeship.getSendNoticeType())) {

        }
    }

    /**
     * 检测是否暂停
     */
    private boolean pauseTask() {
        Object b = SpringUtil.getBean(RedisService.class).getCacheObject(DczxConstants.THREAD_TASK_PARSE_KEY_PREFIX + taskId);
        if (b == null) {
            return false;
        }
        return (boolean) b;
    }

    private void checkPauseTask() {
        if (pauseTask()) {
            throw new TaskPausedException();
        }
    }

    private void extracted(StringLogger sb, String paperid, PaperQuestion paperQuestion, List<DcQuestionOptionVO> options) {
        // 判断题单独处理
        if (DcPaperQuestionTopic.PAN_DUAN.equals(paperQuestion.getFulltopictypecd())) {
            for (DcQuestionOptionVO r : options) {
                String generalanswer = "999";
                if (BooleanUtil.isTrue(r.getIstrue())) {
                    // 正确 999
                } else {
                    // 错误 888
                    generalanswer = "888";
                }
                SavemcapiParam savemcapiParam = HomeworkUtil.convert(paperQuestion, paperid, r.getOptionId());
                savemcapiParam.setGeneralanswer(generalanswer);
                doWorkQuestion(savemcapiParam, r.getOptionContent(), sb);
            }
        } else {
            for (DcQuestionOptionVO r : options) {
                if (BooleanUtil.isTrue(r.getIstrue())) {
                    // 正确答案则提交进度
                    SavemcapiParam savemcapiParam = HomeworkUtil.convert(paperQuestion, paperid, r.getOptionId());
                    doWorkQuestion(savemcapiParam, r.getOptionContent(), sb);
                }
            }
        }
    }

    /**
     * 做题
     *
     * @param savemcapiParam
     * @param optionContent
     */
    private void doWorkQuestion(SavemcapiParam savemcapiParam, String optionContent, StringLogger sb) {
        try {
            if (HomeworkUtil.savemcapi(loginResultVo, savemcapiParam)) {
                sb.info("✔✔提交答案成功：{}", optionContent);
                long sleepTime = RandomUtil.randomLong(2000, 4500);
                sb.info("等待 {} 秒", (sleepTime / 1000F));
                Thread.sleep(sleepTime);
            } else {
                sb.info("❌❌提交答案失败：{}", optionContent);
            }
        } catch (InterruptedException e) {
        } catch (HttpException e) {
            sb.info("❌❌提交答案失败：{}", e.getMessage(), e);
        } catch (Exception e) {
            sb.info("❌❌提交答案失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取每道题目保存的间隔时间
     */
    private long getSleepTime() {
        return 1000;
    }

    private IDcTaskService getTaskService() {
        return SpringUtil.getBean(IDcTaskService.class);
    }

    /**
     * 获取题目对应的答案
     *
     * @param paperQuestionList
     * @param questionId
     * @return
     */
    private PaperQuestion getMatchedPaperQuestion(List<PaperQuestion> paperQuestionList, String questionId) {
        for (PaperQuestion item : paperQuestionList) {
            List<TopicTrunk> topicTrunkList = item.getTOPIC_TRUNK();
            if (CollUtil.isNotEmpty(topicTrunkList)) {
                if (topicTrunkList.get(0).getQUESTION_ID().equals(questionId)) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * 获取匹配的题目答案信息
     */
    private DcPaperQuestionVO getMatchedDcPaperQuestion(List<DcPaperQuestionVO> paperQuestionList, String questionId) {
        for (DcPaperQuestionVO item : paperQuestionList) {
            if (item.getQuestionId().equals(questionId)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 转换接口的题目为系统已有题目，包含正确答案
     *
     * @param newQuestions
     * @return
     */
    private List<DcPaperQuestionVO> convertToDcQuestions(List<PaperQuestion> newQuestions) {
        // 获取题目集合
        List<String> questionIdList = newQuestions.stream().map(r -> {
            List<TopicTrunk> topicTrunks = r.getTOPIC_TRUNK();
            if (CollUtil.isNotEmpty(topicTrunks)) {
                return topicTrunks.get(0).getQUESTION_ID();
            }
            return null;
        }).filter(ObjectUtil::isNotNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(questionIdList)) {
            return new ArrayList<>();
        }
        // 获取答案
        IDcPaperQuestionService questionService = SpringUtil.getBean(IDcPaperQuestionService.class);
        List<DcPaperQuestionVO> dcPaperQuestionList = questionService.findByQuestionIds(ArrayUtil.toArray(questionIdList, String.class));
        return dcPaperQuestionList;
    }

}
