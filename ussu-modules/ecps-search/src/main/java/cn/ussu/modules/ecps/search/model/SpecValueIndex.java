package cn.ussu.modules.ecps.search.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
@Document(indexName = "ecps-sku-spec", type = "specValue")
public class SpecValueIndex {

    //规格值主键
    @Id
    private Integer specId;

    @Field(type = FieldType.Integer)
    private Integer skuId;

    @Field(type = FieldType.Integer)
    private Integer featureId;

    //规格值
    private String specValue;

}
