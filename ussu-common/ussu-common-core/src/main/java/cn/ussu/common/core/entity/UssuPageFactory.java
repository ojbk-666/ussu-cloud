package cn.ussu.common.core.entity;

import cn.hutool.core.map.MapUtil;
import cn.ussu.common.core.util.HttpContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 获取开始页
 */
@Deprecated
public interface UssuPageFactory {

    /**
     * 分页页码key
     */
    default String getPageNumInRequestKey() {
        return "pageNum";
    }

    /**
     * 分页大小key
     */
    default String getPageSizeInRequestKey() {
        return "pageSize";
    }

    /**
     * 获取请求页码
    */
    default long getPageNum(Map param) {
        return MapUtil.getLong(param, this.getPageNumInRequestKey(), 1L);
    }

    /**
     * 获取请求页码
     */
    default long getPageNum(HttpServletRequest request) {
        String pageNum = request.getParameter(getPageNumInRequestKey());
        try {
            return Long.parseUnsignedLong(pageNum);
        } catch (NumberFormatException ex) {
            return 1l;
        }
    }

    /**
     * 获取请求页码
     */
    default long getPageNum() {
        return getPageNum(HttpContext.getRequest());
    }

    /**
     * 从参数获取分页大小
     */
    default long getPageSize(Map param) {
        return MapUtil.getLong(param, this.getPageSizeInRequestKey(), 10L);
    }

    default long getPageSize(HttpServletRequest request) {
        String pageSize = request.getParameter(getPageSizeInRequestKey());
        try {
            return Long.parseUnsignedLong(pageSize);
        } catch (NumberFormatException ex) {
            return 10l;
        }
    }

    default long getPageSize() {
        return getPageSize(HttpContext.getRequest());
    }

    /**
     * 获取分页信息
     *
     * @param <T>
     * @return
     */
    <T> T getRequestPage();

}
