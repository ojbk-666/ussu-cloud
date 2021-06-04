package cn.ussu.modules.dczx.service.impl;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import cn.ussu.modules.dczx.mapper.DcPaperQuestionMapper;
import cn.ussu.modules.dczx.model.param.DcPaperQuestionParam;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @Override
    public ReturnPageInfo<DcPaperQuestion> findPage(DcPaperQuestionParam param) {
        // LambdaQueryWrapper<DcPaperQuestion> qw = new LambdaQueryWrapper<>();
        // qw.orderByDesc(DcPaperQuestion::getCreateTime)
        //         .eq(StrUtil.isNotBlank(param.getQuestionId()), DcPaperQuestion::getQuestionId, param.getQuestionId())
        //         .like(StrUtil.isNotBlank(param.getQuestionTitle()), DcPaperQuestion::getQuestionTitle, param.getQuestionTitle());
        IPage iPage = this.mapper.findPage(DefaultPageFactory.getPage(), param);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }

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
        /*List<DcQuestionOption> options = question.getOptions();
        for (DcQuestionOption option : options) {
            option.setQuestionIdLong(question.getId());
        }*/
        // boolean b = dcQuestionOptionService.saveBatch(options);
        // return insert > 0 && b;
        return true;
    }

    /*@Override
    @Transactional
    public void updateOne(DcPaperQuertion obj) {
        this.mapper.updateById(obj);
    }*/

}
