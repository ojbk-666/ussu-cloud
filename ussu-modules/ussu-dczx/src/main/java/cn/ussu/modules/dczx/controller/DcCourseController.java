package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.annotation.PermCheck;
import cn.ussu.modules.dczx.entity.DcCourse;
import cn.ussu.modules.dczx.entity.DcInterfaceLog;
import cn.ussu.modules.dczx.model.param.DcCourseParam;
import cn.ussu.modules.dczx.service.IDcCourseService;
import cn.ussu.modules.dczx.thread.SaveDczxCourseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * course 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@RestController
@RequestMapping("/course")
public class DcCourseController extends BaseAdminController {

    @Autowired
    private IDcCourseService service;

    /*
     * 分页查询
     */
    @GetMapping
    public Object list(@RequestParam DcCourseParam param) {
        return service.findPage(param);
    }

    /**
     * 接口新增门户
     */
    @PutMapping("/addi")
    public JsonResult addByInterface(DcInterfaceLog dcInterfaceLog) {
        new SaveDczxCourseThread(dcInterfaceLog).start();
        return JsonResult.ok();
    }

    /**
     * 新增
     */
    @PutMapping
    // @PermCheck("dczx:course:add")
    public Object add(DcCourse obj) {
        obj.insert();
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PostMapping
    // @PermCheck("dczx:course:edit")
    public Object edit(DcCourse obj) {
        checkReqParamThrowException(obj.getId());
        obj.updateById();
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PermCheck("dczx:course:delete")
    public Object delete(@PathVariable("id") String id) {
        List<String> idList = splitCommaList(id, true);
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}
