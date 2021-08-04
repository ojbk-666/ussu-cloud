package cn.ussu.modules.ecps.portal.service;

import cn.hutool.core.bean.BeanUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.portal.feign.member.RemoteCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PortalCartSkuService {

    @Autowired
    private RemoteCartService remoteCartService;

    /**
     * 获取用户购物车列表
     */
    public List<EbCartSku> getCartListByUserId() {
        JsonResult jr = remoteCartService.getCartListByUserId();
        if (jr.isSuccess()) {
            List<Map> data = jr.getData();
            return BeanUtil.copyToList(data, EbCartSku.class);
        } else {
            return null;
        }
    }

}
