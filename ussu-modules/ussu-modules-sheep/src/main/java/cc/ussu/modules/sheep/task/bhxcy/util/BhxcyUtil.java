package cc.ussu.modules.sheep.task.bhxcy.util;

import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.modules.sheep.task.bhxcy.constants.BhxcyConstants;
import cc.ussu.modules.sheep.task.bhxcy.constants.GlobalData;
import cc.ussu.modules.sheep.task.bhxcy.vo.BhxcyBaseParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 渤海宣传员工具
 */
public final class BhxcyUtil {

    private static final GlobalData getGlobalData() {
        return JSONUtil.toBean(ConfigUtil.getValue("sheep","sheep", BhxcyConstants.PARAM_KEY_GLOBAL_DATA, "{}"), GlobalData.class);
    }

    public static BhxcyBaseParam formatToParam(String urlstr) {
        Map<String, String> m = HttpUtil.decodeParamMap(urlstr, StandardCharsets.UTF_8);
        return BeanUtil.toBeanIgnoreError(m, BhxcyBaseParam.class);
    }

    /**
     * 获取基础请求路径
     */
    public static String getBaseApi() {
        String url = getGlobalData().getUrl();
        if (url.contains("?")) {
            url += "&";
        } else {
            url += "?";
        }
        return  url += "r=" + getGlobalData().getApi();
    }

    public static String getRequestUri(String apiAction) {
        return getBaseApi() + "&apiAction=" + apiAction;
    }

    public static String md5(String s) {
        return MD5.create().digestHex(s);
    }

    /**
     * 拼接字符串
     *
     * @param strings
     * @return
     */
    public static String getString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 获取签到签名
     *
     * @param token
     * @param uid
     * @return
     */
    public static String getSignInSignure(String token, String uid) {
        return md5(getString(token, getGlobalData().getMiyan(), uid, "SignIn"));
    }

    /**
     * 获取签到参数
     *
     * @param token
     * @param uid
     * @return
     */
    public static Map<String, String> getSignInFormStr(String token, String uid) {
        Map<String, String> m = new ConcurrentHashMap<>();
        m.put(BhxcyConstants.UID, uid);
        m.put(BhxcyConstants.TOKEN, token);
        m.put(BhxcyConstants.SIGNURE, getSignInSignure(token, uid));
        return m;
    }

    /**
     * 获取换取用户信息签名
     *
     * @param code
     * @return
     */
    public static String getGetOpenidSignure(String code) {
        return md5(getString("getOpenid", getGlobalData().getMiyan(), code));
    }

    /**
     * 获取获取用户信息签名
     *
     * @param token
     * @param uid
     * @return
     */
    public static String getGetUserInfoSignure(String token, String uid) {
        return md5(getString("0", "getUserInfo", uid, getGlobalData().getMiyan(), token));
    }

    /**
     * 获取获取用户信息参数
     *
     * @param token
     * @param uid
     * @return
     */
    public static Map<String, String> getGetUserInfoFormStr(String token, String uid) {
        Map<String, String> m = new ConcurrentHashMap<>();
        m.put(BhxcyConstants.UID, uid);
        m.put(BhxcyConstants.TOKEN, token);
        m.put(BhxcyConstants.VERIFICATION, getGlobalData().getVerification() + StrUtil.EMPTY);
        m.put(BhxcyConstants.SIGNURE, getGetUserInfoSignure(token, uid));
        return m;
    }

    /**
     * 获取兑换签名
     *
     * @param token
     * @param uid
     * @param category
     * @param jine
     * @return
     */
    public static String getRechargeSignure(String token, String uid, String category, String jine) {
        return md5("Recharge" + uid + getGlobalData().getMiyan() + category + token + jine);
    }

    /**
     * 获取兑换参数
     *
     * @param token
     * @param uid
     * @param category
     * @param jine
     * @return
     */
    public static Map<String, String> getRechargeFormStr(String token, String uid, String category, String jine) {
        Map<String, String> m = new ConcurrentHashMap<>();
        m.put(BhxcyConstants.UID, uid);
        m.put(BhxcyConstants.TOKEN, token);
        m.put("Type", jine);
        m.put("Category", category);
        m.put(BhxcyConstants.SIGNURE, getRechargeSignure(token, uid, category, jine));
        return m;
    }

}
