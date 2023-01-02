package cc.ussu.modules.dczx.model.vo.score;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChildrenVO implements Serializable {

    private static final long serialVersionUID = 8236781746824882103L;

    private String itemScore;
    private int proportion;
    private String examMethodName;
    private int examMethodId;

}
