package cc.ussu.modules.dczx.thread;

import cc.ussu.common.core.vo.StringLogger;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.entity.DcTask;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import cc.ussu.modules.dczx.entity.vo.DcQuestionOptionVO;
import cc.ussu.modules.dczx.exception.InitExamListmcapiException;
import cc.ussu.modules.dczx.exception.homework.InitWorkListmcapiException;
import cc.ussu.modules.dczx.exception.homework.ShowExamPapermcapiException;
import cc.ussu.modules.dczx.model.vo.*;
import cc.ussu.modules.dczx.model.vo.composite.ExamVo;
import cc.ussu.modules.dczx.model.vo.composite.InitExamListmcapiVo;
import cc.ussu.modules.dczx.model.vo.composite.SaveForMCRExammcapiParam;
import cc.ussu.modules.dczx.model.vo.homework.ShowExamPapermcapiResponse;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcTaskService;
import cc.ussu.modules.dczx.service.SendNoticeAfterTaskFinished;
import cc.ussu.modules.dczx.service.impl.SendNoticeAfterTaskFinishedEmailImpl;
import cc.ussu.modules.dczx.service.impl.SendNoticeAfterTaskFinishedNoticeImpl;
import cc.ussu.modules.dczx.util.CompHomeworkUtil;
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
 * 自动完成综合作业任务的线程
 */
