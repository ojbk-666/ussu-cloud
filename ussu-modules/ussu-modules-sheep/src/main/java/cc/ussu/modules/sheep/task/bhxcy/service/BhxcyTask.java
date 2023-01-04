package cc.ussu.modules.sheep.task.bhxcy.service;

import cc.ussu.common.core.httpclient.MyHttpRequest;
import cc.ussu.common.core.httpclient.MyHttpResponse;
import cc.ussu.modules.sheep.common.RequestErrorException;
import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.common.TaskStopException;
import cc.ussu.modules.sheep.task.bhxcy.constants.BhxcyConstants;
import cc.ussu.modules.sheep.task.bhxcy.constants.RechargeTypeEnum;
import cc.ussu.modules.sheep.task.bhxcy.exception.BhxcyAccountBannedException;
import cc.ussu.modules.sheep.task.bhxcy.exception.BhxcyTokenExpiredException;
import cc.ussu.modules.sheep.task.bhxcy.util.BhxcyUtil;
import cc.ussu.modules.sheep.task.bhxcy.vo.BhxcyBaseParam;
import cc.ussu.modules.sheep.task.bhxcy.vo.BhxcyBaseResponse;
import cc.ussu.modules.sheep.task.bhxcy.vo.GetUserInfoResponse;
import cc.ussu.modules.sheep.task.bhxcy.vo.SignInResponse;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 渤海宣传员小程序签到领京东E卡
 */
@Component
@DisallowConcurrentExecution
public class BhxcyTask extends SheepQuartzJobBean<String> {

    private ThreadLocal<GetUserInfoResponse> userInfoResponseThreadLocal = new ThreadLocal<>();

    @Override
    public String getTaskName() {
        return "渤海宣传员小程序签到领京东E卡";
    }

    @Override
    public List<String> getParamList() {
        return getEnvService().getValueList("bhxcy");
    }

    @Override
    public String getLogRelativePath(String fileName) {
        return "/bhxcy/" + fileName + ".log";
    }

