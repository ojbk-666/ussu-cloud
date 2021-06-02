package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.modules.dczx.entity.DcPaperQuestionTopic;
import cn.ussu.modules.dczx.model.param.DcPaperQuestionTopicParam;
import cn.ussu.modules.dczx.service.IDcPaperQuestionTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/paper-question-topic")
public class DcPaperQuestionTopicController extends BaseAdminController {

    @Autowired
    private IDcPaperQuestionTopicService service;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(@RequestParam DcPaperQuestionTopicParam param) {
        return service.findPage(param);
    }

    /**
     * 新增
     */
    @PutMapping
    public Object add(DcPaperQuestionTopic obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public Object edit(DcPaperQuestionTopic obj) {
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

}
