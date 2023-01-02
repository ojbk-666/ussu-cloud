package cc.ussu.modules.dczx.model.vo.practice;

import lombok.Data;

import java.io.Serializable;

@Data
public class Treek implements Serializable {

    private static final long serialVersionUID = 4974743233909039442L;

    private String CHAPTER_ID;

    private String SUPER_CHAPTER_ID;

    private String WORKER_ID;
    private String WORKER_NAME;
    private Integer WORKER_SCORE;
    private String buttonFlag;
    private String topicCount;

}
