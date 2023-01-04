package cc.ussu.modules.sheep.service.impl;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.service.WsKeyToCookieService;
import cc.ussu.modules.sheep.task.jd.exception.wskey.JdWsKeyExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.wskey.GetJDUserInfoUnionResponse;
import cc.ussu.modules.sheep.task.jd.vo.wskey.WsKeyGenTokenCheckApiResponseVO;
import cc.ussu.modules.sheep.task.jd.vo.wskey.WsKeyJdResponse;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WsKeyToCookieServiceImpl implements WsKeyToCookieService {

    private static final Logger log = LoggerFactory.getLogger(WsKeyToCookieServiceImpl.class);

    /**
     * getUa
     */
    @Override
    public String getUserAgent() {
        return "jdapp;iPhone;11.2.8;;;M/5.0;appBuild/168328;jdSupportDarkMode/0;ef/1;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/0;";
    }

    /**
     * 检查ck有效性
     *
     * @param ck
     */
    @Override
    public boolean checkCk(JdCookieVO ck) {
        String url = "https://me-api.jd.com/user_new/info/GetJDUserInfoUnion";
        MyHttpResponse execute = MyHttpRequest.createGet(url)
                .userAgent(getUserAgent())
                .disableCookie()
                .cookie(ck.toString())
                .header("Referer", "https://home.m.jd.com/myJd/home.action")
                .execute();
        if (execute.isOk()) {
            GetJDUserInfoUnionResponse getJDUserInfoUnionResponseVo = JSONUtil.toBean(execute.body(), GetJDUserInfoUnionResponse.class);
            if ("0".equals(getJDUserInfoUnionResponseVo.getRetcode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取远程参数
     */
    @Override
    public WsKeyGenTokenCheckApiResponseVO getRemoteParam() {
        List<String> remoteUrl = new ArrayList<>();
        remoteUrl.add("http://43.135.90.23/");
        remoteUrl.add("https://shizuku.ml/");
        remoteUrl.add("https://cf.shizuku.ml/");
        for (String url : remoteUrl) {
            MyHttpRequest.HttpRequestBuilder requestBuilder = MyHttpRequest.createGet(url + "check_api")
                    .header("authorization", "Bearer Shizuku")
                    .acceptEncoding("gzip, deflate")
                    .userAgent("python-requests/2.23.0")
                    .accept("*/*");
            MyHttpResponse execute = null;
            try {
                execute = requestBuilder.execute();
            } catch (Exception e) {
                continue;
            }
            if (execute.isOk()) {
                    /*{
                        "code": 200,
                            "update": 20318,
                            "jdurl": "https://me-api.jd.com/user_new/info/GetJDUserInfoUnion",
                            "User-Agent": "jdapp;android;10.3.5;;;appBuild/92468;ef/1;ep/{\"hdid\":\"JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw=\",\"ts\":1652330726019,\"ridx\":-1,\"cipher\":{\"sv\":\"CJS=\",\"ad\":\"EJVwDwOyENS3ENdrDwG3CG==\",\"od\":\"EJC0CzG5EQPrYJunEWYmCq==\",\"ov\":\"CzO=\",\"ud\":\"EJVwDwOyENS3ENdrDwG3CG==\"},\"ciphertype\":5,\"version\":\"1.2.0\",\"appname\":\"com.jingdong.app.mall\"};Mozilla/5.0 (Linux; Android 12; M2102K3C Build/SKQ1.211006.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/97.0.4692.98 Mobile Safari/537.36"
                        }*/
                String body = execute.body();
                // log.info("wskey转ck远程参数check_api获取成功：{}", body);
                // 查询参数
                JSONObject jsonObject = JSONUtil.parseObj(body);
                String ua = jsonObject.getStr("User-Agent");
                MyHttpResponse genTokenResponse = MyHttpRequest.createGet(url + "genToken")
                        .header("user-agent", ua)
                        .header("accept-encoding", "gzip, deflate")
                        .header("accept", "*/*")
                        .execute();
                if (genTokenResponse.isOk()) {
                    String genTokenBody = genTokenResponse.body();
                    // log.info("wskey转ck远程参数genToken获取成功：{}", genTokenBody);
                        /*{
                            "functionId": "genToken",
                                "clientVersion": "10.3.5",
                                "build": "92468",
                                "client": "android",
                                "partner": "google",
                                "oaid": "94tcqvfv0lwjp1hc",
                                "sdkVersion": "31",
                                "lang": "zh_CN",
                                "harmonyOs": "0",
                                "networkType": "UNKNOWN",
                                "uemps": "0-2",
                                "ext": "{\"prstate\": \"0\", \"pvcStu\": \"1\"}",
                                "ef": "1",
                                "ep": "{\"hdid\":\"JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw=\",\"ts\":1652330212287,\"ridx\":-1,\"cipher\":{\"d_model\":\"JWunCVVidRTr\",\"wifiBssid\":\"dW5hbw93bq==\",\"osVersion\":\"CJS=\",\"d_brand\":\"WQvrb21f\",\"screen\":\"CJuyCMenCNq=\",\"uuid\":\"ERK4otLnC2ngcW9zoxr1EK==\",\"aid\":\"ERK4otLnC2ngcW9zoxr1EK==\",\"openudid\":\"ERK4otLnC2ngcW9zoxr1EK==\"},\"ciphertype\":5,\"version\":\"1.2.0\",\"appname\":\"com.jingdong.app.mall\"}",
                                "st": 1652330212287,
                                "sign": "cb28991c1f87f72de203b236c5fbe817",
                                "sv": "102"
                            }*/
                    JSONObject param = JSONUtil.toBean(genTokenBody, JSONObject.class);
                    return new WsKeyGenTokenCheckApiResponseVO().setUa(ua).setParam(param);
                } else {
                    throw new RequestErrorException(genTokenResponse);
                }
            } else {
                throw new RequestErrorException("wskey转换：获取远程参数失败");
            }
        }
        throw new ResponseResultException("没有可用远程参数服务");
    }

    @Override
    public WsKeyGenTokenCheckApiResponseVO getRemoteParam(boolean fromProSeasmallTop) {
        if (fromProSeasmallTop) {
            String s = MyHttpRequest.createGet("https://img.seasmall.top/202210/genToken.json").execute().body();
            String ca = MyHttpRequest.createGet("https://img.seasmall.top/202210/check_api.json").execute().body();
            String ua = JSONUtil.parseObj(ca).getStr("User-Agent");
            return new WsKeyGenTokenCheckApiResponseVO().setUa(ua).setParam(JSONUtil.parseObj(s));
        } else {
            return getRemoteParam();
        }
    }

    /**
     * 根据wskey请求ck
     *
     * @param wskey
     */
    @Override
    public JdCookieVO getCkByWskey(String wskey) throws JdWsKeyExpiredException, RequestErrorException, ResponseResultException {
        WsKeyGenTokenCheckApiResponseVO wsKeyGenTokenCheckApiResponseVo = getRemoteParam(true);
        String url = "https://api.m.jd.com/client.action";
        JSONObject remoteParam = wsKeyGenTokenCheckApiResponseVo.getParam();
        String remoteUa = wsKeyGenTokenCheckApiResponseVo.getUa();
        remoteParam.set("body", "{\"to\":\"https%3a%2f%2fplogin.m.jd.com%2fjd-mlogin%2fstatic%2fhtml%2fappjmp_blank.html\"}");
        MyHttpResponse httpResponse = MyHttpRequest.createPost(url)
                .form(remoteParam)
                .disableRedirect()
                .disableCookie()
                .cookie(wskey)
                .header(Header.CONTENT_TYPE.getValue(), "application/x-www-form-urlencoded; charset=UTF-8")
                .header(Header.ACCEPT_ENCODING.getValue(), "br,gzip,deflate")
                .header(Header.USER_AGENT.getValue(), remoteUa)
                .header("charset", CharsetUtil.UTF_8)
                .header("accept", "*/*")
                .execute();
        String b = httpResponse.body();
        WsKeyJdResponse wskeyJdResponseVo = JSONUtil.toBean(b, WsKeyJdResponse.class);
        if (StrUtil.isNotBlank(wskeyJdResponseVo.getTokenKey())) {
            if (StrUtil.equals("xxx", wskeyJdResponseVo.getTokenKey())) {
                // wskey过期了
                throw new JdWsKeyExpiredException();
            } else {
                // String tokenKeyUrl = wskeyJdResponseVo.getUrl() + "?tokenKey=" + wskeyJdResponseVo.getTokenKey() + "&to=https://plogin.m.jd.com/jd-mlogin/static/html/appjmp_blank.html";
                Map<String, Object> form = new JSONObject().set("tokenKey", wskeyJdResponseVo.getTokenKey()).set("to", "https://plogin.m.jd.com/jd-mlogin/static/html/appjmp_blank.html").getRaw();
                HttpResponse execute = HttpUtil.createGet(wskeyJdResponseVo.getUrl())
                        .header(Header.ACCEPT.getValue(), "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("x-requested-with", "com.jingdong.app.mall")
                        .header(Header.USER_AGENT, getUserAgent())
                        .form(form)
                        // .disableRedirect()
                        .setFollowRedirects(false)
                        .disableCookie()
                        .execute();
                if (String.valueOf(execute.getStatus()).startsWith("30")) {
                    String pt_pin = execute.getCookieValue("pt_pin");
                    String pt_key = execute.getCookieValue("pt_key");
                    if (StrUtil.equals("******", URLDecoder.decode(pt_pin, CharsetUtil.CHARSET_UTF_8)) || StrUtil.startWith(pt_key, "fake_")) {
                        throw new JdWsKeyExpiredException();
                    }
                    return new JdCookieVO().setPt_pin(pt_pin).setPt_key(pt_key);
                } else {
                    throw new RequestErrorException(execute);
                }
            }
        } else {
            log.error("wskey转ck失败：" + b);
            throw new ResponseResultException("wskey转ck失败：" + b);
        }
    }

    public static void main(String[] args) {
        String url = "https://un.m.jd.com/cgi-bin/app/appjmp?tokenKey=AAEAMNPSl0V7EYe-Luqu1XEMwlsAn4XeUgRUUl1kHvo5zQk2bjlUpBNCT6RTmmW05jqWAw0&to=https%3A%2F%2Fplogin.m.jd.com%2Fjd-mlogin%2Fstatic%2Fhtml%2Fappjmp_blank.html";
        MyHttpResponse execute = MyHttpRequest.createGet(url).disableCookie().disableRedirect().execute();

    }

}
