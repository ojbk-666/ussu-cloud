package cc.ussu.modules.dczx.vo;

import cc.ussu.common.core.vo.StringLogger;

public class DczxLogger extends StringLogger<DczxLogger> {

    public DczxLogger() {
        super();
    }

    public DczxLogger(String traceId) {
        super(traceId);
    }

    public DczxLogger(boolean debugEnable) {
        super(debugEnable);
    }

    public DczxLogger(String template, Object... args) {
        super(template, args);
    }

    public DczxLogger(boolean debugEnable, String traceId, String template, Object... args) {
        super(debugEnable, traceId, template, args);
    }

    @Override
    protected void callback(String traceId, String msg) {
        // DczxTaskWebSocketClientMsgHandler bean = SpringUtil.getBean(DczxTaskWebSocketClientMsgHandler.class);
        // if (bean != null) {
        //      bean.sendLogToClient(traceId, msg);
        // }
    }
}
