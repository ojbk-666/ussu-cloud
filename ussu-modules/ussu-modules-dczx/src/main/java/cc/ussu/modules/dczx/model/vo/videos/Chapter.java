package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Data;

import java.util.List;

@Data
public class Chapter {

    /**
     * 章节题，单元一 单元二
     */
    private String title;

    /**
     * 单元下的任务
     */
    private List<Division> divisions;

}
