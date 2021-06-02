package cn.ussu.common.datasource.util;

import cn.hutool.core.bean.BeanUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.datasource.model.vo.UssuPageFactory;
import cn.ussu.common.datasource.model.vo.UssuPageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认的分页工具
 */
public class DefaultPageFactory implements UssuPageFactory {

    @Override
    public Page getRequestPage() {
        return new Page(getPageNum(), getPageSize());
    }

    /**
     * 获取请求分页信息
     */
    public static Page getPage() {
        return new DefaultPageFactory().getRequestPage();
    }

    /**
     * 获取分页结果对象
     */
    protected static UssuPageInfo getPageInfo(IPage iPage) {
        return new UssuPageInfo()
                .setCurrent(iPage.getCurrent())
                .setSize(iPage.getSize())
                .setPages(iPage.getPages())
                .setTotal(iPage.getTotal());
    }

    /**
     * 创建分页返回对象
     */
    public static ReturnPageInfo createReturnPageInfo(IPage iPage) {
        return new ReturnPageInfo()
                .setRecords(iPage.getRecords())
                .setPageInfo(getPageInfo(iPage));
    }

    /**
     * 创建分页对象，替换list
     */
    public static ReturnPageInfo createReturnPageInfo(IPage iPage, List rewriteRecords) {
        IPage t = iPage.setRecords(rewriteRecords);
        return createReturnPageInfo(t);
    }

    /**
     * 转换为result对象
     */
    public static <R> List<R> convertToResult(List<?> list, Class<R> r) {
        return list.stream().map(item -> BeanUtil.toBean(item, r)).collect(Collectors.toList());
    }

}
