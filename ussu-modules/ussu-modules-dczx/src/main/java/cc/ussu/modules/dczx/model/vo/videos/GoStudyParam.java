package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoStudyParam {

    private Integer chapterId;
    private Integer subChapterId;
    private Integer serviceId;
    private Integer serviceType;
    private String studyProgress;

    private String versionCode;

}
