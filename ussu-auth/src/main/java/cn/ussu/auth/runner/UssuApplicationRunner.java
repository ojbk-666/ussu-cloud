package cn.ussu.auth.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UssuApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initAlipayFactory();
    }

    /**
     * 初始化alipay
     */
    private void initAlipayFactory() throws Exception {
        // 1. 设置参数（全局只需设置一次）
        // Factory.setOptions(getConfig());
        // AlipaySystemOauthTokenResponse token = Factory.Base.OAuth().getToken("");

    }

}
