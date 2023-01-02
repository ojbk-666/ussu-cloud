package cc.ussu.modules.dczx.service;

/**
 * 任务完成后发送通知的接口定义
 */
public interface SendNoticeAfterTaskFinished {
    void notice(String receiver, String title, String content);

}
