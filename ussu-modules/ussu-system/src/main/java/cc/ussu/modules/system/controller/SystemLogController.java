package cc.ussu.modules.system.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.modules.system.es.domain.SystemLog;
import cc.ussu.modules.system.es.service.ESSystemLogService;
import cc.ussu.system.api.vo.SystemLogVO;
import cn.easyes.core.biz.EsPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/system-log")
public class SystemLogController extends BaseController {

    @Autowired
    private ESSystemLogService systemLogService;

    /**
     * 查询日志分页
     */
    @GetMapping("/page")
    public JsonResult<PageInfoVO<SystemLog>> page(SystemLog query) {
        EsPageInfo<SystemLog> pageInfo = systemLogService.findPage(query);
        return MybatisPlusUtil.getResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 保存日志
     */
    @PutMapping
    public JsonResult save(@RequestBody SystemLogVO vo) {
        systemLogService.saveLog(vo);
        return JsonResult.ok();
    }

}
