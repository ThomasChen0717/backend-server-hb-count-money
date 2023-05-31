package logic.server.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    user(1, "主角"),
    pet(2, "宠物"),
    vehicleNew(3, "载具（新）"),
    ;

    private final int roleType;
    private final String roleName;

    RoleEnum(int roleType, String roleName) {
        this.roleType = roleType;
        this.roleName = roleName;
    }
}
