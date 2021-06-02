package cn.ussu.modules.dczx.service.impl;

import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cn.ussu.modules.dczx.mapper.DcPaperQuestionTopicMapper;
import cn.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Service
public class DcPaperQuestionTopicServiceImpl extends ServiceImpl<DcPaperQuestionTopicMapper, DcPaperQuestionTopic> implements IDcPaperQuestionTopicService {

    @Autowired
    private DcPaperQuestionTopicMapper mapper;
    @Autowired
    private RedisService redisUtil;

    /**
     * 分页查询
     */
    /*@Override
    public LayuiPageInfo findPage(Map param) {
        QueryWrapper<DcPaperQuestionTopic> qw = new QueryWrapper<>();
        qw.orderByDesc(StrConstants.DB_create_time);
        // 搜索条件
        IPage iPage = this.mapper.selectPage(LayuiPageFactory.defaultPage(), qw);
        return LayuiPageFactory.createPageInfo(iPage);
    }*/

    /**
     * 通过topic_type_id查询，会缓存
     * @param topicTypeId
     * @return
     */
    @Override
    public List<DcPaperQuestionTopic> findByTopicTypeIds(String... topicTypeId) {
        if (topicTypeId == null || topicTypeId.length == 0) {
            return new ArrayList<DcPaperQuestionTopic>();
        }
        List topicTypeIdList = new ArrayList<>(Arrays.asList(topicTypeId));
        List<DcPaperQuestionTopic> list = new ArrayList<>();
        // System.out.println("总共 " + questionIdsList.size() + " 个");
        // 从缓存读取已有数据
        for (String ttid : topicTypeId) {
            DcPaperQuestionTopic topic = (DcPaperQuestionTopic) redisUtil.getCacheObject(IDcPaperQuestionTopicService.DC_PAPER_QUESTION_TOPIC_ + ttid);
            if (topic != null) {
                list.add(topic);
                topicTypeIdList.remove(ttid);
            }
        }
        // 从数据库读取剩余数据并缓存
        // System.out.println("从数据库加载其余 " + questionIdsList.size() + " 个");
        if (!topicTypeIdList.isEmpty()) {
            List<DcPaperQuestionTopic> list1 = this.mapper.findByTopicTypeIds(topicTypeIdList);
            list.addAll(list1);
            for (DcPaperQuestionTopic obj : list1) {
                redisUtil.setCacheObject(IDcPaperQuestionTopicService.DC_PAPER_QUESTION_TOPIC_ + obj.getTopicTypeId(), obj, 1800L);  //缓存30分钟
            }
        }
        return list;
    }



    /*@Override
    @Transactional
    public void addOne(DcPaperQuestionTopic obj) {
        this.mapper.insert(obj);
    }*/

    /*@Override
    @Transactional
    public void updateOne(DcPaperQuestionTopic obj) {
        this.mapper.updateById(obj);
    }*/

}
