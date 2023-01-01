package cc.ussu.modules.ecps.search.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
// @Document(indexName = "ecps-sku-img", type = "skuImg")
@Document(indexName = "ecps-sku-img")
public class SkuImgIndex {

    @Id
    private Integer imgId;

    @Field(type = FieldType.Integer)
    private Integer skuId;

    private String imgUrl;

    private Integer defaultImg;

}
