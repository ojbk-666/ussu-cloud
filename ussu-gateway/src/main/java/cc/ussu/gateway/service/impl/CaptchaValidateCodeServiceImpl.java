package cc.ussu.gateway.service.impl;

import cc.ussu.common.core.exception.CaptchaException;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.gateway.properties.CaptchaProperties;
import cc.ussu.gateway.service.ValidateCodeService;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cc.ussu.common.core.constants.RedisConstants;
import cc.ussu.common.core.vo.JsonResult;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 图形验证码生成
 */
@Service
public class CaptchaValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private CaptchaProperties captchaProperties;


    @Override
    public JsonResult create(String uuid) {
        Map<String, Object> r = new HashMap<>(2);
        r.put("captchaEnabled", captchaProperties.getEnabled());
        // todo 注册开关
        r.put("registerEnable", false);
        if (BooleanUtil.isTrue(captchaProperties.getEnabled())) {
            if (StrUtil.isBlank(uuid)) {
                uuid = UUID.fastUUID().toString(true);
            }
            Captcha captcha = randomCaptcha(captchaProperties.getType());
            String text = captcha.text();
            String base64Img = captcha.toBase64();
            redisService.setCacheObject(RedisConstants.LOGIN_UUID_CAPTCHA_ + uuid, text, 180L);
            r.put("uuid", uuid);
            r.put("img", base64Img);
        }
        return JsonResult.ok(r);
    }

    @Override
    public boolean check(String uuid, String code) throws CaptchaException {
        if (StrUtil.isBlank(uuid) || StrUtil.isBlank(code)) {
            throw new CaptchaException("uuid code 不能为空");
        }
        String cacheResult = redisService.getCacheObject(RedisConstants.LOGIN_UUID_CAPTCHA_ + uuid);
        if (StrUtil.isBlank(cacheResult)) {
            throw new CaptchaException("验证码已失效");
        }
        redisService.deleteObject(RedisConstants.LOGIN_UUID_CAPTCHA_ + uuid);
        // 验证
        if (!cacheResult.equalsIgnoreCase(code.trim())) {
            throw new CaptchaException("验证码错误");
        }
        return true;
    }

    /**
     * 随机验证码类型
     */
    private Captcha randomCaptcha(Integer type) {
        int width = 120;
        int height = 48;
        int i = type % 5;
        Captcha captcha = null;
        if (i == 0) {
            // png
            captcha = new SpecCaptcha(width, height);
        } else if (i == 1) {
            // gif类型
            captcha = new GifCaptcha(width, height);
        } else if (i == 2) {
            // 中文类型
            captcha = new ChineseCaptcha(width, height);
        } else if (i == 3) {
            // 中文gif类型
            captcha = new ChineseGifCaptcha(width, height);
        } else if (i == 4) {
            // 算术类型
            captcha = new ArithmeticCaptcha(width, height);
            captcha.setLen(2);  // 几位数运算
        }
        return captcha;
    }

}
