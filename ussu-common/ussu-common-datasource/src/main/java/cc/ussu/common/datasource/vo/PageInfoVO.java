package cc.ussu.common.datasource.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageInfoVO<T> implements Serializable {

    /**
     * 总记录数
     */
    protected Long total;

    /**
     * 数据
     */
    protected List<T> list;

    public static <T> PageInfoVO<T> build(IPage<T> iPage) {
        return new PageInfoVO<T>().setTotal(iPage.getTotal()).setList(iPage.getRecords());
    }

}
