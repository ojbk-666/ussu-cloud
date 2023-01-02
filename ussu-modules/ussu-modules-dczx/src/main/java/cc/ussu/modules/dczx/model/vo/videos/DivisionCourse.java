package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DivisionCourse {

    private GoStudyParam goStudyParam;

    /**
     * 单元名称
     */
    private String chapterTitle;

    private String divisionTitle;

    private String title;

    /**
     * 是否是查看文档任务
     */
    private boolean isText;

    /**
     * 是否是视频任务
     */
    private boolean isVideo;
    private boolean isHomework;

    /**
     * 文档任务是否完成
     */
    private boolean textDone;

    /**
     * 视频任务是否完成
     */
    private boolean videoDone;
    private boolean homeworkDone;

}
