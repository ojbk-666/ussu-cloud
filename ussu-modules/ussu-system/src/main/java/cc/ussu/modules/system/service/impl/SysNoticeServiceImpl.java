package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysNotice;
import cc.ussu.modules.system.entity.SysNoticeContent;
import cc.ussu.modules.system.entity.vo.SysNoticeVO;
import cc.ussu.modules.system.mapper.SysNoticeMapper;
import cc.ussu.modules.system.service.ISysNoticeContentService;
import cc.ussu.modules.system.service.ISysNoticeService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知公告表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2022-01-18 15:31:26
 */
@Master
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    @Autowired
    private ISysNoticeContentService noticeContentService;
    // @Autowired
    // private SysNoticeWebSocketClientMsgHandler sysNoticeWebSocketClientMsgHandler;

    /**
     * 通知
     *
     * @param receiverId 接收人id
     * @param title      标题
     * @param content    内容
     * @return
     */
    @Transactional
    @Override
    public void notice(String receiverId, String title, String content) {
        SysNoticeContent sysNoticeContent = new SysNoticeContent().setNoticeContent(content);
        noticeContentService.save(sysNoticeContent);
        SysNotice notice = new SysNotice()
                .setNoticeTitle(title)
                .setNoticeType(SysNotice.NOTICE_TYPE_NOTICE)
                .setNoticeContentId(sysNoticeContent.getId())
                .setUserId(receiverId)
                .setCreateTime(new Date())
                .setReadFlag(false);
        super.save(notice);
        // sysNoticeWebSocketClientMsgHandler.refreshNotice(notice.getUserId());
    }

    /**
     * 给超级管理员发送消息
     *
     * @param title
     * @param content
     */
    @Transactional
    @Override
    public void noticeSuperAdmin(String title, String content) {
        SysNoticeContent sysNoticeContent = new SysNoticeContent().setNoticeContent(content);
        noticeContentService.save(sysNoticeContent);
        SysNotice notice = new SysNotice()
                .setNoticeType(SysNotice.NOTICE_TYPE_NOTICE)
                .setNoticeTitle(title)
                .setNoticeContentId(sysNoticeContent.getId())
                .setUserId(SecurityUtil.getSuperAdminUserId())
                .setCreateTime(new Date())
                .setReadFlag(false);
        super.save(notice);
        // sysNoticeWebSocketClientMsgHandler.refreshNotice(notice.getUserId());
    }

    /**
     * 未读通知数量
     *
     * @param userId
     */
    @Override
    public long countUnReadNotice(String userId) {
        return count(Wrappers.lambdaQuery(SysNotice.class)
                .eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_NOTICE)
                .eq(SysNotice::getReadFlag, false).eq(SysNotice::getUserId, userId));
    }

    /**
     * 未读公告数量
     */
    @Override
    public long countUnReadAnnouncement() {
        return count(Wrappers.lambdaQuery(SysNotice.class)
                .eq(SysNotice::getNoticeType, SysNotice.NOTICE_TYPE_ANNOUNCEMENT)
                .leSql(SysNotice::getStartTime, "now()")
                .geSql(SysNotice::getEndTime, "now()"));
    }

    /**
     * 添加
     *
     * @param noticeVO
     */
    @Transactional
    @Override
    public void add(SysNoticeVO noticeVO) {
        // 保存通知内容
        SysNoticeContent sysNoticeContent = new SysNoticeContent().setNoticeContent(noticeVO.getNoticeContent());
        noticeContentService.save(sysNoticeContent);
        // 保存通知
        String noticeType = noticeVO.getNoticeType();
        SysNotice notice = BeanUtil.toBean(noticeVO, SysNotice.class);
        notice.setNoticeContentId(sysNoticeContent.getId()).setCreateTime(new Date()).setDelFlag(StrConstants.CHAR_FALSE).setReadFlag(false);
        if (SysNotice.NOTICE_TYPE_NOTICE.equals(noticeType)) {
            // 通知 选人发送
            List<String> userIds = noticeVO.getUserIds();
            Assert.notEmpty(userIds, "未选择接收人");
            List<SysNotice> collect = userIds.stream().map(r -> {
                SysNotice o = ObjectUtil.cloneByStream(notice);
                o.setUserId(r);
                return o;
            }).collect(Collectors.toList());
            super.saveBatch(collect);
            for (SysNotice n : collect) {
                // sysNoticeWebSocketClientMsgHandler.refreshNotice(n.getUserId());
            }
        } else if (SysNotice.NOTICE_TYPE_ANNOUNCEMENT.equals(noticeType)) {
            // 公告
            Assert.notEmpty(noticeVO.getStartEndTime(), "请选择公告有效期");
            notice.setStartTime(noticeVO.getStartEndTime()[0]).setEndTime(noticeVO.getStartEndTime()[1]);
            super.save(notice);
            if (DateUtil.isIn(new Date(), notice.getStartTime(), notice.getEndTime())) {
                // sysNoticeWebSocketClientMsgHandler.refreshAnnouncement();
            }
        }
    }

    /**
     * 修改
     *
     * @param noticeVO
     */
    @Transactional
    @Override
    public void edit(SysNoticeVO noticeVO) {
        Assert.notBlank(noticeVO.getId(), "id不能为空");
        SysNotice notice = getById(noticeVO.getId());
        Assert.notNull(notice, "未找到该数据");
        super.updateById(new SysNotice().setId(noticeVO.getId()).setNoticeTitle(noticeVO.getNoticeTitle())
                .setRemark(noticeVO.getRemark()));
        Long noticeContentId = notice.getNoticeContentId();
        noticeContentService.updateById(new SysNoticeContent(noticeContentId, noticeVO.getNoticeContent()));
    }
}
