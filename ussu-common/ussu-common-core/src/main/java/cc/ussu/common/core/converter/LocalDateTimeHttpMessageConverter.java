package cc.ussu.common.core.converter;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 日期格式消息转换
 */
@Component
@ConditionalOnMissingBean(name = "localDateTimeHttpMessageConverter")
public class LocalDateTimeHttpMessageConverter implements Converter<String, LocalDateTime> {

    private static final List<String> formarts = new ArrayList<>(4);
    static{
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd hh:mm");
        formarts.add("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public LocalDateTime convert(String source) {
        if (source == null) {
            return null;
        }
        if (StrUtil.isBlank(source.trim())) {
            return null;
        }
        if(source.matches("^\\d{4}-\\d{1,2}$")){
            return LocalDateTimeUtil.parse(source, formarts.get(0));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return LocalDateTimeUtil.parse(source, formarts.get(1));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
            return LocalDateTimeUtil.parse(source, formarts.get(2));
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return LocalDateTimeUtil.parse(source, formarts.get(3));
        }else {
            throw new IllegalArgumentException("LocalDateTime 转换异常:'" + source + "'");
        }
    }

}
