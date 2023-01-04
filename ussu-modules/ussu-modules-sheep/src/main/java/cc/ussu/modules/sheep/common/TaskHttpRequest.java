package cc.ussu.modules.sheep.common;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

public final class TaskHttpRequest {

    private static final int TIMEOUT = 5000;

    private static HttpRequest httpRequest;

    public static TaskHttpRequest createGet(String url) {
        TaskHttpRequest taskHttpRequest = new TaskHttpRequest();
        taskHttpRequest.httpRequest = HttpUtil.createGet(url).timeout(TIMEOUT).disableCookie();
        return taskHttpRequest;
    }

    public static TaskHttpRequest createPost(String url) {
        TaskHttpRequest taskHttpRequest = new TaskHttpRequest();
        taskHttpRequest.httpRequest = HttpUtil.createPost(url).timeout(TIMEOUT).disableCookie();
        return taskHttpRequest;
    }

    public TaskHttpRequest referer(String referer) {
        httpRequest.header(Header.REFERER, referer);
        return this;
    }

    public TaskHttpRequest cookie(String cookie) {
        httpRequest.cookie(cookie);
        return this;
    }

    public TaskHttpRequest host(String host) {
        httpRequest.header(Header.HOST, host);
        return this;
    }

    public TaskHttpRequest accept(String accept) {
        httpRequest.header(Header.ACCEPT, accept);
        return this;
    }

    public TaskHttpRequest acceptEncoding(String acceptEncoding) {
        httpRequest.header(Header.ACCEPT_ENCODING, acceptEncoding);
        return this;
    }

    public TaskHttpRequest acceptCharset(String acceptCharset) {
        httpRequest.header(Header.ACCEPT_CHARSET, acceptCharset);
        return this;
    }

    public TaskHttpRequest acceptLanguage(String acceptLanguage) {
        httpRequest.header(Header.ACCEPT_LANGUAGE, acceptLanguage);
        return this;
    }

    public TaskHttpRequest origin(String origin) {
        httpRequest.header(Header.ORIGIN, origin);
        return this;
    }

    public TaskHttpRequest userAgent(String userAgent) {
        httpRequest.header(Header.USER_AGENT, userAgent);
        return this;
    }

    public TaskHttpRequest timeoutSecond(Integer timeoutSecond) {
        httpRequest.timeout(timeoutSecond * 1000);
        return this;
    }

    public TaskHttpRequest contentType(String contentType) {
        httpRequest.contentType(contentType);
        return this;
    }

    public TaskHttpRequest contentType(ContentType contentType) {
        httpRequest.contentType(contentType.toString());
        return this;
    }

    public TaskHttpRequest contentTypeFormUrlEncoded() {
        httpRequest.contentType(ContentType.FORM_URLENCODED.toString());
        return this;
    }

    public TaskHttpRequest contentTypeJson() {
        httpRequest.contentType(ContentType.JSON.toString());
        return this;
    }

}
