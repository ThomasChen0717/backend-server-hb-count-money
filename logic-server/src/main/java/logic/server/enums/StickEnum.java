package logic.server.enums;

import lombok.Getter;

@Getter
public enum StickEnum {
    ssq(0, "上上签"),
    sjq(1, "上吉签"),
    zjq(2, "中吉签"),
    zpq(3, "中平签"),
    ;

    private final int stickType;
    private final String stickName;

    StickEnum(int stickType, String stickName) {
        this.stickType = stickType;
        this.stickName = stickName;
    }
}
