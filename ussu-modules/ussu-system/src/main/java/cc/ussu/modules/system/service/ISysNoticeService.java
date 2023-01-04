package cc.ussu.modules.system.service;

import cc.ussu.modules.system.entity.SysNotice;
import cc.ussu.modules.system.entity.vo.SysNoticeVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 通知公告表 服务类
 * </p>
 *
 * @author liming
 * @since 2022-01-18 15:31:26
 */
public interface ISysNoticeService extends IService<SysNotice> {

    /**
     * 通知
     *
     * @param receiverId 接收人id
     * @param title      标题
     * @param content    内容
     * @return
     */
    void notice(String receiverId, String title, String content);

    /**
     * 给超级管理员发送消息
     *
     * @param title
     * @param content
     */
    void noticeSuperAdmin(String title, String content);

    /**
     * 未读通知数量
     */
    long countUnReadNotice(String userId);

    /**
     * 未读公告数量
     */
    long countUnReadAnnouncement();

    /**
     * 添加
     */
    void add(SysNoticeVO noticeVO);

    /**
     * 修改
     */
    void edit(SysNoticeVO noticeVO);

}
