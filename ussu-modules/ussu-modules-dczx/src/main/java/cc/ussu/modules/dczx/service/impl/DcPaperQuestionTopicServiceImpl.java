package cc.ussu.modules.dczx.service.impl;

import cc.ussu.common.redis.service.RedisService;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.mapper.DcPaperQuestionTopicMapper;
import cc.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import com.baomidou.dynamic.datasource.annotation.Slave;
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
@Slave
@Service
public class DcPaperQuestionTopicServiceImpl extends ServiceImpl<DcPaperQuestionTopicMapper, DcPaperQuestionTopic> implements IDcPaperQuestionTopicService {

    @Autowired
    private DcPaperQuestionTopicMapper mapper;
    @Autowired
    private RedisService redisUtil;

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

}
