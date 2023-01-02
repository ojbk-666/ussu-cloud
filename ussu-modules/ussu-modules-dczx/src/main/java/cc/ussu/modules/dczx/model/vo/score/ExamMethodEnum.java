package cc.ussu.modules.dczx.model.vo.score;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamMethodEnum {

    JK(11, "机（网）考"),
    DYZY(3, "单元作业"),
    XXGCKH(767, "学习过程考核")
    ;

    private Integer examMethodId;
    private String examMethodName;

    public static ExamMethodEnum getNameById(Integer id) {
        for (ExamMethodEnum item : values()) {
            if (item.getExamMethodId().equals(id)) {
                return item;
            }
        }
        return null;
    }

}
