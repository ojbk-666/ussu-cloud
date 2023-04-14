package cc.ussu.common.core.http.httpclient;

import cc.ussu.common.core.http.IMyHttpRequest;
import cc.ussu.common.core.http.IMyHttpResponse;
import cc.ussu.common.core.httpclient.HttpClientUtil;
import cc.ussu.common.core.util.JsonUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HttpClientHttpRequest implements IMyHttpRequest {

    private static final ThreadLocal<HttpRequestBase> threadLocal = new ThreadLocal<>();

    @Override
    public IMyHttpRequest create(String url, String method) {
        HttpRequestBase httpRequestBase = new HttpRequestBase() {
            @Override
            public String getMethod() {
                return method;
            }
        };
        if (StrUtil.isNotBlank(url)) {
            try {
                httpRequestBase.setURI(new URIBuilder(url, CharsetUtil.CHARSET_UTF_8).build());
            } catch (URISyntaxException e) {
                log.error("创建httpclient失败：{}", e.getMessage(), e);
            }
        }
        threadLocal.set(httpRequestBase);
        return this;
    }

    @Override
    public IMyHttpRequest createGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        threadLocal.set(httpGet);
        return this;
    }

    @Override
    public IMyHttpRequest createPost(String url) {
        HttpPost httpGet = new HttpPost(url);
        threadLocal.set(httpGet);
        return this;
    }

    @Override
    public IMyHttpRequest header(String name, String value, boolean isOverride) {
        if (isOverride) {
            threadLocal.get().setHeader(name, value);
        } else {
            threadLocal.get().addHeader(name, value);
        }
        return this;
    }

    @Override
    public IMyHttpRequest headerMap(Map<String, String> headers, boolean isOverride) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            header(entry.getKey(), entry.getValue(), isOverride);
        }
        return this;
    }

    @Override
    public IMyHttpRequest disableCookie() {
        threadLocal.get().removeHeaders("Cookie");
        return this;
    }

    @Override
    public IMyHttpRequest cookie(String cookie) {
        threadLocal.get().setHeader("Cookie", cookie);
        return this;
    }

    @Override
    public IMyHttpRequest contentType(String contentType) {
        return header("Content-Type", contentType, true);
    }

    @Override
    public IMyHttpRequest contentTypeJson() {
        return contentType(ContentType.APPLICATION_JSON.toString());
    }

    @Override
    public IMyHttpRequest contentTypeFormUrlencoded() {
        return contentType(ContentType.APPLICATION_FORM_URLENCODED.toString());
    }

    @Override
    public IMyHttpRequest contentEncoding(String contentEncoding) {
        return header("Content-Encoding", contentEncoding, true);
    }

    @Override
    public IMyHttpRequest userAgent(String userAgent) {
        return header("User-Agent", userAgent, true);
    }

    @Override
    public IMyHttpRequest referer(String referer) {
        return header("Referer", referer, true);
    }

    @Override
    public IMyHttpRequest origin(String origin) {
        return header("Origin", origin, true);
    }

    @Override
    public IMyHttpRequest disableRedirect() {
        threadLocal.get().setConfig(HttpClientUtil.getRequestConfig(false));
        return this;
    }

    @Override
    public IMyHttpRequest form(Map<String, Object> paramMap) {
        HttpRequestBase httpRequestBase = threadLocal.get();
        if ("GET".equalsIgnoreCase(httpRequestBase.getMethod())) {
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
        } else if ("POST".equalsIgnoreCase(httpRequestBase.getMethod())) {
            List<BasicNameValuePair> bnvpList = paramMap.entrySet().stream().filter(r -> StrUtil.isNotBlank(r.getKey()) && r.getValue() != null).map(r -> new BasicNameValuePair(r.getKey(), r.getValue().toString())).collect(Collectors.toList());
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(bnvpList, CharsetUtil.CHARSET_UTF_8);
            HttpPost httpPost = (HttpPost) httpRequestBase;
            httpPost.setEntity(formEntity);
        }
        return this;
    }

    @Override
    public IMyHttpRequest body(String bodyStr) {
        HttpRequestBase httpRequestBase = threadLocal.get();
        String method = httpRequestBase.getMethod();
        if (RequestMethod.GET.name().equalsIgnoreCase(method)) {
            // get方法什么也不做
        } else if (RequestMethod.POST.name().equalsIgnoreCase(method)) {
            HttpPost httpPost = (HttpPost) httpRequestBase;
            if (JsonUtils.isTypeJSON(bodyStr)) {
                StringEntity jsonEntity = new StringEntity(bodyStr, CharsetUtil.CHARSET_UTF_8);
                jsonEntity.setContentType(cn.hutool.http.ContentType.JSON.getValue());
                httpPost.setEntity(jsonEntity);
            } else {
                StringEntity stringEntity = new StringEntity(bodyStr, CharsetUtil.CHARSET_UTF_8);
                stringEntity.setContentType(cn.hutool.http.ContentType.FORM_URLENCODED.getValue());
                httpPost.setEntity(stringEntity);
            }
        } else if (RequestMethod.PUT.name().equalsIgnoreCase(method)) {
            HttpPut httpPut = (HttpPut) httpRequestBase;
            if (JsonUtils.isTypeJSON(bodyStr)) {
                StringEntity stringEntity = new StringEntity(bodyStr, CharsetUtil.CHARSET_UTF_8);
                stringEntity.setContentType(cn.hutool.http.ContentType.JSON.getValue());
                httpPut.setEntity(stringEntity);
            }
        } else {
            // 什么也不做
        }
        return this;
    }

    @Override
    public IMyHttpResponse execute() {
        IMyHttpResponse iMyHttpResponse = null;
        try {
            iMyHttpResponse = HttpClientUtil.doHttpIMyHttpResponse(threadLocal.get());
        } finally {
            threadLocal.remove();
        }
        return iMyHttpResponse;
    }
}
