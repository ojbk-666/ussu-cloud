package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TodayJxBeanDetailResponseVo {

    private Integer ret;
    private String retmsg;
    private List<Detail> detail;

    @Data
    public class Detail {

        /**
         * 收入喜豆个数 负数表示支出
         */
        private Integer amount;
        /**
         * 收入时间
         */
        private Date createdate;
        /**
         * 收入详情 收入来源 说明
         */
        private String visibleinfo;
    }

}
