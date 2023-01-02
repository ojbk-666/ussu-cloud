package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.dczx.entity.DcQuestionOption;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 题目的选项 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-question-option")
public class DcQuestionOptionController extends BaseController {

    @Autowired
    private IDcQuestionOptionService service;

    /*
     * 分页查询
     */
    @GetMapping("/page")
    public Object list(DcQuestionOption p) {
        LambdaQueryWrapper<DcQuestionOption> qw = Wrappers.lambdaQuery(DcQuestionOption.class)
                .orderByDesc(DcQuestionOption::getOptionId)
                .like(StrUtil.isNotBlank(p.getOptionContent()), DcQuestionOption::getOptionContent, p.getOptionContent());
        IPage iPage = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(iPage);
    }

    @RequestMapping("/refresh")
    public Object refresh() {
        // int i = dcPaperQuestionOptionSearch.refresh2ElasticSearch();
        int i = 0;
        return JsonResult.ok(null,"刷新成功，更新数据 "+i+" 条");
    }

}
