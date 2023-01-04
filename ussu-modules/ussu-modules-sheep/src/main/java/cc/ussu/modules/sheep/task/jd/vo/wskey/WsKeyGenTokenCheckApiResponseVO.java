package cc.ussu.modules.sheep.task.jd.vo.wskey;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class WsKeyGenTokenCheckApiResponseVO {

    private String ua;
    private JSONObject param;

}
