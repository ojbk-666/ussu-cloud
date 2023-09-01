package cc.ussu.common.core.http.httpclient;

import cc.ussu.common.core.http.IMyHttpResponse;
import cc.ussu.common.core.http.hutool.HuToolHttpResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientHttpResponse implements IMyHttpResponse {

    private int status;

    private Map<String, List<String>> headersMap = new HashMap<>();

    private Map<String, String> cookies = new HashMap<>();

    private byte[] byteArray;

    public HttpClientHttpResponse setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
        return this;
    }

    @Override
    public HuToolHttpResponse create(Object obj) {
        return null;
    }

    @Override
    public IMyHttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public IMyHttpResponse setHeaders(Map headers) {
        this.headersMap = headers;
        return this;
    }

    @Override
    public boolean isOk() {
        return this.status >= 200 && this.status < 300;
    }

    @Override
    public List<String> headers(String name) {
        return headersMap.get(name);
    }

    @Override
    public String getContentType() {
        List<String> headers = headers("Content-Type");
        return CollUtil.getFirst(headers);
    }

    @Override
    public String getCookieStr() {
        return MapUtil.joinIgnoreNull(cookies, ";", "=");
    }

    @Override
    public List<HttpCookie> getSetCookies() {
        List<String> setCookies = headersMap.get("Set-Cookie");
        List<HttpCookie> httpCookieList = new ArrayList<>();
        if (CollUtil.isNotEmpty(setCookies)) {
            setCookies.forEach(r -> httpCookieList.addAll(HttpCookie.parse(r)));
        }
        return httpCookieList;
    }

    @Override
    public String body() {
        if (this.byteArray == null) {
            throw new IllegalArgumentException("响应内容已被读取");
        }
        String str = StrUtil.str(this.byteArray, StandardCharsets.UTF_8);
        // this.byteArray = null;
        return str;
    }
}
