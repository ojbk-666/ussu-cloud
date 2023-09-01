package cc.ussu.common.core.http;

import cc.ussu.common.core.http.hutool.HuToolHttpResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.Header;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public interface IMyHttpResponse<T> {

    HuToolHttpResponse create(T obj);

    IMyHttpResponse setStatus(int status);

    int getStatus();

    IMyHttpResponse setHeaders(Map<String, List<String>> headers);

    boolean isOk();

    default boolean is3xx() {
        return getStatus() / 300 == 1;
    }

    List<String> headers(String name);

    default String header(String name) {
        List<String> headers = headers(name);
        if (CollUtil.isNotEmpty(headers)) {
            return CollUtil.getFirst(headers);
        }
        return null;
    }

    String getContentType();

    String getCookieStr();

    List<HttpCookie> getSetCookies();

    default String getRedirectLocation() {
        if (is3xx()) {
            return header(Header.LOCATION.getValue());
        }
        return null;
    }

    String body();

}
