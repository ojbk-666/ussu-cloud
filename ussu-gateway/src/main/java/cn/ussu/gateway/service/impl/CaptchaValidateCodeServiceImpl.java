package cn.ussu.gateway.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.RedisConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.exception.CaptchaException;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.gateway.service.ValidateCodeService;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private Environment environment;

    @Override
    public JsonResult create(String uuid) {
        if (StrUtil.isBlank(uuid)) {
            uuid = UUID.fastUUID().toString(true);
        }
        Captcha captcha = randomCaptcha();
        String text = captcha.text();
        String base64Img = captcha.toBase64();
        redisService.setCacheObject(RedisConstants.LOGIN_UUID_CAPTCHA_ + uuid, text, 180L);
        Map<String, Object> r = new HashMap<>(2);
        r.put("uuid", uuid);
        r.put("img", base64Img);
        if (ArrayUtil.contains(environment.getActiveProfiles(), "dev")) {
            r.put("r", text);
        }
        return JsonResult.ok().data(r);
    }

    @Override
    public boolean check(String uuid, String code) {
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
    private Captcha randomCaptcha() {
        int width = 120;
        int height = 48;
        int i = RandomUtil.randomInt(5);
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
