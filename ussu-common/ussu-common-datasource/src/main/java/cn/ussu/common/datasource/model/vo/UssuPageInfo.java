package cn.ussu.common.datasource.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页信息
 */
@Data
@Accessors(chain = true)
public class UssuPageInfo implements Serializable {

    private static final long serialVersionUID = -7115452556870193734L;

    // 当前是第几页
    private long current;
    // 分页大小啊
    private long size;
    // 总页数
    private long pages;
    // 总记录数
    private long total;

}
