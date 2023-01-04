package cc.ussu.modules.sheep.controller;

import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.sheep.entity.JdDayBeans;
import cc.ussu.modules.sheep.service.IJdDayBeansService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 每日京豆收支 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-04-01 11:13:57
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.sheep}/jd-day-beans")
public class JdDayBeansController extends BaseController {

    @Autowired
    private IJdDayBeansService service;

    /**
     * 分页
     */
    @PermCheck("jd:jd-day-beans:select")
    @GetMapping("/page")
    public Object page() {
        LambdaQueryWrapper<JdDayBeans> qw = Wrappers.lambdaQuery(JdDayBeans.class);
        Page<JdDayBeans> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

}

