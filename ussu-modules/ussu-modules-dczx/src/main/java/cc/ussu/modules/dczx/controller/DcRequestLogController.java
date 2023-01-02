package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.dczx.entity.DcRequestLog;
import cc.ussu.modules.dczx.service.IDcRequestLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 东财接口访问日志 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-03 14:16:34
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-request-log")
public class DcRequestLogController extends BaseController {

    @Autowired
    private IDcRequestLogService service;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-request-log:select")
    @GetMapping("/page")
    public Object page() {
        LambdaQueryWrapper<DcRequestLog> qw = Wrappers.lambdaQuery(DcRequestLog.class)
                .orderByDesc(DcRequestLog::getCreateTime);
        Page<DcRequestLog> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

}

