package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 京豆收入详情详情
 */
@Data
public class JdDetailResponseVo {

    private String code;
    private List<DetailList> detailList;
    private List<DetailList> jingDetailList;

    @Data
    public class DetailList {
        /**
         * 京豆获取时间
         */
        private Date date;
        /**
         * 收入数量 可能为负
         */
        private Integer amount;
        /**
         * 收入来源
         */
        private String eventMassage;
    }

}
