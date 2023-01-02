package cc.ussu.modules.dczx.model.vo.score;

import lombok.Data;

import java.io.Serializable;

@Data
public class MethodVO implements Serializable {

    private static final long serialVersionUID = -9054458228317825412L;

    /**
     * 课程id
     */
    private String courseId;
    private int disOrder;
    private String disScore;
    private int examMethodId;
    private String examMethodName;
    /**
     * 分项成绩，即 按比例计算后的分数
     */
    private String itemScore;
    /**
     * 比例
     */
    private String proportion;
    /**
     * 分数
     */
    private String score;

}
