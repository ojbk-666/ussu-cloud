package cc.ussu.modules.system.runner;

import cc.ussu.modules.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheConfigRunner implements ApplicationRunner {

    @Autowired
    private ISysConfigService sysConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysConfigService.refreshCache();
    }
}
