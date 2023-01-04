package cc.ussu.modules.system.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.system.entity.SysLoginLog;
import cc.ussu.modules.system.service.ISysLoginLogService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户会话记录/登录日志 表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2022-01-01 17:31:29
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-login-log")
public class SysLoginLogController extends BaseController {

    private static final String PERM_PREFIX = "system:login-log:";

    @Autowired
    private ISysLoginLogService service;

    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public Object page(SysLoginLog p) {
        LambdaQueryWrapper<SysLoginLog> qw = Wrappers.lambdaQuery(SysLoginLog.class)
            .orderByDesc(SysLoginLog::getCreateTime)
            .like(StrUtil.isNotBlank(p.getLoginIp()), SysLoginLog::getLoginIp, p.getLoginIp())
            .eq(StrUtil.isNotBlank(p.getAccount()), SysLoginLog::getAccount, p.getAccount());
        IPage<SysLoginLog> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(iPage);
    }

    // @PermCheck(PERM_PREFIX + DELETE)
    // @DeleteMapping("/{ids}")
    public JsonResult delete(@PathVariable String ids) {
        List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

