package cc.ussu.modules.dczx.constants;

public interface DczxConstants {

    String VIDEO_THREAD_POOL_NAME = "dczxVideoThreadPool";
    /**
     * jsessionid缓存前缀
     */
    String CACHE_JSESSIONID_PREFIX = "dczx:jsessionid:";
    String JSESSIONID = "JSESSIONID";
    String PARAM_KEY_DCZX_VIDEO_PAUSE_TIME = "dczx:video:pause-time";
    /**
     * 是否被终止的标识
     */
    String THREAD_TASK_PARSE_KEY_PREFIX = "dczx:task:pause-flag:";

    /**
     * 配置key - 启动后是否刷新es数据
     */
    String PARAM_KEY_REFRESH_ES_AFTER_BOOT = "dczx:refresh-es-after-boot";
    /**
     * 配置key - 插件获取单元作业答案的时候是否从es查询
     */
    String PARAM_KEY_REQUEST_RIGHT_OPTION_FROM_ES = "dczx:plugins:api-request-es";

    /**
     * 类型 视频
     */
    Integer TASK_TYPE_VIDEO = 1;
    /**
     * 类型浏览课件
     */
    Integer TASK_TYPE_TEXT = 2;
    /**
     * 类型 家庭作业
     */
    Integer TASK_TYPE_HOMEWORK = 3;
    /**
     * 状态 待处理
     */
    Integer TASK_STATUS_NOT_START = 0;
    /**
     * 状态 处理中
     */
    Integer TASK_STATUS_DOING = 1;
    /**
     * 状态 已完成
     */
    Integer TASK_STATUS_FINISHED = 2;
    /**
     * 状态 错误
     */
    Integer TASK_STATUS_ERROR = 3;

}
