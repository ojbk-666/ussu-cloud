package cc.ussu.modules.sheep.task.jd.vo.speedsignfree;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class JdSpeedSignfreeTaskLocalThreadDTO {

    private JdCookieVO jdCookieVO;

    private List<SignFreeHomeResponse.Data1.SignFreeOrderInfo> signFreeOrderInfoList;

}
