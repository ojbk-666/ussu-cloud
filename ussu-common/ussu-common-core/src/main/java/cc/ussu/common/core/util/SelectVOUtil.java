package cc.ussu.common.core.util;

import cc.ussu.common.core.function.FieldFunction;
import cc.ussu.common.core.vo.SelectVO;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class SelectVOUtil {

    /**
     * 将指定bean转为selectvo
     */
    public static List<SelectVO> render(Collection<?> collection, String labelKey, String valueKey) {
        LinkedList<SelectVO> selectVOS = new LinkedList<>();
        for (Object o : collection) {
            Object label = ReflectUtil.getFieldValue(o, labelKey);
            Object value = ReflectUtil.getFieldValue(o, valueKey);
            if (label != null && value != null) {
                selectVOS.add(new SelectVO().setLabel(((String) label)).setValue(value));
            }
        }
        return selectVOS;
    }

    public static <T> List<SelectVO> render(Collection<?> collection, FieldFunction<T, ?> label, FieldFunction<T, ?> value) {
        String l = FieldFunctionUtil.getFieldName(label);
        String v = FieldFunctionUtil.getFieldName(value);
        if (StrUtil.isAllNotBlank(l, v)) {
            return render(collection, l, v);
        }
        return null;
    }

}
