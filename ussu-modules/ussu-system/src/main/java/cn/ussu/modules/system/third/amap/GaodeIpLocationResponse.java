package cn.ussu.modules.system.third.amap;

import lombok.Data;

/**
 * 高德Ip位置服务响应实体
 */
@Data
public class GaodeIpLocationResponse {

    /**
     * 返回结果状态值 值为0或1,0表示失败；1表示成功
     */
    String status;
    /**
     * 返回状态说明，status为0时，info返回错误原因，否则返回“OK”。
     */
    String info;
    /**
     * 返回状态说明,10000代表正确,详情参阅info状态表
     */
    String infocode;
    /**
     * 省份名称 若为直辖市则显示直辖市名称；如果在局域网 IP网段内，则返回“局域网”；非法IP以及国外IP则返回空
     */
    String province;
    /**
     * 城市名称 若为直辖市则显示直辖市名称；如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    String city;
    /**
     * 城市的adcode编码
     */
    String adcode;
    /**
     * 所在城市矩形区域范围 所在城市范围的左下右上对标对
     */
    String rectangle;

    public boolean success() {
        return "1".equals(status) && "OK".equals(info) && "10000".equals(infocode);
    }

}
