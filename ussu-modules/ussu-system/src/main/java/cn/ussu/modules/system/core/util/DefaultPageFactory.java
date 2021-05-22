package cn.ussu.modules.system.core.util;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.common.core.entity.UssuPageFactory;
import cn.ussu.common.core.entity.UssuPageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
     * 创建分页返回对象
     */
    public static ReturnPageInfo createReturnPageInfo(IPage iPage) {
        UssuPageInfo ussuPageInfo = new UssuPageInfo()
                .setCurrent(iPage.getCurrent())
                .setSize(iPage.getSize())
                .setPages(iPage.getPages())
                .setTotal(iPage.getTotal());
        return new ReturnPageInfo()
                .setRecords(iPage.getRecords())
                .setPageInfo(ussuPageInfo);
    }

}
