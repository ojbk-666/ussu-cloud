package cc.ussu.modules.ecps.search.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
// @Document(indexName = "ecps-item-para", type = "paraValue")
@Document(indexName = "ecps-item-para")
public class ParaValueIndex {

    //参数值主键
    @Id
    private Integer paraId;

    @Field(type = FieldType.Integer)
    private Integer itemId;

    @Field(type = FieldType.Integer)
    private Integer featureId;

    // 参数值
    private String paraValue;

}
