package cc.ussu.modules.sheep.task.jd.vo.joypark;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.joypark.response.GetJoyListResponse;
import cc.ussu.modules.sheep.task.jd.vo.joypark.response.JoyBaseInfoResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JdJoyParkTaskThreadLocalDTO {

    private JdCookieVO jdCookieVO;

    private JoyBaseInfoResponse.JoyBaseInfo joyBaseInfo;

    private GetJoyListResponse.GetJoyListData joyListData;

}
