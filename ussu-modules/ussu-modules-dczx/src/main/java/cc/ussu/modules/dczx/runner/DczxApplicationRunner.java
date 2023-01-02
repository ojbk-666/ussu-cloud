package cc.ussu.modules.dczx.runner;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcPaper;
import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import cc.ussu.modules.dczx.service.IDcPaperService;
import cc.ussu.modules.dczx.util.DczxUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DczxApplicationRunner implements ApplicationRunner {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IDcPaperQuestionService questionService;
    @Autowired
    private IDcCourseService courseService;
    @Autowired
    private IDcPaperService paperService;
    @Autowired
    private IDcPaperQuestionTopicService topicService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 加载到redis
        // DynamicDataSourceContextHolder.push(DczxUtil.SLAVE);
        loadQuestionId();
        loadCourse();
        loadPaper();
        loadTopic();
        // DynamicDataSourceContextHolder.clear();
    }

    /**
     * 加载题目id缓存
     */
    private void loadQuestionId() {
        redisService.deleteObject(DczxUtil.QUESTION_ID_KEY_IN_REDIS);
        List<DcPaperQuestion> list = questionService.list(Wrappers.lambdaQuery(DcPaperQuestion.class).select(DcPaperQuestion::getQuestionId));
        List<String> questionIdList = list.stream().map(DcPaperQuestion::getQuestionId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(questionIdList)) {
            redisService.setCacheSet(DczxUtil.QUESTION_ID_KEY_IN_REDIS, CollUtil.newHashSet(questionIdList));
        }
    }

    /**
     * 加载课程信息
     */
    private void loadCourse() {
        String key = DczxUtil.COURSE_KEY_IN_REDIS;
        redisService.deleteObject(key);
        List<DcCourse> list = courseService.list();
        for (DcCourse item : list) {
            redisService.setCacheMapValue(key, item.getCourseId(), item);
        }
    }

    /**
     * 加载试卷信息
     */
    private void loadPaper() {
        String key = DczxUtil.PAPER_KEY_IN_REDIS;
        redisService.deleteObject(key);
        List<DcPaper> list = paperService.list();
        for (DcPaper item : list) {
            redisService.setCacheMapValue(key, item.getPaperId(), item);
        }
    }

    /**
     * 题型
     */
    private void loadTopic() {
        String key = DczxUtil.TOPIC_KEY_IN_REDIS;
        redisService.deleteObject(key);
        List<DcPaperQuestionTopic> list = topicService.list();
        for (DcPaperQuestionTopic item : list) {
            redisService.setCacheMapValue(key, item.getFullTopicTypeCd(), item);
        }
    }

}
