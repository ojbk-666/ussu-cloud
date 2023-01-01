package cc.ussu.modules.ecps.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
// @Document(indexName = "ecps-brand", type = "brand")
@Document(indexName = "ecps-brand")
public class BrandIndex {

    @Id
    private Integer brandId;

    private String brandName;

    private String brandDesc;

    private String imgs;

    @Field(type = FieldType.Integer)
    private Integer brandSort;

}
