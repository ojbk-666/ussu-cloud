package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.model.param.DcInterfaceLogParam;
import cn.ussu.modules.dczx.service.IDcInterfaceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 接口日志 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/interface-log")
public class DcInterfaceLogController extends BaseAdminController {

    @Autowired
    private IDcInterfaceLogService service;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(@RequestParam DcInterfaceLogParam param) {
        return service.findPage(param);
    }

    /**
     * 新增
     */
    @PutMapping
    // @PermCheck("dczx:interfacelog:add")
    public Object add(DcInterfaceLog obj) {
        obj.setCreateBy(SecurityUtils.getUserId());
        // todo new SaveDczxPaperQuestionThread(obj).start();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    // @PermCheck("dczx:interfacelog:edit")
    public Object edit(DcInterfaceLog obj) {
        checkReqParamThrowException(obj.getId());
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    // @PermCheck("dczx:interfacelog:edit")
    public Object delete(@PathVariable("id") String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}
