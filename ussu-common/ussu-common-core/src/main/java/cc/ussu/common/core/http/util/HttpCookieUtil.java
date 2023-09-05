package cc.ussu.common.core.http.util;

import cn.hutool.core.collection.CollUtil;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class HttpCookieUtil {

    public static String toString(List<HttpCookie> httpCookies) {
        if (CollUtil.isNotEmpty(httpCookies)) {
            return httpCookies.stream().map(HttpCookie::toString).collect(Collectors.joining(";"));
        }
        return null;
    }

    public static String toString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    public static Map<String, String> toMap(List<HttpCookie> httpCookies) {
        if (CollUtil.isNotEmpty(httpCookies)) {
            Map<String, String> map = new HashMap<>();
            for (HttpCookie httpCookie : httpCookies) {
                map.put(httpCookie.getName(), httpCookie.getValue());
            }
            return map;
        }
        return new HashMap<>();
    }

}
