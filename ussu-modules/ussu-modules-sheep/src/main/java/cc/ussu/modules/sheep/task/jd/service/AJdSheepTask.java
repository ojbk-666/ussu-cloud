package cc.ussu.modules.sheep.task.jd.service;

import cc.ussu.modules.sheep.common.SheepQuartzJobBean;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 京东相关任务
 */
public abstract class AJdSheepTask extends SheepQuartzJobBean<JdCookieVO> {

    protected String getUserAgent() {
        return "jdapp;iPhone;11.2.8;;;M/5.0;appBuild/168328;jdSupportDarkMode/0;ef/1;Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148;supportJDSHWK/0;";
    }

    /**
     * 获取ck列表
     */
    @Override
    public List<JdCookieVO> getParamList() {
        List<String> list = getEnvService().getValueList(JdConstants.JD_COOKIE);
        return list.stream().filter(StrUtil::isNotBlank).map(JdCookieVO::new).collect(Collectors.toList());
    }
}
