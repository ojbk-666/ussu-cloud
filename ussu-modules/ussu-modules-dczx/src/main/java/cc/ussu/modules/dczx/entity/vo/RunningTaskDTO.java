package cc.ussu.modules.dczx.entity.vo;

import cc.ussu.modules.dczx.entity.DcTaskVideo;
import cc.ussu.modules.dczx.model.vo.videos.DivisionCourse;
import cc.ussu.modules.dczx.model.vo.videos.GoStudyParam;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RunningTaskDTO implements Serializable {

    private static final long serialVersionUID = 1860748831647605972L;
    /**
     * ck
     */
    private String cookieStr;

    /**
     * 跳转的参数
     */
    private GoStudyParam goStudyParam;

    private DivisionCourse divisionCourse;

    /**
     * 视频信息
     */
    private DcTaskVideo dcTaskVideo;

    /**
     * 单元一 单元二
     */
    private String chapterTitle;

    /**
     * 任务id
     */
    private String taskId;

}
