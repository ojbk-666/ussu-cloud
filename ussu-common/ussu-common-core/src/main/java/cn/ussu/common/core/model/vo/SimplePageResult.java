package cn.ussu.common.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 简单分页信息返回
 * @param <T>
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimplePageResult<T> implements Serializable {

    private static final long serialVersionUID = -8794585345464526058L;

    /**
     * 分页结果信息
     */
    private SimplePageInfo pageInfo;
    /**
     * 当前页记录
     */
    private List<T> records;

}
