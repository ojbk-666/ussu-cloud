package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LmsStudyVO {

    private String versionCode;

    private String homeworkFlag;

    private List<Chapter> chapterList;

}
