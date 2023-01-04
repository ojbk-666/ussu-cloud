package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

@Data
public class ClockInForFarmResponse extends JdBaseResponse {

    private Integer signDay;
    private Integer amount;

}
