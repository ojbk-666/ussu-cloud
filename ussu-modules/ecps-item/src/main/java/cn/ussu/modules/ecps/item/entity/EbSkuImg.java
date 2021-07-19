package cn.ussu.modules.ecps.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EbSkuImg对象", description="")
public class EbSkuImg extends Model<EbSkuImg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "img_id", type = IdType.AUTO)
    private Integer imgId;

    private Integer skuId;

    private String imgUrl;

    private Integer defaultImg;

}
