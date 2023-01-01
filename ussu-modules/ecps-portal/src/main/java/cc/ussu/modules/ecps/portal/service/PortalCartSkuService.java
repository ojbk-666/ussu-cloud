package cc.ussu.modules.ecps.portal.service;

import cc.ussu.modules.ecps.member.entity.EbCartSku;
import cc.ussu.modules.ecps.portal.feign.member.RemoteCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortalCartSkuService {

    @Autowired
    private RemoteCartService remoteCartService;

    /**
     * 获取用户购物车列表
     */
    public List<EbCartSku> getCartListByUserId() {
        return remoteCartService.getCartListByUserId();
    }

}
