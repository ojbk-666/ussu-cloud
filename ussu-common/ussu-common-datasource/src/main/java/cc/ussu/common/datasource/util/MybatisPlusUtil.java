package cc.ussu.common.datasource.util;

import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.vo.JsonResult;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MybatisPlusUtil {

    public static final String[] PAGE_NO_KEY = new String[]{"page", "pageNo", "pageNum", "current"};
    public static final String[] PAGE_SIZE_KEY = new String[]{"limit", "pageSize", "size"};

    public static final String TOTAL = "total";
    public static final String COUNT = "count";
    public static final String LIST = "list";

    public static Long getPageNo() {
        HttpServletRequest request = HttpContextHolder.getRequest();
        String pageNo = null;
        for (String pnk : PAGE_NO_KEY) {
            pageNo = request.getParameter(pnk);
            if (StrUtil.isNotBlank(pageNo)) {
                break;
            }
        }
        pageNo = StrUtil.blankToDefault(pageNo, "1");
        long pn = NumberUtil.parseLong(pageNo);
        if (pn < 1) {
            pn = 1;
        }
        return pn;
    }

    public static Long getPageSize() {
        HttpServletRequest request = HttpContextHolder.getRequest();
        String pageSize = null;
        for (String psk : PAGE_SIZE_KEY) {
            pageSize = request.getParameter(psk);
            if (StrUtil.isNotBlank(pageSize)) {
                break;
            }
        }
        pageSize = StrUtil.blankToDefault(pageSize, "10");
        long ps = NumberUtil.parseLong(pageSize);
        if (ps < 1) {
            ps = 10;
        }
        return ps;
    }

    /**
     * 获取分页信息
     */
    public static Page getPage() {
        return new Page(getPageNo(), getPageSize());
    }

    public static <T> JsonResult getResult(IPage<T> iPage) {
        Map<String, Object> m = new HashMap<>();
        m.put("total", iPage.getTotal());
        m.put("list", iPage.getRecords());
        return JsonResult.ok(m);
    }

    public static <T> JsonResult getResult(long total, List<T> list) {
        return getResult(new Page<T>().setTotal(total).setRecords(list));
    }
}
