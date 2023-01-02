package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.dczx.entity.DcPaperQuestion;
import cc.ussu.modules.dczx.service.IDcPaperQuestionService;
import cc.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 题目 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-paper-question")
public class DcPaperQuestionController extends BaseController {

    @Autowired
    private IDcPaperQuestionService service;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;

    /*
     * 分页查询
     */
    @GetMapping("/page")
    public Object list(@RequestParam Map param, DcPaperQuestion question) {
        IPage iPage = service.findPage(param);
        return MybatisPlusUtil.getResult(iPage);
    }

    // @Autowired
    // private DcPaperQuestionSearch dcPaperQuestionSearch;

    @RequestMapping("/refresh")
    public Object refresh() {
        // int i = dcPaperQuestionSearch.refresh2ElasticSearch();
        int i = 0;
        return JsonResult.ok(null,"刷新成功，更新数据 "+i+" 条");
    }

    @GetMapping("/searchEs")
    public Object searchEs(String query_questionTitle, Integer page, Integer limit) {
        // LayuiPageInfo pageInfo = dcPaperQuestionSearch.searchEs(query_questionTitle, page, limit);
        return null;
    }

}
