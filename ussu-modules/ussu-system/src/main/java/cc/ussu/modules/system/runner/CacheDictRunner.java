package cc.ussu.modules.system.runner;

import cc.ussu.modules.system.service.ISysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动后刷新字典数据到redis
 */
@Component
public class CacheDictRunner implements ApplicationRunner {

    @Autowired
    private ISysDictDataService dictDataService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dictDataService.refreshCache();
    }
}
