package cc.ussu.modules.ecps.skill.feign;

import cc.ussu.modules.ecps.item.entity.EbSku;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class RemoteSkuServiceFallback implements RemoteSkuService {

    @Override
    public List<EbSku> getSimpleDetailBySkuIdList(String skuIds) {
        log.warning("远程调用失败");
        return new ArrayList<>();
    }
}
