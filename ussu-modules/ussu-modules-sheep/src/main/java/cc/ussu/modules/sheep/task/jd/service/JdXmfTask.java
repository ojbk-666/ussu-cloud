package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.*;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.exception.JdCookieExpiredException;
import cc.ussu.modules.sheep.task.jd.vo.JdBodyParam;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.xmf.JdXmfThreadLocalDTO;
import cc.ussu.modules.sheep.task.jd.vo.xmf.response.DoInteractiveAssignmentResponse;
import cc.ussu.modules.sheep.task.jd.vo.xmf.response.GetInteractionHomeInfoResponse;
import cc.ussu.modules.sheep.task.jd.vo.xmf.response.QueryInteractiveInfoResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 京东小魔方
 */
@Component
@DisallowConcurrentExecution
public class JdXmfTask extends SheepQuartzJobBean<String> {

    public static final ThreadLocal<JdXmfThreadLocalDTO> threadLocal = new ThreadLocal<>();

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "京东小魔方";
    }

    /**
     * 任务分组
     */
    @Override
    public String getTaskGroupName() {
        return JdConstants.TASK_GROUP_NAME_JD;
    }

    /**
     * 获取参数
     */
    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList(JdConstants.JD_COOKIE);
    }

    /**
     * 获取日志存放的相对路径
     *
     * @param fileName 文件名
     * @return 相对路径地址 例 /a/b/c/2022-10-16.log
     */
    @Override
    public String getLogRelativePath(String fileName) {
        return "/jd/xmf/" + fileName + ".log";
    }

    /**
     * 获取任务数据库id
     */
    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    /**
     * 执行任务
     *
     * @param params
     */
    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量", CollUtil.size(params));
        for (String ckval : params) {
            JdCookieVO jdCookieVO = new JdCookieVO(ckval);
            threadLocal.set(new JdXmfThreadLocalDTO().setJdCookieVO(jdCookieVO).setUserAgent(getUserAgent()));
            loggerThreadLocal.get().info("账号 {} 开始执行", jdCookieVO.getPt_pin());
            try {
                doMain();
                checkStop();
            } catch (JdCookieExpiredException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (ResponseResultException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (Exception e) {
                loggerThreadLocal.get().info("未知错误：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    loggerThreadLocal.get().info("账号 {} 运行完毕", jdCookieVO.getPt_pin()).newline();
                }
                threadLocal.remove();
            }
        }
    }

    private void doMain() throws InterruptedException {
        GetInteractionHomeInfoResponse.GetInteractionHomeInfoResult getInteractionHomeInfoResult = getInteractionHomeInfo();
        Thread.sleep(1000);
        checkStop();
        queryInteractiveInfo();
        threadLocal.get().setFengxian(false);
        List<QueryInteractiveInfoResponse.Assignment> taskList = threadLocal.get().getTaskList();
        if (CollUtil.isNotEmpty(taskList)) {
            for (QueryInteractiveInfoResponse.Assignment vo : taskList) {
                QueryInteractiveInfoResponse.Ext ext = vo.getExt();
                if (ext != null) {
                    String extraType = ext.getExtraType();
                    if (!"brandMemberList".equals(extraType) && !"assistTaskDetail".equals(extraType)) {
                        if (vo.getCompletionCnt() < vo.getAssignmentTimesLimit()) {
                            loggerThreadLocal.get().info("去完成任务 {}", vo.getAssignmentName());
                            String encryptAssignmentId = vo.getEncryptAssignmentId();
                            if ("sign1".equals(extraType)) {
                                doInteractiveAssignment(encryptAssignmentId, vo.getExt().getSign1().getItemId(), null);
                            }
                            List<QueryInteractiveInfoResponse.ProductsInfo> productsInfo = ext.getProductsInfo();
                            if (CollUtil.isNotEmpty(productsInfo)) {
                                for (QueryInteractiveInfoResponse.ProductsInfo vi : productsInfo) {
                                    if (1 == vi.getStatus()) {
                                        doInteractiveAssignment(encryptAssignmentId, vi.getItemId(), null);
                                    }
                                }
                            }
                            List<QueryInteractiveInfoResponse.ShoppingActivity> shoppingActivity = ext.getShoppingActivity();
                            if (CollUtil.isNotEmpty(shoppingActivity)) {
                                for (QueryInteractiveInfoResponse.ShoppingActivity vi : shoppingActivity) {
                                    if (1 == vi.getStatus()) {
                                        doInteractiveAssignment(encryptAssignmentId, vi.getAdvId(), "1");
                                        Thread.sleep(6000);
                                        doInteractiveAssignment(encryptAssignmentId, vi.getAdvId(), "0");
                                    }
                                }
                            }
                            List<QueryInteractiveInfoResponse.BrowseShop> browseShop = ext.getBrowseShop();
                            if (CollUtil.isNotEmpty(browseShop)) {
                                for (QueryInteractiveInfoResponse.BrowseShop vi : browseShop) {
                                    if (1 == vi.getStatus()) {
                                        doInteractiveAssignment(encryptAssignmentId, vi.getItemId(), "1");
                                        Thread.sleep(6000);
                                        doInteractiveAssignment(encryptAssignmentId, vi.getItemId(), "0");
                                    }
                                }
                            }
                            List<QueryInteractiveInfoResponse.AddCart> addCart = ext.getAddCart();
                            if (CollUtil.isNotEmpty(addCart)) {
                                for (QueryInteractiveInfoResponse.AddCart vi : addCart) {
                                    if (1 == vi.getStatus()) {
                                        doInteractiveAssignment(encryptAssignmentId, vi.getItemId(), "1");
                                        Thread.sleep(6000);
                                        doInteractiveAssignment(encryptAssignmentId, vi.getItemId(), "0");
                                    }
                                }
                            }
                            if (BooleanUtil.isTrue(threadLocal.get().getFengxian())) {
                                return;
                            }
                        } else {
                            loggerThreadLocal.get().info("任务 {} 已完成", vo.getAssignmentName());
                        }
                    }
                }
            }
        } else {
            loggerThreadLocal.get().info("没有获取到活动信息");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JdXmfTask task = new JdXmfTask();
        JdCookieVO jdCookieVO = new JdCookieVO("pt_pin=jd_sAcAzzgtBSBX;pt_key=app_openAAJjZI5DADCy0FxaDoEisUESi9IRe_OCHR_qDH13oiGORfkvoE8L3KFEH5BjCs2kII-bF9YjN90;");
        task.threadLocal.set(new JdXmfThreadLocalDTO().setJdCookieVO(jdCookieVO).setUserAgent(task.getUserAgent()));
        task.loggerThreadLocal.set(new SheepLogger());
        task.doMain();
        // task.getInteractionHomeInfo();
    }

    /**
     * 完成任务
     *
     * @param encryptAssignmentId
     * @param itemId
     * @param actionType
     */
    private void doInteractiveAssignment(String encryptAssignmentId, String itemId, String actionType) throws TaskStopException {
        if (actionType == null) {
            actionType = "";
        }
        String signStr = "1667567640059~1TaIVhsZucnMDFSRWlnQTAxMQ==.Y3NfUHRkcl9Ud2R3XxkTOy1aVDFrAxw3P2NpX0tyfncXVT9jOxoUcDE/OS8iBCkHNCsLMg4MGRsLI1M2b3gXGQ==.c8b7a911~3,2~16FA8760BFCEAAC6986C447FB3BD50D6729C925F64D992DE17EAF3DF3FC0908D~0lb7kii~C~SxVHXBcKbG4dF0RZXhMPaxVXBBgGeB91fRwEdn0ZURtHExkSUwQcA38bd3kZA39xHVQcQxFsGRJQQV8XCgYfE0ZDFQkTBAYBBAQDBwEABAMEDgIIBgUVHxNCVVMRCxdEQ0dFQVZCVRMZEkBWUBcKFVVXQURDR0RUEhsRQVFeFQlqAhwFBAAZCBsLHQQcWUZdWW0bEVtfEg0CHRdTRBELFwUABQMFAVQAVQwEB1cCAwgPAAIDCFEFCAYBAVYGBwIPER0XXkcRCxdZZ1teW1UVHxNBEg0CBwMHAgUGAwQHAwQMHBVZWhcKFQUABAZRBAVRCQ4EBgQAVQUEVAgFVlRRCVNVVAxTAFEBUVIEUQkGCQcRHRdWR1ETDxJEQABWSmd7UmNcWWBbbEdQWGl8fn0HRggNFR8TW0YVCRN0QEdfVBVzWF5BQERSQR0VeVlQHxccFV1QQxINEQADCAQHABccFUBSRxINaAcDBxsABgBtGxFDWhINaBNcYF9cX1AIGwITGRJefGIXHBUCAxsBFR8TBAEZAB8FEhsRAAMIBAcAFxwVBQAEBlEEBVEJDgQGBABVBQRUCAVWVFEJU1VUDFMAUQFRUgRRCQYJBxEdF1EVbh0XWVhSEw8SUVVXU1ZRR0UXHBVSWxcKFUYTGRJUWhMPEkAAHwceAxEdF1NRbEcXChUDABccFVFVFwoVQVBbVFheDAYCDgMHBgkCER0XXV0RC24BGwMdBW0bEVNZX1ARCxcBAQUGAAYABQIABQ8FTwRzR0oEAAV5W1hBfXN5aWZ1BgNgT3FOfXAIDRlpXwAHZgBrXVYEX3NRdw4cV3Rfe3xLDWBlYlIaVUB+c2VjcVh/BFhHVgJeSmZZAFh+XWJEUVpcXXBhCURqWXcfZgBgYndQclp/SEcLdU19Q3JYVnp6YFZZfVp6A3BJD0B2YWlEe19GcXpEWlFgZWIId3J+BXpNaVl/YmBycGdwBVVmB1NWdHELf3MJQHt1BUNwa3hZeU9AY3NkBgV/d3oGfmMBcnlrBER+XX4MG1MHUlIABVZWT0wcBk1PS3JJYFlzZGBbeH92BnJ2fWFuZnldemV2Rl5nYHF0VnVhAnNwYVhhdF0Ib2FJVnpxVHx9c3ZiZX1IbWBnYglxdlZGYnZydGZ4T1dzc0dcZHd3BGRkAXhic1t8dnh2fnB0YXpgeU16ZHVGXmhyZloITgZAAwBdDlkTGRJaQFYXChURTA==~0omwocl";
        JdBodyParam jdBodyParam = new JdBodyParam().encryptProjectId(threadLocal.get().getProjectId()).encryptAssignmentId(encryptAssignmentId)
                .sourceCode("acexinpin0823").itemId(itemId).activeType(actionType).completionFlag("")
                .put("ext", new HashMap<>())
                .put("extParam", new JdBodyParam().put("businessData",
                        new JdBodyParam().random("32906395").getMap()).sceneid("XMFhPageh5").signStr(signStr).getMap());
        DoInteractiveAssignmentResponse rep = requestPost(DoInteractiveAssignmentResponse.class, "doInteractiveAssignment", jdBodyParam);
        String msg = rep.getMsg();
        loggerThreadLocal.get().info(msg);
        if (msg.contains("风险等级未通过")) {
            threadLocal.get().setFengxian(true);
        }
    }

    /**
     * 获取任务列表
     */
    private void queryInteractiveInfo() throws TaskStopException {
        QueryInteractiveInfoResponse rep = requestPost(QueryInteractiveInfoResponse.class, "queryInteractiveInfo",
                new JdBodyParam().encryptProjectId(threadLocal.get().getProjectId()).sourceCode("acexinpin0823").put("ext", new HashMap<>()));
        if (rep.isSuccess()) {
            threadLocal.get().setTaskList(rep.getAssignmentList());
        }
    }

    /**
     * 获取基础信息
     */
    private GetInteractionHomeInfoResponse.GetInteractionHomeInfoResult getInteractionHomeInfo() throws TaskStopException {
        GetInteractionHomeInfoResponse rep = requestGet(GetInteractionHomeInfoResponse.class, "getInteractionHomeInfo", new JdBodyParam().sign("u6vtLQ7ztxgykLEr").source(2));
        if (rep.isSuccess()) {
            GetInteractionHomeInfoResponse.GetInteractionHomeInfoResult result = rep.getResult();
            if (result != null) {
                threadLocal.get().setProjectId(result.getTaskConfig().getProjectId());
                return result;
            } else {
                loggerThreadLocal.get().info("获取projectId失败");
                throw new ResponseResultException("getInteractionHomeInfo.result -> error");
            }
        } else {
            throw new ResponseResultException("getInteractionHomeInfo -> error");
        }
    }

    private MyHttpRequest.HttpRequestBuilder taskPostUrl(Method method, String functionId, String body) {
        String url = "https://api.m.jd.com/?functionId=" + functionId + "&body=" + URLEncodeUtil.encodeAll(body) + "&appid=content_ecology&client=wh5&clientVersion=1.0.0";
        return MyHttpRequest.create(method, url)
                .header(Header.HOST, "api.m.jd.com")
                .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString())
                .header(Header.ORIGIN, "https://h5.m.jd.com")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.ACCEPT, "application/json, text/plain, */*")
                .header(Header.REFERER, "https://h5.m.jd.com/babelDiy/Zeus/2bf3XEEyWG11pQzPGkKpKX2GxJz2/index.html")
                .header(Header.USER_AGENT, threadLocal.get().getUserAgent())
                .connectionKeepAlive()
                .disableCookie()
                .cookie(threadLocal.get().getJdCookieVO().toString());
    }

    private <T> T requestGet(Class<T> t, String functionId, JdBodyParam jdBodyParam) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskPostUrl(Method.GET, functionId, jdBodyParam.getBody()).execute();
        if (execute.isOk()) {
            String body = execute.body();
            System.out.println("------------");
            System.out.println(body);
            System.out.println("------------");
            if (false) {
                throw new JdCookieExpiredException();
            }
            return JSONUtil.toBean(body, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private <T> T requestPost(Class<T> t, String functionId, JdBodyParam jdBodyParam) throws TaskStopException {
        checkStop();
        MyHttpResponse execute = taskPostUrl(Method.POST, functionId, jdBodyParam.getBody()).execute();
        if (execute.isOk()) {
            String body = execute.body();
            System.out.println("------------");
            System.out.println(body);
            System.out.println("------------");
            if (false) {
                throw new JdCookieExpiredException();
            }
            return JSONUtil.toBean(body, t);
        } else {
            throw new RequestErrorException(execute);
        }
    }

    private String getUserAgent() {
        return "jdapp;iPhone;10.0.8;14.6;" + UUID.fastUUID().toString(true) + ";network/wifi;JDEbook/openapp.jdreader;model/iPhone9,2;addressid/2214222493;appBuild/168841;jdSupportDarkMode/0;Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16E158;supportJDSHWK/1";
    }

}
