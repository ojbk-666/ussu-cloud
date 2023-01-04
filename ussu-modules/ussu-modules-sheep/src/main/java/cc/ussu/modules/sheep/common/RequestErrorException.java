package cc.ussu.modules.sheep.common;

import cc.ussu.common.core.httpclient.MyHttpResponse;
import cn.hutool.http.HttpResponse;

public class RequestErrorException extends RuntimeException {

    public RequestErrorException(int status) {
        super("网络异常：" + status);
    }
    @Deprecated
    public RequestErrorException(HttpResponse httpResponse) {
        super("网络异常：" + httpResponse.getStatus() + " -> " + httpResponse.body());
    }
    public RequestErrorException(MyHttpResponse httpResponse) {
        super("网络异常：" + httpResponse.getStatus() + " -> " + httpResponse.body());
    }
    public RequestErrorException(String message) {
        super(message);
    }
}
