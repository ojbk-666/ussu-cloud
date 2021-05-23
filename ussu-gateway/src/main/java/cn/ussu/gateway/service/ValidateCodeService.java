package cn.ussu.gateway.service;

/**
 * 验证码
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     */
    <T> T create(String uuid);

    /**
     * 验证验证码
     * @param uuid uuid
     * @param code 输入的值
     */
    boolean check(String uuid, String code);

}
