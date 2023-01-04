package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

@Data
public class GetFullCollectionRewardResponse extends JdBaseResponse {

    private String title;
    private Boolean hasLimit;

}
