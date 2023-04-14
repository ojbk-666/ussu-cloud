package cc.ussu.modules.sheep.task.jdbeans.service.impl;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.ResponseResultException;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jdbeans.service.JdBeansService;
import cc.ussu.modules.sheep.task.jdbeans.util.JdBeansUtil;
import cc.ussu.modules.sheep.task.jdbeans.vo.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 获取资产相关
 */
@Service
public class JdBeansServiceImpl implements JdBeansService {

    @Override
    public JSONObject getStatisticData(String ck) {
        JSONObject jsonObject = new JSONObject();
        if (StrUtil.isNotBlank(ck)) {
            List<CustomData> data = new LinkedList<>();
            TotalBeanResponseVo.TotalBeanResponseDataVo totalBeanResponseDataVo = totalBean(ck);
            jsonObject.set("nickname", totalBeanResponseDataVo.getUserInfo().getBaseInfo().getNickname());
            jsonObject.set("headImageUrl", totalBeanResponseDataVo.getUserInfo().getBaseInfo().getHeadImageUrl());
            String zhxx = JdBeansUtil.getMemberLevel(totalBeanResponseDataVo);
            TotalBean2ResponseVo totalBean2ResponseVo = totalBean2(ck);
            if (totalBean2ResponseVo != null) {
                zhxx += "," + totalBean2ResponseVo.getUser().getUclass();
            }
            jsonObject.set("extUserInfo", zhxx);
            // data.add(new CustomData("账号信息", zhxx));
            /*String dqjd = totalBeanResponseDataVo.getAssetInfo().getBeanNum();*/
            data.add(new CustomData("今日京豆", JdBeansUtil.getMsg(todayBean(ck))));
            data.add(new CustomData("当前京豆", JdBeansUtil.getMsg(totalBeanResponseDataVo)));
            data.add(new CustomData("今日喜豆", JdBeansUtil.getMsg(todayJxBean(ck))));
            data.add(new CustomData("当前喜豆", JdBeansUtil.getMsg(jxBean(ck))));
            // data.add(new CustomData("京喜牧场", ""));
            data.add(new CustomData("极速金币", JdBeansUtil.getMsg(jdSpeedCoin(ck))));
            data.add(new CustomData("京东赚赚", JdBeansUtil.getMsg(getJdZZ(ck))));
            data.add(new CustomData("京东秒杀", JdBeansUtil.getMsg(getMs(ck))));
            JdCashResponseVo.JdCashDataVo jdCashDataVo = jdCash(ck);
            if (jdCashDataVo != null) {
                data.add(new CustomData("其他信息", JdBeansUtil.getMsg(jdCashDataVo)));
            }
            data.add(new CustomData("东东农场", JdBeansUtil.getMsg(jdFruit(ck))));
            data.add(new CustomData("东东萌宠", JdBeansUtil.getMsg(jdPet(ck))));
            // 红包
            RedPacketRespoonseVo redPacket = getRedPacket(ck);
            data.add(new CustomData("红包总额", JdBeansUtil.getMsg(redPacket)));
            data.add(new CustomData("京东红包", JdBeansUtil.getMsgRedPacketJd(redPacket)));
            data.add(new CustomData("京喜红包", JdBeansUtil.getMsgRedPacketJx(redPacket)));
            data.add(new CustomData("极速红包", JdBeansUtil.getMsgRedPacketJs(redPacket)));
            data.add(new CustomData("健康红包", JdBeansUtil.getMsgRedPacketJk(redPacket)));
            jsonObject.set("noticeArr", data);
        }
        return jsonObject;
    }

    @Data
    @Accessors(chain = true)
    public class CustomData {
        public CustomData(String title, String content) {
            this.title = title;
            this.content = content;
        }

        private String title;
        private String content;
    }

    private String getUserAgent() {
        return "jdapp;iPhone;9.4.4;14.3;network/4g;Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/1";
    }

    private Map<String, String> defaultHeader(String cookie) {
        Map<String, String> m = new HashMap<>();
        m.put(Header.COOKIE.getValue(), cookie);
        m.put(Header.USER_AGENT.getValue(), getUserAgent());
        return m;
    }

    private int getTimeout() {
        return 5000;
    }

    private String getJdApiHost() {
        return "https://api.m.jd.com/client.action";
    }

    private long getTime() {
        return new Date().getTime();
    }

