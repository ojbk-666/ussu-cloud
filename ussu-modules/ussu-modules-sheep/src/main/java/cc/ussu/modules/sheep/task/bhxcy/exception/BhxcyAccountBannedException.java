package cc.ussu.modules.sheep.task.bhxcy.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 账号被封禁
 */
public class BhxcyAccountBannedException extends RuntimeException {

    public BhxcyAccountBannedException() {
        super("账号已被封禁");
    }

    public BhxcyAccountBannedException(String account) {
        super(StrUtil.format("账号{}已被封禁", account));
    }

}
