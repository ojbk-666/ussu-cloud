package cc.ussu.modules.sheep.task.bhxcy.vo;

import lombok.Data;

@Data
public class GetUserInfoResponse extends BhxcyBaseResponse {

    /**
     * 手机号
     */
    private String MobilePhone;
    /**
     * 邀请码
     */
    private String invCode;

    /**
     *
     */
    private Integer receive;
    /**
     * 签到余额
     */
    private String PhoneBill;

}