@Slf4j
@AllArgsConstructor
public class AutoFinishCompHomeworkThread extends Thread {

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
            // 获取已完成的综合作业列表及分数
            InitExamListmcapiVo initExamListmcapiVo = CompHomeworkUtil.getCompHomeworkList(loginResultVo, studyPlanVO);
            if (CollUtil.isEmpty(initExamListmcapiVo.getExamList())) {
                sb.info("该课程没有综合作业任务");
                throw new InitExamListmcapiException();
            }
            ExamVo examVo = initExamListmcapiVo.getExamList().get(0);
            Integer currentScore = examVo.getWORKER_SCORE();
            // float currentScore = initWorkListmcapiResponse.getExampaperScore();
            String courseName = examVo.getWORKER_NAME();    // 论文写作指导综合作业
            // 分数合格
            final int targetScore = trusteeship.getCompHomeworkTargetScore();
            // 最大做题次数
            int compHomeworkMaxCount = trusteeship.getCompHomeworkMaxCount().intValue();
            if (currentScore >= targetScore) {
                sb.info("课程 {} 已达到目标分数 {} -> {}", studyPlanVO.getCourseName(), currentScore, targetScore);
                updateStatus = DczxConstants.TASK_STATUS_FINISHED;
                progress = 100;
                taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus)
                        .setProgress(progress).setTaskLog(sb.toString()));
                return;
            }
            // 已完成几次
            int doCount = Integer.parseInt(examVo.getDO_COUNT());
            if (doCount >= compHomeworkMaxCount) {
                sb.info("已达到最大完成次数，建议手动做题");
            }
            // 准备做题
            sb.info("现在开始做题");
            sb.info("一.获取试题...");
            String workerId = examVo.getWORKER_ID();  // WORKER_ID
            ShowExamPapermcapiResponse newHomework = CompHomeworkUtil.showExamPapermcapi(loginResultVo, studyPlanVO, workerId);
            if (newHomework == null) {
                sb.info("获取新的综合作业题目异常");
                throw new ShowExamPapermcapiException("获取新的综合作业题目异常");
            }
            String paperid = newHomework.getPAPERID();  // PAPERID
            // 本地获取的试题有多少道题目
            int newHomeworkQuestionCount = CollUtil.size(newHomework.getPAPER_QUESTIONS());
            sb.info("获取试题成功，共 {} 道题目", newHomeworkQuestionCount);
            // 获取题目的答案
            sb.info("二.搜索正确答案...");
            List<PaperQuestion> newHomeworkPAPERQuestions = newHomework.getPAPER_QUESTIONS();
            List<DcPaperQuestionVO> dcQuestions = convertToDcQuestions(newHomeworkPAPERQuestions);
            sb.info("正确答案获取成功,获取到 {} 个题目的答案", dcQuestions.size());
            // 预计成绩
            int expectedScore = 100;
            sb.info("三.准备开始做题...");
            // 提交答案
            b:
            for (int i = 0; i < newHomeworkPAPERQuestions.size(); i++) {
                PaperQuestion paperQuestion = newHomeworkPAPERQuestions.get(i);
                String topicName = paperQuestion.getQUESTION_TYPE_NM(); // 题型
                List<TopicTrunk> topicTrunks = paperQuestion.getTOPIC_TRUNK();
                if (CollUtil.isEmpty(topicTrunks)) {
                    continue b;
                }
                TopicTrunk topicTrunk = topicTrunks.get(0);
                String questionTitle = topicTrunk.getQUESTION_TITLE();
                List<QuestionOption> options = topicTrunk.getQUESTION_OPTIONS();
                // 做题
                sb.info("正在做第 {} 题：{} -> {}", (i+1), topicName, questionTitle);
                extracted(sb, paperid, paperQuestion, dcQuestions);
                long sleepTime = RandomUtil.randomLong(2000, 4500);
                sb.info("等待 {} 秒", (sleepTime / 1000F));
                Thread.sleep(sleepTime);
                // 进度
                int count = 100 / newHomeworkQuestionCount;
                progress = count * i;
            }
            sb.info("四.开始交卷...");
            // 提交综合作业
            Float newScore = CompHomeworkUtil.getExamScoremcapi(loginResultVo, studyPlanVO, paperid, workerId, newHomework.getUserexampaper_id());
            if (newScore != null) {
                sb.info("综合作业答案提交成功 -> 分数为：{}", newScore);
            } else {
                sb.info("综合作业答案提交成功");
            }
            sb.newline();
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
        } catch (InterruptedException e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = "任务被暂停";
        } catch (Exception e) {
            updateStatus = DczxConstants.TASK_STATUS_ERROR;
            reason = e.getMessage();
        } finally {
            taskService.updateById(new DcTask().setId(taskId).setStatus(updateStatus)
                    .setProgress(progress).setEndTime(new Date())
                    .setReason(reason).setTaskLog(sb.toString()));
        }
        // 下一个
        // if (!pauseTask()) {
        DcTask notStartOne = taskService.getOne(Wrappers.lambdaQuery(DcTask.class)
                .orderByAsc(DcTask::getCreateTime)
                .eq(DcTask::getDcUsername, trusteeship.getUsername())
                .eq(DcTask::getTaskType, DcTask.TYPE_COMP_HOMEWORK)
                .eq(DcTask::getStatus, DczxConstants.TASK_STATUS_NOT_START)
                .last(" limit 1 "));
        if (notStartOne != null) {
            taskService.runTask(notStartOne);
        } else {
            // 没有任务了
            sendAllTaskFinishedNotice(taskInfo);
        }
        // }
    }

    /**
     * 发送单个任务完成通知
     */
    private void sendOneTaskFinishedNotice(DcTask taskInfo, String courseName) {
        if (BooleanUtil.isTrue(trusteeship.getSendNoticeFlag()) && DcTrusteeship.NOTICE_TYPE_ONE_TASK.equals(trusteeship.getSendNoticeType())) {
            String reciever = trusteeship.getSendNoticeId();
            String noticeTitle = StrUtil.format("账号 {} {} 任务完成", taskInfo.getDcUsername(), courseName);
            String noticeContent = StrUtil.format("您于 {} 提交的账号 {} 的 {} 刷综合作业任务 已完成",
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

    private void extracted(StringLogger sb, String paperid, PaperQuestion paperQuestion, List<DcPaperQuestionVO> dcPaperQuestionVOS) {
        List<TopicTrunk> topicTrunks = paperQuestion.getTOPIC_TRUNK();
        if (CollUtil.isEmpty(topicTrunks)) {
            return;
        }
        TopicTrunk topicTrunk = topicTrunks.get(0);
        // 找答案
        DcPaperQuestionVO matchedDcPaperQuestion = getMatchedDcPaperQuestion(dcPaperQuestionVOS, topicTrunk.getQUESTION_ID());
        if (matchedDcPaperQuestion == null) {
            sb.info("❗❗❗❗❗没有找到正确答案❗❗❗❗❗");
            return;
        }
        // 判断题单独处理
        if (DcPaperQuestionTopic.PAN_DUAN.equals(paperQuestion.getFulltopictypecd())) {
            for (DcQuestionOptionVO r : matchedDcPaperQuestion.getOptions()) {
                String generalanswer = "999";
                if (BooleanUtil.isTrue(r.getIstrue())) {
                    // 正确 999
                } else {
                    // 错误 888
                    generalanswer = "888";
                }
                List<SaveForMCRExammcapiParam> list = CompHomeworkUtil.convert(paperQuestion, paperid, generalanswer);
                doWorkQuestion(list, "999".equals(generalanswer)?"正确":"错误", sb);
            }
        } else {
            List<DcQuestionOptionVO> options = matchedDcPaperQuestion.getOptions().stream().filter(r -> BooleanUtil.isTrue(r.getIstrue())).collect(Collectors.toList());
            List<String> rightOptionIds = options.stream().map(DcQuestionOptionVO::getOptionId).collect(Collectors.toList());
            List<String> rightOptionContents = options.stream().map(DcQuestionOptionVO::getOptionContent).collect(Collectors.toList());
            // 正确答案则提交进度
            List<SaveForMCRExammcapiParam> list = CompHomeworkUtil.convert(paperQuestion, paperid, CollUtil.join(rightOptionIds, StrUtil.COMMA));
            doWorkQuestion(list, CollUtil.join(rightOptionContents, ";"), sb);
        }
    }

    /**
     * 做题
     *
     * @param savemcapiParam
     * @param optionContent
     */
    private void doWorkQuestion(List<SaveForMCRExammcapiParam> savemcapiParam, String optionContent, StringLogger sb) {
        try {
            if (CompHomeworkUtil.saveForMCRExammcapi(loginResultVo, savemcapiParam)) {
                sb.info("✔✔提交答案成功：{}", optionContent);
            } else {
                sb.info("❌❌提交答案失败：{}", optionContent);
            }
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
