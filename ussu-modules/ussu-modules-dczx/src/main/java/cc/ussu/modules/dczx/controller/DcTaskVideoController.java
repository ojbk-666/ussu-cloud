package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.PageHelperUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.entity.vo.DcTaskVideoQueryVO;
import cc.ussu.modules.dczx.entity.vo.DcTaskVideoVO;
import cc.ussu.modules.dczx.mapper.DcTaskVideoMapper;
import cc.ussu.modules.dczx.service.IDcTaskVideoService;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 视频信息 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-02 14:01:34
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-task-video")
public class DcTaskVideoController extends BaseController {

    @Autowired
    private IDcTaskVideoService service;
    @Autowired
    private DcTaskVideoMapper taskVideoMapper;

    /**
     * 分页
     */
    @PermCheck("dczx:dc-task-video:select")
    @GetMapping("/page")
    public JsonResult page(DcTaskVideoQueryVO p) {
        PageHelperUtil.startPage();
        List<DcTaskVideoVO> page = taskVideoMapper.findPage(p);
        return PageHelperUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck("dczx:dc-task-video:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<DcTaskVideo> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 更新视频长度
     */
    @PermCheck("dczx:dc-task-video:update-duration")
    @PostMapping("/update-duration")
    public JsonResult updateDuration(@RequestBody DcTaskVideo p) {
        Assert.notBlank(p.getVid());
        Assert.notNull(p.getDuration());
        DcTaskVideo u = DcTaskVideo.builder().vid(p.getVid()).duration(p.getDuration()).build();
        service.updateById(u);
        return JsonResult.ok();
    }

    /**
     * 添加
     */
    @PermCheck("dczx:dc-task-video:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody DcTaskVideo p) {
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PermCheck("dczx:dc-task-video:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody DcTaskVideo p) {
        Assert.notBlank(p.getVid(), "id不能为空");
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @PermCheck("dczx:dc-task-video:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

}

