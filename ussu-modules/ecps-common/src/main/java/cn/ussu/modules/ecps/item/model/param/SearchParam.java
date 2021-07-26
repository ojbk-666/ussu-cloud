package cn.ussu.modules.ecps.item.model.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SearchParam implements Serializable {

    private static final long serialVersionUID = -4074904702259567655L;

    private Integer catId;
    private Integer brandId;
    private String price;
    private String paras;
    private String keywords;
    private Integer hasStore;
    private String sort;
    private Integer pageNum;

}