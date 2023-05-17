package logic.server.enums;

import lombok.Getter;

@Getter
public enum AttributeEnum {
    strengthLevel(1, "力量等级"),
    physicalLevel(2, "体力等级"),
    physicalRestoreLevel(3, "体力恢复等级"),
    enduranceLevel(4, "耐力等级"),
    petLevel(5, "宠物等级"),

    incomeMultiple(6, "收益倍数"),
    strength(7, "力量"),
    physical(8, "体力"),
    physicalRestore(9, "体力恢复"),
    endurance(10, "耐力"),
    watermelonStallDuration(11, "瓜摊生效时长"),
    watermelonStallEffect(12, "瓜摊加成效果"),
    beautyDuration(13, "探班美女生效时长"),
    beautyEffect(14, "探班美女加成效果"),
    autoCountMoneyDuration(15, "自动数钱生效时长"),
    autoCountMoneyEffect(16, "自动数钱加成效果"),
    petSettlementMoney(17, "宠物结算金额"),
    ;

    private final int attributeType;
    private final String attributeName;

    AttributeEnum(int attributeType, String attributeName) {
        this.attributeType = attributeType;
        this.attributeName = attributeName;
    }
}
