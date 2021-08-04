package cn.ussu.modules.ecps.search.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "ecps-item", type = "item")
public class ItemIndex {

    // 商品主键
    @Id
    private Integer itemId;

    private String itemName;

    private String itemNo;

    @Field(type = FieldType.Integer)
    private Integer brandId;

    @Field(type = FieldType.Integer)
    private Integer catId;

    // 商品标签图片id
    private Integer tagImgId;

    // 0否，1是，默认1
    @Field(type = FieldType.Integer)
    private Integer isNew;

    // 0否，1是，默认0
    @Field(type = FieldType.Integer)
    private Integer isHot;

    // 促销语：用于前台显示、关键词搜索
    private String promotion;

    // 上下架状态：1.为上架 0.为下架
    @Field(type = FieldType.Integer)
    private Integer showStatus;

    @ApiModelProperty(value = "上传图片：最多5张图片的存储地址，用分隔符分开。可增加上传图片，具备删除和设置排序功能，最多只能上传五张图片，图片上传后共保存5个尺寸（原始尺寸，缩放生成4个尺寸 50x50 、 100x100 、 150x150、300x300），各尺寸提供前台页面调用。	            每张图片上传之后的图片重命名规则为：上传用户ID+日期时间（精确到秒）+6位英文数字随机码,  根据图片格式存放在不同的文件夹，方便未来扩展。	            每张图片大小遵循“系统设置”对附件上传大小的限制。一期默认图片格式支持jpg、gif、png(3个格式不区分大小写)，大小不超过3MB，水印功能一期不开发，图片裁剪功能一期不开发")
    private String imgs;

    // 页面关键词
    private String keywords;

    // 页面描述：文本区
    private String pageDesc;

    // 是否删除：0：未删除；1：已经删除；默认值：0
    @Field(type = FieldType.Integer)
    private Integer itemRecycle;

    /**
     * 商品详情
     */
    @Transient
    private ItemClobIndex itemClob;

    /**
     * 属性列表
     */
    @Transient
    private List<ParaValueIndex> paraList;

    @Transient
    private BrandIndex brand;

}
