package cc.ussu.modules.dczx.entity.vo;

import cn.easyes.annotation.IndexField;
import cn.easyes.common.enums.FieldType;
import lombok.Data;

import java.io.Serializable;

@Data
public class DcQuestionOptionVO implements Serializable {

    private static final long serialVersionUID = -8938888442550221061L;

    private String optionId;

    private String optionContent;

    @IndexField(fieldType = FieldType.BOOLEAN)
    private Boolean istrue;

}
