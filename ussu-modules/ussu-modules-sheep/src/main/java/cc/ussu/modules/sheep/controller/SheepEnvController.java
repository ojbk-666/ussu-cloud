package cc.ussu.modules.sheep.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.modules.sheep.entity.SheepEnv;
import cc.ussu.modules.sheep.service.ISheepEnvService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 环境变量 前端控制器
 * </p>
 *
 * @author mp-generator
 * @since 2022-10-16 09:40:01
 */
@RestController
@RequestMapping("${ussu.mapping-prefix.sheep}/sheep-env")
public class SheepEnvController extends BaseController {

    @Autowired
    private ISheepEnvService service;

    /**
     * 分页
     */
    @PermCheck("sheep:sheep-env:select")
    @GetMapping("/page")
    public Object page(SheepEnv query) {
        String keyword = query.getName();
        LambdaQueryWrapper<SheepEnv> qw = Wrappers.lambdaQuery(SheepEnv.class)
            .orderByAsc(SheepEnv::getDisabled, SheepEnv::getName)
            .like(StrUtil.isNotBlank(keyword), SheepEnv::getName, keyword)
            .or(StrUtil.isNotBlank(keyword)).like(StrUtil.isNotBlank(keyword), SheepEnv::getValue, keyword)
            .or(StrUtil.isNotBlank(keyword)).like(StrUtil.isNotBlank(keyword), SheepEnv::getRemarks, keyword);
        Page<SheepEnv> page = service.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(page);
    }

    /**
     * 单个详情
     */
    @PermCheck("sheep:sheep-env:select")
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SheepEnv> detail(@PathVariable String id) {
        return JsonResult.ok(service.getById(id));
    }

    /**
     * 禁用
     */
    @PostMapping("/disable/{id}")
    public JsonResult disable(@PathVariable String id) {
        service.disable(id);
        return JsonResult.ok();
    }

    /**
     * 启用
     */
    @PostMapping("/enable/{id}")
    public JsonResult enable(@PathVariable String id) {
        service.enable(id);
        return JsonResult.ok();
    }

    /**
     * 添加
     */
    @PermCheck("sheep:sheep-env:add")
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SheepEnv p) {
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.save(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @PermCheck("sheep:sheep-env:edit")
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SheepEnv p) {
        Assert.notBlank(p.getId(), "id不能为空");
        // Assert.notBlank(p.getName(), "名称不能为空");
        service.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @PermCheck("sheep:sheep-env:delete")
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        service.removeByIds(idList);
        return JsonResult.ok();
    }

    /**
     * 通过json导入
     */
    @PostMapping("/import-json")
    public JsonResult importFromJson(@RequestBody List<SheepEnv> list) {
        if (CollUtil.isNotEmpty(list)) {
            for (SheepEnv env : list) {
                if (StrUtil.isBlank(env.getName()) || StrUtil.isBlank(env.getValue())) {
                    throw new IllegalArgumentException("name或value不能为空");
                }
                env.setId(null).setCreateTime(null).setUpdateTime(null);
            }
            service.saveBatch(list);
        }
        return JsonResult.ok();
    }

}

