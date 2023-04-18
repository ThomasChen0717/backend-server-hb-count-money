package common.pb.enums;

import lombok.Getter;

@Getter
public enum LoginPlatformEnum {
    dy(1, "dy","抖音");

    private final int id;
    private final String name;
    private final String remark;

    LoginPlatformEnum(int id, String name,String remark) {
        this.id = id;
        this.name = name;
        this.remark = remark;
    }
}
