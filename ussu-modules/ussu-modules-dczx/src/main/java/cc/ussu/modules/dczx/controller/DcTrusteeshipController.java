package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.SelectVO;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.dczx.entity.DcTrusteeship;
import cc.ussu.modules.dczx.entity.vo.DcTrusteeshipVO;
import cc.ussu.modules.dczx.mapper.DcTrusteeshipMapper;
import cc.ussu.modules.dczx.service.IDcTrusteeshipService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 托管列表 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 20:32:44
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-trusteeship")
public class DcTrusteeshipController extends BaseController {

    private static final String SYSTEM_LOG_GROUP = "账号托管";

    @Autowired
    private IDcTrusteeshipService service;
    @Autowired
    private DcTrusteeshipMapper dcTrusteeshipMapper;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-trusteeship:select")
    @GetMapping("/page")
    public Object page(DcTrusteeshipVO p) {
        if (SecurityUtil.isNotSuperAdmin()) {
            p.setCreateBy(SecurityUtil.getLoginUser().getUserId());
        }
        LambdaQueryWrapper<DcTrusteeship> qw = Wrappers.lambdaQuery(DcTrusteeship.class)
                .orderByDesc(DcTrusteeship::getCreateTime)
                .eq(StrUtil.isNotBlank(p.getCreateBy()), DcTrusteeship::getCreateBy, p.getCreateBy())
                .eq(StrUtil.isNotBlank(p.getDisableFlag()), DcTrusteeship::getDisableFlag, p.getDisableFlag())
                .like(StrUtil.isNotBlank(p.getUsername()), DcTrusteeship::getUsername, p.getUsername());
        IPage<DcTrusteeship> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        List<DcTrusteeshipVO> collect = iPage.getRecords().stream().map(r -> BeanUtil.toBean(r, DcTrusteeshipVO.class)).collect(Collectors.toList());
        return MybatisPlusUtil.getResult(iPage.getTotal(), collect);
    }

    /**
     * 获取可选列表
     */
    @PermCheck({"dczx:dc-trusteeship:select", "dczx:dc-task:add", "dczx:dc-task:edit"})
    @GetMapping("/select")
    public JsonResult<List<SelectVO>> getSelectList() {
        LambdaQueryWrapper<DcTrusteeship> qw = Wrappers.lambdaQuery(DcTrusteeship.class)
                .orderByAsc(DcTrusteeship::getDisableFlag)
                .orderByDesc(DcTrusteeship::getCreateTime)
                .eq(SecurityUtil.isNotSuperAdmin(), DcTrusteeship::getCreateBy, SecurityUtil.getLoginUser().getUserId())
                .select(DcTrusteeship::getId, DcTrusteeship::getUsername);
        List<SelectVO> collect = service.list(qw)
                .stream().map(r -> new SelectVO().setLabel(r.getUsername()).setValue(r.getId()).setDisabled(StrConstants.CHAR_TRUE.equals(r.getDisableFlag())))
                .collect(Collectors.toList());
        return JsonResult.ok(collect);
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-trusteeship:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcTrusteeship> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck("dczx:dc-trusteeship:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody DcTrusteeshipVO p) {
        p.setCreateBy(SecurityUtil.getLoginUser().getUserId());
        service.add(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.UPDATE)
    @PermCheck("dczx:dc-trusteeship:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody DcTrusteeshipVO p) {
        Assert.notBlank(p.getId(), "id不能为空");
        p.setCreateBy(SecurityUtil.getLoginUser().getUserId());
        service.edit(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.DELETE)
    @PermCheck("dczx:dc-trusteeship:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.delIds(idList);
        return JsonResult.ok();
    }

}

