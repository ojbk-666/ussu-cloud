package cc.ussu.modules.ecps.common.constants;

/**
 * 订单状态
 */
public enum OrderStatus {

    NOT_PAID(0, "待支付"),
    PAID(1, "待发货"),
    SHIPPED(2, "已发货"),
    FINISH(3, "已完成"),
    CLOSE(7, "已取消"),
    ;

    private Integer code;
    private String status;

    OrderStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static String getStatusByCode(Integer code) {
        for (OrderStatus value : OrderStatus.values()) {
            if (value.getCode().equals(code)) {
                return value.getStatus();
            }
        }
        return null;
    }

}
