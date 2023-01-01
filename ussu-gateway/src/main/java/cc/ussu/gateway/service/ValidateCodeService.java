package cc.ussu.gateway.service;

import cc.ussu.common.core.exception.CaptchaException;
import cc.ussu.common.core.vo.JsonResult;

/**
 * 验证码
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     */
    JsonResult create(String uuid);

    /**
     * 验证验证码
     * @param uuid uuid
     * @param code 输入的值
     */
    boolean check(String uuid, String code) throws CaptchaException;

}
