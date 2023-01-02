package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Data;

import java.util.List;

@Data
public class GetVideoJsonVO {

    private String image;

    private String title;

    private List<Source> sources;

    @Data
    public static class Source {
        private String file;
        private String vid;
    }

}
