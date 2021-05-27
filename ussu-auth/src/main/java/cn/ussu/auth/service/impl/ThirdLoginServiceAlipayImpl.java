package cn.ussu.auth.service.impl;

import cn.ussu.auth.model.param.login.third.AlipayThirdLoginParam;
import cn.ussu.auth.service.ThirdLoginService;
import org.springframework.stereotype.Service;

/**
 * 支付宝登录
 *
 * @author liming
 * @date 2021-05-27 23:11
 */
@Service
public class ThirdLoginServiceAlipayImpl implements ThirdLoginService<AlipayThirdLoginParam> {

    @Override
    public String login(AlipayThirdLoginParam alipayThirdLoginParam) {
        return null;
    }
}
