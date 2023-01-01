package cc.ussu.common.core.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 权限认证未通过
 */
public class NotPermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotPermissionException(String permission) {
        super(permission);
    }

    public NotPermissionException(String[] permissions) {
        super(StrUtil.join(StrUtil.COMMA, permissions));
    }

}
