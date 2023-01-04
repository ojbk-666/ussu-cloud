package cc.ussu.modules.sheep.task.jd.vo.factory;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.factory.response.GetHomeDataResponse;
import cc.ussu.modules.sheep.task.jd.vo.factory.response.GetTaskDetailResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class JdFactoryThreadLocalDTO implements Serializable {

    private static final long serialVersionUID = 8372471535807692454L;

    private JdCookieVO jdCookieVO;

    /**
     * 工厂信息
     */
    private GetHomeDataResponse.GetHomeDataResult getHomeDataResult;

    /**
     * 任务列表
     */
    private List<GetTaskDetailResponse.TaskVo> taskVos;

    private String myInviteCode;

    /**
     * 互助码列表
     */
    private List<String> inviteCodeList = new ArrayList<>();

}
