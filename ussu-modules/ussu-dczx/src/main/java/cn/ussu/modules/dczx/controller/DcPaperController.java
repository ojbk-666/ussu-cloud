package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.dczx.entity.DcPaper;
import cn.ussu.modules.dczx.model.param.DcPaperParam;
import cn.ussu.modules.dczx.service.IDcPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * paper 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/paper")
public class DcPaperController extends BaseAdminController {

    @Autowired
    private IDcPaperService service;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(DcPaperParam param) {
        return JsonResult.ok().data(service.findPage(param));
    }

    /**
     * 新增
     */
    @PutMapping
    public Object add(DcPaper obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    public Object edit(DcPaper obj) {
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
