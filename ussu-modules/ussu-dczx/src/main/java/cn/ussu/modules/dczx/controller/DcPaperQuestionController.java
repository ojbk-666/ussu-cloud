package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.entity.DcPaperQuestion;
import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.model.param.DcPaperQuestionParam;
import cn.ussu.modules.dczx.service.IDcPaperQuestionService;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 题目 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/paper-question")
public class DcPaperQuestionController extends BaseAdminController {

    @Autowired
    private IDcPaperQuestionService service;
    @Autowired
    private IDcQuestionOptionService dcQuestionOptionService;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(DcPaperQuestionParam param) {
        return JsonResult.ok().data(service.findPage(param));
    }

    /**
     * 新增
     */
    /*@PutMapping
    @PreAuthorize("@pc.check('dc-paper-quertion:edit')")
    public Object add(DcPaperQuestion obj) {
        obj.insert();
        return JsonResult.ok();
    }*/
    @PutMapping
    public Object add(DcInterfaceLog dcInterfaceLog) throws Exception {
        // SaveDczxPaperQuestionThread t = new SaveDczxPaperQuestionThread(dcInterfaceLog);
        // t.start();
        // t.join();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public Object edit(DcPaperQuestion obj) {
        checkReqParamThrowException(obj.getId());
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        checkReqParamThrowException(id);
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        // 删除选项
        for (String delId : idList) {
            QueryWrapper<DcQuestionOption> qw = new QueryWrapper<>();
            qw.eq("question_id_long", delId);
            dcQuestionOptionService.remove(qw);
        }
        return JsonResult.ok();
    }

    // @Autowired
    // private DcPaperQuestionSearch dcPaperQuestionSearch;

    @RequestMapping("/refresh")
    public Object refresh() {
        // int i = dcPaperQuestionSearch.refresh2ElasticSearch();
        int i = 0;
        return JsonResult.ok("刷新成功，更新数据 " + i + " 条");
    }

    @GetMapping("/searchEs")
    public Object searchEs(String query_questionTitle, Integer page, Integer limit) {
        // LayuiPageInfo pageInfo = dcPaperQuestionSearch.searchEs(query_questionTitle, page, limit);
        // return pageInfo;
        return null;
    }

}
