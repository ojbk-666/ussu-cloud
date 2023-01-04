package cc.ussu.modules.sheep.task.jd.vo.xmf;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.xmf.response.QueryInteractiveInfoResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class JdXmfThreadLocalDTO {

    private JdCookieVO jdCookieVO;

    private String projectId;

    private List<QueryInteractiveInfoResponse.Assignment> taskList;

    private String userAgent;

    private Boolean fengxian;

}