package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 今日京豆收入vo封装
 */
@Data
@Accessors(chain = true)
public class TodayBeanVO {

    /**
     * 收入
     */
    private BigDecimal income;

    /**
     * 收入明细
     */
    private List<JdDetailResponseVo.DetailList> incomeList;

    /**
     * 支出
     */
    private BigDecimal out;

    /**
     * 支出明细
     */
    private List<JdDetailResponseVo.DetailList> outList;

    /**
     * 昨日收入
     */
    private BigDecimal incomeYesterday;

    /**
     * 昨日支出
     */
    private BigDecimal outYesterday;

    /**
     * 今日收入明细
     */
    private List<JdDetailResponseVo.DetailList> todayDetailList;

    /**
     * 昨日收入明细
     */
    private List<JdDetailResponseVo.DetailList> yesterdayDetailList;

}
