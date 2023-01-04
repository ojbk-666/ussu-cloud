package cc.ussu.common.core.vo;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串拼接
 */
public class StringLogger<T> {

    private static final Logger logger = LoggerFactory.getLogger(StringLogger.class);

    private StringBuffer sb;
    private boolean debugEnable = false;
    private String traceId;
    public String newline = StrPool.LF;

    public StringLogger() {
        this.sb = new StringBuffer();
    }

    public StringLogger(String traceId) {
        this.sb = new StringBuffer();
        this.traceId = traceId;
    }

    public StringLogger(boolean debugEnable) {
        this();
        this.debugEnable = debugEnable;
    }

    public StringLogger(String template, Object... args) {
        this();
        this.append(template, args);
    }

    public StringLogger(boolean debugEnable, String traceId, String template, Object... args) {
        this();
        this.append(template, args);
    }

    public StringLogger setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getNewline() {
        return this.newline;
    }

    public StringLogger setNewline(String newline) {
        this.newline = newline;
        return this;
    }

    protected <T> T append(String template, Object... args) {
        return append(false, template, args);
    }

    protected <T> T append(boolean appendNewLine, String template, Object... args) {
        String tmp = StrUtil.format(template, args);
        logger.info(tmp);
        if (appendNewLine) {
            tmp += getNewline();
        }
        sb.append(tmp);
        callback(getTraceId(), tmp);
        return (T) this;
    }

    public StringLogger log(String template, Object... args) {
        return append(template, args);
    }

    public StringLogger info(String template, Object... args) {
        return append(true, template, args);
    }

    public StringLogger debug(String template, Object... args) {
        if (debugEnable) {
            return append(true, template, args);
        } else {
            return this;
        }
    }

    public StringLogger newline() {
        sb.append(getNewline());
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * 回调函数
     */
    protected void callback(String traceId, String msg) {
    }

    protected void finish(String traceId, String allMsg) {
    }

}
