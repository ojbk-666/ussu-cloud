package cc.ussu.modules.dczx.model.vo.practice;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 随堂联系列表
 *
 * https://classroom.edufe.com.cn/Practice?batchValue=202003&gradationValue=a23&subjectValue=37&exampapertypeValue=1&courseValue=B0227A_1&courseId=B0227A
 */
@Data
public class InitTreeListmcapiResponse implements Serializable {

    private static final long serialVersionUID = 8755421595632241298L;

    private String courseName;

    private List<Treek> treekList;

}
