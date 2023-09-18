package cc.ussu.common.core.http;

import java.util.List;
import java.util.Map;

public interface IMyHttpRequest {

    IMyHttpRequest create(String url, String method);

    IMyHttpRequest createGet(String url);

    IMyHttpRequest createPost(String url);

    String getUrl();

    /**
     * 获取所有请求头
     */
    Map<String, List<String>> headers();

    IMyHttpRequest header(String name, String value, boolean isOverride);

    default IMyHttpRequest header(String header, String value) {
        return header(header, value, true);
    }

    IMyHttpRequest headerMap(Map<String, String> headers, boolean isOverride);

    default IMyHttpRequest headerMap(Map<String, String> headers) {
        return headerMap(headers, true);
    }

    IMyHttpRequest disableCookie();

    IMyHttpRequest cookie(String cookie);

    // IMyHttpRequest host(String host);

    IMyHttpRequest contentType(String contentType);

    IMyHttpRequest contentTypeJson();

    IMyHttpRequest contentTypeFormUrlencoded();

    IMyHttpRequest contentEncoding(String contentEncoding);

    IMyHttpRequest userAgent(String userAgent);

    IMyHttpRequest referer(String referer);

    IMyHttpRequest origin(String origin);

    IMyHttpRequest disableRedirect();

    IMyHttpRequest form(Map<String, Object> paramMap);

    IMyHttpRequest body(String bodyStr);

    IMyHttpResponse execute();

}
