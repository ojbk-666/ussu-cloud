package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.modules.dczx.entity.DcPaper;
import cc.ussu.modules.dczx.service.IDcPaperService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * paper 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-paper")
public class DcPaperController extends BaseController {

    @Autowired
    private IDcPaperService service;

    /*
     * 分页查询
     */
    @GetMapping("/page")
    public Object list(DcPaper p) {
        LambdaQueryWrapper<DcPaper> qw = Wrappers.lambdaQuery(DcPaper.class);
        qw.like(StrUtil.isNotBlank(p.getPaperName()), DcPaper::getPaperName, p.getPaperName());
        Page page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

}