    @Override
    public void doTask(List<String> params) {
        loggerThreadLocal.get().info("获取到 {} 个变量。", CollUtil.size(params));
        for (String param : params) {
            try {
                // 获取用户信息
                BhxcyBaseParam bhxcyBaseParam = BhxcyUtil.formatToParam(param);
                GetUserInfoResponse userInfo = getUserInfo(bhxcyBaseParam);
                loggerThreadLocal.get().info("用户：{}，邀请码为：{}，余额：{}", userInfo.getMobilePhone(), userInfo.getInvCode(), userInfo.getPhoneBill());
                // 签到
                signIn(bhxcyBaseParam);
                Thread.sleep(500);
                // 提现
                withdraw(bhxcyBaseParam);
                checkStop();
                Thread.sleep(3000);
                checkStop();
            } catch (BhxcyAccountBannedException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (BhxcyTokenExpiredException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (RequestErrorException e) {
                loggerThreadLocal.get().info(e.getMessage());
            } catch (TaskStopException e) {
                loggerThreadLocal.get().info(e.getMessage());
                break;
            } catch (Exception e) {
                e.printStackTrace();
                loggerThreadLocal.get().info("未知异常：{}", e.getMessage());
            } finally {
                if (!isInterrupted) {
                    GetUserInfoResponse userInfoResponse = userInfoResponseThreadLocal.get();
                    loggerThreadLocal.get().info("账号 {} 执行完毕", userInfoResponse == null ? param : userInfoResponse.getMobilePhone()).newline();
                }
                userInfoResponseThreadLocal.remove();
            }
        }
    }

    @Override
    protected String getSheepTaskId() {
        return this.getClass().getName();
    }

    /**
     * 获取用户信息
     *
     * @param param
     * @return
     * @throws BhxcyAccountBannedException
     * @throws RequestErrorException
     */
    private synchronized GetUserInfoResponse getUserInfo(BhxcyBaseParam param) throws BhxcyAccountBannedException, RequestErrorException, TaskStopException {
        checkStop();
        Assert.notNull(param, "请求参数为空");
        MyHttpResponse execute = MyHttpRequest.createPost(BhxcyUtil.getRequestUri(BhxcyConstants.URI_GET_USERINFO))
                .connectionKeepAlive()
                // .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .header(Header.REFERER, "https://servicewechat.com/wx4ab510946f1d9a5f/12/page-frame.html")
                .form(BhxcyUtil.getGetUserInfoFormStr(param.getToken(), param.getUid()))
                .execute();
        if (execute.isOk()) {
            GetUserInfoResponse getUserInfoResponse = JSONUtil.toBean(execute.body(), GetUserInfoResponse.class);
            if (BhxcyBaseResponse.BAN.equals(getUserInfoResponse.getResult())) {
                // 账号被封禁
                throw new BhxcyAccountBannedException();
            } else if (0 == getUserInfoResponse.getResult()) {
                // 未登录
                throw new BhxcyTokenExpiredException();
            }
            userInfoResponseThreadLocal.set(getUserInfoResponse);
            return getUserInfoResponse;
        } else {
            throw new RequestErrorException(execute.getStatus());
        }
    }

    /**
     * 签到
     */
    private SignInResponse signIn(BhxcyBaseParam param) throws RequestErrorException, TaskStopException {
        checkStop();
        MyHttpResponse execute = MyHttpRequest.createPost(BhxcyUtil.getRequestUri(BhxcyConstants.URI_SIGN_IN))
                // .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                .connectionKeepAlive()
                .header(Header.REFERER, "https://servicewechat.com/wx4ab510946f1d9a5f/12/page-frame.html")
                .form(BhxcyUtil.getSignInFormStr(param.getToken(), param.getUid()))
                .execute();
        if (execute.isOk()) {
            SignInResponse signInResponse = JSONUtil.toBean(execute.body(), SignInResponse.class);
            if (1 == signInResponse.getResult()) {
                loggerThreadLocal.get().info("账号签到：{} -> 签到成功", userInfoResponseThreadLocal.get().getMobilePhone());
            } else if (2 == signInResponse.getResult()) {
                loggerThreadLocal.get().info("账号签到：{} -> 现金: {} 元", userInfoResponseThreadLocal.get().getMobilePhone(), signInResponse.getMsg());
            } else {
                loggerThreadLocal.get().info("账号签到：{} -> 签到结果：{}", userInfoResponseThreadLocal.get().getMobilePhone(), signInResponse.getMsg());
            }
            return signInResponse;
        } else {
            throw new RequestErrorException(execute.getStatus());
        }
    }

    /**
     * 提现
     */
    private void withdraw(BhxcyBaseParam param) throws TaskStopException {
        checkStop();
        GetUserInfoResponse userInfo = getUserInfo(param);
        String phoneBill = userInfo.getPhoneBill();
        if (StrUtil.isNotBlank(phoneBill)
                && NumberUtil.isGreaterOrEqual(new BigDecimal(phoneBill), new BigDecimal(10))) {
            // 达到体现门槛
            loggerThreadLocal.get().debug("达到体现门槛（{}元）：", phoneBill);
            checkStop();
            if ("true".equals(getEnvService().getValue(BhxcyConstants.PARAM_KEY_AUTO_RECHARGE))) {
                // 判断账号是否需要跳过
                List<String> excludes = getEnvService().getValueList(BhxcyConstants.PARAM_KEY_AUTO_RECHARGE_EXCLUDE);
                if (excludes.contains(userInfo.getMobilePhone())) {
                    loggerThreadLocal.get().info("已配置提现跳过该账号");
                    return;
                }
                boolean withdraw50 = NumberUtil.isGreaterOrEqual(new BigDecimal(phoneBill), new BigDecimal(50));
                boolean withdraw20 = NumberUtil.isGreaterOrEqual(new BigDecimal(phoneBill), new BigDecimal(20));
                boolean withdraw10 = NumberUtil.isGreaterOrEqual(new BigDecimal(phoneBill), new BigDecimal(10));
                if (withdraw50 || withdraw20 || withdraw10) {
                    RechargeTypeEnum rechargeTypeEnum = withdraw50 ? RechargeTypeEnum.E_CARD_50 : (withdraw20 ? RechargeTypeEnum.E_CARD_20 : RechargeTypeEnum.E_CARD_10);
                    checkStop();
                    MyHttpResponse rechargeResponse = MyHttpRequest.createPost(BhxcyUtil.getRequestUri(BhxcyConstants.URI_GET_RECHARGE))
                            // .header(Header.CONTENT_TYPE, ContentType.FORM_URLENCODED.getValue())
                            .connectionKeepAlive()
                            .form(BhxcyUtil.getRechargeFormStr(param.getToken(), param.getUid(), rechargeTypeEnum.getCategory(), rechargeTypeEnum.getJine()))
                            .execute();
                    if (rechargeResponse.isOk()) {
                        BhxcyBaseResponse bhxcyBaseResponse = JSONUtil.toBean(rechargeResponse.body(), BhxcyBaseResponse.class);
                        if (BhxcyBaseResponse.BAN.equals(bhxcyBaseResponse.getResult())) {
                            throw new BhxcyAccountBannedException();
                        } else if (0 == bhxcyBaseResponse.getResult()) {
                            loggerThreadLocal.get().info(bhxcyBaseResponse.getMsg());
                        } else if (1 == bhxcyBaseResponse.getResult()) {
                            loggerThreadLocal.get().info("兑换请求提交成功");
                        }
                    } else {
                        throw new RequestErrorException(rechargeResponse);
                    }
                }
            } else {
                // 没有开启自动提现
                loggerThreadLocal.get().debug("自动提现开关未开启，请确认环境变量 {} 值为true", BhxcyConstants.PARAM_KEY_AUTO_RECHARGE);
            }
        } else {
            loggerThreadLocal.get().debug("没有达到体现门槛");
        }
    }

}
