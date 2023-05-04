package common.pb.enums;

import lombok.Getter;

@Getter
public enum LoginPlatformEnum {
    dy(1, "dy","抖音"),
    ty(2, "ty","三方"),
    hb(3, "hb","自平台");

    private final int id;
    private final String name;
    private final String remark;

    LoginPlatformEnum(int id, String name,String remark) {
        this.id = id;
        this.name = name;
        this.remark = remark;
    }
}
