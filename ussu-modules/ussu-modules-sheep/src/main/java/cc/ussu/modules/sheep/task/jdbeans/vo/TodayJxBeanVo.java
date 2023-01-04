package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 今日喜豆收入vo封装
 */
@Data
@Accessors(chain = true)
public class TodayJxBeanVo {

    /**
     * 收入
     */
    private BigDecimal income;
    /**
     * 支出
     */
    private BigDecimal out;

    /**
     * 昨日收入
     */
    private BigDecimal incomeYesterday;

    /**
     * 昨日支出
     */
    private BigDecimal outYesterday;

}
