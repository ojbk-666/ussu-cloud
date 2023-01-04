package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.exception.fruit.InitForFarmException;
import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.JdFruitInviteCodeVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.FriendListInitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForTurntableFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.TaskInitForFarmResponse;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface JdFruitService {

    Logger logger = LoggerFactory.getLogger(JdFruitService.class);

    String JD_API_HOST = "https://api.m.jd.com/client.action";
    // 超时时间
    int TIMEOUT = 10000;

    default String getUserAgent() {
        return "jdapp;iPhone;11.2.8;;;M/5.0;appBuild/168328;jdSupportDarkMode/0;ef/1;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/0;";
    }

    /**
     * 获取东东农场的助力码缓存key
     */
    default String getJdFruitShareCodeCacheKey() {
        return "sheep:share_code:jd_fruit:" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
    }

    default MyHttpRequest.HttpRequestBuilder taskUrl(String ckstr, Method method, String functionId, String body) {
        body = StrUtil.blankToDefault(body, "{}");
        String url = StrUtil.format("{}?functionId={}&body={}&appid=wh5", JD_API_HOST, functionId, URLEncodeUtil.encodeAll(body));
        // HttpRequest httpRequest = HttpUtil.createRequest(method, url)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .header(Header.ORIGIN, "https://carry.m.jd.com")
        //         .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
        //         .header(Header.REFERER, "https://carry.m.jd.com/")
        //         .cookie(ckstr)
        //         .timeout(TIMEOUT);
        // return httpRequest;
        return MyHttpRequest.create(method).uri(url)
                .host("api.m.jd.com").accept("*/*").origin("https://carry.m.jd.com")
                .userAgent(getUserAgent()).acceptEncoding("gzip, deflate, br").acceptLanguage("zh-CN,zh-Hans;q=0.9")
                .referer("https://carry.m.jd.com/")
                .connectionKeepAlive()
                .disableCookie().cookie(ckstr);
    }

    default <T> T request(String ckval, Class<T> t, String functionId, String body) throws JdCookieExpiredException, RequestErrorException {
        // HttpResponse execute = httpRequest.execute();
        MyHttpResponse execute = taskUrl(ckval, Method.GET, functionId, body).execute();
        String responseBody = execute.body();
        if (execute.isOk()) {
            JdBaseResponse jdBaseResponse = JSONUtil.toBean(responseBody, JdBaseResponse.class);
            if (jdBaseResponse.isSuccess()) {
                return JSONUtil.toBean(responseBody, t);
            } else if ("3".equals(jdBaseResponse.getCode())) {
                // 没有登录
                throw new JdCookieExpiredException();
            } else {
                return JSONUtil.toBean(responseBody, t);
            }
        } else {
            throw new RequestErrorException("东东农场发起请求失败 HTTP状态码：" + execute.getStatus());
        }
    }

    default <T> T request(String ckval, Class<T> t, String functionId, JdBodyParam p) throws JdCookieExpiredException, RequestErrorException {
        return request(ckval, t, functionId, p.getBody());
    }

    default <T> T request(String ckval, Class<T> t, String functionId) throws JdCookieExpiredException, RequestErrorException {
        return request(ckval, t, functionId, "{}");
    }

    /**
     * 获取初始化农场数据
     *
     * @param ckval
     * @return
     * @throws JdCookieExpiredException
     * @throws RequestErrorException
     */
    InitForFarmResponse initForFarm(String ckval) throws InitForFarmException, JdCookieExpiredException, RequestErrorException;

    /**
     * 初始化农场数据，充实3次
     * @param ckval
     * @return
     * @throws JdCookieExpiredException
     * @throws RequestErrorException
     */
    InitForFarmResponse tryInitForFarm(String ckval) throws InitForFarmException, JdCookieExpiredException, RequestErrorException;

    /**
     * 获取初始化农场数据
     *
     * @param ck
     * @return
     * @throws JdCookieExpiredException
     * @throws RequestErrorException
     */
    InitForFarmResponse initForFarm(JdCookieVO ck) throws JdCookieExpiredException, RequestErrorException;

    /**
     * 初始化任务列表
     */
    TaskInitForFarmResponse taskInitForFarm(String ckval) throws JdCookieExpiredException, RequestErrorException;

    /**
     * 收集助理码
     */
    void collectInviteCode(List<String> cks);

    /**
     * 获取助力码
     */
    Set<JdFruitInviteCodeVO> getInviteCodes();

    /**
     * 初始化集卡抽奖活动数据API
     */
    InitForTurntableFarmResponse initForTurntableFarm(String ckval);

    /**
     * 获取好友列表
     */
    FriendListInitForFarmResponse friendListInitForFarm(String ckval);
}
