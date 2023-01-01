package cc.ussu.common.core.httpclient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyHttpRequest {

    public static HttpRequestBuilder create(String method, String uri) {
        HttpRequestBase httpRequestBase = new HttpRequestBase() {
            @Override
            public String getMethod() {
                return method;
            }
        };
        if (StrUtil.isNotBlank(uri)) {
            try {
                httpRequestBase.setURI(new URIBuilder(uri, CharsetUtil.CHARSET_UTF_8).build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return new HttpRequestBuilder(httpRequestBase);
    }

    public static HttpRequestBuilder create(Method method) {
        return create(method.name(), null);
    }

    public static HttpRequestBuilder create(Method method, String uri) {
        return create(method.name(), uri);
    }

    public static HttpRequestBuilder createGet() {
        return new HttpRequestBuilder(new HttpGet());
    }

    public static HttpRequestBuilder createGet(String uri) {
        return new HttpRequestBuilder(new HttpGet(uri));
    }

    public static HttpRequestBuilder createPost() {
        return new HttpRequestBuilder(new HttpPost());
    }

    public static HttpRequestBuilder createPost(String uri) {
        return new HttpRequestBuilder(new HttpPost(uri));
    }

    public static HttpRequestBuilder createPut() {
        return new HttpRequestBuilder(new HttpPut());
    }

    public static HttpRequestBuilder createPut(String uri) {
        return new HttpRequestBuilder(new HttpPut(uri));
    }

    public static HttpRequestBuilder createDelete() {
        return new HttpRequestBuilder(new HttpDelete());
    }

    public static HttpRequestBuilder createDelete(String uri) {
        return new HttpRequestBuilder(new HttpDelete(uri));
    }

    /**
     * head请求获取文件大小
     *
     * @param uri 远程文件url
     * @return 文件大小 如果服务段没有返回则为null
     */
    public static Long getRemoteFileSize(String uri) {
        MyHttpResponse execute = create(Method.HEAD, uri).execute();
        if (execute.isOk() && execute.hasContentLength()) {
            return execute.getContentLength();
        }
        return null;
    }

    /**
     * 下载远程文件
     *
     * @param url      请求的url
     * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @return 下载的文件对象
     */
    public static File downloadFileFromUrl(String url, File destFile) {
        String absolutePath = FileUtil.getAbsolutePath(destFile);
        if (FileUtil.isDirectory(destFile)) {
            // 尝试获取文件名
            String decode = URLDecoder.decode(url, CharsetUtil.CHARSET_UTF_8);
            String fileName = FileNameUtil.getName(decode);
            absolutePath = StrUtil.appendIfMissing(absolutePath, StrUtil.SLASH, StrUtil.SLASH) + fileName;
        }
        MyHttpResponse execute = HttpClientUtil.doHttp(new HttpGet(url));
        FileUtil.writeBytes(execute.getByteArray(), absolutePath);
        return new File(absolutePath);
    }

    public static class HttpRequestBuilder {
        private HttpRequestBase httpRequestBase;

        HttpRequestBuilder(HttpRequestBase httpRequestBase) {
            this.httpRequestBase = httpRequestBase;
        }

        public HttpRequestBuilder uri(String uri) {
            httpRequestBase.setURI(URI.create(uri));
            return this;
        }

        public HttpRequestBuilder header(String name, String value, boolean isOverride) {
            if (isOverride) {
                httpRequestBase.setHeader(name, value);
            } else {
                if (!httpRequestBase.containsHeader(name)) {
                    httpRequestBase.setHeader(name, value);
                }
            }
            return this;
        }

        public HttpRequestBuilder header(Header header, String value) {
            return header(header.getValue(), value, true);
        }

        public HttpRequestBuilder header(String name, String value) {
            return header(name, value, true);
        }

        /**
         * 设置请求头
         *
         * @param headers    请求头
         */
        public HttpRequestBuilder headerMap(Map<String, String> headers, boolean isOverride) {
            if (CollUtil.isNotEmpty(headers)) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    header(entry.getKey(), entry.getValue(), isOverride);
                }
            }
            return this;
        }

        public HttpRequestBuilder headerMap(Map<String, String> headers) {
            return headerMap(headers, true);
        }

        private boolean isGetMethod() {
            return RequestMethod.GET.name().equalsIgnoreCase(httpRequestBase.getMethod());
        }

        private boolean isPostMethod() {
            return RequestMethod.POST.name().equalsIgnoreCase(httpRequestBase.getMethod());
        }

        /**
         * 移除所有cookie
         */
        public HttpRequestBuilder disableCookie() {
            httpRequestBase.removeHeaders(Header.COOKIE.getValue());
            return this;
        }

        /**
         * 设置cookie值
         *
         * @param cookie
         * @return
         */
        public HttpRequestBuilder cookie(String cookie) {
            httpRequestBase.setHeader(Header.COOKIE.getValue(), cookie);
            return this;
        }

        /**
         * 设置请求头的host
         *
         * @param host
         * @return
         */
        public HttpRequestBuilder host(String host) {
            httpRequestBase.setHeader(Header.HOST.getValue(), host);
            return this;
        }

        /**
         * 设置请求类型
         *
         * @param contentType
         * @return
         */
        public HttpRequestBuilder contentType(String contentType) {
            httpRequestBase.setHeader(Header.CONTENT_TYPE.getValue(), contentType);
            return this;
        }
        public HttpRequestBuilder contentTypeJson() {
            return contentType(ContentType.JSON.getValue());
        }
        public HttpRequestBuilder contentTypeFormUrlencoded() {
            return contentType(ContentType.FORM_URLENCODED.getValue());
        }

        /**
         * 设置请求内容编码
         *
         * @param contentEncoding
         * @return
         */
        public HttpRequestBuilder contentEncoding(String contentEncoding) {
            httpRequestBase.setHeader(Header.CONTENT_ENCODING.getValue(), contentEncoding);
            return this;
        }

        /**
         * 设置UA
         *
         * @param userAgent
         * @return
         */
        public HttpRequestBuilder userAgent(String userAgent) {
            httpRequestBase.setHeader(Header.USER_AGENT.getValue(), userAgent);
            return this;
        }

        /**
         * 设置请求头 REFERER 信息
         *
         * @param referer
         * @return
         */
        public HttpRequestBuilder referer(String referer) {
            httpRequestBase.setHeader(Header.REFERER.getValue(), referer);
            return this;
        }

        /**
         * 设置请求头origin信息
         *
         * @param origin
         * @return
         */
        public HttpRequestBuilder origin(String origin) {
            httpRequestBase.setHeader(Header.ORIGIN.getValue(), origin);
            return this;
        }

        /**
         * 设置accept请求头信息
         *
         * @param accept
         * @return
         */
        public HttpRequestBuilder accept(String accept) {
            httpRequestBase.setHeader(Header.ACCEPT.getValue(), accept);
            return this;
        }
        public HttpRequestBuilder acceptLanguage(String acceptLanguage) {
            httpRequestBase.setHeader(Header.ACCEPT_LANGUAGE.getValue(), acceptLanguage);
            return this;
        }
        public HttpRequestBuilder acceptEncoding(String acceptEncoding) {
            httpRequestBase.setHeader(Header.ACCEPT_ENCODING.getValue(), acceptEncoding);
            return this;
        }
        public HttpRequestBuilder acceptCharset(String acceptCharset) {
            httpRequestBase.setHeader(Header.ACCEPT_CHARSET.getValue(), acceptCharset);
            return this;
        }

        public HttpRequestBuilder connectionKeepAlive() {
            httpRequestBase.setHeader(Header.CONNECTION.getValue(), "keep-alive");
            return this;
        }

        /**
         * 禁用自动重定向
         */
        public HttpRequestBuilder disableRedirect() {
            httpRequestBase.setConfig(HttpClientUtil.getRequestConfig(false));
            return this;
        }

        /**
         * 设置表单请求体<br/>
         * 会自动将content-type设置为form-url-encode
         */
        public HttpRequestBuilder form(Map<String, ?> paramMap) {
            if (isGetMethod()) {
                if (CollUtil.isNotEmpty(paramMap)) {
                    List<NameValuePair> list = new ArrayList<>();
                    for (Map.Entry<String, ?> param : paramMap.entrySet()) {
                        list.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
                    }
                    URI uri = httpRequestBase.getURI();
                    URIBuilder uriBuilder = new URIBuilder(uri, CharsetUtil.CHARSET_UTF_8);
                    uriBuilder.setParameters(list);
                    try {
                        httpRequestBase.setURI(uriBuilder.build());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (isPostMethod()) {
                List<BasicNameValuePair> bnvpList = paramMap.entrySet().stream().filter(r -> StrUtil.isNotBlank(r.getKey()) && r.getValue() != null).map(r -> new BasicNameValuePair(r.getKey(), r.getValue().toString())).collect(Collectors.toList());
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(bnvpList, CharsetUtil.CHARSET_UTF_8);
                HttpPost httpPost = (HttpPost) httpRequestBase;
                httpPost.setEntity(formEntity);
            }
            return this;
        }

        public HttpRequestBuilder body(String bodyStr) {
            String method = httpRequestBase.getMethod();
            if (RequestMethod.GET.name().equalsIgnoreCase(method)) {
                // get方法什么也不做
            } else if (RequestMethod.POST.name().equalsIgnoreCase(method)) {
                HttpPost httpPost = (HttpPost) httpRequestBase;
                if (JSONUtil.isTypeJSON(bodyStr)) {
                    StringEntity stringEntity = new StringEntity(bodyStr, CharsetUtil.CHARSET_UTF_8);
                    stringEntity.setContentType(ContentType.JSON.getValue());
                    httpPost.setEntity(stringEntity);
                }
            } else if (RequestMethod.PUT.name().equalsIgnoreCase(method)) {
                HttpPut httpPut = (HttpPut) httpRequestBase;
                if (JSONUtil.isTypeJSON(bodyStr)) {
                    StringEntity stringEntity = new StringEntity(bodyStr, CharsetUtil.CHARSET_UTF_8);
                    stringEntity.setContentType(ContentType.JSON.getValue());
                    httpPut.setEntity(stringEntity);
                }
            } else {
                // 什么也不做
            }
            return this;
        }

        public HttpRequestBase build() {
            return httpRequestBase;
        }

        /**
         * 发送请求
         */
        public MyHttpResponse execute() {
            return HttpClientUtil.doHttp(httpRequestBase);
        }

    }

}
