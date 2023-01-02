package cc.ussu.modules.dczx.model.vo.homework;

import cc.ussu.modules.dczx.model.vo.PaperQuestion;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 进入单元作业获取题目的相应
 */
@Data
public class ShowExamPapermcapiResponse implements Serializable {

    private static final long serialVersionUID = 7523730538322983854L;

    private String PAPERID;

    private List<PaperQuestion> PAPER_QUESTIONS;

    private String everytopicscore;

    private boolean finish;

    /**
     * 已经耗费时间
     */
    private String expendTime;

    private String questiontime;

    private String userexampaper_id;

}
