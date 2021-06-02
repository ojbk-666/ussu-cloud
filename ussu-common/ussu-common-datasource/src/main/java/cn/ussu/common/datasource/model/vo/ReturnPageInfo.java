package cn.ussu.common.datasource.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回信息
 */
@Data
@Accessors(chain = true)
public class ReturnPageInfo<T> implements Serializable {

    private static final long serialVersionUID = 3200139621708338009L;

    /**
     * 分页结果信息
     */
    private UssuPageInfo pageInfo;
    /**
     * 当前页记录
     */
    private List<T> records;

}
