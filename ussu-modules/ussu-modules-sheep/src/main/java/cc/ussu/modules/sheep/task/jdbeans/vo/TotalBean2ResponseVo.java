package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 总京豆响应
 */
@Data
@Accessors(chain = true)
public class TotalBean2ResponseVo {

    private String code;
    private String message;
    private String sid;
    private String desPin;
    private String userFlagCheck;
    private TotalBean2UserVo user;

    public boolean isOk() {
        return "0".equals(code);
    }

    @Data
    public class TotalBean2UserVo {
        /**
         * 邮箱
         */
        private String email;
        /**
         * 头像
         */
        private String imgUrl;
        /**
         * 请求ip
         */
        private String ipAddress;
        /**
         * 京豆
         */
        private String jingBean;
        private String lastTime;
        /**
         * 昵称
         */
        private String petName;
        private String qq;
        /**
         * 注册ip
         */
        private String regIp;
        /**
         * 注册时间
         */
        private String regTime;
        /**
         * 京享值522
         */
        private String uclass;
        /**
         * pin
         */
        private String unickName;
        private Integer sex;
    }

}
