package cn.ussu.modules.system.third.amap;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 高德普通ip地址解析服务
 *
 * @author liming
 * @date 2020-05-08 10:25
 */
@Service
public class GaodeIpLocationService {

    @Value(("${amap.iplocation.key}"))
    private String key;
    private String URL_HTTPS = "https://restapi.amap.com/v3/ip?ip={}&output=JSON&key=";

    /**
     * 获取位置
     */
    public GaodeIpLocationResponse getLocation(String ipAddr) {
        String realUrl = StrUtil.format(URL_HTTPS + key, ipAddr);
        String repStr = HttpUtil.get(realUrl, 3 * 1000);
        GaodeIpLocationResponse gaodeIpLocationResponse = JSON.parseObject(repStr, GaodeIpLocationResponse.class);
        if (gaodeIpLocationResponse.success()) {
            // 成功调用
            return gaodeIpLocationResponse;
        }
        return new GaodeIpLocationResponse();
    }

}
