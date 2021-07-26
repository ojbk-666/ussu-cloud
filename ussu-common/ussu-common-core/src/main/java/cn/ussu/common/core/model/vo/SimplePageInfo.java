package cn.ussu.common.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 简单分页信息
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SimplePageInfo implements Serializable {

    private static final long serialVersionUID = 1207030339036256665L;

    /**
     * 当前是第几页
     */
    private long current;
    /**
     * 分页大小
     */
    private long size;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 总记录数
     */
    private long total;

}
