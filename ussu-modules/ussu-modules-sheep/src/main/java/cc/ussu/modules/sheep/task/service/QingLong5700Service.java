package cc.ussu.modules.sheep.task.service;

import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.modules.sheep.task.jd.constants.JdConstants;
import cc.ussu.support.qinglong.dto.EnvListDTO;
import cc.ussu.support.qinglong.model.QingLongConfig;
import cc.ussu.support.qinglong.service.impl.AbstractQingLongService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class QingLong5700Service extends AbstractQingLongService {

    /**
     * 获取配置
     */
    @PostConstruct
    @Override
    public QingLongConfig getQingLongConfig() {
        return QingLongConfig.builder()
                .url(ConfigUtil.getValue("sheep","qinglong", "url"))
                .clientId(ConfigUtil.getValue("sheep","qinglong", "client-id"))
                .clientSecret(ConfigUtil.getValue("sheep","qinglong", "client-secret"))
                .build();
    }

    /**
     * 获取缓存key
     */
    @Override
    public String getCacheKey() {
        return "qinglong:5700";
    }

    /**
     * 获取cookie
     *
     * @param pin
     */
    public String getJdCkByPin(String pin) {
        if (StrUtil.isNotBlank(pin)) {
            EnvListDTO envList = getEnvList(pin);
            List<EnvListDTO.EnvDTO> envVoList = envList.getData();
            for (EnvListDTO.EnvDTO envVo : envVoList) {
                if (envVo.getName().equals(JdConstants.JD_COOKIE)/* && envVo.getStatus() == EnvListDTO.STATUS_NORMAL*/) {
                    return envVo.getValue();
                }
            }
        }
        return null;
    }

}