    /**
     * 总资产
     *
     * @param cookie
     */
    @Override
    public TotalBeanResponseVo.TotalBeanResponseDataVo totalBean(String cookie) throws JdCookieExpiredException, RequestErrorException, ResponseResultException {
        MyHttpResponse execute = MyHttpRequest.createGet("https://me-api.jd.com/user_new/info/GetJDUserInfoUnion")
                .disableRedirect()
                .disableCookie()
                .cookie(cookie)
                .header(Header.USER_AGENT, getUserAgent())
                .execute();
        if (execute.isOk()) {
            String body = execute.body();
            TotalBeanResponseVo totalBeanResponseVo = JSONUtil.toBean(body, TotalBeanResponseVo.class);
            String retcode = totalBeanResponseVo.getRetcode();
            if ("1001".equals(retcode)) {
                // 登录过期
                throw new JdCookieExpiredException();
            } else if (totalBeanResponseVo.isOk() && totalBeanResponseVo.getData() != null) {
                return totalBeanResponseVo.getData();
            } else {
                throw new ResponseResultException(body);
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 总资产2
     *
     * @param cookie
     */
    @Override
    public TotalBean2ResponseVo totalBean2(String cookie) throws RequestErrorException {
        MyHttpResponse execute = MyHttpRequest.createPost("https://wxapp.m.jd.com/kwxhome/myJd/home.json?&useGuideModule=0&bizId=&brandId=&fromType=wxapp&timestamp=" + new Date().getTime())
                .disableCookie()
                .cookie(cookie)
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.ACCEPT_ENCODING, "gzip,compress,br,deflate")
                .header(Header.REFERER, "https://servicewechat.com/wxa5bf5ee667d91626/161/page-frame.html")
                .header(Header.HOST, "wxapp.m.jd.com")
                .header(Header.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.10(0x18000a2a) NetType/WIFI Language/zh_CN")
                .execute();
        if (execute.isOk()) {
            String body = execute.body();
            TotalBean2ResponseVo totalBean2ResponseVo = JSONUtil.toBean(body, TotalBean2ResponseVo.class);
            if (!totalBean2ResponseVo.isOk()) {
                throw new ResponseResultException(totalBean2ResponseVo.getMessage());
            }
            return totalBean2ResponseVo;
        } else {
            throw new RequestErrorException(execute);
        }
    }

    @Override
    public MyWalletInfoResponse myWalletInfo(String cookie) {
        Map<String, Object> form = new HashMap<>();
        HashMap<String, String> clientInfo = new HashMap<>();
        clientInfo.put("uuid", "a61d4d6c621a4ef8f7dbcdaff9f5daacb7c97021");
        clientInfo.put("un_area", "13_1000_40491_59669");
        clientInfo.put("idfa", "");
        clientInfo.put("brand", "iPhone");
        clientInfo.put("partner", "");
        clientInfo.put("networkType", "wifi");
        clientInfo.put("systemName", "iOS");
        clientInfo.put("systemVersion", "15.4");
        clientInfo.put("idfv", "2631B887-8AE5-4DCA-81B9-B91EF5A920FD");
        clientInfo.put("appVersion", "11.3.2");
        clientInfo.put("model", "14.5");
        clientInfo.put("appBuild", "168346");
        clientInfo.put("client", "iOS");
        clientInfo.put("clientVersion", "11.3.2");
        form.put("reqData", clientInfo);
        MyHttpResponse execute = MyHttpRequest.createPost("https://ms.jr.jd.com/gw2/generic/MyWallet/h5/m/myWalletInfo")
            .disableCookie()
            .cookie(cookie)
            .origin("https://mallwallet.jd.com")
            .referer("https://mallwallet.jd.com/")
            .connectionKeepAlive()
            .form(form)
            .execute();
        if (execute.isOk()) {
            String body = execute.body();
            return JSONUtil.toBean(body, MyWalletInfoResponse.class);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 今日收入京豆
     *
     * @param cookie
     */
    @Deprecated
    @Override
    public TodayBeanVO todayBean(String cookie) {
        int page = 1;
        // 今日收入
        BigDecimal income = new BigDecimal(0);
        // 今日支出
        BigDecimal out = new BigDecimal(0);
        // 收入明细
        List<JdDetailResponseVo.DetailList> incomeList = new LinkedList<>();
        // 支出明细
        List<JdDetailResponseVo.DetailList> outList = new LinkedList<>();
        // 昨日收入
        BigDecimal incomeYesterday = new BigDecimal(0);
        // 昨日支出
        BigDecimal outYesterday = new BigDecimal(0);
        String today = DateUtil.formatDate(new Date());
        String yesterday = DateUtil.formatDate(DateUtil.yesterday());
        while (true && page <= 10) {
            List<JdDetailResponseVo.DetailList> list = getJingBeanDetail(cookie, page);
            if (CollUtil.isEmpty(list)) {
                break;
            }
            for (JdDetailResponseVo.DetailList item : list) {
                String formatDate = DateUtil.formatDate(item.getDate());
                if (today.equals(formatDate)) {
                    if (item.getAmount() > 0) {
                        income = NumberUtil.add(income, item.getAmount());
                        incomeList.add(item);
                    } else {
                        out = NumberUtil.add(out, -item.getAmount());
                        outList.add(item);
                    }
                } else if (yesterday.equals(formatDate)) {
                    if (item.getAmount() > 0) {
                        incomeYesterday = NumberUtil.add(incomeYesterday, item.getAmount());
                    } else {
                        outYesterday = NumberUtil.add(outYesterday, -item.getAmount());
                    }
                } else {
                    break;
                }
            }
            page++;
        }
        return new TodayBeanVO().setIncome(income).setOut(out).setIncomeList(incomeList).setOutList(outList)
                .setIncomeYesterday(incomeYesterday).setOutYesterday(outYesterday);
    }

    /**
     * 今日收入京豆
     *
     * @param cookie
     */
    @Override
    public TodayBeanVO todayBean2(String cookie) {
        int page = 1;
        // 今日收入
        BigDecimal income = new BigDecimal(0);
        // 今日支出
        BigDecimal out = new BigDecimal(0);
        // 收入明细
        List<JdDetailResponseVo.DetailList> incomeList = new LinkedList<>();
        // 支出明细
        List<JdDetailResponseVo.DetailList> outList = new LinkedList<>();
        // 昨日收入
        BigDecimal incomeYesterday = new BigDecimal(0);
        // 昨日支出
        BigDecimal outYesterday = new BigDecimal(0);
        // 明细
        List<JdDetailResponseVo.DetailList> todayDetailList = new LinkedList<>();
        List<JdDetailResponseVo.DetailList> yesterdayDetailList = new LinkedList<>();
        String today = DateUtil.formatDate(new Date());
        String yesterday = DateUtil.formatDate(DateUtil.yesterday());
        a:
        while (page <= 10) {
            List<JdDetailResponseVo.DetailList> list = getJingBeanDetail2(cookie, page);
            if (CollUtil.isEmpty(list)) {
                break;
            }
            b:
            for (JdDetailResponseVo.DetailList item : list) {
                String formatDate = DateUtil.formatDate(item.getDate());
                if (today.equals(formatDate)) {
                    todayDetailList.add(item);
                    if (item.getAmount() > 0) {
                        income = NumberUtil.add(income, item.getAmount());
                        incomeList.add(item);
                    } else {
                        out = NumberUtil.add(out, -item.getAmount());
                        outList.add(item);
                    }
                } else if (yesterday.equals(formatDate)) {
                    yesterdayDetailList.add(item);
                    if (item.getAmount() > 0) {
                        incomeYesterday = NumberUtil.add(incomeYesterday, item.getAmount());
                    } else {
                        outYesterday = NumberUtil.add(outYesterday, -item.getAmount());
                    }
                } else {
                    break a;
                }
            }
            page++;
        }
        return new TodayBeanVO().setIncome(income).setOut(out).setIncomeList(incomeList).setOutList(outList)
            .setIncomeYesterday(incomeYesterday).setOutYesterday(outYesterday)
            .setTodayDetailList(todayDetailList).setYesterdayDetailList(yesterdayDetailList);
    }

    public static void main(String[] args) {
        String c = "pt_pin=jd_sAcAzzgtBSBX;pt_key=app_openAAJimE0zADB0dun5JM2mhdjLY_4lxCXBc2xdcHXe4v-IoUYr6CMrq5SlRpKTwKn3fQVaK-zyaZs;";
        JdBeansServiceImpl service = new JdBeansServiceImpl();
        // Object o = service.totalBean(c);
        TotalBean2ResponseVo totalBean2ResponseVo = service.totalBean2(c);
        // Object jingBeanDetail = service.getJingBeanDetail(c, 1);
        // Object expireJingBean = service.getExpireJingBean(c);
        // service.expireJdExchangeJxBeans(c, 1);
        // Object redPacket = service.getRedPacket(c);
        // Object o = service.getXiBeanDetail(c);
        // Object o = service.todayBean(c);
        // Object o = service.todayJxBean(c);

        /*int page = 1;
        HashMap<String, TodayBeanVO> m = new HashMap<>();
        while (true && page <= 15) {
            List<JdDetailResponseVo.DetailList> list = service.getJingBeanDetail(c, page);
            if (CollUtil.isEmpty(list)) {
                break;
            }
            for (JdDetailResponseVo.DetailList item : list) {
                String formatDate = DateUtil.formatDate(item.getDate());
                TodayBeanVO beanVo = m.get(formatDate);
                if (beanVo == null) {
                    beanVo = new TodayBeanVO().setIncome(new BigDecimal(0)).setOut(new BigDecimal(0));
                    m.put(formatDate, beanVo);
                }
                if (item.getAmount() >= 0) {
                    beanVo.setIncome(NumberUtil.add(beanVo.getIncome(), item.getAmount()));
                } else {
                    beanVo.setOut(NumberUtil.add(beanVo.getOut(), -item.getAmount()));
                }
            }
            page++;
        }
        String pin = ReUtil.get("pin=[\\w]{0,}", c, 0);
        pin = pin.replaceAll("pin=", StrUtil.EMPTY).replaceAll(";", StrUtil.EMPTY);
        for (Map.Entry<String, TodayBeanVO> item : m.entrySet()) {
            System.out.println(StrUtil.format("INSERT INTO `jd_day_beans`(`id`, `jd_user_id`, `create_date`, `income_bean`, `out_bean`) VALUES ('{}', '{}', '{}', {}, {});\n",
                    IdWorker.getIdStr(), pin, item.getKey(), item.getValue().getIncome().intValue(), item.getValue().getOut().intValue()));
        }*/
        System.out.println("-----------------------");
    }

    /**
     * 今日喜豆收入
     *
     * @param cookie
     */
    @Override
    public TodayJxBeanVo todayJxBean(String cookie) {
        List<TodayJxBeanDetailResponseVo.Detail> detailList = getXiBeanDetail(cookie);
        if (CollUtil.isNotEmpty(detailList)) {
            BigDecimal income = new BigDecimal(0);
            BigDecimal out = new BigDecimal(0);
            BigDecimal incomeYesterday = new BigDecimal(0);
            BigDecimal outYesterday = new BigDecimal(0);
            Date yesterday = new Date();
            String today = DateUtil.formatDate(new Date());
            for (TodayJxBeanDetailResponseVo.Detail item : detailList) {
                Integer amount = item.getAmount();
                String formatDate = DateUtil.formatDate(item.getCreatedate());
                if (today.equals(formatDate)) {
                    // 今日
                    if (amount > 0) {
                        income = NumberUtil.add(income, amount);
                    } else {
                        out = NumberUtil.add(out, -amount);
                    }
                } else if (yesterday.equals(formatDate)) {
                    // 昨日
                    if (amount > 0) {
                        incomeYesterday = NumberUtil.add(incomeYesterday, amount);
                    } else {
                        outYesterday = NumberUtil.add(outYesterday, -amount);
                    }
                }
            }
            return new TodayJxBeanVo().setIncome(income).setOut(out)
                    .setIncomeYesterday(incomeYesterday).setOutYesterday(outYesterday);
        }
        return null;
    }

    /**
     * 查询京豆详情
     *
     * @param cookie
     */
    @Deprecated
    @Override
    public List<JdDetailResponseVo.DetailList> getJingBeanDetail(String cookie, Integer page) throws RequestErrorException, JdCookieExpiredException, ResponseResultException {
        if (page == null || page < 1) {
            page = 1;
        }
        String url = "https://api.m.jd.com/client.action?functionId=getJingBeanBalanceDetail";
        JSONObject bodyObj = new JSONObject().set("pageSize", "20").set("page", page + "");
        String body = "body=" + URLEncodeUtil.encode(bodyObj.toString()) + "&appid=ld";
        MyHttpResponse execute = MyHttpRequest.createGet(url + "&" + body)
                // .body(body)
                .disableCookie()
                .cookie(cookie)
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.USER_AGENT, getUserAgent())
                .execute();
        if (execute.isOk()) {
            JdDetailResponseVo jdDetailResponseVo = JSONUtil.toBean(execute.body(), JdDetailResponseVo.class);
            if ("0".equals(jdDetailResponseVo.getCode())) {
                List<JdDetailResponseVo.DetailList> detailList = jdDetailResponseVo.getDetailList();
                return detailList;
            } else {
                throw new JdCookieExpiredException();
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 查询京豆详情
     *
     * @param cookie
     */
    @Deprecated
    @Override
    public List<JdDetailResponseVo.DetailList> getJingBeanDetail2(String cookie, Integer page) throws RequestErrorException, JdCookieExpiredException, ResponseResultException {
        if (page == null || page < 1) {
            page = 1;
        }
        String url = "https://bean.m.jd.com/beanDetail/detail.json";
        Map<String, Object> form = new HashMap<>();
        form.put("page", page);
        MyHttpResponse execute = MyHttpRequest.createGet(url)
                .host("bean.m.jd.com")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .form(form)
                .accept("application/json, text/javascript, */*; q=0.01")
                .origin("https://bean.m.jd.com")
                .referer("https://bean.m.jd.com/beanDetail/index.action?resourceValue=orderDetail&sid=61f5230a75bfa646832662d3951ef63w&")
                .header(Header.USER_AGENT, getUserAgent())
                .disableCookie()
                .cookie(cookie)
                .execute();
        if (execute.isOk()) {
            JdDetailResponseVo jdDetailResponseVo = JSONUtil.toBean(execute.body(), JdDetailResponseVo.class);
            if ("0".equals(jdDetailResponseVo.getCode())) {
                List<JdDetailResponseVo.DetailList> detailList = jdDetailResponseVo.getJingDetailList();
                return detailList;
            } else {
                throw new JdCookieExpiredException();
            }
        } else {
            throw new RequestErrorException(execute);
        }
    }

    /**
     * 查询喜豆详情
     *
     * @param cookie
     */
    @Override
    public List<TodayJxBeanDetailResponseVo.Detail> getXiBeanDetail(String cookie) {
        String url = "https://m.jingxi.com/activeapi/queryuserjingdoudetail?pagesize=10&type=16&_=" + getTime() + 2 + "&sceneval=2&g_login_type=1&" +
                "callback=jsonpCBK2A" + "&g_ty=ls";
        String jxua = "jdpingou;iPhone;4.13.0;14.4.2;" + RandomUtil.randomString(40) + ";network/wifi;model/iPhone10,2;appBuild/100609;supportApplePay/1;hasUPPay/0;pushNoticeIsOpen/1;hasOCPay/0;supportBestPay/0;session/" +
                RandomUtil.randomInt(99) + ";pap/JA2019_3111789;brand/apple;supportJDSHWK/1;Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148";
        HttpResponse execute = HttpUtil.createGet(url)
                .disableCookie()
                .cookie(cookie)
                .header(Header.HOST, "m.jingxi.com")
                .header(Header.ACCEPT, "*/*")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.USER_AGENT, jxua)
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.REFERER, "https://st.jingxi.com/")
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            String body = execute.body();
            String json = ReUtil.get("jsonpCBK2.?\\((.*);*", body, 1).replaceAll("\n", StrUtil.EMPTY).replaceAll("\\);}catch\\(e\\)\\{}", StrUtil.EMPTY);
            TodayJxBeanDetailResponseVo jxBeanDetailResponseVo = JSONUtil.toBean(json, TodayJxBeanDetailResponseVo.class);
            if (0 == jxBeanDetailResponseVo.getRet()) {
                return jxBeanDetailResponseVo.getDetail();
            } else {
                throw new JdCookieExpiredException();
            }
        } else {
            throw new JdCookieExpiredException();
        }
        // return null;
    }

    /**
     * 查询过期京豆
     */
    @Override
    public List<JdExpireResponseVo.Expirejingdou> getExpireJingBean(String ck) {
        String url = "https://wq.jd.com/activep3/singjd/queryexpirejingdou?_=" + getTime() + "&g_login_type=1&sceneval=2";
        HttpResponse execute = HttpUtil.createGet(url)
                .disableCookie()
                .cookie(ck)
                .header(Header.ACCEPT, "*/*")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.HOST, "wq.jd.com")
                .header(Header.REFERER, "https://wqs.jd.com/promote/201801/bean/mybean.html")
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            String s = execute.body().replaceAll("try\\{QueryExpireJingdou\\(", StrUtil.EMPTY)
                    .replaceAll("\\);}catch(e)\\{}", StrUtil.EMPTY).replaceAll("\n", StrUtil.EMPTY);
            JdExpireResponseVo jdExpireResponseVo = JSONUtil.toBean(s, JdExpireResponseVo.class);
            if (0 == jdExpireResponseVo.getRet()) {
                List<JdExpireResponseVo.Expirejingdou> expirejingdouList = jdExpireResponseVo.getExpirejingdou();
                return expirejingdouList;
            }
        }
        return null;
    }

    /**
     * 过期京豆兑换喜豆
     */
    @Deprecated
    @Override
    public Object expireJdExchangeJxBeans(String ck, Integer expireJdNum) {
        if (expireJdNum == null || expireJdNum < 0) {
            expireJdNum = 0;
        }
        String url = "https://m.jingxi.com/deal/masset/jd2xd?use=" + expireJdNum + "&canpintuan=&setdefcoupon=0&r=" + Math.random() + "&sceneval=2";
        String jxua = "jdpingou;iPhone;4.13.0;14.4.2;" + RandomUtil.randomString(40)
                + ";network/wifi;model/iPhone10,2;appBuild/100609;ADID/00000000-0000-0000-0000-000000000000;supportApplePay/1;hasUPPay/0;pushNoticeIsOpen/1;hasOCPay/0;supportBestPay/0;session/"
                + RandomUtil.randomInt(99) + Math.random()
                + ";pap/JA2019_3111789;brand/apple;supportJDSHWK/1;Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148";
        HttpResponse execute = HttpUtil.createGet(url)
                .disableCookie()
                .cookie(ck)
                .header(Header.HOST, "m.jingxi.com")
                .header(Header.ACCEPT, "*/*")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.USER_AGENT, jxua)
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.REFERER, "https://m.jingxi.com/deal/confirmorder/main/")
                .header(Header.ORIGIN, "https://carry.m.jd.com")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            ExpireJdExchangeJxBeansResponseVo exchangeJxBeansResponseVo = JSONUtil.toBean(execute.body(), ExpireJdExchangeJxBeansResponseVo.class);
            System.out.println(exchangeJxBeansResponseVo);
        }
        return null;
    }

    /**
     * 获取红包金额
     *
     * @param ck
     */
    @Override
    public RedPacketRespoonseVo getRedPacket(String ck) {
        String url = "https://m.jingxi.com/user/info/QueryUserRedEnvelopesV2?type=1&orgFlag=JD_PinGou_New&page=1&cashRedType=1&redBalanceFlag=1&channel=1&_=" + getTime() + "&sceneval=2&g_login_type=1&g_ty=ls";
        HttpResponse execute = HttpUtil.createGet(url)
                .disableCookie()
                .cookie(ck)
                .header(Header.HOST, "m.jingxi.com")
                .header(Header.ACCEPT, "*/*")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.REFERER, "https://st.jingxi.com/my/redpacket.shtml?newPg=App&jxsid=16156262265849285961")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.USER_AGENT, getUserAgent())
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            RedPacketRespoonseVo redPacketRespoonseVo = JSONUtil.toBean(execute.body(), RedPacketRespoonseVo.class);
            return redPacketRespoonseVo;
        }
        return null;
    }

    /**
     * 汪汪乐园
     *
     * @param cookie
     */
    @Override
    public void getJoyPark(String cookie) {
        String url = "https://api.m.jd.com/client.action?functionId=joyBaseInfo";
        String body = "body={\"taskId\":\"{}\",\"inviteType\":\"{}\",\"inviterPin\":\"{}\",\"linkId\":\"LsQNxL7iWDlXUs6cFl-AAg\"}&appid=activities_platform";
        HttpResponse execute = HttpUtil.createPost(url).body(StrUtil.format(body, "", "", ""))
                .disableCookie()
                .cookie(cookie)
                .header(Header.HOST, "wxapp.m.jd.com")
                .header(Header.ACCEPT, "*/*")
                .header(Header.ORIGIN, "https://carry.m.jd.com")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.10(0x18000a2a) NetType/WIFI Language/zh_CN")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.REFERER, "https://carry.m.jd.com/")
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            String body1 = execute.body();

        }
    }

    /**
     * 京东赚赚
     *
     * @param cookie
     */
    @Override
    public JdzzResponseVo.JdzzDataResponseVo getJdZZ(String cookie) {
        String url = getJdApiHost() + "?functionId=interactTaskIndex&body=" + URLEncodeUtil.encode("{}") + "&client=wh5&clientVersion=9.1.0";
        HttpResponse execute = HttpUtil.createGet(url)
                .disableCookie()
                .cookie(cookie)
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .header(Header.REFERER, "http://wq.jd.com/wxapp/pages/hd-interaction/index/index")
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.ACCEPT_LANGUAGE, "zh-cn")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            JdzzResponseVo jdzzResponseVo = JSONUtil.toBean(execute.body(), JdzzResponseVo.class);
            if (jdzzResponseVo.getCode().equals("0")) {
                // 总金币
                String totalNum = jdzzResponseVo.getData().getTotalNum();
                // 可体现xx元
                String cashExpected = jdzzResponseVo.getData().getCashExpected();
                return jdzzResponseVo.getData();
            }
        }
        return null;
    }

    /**
     * 京东秒杀
     *
     * @param cookie
     */
    @Override
    public JdMsResponseVo.JdMsResultAssignmentVo getMs(String cookie) {
        String url = getJdApiHost() + "?functionId=homePageV2&body=" + URLEncodeUtil.encode(JSONUtil.toJsonStr(new HashMap<>()))
                + "&client=wh5&clientVersion=1.0.0&" + "appid=SecKill2020";
        HttpResponse execute = HttpUtil.createPost(url)
                .disableCookie()
                .cookie(cookie)
                .header(Header.ORIGIN, "https://h5.m.jd.com")
                .header(Header.REFERER, "https://h5.m.jd.com/babelDiy/Zeus/2NUvze9e1uWf4amBhe1AV6ynmSuH/index.html")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.USER_AGENT, getUserAgent())
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            JdMsResponseVo msResponseVo = JSONUtil.toBean(execute.body(), JdMsResponseVo.class);
            JdMsResponseVo.JdMsResultAssignmentVo assignment = msResponseVo.getResult().getAssignment();
            Integer assignmentPoints = assignment.getAssignmentPoints();
            return assignment;
        }
        return null;
    }

    /**
     * 东东农场
     *
     * @param cookie
     */
    @Override
    public JdFruitInitForFarmResponseVo jdFruit(String cookie) {
        // Map<String, Object> p = new HashMap<>();
        // p.put("version", 14);
        // p.put("channel", 1);
        // p.put("babelChannel", "120");
        // String url = getJdApiHost() + "?functionId=taskInitForFarm&body=" + URLEncodeUtil.encode(JSONUtil.toJsonStr(p)) + "&appid=wh5";
        // HttpResponse execute = HttpUtil.createGet(url)
        //         .header(Header.COOKIE, cookie)
        //         .header(Header.HOST, "api.m.jd.com")
        //         .header(Header.ACCEPT, "*/*")
        //         .header(Header.ORIGIN, "https://carry.m.jd.com")
        //         .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
        //         .header(Header.USER_AGENT, getUserAgent())
        //         .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
        //         .header(Header.REFERER, "https://carry.m.jd.com/")
        //         // .header(Header.CONNECTION, "keep-alive")
        //         .timeout(getTimeout())
        //         .execute();
        // if (execute.isOk()) {
        // }
        // JdFruitTaskInitForFarmResponseVo jdFruitTaskInitForFarmResponseVo = JSONUtil.toBean(execute.body(), JdFruitTaskInitForFarmResponseVo.class);
        // Integer totalWaterTaskTimes = jdFruitTaskInitForFarmResponseVo.getTotalWaterTaskInit().getTotalWaterTaskTimes();
        // 另一个请求
        Map<String, Object> p1 = new HashMap<>();
        p1.put("version", 4);
        String u = getJdApiHost() + "?functionId=initForFarm&body=" + URLEncodeUtil.encode(JSONUtil.toJsonStr(p1)) + "&appid=wh5&clientVersion=9.1.0";
        HttpResponse execute1 = HttpUtil.createPost(u)
                .header(Header.ACCEPT, "*/*")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.CACHE_CONTROL, "no-cache")
                .disableCookie()
                .cookie(cookie)
                .header(Header.ORIGIN, "https://home.m.jd.com")
                .header(Header.PRAGMA, "no-cache")
                .header(Header.REFERER, "https://home.m.jd.com/myJd/newhome.action")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-site")
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .timeout(getTimeout())
                .execute();
        if (execute1.isOk()) {
            JdFruitInitForFarmResponseVo initForFarmResponseVo = JSONUtil.toBean(execute1.body(), JdFruitInitForFarmResponseVo.class);
            return initForFarmResponseVo;
        }
        return null;
    }

    /**
     * 极速金币
     *
     * @param cookie
     */
    @Override
    public JdSpeedCoinResponseVo.JdSpeedCoinDataVo jdSpeedCoin(String cookie) {
        long time = new Date().getTime();
        Map<String, Object> body = new HashMap<>();
        body.put("method", "userCashRecord");
        Map<String, Object> p = new HashMap<>();
        p.put("channel", 1);
        p.put("pageNum", 1);
        p.put("pageSize", 20);
        body.put("data", p);
        String data = "lite-android&" + JSONUtil.toJsonStr(body) + "&android&3.1.0&MyAssetsService.execute&" + time + "&846c4c32dae910ef";
        String sign = SecureUtil.hmacSha256("12aea658f76e453faf803d15c40a72e0").digestHex(data);
        String url = getJdApiHost() + "api?functionId=MyAssetsService.execute&body=" + URLEncodeUtil.encode(JSONUtil.toJsonStr(body))
                + "&appid=lite-android&client=android&uuid=846c4c32dae910ef&clientVersion=3.1.0&t=" + time + "&sign=" + sign;
        HttpResponse execute = HttpUtil.createGet(url)
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.ACCEPT, "*/*")
                .header("kernelplatform", "RN")
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.ACCEPT_LANGUAGE, "zh-Hans-CN;q=1, ja-CN;q=0.9")
                .disableCookie()
                .cookie(cookie)
                .execute();
        if (execute.isOk()) {
            JdSpeedCoinResponseVo speedCoinResponseVo = JSONUtil.toBean(execute.body(), JdSpeedCoinResponseVo.class);
            return speedCoinResponseVo.getData();
        }
        return null;
    }

    private HttpResponse petRequest(String ck, String functionId) {
        return petRequest(ck, functionId, null);
    }

    private HttpResponse petRequest(String ck, String functionId, JSONObject body) {
        String url = getJdApiHost() + "?functionId=" + functionId;
        if (body == null) {
            body = new JSONObject();
        }
        body.set("version", 2).set("channel", "app");
        HttpResponse execute = HttpUtil.createPost(url)
                .body("body=" + URLEncodeUtil.encode(body.toString()) + "&appid=wh5&loginWQBiz=pet-town&clientVersion=9.0.4")
                .disableCookie()
                .cookie(ck)
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.USER_AGENT, getUserAgent())
                .timeout(getTimeout())
                .execute();
        return execute;
    }

    /**
     * 东东萌宠
     *
     * @param ck
     */
    @Override
    public JdPetInitPetTownResponseVo.JdPetInitPetTownResult jdPet(String ck) {
        // HttpResponse response = petRequest(ck, "energyCollect");
        // if (response.isOk()) {
        //     JdPetEnergyCollectResponseVo energyCollectResponseVo = JSONUtil.toBean(response.body(), JdPetEnergyCollectResponseVo.class);
        // }
        // try {
        //     Thread.sleep(3000);
        // } catch (InterruptedException e) {
        // }
        HttpResponse rep = petRequest(ck, "initPetTown");
        if (rep.isOk()) {
            JdPetInitPetTownResponseVo petTownResponseVo = JSONUtil.toBean(rep.body(), JdPetInitPetTownResponseVo.class);
            if ("0".equals(petTownResponseVo.getCode()) && "0".equals(petTownResponseVo.getResultCode())) {
                return petTownResponseVo.getResult();
            }
        }
        return null;
    }

    private void requestAlgo() {
        String fingerprint = RandomUtil.randomNumbers(16);
        String appId = "10028";
        JSONObject body = new JSONObject()
                .set("version", "1.0")
                .set("fp", fingerprint)
                .set("appId", appId)
                .set("timestamp", new Date().getTime())
                .set("platform", "web")
                .set("expandParams", "");
        HttpResponse execute = HttpUtil.createPost("https://cactus.jd.com/request_algo?g_ty=ajax")
                .header("Authority", "cactus.jd.co")
                .header(Header.PRAGMA, "no-cache")
                .header(Header.CACHE_CONTROL, "no-cache")
                .header(Header.ACCEPT, ContentType.JSON.getValue())
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .header(Header.ORIGIN, "https://st.jingxi.com")
                .header("Sec-Fetch-Site", "cross-site")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header(Header.REFERER, "https://st.jingxi.com/")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9,zh-TW;q=0.8,en;q=0.7")
                .body(JSONUtil.toJsonStr(body))
                .execute();
        if (execute.isOk()) {
            JxmcRequestAlgoResponseVo requestAlgoResponseVo = JSONUtil.toBean(execute.body(), JxmcRequestAlgoResponseVo.class);
            String tk = requestAlgoResponseVo.getData().getResult().getTk();
            String algo = requestAlgoResponseVo.getData().getResult().getAlgo();
        }
    }

    /**
     * 京喜牧场
     *
     * @param ck
     */
    @Override
    public void getJxmc(String ck) {
        requestAlgo();
    }

    /**
     * 京喜工厂
     *
     * @param ck
     */
    @Override
    public void getJxFactory(String ck) {

    }

    /**
     * 京东工厂
     *
     * @param ck
     */
    @Override
    public void getDdFactory(String ck) {
        long time = new Date().getTime();
        // String url = "https://m.jingxi.com/dreamfactory/userinfo/GetUserInfo?zone=dream_factory&"++"&sceneval=2&g_login_type=1&_time=" + time + "&_=" + time + 2 + "&_ste=1";
        // HttpUtil.createGet("")
    }

    /**
     * 领现金
     *
     * @param ck
     */
    @Override
    public JdCashResponseVo.JdCashDataVo jdCash(String ck) {
        String sign2 = getJdCashSign();
        String sign = "body=%7B%7D&build=167968&client=apple&clientVersion=10.4.0&d_brand=apple&d_model=iPhone13%2C3&ef=1&eid=eidI25488122a6s9Uqq6qodtQx6rgQhFlHkaE1KqvCRbzRnPZgP/93P%2BzfeY8nyrCw1FMzlQ1pE4X9JdmFEYKWdd1VxutadX0iJ6xedL%2BVBrSHCeDGV1&ep=%7B%22ciphertype%22%3A5%2C%22cipher%22%3A%7B%22screen%22%3A%22CJO3CMeyDJCy%22%2C%22osVersion%22%3A%22CJUkDK%3D%3D%22%2C%22openudid%22%3A%22CJSmCWU0DNYnYtS0DtGmCJY0YJcmDwCmYJC0DNHwZNc5ZQU2DJc3Zq%3D%3D%22%2C%22area%22%3A%22CJZpCJCmC180ENcnCv80ENc1EK%3D%3D%22%2C%22uuid%22%3A%22aQf1ZRdxb2r4ovZ1EJZhcxYlVNZSZz09%22%7D%2C%22ts%22%3A1648428189%2C%22hdid%22%3A%22JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw%3D%22%2C%22version%22%3A%221.0.3%22%2C%22appname%22%3A%22com.360buy.jdmobile%22%2C%22ridx%22%3A-1%7D&ext=%7B%22prstate%22%3A%220%22%2C%22pvcStu%22%3A%221%22%7D&isBackground=N&joycious=104&lang=zh_CN&networkType=3g&networklibtype=JDNetworkBaseAF&partner=apple&rfs=0000&scope=11&sign=98c0ea91318ef1313786d86d832f1d4d&st=1648428208392&sv=101&uemps=0-0&uts=0f31TVRjBSv7E8yLFU2g86XnPdLdKKyuazYDek9RnAdkKCbH50GbhlCSab3I2jwM04d75h5qDPiLMTl0I3dvlb3OFGnqX9NrfHUwDOpTEaxACTwWl6n//EOFSpqtKDhg%2BvlR1wAh0RSZ3J87iAf36Ce6nonmQvQAva7GoJM9Nbtdah0dgzXboUL2m5YqrJ1hWoxhCecLcrUWWbHTyAY3Rw%3D%3D";
        if (StrUtil.isNotBlank(sign)) {
            String url = getJdApiHost() + "?functionId=cash_homePage&" + sign;
            HttpResponse execute = HttpUtil.createPost(url)
                    .disableCookie()
                    .cookie(ck)
                    .header(Header.HOST, "api.m.jd.com")
                    .header(Header.CONNECTION, "keep-alive")
                    .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                    .header(Header.REFERER, "")
                    .header(Header.USER_AGENT, getUserAgent())
                    .header(Header.ACCEPT_LANGUAGE, "zh-Hans-CN;q=1")
                    .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                    .timeout(getTimeout())
                    .execute();
            if (execute.isOk()) {
                JdCashResponseVo cashResponseVo = JSONUtil.toBean(execute.body(), JdCashResponseVo.class);
                return cashResponseVo.getData();
            }
        }
        return null;
    }

    /**
     * 获取领现金签名
     */
    private String getJdCashSign() {
        String url = "http://cdn.nz.lu/ddo";
        String[] hostArr = new String[]{"jdsign.cf", "signer.nz.lu"};
        Map<String, Object> p = new HashMap<>();
        p.put("functionId", "cash_homePage");
        String body = "body=%7B%7D&build=167968&client=apple&clientVersion=10.4.0&d_brand=apple&d_model=iPhone13%2C3&ef=1&eid=eidI25488122a6s9Uqq6qodtQx6rgQhFlHkaE1KqvCRbzRnPZgP/93P%2BzfeY8nyrCw1FMzlQ1pE4X9JdmFEYKWdd1VxutadX0iJ6xedL%2BVBrSHCeDGV1&ep=%7B%22ciphertype%22%3A5%2C%22cipher%22%3A%7B%22screen%22%3A%22CJO3CMeyDJCy%22%2C%22osVersion%22%3A%22CJUkDK%3D%3D%22%2C%22openudid%22%3A%22CJSmCWU0DNYnYtS0DtGmCJY0YJcmDwCmYJC0DNHwZNc5ZQU2DJc3Zq%3D%3D%22%2C%22area%22%3A%22CJZpCJCmC180ENcnCv80ENc1EK%3D%3D%22%2C%22uuid%22%3A%22aQf1ZRdxb2r4ovZ1EJZhcxYlVNZSZz09%22%7D%2C%22ts%22%3A1648428189%2C%22hdid%22%3A%22JM9F1ywUPwflvMIpYPok0tt5k9kW4ArJEU3lfLhxBqw%3D%22%2C%22version%22%3A%221.0.3%22%2C%22appname%22%3A%22com.360buy.jdmobile%22%2C%22ridx%22%3A-1%7D&ext=%7B%22prstate%22%3A%220%22%2C%22pvcStu%22%3A%221%22%7D&isBackground=N&joycious=104&lang=zh_CN&networkType=3g&networklibtype=JDNetworkBaseAF&partner=apple&rfs=0000&scope=11&sign=98c0ea91318ef1313786d86d832f1d4d&st=1648428208392&sv=101&uemps=0-0&uts=0f31TVRjBSv7E8yLFU2g86XnPdLdKKyuazYDek9RnAdkKCbH50GbhlCSab3I2jwM04d75h5qDPiLMTl0I3dvlb3OFGnqX9NrfHUwDOpTEaxACTwWl6n//EOFSpqtKDhg%2BvlR1wAh0RSZ3J87iAf36Ce6nonmQvQAva7GoJM9Nbtdah0dgzXboUL2m5YqrJ1hWoxhCecLcrUWWbHTyAY3Rw%3D%3D";
        p.put("body", body);
        p.put("client", "apple");
        p.put("clientVersion", "10.3.0");
        HttpResponse execute = HttpUtil.createPost(url)
                .header(Header.HOST, hostArr[RandomUtil.randomInt(2)])
                // .header(Header.USER_AGENT, getUserAgent())
                .header(Header.USER_AGENT, "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/87.0.4280.88")
                .contentType(ContentType.FORM_URLENCODED.getValue())
                .body(JSONUtil.toJsonStr(p))
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            return execute.body();
        }
        return null;
    }

    /**
     * 喜豆查询
     *
     * @param ck
     */
    @Override
    public JxBeanQuerybeanamountResponseVo.JxBeanQuerybeanamountDataVo jxBean(String ck) {
        String url = "https://m.jingxi.com/activeapi/querybeanamount?_=" + new Date().getTime() + 2 +
                "&sceneval=2&g_login_type=1&callback=jsonpCBK" + /*RandomUtil.randomInt(26) + UnicodeUtil.toUnicode("A") +*/ "&g_ty=ls";
        HttpResponse execute = HttpUtil.createGet(url)
                .header(Header.HOST, "m.jingxi.com")
                .header(Header.ACCEPT, "*/*")
                .header(Header.ACCEPT_ENCODING, "gzip,br,deflate")
                .header(Header.USER_AGENT, getUserAgent())
                .header(Header.ACCEPT_LANGUAGE, "zh-CN,zh-Hans;q=0.9")
                .header(Header.REFERER, "https://st.jingxi.com/")
                .disableCookie()
                .cookie(ck)
                .timeout(getTimeout())
                .execute();
        if (execute.isOk()) {
            String replace = execute.body().replaceAll("jsonpCBK\\(", "").replaceAll("\n", "").replace(")", "");
            JxBeanQuerybeanamountResponseVo querybeanamountResponseVo = JSONUtil.toBean(replace, JxBeanQuerybeanamountResponseVo.class);
            Integer jingbean = querybeanamountResponseVo.getData().getJingbean();
            Integer xibean = querybeanamountResponseVo.getData().getXibean();
            // 调用另一个接口
            return querybeanamountResponseVo.getData();
        }
        return null;
    }
}
