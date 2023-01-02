package cc.ussu.modules.dczx.service.impl;

import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.modules.dczx.constants.DczxConstants;
import cc.ussu.modules.dczx.es.mapper.ESDcPaperQuestionMapper;
import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionVO;
import cc.ussu.modules.dczx.mapper.DcPaperQuestionMapper;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
@Service
public class DcPaperQuestionServiceImpl extends ServiceImpl<DcPaperQuestionMapper, DcPaperQuestion> implements IDcPaperQuestionService {

    @Autowired
    private DcPaperQuestionMapper mapper;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;
    @Autowired
    private ESDcPaperQuestionMapper esDcPaperQuestionMapper;

    /**
     * 分页查询
     */
    @Override
    public IPage<DcPaperQuestionVO> findPage(Map param) {
        // 搜索条件
        String query3 = (String) param.get("questionTitle");
        if (StrUtil.isNotBlank(query3)) {
            // 扔掉多余字符匹配
            query3 = query3.replaceAll(" ", "")
                    .replaceAll("\n", "")
                    .replaceAll("\t\n", "")
                    .trim();
            if (query3.startsWith("【")) {
                query3 = query3.replaceAll("[【]\\d[】]", "");
            }
            if (query3.endsWith("。")) {
                query3 = query3.substring(0, query3.length() - 1);
            }
            param.put("questionTitle", query3);
        }
        Page page = MybatisPlusUtil.getPage();
        if (page.getSize()>20) {
            page.setSize(15);
        }
        return this.mapper.findPage(page, param);
    }

    // 通过question获取，走缓存，提高性能
    @Override
    public List<DcPaperQuestionVO> findByQuestionIds(String... questionIds) {
        if (questionIds == null || questionIds.length == 0) {
            return new ArrayList<>();
        }
        List<String> questionIdsList = new ArrayList<>(Arrays.asList(questionIds));
        if (CollUtil.isEmpty(questionIdsList)) {
            return new ArrayList<>();
        }
        if (DictUtil.getValueBoolean("dczx", DczxConstants.PARAM_KEY_REQUEST_RIGHT_OPTION_FROM_ES)) {
            // 走ElasticSearch
            return esDcPaperQuestionMapper.selectBatchIds(questionIdsList);
        } else {
            // 从数据库
            return this.mapper.findWithOptions(questionIdsList);
        }
    }

}
