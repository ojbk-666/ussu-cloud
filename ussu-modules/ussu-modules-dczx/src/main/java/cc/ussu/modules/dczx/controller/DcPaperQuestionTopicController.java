package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cc.ussu.modules.dczx.entity.vo.DcPaperQuestionTopicVO;
import cc.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/question-topic")
public class DcPaperQuestionTopicController extends BaseController {

    @Autowired
    private IDcPaperQuestionTopicService service;

    /*
     * 分页查询
     */
    @GetMapping("/page")
    public Object list(DcPaperQuestionTopic p) {
        LambdaQueryWrapper<DcPaperQuestionTopic> qw = Wrappers.lambdaQuery(DcPaperQuestionTopic.class).orderByDesc(DcPaperQuestionTopic::getTopicId)
                .like(StrUtil.isNotBlank(p.getQuestionTypeNm()), DcPaperQuestionTopic::getQuestionTypeNm, p.getQuestionTypeNm())
                .like(StrUtil.isNotBlank(p.getTopicTypeCd()), DcPaperQuestionTopic::getTopicTypeCd, p.getTopicTypeCd())
                .like(StrUtil.isNotBlank(p.getFullTopicTypeCd()), DcPaperQuestionTopic::getFullTopicTypeCd, p.getFullTopicTypeCd());
        IPage<DcPaperQuestionTopic> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        List<DcPaperQuestionTopicVO> voList = iPage.getRecords().stream().map(r -> BeanUtil.toBean(r, DcPaperQuestionTopicVO.class)).collect(Collectors.toList());
        return MybatisPlusUtil.getResult(iPage.getTotal(), voList);
    }

}
