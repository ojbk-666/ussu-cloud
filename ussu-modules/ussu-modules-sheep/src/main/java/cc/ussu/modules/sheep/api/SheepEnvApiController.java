package cc.ussu.modules.sheep.api;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.sheep.entity.SheepEnv;
import cc.ussu.modules.sheep.service.ISheepEnvService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sheep/env")
public class SheepEnvApiController {

    @Autowired
    private ISheepEnvService sheepEnvService;

    /**
     * 查找指定环境变量
     */
    @GetMapping("/{secret}/{key}")
    public JsonResult queryByKey(@PathVariable String key, @PathVariable String secret) {
        if (StrUtil.isBlank(key)) {
            return JsonResult.error();
        }
        if (!"825f8169b8c99e7c5dc77460612a790c".equals(secret.toLowerCase())) {
            return JsonResult.error("xxx");
        }
        List<SheepEnv> list = sheepEnvService.list(Wrappers.lambdaQuery(SheepEnv.class).eq(SheepEnv::getDisabled, StrConstants.CHAR_FALSE).eq(SheepEnv::getName, key));
        return JsonResult.ok(list);
    }

}
