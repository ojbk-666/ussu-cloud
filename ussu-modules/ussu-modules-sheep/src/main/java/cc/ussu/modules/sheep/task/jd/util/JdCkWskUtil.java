package cc.ussu.modules.sheep.task.jd.util;

import cc.ussu.support.qinglong.dto.EnvListDTO;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.wskey.JdWsKeyVO;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

public final class JdCkWskUtil {

    public static JdCookieVO convert(EnvListDTO.EnvDTO envVo) {
        String value = envVo.getValue();
        return convert(value);
    }

    public static JdCookieVO convert(String ck) {
        return new JdCookieVO().setPt_pin(getPinByCk(ck)).setPt_key(getKeyByCk(ck));
    }

    public static JdWsKeyVO convertToWsKey(String ck) {
        return new JdWsKeyVO().setPin(getPinByWsck(ck)).setWskey(getWskeyByWsck(ck));
    }

    public static String getPinByCk(String ck) {
        String pin = ReUtil.get("pin=([^; ]+)(?=;?)", ck, 0);
        pin = pin == null ? "" : pin;
        return pin.replaceAll("pin=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
    }

    public static String getKeyByCk(String ck) {
        String pin = ReUtil.get("key=([^; ]+)(?=;?)", ck, 0);
        pin = pin == null ? "" : pin;
        return pin.replaceAll("key=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
    }

    public static String getPinByWsck(String jdWsck) {
        String pin = ReUtil.get("pin=([^; ]+)(?=;?)", jdWsck, 0);
        pin = pin == null ? "" : pin;
        return pin.replaceAll("pin=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
    }

    public static String getWskeyByWsck(String jdWsck) {
        String pin = ReUtil.get("wskey=([^; ]+)(?=;?)", jdWsck, 0);
        pin = pin == null ? "" : pin;
        return pin.replaceAll("wskey=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
    }

}
