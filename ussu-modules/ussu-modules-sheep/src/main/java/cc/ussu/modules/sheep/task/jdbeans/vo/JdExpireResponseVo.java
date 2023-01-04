package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 最近7天过期京豆
 */
@Data
public class JdExpireResponseVo {

    private Integer ret;
    private String errmsg;
    private List<Expirejingdou> expirejingdou;

    @Data
    public class Expirejingdou {
        private Integer expireamount;
        /**
         * 时间单位是秒
         */
        private Long time;

        public void setTime(Long time) {
            this.time = time;
            if (time != null) {
                this.timeDate = new Date(time * 1000);
            }
        }

        /**
         * 时间 日期类型
         */
        private Date timeDate;
    }

}
