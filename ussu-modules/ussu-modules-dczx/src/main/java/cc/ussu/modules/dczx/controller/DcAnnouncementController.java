package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.dczx.entity.DcAnnouncement;
import cc.ussu.modules.dczx.service.IDcAnnouncementService;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 东财在线插件公告 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-11-22 15:12:34
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-announcement")
public class DcAnnouncementController extends BaseController {

    private static final String SYSTEM_LOG_GROUP = "东财在线插件公告";

    @Autowired
    private IDcAnnouncementService service;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-announcement:select")
    @GetMapping("/page")
    public JsonResult<PageInfoVO<DcAnnouncement>> page() {
        LambdaQueryWrapper<DcAnnouncement> qw = Wrappers.lambdaQuery(DcAnnouncement.class);
        Page<DcAnnouncement> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-announcement:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcAnnouncement> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 添加
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.INSERT)
    @PermCheck("dczx:dc-announcement:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody DcAnnouncement p) {
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.UPDATE)
    @PermCheck("dczx:dc-announcement:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody DcAnnouncement p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(group = SYSTEM_LOG_GROUP, name = SystemLogConstants.DELETE)
    @PermCheck("dczx:dc-announcement:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

