package cc.ussu.modules.sheep.task.jd.vo.fruit;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForFarmResponse;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JdFruitHelpThreadLocalDTO {

    private JdCookieVO jdCookieVO;

    private InitForFarmResponse farmInfo;

}
