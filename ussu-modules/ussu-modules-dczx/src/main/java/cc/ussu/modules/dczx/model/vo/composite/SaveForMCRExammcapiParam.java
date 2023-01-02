package cc.ussu.modules.dczx.model.vo.composite;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存单元作业进度的参数
 */
@Data
@Accessors(chain = true)
public class SaveForMCRExammcapiParam implements Serializable {

    private static final long serialVersionUID = 7152729453623398896L;

    private String paperId;
    private String paperType;
    private String typeId = "tk0000002";
    private String questionId;
    private String trunkId;
    /**
     * 答案 多个用,拼接
     */
    private String answer;
    private String type;
    private String checked = "on";

    public List<SaveForMCRExammcapiParam> getParam() {
        List<SaveForMCRExammcapiParam> list = new ArrayList<>();
        list.add(this);
        return list;
    }

}
