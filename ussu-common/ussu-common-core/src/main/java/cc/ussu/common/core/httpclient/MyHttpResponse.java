package cc.ussu.common.core.httpclient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHttpResponse {

    private int status;

    private Map<String, List<String>> headersMap = new HashMap<>();

    private Map<String, String> cookies = new HashMap<>();

    private byte[] byteArray;

    public MyHttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public MyHttpResponse setHeaders(Map<String, List<String>> headers) {
        this.headersMap = headers;
        // 解析cookie
        parseCookie();
        return this;
    }

    private void parseCookie() {
        List<String> cookieList = this.headersMap.get(Header.SET_COOKIE.getValue().toLowerCase());
        if (CollUtil.isNotEmpty(cookieList)) {
            a:
            for (String cookie : cookieList) {
                List<String> split = StrUtil.split(cookie, ";");
                if (CollUtil.isNotEmpty(split)) {
                    b:
                    for (String s : split) {
                        if (StrUtil.isNotBlank(s)) {
                            if (StrUtil.containsAnyIgnoreCase(s, "EXPIRES=", "PATH=", "DOMAIN=")) {
                                continue b;
                            }
                            List<String> nv = StrUtil.split(s.trim(), "=");
                            if (CollUtil.isNotEmpty(nv) && nv.size() == 2) {
                                this.cookies.put(nv.get(0), nv.get(1));
                            }
                        }
                    }
                }
            }
        }
    }

    public MyHttpResponse setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
        return this;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public boolean isOk() {
        return this.status >= 200 && this.status < 300;
    }

    public List<String> headers(String name) {
        if (ArrayUtil.isNotEmpty(headersMap) && StrUtil.isNotBlank(name)) {
            for (Map.Entry<String, List<String>> entry : headersMap.entrySet()) {
                if (name.equalsIgnoreCase(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return new ArrayList<>();
    }

    public String header(String name) {
        return CollUtil.getFirst(headers(name));
    }


    public String header(Header header) {
        return header(header.getValue());
    }

    /**
     * 是否为gzip压缩过的内容
     *
     * @return 是否为gzip压缩过的内容
     */
    public boolean isGzip() {
        final String contentEncoding = header(HttpHeaders.CONTENT_ENCODING);
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    /**
     * 是否为zlib(Deflate)压缩过的内容
     *
     * @return 是否为zlib(Deflate)压缩过的内容
     * @since 4.5.7
     */
    public boolean isDeflate() {
        final String contentEncoding = header(HttpHeaders.CONTENT_ENCODING);
        return "deflate".equalsIgnoreCase(contentEncoding);
    }

    /**
     * 是否为Transfer-Encoding:Chunked的内容
     *
     * @return 是否为Transfer-Encoding:Chunked的内容
     * @since 4.6.2
     */
    public boolean isChunked() {
        final String transferEncoding = header(HttpHeaders.TRANSFER_ENCODING);
        return "Chunked".equalsIgnoreCase(transferEncoding);
    }

    /**
     * 获取响应头的Content-Type字段值
     */
    public String getContentType() {
        return header(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * 获取响应头的Content-Length字段值<br/>
     * 获取响应体大小
     */
    public Long getContentLength() {
        String s = header(HttpHeaders.CONTENT_LENGTH);
        if (StrUtil.isNotBlank(s)) {
            return Long.parseLong(s);
        }
        return null;
    }

    /**
     * 响应头部是否有Content-Length字段
     */
    public boolean hasContentLength() {
        return CollUtil.size(headers(HttpHeaders.CONTENT_LENGTH)) > 0;
    }

    /**
     * 获取本次请求服务器返回的Cookie信息
     * 用于请求携带cookie 格式为a=12;b=34
     *
     * @return Cookie字符串
     * @since 3.1.1
     */
    public String getCookieStr() {
        return MapUtil.joinIgnoreNull(cookies, ";", "=");
    }

    public String getCookieValue(String name) {
        return cookies.get(name);
    }

    /**
     * 获取重定向地址
     */
    public String getRedirectLocation() {
        if (status / 300 == 1) {
            return header(HttpHeaders.LOCATION);
        }
        return null;
    }

    /**
     * 从Content-Disposition头中获取文件名
     *
     * @return 文件名，empty表示无
     */
    private String getFileNameFromDisposition() {
        String fileName = null;
        final String disposition = header("Content-Disposition");
        if (StrUtil.isNotBlank(disposition)) {
            fileName = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
            if (StrUtil.isBlank(fileName)) {
                fileName = StrUtil.subAfter(disposition, "filename=", true);
            }
        }
        return fileName;
    }

    public String body() {
        if (this.byteArray == null) {
            throw new IllegalArgumentException("响应内容已被读取");
        }
        String str = StrUtil.str(this.byteArray, CharsetUtil.CHARSET_UTF_8);
        // this.byteArray = null;
        return str;
    }

    public <T> T body(Class<T> clazz) {
        String str = body();
        if (str != null) {
            return JSONUtil.toBean(str, clazz);
        }
        return null;
    }

}
