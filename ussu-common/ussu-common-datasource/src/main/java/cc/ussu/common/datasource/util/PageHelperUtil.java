package cc.ussu.common.datasource.util;

import cc.ussu.common.core.vo.JsonResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PageHelperUtil {

    public static void startPage() {
        PageHelper.startPage(MybatisPlusUtil.getPageNo().intValue(), MybatisPlusUtil.getPageSize().intValue());
    }

    public static JsonResult getResult(List<?> list) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("count", total);
        result.put("list", pageInfo.getList());
        return JsonResult.ok(result);
    }
}
