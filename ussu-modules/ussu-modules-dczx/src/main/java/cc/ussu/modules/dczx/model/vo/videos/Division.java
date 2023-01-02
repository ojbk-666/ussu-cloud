package cc.ussu.modules.dczx.model.vo.videos;

import lombok.Data;

import java.util.List;

/**
 * 任务
 */
@Data
public class Division {

    private String title;

    private List<DivisionCourse> courseList;

    private boolean done;

    private boolean half;

    private boolean todo;

}
