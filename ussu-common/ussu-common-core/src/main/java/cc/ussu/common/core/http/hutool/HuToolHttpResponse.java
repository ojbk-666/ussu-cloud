package cc.ussu.common.core.http.hutool;

import cc.ussu.common.core.http.IMyHttpResponse;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;

import java.io.InputStream;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class HuToolHttpResponse implements IMyHttpResponse<HttpResponse> {

    private HttpResponse httpResponse;

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    @Override
    public HuToolHttpResponse create(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        return this;
    }

    @Override
    public IMyHttpResponse setStatus(int status) {
        // super.status = status;
        return this;
    }

    @Override
    public int getStatus() {
        return httpResponse.getStatus();
    }

    @Override
    public IMyHttpResponse setHeaders(Map<String, List<String>> headers) {
        // super.header(headers);
        return this;
    }

    @Override
    public boolean isOk() {
        return httpResponse.isOk();
    }

    @Override
    public Map<String, List<String>> headers() {
        return httpResponse.headers();
    }

    @Override
    public List<String> headers(String name) {
        return httpResponse.headerList(name);
    }

    @Override
    public String getContentType() {
        return httpResponse.header(Header.CONTENT_TYPE);
    }

    @Override
    public String getCookieStr() {
        return httpResponse.getCookieStr();
    }

    @Override
    public List<HttpCookie> getSetCookies() {
        return httpResponse.getCookies();
    }

    @Override
    public String getRedirectLocation() {
        return header(Header.LOCATION.getValue());
    }

    @Override
    public String body() {
        return httpResponse.body();
    }

    @Override
    public InputStream bodyStream() {
        return httpResponse.bodyStream();
    }
}
