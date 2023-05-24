package logic.server.enums;

import lombok.Getter;

@Getter
public enum StoneEnum {
    grey(0, "灰（破）"),
    yellow(1, "黄（豆）"),
    green(2, "绿（玻璃）"),
    ;

    private final int stoneType;
    private final String stoneName;

    StoneEnum(int stoneType, String stoneName) {
        this.stoneType = stoneType;
        this.stoneName = stoneName;
    }
}
