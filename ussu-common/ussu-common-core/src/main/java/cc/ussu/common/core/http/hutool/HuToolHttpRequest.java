package cc.ussu.common.core.http.hutool;

import cc.ussu.common.core.http.IMyHttpRequest;
import cc.ussu.common.core.http.IMyHttpResponse;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
public class HuToolHttpRequest implements IMyHttpRequest {

    private static final ThreadLocal<HttpRequest> threadLocal = new ThreadLocal<>();

    private CookieManager cookieManager;

    public HuToolHttpRequest() {
    }
    public HuToolHttpRequest(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    public IMyHttpRequest create(String url, String method) {
        HttpRequest httpRequest = null;
        if ("get".equals(StrUtil.trim(method))) {
            httpRequest = HttpUtil.createRequest(Method.GET, url);
        } else if ("post".equals(StrUtil.trim(method))) {
            httpRequest = HttpUtil.createRequest(Method.POST, url);
        } else if ("put".equals(StrUtil.trim(method))) {
            httpRequest = HttpUtil.createRequest(Method.PUT, url);
        } else if ("delete".equals(StrUtil.trim(method))) {
            httpRequest = HttpUtil.createRequest(Method.DELETE, url);
        }
        httpRequest.setReadTimeout(10000);
        threadLocal.set(httpRequest);
        return this;
    }

    @Override
    public IMyHttpRequest createGet(String url) {
        return create(url, "get");
    }

    @Override
    public IMyHttpRequest createPost(String url) {
        return create(url, "post");
    }

    @Override
    public String getUrl() {
        return threadLocal.get().getUrl();
    }

    /**
     * 获取所有请求头
     */
    @Override
    public Map<String, List<String>> headers() {
        return threadLocal.get().headers();
    }

    @Override
    public IMyHttpRequest header(String name, String value, boolean isOverride) {
        threadLocal.get().header(name, value, isOverride);
        return this;
    }

    @Override
    public IMyHttpRequest headerMap(Map<String, String> headers, boolean isOverride) {
        threadLocal.get().headerMap(headers, true);
        return this;
    }

    @Override
    public IMyHttpRequest disableCookie() {
        threadLocal.get().disableCookie();
        return this;
    }

    @Override
    public IMyHttpRequest cookie(String cookie) {
        threadLocal.get().cookie(cookie);
        return this;
    }

    @Override
    public IMyHttpRequest contentType(String contentType) {
        threadLocal.get().contentType(contentType);
        return this;
    }

    @Override
    public IMyHttpRequest contentTypeJson() {
        return contentType(ContentType.JSON.getValue());
    }

    @Override
    public IMyHttpRequest contentTypeFormUrlencoded() {
        return contentType(ContentType.FORM_URLENCODED.getValue());
    }

    @Override
    public IMyHttpRequest contentEncoding(String contentEncoding) {
        return header(Header.CONTENT_ENCODING.getValue(), contentEncoding);
    }

    @Override
    public IMyHttpRequest userAgent(String userAgent) {
        threadLocal.get().header(Header.USER_AGENT, userAgent);
        return this;
    }

    @Override
    public IMyHttpRequest referer(String referer) {
        threadLocal.get().header(Header.REFERER, referer);
        return this;
    }

    @Override
    public IMyHttpRequest origin(String origin) {
        threadLocal.get().header(Header.ORIGIN, origin);
        return this;
    }

    @Override
    public IMyHttpRequest disableRedirect() {
        threadLocal.get().setFollowRedirects(false);
        return this;
    }

    @Override
    public IMyHttpRequest form(Map<String, Object> paramMap) {
        threadLocal.get().form(paramMap);
        return this;
    }

    @Override
    public IMyHttpRequest body(String bodyStr) {
        threadLocal.get().body(bodyStr);
        return this;
    }

    @Override
    public IMyHttpResponse execute() {
        try {
            HttpRequest httpRequest = threadLocal.get();
            if (cookieManager != null) {
                httpRequest.cookie(cookieManager.getCookieStore().get(URI.create(httpRequest.getUrl())));
            }
            HttpResponse execute = threadLocal.get().execute();
            if (cookieManager != null) {
                try {
                    cookieManager.put(URI.create(httpRequest.getUrl()), execute.headers());
                } catch (IOException e) {
                    log.error("cookie存储失败:{}", e.getMessage(), e);
                }
            }
            HuToolHttpResponse response = new HuToolHttpResponse().create(execute);
            return response;
        } finally {
            threadLocal.remove();
        }
    }

}
