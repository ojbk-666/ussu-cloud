package cn.ussu.modules.ecps.common.constants;

/**
 * 订单支付状态
 */
public enum OrderPayStatus {

    NOT_PAID(0, "未支付"),
    PAID(1, "已支付"),
    WAIT_REFUND(2, "待退款"),
    REFUND_SUCCESS(3, "退款成功"),
    REFUND_FAILED(4, "退款失败"),
    REVOCATION_SUCCESS(5, "撤销成功"),
    REVOCATION_FAILED(6, "撤销失败"),
    CLOSE(7, "已关闭"),
    ;

    OrderPayStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    private Integer code;
    private String status;

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
