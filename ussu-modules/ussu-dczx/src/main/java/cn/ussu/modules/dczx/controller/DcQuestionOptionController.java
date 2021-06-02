package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.modules.dczx.entity.DcQuestionOption;
import cn.ussu.modules.dczx.model.param.DcQuestionOptionParam;
import cn.ussu.modules.dczx.service.IDcQuestionOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 题目的选项 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/question-option")
public class DcQuestionOptionController extends BaseAdminController {

    @Autowired
    private IDcQuestionOptionService service;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(@RequestParam DcQuestionOptionParam param) {
        return service.findPage(param);
    }

    /**
     * 新增
     */
    @PutMapping
    public Object add(DcQuestionOption obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public Object edit(DcQuestionOption obj) {
        checkReqParamThrowException(obj.getId());
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

    // @Autowired
    // private DcPaperQuestionOptionSearch dcPaperQuestionOptionSearch;

    @RequestMapping("/refresh")
    public Object refresh() {
        // int i = dcPaperQuestionOptionSearch.refresh2ElasticSearch();
        int i = 0;
        return JsonResult.ok("刷新成功，更新数据 " + i + " 条");
    }

}
