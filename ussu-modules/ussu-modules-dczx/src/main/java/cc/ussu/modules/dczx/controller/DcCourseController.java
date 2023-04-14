package cc.ussu.modules.dczx.controller;

import cc.ussu.common.core.util.SelectVOUtil;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.vo.SelectVO;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.modules.dczx.entity.DcCourse;
import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.entity.vo.DcCourseVO;
import cc.ussu.modules.dczx.service.IDcCourseService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * course 控制器
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@Slf4j
@RestController
@RequestMapping("${ussu.mapping-prefix.dczx}/dc-course")
public class DcCourseController extends BaseController {

    @Autowired
    private IDcCourseService service;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public JsonResult<PageInfoVO<DcCourseVO>> list(DcCourse p) {
        LambdaQueryWrapper<DcCourse> qw = Wrappers.lambdaQuery(DcCourse.class);
        qw.eq(StrUtil.isNotBlank(p.getCourseId()), DcCourse::getCourseId, p.getCourseId())
                .like(StrUtil.isNotBlank(p.getCourseName()), DcCourse::getCourseName, p.getCourseName());
        IPage<DcCourse> iPage = service.page(MybatisPlusUtil.getPage(), qw);
        List<DcCourseVO> voList = iPage.getRecords().stream().map(r -> BeanUtil.toBean(r, DcCourseVO.class)).collect(Collectors.toList());
        return MybatisPlusUtil.getResult(iPage.getTotal(), voList);
    }

    /**
     * 获取可选列表
     */
    @GetMapping("/select")
    public JsonResult<List<SelectVO>> getSelectList() {
        return JsonResult.ok(SelectVOUtil.render(service.list(), DcCourse::getCourseName, DcCourse::getCourseId));
    }

    /**
     * 获取所有
     */
    @GetMapping("/all")
    public JsonResult all() {
        return JsonResult.ok(service.list());
    }

    /**
     * 接口新增
     */
    @PutMapping("/addi")
    public JsonResult addByInterface(DcInterfaceLog dcInterfaceLog) {
        // new SaveDczxCourseThread(dcInterfaceLog).start();
        return JsonResult.ok();
    }

}
