package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.dczx.entity.DcUserInfo;
import cc.ussu.modules.dczx.service.IDcUserInfoService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 学生基本信息 前端控制器
 * </p>
 *
 * @author liming
 * @since 2022-03-07 16:06:24
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-user-info")
public class DcUserInfoController extends BaseController {

    @Autowired
    private IDcUserInfoService service;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-user-info:select")
    @GetMapping("/page")
    public Object page(DcUserInfo p) {
        LambdaQueryWrapper<DcUserInfo> qw = Wrappers.lambdaQuery(DcUserInfo.class)
                .orderByDesc(DcUserInfo::getUpdateTime)
                .orderByDesc(DcUserInfo::getCreateTime)
                .eq(StrUtil.isNotBlank(p.getSex()), DcUserInfo::getSex, p.getSex())
                .like(StrUtil.isNotBlank(p.getUserId()), DcUserInfo::getUserId, p.getUserId())
                .like(StrUtil.isNotBlank(p.getStudentName()), DcUserInfo::getStudentName, p.getStudentName())
                .like(StrUtil.isNotBlank(p.getStuId()), DcUserInfo::getStuId, p.getStuId())
                .like(StrUtil.isNotBlank(p.getEmail()), DcUserInfo::getEmail, p.getEmail())
                .like(StrUtil.isNotBlank(p.getMobile()), DcUserInfo::getMobile, p.getMobile())
                .like(StrUtil.isNotBlank(p.getFdzName()), DcUserInfo::getFdzName, p.getFdzName())
                .like(StrUtil.isNotBlank(p.getRxLevel()), DcUserInfo::getRxLevel, p.getRxLevel())
                .like(StrUtil.isNotBlank(p.getCardId()), DcUserInfo::getCardId, p.getCardId());
        Page<DcUserInfo> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-user-info:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcUserInfo> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 删除
     */
    @PermCheck("dczx:dc-user-info:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

