package cn.ussu.modules.dczx.service.impl;

import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.mapper.DcPaperQuestionMapper;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Service
public class DcPaperQuestionServiceImpl extends ServiceImpl<DcPaperQuestionMapper, DcPaperQuestion> implements IDcPaperQuestionService {

    @Autowired
    private DcPaperQuestionMapper mapper;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;
    @Autowired
    private RedisService redisUtil;

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcPaperQuestion> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        String query2 = (String) param.get("query_questionId");
        qw.eq(StrUtil.isNotBlank(query2), "question_id", query2);
        String query3 = (String) param.get("query_questionTitle");
        String queryCourseIdStr = (String) param.get("query_courseIdStr");
        if (StrUtil.isNotBlank(query3)) {
            // 扔掉多余字符匹配
            query3 = query3.replaceAll(" ", "")
                    .replaceAll("\n", "")
                    .replaceAll("\t\n", "")
                    .trim();
            if (query3.startsWith("【")) {
                query3 = query3.replaceAll("[【]\\d[】]", "");
            }
            if (query3.endsWith("。")) query3 = query3.substring(0, query3.length() - 1);
            qw.like("question_title", query3);
            param.put("query_questionTitle", query3);
        }
        String query4 = (String) param.get("query_topictrunkType");
        qw.eq(StrUtil.isNotBlank(query4), "topictrunk_type", query4);
        // qw.eq(StrUtil.isNotBlank(queryCourseIdStr),"course_id_str", queryCourseIdStr);
        // IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        IPage iPage = this.mapper.findPage(LayuiPageFactory.defaultPage(), param);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

    // 通过question获取，走缓存，提高性能
    @Override
    public List<DcPaperQuestion> findByQuestionIds(String... questionIds) {
        if (questionIds == null || questionIds.length == 0) {
            return new ArrayList<DcPaperQuestion>();
        }
        List<String> questionIdsList = new ArrayList<>(Arrays.asList(questionIds));
        List<DcPaperQuestion> list = new ArrayList<>();
        // System.out.println("总共 " + questionIdsList.size() + " 个");
        // 从缓存读取已有数据
        for (String questionId : questionIds) {
            DcPaperQuestion question = (DcPaperQuestion) redisUtil.getCacheObject(IDcPaperQuestionService.DC_PAPER_QUESTION_ + questionId);
            if (question != null) {
                list.add(question);
                questionIdsList.remove(questionId);
            }
        }
        // 从数据库读取剩余数据并缓存
        // System.out.println("从数据库加载其余 " + questionIdsList.size() + " 个");
        if (!questionIdsList.isEmpty()) {
            List<DcPaperQuestion> list1 = this.mapper.findByQuestionIds(questionIdsList);
            list.addAll(list1);
            for (DcPaperQuestion dcPaperQuestion : list1) {
                redisUtil.setCacheObject(IDcPaperQuestionService.DC_PAPER_QUESTION_ + dcPaperQuestion.getQuestionId(), dcPaperQuestion, 1800L);  //缓存30分钟
            }
        }
        return list;
    }

    /**
     * 新增
     */
    @Override
    @Transactional
    public boolean addOne(DcPaperQuestion question) {
        // 来源为手动录入
        question.setSource(20);
        int insert = this.mapper.insert(question);
        // 写入选项
        List<DcQuestionOption> options = question.getOptions();
        for (DcQuestionOption option : options) {
            option.setQuestionIdLong(question.getId());
        }
        boolean b = dcQuestionOptionService.saveBatch(options);
        return insert > 0 && b;
    }

    /*@Override
    @Transactional
    public void updateOne(DcPaperQuertion obj) {
        this.mapper.updateById(obj);
    }*/

}
