package cn.ussu.modules.dczx.controller;

import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.dczx.mapper.DcPaperQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计
 */
@RestController
@RequestMapping("/statistics")
public class DcStatisticsController extends BaseAdminController {

    @Autowired
    private DcPaperQuestionMapper dcPaperQuestionMapper;

    @GetMapping("/courseNum")
    public JsonResult countCourseNum() {
        return JsonResult.ok().data(dcPaperQuestionMapper.countCourseNum());
    }

    @GetMapping("/questionNumByUserid")
    public JsonResult questionNumByUserid() {
        return JsonResult.ok().data(dcPaperQuestionMapper.countQuestionNumByUserid());
    }

}
