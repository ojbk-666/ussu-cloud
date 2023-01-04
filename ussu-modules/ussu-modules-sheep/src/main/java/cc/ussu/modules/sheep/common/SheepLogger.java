package cc.ussu.modules.sheep.common;

import cc.ussu.common.core.vo.StringLogger;

public class SheepLogger extends StringLogger<SheepLogger> {

    public SheepLogger() {
        super();
    }
    public SheepLogger(boolean debugEnable) {
        super(debugEnable);
    }

    public SheepLogger(String template, Object... args) {
        this();
        this.append(template, args);
    }

    @Override
    protected void callback(String traceId, String msg) {
        /*SheepTaskWebSocketClientMsgHandler bean = SpringUtil.getBean(SheepTaskWebSocketClientMsgHandler.class);
        if (bean != null) {
            bean.sendLogToClient(traceId, msg);
        }*/
    }
}
