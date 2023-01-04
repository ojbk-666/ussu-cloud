package cc.ussu.modules.sheep.task.bhxcy.constants;

import lombok.Getter;

/**
 * 提现类型枚举
 */
@Getter
public enum RechargeTypeEnum {

    /**
     * 10元E卡
     */
    E_CARD_10("10", "2", "1"),
    /**
     * 20元E卡
     */
    E_CARD_20("20", "2", "2"),
    /**
     * 50元E卡
     */
    E_CARD_50("30", "2", "3"),
    ;

    private String type;
    private String category;
    private String jine;

    RechargeTypeEnum(String type, String category, String jine) {
        this.type = type;
        this.category = category;
        this.jine = jine;
    }

}
