package cc.ussu.modules.ecps.search.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
// @Document(indexName = "ecps-sku", type = "sku")
@Document(indexName = "ecps-sku")
public class SkuIndex {

    @Id
    private Integer skuId;

    @Field(type = FieldType.Integer)
    private Integer itemId;

    private String sku;

    private BigDecimal skuPrice;

    // 上下架状态：1.为上架；0.为下架
    @Field(type = FieldType.Integer)
    private Integer showStatus;

    @Field(type = FieldType.Integer)
    private Integer stockInventory;

    private String skuImg;

    @Field(type = FieldType.Integer)
    private Integer skuSort;

    private String skuName;

    @Field(type = FieldType.Integer)
    private Integer sales;

    /**
     * 品牌信息
     */
    @Transient
    private BrandIndex brand;

    /**
     * 规格列表
     */
    @Transient
    private List<SpecValueIndex> specList;

    /**
     * 图片列表
     */
    @Transient
    private List<SkuImgIndex> skuImgList;

    @Transient
    private ItemIndex item;

}
