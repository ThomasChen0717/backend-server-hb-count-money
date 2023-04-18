package logic.server.enums;

import lombok.Getter;

@Getter
public enum AttributeEnum {
    strengthLevel(1, "力量等级"),
    physicalLevel(2, "体力等级"),
    physicalRestoreLevel(3, "体力恢复等级"),
    enduranceLevel(4, "耐力等级"),
    petLevel(5, "宠物等级")
    ;

    private final int attributeType;
    private final String attributeName;

    AttributeEnum(int attributeType, String attributeName) {
        this.attributeType = attributeType;
        this.attributeName = attributeName;
    }
}
