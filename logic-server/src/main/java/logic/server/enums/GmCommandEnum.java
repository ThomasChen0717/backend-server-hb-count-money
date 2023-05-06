package logic.server.enums;

import lombok.Getter;

@Getter
public enum GmCommandEnum {
    addMoney(1, "加钱"),
    unlockEquipment(2, "装备全解锁"),
    unlockVehicle(3, "载具全解锁"),
    unlockMagnate(4, "富豪全解锁"),
    unlockBoss(5, "boss全解锁"),
    attributeLevelUp(6, "属性升级"),
    ;

    private final int gmCommandId;
    private final String remark;

    GmCommandEnum(int gmCommandId, String remark) {
        this.gmCommandId = gmCommandId;
        this.remark = remark;
    }
}
