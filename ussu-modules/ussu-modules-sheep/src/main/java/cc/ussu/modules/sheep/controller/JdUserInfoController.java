package cc.ussu.modules.sheep.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.sheep.entity.JdUserInfo;
import cc.ussu.modules.sheep.service.IJdUserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 京东用户信息 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-02 13:55:06
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.sheep}/jd-user-info")
public class JdUserInfoController extends BaseController {

    @Autowired
    private IJdUserInfoService service;

    /**
     * 分页
     */
    @PermCheck("jd:jd-user-info:select")
    @GetMapping("/page")
    public JsonResult<PageInfoVO<JdUserInfo>> page() {
        LambdaQueryWrapper<JdUserInfo> qw = Wrappers.lambdaQuery(JdUserInfo.class);
        Page<JdUserInfo> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

}

