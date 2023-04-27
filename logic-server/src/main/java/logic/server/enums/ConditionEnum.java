package logic.server.enums;

import lombok.Getter;

@Getter
public enum ConditionEnum {
    ad(0, "广告"),
    money(1, "金钱"),
    ;

    private final int conditionType;
    private final String remark;

    ConditionEnum(int conditionType, String remark) {
        this.conditionType = conditionType;
        this.remark = remark;
    }
}