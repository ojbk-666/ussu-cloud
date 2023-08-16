package cc.ussu.common.core.httpclient;

import cc.ussu.common.core.http.IMyHttpResponse;
import cc.ussu.common.core.http.httpclient.HttpClientHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http连接池
 * https://blog.csdn.net/zh_9590/article/details/108050951
 */
@Slf4j
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static CloseableHttpClient httpClient = null;

    static {
        // PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager = new PoolingHttpClientConnectionManager();
        // 总连接池数量
        connectionManager.setMaxTotal(1500);
        // 可为每个域名设置单独的连接池数量
        // connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("127.0.0.1")), 500);
        connectionManager.setDefaultMaxPerRoute(150);  // 这个必须设置，默认2，也就是单个路由最大并发数是2

        // setTcpNoDelay 是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
        // setSoReuseAddress 是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
        // setSoLinger 关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
        // setSoTimeout 接收数据的等待超时时间，单位ms
        // setSoKeepAlive 开启监视TCP连接是否有效
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .setSoLinger(60)
                .setSoTimeout(500)
                .setSoKeepAlive(true)
                .build();
        connectionManager.setDefaultSocketConfig(socketConfig);

        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig requestConfig = getRequestConfig();

        // 重试处理器，StandardHttpRequestRetryHandler这个是官方提供的，看了下感觉比较挫，很多错误不能重试，可自己实现HttpRequestRetryHandler接口去做
//        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
        // 关闭重试策略
        HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);

        // 自定义请求存活策略
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
            /**
             * 返回时间单位是毫秒
             */
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                return 60 * 1000;
            }
        };

        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(requestRetryHandler)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                // .setDefaultCookieStore(basicCookieStore)
                .build();
    }

    public static RequestConfig getRequestConfig(boolean allowRedirect) {
        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000);
        // 默认允许重定向
        builder.setRedirectsEnabled(allowRedirect);
        return builder.build();
    }

    public static RequestConfig getRequestConfig() {
        return getRequestConfig(true);
    }

    /**
     * 关闭连接池
     */
    private static void shutdown() {
        connectionManager.shutdown();
    }

    /**
     * 关闭资源
     */
    private static void tryClose(CloseableHttpResponse response, HttpRequestBase httpRequestBase) {
        try {
            if (null != response) {
                response.close();
            }
        } catch (IOException e) {
            log.error("CloseableHttpClient-post-请求异常,释放连接异常", e);
        }
        try {
            if (null != httpRequestBase) {
                httpRequestBase.releaseConnection();
            }
        } catch (Exception e) {
            log.error("CloseableHttpClient-post-请求异常,释放连接异常", e);
        }
    }

    public static boolean isOk(CloseableHttpResponse response) {
        return HttpStatus.SC_OK == response.getStatusLine().getStatusCode();
    }

    public static IMyHttpResponse doHttpIMyHttpResponse(HttpRequestBase httpRequestBase) {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequestBase);
            Header[] allHeaders = response.getAllHeaders();
            Map<String, List<String>> headersMap = new HashMap<>();
            for (Header header : allHeaders) {
                String headerName = header.getName();
                String headerValue = header.getValue();
                List<String> tempList = headersMap.get(headerName);
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(headerValue);
                headersMap.put(headerName, tempList);
            }
            // 封装自定义返回
            return new HttpClientHttpResponse()
                    .setByteArray(response.getEntity() == null ? null : EntityUtils.toByteArray(response.getEntity()))
                    .setStatus(response.getStatusLine().getStatusCode())
                    .setHeaders(headersMap);
        } catch (Exception e) {
            log.error("CloseableHttpClient-请求异常", e);
        } finally {
            tryClose(response, httpRequestBase);
        }
        return null;
    }

    /**
     * 请求
     */
    public static MyHttpResponse doHttp(HttpRequestBase httpRequestBase) {
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpRequestBase);
            Header[] allHeaders = response.getAllHeaders();
            Map<String, List<String>> headersMap = new HashMap<>();
            for (Header header : allHeaders) {
                String headerName = header.getName();
                String headerValue = header.getValue();
                List<String> tempList = headersMap.get(headerName.toLowerCase());
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(headerValue);
                headersMap.put(headerName.toLowerCase(), tempList);
            }
            // 封装自定义返回
            return new MyHttpResponse()
                    .setStatus(response.getStatusLine().getStatusCode())
                    .setHeaders(headersMap)
                    .setByteArray(response.getEntity() == null ? null : EntityUtils.toByteArray(response.getEntity()));
        } catch (Exception e) {
            log.error("CloseableHttpClient-请求异常", e);
        } finally {
            tryClose(response, httpRequestBase);
        }
        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // String url = "https://json6.cn/";
        // HttpRequestBase build = MyHttpRequest.createGet(url).build();
        // MyHttpResponse myHttpResponse = HttpClientUtil.doHttp(build);
        // System.out.println(myHttpResponse.isOk());
        // MyHttpResponse execute = MyHttpRequest.createGet(url).execute();
        // if (execute.isOk()) {
        //     System.out.println(execute.body());
        // }
        // 重定向测试
        // MyHttpResponse execute = MyHttpRequest.createGet("http://42.192.42.203:5678").disableRedirect().execute();
        // System.out.println(execute);
        // 文件头大小请求测试
        // MyHttpResponse put = MyHttpRequest.create(Method.HEAD,"https://img.seasmall.top/%E5%BF%83%E6%AC%B2%E5%91%90%E5%96%8A%20%E4%B8%BB%E9%A2%98%E6%9B%B2.mp4").execute();
        // System.out.println(put.getStatus());

        // MyHttpRequest.downloadFileFromUrl("https://img.seasmall.top/huajibeizi.jpg", new File("D:\\abc"));
    }

}
