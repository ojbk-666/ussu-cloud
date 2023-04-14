package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.datasource.util.MybatisPlusUtil;
import cc.ussu.common.datasource.vo.PageInfoVO;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.log.constants.SystemLogConstants;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysNotice;
import cc.ussu.modules.system.entity.SysNoticeContent;
import cc.ussu.modules.system.entity.vo.SysNoticeVO;
import cc.ussu.modules.system.service.ISysNoticeContentService;
import cc.ussu.modules.system.service.ISysNoticeService;
import cc.ussu.system.api.vo.SendNoticeVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知公告表 前端控制器
 * </p>
 *
 * @author liming
 * @since 2022-01-18 15:31:26
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "通知公告管理")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/sys-notice")
public class SysNoticeController extends BaseController {

    private static final String PERM_PREFIX = "system:notice:";

    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private ISysNoticeContentService noticeContentService;

    /**
     * 获取个人消息分页
     */
    @GetMapping("/notice")
    public JsonResult<Map<String, Object>> noticePage(SysNotice p) {
        String userId = SecurityUtil.getLoginUser().getUserId();
        LambdaQueryWrapper<SysNotice> qw = Wrappers.lambdaQuery(SysNotice.class)
                .orderByDesc(SysNotice::getCreateTime)
                .eq(StrUtil.isNotBlank(p.getNoticeType()), SysNotice::getNoticeType, p.getNoticeType())
                .like(StrUtil.isNotBlank(p.getNoticeTitle()), SysNotice::getNoticeTitle, p.getNoticeTitle())
                .eq(p.getReadFlag() != null, SysNotice::getReadFlag, p.getReadFlag())
                .and(w -> w.and(w2 -> w2.eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_ANNOUNCEMENT)
                                .leSql(SysNotice::getStartTime, "now()").geSql(SysNotice::getEndTime, "now()"))
                        .or(w2 -> w2.eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_NOTICE)
                                .eq(SysNotice::getUserId, userId)));
        IPage<SysNotice> page = noticeService.page(MybatisPlusUtil.getPage(), qw);
        // Map<String, Object> result = new HashMap<>();
        PageInfoVO<SysNotice> pageInfoVO = new PageInfoVO<>();
        pageInfoVO.setTotal(page.getTotal()).setList(page.getRecords());
        // result.put(MybatisPlusUtil.TOTAL, page.getTotal());
        // result.put(MybatisPlusUtil.LIST, page.getRecords());
        // 未读
        Map<String, Long> unRead = new HashMap<>();
        long cn = noticeService.countUnReadNotice(userId);
        unRead.put("1", cn);
        long ca = noticeService.countUnReadAnnouncement();
        unRead.put("2", ca);
        unRead.put("all", cn + ca);
        Map<String, Object> map = new HashMap<>();
        map.put("result", pageInfoVO);
        map.put("unRead", unRead);
        return JsonResult.ok(map);
    }

    /**
     * 维护分页
     */
    @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping("/page")
    public JsonResult<PageInfoVO<SysNotice>> page(SysNotice p) {
        LambdaQueryWrapper<SysNotice> qw = Wrappers.lambdaQuery(SysNotice.class)
                .orderByDesc(SysNotice::getCreateTime)
                .eq(StrUtil.isNotBlank(p.getNoticeType()), SysNotice::getNoticeType, p.getNoticeType())
                .like(StrUtil.isNotBlank(p.getNoticeTitle()), SysNotice::getNoticeTitle, p.getNoticeTitle())
                .eq(p.getReadFlag() != null, SysNotice::getReadFlag, p.getReadFlag());
        IPage<SysNotice> iPage = noticeService.page(MybatisPlusUtil.getPage(), qw);
        return MybatisPlusUtil.getResult(iPage);
    }

    /**
     * 单个详情
     */
    // @PermCheck(PERM_PREFIX + SELECT)
    @GetMapping({"/{id}", "/detail/{id}"})
    public JsonResult<SysNoticeVO> detail(@PathVariable String id) {
        SysNotice notice = noticeService.getById(id);
        Assert.notNull(notice, "没有找到该条数据");
        if (SysNotice.NOTICE_TYPE_NOTICE.equals(notice.getNoticeType()) && SecurityUtil.isNotSuperAdmin()) {
            Assert.equals(SecurityUtil.getLoginUser().getUserId(), notice.getUserId(), "没有找到该条数据");
        }
        SysNoticeVO noticeVO = BeanUtil.toBean(notice, SysNoticeVO.class);
        SysNoticeContent noticeContent = noticeContentService.getById(notice.getNoticeContentId());
        if (noticeContent != null) {
            noticeVO.setNoticeContent(noticeContent.getNoticeContent());
        }
        if (SysNotice.NOTICE_TYPE_ANNOUNCEMENT.equals(noticeVO.getNoticeType())) {
            noticeVO.setStartEndTime(new Date[]{noticeVO.getStartTime(), noticeVO.getEndTime()});
        }
        return JsonResult.ok(noticeVO);
    }

    /**
     * 发送通知消息
     */
    @PutMapping("/send-notice")
    public JsonResult sendNotice(@Validated @RequestBody SendNoticeVO sendNoticeVO) {
        noticeService.notice(sendNoticeVO.getUserId(), sendNoticeVO.getTitle(), sendNoticeVO.getContent());
        return JsonResult.ok();
    }

    /**
     * 发送通知消息
     */
    @PutMapping("/send-notice-super-admin")
    public JsonResult sendNoticeSuperAdmin(@Validated @RequestBody SendNoticeVO sendNoticeVO) {
        noticeService.noticeSuperAdmin(sendNoticeVO.getTitle(), sendNoticeVO.getContent());
        return JsonResult.ok();
    }

    /**
     * 添加
     */
    @SystemLog(name = SystemLogConstants.INSERT)
    @PermCheck(PERM_PREFIX + ADD)
    @PutMapping({"", "/add"})
    public JsonResult add(@Validated @RequestBody SysNoticeVO p) {
        noticeService.add(p);
        return JsonResult.ok();
    }

    /**
     * 修改
     */
    @SystemLog(name = SystemLogConstants.UPDATE)
    @PermCheck(PERM_PREFIX + EDIT)
    @PostMapping({"", "/edit"})
    public JsonResult edit(@Validated @RequestBody SysNoticeVO p) {
        noticeService.edit(p);
        return JsonResult.ok();
    }

    /**
     * 标记为已读
     */
    @PostMapping("/read/{ids}")
    public JsonResult read(@PathVariable("ids") String ids) {
        Date now = new Date();
        if ("all".equals(ids)) {
            noticeService.update(new SysNotice().setReadFlag(true).setReadTime(now), Wrappers.lambdaQuery(SysNotice.class)
                    .eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_NOTICE)
                    .eq(SysNotice::getUserId, SecurityUtil.getLoginUser().getUserId()));
        } else {
            List<String> idList = CollUtil.removeBlank(StrUtil.split(ids, StrUtil.COMMA));
            noticeService.update(new SysNotice().setReadFlag(true).setReadTime(now), Wrappers.lambdaQuery(SysNotice.class)
                    .eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_NOTICE)
                    .eq(SysNotice::getUserId, SecurityUtil.getLoginUser().getUserId()).in(SysNotice::getId, idList));
        }
        return JsonResult.ok();
    }

    /**
     * 删除
     */
    @SystemLog(name = SystemLogConstants.DELETE)
    // @PermCheck(PERM_PREFIX + DELETE)
    @DeleteMapping({"/{ids}", "/del/{ids}"})
    public JsonResult del(@PathVariable String ids) {
        List<String> idList = splitCommaList(ids);
        Assert.notEmpty(idList, "id不能为空");
        noticeService.remove(Wrappers.lambdaQuery(SysNotice.class).eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_NOTICE)
                .eq(SysNotice::getUserId, SecurityUtil.getLoginUser().getUserId()).in(SysNotice::getId, ids));
        return JsonResult.ok();
    }

}

